package com.snow.stonehedge.shared;

import com.snow.stonehedge.marketdata.model.Book;
import com.snow.stonehedge.marketdata.model.Quote;
import com.snow.stonehedge.orders.model.Order;
import com.snow.stonehedge.orders.model.OrderResponse;

import java.math.BigDecimal;
import java.util.*;

public class Data {

    public static Map<String, Quote> QUOTES = new HashMap<>();
    private static long orderId = 0;
    public static Queue<Order> ORDER_LIST = new LinkedList<>();
    public static List<OrderResponse> HISTORICAL_ORDERS = new LinkedList<>();

    static {
        QUOTES.put("spx.us", new Quote("spx.us", new Book(BigDecimal.valueOf(2300.0).doubleValue())));
    }

    public static void RESET_DATA() {
        ORDER_LIST.clear();
        QUOTES.clear();
        QUOTES.put("spx.us", new Quote("spx.us", new Book(BigDecimal.valueOf(2300.0).doubleValue())));
    }

    public static long GET_ORDER_ID() {
        orderId++;
        return orderId;
    }

}
