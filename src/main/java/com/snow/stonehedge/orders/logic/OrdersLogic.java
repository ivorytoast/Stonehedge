package com.snow.stonehedge.orders.logic;

import com.snow.stonehedge.marketdata.model.Book;
import com.snow.stonehedge.orders.model.BuyOrSell;
import com.snow.stonehedge.orders.model.Order;
import com.snow.stonehedge.shared.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

public class OrdersLogic {

    public BigDecimal routeOrder(Order order) {
        if (order.getOrderRequest().getBuyOrSell() == BuyOrSell.BUY) {
            if (Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().getTotalNumberOfAsks() < order.getOrderRequest().getQuantity()) return BigDecimal.valueOf(-1);
            Set<Map.Entry<Double, Long>> entrySet = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().getAsks().entrySet();
            return processOrder(order, entrySet, Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook());
        } else {
            if (Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().getTotalNumberOfBids() < order.getOrderRequest().getQuantity()) return BigDecimal.valueOf(-1);
            Set<Map.Entry<Double, Long>> entrySet = Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook().getBids().descendingMap().entrySet();
            return processOrder(order, entrySet, Data.QUOTES.get(order.getOrderRequest().getSymbol()).getBook());
        }
    }

    public BigDecimal processOrder(Order order, Set<Map.Entry<Double, Long>> entrySet, Book book) {
        double fillPrice = 0.0;
        long sharesToAction = order.getOrderRequest().getQuantity();
        BuyOrSell buyOrSell = order.getOrderRequest().getBuyOrSell();
        for (Map.Entry<Double, Long> entry : entrySet) {
            if (entry.getValue() >= sharesToAction) {
                if (buyOrSell == BuyOrSell.BUY) {
                    book.decreaseAsks(entry.getKey(), sharesToAction);
                } else {
                    book.decreaseBids(entry.getKey(), sharesToAction);
                }
                fillPrice += (entry.getKey() * sharesToAction);
                return new BigDecimal(fillPrice / order.getOrderRequest().getQuantity()).setScale(2, RoundingMode.HALF_UP);
            } else {
                fillPrice += (entry.getValue() * entry.getKey());
                sharesToAction -= entry.getValue();
                if (buyOrSell == BuyOrSell.BUY) {
                    book.decreaseAsksToZero(entry.getKey());
                } else {
                    book.decreaseBidsToZero(entry.getKey());
                }
            }
        }
        return BigDecimal.valueOf(-1);
    }

}
