package com.guorui.demo.test;

import com.guorui.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;



@Controller
public class MyController {
    @Autowired
    UserService userService;


    public String getMsgByRoomId(String strings){
        System.out.println("Controller run...");
        String[] ss = strings.split("&");
        Map<String,String> param = new HashMap<>();
        for (String s:ss){
            param.put(s.split("\\=")[0],s.split("=")[1]);
        }
        return userService.getMsgByRoomId(Integer.parseInt(param.get("id")),10).toString();
    }

    public String favicon(String param){
        System.out.println("favicon run...");
        return "asd";
    }

}
