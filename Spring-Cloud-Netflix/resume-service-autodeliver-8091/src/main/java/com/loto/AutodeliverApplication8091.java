package com.loto;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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
public class AutodeliverApplication8091 {
    public static void main(String[] args) {
        SpringApplication.run(AutodeliverApplication8091.class, args);
    }

    // 使用 RestTemplate 模板对象进行远程调用
    @Bean
    @LoadBalanced // Ribbon 负载均衡
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 在被监测的微服务中注册监控 servlet
     */
    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
