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

    //http://localhost:8093/autodeliver/checkState3/1545132
    /**
     * 使用 Ribbon 负载均衡
     */
    @GetMapping("/checkState3/{userId}")
    public Integer findResumeOpenState3(@PathVariable Long userId) {
        // 指定服务名
        String url = "http://resume-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }
}
