package com.snow.stonehedge.orders.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class OrderRequest {

    private BuyOrSell buyOrSell;
    private long quantity;
    private String symbol;
    @JsonIgnore
    private double fillPrice = 0;
    @JsonIgnore
    private FillType fillType = FillType.MARKET;
    @JsonIgnore
    private long amountThatHasBeenFilled = 0;
    @JsonIgnore
    private double totalDollarAmountFilled = 0;
    @JsonIgnore
    private long timeLimit = 1;

    public OrderRequest(BuyOrSell buyOrSell, long quantity, String symbol) {
        this.buyOrSell = buyOrSell;
        this.quantity = quantity;
        this.symbol = symbol;
    }

    public void addOneToTimeLimit() {
        timeLimit++;
    }

    public void updateAmountThatHasBeenFilled(long amount) {
        this.amountThatHasBeenFilled += amount;
    }

    public void addToTotalDollarAmountFilled(double amount) {
        this.totalDollarAmountFilled += amount;
        updateFillPrice();
    }

    public void updateFillPrice() {
        if (amountThatHasBeenFilled == 0 || totalDollarAmountFilled == 0) {
//            log.error("Amount: " + amountThatHasBeenFilled);
//            log.error("Dollar: " + totalDollarAmountFilled);
//            log.warn("Price: " + this.fillPrice);
            fillPrice = 0;
        } else {
//            log.warn("Price: " + this.fillPrice);
            this.fillPrice = totalDollarAmountFilled / amountThatHasBeenFilled;
        }
    }
}
