package com.snow.stonehedge.enigma;

public class Symbol {

    public Book bids;
    public Book asks;
    public String underlier;

    public Symbol(String underlier) {
        this.bids = new Bids();
        this.asks = new Asks();
        this.underlier = underlier;
    }

}
