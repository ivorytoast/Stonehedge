package com.snow.stonehedge.orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private BuyOrSell buyOrSell;
    private long quantity;
    private String symbol;
    private FillType fillType;
    private double price;
    private long amountThatHasBeenFilled = 0;
    private long timeLimit = 1;

    public OrderRequest(BuyOrSell buyOrSell, long quantity, String symbol, FillType fillType, double price) {
        this.buyOrSell = buyOrSell;
        this.quantity = quantity;
        this.symbol = symbol;
        this.fillType = fillType;
        this.price = price;
    }

    public void addOneToTimeLimit() {
        timeLimit++;
    }

    public void updateAmountThatHasBeenFilled(long amount) {
        this.amountThatHasBeenFilled += amount;
    }
}
