package com.snow.stonehedge.enigma;

public class CancelOrderRequest {
    long id;
    double price;
    long quantity;

    public CancelOrderRequest(long id, double price, long quantity) {
        id = Data.getId();
        this.price = price;
        this.quantity = quantity;
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

    public void setId(long id) {
        this.id = id;
    }
}
