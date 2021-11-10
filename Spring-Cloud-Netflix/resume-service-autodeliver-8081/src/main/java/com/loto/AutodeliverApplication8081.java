package com.loto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableHystrix       // 开启 Hystrix 功能
@EnableCircuitBreaker  // 开启熔断器功能（通用）

// 综合性的注解：@SpringCloudApplication  = @SpringBootApplication + @EnableDiscoveryClient + @EnableCircuitBreaker
//@SpringCloudApplication
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
