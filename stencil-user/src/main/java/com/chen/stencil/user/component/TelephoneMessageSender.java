package com.chen.stencil.user.component;

import com.chen.stencil.user.domain.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TelephoneMessageSender {

    private static Logger LOGGER = LoggerFactory.getLogger(TelephoneMessageSender.class);
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(Map obj) {

        amqpTemplate.convertAndSend(QueueEnum.QUEUE_TEL_CODE.getExchange(), QueueEnum.QUEUE_TEL_CODE.getRouteKey(), obj);
        LOGGER.info("send telephone:{}", obj.get("telephone"));
    }
}
