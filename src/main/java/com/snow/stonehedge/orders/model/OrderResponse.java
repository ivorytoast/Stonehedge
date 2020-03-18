package com.snow.stonehedge.orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {

    private long tradeId;
    private SuccessOrFailure successOrFailure;
    private BuyOrSell buyOrSell;
    private String symbol;
    private long quantity;
    private BigDecimal fillPrice;

    public OrderResponse() {
        tradeId = -1;
        successOrFailure = SuccessOrFailure.FAILURE;
        buyOrSell = BuyOrSell.BUY;
        symbol = "";
        quantity = -1;
        fillPrice = BigDecimal.valueOf(-1.0);
    }

}
