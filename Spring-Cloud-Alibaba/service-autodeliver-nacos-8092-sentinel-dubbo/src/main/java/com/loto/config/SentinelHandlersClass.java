package com.loto.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class SentinelHandlersClass {
    // 触发在 Sentinel DashBoard 设置的降级策略后，接收异常，返回自定义兜底数据
    // 在形参中添加 BlockException 参数，用于接收异常
    public static Integer handleException(Long userId, BlockException blockException) {
        return -100;
    }

    // 触发 java 程序异常
    public static Integer handleError(Long userId) {
        return -500;
    }
}
