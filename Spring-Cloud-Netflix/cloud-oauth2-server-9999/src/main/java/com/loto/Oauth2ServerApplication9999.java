package com.loto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.loto.pojo")
public class Oauth2ServerApplication9999 {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication9999.class, args);
    }
}
