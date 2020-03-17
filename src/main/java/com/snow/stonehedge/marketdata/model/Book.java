package com.snow.stonehedge.marketdata.model;

import lombok.*;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Getter
@Setter
@AllArgsConstructor
public class Book {

    private TreeMap<Double, Long> bids;
    private TreeMap<Double, Long> asks;
    private long totalNumberOfBids;
    private long totalNumberOfAsks;
    private double currentPrice;

    public Book() {
        bids = new TreeMap<>();
        asks = new TreeMap<>();
        updateCurrentPrice();
    }

    public void updateCurrentPrice() {
        Optional<Double> bestAsk = asks.entrySet().stream()
                .filter(ask -> ask.getValue() != 0)
                .findFirst()
                .map(Map.Entry::getKey);
        Optional<Double> bestBid = bids.descendingMap().entrySet().stream()
                .filter(bid -> bid.getValue() != 0)
                .findFirst()
                .map(Map.Entry::getKey);
        if (bestAsk.isEmpty()) return;
        if (bestBid.isEmpty()) return;
        currentPrice = (bestAsk.get() + bestBid.get()) / 2.0;
    }

    public void increaseBids(double price, long increaseBy) {
        if (bids.containsKey(price)) {
            bids.put(price, bids.get(price) + increaseBy);
        } else {
            bids.put(price, increaseBy);
        }
        totalNumberOfBids += increaseBy;
        updateCurrentPrice();
    }

    public void increaseAsks(double price, long increaseBy) {
        if (asks.containsKey(price)) {
            asks.put(price, asks.get(price) + increaseBy);
        } else {
            asks.put(price, increaseBy);
        }
        totalNumberOfAsks += increaseBy;
        updateCurrentPrice();
    }

    @SneakyThrows
    public void decreaseBids(double price, long decreaseBy) {
        if (bids.containsKey(price)) {
            bids.put(price, bids.get(price) - decreaseBy);
            totalNumberOfBids -= decreaseBy;
            updateCurrentPrice();
        } else {
            throw new Exception("Can't remove something that is not there!");
        }
    }

    @SneakyThrows
    public void decreaseBidsToZero(double price) {
        if (bids.containsKey(price)) {
            totalNumberOfBids -= bids.get(price);
            bids.put(price, 0L);
            updateCurrentPrice();
        } else {
            throw new Exception("Can't remove something that is not there!");
        }
    }

    @SneakyThrows
    public void decreaseAsks(double price, long decreaseBy) {
        if (asks.containsKey(price)) {
            asks.put(price, asks.get(price) - decreaseBy);
            totalNumberOfAsks -= decreaseBy;
            updateCurrentPrice();
        } else {
            throw new Exception("Can't remove something that is not there!");
        }
    }

    @SneakyThrows
    public void decreaseAsksToZero(double price) {
        if (asks.containsKey(price)) {
            totalNumberOfAsks -= asks.get(price);
            asks.put(price, 0L);
            updateCurrentPrice();
        } else {
            throw new Exception("Can't remove something that is not there!");
        }
    }

}
