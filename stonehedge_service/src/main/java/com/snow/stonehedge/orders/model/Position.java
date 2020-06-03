package com.snow.stonehedge.orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class Position {

    private BuyOrSell buyOrSell;
    private CallOrPut callOrPut;
    private double strike;
    private double size;
    private double multiplier;
    private LocalDate expiryDate;

    public double getPnL(double currentPrice) {
        double pnl = Math.max(((callOrPut == CallOrPut.CALL) ? currentPrice - strike : strike - currentPrice), 0);
        double value = (buyOrSell == BuyOrSell.BUY) ? (pnl * size * multiplier) : (pnl * -1.0 * size * multiplier);
        return value;
    }

}
