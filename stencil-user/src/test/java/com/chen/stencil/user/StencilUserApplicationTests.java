package com.chen.stencil.user;

import cn.hutool.extra.mail.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StencilUserApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    public void sentEmail() throws Exception {
        MailUtil.send("563960993@qq.com", "测试", "邮件来自Hutool测试", false);

    }

}
