package com.snow.stonehedge.orders.service;

import com.snow.stonehedge.orders.logic.OrdersLogic;
import com.snow.stonehedge.orders.model.*;
import com.snow.stonehedge.shared.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrdersService {

    OrdersLogic ordersLogic = new OrdersLogic();

    public long submitOrder(OrderRequest orderRequest) {
        long orderId = Data.GET_ORDER_ID();
        Data.ORDER_LIST.add(new Order(orderId, orderRequest));
        return orderId;
    }

    /*
    Eventually send out an email for each of the orders to confirm that it has either been filled or not.
     */
    public List<OrderResponse> processOrders() {
        List<OrderResponse> orderResponseList = new ArrayList<>();
        Data.ORDER_LIST.forEach(order -> {
            BigDecimal fillPrice = calculateFillPrice(order);
            orderResponseList.add(new OrderResponse(
                order.getId(),
                fillPrice.equals(BigDecimal.valueOf(-1)) ? SuccessOrFailure.FAILURE : SuccessOrFailure.SUCCESS,
                order.getOrderRequest().getBuyOrSell(),
                order.getOrderRequest().getSymbol(),
                order.getOrderRequest().getQuantity(),
                fillPrice)
            );
        });
        return orderResponseList;
    }

    private BigDecimal calculateFillPrice(Order order) {
        return ordersLogic.routeOrder(order);
    }

}
