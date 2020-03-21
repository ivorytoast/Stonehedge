package com.snow.stonehedge.orders.service;

import com.snow.stonehedge.marketdata.model.Book;
import com.snow.stonehedge.orders.model.*;
import com.snow.stonehedge.shared.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class OrdersService {

    public long submitOrder(OrderRequest orderRequest) {
        long orderID = Data.GET_ORDER_ID();
        Order newOrder = new Order(orderID, orderRequest);
        Data.ORDER_LIST.add(newOrder);
        if (Data.QUOTES.containsKey(orderRequest.getSymbol())) {
            Data.QUOTES.get(orderRequest.getSymbol()).getBook().addOrder(newOrder);
            return orderID;
        } else {
            log.error("Symbol does not exist on the exchange!");
            return -1;
        }
    }

    // TODO -- YOU NEED TO UPDATE MOST RECENT TRADE
    // TODO -- CLARIFY ON EXACTLY WHICH TRADES IT IS MATCHING (FOR EXAMPLE, IS IT MATCHING WITH $3000 -> $2999 -> $2998.0 to buy or $2998.0 -> 2999.0 -> 3000.0)

    public List<OrderResponse> processOrders() {
        /*
        For each order:
            1. Try to fill the order
                1. Update the order fill amounts
                2. Update the bid/ask amounts
            2. Determine if it is a success or failure
                1. If it has gone through 5 cycles with no partial fill, it is a failure
                2. If it is a partial fill AND it has completed the 5 cycles, then it is a SUCCESS
                    3. If it is not full, update to PARTIAL
                3. If during any cycle, it gets fully filled, it is a SUCCESS with a FULL status
            3. After each order gets processed, if any tarde has been bought or sold, that price becomes the current price of the underlier
            4. Remove the order list and re-populate with the existing orders that have not been filled
        */
        List<OrderResponse> orderResponseList = new ArrayList<>();
        Queue<Order> carryOverOrderList = new LinkedList<>();
        for (Order order : Data.ORDER_LIST) {
            Book book = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook();
            attemptToFillOrder(order);
            if (hasOrderFinished(order) || order.isHasBeenFilled()) {
                SuccessOrFailure successOrFailure = order.getOrderRequest().getAmountThatHasBeenFilled() > 0L ? SuccessOrFailure.SUCCESS : SuccessOrFailure.FAILURE;
                FillStatus fillStatus = order.getOrderRequest().getAmountThatHasBeenFilled() == order.getOrderRequest().getQuantity() ? FillStatus.FULL : FillStatus.PARTIAL;
                if (successOrFailure == SuccessOrFailure.FAILURE) {
                    fillStatus = FillStatus.NONE;
                    book.removeOrder(order);
                    book.updateOrderFillAmount(order, order.getOrderRequest().getQuantity());
                    order.getOrderRequest().setFillPrice(0.0);
                }
                orderResponseList.add(new OrderResponse(
                    order.getId(),
                    successOrFailure,
                    order.getOrderRequest().getBuyOrSell(),
                    order.getOrderRequest().getSymbol(),
                    order.getOrderRequest().getQuantity(),
                    BigDecimal.valueOf(order.getOrderRequest().getFillPrice()),
                    fillStatus)
                );
            } else {
                carryOverOrderList.add(order);
            }
            order.getOrderRequest().addOneToTimeLimit();
        }
        Data.ORDER_LIST.clear();
        Data.ORDER_LIST = carryOverOrderList;
        Data.HISTORICAL_ORDERS.addAll(orderResponseList);
        return orderResponseList;
    }

    private void attemptToFillOrder(Order order) {
        if (isBuy(order)) {
            // Go through asks to try to fill
            tryToFillBuyOrderInAsks(order);
        } else {
            // Go through bids to try to fill
            tryToFillSellOrderInBids(order);
        }
    }

    // TODO -- COMBINE THESE INTO ONE METHOD "TRY_TO_FILL_ORDER()"

    private void tryToFillBuyOrderInAsks(Order order) {
        Book book = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook();
        // Get all the orders that could potentially fill this one
        TreeMap<Double, Map<Long, Order>> asks = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().getAsks();
        // Get all the prices that the underlier has that MIGHT have stock
        Set<Double> keys = asks.keySet();
        for (double key : keys) System.out.print(key + ", ");
        for (double askPrice : keys) {
            log.info("The current ask price we are looking at is {}", askPrice);
            // For each price, get each order
            for (Order ask : asks.get(askPrice).values()) {
                // Get the amount that is present
                long askQuantity = ask.getOrderRequest().getQuantity();
                // Get the amount that wants to be filled
                long amountTryingToBeFilled = (order.getOrderRequest().getQuantity() - order.getOrderRequest().getAmountThatHasBeenFilled());
                // If the amount requested is equal to what can be provided, completely fill the order. Remove both positions
                if (amountTryingToBeFilled == askQuantity) {
                    book.removeOrder(order);
                    book.removeOrder(ask);
                    book.updateOrderFillAmount(order, amountTryingToBeFilled);
                    book.updateOrderFillAmount(ask, amountTryingToBeFilled);
                    order.getOrderRequest().addToTotalDollarAmountFilled(askPrice * amountTryingToBeFilled);
                    ask.getOrderRequest().addToTotalDollarAmountFilled(askPrice * amountTryingToBeFilled);
                // If the amount requested is less than what can be provided, completely fill requested order. Remove the requested order. Leave the other order be.
                } else if (amountTryingToBeFilled < askQuantity) {
                    // If not, fill what you can
                    book.removeOrder(order);
                    book.updateOrderFillAmount(order, amountTryingToBeFilled);
                    book.updateOrderFillAmount(ask, amountTryingToBeFilled);
                    order.getOrderRequest().addToTotalDollarAmountFilled(askPrice * amountTryingToBeFilled);
                    ask.getOrderRequest().addToTotalDollarAmountFilled(askPrice * amountTryingToBeFilled);
                // If the amount requested is more than what can be provided, partially fill the requested order. Remove the provided order. Leave the requested order be.
                } else {
                    book.removeOrder(ask);
                    book.updateOrderFillAmount(order, askQuantity);
                    book.updateOrderFillAmount(ask, askQuantity);
                    order.getOrderRequest().addToTotalDollarAmountFilled(askPrice * askQuantity);
                    ask.getOrderRequest().addToTotalDollarAmountFilled(askPrice * askQuantity);
                }
                // If the requested order's filled total is as much as the original quantity, set it to filled
                if (order.getOrderRequest().getQuantity() <= order.getOrderRequest().getAmountThatHasBeenFilled()) order.setHasBeenFilled(true);
                // If the provided order's filled total is as much as the original quantity, set it to filled
                if (ask.getOrderRequest().getQuantity() <= ask.getOrderRequest().getAmountThatHasBeenFilled()) ask.setHasBeenFilled(true);
            }
        }
    }

    private void tryToFillSellOrderInBids(Order order) {
        Book book = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook();
        NavigableMap<Double, Map<Long, Order>> bids = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().getBids().descendingMap();
        Set<Double> keys = bids.keySet();
        for (double bidPrice : keys) {
            log.info("The current bid price we are looking at is {}", bidPrice);
            // For each price, get each order
            for (Order bid : bids.get(bidPrice).values()) {
                // Get the amount that is present
                long bidQuantity = bid.getOrderRequest().getQuantity();
                // Get the amount that wants to be filled
                long amountTryingToBeFilled = (order.getOrderRequest().getQuantity() - order.getOrderRequest().getAmountThatHasBeenFilled());
                // If the amount requested is equal to what can be provided, completely fill the order. Remove both positions
                if (amountTryingToBeFilled == bidQuantity) {
                    book.removeOrder(order);
                    book.removeOrder(bid);
                    book.updateOrderFillAmount(order, amountTryingToBeFilled);
                    book.updateOrderFillAmount(bid, amountTryingToBeFilled);
                    order.getOrderRequest().addToTotalDollarAmountFilled(bidPrice * amountTryingToBeFilled);
                    bid.getOrderRequest().addToTotalDollarAmountFilled(bidPrice * amountTryingToBeFilled);
                    // If the amount requested is less than what can be provided, completely fill requested order. Remove the requested order. Leave the other order be.
                } else if (amountTryingToBeFilled < bidQuantity) {
                    // If not, fill what you can
                    book.removeOrder(order);
                    book.updateOrderFillAmount(order, amountTryingToBeFilled);
                    book.updateOrderFillAmount(bid, amountTryingToBeFilled);
                    order.getOrderRequest().addToTotalDollarAmountFilled(bidPrice * amountTryingToBeFilled);
                    bid.getOrderRequest().addToTotalDollarAmountFilled(bidPrice * amountTryingToBeFilled);
                    // If the amount requested is more than what can be provided, partially fill the requested order. Remove the provided order. Leave the requested order be.
                } else {
                    book.removeOrder(bid);
                    book.updateOrderFillAmount(order, bidQuantity);
                    book.updateOrderFillAmount(bid, bidQuantity);
                    order.getOrderRequest().addToTotalDollarAmountFilled(bidPrice * bidQuantity);
                    bid.getOrderRequest().addToTotalDollarAmountFilled(bidPrice * bidQuantity);
                }
                // If the requested order's filled total is as much as the original quantity, set it to filled
                if (order.getOrderRequest().getQuantity() <= order.getOrderRequest().getAmountThatHasBeenFilled()) order.setHasBeenFilled(true);
                // If the provided order's filled total is as much as the original quantity, set it to filled
                if (bid.getOrderRequest().getQuantity() <= bid.getOrderRequest().getAmountThatHasBeenFilled()) bid.setHasBeenFilled(true);
            }
        }
    }

    private boolean hasOrderFinished(Order order) {
        return order.getOrderRequest().getTimeLimit() >= 5;
    }

    private boolean isBuy(Order order) {
        return order.getOrderRequest().getBuyOrSell() == BuyOrSell.BUY;
    }

}
