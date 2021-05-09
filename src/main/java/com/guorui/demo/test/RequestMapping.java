package com.guorui.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestMapping {

    @Autowired
    MyController myController;

    public  Map<String, Method> Mapping = new HashMap<>();

    public void run(String url,String param, OutputStream outputStream) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        System.out.println("requestMapping run..." + url + "param:" + param);
        Method method = Mapping.get(url);
        System.out.println(method.getName());
        if (param==null){
            System.out.println("param is null");
            param = "123";
        }
        outputStream.write(method.invoke(myController,param).toString().getBytes("GBK"));
    }

    public RequestMapping()
     {
        try {
            Mapping.put("/api/getMsgByRoomId", MyController.class.getMethod("getMsgByRoomId", String.class));
            Mapping.put("/favicon.ico",MyController.class.getMethod("favicon",String.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
