package com.snow.stonehedge.orders.service;

import com.snow.stonehedge.marketdata.model.Book;
import com.snow.stonehedge.orders.logic.OrdersLogic;
import com.snow.stonehedge.orders.model.*;
import com.snow.stonehedge.shared.Data;

import java.math.BigDecimal;
import java.util.*;

public class OrdersService {

    public long submitOrder(OrderRequest orderRequest) {
        long orderID = Data.GET_ORDER_ID();
        Order newOrder = new Order(orderID, orderRequest);
        Data.ORDER_LIST.add(newOrder);
        Data.QUOTES.get(orderRequest.getSymbol()).getBook().addOrder(newOrder);
        return orderID;
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
                }
                orderResponseList.add(new OrderResponse(
                    order.getId(),
                    successOrFailure,
                    order.getOrderRequest().getBuyOrSell(),
                    order.getOrderRequest().getSymbol(),
                    order.getOrderRequest().getQuantity(),
                    BigDecimal.valueOf(534.0),
                    fillStatus)
                );
            } else {
                carryOverOrderList.add(order);
            }
            order.getOrderRequest().addOneToTimeLimit();
        }
        Data.ORDER_LIST.clear();
        Data.ORDER_LIST = carryOverOrderList;
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
        TreeMap<Double, Map<Long, Order>> asks = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().getAsks();
        Set<Double> keys = asks.keySet();
        for (double askPrice : keys) {
            for (Order ask : asks.get(askPrice).values()) {
                long askQuantity = ask.getOrderRequest().getQuantity();
                long amountTryingToBeFilled = (order.getOrderRequest().getQuantity() - order.getOrderRequest().getAmountThatHasBeenFilled());
                if (askQuantity >= 0) {
                    if (amountTryingToBeFilled <= askQuantity) {
                        // If the amount requested is less or equal to what can be provided, completely fill the order
                        book.removeOrder(order);
                        book.removeOrder(ask);
                        book.updateOrderFillAmount(order, amountTryingToBeFilled);
                        book.updateOrderFillAmount(ask, amountTryingToBeFilled);
                    } else {
                        // If not, fill what you can
                        book.updateOrderFillAmount(order, askQuantity);
                        book.updateOrderFillAmount(ask, askQuantity);
                    }
                }
                if (order.getOrderRequest().getQuantity() <= order.getOrderRequest().getAmountThatHasBeenFilled()) order.setHasBeenFilled(true);
                if (ask.getOrderRequest().getQuantity() <= ask.getOrderRequest().getAmountThatHasBeenFilled()) ask.setHasBeenFilled(true);
            }
        }
    }

    private void tryToFillSellOrderInBids(Order order) {
        Book book = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook();
        NavigableMap<Double, Map<Long, Order>> bids = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().getBids().descendingMap();
        Set<Double> keys = bids.keySet();
        for (double bidPrice : keys) {
            for (Order bid : bids.get(bidPrice).values()) {
                long bidQuantity = bid.getOrderRequest().getQuantity();
                long amountTryingToBeFilled = (order.getOrderRequest().getQuantity() - order.getOrderRequest().getAmountThatHasBeenFilled());
                if (bidQuantity >= 0) {
                    if (amountTryingToBeFilled <= bidQuantity) {
                        // If the amount requested is less or equal to what can be provided, completely fill the order
                        book.removeOrder(order);
                        book.removeOrder(bid);
                        book.updateOrderFillAmount(order, amountTryingToBeFilled);
                        book.updateOrderFillAmount(bid, amountTryingToBeFilled);
                    } else {
                        // If not, fill what you can
                        book.updateOrderFillAmount(order, bidQuantity);
                        book.updateOrderFillAmount(bid, bidQuantity);
                    }
                }
                if (order.getOrderRequest().getQuantity() <= order.getOrderRequest().getAmountThatHasBeenFilled()) order.setHasBeenFilled(true);
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
