package com.guorui.demo.Service;

import com.guorui.demo.bean.*;
import com.guorui.demo.config.WebSocketConfig;
import com.guorui.demo.dao.UserDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
@Service
public class UserService implements UserDetailsService {

    @Autowired
    SimpMessagingTemplate messagingTemplate;


    @Autowired
    IMailServiceImpl mailService;

    public static int getUserIdCurrent(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = ((User)principal).getId();
        return id;
    }

    @Autowired
    UserDao userDao;


    public Result updateFontFamily(String fontFamily){
        Result result = new Result();
        int id = getUserIdCurrent();
        try {
            System.out.println(fontFamily+"" +id);
            userDao.updateFontFamily(fontFamily,id);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(403);
            result.setMsg("出现异常");
            return result;
        }
        result.setStatus(200);
        result.setMsg("修改成功");
        return result;
    }

    public Result getRoomInfor(int roomId){
        Result result = new Result();
        try{
            String userss = userDao.getRoomAuth(roomId);
            String[] users = userss.substring(1,userss.length()-1).split("\\|");
            MyFriend[] myFriends = userDao.getMyFriends(users);
            for (MyFriend myFriend:myFriends){
                if (WebSocketConfig.users.contains(myFriend.getId())){
                    myFriend.setOnline(true);
                }else{
                    myFriend.setOnline(false);
                }
            }
            result.setObj(myFriends);
            result.setMsg(userDao.getRoomName(roomId));
        }catch (Exception e){
            e.printStackTrace();
            result.setStatus(403);
            result.setMsg("获取房间信息出现异常");
        }
        return result;
    }

    public Result getAllInfor() {
        Result result = new Result();
        int id = getUserIdCurrent();
        try {
            result.setObj(this.userDao.getAllInfor(id));
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(403);
            result.setMsg("出现异常");
            return result;
        }
        result.setStatus(200);
        result.setMsg("获取成功");
        return result;
    }

    public Result resInfor(int fromUid, String finalMsg) {
        Result result = new Result();
        int toUid = getUserIdCurrent();

        //先判断是否有这个infor
            if (userDao.selectInfor(fromUid,toUid) == null){
                result.setMsg("不存在该消息！！！");
                return result;
            }
        try {
            this.userDao.updateInfor(fromUid, toUid, finalMsg);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(403);
            result.setMsg("出现异常");
            return result;
        }
        result.setStatus(200);
        result.setMsg("成功");
        int id1 = (fromUid > toUid) ? toUid : fromUid;
        int id2 = (fromUid > toUid) ? fromUid : toUid;
        String name = "|" + this.userDao.getMyName(id1) + "|" + this.userDao.getMyName(id2) + "|";
        if (this.userDao.getSmallRoom(name) != null) {
            result.setMsg("成功,房间已存在");
            return result;
        }
        this.userDao.addFriend(fromUid, toUid);
        this.userDao.addFriend(toUid, fromUid);
        String img = "|" + this.userDao.getMyImg(id1) + "|" + this.userDao.getMyImg(id2) + "|";
        String uids = "|" + id1 + "|" + id2 + "|";
        System.out.println("img:" + img + "uids:" + uids + "name:" + name);
        this.userDao.insertSmallRoom(name, uids, img, new Date());
        int roomId = userDao.getRoomIdByName(name);
        System.out.println("创建房间成功,发送第一条初始消息" + roomId);
        userDao.insertMsgByRoomId(roomId,toUid,new Date(),"我同意了你的好友请求",userDao.getMyImg(toUid),userDao.getMyName(toUid));
        WebSocketMessage message = new WebSocketMessage();
        message.setTimeStamp(System.currentTimeMillis());
        message.setContent("我同意了你的好友请求");
        message.setImg(userDao.getMyImg(toUid));
        message.setName(userDao.getMyName(toUid));
        message.setRoomId(roomId+"");
        message.setUid(toUid+"");
        messagingTemplate.convertAndSend("/topicHome/" + toUid,message);
        messagingTemplate.convertAndSend("/topicHome/" + fromUid,message);
        return result;
    }

    public Result sendFriendRequest(int toUid) {
        Result result = new Result();
        int fromUid = getUserIdCurrent();
        if (this.userDao.getInfor(fromUid, toUid) != null) {
            String msg = this.userDao.getInfor(fromUid, toUid).getFinalMsg();
            if (msg.equals(""))
                msg = "尚未响应";
            result.setMsg("好友申请已存在，对方"+ msg);
                    result.setStatus(200);
            return result;
        }
        try {
            this.userDao.insertInfor(fromUid, toUid);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(403);
            result.setMsg("发送失败，出现异常");
        }
        result.setStatus(200);
        result.setMsg("发送成功");
        WebSocketMessage message = new WebSocketMessage();
        message.setTimeStamp(System.currentTimeMillis());
        message.setContent("请求加为好友");
        message.setUid(fromUid+"");
        message.setName(userDao.getMyName(fromUid));
        messagingTemplate.convertAndSend("/topicHome/" + toUid,message);
        return result;
    }

