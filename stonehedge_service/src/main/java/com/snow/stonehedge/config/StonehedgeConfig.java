package com.snow.stonehedge.config;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.snow.stonehedge.fair.database.functions.DatabaseFunctions;
import com.snow.stonehedge.orders.service.OrdersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StonehedgeConfig {

    @Bean
    public OrdersService ordersService() {
        return new OrdersService();
    }

    @Bean
    public Firestore fireStoreClient() {
        return FirestoreClient.getFirestore();
    }

    @Bean
    public DatabaseFunctions databaseFunctions(Firestore firestore) {
        return new DatabaseFunctions(firestore);
    }

}
