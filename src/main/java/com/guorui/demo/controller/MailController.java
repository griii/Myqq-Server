package com.guorui.demo.controller;


import com.guorui.demo.Service.IMailServiceImpl;
import com.guorui.demo.Service.UserService;
import com.guorui.demo.bean.MyFriend;
import com.guorui.demo.bean.Result;
import com.guorui.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
public class MailController {

    @Autowired
    IMailServiceImpl mailService;

    @Autowired
    UserDao userDao;

    @ResponseBody
    @RequestMapping("sendMail")
    public Result sendMail(String toMailUrl){
        Result result = new Result();
        //发送邮件包含code验证码，然后将验证码存入数据库中...
        if ( userDao.selectUidByMail(toMailUrl) == null){
            result.setStatus(403);
            result.setMsg("该邮箱未注册账号！");
            return result;
        }
        int id = userDao.selectUidByMail(toMailUrl).getId();
        int code = new Random().nextInt(999999);
        System.out.println(code + "");
        try {
            mailService.sendSimpleMail(toMailUrl, "MyQQ注册/找回密码验证码", "本次验证码为:" + code);
            System.out.println("修改code:" + code + "id:" + id);
            userDao.updateCode(code+"",id);
        }catch (Exception e){
            result.setStatus(403);
            result.setMsg("发送失败,请检查邮箱");
            return result;
        }
        result.setStatus(200);
        result.setMsg("发送成功");
        return result;
    }
}
