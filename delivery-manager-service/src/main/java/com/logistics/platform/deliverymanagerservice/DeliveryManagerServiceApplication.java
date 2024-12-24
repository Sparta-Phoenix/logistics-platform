package com.logistics.platform.deliverymanagerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DeliveryManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryManagerServiceApplication.class, args);
    }

}
