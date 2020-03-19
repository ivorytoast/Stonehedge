package com.snow.stonehedge.marketdata.model;

import com.snow.stonehedge.orders.model.BuyOrSell;
import com.snow.stonehedge.orders.model.Order;
import com.snow.stonehedge.orders.model.OrderRequest;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class Book {

    private TreeMap<Double, Map<Long, Order>> bids;
    private TreeMap<Double, Map<Long, Order>> asks;
    private OrderRequest mostRecentTrade;
    private long totalNumberOfBids;
    private long totalNumberOfAsks;

    public Book() {
        bids = new TreeMap<>();
        asks = new TreeMap<>();
        mostRecentTrade = new OrderRequest();
    }

    public void addOrder(Order order) {
        double price = order.getOrderRequest().getPrice();
        if (order.getOrderRequest().getBuyOrSell() == BuyOrSell.BUY) {
            if (this.bids.containsKey(price)) {
                Map<Long, Order> values = this.bids.get(price);
                values.put(order.getId(), order);
                this.bids.put(price, values);
            } else {
                Map<Long, Order> values = new HashMap<>();
                values.put(order.getId(), order);
                this.bids.put(price, values);
            }
            this.totalNumberOfBids += order.getOrderRequest().getQuantity();
        } else {
            if (this.asks.containsKey(price)) {
                Map<Long, Order> values = this.asks.get(price);
                values.put(order.getId(), order);
                this.asks.put(price, values);
            } else {
                Map<Long, Order> values = new HashMap<>();
                values.put(order.getId(), order);
                this.asks.put(price, values);
            }
            this.totalNumberOfAsks += order.getOrderRequest().getQuantity();
        }
    }

    public void updateOrderFillAmount(Order order, long amount) {
        order.getOrderRequest().updateAmountThatHasBeenFilled(amount);
        if (order.getOrderRequest().getBuyOrSell() == BuyOrSell.BUY) {
            this.totalNumberOfBids -= amount;
        } else {
            this.totalNumberOfAsks -= amount;
        }
    }

    public void removeOrder(Order order) {
        double price = order.getOrderRequest().getPrice();
        long orderID = order.getId();
        if (order.getOrderRequest().getBuyOrSell() == BuyOrSell.BUY) {
            Map<Long, Order> values = this.bids.get(price);
            values.remove(orderID);
        } else {
            Map<Long, Order> values = this.asks.get(price);
            values.remove(orderID);
        }
    }

}
