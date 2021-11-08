package com.loto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/autodeliver")
public class AutodeliverController {
    // RestTemplate 模板对象（Rest 风格的远程服务调用）
    @Autowired
    private RestTemplate restTemplate;

    // 注入服务发现客户端
    @Autowired
    private DiscoveryClient discoveryClient;

    //http://localhost:8081/autodeliver/checkState/1545132
    /**
     * 原始写法
     */
    //@GetMapping("/checkState/{userId}")
    //public Integer findResumeOpenState(@PathVariable Long userId) {
    //    // 调用远程服务（简历微服务接口） - RestTemplate
    //    return restTemplate.getForObject("http://localhost:8080/resume/openstate/" + userId, Integer.class);
    //}

    //http://localhost:8081/autodeliver/checkState/1545132
    /**
     * 服务注册到 Eureka 之后的改造：从注册中心拿服务实例，进行访问
     */
    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId) {
        // 1、从 Eureka Server 中获取 resume-service-resume 服务的实例信息（使用客户端对象做这件事）
        List<ServiceInstance> instances = discoveryClient.getInstances("resume-service-resume");

        // 2、如果有多个实例，选择一个使用(负载均衡的过程)
        ServiceInstance serviceInstance = instances.get(0);

        // 3、从元数据信息获取 host、port
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        String url = "http://" + host + ":" + port + "/resume/openstate/" + userId;
        System.out.println("===============>>>从EurekaServer集群获取服务实例拼接的url：" + url);

        // 调用远程服务（简历微服务接口） - RestTemplate
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }
}
