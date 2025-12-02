package org.example.soutenanceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.example.soutenanceservice.client")
public class SoutenanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoutenanceServiceApplication.class, args);
        System.out.println(" Soutenance Service démarré et enregistré sur Eureka !");

    }
}