    public Result updateMyImg(String img) {
        Result result = new Result();
        int uid = getUserIdCurrent();
        try {
            this.userDao.updateMyImg(uid,img);
            this.userDao.updateMyImg2(uid,img);
            String myName = userDao.getMyName(uid);
            MyRoom[] rooms = userDao.getMyRoomsByName("%|"+myName+"|%");
            String[] imgs;
            String[] names;
            for (MyRoom room : rooms){
                imgs = room.getImg().substring(1,room.getImg().length()-1).split("\\|");
                names = room.getName().substring(1,room.getName().length()-1).split("\\|");
                if (names[0].equals(myName)){
                    imgs[0] = img;
                }else{
                    imgs[1] = img;
                }
                room.setImg("|" + imgs[0] + "|" + imgs[1] + "|");
                userDao.updateRoomImg(room.getImg(),room.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(403);
            result.setMsg("修改失败，出现异常");
            return result;
        }
        result.setStatus(200);
        result.setMsg("修改成功");
        return result;
    }

    public Result getUsersRooms(String users,String rooms){
        Result result = new Result();
        System.out.println(userDao.getMyFriends(users.split("@")).toString());
        result.setObj(userDao.getMyFriends(users.split("@")));
        return result;
    }

    public Result register(String account,String name,String password,String email){
        //检测是否有账号存在
        System.out.println(password);
        Result result = new Result();
        if (userDao.getUserByAccount(account) != null){
            result.setMsg("账号已存在！");
            result.setStatus(403);
            return result;
        }
        int ac;
        try{
             ac = Integer.parseInt(account);
        }catch (Exception e){
            result.setMsg("账号只能是纯数字!");
            result.setStatus(403);
            return result;
        }
        int img = new Random().nextInt(100)+1;
        userDao.insertUser(ac,new BCryptPasswordEncoder().encode(password),name,img,email);
        userDao.insertUserToDefaultRoom(ac);
        result.setMsg("注册成功");
        result.setStatus(200);
        return result;
    }

    public Result getRooms(){
        Result result = new Result();
        try{
            int uid = getUserIdCurrent();
            System.out.println(uid);
            ArrayList<Room> rooms = userDao.getRooms(uid);
            System.out.println(rooms.toString());
            result.setObj(rooms);
            result.setMsg("获取成功");
            result.setStatus(200);
        }catch (Exception e){
            e.printStackTrace();
            result.setMsg("出现异常");
            result.setStatus(402);
        }
        return result;
    }

    //根据房间id拿msg信息
    public Result getMsgByRoomId(int id,int limit){
        Result result = new Result();
        int uid = getUserIdCurrent();
        System.out.println("uid:" + uid);
        String roomAuth = userDao.getRoomAuth(id);
        String[] auths = roomAuth.split("\\|");
        for (String auth:auths) {
            System.out.println(auth);
            if (auth.equals(uid + "")) {
                result.setObj(userDao.getMsgByRoomId(id,limit));
                result.setStatus(200);
                result.setMsg("获取成功!");
                break;
            }else{
                result.setMsg("没有权限访问!");
                result.setStatus(401);
            }
        }
        return result;
    }

    public void insertMsg(int roomId,int uid,String msg,String img,String name){
        userDao.insertMsgByRoomId(roomId,uid,new Date(),msg,img,name);
    }

    public Result insertMsgByRoomId(int id,String msg,String img,String name){
        Result result = new Result();
        int uid = getUserIdCurrent();
        String roomAuth = userDao.getRoomAuth(id);
        String[] auths = roomAuth.split("\\|");
        for (String auth:auths) {
            if (auth.equals(uid + "")) {
                userDao.insertMsgByRoomId(id,uid,new Date(),msg,img,name);
                result.setStatus(200);
                result.setMsg("发送成功");
                break;
            } else {
                result.setMsg("没有权限访问");
                result.setStatus(401);
            }
        }
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User use = userDao.getUserByAccount(s);
        if (use == null){
            throw new UsernameNotFoundException("用户名不存在");
        }else{
            return new User(use.getId(),use.getName(),use.getImg(),use.getAccount(),use.getPassword(),"ROLE_USER",
           userDao.getMyFriends(use.getFriends().split("\\|")),userDao.getMyRooms(use.getRooms().split("\\|")),use.getFontFamily());
        }
    }
}



