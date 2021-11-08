package com.loto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/autodeliver")
public class AutodeliverController {
    // RestTemplate 模板对象（Rest 风格的远程服务调用）
    @Autowired
    private RestTemplate restTemplate;

    //http://localhost:8081/autodeliver/checkState/1545132
    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId) {
        // 调用远程服务（简历微服务接口） - RestTemplate
        return restTemplate.getForObject("http://localhost:8080/resume/openstate/" + userId, Integer.class);
    }
}
