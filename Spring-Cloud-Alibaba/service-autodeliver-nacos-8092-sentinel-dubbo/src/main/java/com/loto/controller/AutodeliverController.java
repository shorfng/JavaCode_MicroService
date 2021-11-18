package com.loto.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.loto.ResumeService;
import com.loto.config.SentinelHandlersClass;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autodeliver")
public class AutodeliverController {
    @Reference
    private ResumeService resumeService;

    //http://localhost:8092/autodeliver/checkState/1545132
    @GetMapping("/checkState/{userId}")
    @SentinelResource(
            value = "findResumeOpenState",
            blockHandlerClass = SentinelHandlersClass.class,
            blockHandler = "handleException",
            fallbackClass = SentinelHandlersClass.class,
            fallback = "handleError")
    public Integer findResumeOpenState(@PathVariable Long userId) {
        return resumeService.findDefaultResumeByUserId(userId);
    }
}
