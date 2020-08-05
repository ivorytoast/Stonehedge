package com.snow.stonehedge;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;

@SpringBootApplication()
@Slf4j
public class StonehedgeApplication {

    public static void main(String[] args) throws Exception {
//        OrdersService ordersService = new OrdersService();
        FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://fair-1fe18.firebaseio.com")
            .build();

        if (FirebaseApp.getApps().size() == 0) {
            FirebaseApp.initializeApp(options);
        } else if (FirebaseApp.getApps().size() == 1) {
            FirebaseApp.getInstance();
        } else {
            throw new Exception("Multiple Firebase Apps running at the same time -- cannot start!");
        }

        SpringApplication.run(StonehedgeApplication.class, args);
//        while (true) {
//            try {
//                log.info("Waiting 20 seconds to process orders...");
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.info("Processing orders...");
//            List<OrderResponse> responses = ordersService.processOrders();
//            if (responses.size() == 0) log.info("No orders processed...");
//            responses.forEach(orderResponse -> {
//                log.info("Order # " + orderResponse.getTradeId() + " (" + orderResponse.getSuccessOrFailure() + ", " + orderResponse.getBuyOrSell() + "), @ " + orderResponse.getFillPrice());
//            });
//            log.info("Processed orders...");
//        }
    }

}
