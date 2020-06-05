package com.snow.stonehedge.enigma;

import java.util.Objects;

public class Order {
    long id = 0;
    double price;
    long quantity;
    long originalQuantity;

    public Order(double price, long quantity) {
        id = Data.getId();
        this.price = price;
        this.quantity = quantity;
        this.originalQuantity = quantity;
    }

    public Order(long id, double price, long quantity, long originalQuantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.originalQuantity = originalQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getId() {
        return this.id;
    }

    public long getOriginalQuantity() {
        return this.originalQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id &&
            price == order.price &&
            quantity == order.quantity &&
            originalQuantity == order.originalQuantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, quantity, originalQuantity);
    }
}
