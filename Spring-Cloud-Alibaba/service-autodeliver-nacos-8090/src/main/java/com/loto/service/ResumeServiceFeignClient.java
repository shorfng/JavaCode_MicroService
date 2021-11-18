package com.loto.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient：表明当前类是一个 Feign 客户端
// value：指定该客户端要请求的服务名称（登记到注册中心上的服务提供者的服务名称）
// fallback：回退实现类
// path：当使用 fallback 时，无法使用 @RequestMapping("/resume")，而需要使用 path
@FeignClient(value = "service-resume-nacos", fallback = ResumeFallback.class, path = "/resume")
public interface ResumeServiceFeignClient {
    // Feign 作用：拼装 url，发起请求
    // 调用该方法就是调用本地接口方法，实际上做的是远程请求
    @GetMapping("/openstate/{userId}")
    public Integer findDefaultResumeState(@PathVariable("userId") Long userId);
}
