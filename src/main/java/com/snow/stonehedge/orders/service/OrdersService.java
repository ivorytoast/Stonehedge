package com.snow.stonehedge.orders.service;

import com.snow.stonehedge.orders.logic.OrdersLogic;
import com.snow.stonehedge.orders.model.*;
import com.snow.stonehedge.shared.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OrdersService {

    OrdersLogic ordersLogic = new OrdersLogic();

    public long submitOrder(OrderRequest orderRequest) {
        long orderId = Data.GET_ORDER_ID();
        Data.ORDER_LIST.add(new Order(orderId, orderRequest));
        if (orderRequest.getBuyOrSell() == BuyOrSell.BUY) {
            Data.QUOTES.get(orderRequest.getSymbol()).getBook().increaseBids(orderRequest.getPrice(), orderRequest.getQuantity());
        } else {
            Data.QUOTES.get(orderRequest.getSymbol()).getBook().increaseAsks(orderRequest.getPrice(), orderRequest.getQuantity());
        }
        return orderId;
    }

    /*
    Eventually send out an email for each of the orders to confirm that it has either been filled or not.
     */
    public List<OrderResponse> processOrders() {
        List<OrderResponse> orderResponseList = new ArrayList<>();
        Queue<Order> newOrderList = new LinkedList<>();
        Data.ORDER_LIST.forEach(order -> {
            BigDecimal fillPrice = calculateFillPrice(order);
            SuccessOrFailure successOrFailure = fillPrice.equals(BigDecimal.valueOf(-1)) ? SuccessOrFailure.FAILURE : SuccessOrFailure.SUCCESS;
            if (order.getOrderRequest().getTimeLimit() >= 5 || successOrFailure == SuccessOrFailure.SUCCESS) {
                orderResponseList.add(new OrderResponse(
                    order.getId(),
                    successOrFailure,
                    order.getOrderRequest().getBuyOrSell(),
                    order.getOrderRequest().getSymbol(),
                    order.getOrderRequest().getQuantity(),
                    fillPrice)
                );
                if (order.getOrderRequest().getTimeLimit() >= 5) {
                    if (order.getOrderRequest().getBuyOrSell() == BuyOrSell.BUY) {
                        Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().decreaseBids(order.getOrderRequest().getPrice(), order.getOrderRequest().getQuantity());
                    } else {
                        Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().decreaseAsks(order.getOrderRequest().getPrice(), order.getOrderRequest().getQuantity());
                    }
                }
            } else {
                newOrderList.add(order);
            }
            order.getOrderRequest().addOneToTimeLimit();
        });
        Data.ORDER_LIST.clear();
        Data.ORDER_LIST = newOrderList;
        return orderResponseList;
    }

    private BigDecimal calculateFillPrice(Order order) {
        return ordersLogic.routeOrder(order);
    }

}
