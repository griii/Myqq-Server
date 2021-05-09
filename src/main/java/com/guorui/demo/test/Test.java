package com.guorui.demo.test;

import com.guorui.demo.Service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
public class Test {

    @Autowired
    private IMailService mailService;

    @Autowired
    private static SessionRegistry sessionRegistry;

    public static void main(String[] args) {
        new Test().test();
    }

    public void test(){
        mailService.sendSimpleMail("437473465@qq.com","测试","测试");
    }


}
