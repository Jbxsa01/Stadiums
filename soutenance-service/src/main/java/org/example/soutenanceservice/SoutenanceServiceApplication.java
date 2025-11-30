package org.example.soutenanceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.soutenanceservice.client")
public class SoutenanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoutenanceServiceApplication.class, args);
    }
}
