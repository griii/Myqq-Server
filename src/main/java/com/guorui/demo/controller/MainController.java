package com.guorui.demo.controller;

import com.guorui.demo.Service.UserService;
import com.guorui.demo.bean.Message;
import com.guorui.demo.bean.MyFriend;
import com.guorui.demo.bean.Result;
import com.guorui.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;


@Controller
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;


    @ResponseBody
    @RequestMapping("updateFontFamily")
    public Result updateFontFamily(String fontFamily){
        return userService.updateFontFamily(fontFamily);
    }

    @ResponseBody
    @RequestMapping("/getRoomInfor")
    public Result getRoomInfor(int roomId){
        return userService.getRoomInfor(roomId);
    }

    @ResponseBody
    @RequestMapping("/rePsw")
    public Result rePsw(String password,String code,String mail){
        Result result = new Result();
        try{
            int id = userDao.selectUidByMail(mail).getId();
            String codeDao = userDao.selectCode(id);
            if (codeDao == null){
                result.setStatus(403);
                result.setMsg("请先获取验证码!");
                return result;
            }
            System.out.println("code:" + code + "codeDao:" + codeDao);
            if (codeDao.equals(code)){
                userDao.rePsw(new BCryptPasswordEncoder().encode(password),id);
                result.setMsg("修改成功");
                result.setStatus(200);
            }else{
                result.setStatus(403);
                result.setMsg("验证码错误!");
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
            result.setStatus(403);
            result.setMsg("处理失败,出现异常!");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping({"/getFriendByAccount"})
    public MyFriend getFriendByAccount(String account) {
        return userDao.getFriendByAccount(account);
    }

    @ResponseBody
    @RequestMapping({"/resInfor"})
    public Result resInfor(int fromUid, String finalMsg) {
        return this.userService.resInfor(fromUid, finalMsg);
    }

    @ResponseBody
    @RequestMapping({"/sendFriendRequest"})
    public Result sendFriendRequest(int toUid) {
        return this.userService.sendFriendRequest(toUid);
    }

    @ResponseBody
    @RequestMapping({"/getAllInfor"})
    public Result getAllInfor() {
        return this.userService.getAllInfor();
    }

    @ResponseBody
    @RequestMapping({"/updateMyImg"})
    public Result updateMyImg(String img) {
        return this.userService.updateMyImg(img);
    }


    @ResponseBody
    @RequestMapping("/register")
    public Result register(String account,String name,String password,String email){
        return userService.register(account,name,password,email);
    }

    @ResponseBody
    @RequestMapping("/getRooms")
    public Result getRoomIdByNames(String[] names){
        return userService.getRooms();
    }

    @ResponseBody
    @GetMapping("/getMsgByRoomId")
    public Result getMsgByRoomId(int id,int page){
        return userService.getMsgByRoomId(id,(page-1)*15);
    }

    @ResponseBody
    @RequestMapping("/session/invalid")
    public String sessionInvalid(){
        return "登录已过期,请重新登录";
    }

    @ResponseBody
    @RequestMapping("/getUsersRooms")
    public Result getUsersRooms(String users,String rooms){
        return userService.getUsersRooms(users,rooms);
    }

    @ResponseBody
    @RequestMapping("/loadImg")
    public Result loadImg(@RequestParam("file") MultipartFile file){
        String name = System.currentTimeMillis() + "" + file.getSize();
        try {
            System.out.println("写入" + name);
            OutputStream out = new FileOutputStream("C:\\nginx1\\myqq\\img\\" + name + ".jpg");
            out.write(file.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("erro!!!!");
            e.printStackTrace();
        }
        Result result = new Result();
        result.setMsg(name);
        return result;
    }

    @ResponseBody
    @RequestMapping("/loadFile")
    public Result loadFile(@RequestParam("file") MultipartFile file){
        String d = System.currentTimeMillis() + "";
        String fn = file.getOriginalFilename();

        try {
            System.out.println("写入");
            System.out.println("C:\\nginx1\\myqq\\file\\" + d + "\\" + fn);
            File dir = new File("C:\\nginx1\\myqq\\file\\" + d);
            dir.mkdir();
            OutputStream out = new FileOutputStream("C:\\nginx1\\myqq\\file\\" + d +"\\" + fn);
            out.write(file.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("erro!!!!");
            e.printStackTrace();
        }
        Result result = new Result();
        result.setMsg(d+"\\" + fn);
        System.out.println(result.getMsg());
        return result;
    }

//    @ResponseBody
//    @RequestMapping("/insertMsgByRoomId")
//    public Result insertMsgByRoomId(int id,String msg){
//        return userService.insertMsgByRoomId(id,msg);
//    }
}
