package com.loto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AutodeliverApplication8081 {
    public static void main(String[] args) {
        SpringApplication.run(AutodeliverApplication8081.class, args);
    }

    // 使用 RestTemplate 模板对象进行远程调用
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
