package com.snow.stonehedge.enigma;

public class Symbol {

    public Book bids;
    public Book asks;
    public String underlier;

    public Symbol(String underlier, double startingBestPrice) {
        this.bids = new Bids(startingBestPrice);
        this.asks = new Asks(startingBestPrice);
        this.underlier = underlier;
    }

}
