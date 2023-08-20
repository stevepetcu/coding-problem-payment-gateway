package com.stefanpetcu.paymentgatewayapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PaymentGatewayApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentGatewayApiApplication.class, args);
    }

}
