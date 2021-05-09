package com.guorui.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class MyTomcat {

    @Autowired
    RequestMapping requestMapping;

    public static String readInputString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
            return result.toString("UTF-8");
        }
        return "";
    }
    public  void start(String[] args) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        System.out.println("MyTomcat执行");
        ServerSocket serverSocket = new ServerSocket(8010);
        Socket socket = null;
        MyRequest myRequest;
        while(true){
            socket = serverSocket.accept();
            System.out.println("socket run...");
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            myRequest = new MyRequest(inputStream);
            requestMapping.run(myRequest.getUrl(),myRequest.getParam(),outputStream);
            myRequest = null;
            inputStream.close();
            outputStream.close();
            socket.close();
        }
    }
}
