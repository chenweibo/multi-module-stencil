package com.chen.stencil.user.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "chen.message.telephone")
public class TelephoneMessageReceiver {

    private static Logger LOGGER = LoggerFactory.getLogger(TelephoneMessageReceiver.class);


    @RabbitHandler
    public void handle(Map obj) {

        LOGGER.info("process telephone:{}", obj.toString());
    }
}
