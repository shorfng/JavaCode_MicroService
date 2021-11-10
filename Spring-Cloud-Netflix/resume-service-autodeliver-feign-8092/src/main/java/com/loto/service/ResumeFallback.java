package com.loto.service;

import org.springframework.stereotype.Component;

/**
 * 降级回退逻辑，实现 FeignClient 接口，实现接口中的方法
 */
@Component
public class ResumeFallback implements ResumeServiceFeignClient {
    @Override
    public Integer findDefaultResumeState(Long userId) {
        return -1;
    }
}
