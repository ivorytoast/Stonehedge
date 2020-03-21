package com.snow.stonehedge.config;

import com.snow.stonehedge.orders.service.OrdersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StonehedgeConfig {

    @Bean
    public OrdersService ordersService() {
        return new OrdersService();
    }

}
