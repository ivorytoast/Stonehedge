package com.snow.stonehedge.enigma;

public class Symbol {

    public Book bids;
    public Book asks;
    public String underlier;
    public double lastTradePrice;

    public Symbol(String underlier, double startPrice) {
        this.bids = new Bids();
        this.asks = new Asks();
        this.underlier = underlier;
        this.lastTradePrice = startPrice;
    }

    public void updateLastTradePrice(Book book) {
        if (book.completedOrders.isEmpty()) return;
        int size = book.completedOrders.size() - 1;
        lastTradePrice = book.completedOrders.get(size).price;
    }

}
