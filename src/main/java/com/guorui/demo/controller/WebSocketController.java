package com.guorui.demo.controller;


import com.guorui.demo.Service.UserService;
import com.guorui.demo.bean.User;
import com.guorui.demo.bean.WebSocketMessage;
import com.guorui.demo.config.WebSocketConfig;
import com.guorui.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.enterprise.inject.spi.SessionBeanType;
import java.util.Date;

@Controller
public class WebSocketController {


    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello")
    @SendTo({"/topic/greeting" })
    public String greeting(String msg){
        System.out.println("接收到客户端信息，这里转发给消息代理进行转发...");
        return msg;
    }

    @MessageMapping("/sengImg")
    public void sengImg(WebSocketMessage message){
        System.out.println(message.toString());
    }
    //创建一个所有人都可以访问的webSocket

    @MessageMapping("/rtc")
    public void rtc(WebSocketMessage message){
        messagingTemplate.convertAndSend("/topicRTC/"+message.getUid(),message);
    }

    @MessageMapping("/test")
    public void test(WebSocketMessage message){
        message.setTimeStamp(new Date().getTime());
        if (!message.getContent().equals("窗口抖动"))
        userService.insertMsg(Integer.parseInt(message.getRoomId()),Integer.parseInt(message.getUid()),message.getContent(),message.getImg(),message.getName());
        String auth = userDao.getRoomAuth(Integer.parseInt(message.getRoomId()));
        String[] allUsers = auth.substring(1,auth.length()-1).split("\\|");
        for(int obj: WebSocketConfig.users){
            for (String user:allUsers){
                if (Integer.parseInt(user) == obj){
                    messagingTemplate.convertAndSend("/topicHome/" + Integer.parseInt(user),message);
                    messagingTemplate.convertAndSend("/topicChat/" + Integer.parseInt(user),message);
                }
            }
        }
    }

    @MessageMapping("/webRTC")
    public void webRTC(WebSocketMessage message){
        System.out.println(message);
    }
}
