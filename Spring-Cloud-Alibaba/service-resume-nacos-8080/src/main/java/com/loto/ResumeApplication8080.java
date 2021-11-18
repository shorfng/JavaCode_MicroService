package com.loto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EntityScan("com.loto.pojo")
// 开启注册中心客户端 （通用型注解，使用Eureka或者Nacos等作为注册中心时使用）
@EnableDiscoveryClient
public class ResumeApplication8080 {
    public static void main(String[] args) {
        SpringApplication.run(ResumeApplication8080.class,args);
    }
}
