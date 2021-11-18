package com.loto.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.loto.config.SentinelHandlersClass;
import com.loto.service.ResumeServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autodeliver")
public class AutodeliverController {
    @Autowired
    private ResumeServiceFeignClient resumeServiceFeignClient;

    //http://localhost:8091/autodeliver/checkState1/1545132
    /**
     * 服务限流：QPS - 直接 - 快速失败
     */
    @GetMapping("/checkState1/{userId}")
    public Integer findResumeOpenState1(@PathVariable Long userId) {
        Integer defaultResumeState = resumeServiceFeignClient.findDefaultResumeState(userId);
        return defaultResumeState;
    }

    //http://localhost:8091/autodeliver/checkState2/1545132
    /**
     * 服务限流：线程数 - 直接
     */
    @GetMapping("/checkState2/{userId}")
    public Integer findResumeOpenState2(@PathVariable Long userId) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer defaultResumeState = resumeServiceFeignClient.findDefaultResumeState(userId);
        return defaultResumeState;
    }

    //http://localhost:8091/autodeliver/checkState3/1545132
    /**
     * 服务降级：异常比例
     */
    @GetMapping("/checkState3/{userId}")
    public Integer findResumeOpenState3(@PathVariable Long userId) {
        // 模拟降级：异常比例
        int i = 1/0;

        Integer defaultResumeState = resumeServiceFeignClient.findDefaultResumeState(userId);
        return defaultResumeState;
    }

    //http://localhost:8091/autodeliver/checkState4/1545132
    /**
     * Sentinel 自定义兜底方法
     */
    @SentinelResource(
            value = "findResumeOpenState4",
            blockHandlerClass = SentinelHandlersClass.class,
            blockHandler = "handleException",
            fallbackClass = SentinelHandlersClass.class,
            fallback = "handleError")
    @GetMapping("/checkState4/{userId}")
    public Integer findResumeOpenState4(@PathVariable Long userId) {
        // 模拟降级：异常比例
        int i = 1/0;

        Integer defaultResumeState = resumeServiceFeignClient.findDefaultResumeState(userId);
        return defaultResumeState;
    }

    //http://localhost:8091/autodeliver/checkState5/1545132
    /**
     * 基于 Nacos 实现 Sentinel 规则持久化
     */
    @SentinelResource(
            value = "findResumeOpenState5",
            blockHandlerClass = SentinelHandlersClass.class,
            blockHandler = "handleException",   // ⽤来指定不满⾜ Sentinel 规则的降级兜底⽅法
            fallbackClass = SentinelHandlersClass.class,
            fallback = "handleError")           // ⽤于指定 Java 运⾏时异常兜底⽅法
    @GetMapping("/checkState5/{userId}")
    public Integer findResumeOpenState5(@PathVariable Long userId) {
        Integer defaultResumeState = resumeServiceFeignClient.findDefaultResumeState(userId);
        return defaultResumeState;
    }
}
