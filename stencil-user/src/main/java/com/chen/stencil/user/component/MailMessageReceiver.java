package com.chen.stencil.user.component;

import cn.hutool.extra.mail.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "chen.message.mail")
public class MailMessageReceiver {

    private static Logger LOGGER = LoggerFactory.getLogger(MailMessageReceiver.class);


    @RabbitHandler
    public void handle(Map obj) {

        String mail = obj.get("mail").toString();
        String code = obj.get("code").toString();
        MailUtil.send(mail, "您正在绑定邮箱操作", "验证码为:" + code, false);
        LOGGER.info("process mail:{}", obj.toString());
    }
}
