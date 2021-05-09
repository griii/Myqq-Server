package com.guorui.demo.config;

import org.springframework.stereotype.Component;
 
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
 
/**
 * @author: wyh
 * @description:websocket服务端点，处理具体逻辑
 * @date: 12/22/2018 15:09
 */
@ServerEndpoint(value="/chat/{userid}")//该注解是一个类级别的注解，它的作用就是将当前类定义为一个websocket的服务器端，value的值表示访问路径。
@Component//将该类注册到spring容器中，个人理解这里并没有用到分层，所以不必使用@controller等分化后的注解
public class MySocketServer {
 
    private Session session;//记录当前连接，每个客户端都有对应的session，服务器端通过它向客户端发送消息
    //private static Map<String,Session> sessionPool = new HashMap<String,Session>();//存储所有userid及对应的session
    //private static Map<String,String> sessionIds = new HashMap<String,String>();//存储所有的sessionid和userid
    //因为HashMap是线程不安全的，所以我们使用concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MySocketServer> webSocketSet = new CopyOnWriteArraySet<MySocketServer>();//泛型就是当前类
    private static int onLineNum = 0;//静态变量，记录当前在线连接数，所有对在线数的操作都应加上synchhronized关键字以确保线程安全
    /**
     * @author wyh
     * @description  有新的链接建立成功时调用该方法
     * @param session
     * @param userid
     * @return void
     **/
    @OnOpen//该注解表示当有新用户连接时调用该方法
    public void open(Session session, @PathParam(value="userid")String userid){//@PathParam(value="userid")表示从访问路径上获取参数赋值给当前形参,value的值与路径上的名字一致
        this.session = session;//获取当前session
        webSocketSet.add(this);//将当前session加入到set中
        addOnlineNum();//在线数增加
    }
 
 
    /**
     * @author wyh
     * @description  接收到消息时调用该方法
     * @param message
     * @return void
     **/
    @OnMessage//该注解表示当服务器端收到客户端发送的消息时调用该方法
    public void onMessage(String message){
 
        System.out.println("当前发送人sessionid为"+session.getId()+",发送内容为："+message);
    }
 
 
 
    /**
     * @author wyh
     * @description  断开连接时调用该方法
     * @param
     * @return void
     **/
    @OnClose//该注解表示当前用户连接断开时调用该方法
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineNum();//在线数减1
    }
 
 
    /**
     * @author wyh
     * @description 出现错误时调用
     * @param session
     * @param error
     * @return void
     **/
    @OnError
    public void onError(Session session,Throwable error){
        System.out.println("发生错误："+error.getMessage()+",sessionid为"+session.getId());
        error.printStackTrace();
    }
 
 
 
    /**
     * @author wyh
     * @description  服务器端主动发送消息
     * @param message
     * @return void
     **/
    public void sendMessage(String message) throws IOException {
       this.session.getBasicRemote().sendText(message);
        System.out.println("发总给指定客户端，sessionid为："+this.session.getId()+"消息为："+message);
    }
 
 
 
    /**
     * @author wyh
     * @description  群发消息
     * @param message
     * @return void
     **/
    public void sendMessages(String message){
        for(MySocketServer s : webSocketSet){
            try{
                s.session.getBasicRemote().sendText(message);
                System.out.println("群发给sessionid为："+s.session.getId()+"的客户端,消息为："+message);
            }catch (IOException e){
                e.printStackTrace();
            }
 
        }
    }
 
 
 
    /**
     * @author wyh
     * @description  获取当前在线数,使用关键字
     * @param
     * @return int
     **/
    public synchronized int getOnlineNum(){
        return onLineNum;
    }
 
 
    /**
     * @author wyh
     * @description  在线数加1，注意关键字
     * @param
     * @return void
     **/
    public static synchronized void addOnlineNum(){
        onLineNum++;
    }
 
 
    /**
     * @author wyh
     * @description  在线数减1
     * @param
     * @return void
     **/
    public static synchronized void subOnlineNum(){
        onLineNum--;
    }
 
 
 
 
 
}