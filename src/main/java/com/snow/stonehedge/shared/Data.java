package com.snow.stonehedge.shared;

import com.snow.stonehedge.marketdata.model.Book;
import com.snow.stonehedge.marketdata.model.Quote;
import com.snow.stonehedge.orders.model.Order;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Data {

    public static Map<String, Quote> QUOTES = new HashMap<>();
    private static long orderId = 0;
    public static Queue<Order> ORDER_LIST = new LinkedList<>();

    static {
        QUOTES.put("spx.us", new Quote("spx.us", new Book()));
        QUOTES.put("amzn.q", new Quote("amzn.q", new Book()));
        QUOTES.put("c.n", new Quote("c.n", new Book()));
    }

    public static void RESET_DATA() {
        ORDER_LIST.clear();
        QUOTES.clear();
        QUOTES.put("spx.us", new Quote("spx.us", new Book()));
        QUOTES.put("amzn.q", new Quote("amzn.q", new Book()));
        QUOTES.put("c.n", new Quote("c.n", new Book()));
    }

    public static long GET_ORDER_ID() {
        orderId++;
        return orderId;
    }

}
