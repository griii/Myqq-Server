package com.guorui.demo.config;

import com.guorui.demo.bean.User;
import com.guorui.demo.dao.UserDao;
import org.apache.commons.collections.list.SynchronizedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.HandshakeRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
 
//    这个方法的作用是添加一个服务端点，来接收客户端的连接。
//    registry.addEndpoint("/socket")表示添加了一个/socket端点，客户端就可以通过这个端点来进行连接。
//    withSockJS()的作用是开启SockJS支持，

    public static ArrayList<Integer> users = new ArrayList<>();

    @Autowired
    UserDao userdao;

    @EventListener
    public void connectedEvent(SessionConnectedEvent event) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("接入...");
        event.getTimestamp();
        Principal principal = event.getUser();
        Method method = principal.getClass().getDeclaredMethod("getPrincipal");
        int id = ((User)(method.invoke(event.getUser(),null))).getId();
        this.users.add(id);
        System.out.println("由用户接入连接..."+   "当前用户总数" + users.toString());
    }
    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        System.out.println("退出...");
        Principal principal = event.getUser();
        Method method = principal.getClass().getDeclaredMethod("getPrincipal");
        int id = ((User)(method.invoke(event.getUser(),null))).getId();
        System.out.println("退出...");
        int index=0;
        Boolean is = false;
        for (;index<users.size();index++){
            if (users.get(index) == id){
                is = true;
                break;
            }
        }
        if (is)
        this.users.remove(index);
        userdao.updateLastDate(new Date(),id);
        System.out.println("由用户退出连接...当前用户总数" + users.toString());
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins("https://www.guorii.cn").withSockJS();
        //registry.addEndpoint("/chat").setAllowedOrigins("http://localhost:8081").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //表示客户端订阅地址的前缀信息，也就是客户端接收服务端消息的地址的前缀信息
        String[] strings = new String[]{"/topicHome","/topicChat","/topicRTC","/topicLog"};
        registry.enableSimpleBroker(strings);
        //指服务端接收地址的前缀，意思就是说客户端给服务端发消息的地址的前缀
        registry.setApplicationDestinationPrefixes("/app");
    }
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}