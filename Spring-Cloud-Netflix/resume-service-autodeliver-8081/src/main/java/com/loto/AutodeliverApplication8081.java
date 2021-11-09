package com.loto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class AutodeliverApplication8081 {
    public static void main(String[] args) {
        SpringApplication.run(AutodeliverApplication8081.class, args);
    }

    // 使用 RestTemplate 模板对象进行远程调用
    @Bean
    @LoadBalanced // Ribbon 负载均衡
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
