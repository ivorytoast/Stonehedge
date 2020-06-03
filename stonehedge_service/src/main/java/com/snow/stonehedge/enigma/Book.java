package com.snow.stonehedge.enigma;

import java.util.*;
import java.util.stream.Collectors;

public class Book {

    public boolean isBidBook;

    public double bestPrice;
    public double amountAvailable;

    public TreeMap<Double, Long> availablePerPrice;
    public Map<Double, List<Order>> ordersPerPrice;
    public List<Order> completedOrders;

    public Book(boolean isBidBook) {
        this.isBidBook = isBidBook;
        if (isBidBook) {
            this.bestPrice = 0.0;
            this.availablePerPrice = new TreeMap<>(Collections.reverseOrder());
        } else {
            this.bestPrice = Double.MAX_VALUE;
            this.availablePerPrice = new TreeMap<>();
        }
        this.amountAvailable = 0.0;
        this.ordersPerPrice = new HashMap<>();
        this.completedOrders = new ArrayList<>();
    }

    public void processOrder(Order order) {
        addOrder(order);
        increaseAmountAvailable(order);
        updateBestPrice(order);
    }

    public void removeOrder(Order order) {
        Iterator<Order> iterator = ordersPerPrice.get(order.price).iterator();
        while (iterator.hasNext()) {
            Order orderValue = iterator.next();
            if (orderValue.getId() == order.getId()) {
                iterator.remove();
                completedOrders.add(orderValue);
                adjustAffectedValuesWhenRemovingOrder(order);
                return;
            }
        }
    }

    public void matchOrder(Order incomingOrder) {
        List<Double> prices = findTheBestPricesToFillWith(incomingOrder);
        for (double price : prices) {
            Order existingOrder = ordersPerPrice.get(price).get(0);
            if (incomingOrder.quantity == 0) return;
            if (incomingOrder.quantity <= existingOrder.quantity) {
                adjustAvailablePerPrice(existingOrder.price, (incomingOrder.quantity * -1));
                amountAvailable -= incomingOrder.quantity;
                existingOrder.quantity -= incomingOrder.quantity;
                incomingOrder.quantity = 0;
            } else {
                incomingOrder.quantity -= existingOrder.quantity;
                removeOrder(existingOrder);
            }
        }

    }

    private List<Double> findTheBestPricesToFillWith(Order order) {
        if (amountAvailable < order.originalQuantity) {
            return new ArrayList<>();
        } else {
            long toBeFilled = order.quantity;
            List<Double> prices = new ArrayList<>();
            for(Map.Entry<Double, Long> entry : availablePerPrice.entrySet()) {
                if (toBeFilled == 0) return prices;
                if (entry.getValue() > 0) {
                    if (toBeFilled <= entry.getValue()) {
                        toBeFilled = 0;
                    } else {
                        toBeFilled -= entry.getValue();
                    }
                    prices.add(entry.getKey());
                }
            }
            return prices;
        }
    }

    private void adjustAffectedValuesWhenRemovingOrder(Order order) {
        adjustAvailablePerPrice(order.price, (order.originalQuantity * -1));
        amountAvailable -= order.originalQuantity;
        updateBestPrice();
    }

    private void addOrder(Order order) {
        adjustAvailablePerPrice(order.price, order.quantity);
        if (ordersPerPrice.containsKey(order.price)) {
            List<Order> values = ordersPerPrice.get(order.price);
            values.add(order);
            ordersPerPrice.put(order.price, values);
        } else {
            ordersPerPrice.put(order.price, new ArrayList<>(Collections.singletonList(order)));
        }
    }

    private void adjustAvailablePerPrice(double price, long amount) {
        if (availablePerPrice.containsKey(price)) {
            availablePerPrice.put(price, availablePerPrice.get(price) + amount);
        } else {
            availablePerPrice.put(price, amount);
        }
    }

    private void increaseAmountAvailable(Order order) {
        amountAvailable += order.quantity;
    }

    private void updateBestPrice(Order order) {
        if (isBidBook) {
            if (order.price > bestPrice) bestPrice = order.price;
        } else {
            if (order.price < bestPrice) bestPrice = order.price;
        }

    }

    private void updateBestPrice() {
        double bestBidPrice;
        List<Order> bidsToTest = ordersPerPrice.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
        if (isBidBook) {
            bestBidPrice = 0.0;
            for (Order value : bidsToTest) {
                if (value.quantity > 0 && value.price > bestBidPrice) bestBidPrice = value.price;
            }
        } else {
            bestBidPrice = Double.MAX_VALUE;
            for (Order value : bidsToTest) {
                if (value.quantity > 0 && value.price < bestBidPrice) bestBidPrice = value.price;
            }
        }
        bestPrice = bestBidPrice;
    }

}
