package com.snow.stonehedge;

import com.snow.stonehedge.orders.model.OrderResponse;
import com.snow.stonehedge.orders.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication()
@Slf4j
public class StonehedgeApplication {

    public static void main(String[] args) {
        OrdersService ordersService = new OrdersService();
        SpringApplication.run(StonehedgeApplication.class, args);
        while (true) {
            try {
//                log.info("Waiting 20 seconds to process orders...");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            log.info("Processing orders...");
            List<OrderResponse> responses = ordersService.processOrders();
//            if (responses.size() == 0) log.info("No orders processed...");
            responses.forEach(orderResponse -> {
                log.info("Order # " + orderResponse.getTradeId() + " (" + orderResponse.getSuccessOrFailure() + ", " + orderResponse.getBuyOrSell() + "), @ " + orderResponse.getFillPrice());
            });
//            log.info("Processed orders...");
        }
    }

}
