package com.snow.stonehedge.orders.controller;

import com.google.cloud.firestore.Firestore;
import com.snow.stonehedge.fair.database.DatabaseCollections;
import com.snow.stonehedge.fair.database.functions.DatabaseFunctions;
import com.snow.stonehedge.orders.model.Order;
import com.snow.stonehedge.orders.model.OrderRequest;
import com.snow.stonehedge.orders.model.OrderResponse;
import com.snow.stonehedge.orders.service.OrdersService;
import com.snow.stonehedge.shared.Data;
import com.snow.stonehedge.shared.exceptions.CustomErrorResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController()
public class OrdersController {

    private Firestore firestore;
    private OrdersService ordersService;
    private DatabaseFunctions databaseFunctions;

    public OrdersController(DatabaseFunctions databaseFunctions) {
        ordersService = new OrdersService();
        this.databaseFunctions = databaseFunctions;
    }

    @PostMapping("/v1/submit")
    public long submitOrder(@RequestBody OrderRequest orderRequest) {
//        return ordersService.submitOrder(orderRequest);
        return -1;
    }

    @GetMapping("/enigma/order/v1/{orderID}")
    public String getOrderFromDatabase(@PathVariable() String orderID) throws Exception {
        return getOrder(orderID);
    }

    @PostMapping("/enigma/submit/v1")
    public void submitOrderToDatabase(@RequestBody Order order) throws Exception {
        submitOrder(order);
    }

    private String getOrder(String orderID) throws Exception {
        return databaseFunctions.getDocument(DatabaseCollections.ORDERS, orderID);
    }

    private void submitOrder(Order order) throws Exception {
        databaseFunctions.addDocument(DatabaseCollections.ORDERS, order.toDatabaseMap());
    }

    @GetMapping("/v1/realtime/orders/all")
    public List<Order> getAllOrders() {
        return new ArrayList<>(Data.ORDER_LIST);
    }

    @GetMapping("/v1/realtime/orders/")
    @NonNull
    public List<Order> getOrder() {
        return new ArrayList<>(Data.ORDER_LIST);
    }

    @GetMapping("/v1/realtime/orders/{orderID}")
    @NonNull
    public Order getOrderInformationForOrder(@PathVariable long orderID) {
//        Optional<Order> output = Data.ORDER_LIST.stream()
//            .filter(order -> order.getId() == orderID)
//            .findFirst();
//        return output.orElseGet(Order::new);
        return null;
    }

    @GetMapping("/v1/realtime/orders/historical/{orderID}")
    @NonNull
    public OrderResponse getHistoricalOrderResponse(@PathVariable long orderID) {
        Optional<OrderResponse> output = Data.HISTORICAL_ORDERS.stream()
            .filter(order -> order.getTradeId() == orderID)
            .findFirst();
        return output.orElseGet(OrderResponse::new);
    }

    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

    }

}
