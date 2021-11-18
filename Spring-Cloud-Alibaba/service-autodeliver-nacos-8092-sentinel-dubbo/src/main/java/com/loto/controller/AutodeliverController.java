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
            blockHandler = "handleException",   // ⽤来指定不满⾜ Sentinel 规则的降级兜底⽅法
            fallbackClass = SentinelHandlersClass.class,
            fallback = "handleError")           // ⽤于指定 Java 运⾏时异常兜底⽅法
    public Integer findResumeOpenState(@PathVariable Long userId) {
        return resumeService.findDefaultResumeByUserId(userId);
    }
}
