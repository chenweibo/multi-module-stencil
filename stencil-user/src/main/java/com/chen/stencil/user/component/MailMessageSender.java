package com.chen.stencil.user.component;

import com.chen.stencil.user.domain.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MailMessageSender {

    private static Logger LOGGER = LoggerFactory.getLogger(MailMessageSender.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(Map obj) {

        amqpTemplate.convertAndSend(QueueEnum.QUEUE_EMAIL.getExchange(), QueueEnum.QUEUE_EMAIL.getRouteKey(), obj);
        LOGGER.info("send mall:{}", obj.toString());
    }
}
