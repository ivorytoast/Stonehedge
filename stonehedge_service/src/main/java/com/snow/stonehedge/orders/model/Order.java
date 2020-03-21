package com.snow.stonehedge.orders.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

    private long id;
    private OrderRequest orderRequest;
    private boolean hasBeenFilled;

    public Order() {}

    public Order(long id, OrderRequest orderRequest) {
        this.id = id;
        this.orderRequest = orderRequest;
    }

}
