package com.loto.service.impl;

import com.loto.service.IMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;

// Source.class 里面是对输出通道的定义（这是 Spring Cloud Stream 内置的通道封装）
@EnableBinding(Source.class)
public class MessageProducerImpl implements IMessageProducer {
    // 将MessageChannel 的封装对象 Source 注入到这里使用
    @Autowired
    private Source source;

    @Override
    public void sendMessage(String content) {
        // 向 mq 中发送消息（并不是直接操作 mq，而是通过 spring cloud stream 操作的）
        // 使用通道向外发出消息(指的是 Source 里面的 output 通道)
        source.output().send(MessageBuilder.withPayload(content).build());
    }
}
