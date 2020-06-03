package com.snow.stonehedge.enigma;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Book bids = new Book(true);
        Book asks = new Book(false);

        Order order1 = new Order(11, 50);
        Order order2 = new Order(13, 100);
        Order order3 = new Order(12, 300);

        Order matchOrder = new Order(0, 14);

//        Order order1 = new Order(13, 50);
//        Order order2 = new Order(11, 100);
//        Order order3 = new Order(12, 300);
//
//        Order matchOrder = new Order(0, 150);

//        Order order1 = new Order(9, 100);
//        Order order2 = new Order(10, 200);
//        Order order3 = new Order(8, 50);
//        Order order4 = new Order(9, 100);
//        Order order5 = new Order(10, 200);
//        Order order6 = new Order(8, 50);

        bids.processOrder(order1);
        bids.processOrder(order2);
        bids.processOrder(order3);
//        bids.processOrder(order4);
//        bids.processOrder(order5);
//        bids.processOrder(order6);

//        bids.removeOrder(order1);
//        bids.removeOrder(order2);
//        bids.removeOrder(order3);
//        bids.removeOrder(order4);
//        bids.removeOrder(order5);
//        bids.removeOrder(order6);
//
//        asks.processOrder(order1);
//        asks.processOrder(order2);
//        asks.processOrder(order3);
//        asks.processOrder(order4);
//        asks.processOrder(order5);
//        asks.processOrder(order6);

//        asks.removeOrder(order1);
//        asks.removeOrder(order2);
//        asks.removeOrder(order3);
//        asks.removeOrder(order4);
//        asks.removeOrder(order5);
//        asks.removeOrder(order6);

        bids.matchOrder(matchOrder);

//        printOut(asks);
        printOut(bids);
    }

    static void printOut(Book book) {
        for (Map.Entry<Double, List<Order>> bid : book.ordersPerPrice.entrySet()) {
            System.out.println();
            System.out.print(bid.getKey() + " -> [");
            for (Order value : bid.getValue()) {
                System.out.print("[" + value.getId() + "] " + value.quantity + " @ $" + value.price + ", ");
            }
            System.out.print("]");
        }
        System.out.println();
        System.out.println("-----");
        for (Order value : book.completedOrders) {
            System.out.println("[" + value.getId() + "] " + value.quantity + " @ $" + value.price + ", ");
        }
        System.out.println("-----");
        for (Map.Entry<Double, Long> stockPerPrice : book.availablePerPrice.entrySet()) {
            System.out.println(stockPerPrice.getKey() +  " -> " + stockPerPrice.getValue());
        }
        System.out.println("-----");
        System.out.println("Best Price -> " + book.bestPrice);
        System.out.println("Total amount of bids -> " + book.amountAvailable);
    }

}
