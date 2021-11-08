package com.loto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EntityScan("com.loto.pojo")

// 说明：从 SpringCloud Edgware 版本开始，不加注解也可以，但是建议加上
// 方式1：开启 Eureka Client（使用 Eureka 作为注册中心时）
//@EnableEurekaClient

// 方式2：开启注册中心客户端 （通用型注解，使用Eureka或者Nacos等作为注册中心时使用）
@EnableDiscoveryClient
public class ResumeApplication8080 {
    public static void main(String[] args) {
        SpringApplication.run(ResumeApplication8080.class,args);
    }
}
