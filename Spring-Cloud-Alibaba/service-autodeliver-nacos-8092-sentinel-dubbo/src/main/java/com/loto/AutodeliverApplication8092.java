package com.loto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AutodeliverApplication8092 {
    public static void main(String[] args) {
        SpringApplication.run(AutodeliverApplication8092.class, args);
    }
}
