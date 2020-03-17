package com.snow.stonehedge.orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderRequest {

    private BuyOrSell buyOrSell;
    private long quantity;
    private String symbol;
    private FillType fillType;
    private double price;

}
