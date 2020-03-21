package com.snow.stonehedge;

import com.snow.stonehedge.orders.model.*;
import com.snow.stonehedge.orders.service.OrdersService;
import com.snow.stonehedge.shared.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class StonehedgeApplicationTests {

    OrdersService ordersService = new OrdersService();
    OrderRequest Price_Updates_Correctly_On_Buy_Request;
    OrderRequest Price_Updates_Correctly_On_Sell_Request;
    OrderRequest Can_Fill_Buy_Request;
    OrderRequest Cannot_Fill_Buy_Request;
    OrderRequest Can_Fill_Sell_Request;
    OrderRequest Cannot_Fill_Sell_Request;

    @BeforeEach
    void init() {
        Price_Updates_Correctly_On_Buy_Request = new OrderRequest(BuyOrSell.BUY, 500, "spx.us");
        Price_Updates_Correctly_On_Sell_Request = new OrderRequest(BuyOrSell.SELL, 500, "spx.us");

        Can_Fill_Buy_Request = new OrderRequest(BuyOrSell.BUY, 500, "spx.us");
        Cannot_Fill_Buy_Request = new OrderRequest(BuyOrSell.BUY, 3000, "spx.us");

        Can_Fill_Sell_Request = new OrderRequest(BuyOrSell.BUY, 300, "spx.us");
        Cannot_Fill_Sell_Request = new OrderRequest(BuyOrSell.SELL, 250, "spx.us");

        Data.RESET_DATA();
    }

    @Test
    public void testOne() {
        ordersService.submitOrder(Can_Fill_Sell_Request);
        ordersService.submitOrder(Cannot_Fill_Sell_Request);

        List<OrderResponse> response = ordersService.processOrders();

        System.out.println("CHA");
    }

    @Test
    public void testTwo() {
        ordersService.submitOrder(Cannot_Fill_Sell_Request);
        ordersService.submitOrder(Can_Fill_Sell_Request);

        List<OrderResponse> response = ordersService.processOrders();

        System.out.println("HELLO");
    }

}
