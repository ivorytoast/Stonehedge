package com.snow.stonehedge;

import com.snow.stonehedge.orders.model.*;
import com.snow.stonehedge.orders.service.OrdersService;
import com.snow.stonehedge.shared.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Price_Updates_Correctly_On_Buy_Request = new OrderRequest(BuyOrSell.BUY, 500, "spx.us", FillType.MARKET, 3000.0);
        Price_Updates_Correctly_On_Sell_Request = new OrderRequest(BuyOrSell.SELL, 500, "spx.us", FillType.MARKET, 3000.0);

        Can_Fill_Buy_Request = new OrderRequest(BuyOrSell.BUY, 500, "spx.us", FillType.MARKET, 3000.0);
        Cannot_Fill_Buy_Request = new OrderRequest(BuyOrSell.BUY, 3000, "spx.us", FillType.MARKET, 3000.0);

        Can_Fill_Sell_Request = new OrderRequest(BuyOrSell.SELL, 650, "spx.us", FillType.MARKET, 3000.0);
        Cannot_Fill_Sell_Request = new OrderRequest(BuyOrSell.SELL, 3000, "spx.us", FillType.MARKET, 3000.0);

        Data.RESET_DATA();
    }

    @Test
    void Price_Updates_Correctly_On_Buy() {
        assertEquals(3000.00, Data.QUOTES.get("spx.us").getBook().getCurrentPrice(), "The start price should 3000.0");
        ordersService.submitOrder(Price_Updates_Correctly_On_Buy_Request);

        List<OrderResponse> response = ordersService.processOrders();

        assertEquals(1, response.size(),"Make sure the deal processed without an error");
        assertEquals(response.get(0).getSuccessOrFailure(), SuccessOrFailure.SUCCESS, "Make sure the first deal filled successfully");
        assertEquals(0, Data.QUOTES.get("spx.us").getBook().getAsks().get(3000.0), "The 3000.0 asks should be wiped out");
        assertEquals(3000.50, Data.QUOTES.get("spx.us").getBook().getCurrentPrice(), "The current price should be 3000.5");
        assertEquals(new BigDecimal("3000.00"), response.get(0).getFillPrice(), "The average fill price should 3000.00");
    }

    @Test
    void Price_Updates_Correctly_On_Sell() {
        assertEquals(0.0, Data.QUOTES.get("spx.us").getBook().getCurrentPrice(), "The start price should 0.0");
        ordersService.submitOrder(Price_Updates_Correctly_On_Sell_Request);
        ordersService.submitOrder(Can_Fill_Buy_Request);

        List<OrderResponse> response = ordersService.processOrders();

        assertEquals(2, response.size(),"Make sure the deal processed without an error");
        assertEquals(response.get(0).getSuccessOrFailure(), SuccessOrFailure.SUCCESS, "Make sure the first deal filled successfully");
        assertEquals(0, Data.QUOTES.get("spx.us").getBook().getBids().get(3000.0), "The 3000.0 bids should be wiped out");
        assertEquals(0, Data.QUOTES.get("spx.us").getBook().getAsks().get(3000.0), "The 3000.0 asks should be wiped out");
        assertEquals(3000.0, Data.QUOTES.get("spx.us").getBook().getCurrentPrice(), "The current price should be 2999.5");
        assertEquals(new BigDecimal("3000.00"), response.get(0).getFillPrice(), "The average fill price should 3000.00");
    }

    @Test
    void Can_Fill_Buy() {
        ordersService.submitOrder(Can_Fill_Buy_Request);

        List<OrderResponse> response = ordersService.processOrders();

        assertEquals(1, response.size(),"Make sure the deal processed without an error");
        assertEquals(response.get(0).getSuccessOrFailure(), SuccessOrFailure.SUCCESS, "Make sure the first deal filled successfully");
        assertEquals(0, Data.QUOTES.get("spx.us").getBook().getAsks().get(3000.0), "The 3000.0 asks should be wiped out");
        assertEquals(50, Data.QUOTES.get("spx.us").getBook().getAsks().get(3001.0), "There should only be 50 3001.0 asks");
        assertEquals(new BigDecimal("3000.23"), response.get(0).getFillPrice(), "The average fill price should 3000.23");
    }

    @Test
    void Cannot_Fill_Buy() {
        ordersService.submitOrder(Cannot_Fill_Buy_Request);

        List<OrderResponse> response = ordersService.processOrders();

        assertEquals(1, response.size(),"Make sure the deal processed without an error");
        assertEquals(response.get(0).getSuccessOrFailure(), SuccessOrFailure.FAILURE, "The deal should fail to be filled");
        assertEquals(500, Data.QUOTES.get("spx.us").getBook().getAsks().get(3000.0), "There should be no change in 3000.0 asks");
        assertEquals(200, Data.QUOTES.get("spx.us").getBook().getAsks().get(3001.0), "There should be no change in 3001.0 asks");
        assertEquals(200, Data.QUOTES.get("spx.us").getBook().getAsks().get(3002.0), "There should be no change in 3002.0 asks");
        assertEquals(new BigDecimal("-1"), response.get(0).getFillPrice(), "Return -1 since not filled");
    }

    @Test
    void Can_Fill_Sell() {
        ordersService.submitOrder(Can_Fill_Sell_Request);

        List<OrderResponse> response = ordersService.processOrders();

        assertEquals(1, response.size(),"Make sure the deal processed without an error");
        assertEquals(response.get(0).getSuccessOrFailure(), SuccessOrFailure.SUCCESS, "Make sure the first deal filled successfully");
        assertEquals(0, Data.QUOTES.get("spx.us").getBook().getBids().get(3000.0), "The 3000.0 bids should be wiped out");
        assertEquals(50, Data.QUOTES.get("spx.us").getBook().getBids().get(2999.0), "There should only be 50 2999.0 bids");
        assertEquals(new BigDecimal("2999.77"), response.get(0).getFillPrice(), "The average fill price should 2999.77");
    }

    @Test
    void Cannot_Fill_Sell() {
        ordersService.submitOrder(Cannot_Fill_Sell_Request);

        List<OrderResponse> response = ordersService.processOrders();

        assertEquals(1, response.size(),"Make sure the deal processed without an error");
        assertEquals(response.get(0).getSuccessOrFailure(), SuccessOrFailure.FAILURE, "The deal should fail to be filled");
        assertEquals(500, Data.QUOTES.get("spx.us").getBook().getBids().get(3000.0), "There should be no change in 3000.0 bids");
        assertEquals(200, Data.QUOTES.get("spx.us").getBook().getBids().get(2999.0), "There should be no change in 2999.0 bids");
        assertEquals(200, Data.QUOTES.get("spx.us").getBook().getBids().get(2998.0), "There should be no change in 2998.0 bids");
        assertEquals(new BigDecimal("-1"), response.get(0).getFillPrice(), "Return -1 since not filled");
    }

}
