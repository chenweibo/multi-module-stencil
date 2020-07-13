package com.chen.stencil.user.config;

import com.chen.stencil.user.domain.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 发送短信队列所绑定的交换机
     */
    @Bean
    DirectExchange messageDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_TEL_CODE.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 发送短信队列
     */
    @Bean
    public Queue sentTelQueue() {
        return new Queue(QueueEnum.QUEUE_TEL_CODE.getName());
    }


    /**
     * 将订短信队列绑定到交换机
     */
    @Bean
    Binding telephoneBinding(DirectExchange messageDirect, Queue sentTelQueue) {
        return BindingBuilder
                .bind(sentTelQueue)
                .to(messageDirect)
                .with(QueueEnum.QUEUE_TEL_CODE.getRouteKey());
    }


    /**
     * 发送短信队列
     */
    @Bean
    public Queue sentMailQueue() {
        return new Queue(QueueEnum.QUEUE_EMAIL.getName());
    }


    /**
     * 将订邮件队列绑定到交换机
     */
    @Bean
    Binding mailBinding(DirectExchange messageDirect, Queue sentMailQueue) {
        return BindingBuilder
                .bind(sentMailQueue)
                .to(messageDirect)
                .with(QueueEnum.QUEUE_EMAIL.getRouteKey());
    }

}
