package com.snow.stonehedge.enigma;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Book {

    public BookType bookType;

    public double bestPrice;
    public double amountAvailable;

    public TreeMap<Double, Long> availablePerPrice;
    public Map<Double, List<Order>> ordersPerPrice;
    public List<Order> completedOrders;

    public Book(BookType bookType) {
        this.bookType = bookType;
        if (bookType == BookType.BID) {
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

    public void removeOrder(Order order, double atPrice) {
        if (ordersPerPrice.containsKey(order.price) && ordersPerPrice.get(order.price).contains(order)) {
            availablePerPrice.put(atPrice, availablePerPrice.get(atPrice) - order.quantity);
            ordersPerPrice.get(atPrice).remove(order);
            amountAvailable -= order.quantity;
            order.quantity = 0;
            completedOrders.add(order);
            updateBestPrice();
        }
    }

    public void removeExistingOrder(Order order) {
        availablePerPrice.put(order.price, availablePerPrice.get(order.price) - order.quantity);
        ordersPerPrice.get(order.price).remove(order);
        amountAvailable -= order.quantity;
        order.quantity = 0;
        completedOrders.add(order);
        updateBestPrice();
    }

    // Order hasn't been added yet. So: take away amount of shares matched, set quantity to 0 and add to completed list.
    public void removeIncomingOrder(Order order) {
        amountAvailable -= order.quantity;
        order.quantity = 0;
        completedOrders.add(order);
        updateBestPrice();
    }

    public boolean matchOrder(Order incomingOrder) {
        List<Double> prices = findTheBestPricesToFillWith(incomingOrder);
        for (double price : prices) {
            Order existingOrder = ordersPerPrice.get(price).get(0);
            if (incomingOrder.quantity == 0) break;
            if (incomingOrder.quantity == existingOrder.quantity) {
                incomingOrder.quantity = 0;
                removeExistingOrder(existingOrder);
                removeIncomingOrder(incomingOrder);
            } else if (incomingOrder.quantity < existingOrder.quantity) {
                existingOrder.quantity -= incomingOrder.quantity;
                removeIncomingOrder(incomingOrder);
            } else {
                incomingOrder.quantity -= existingOrder.quantity;
                removeExistingOrder(existingOrder);
            }
        }
        updateBestPrice();
        return incomingOrder.quantity == 0;
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
        if (bookType == BookType.BID) {
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
        if (bookType == BookType.BID) {
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
