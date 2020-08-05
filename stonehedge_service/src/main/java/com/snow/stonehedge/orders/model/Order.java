package com.snow.stonehedge.orders.model;

import lombok.*;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "orderCreator", buildMethodName = "createNewOrder")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    @Builder.Default private String id = UUID.randomUUID().toString();
    @NonNull private String buyOrSell;
    @NonNull private long quantity;
    @NonNull private String symbol;
    @NonNull private String fillType;
    @Builder.Default private boolean filled = false;
    @Builder.Default private double fillPrice = -1;
    @Builder.Default private long amountThatHasBeenFilled = -1;
    @Builder.Default private long totalDollarAmountFilled = -1;

    public Order(String buyOrSell, long quantity, String symbol, String fillType) {
        this.buyOrSell = buyOrSell;
        this.quantity = quantity;
        this.symbol  = symbol;
        this.fillType = fillType;
    }

    public HashMap<String, Object> toDatabaseMap() {
        HashMap<String, Object> databaseMap = new HashMap<>();
        databaseMap.put("id", getId());
        databaseMap.put("buyOrSell", getBuyOrSell());
        databaseMap.put("quantity", getQuantity());
        databaseMap.put("symbol", getSymbol());
        databaseMap.put("fillType", getFillType());
        databaseMap.put("hasBeenFilled", isFilled());
        databaseMap.put("fillPrice", getFillPrice());
        databaseMap.put("amountThatHasBeenFilled", getAmountThatHasBeenFilled());
        databaseMap.put("totalDollarAmountFilled", getTotalDollarAmountFilled());
        return databaseMap;
    }

}
