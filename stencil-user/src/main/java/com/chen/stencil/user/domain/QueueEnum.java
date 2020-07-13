package com.chen.stencil.user.domain;


import lombok.Getter;

/**
 * 消息队列枚举配置
 */
@Getter
public enum QueueEnum {


    /**
     * 短信通知队列
     */
    QUEUE_TEL_CODE("chen.message.direct", "chen.message.telephone", "chen.message.telephone"),


    /**
     * 邮件通知队列
     */
    QUEUE_EMAIL("chen.message.direct", "chen.message.mail", "chen.message.mail");
    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
