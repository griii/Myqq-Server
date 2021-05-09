package com.guorui.demo.dao;

import com.guorui.demo.bean.Message;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MassegeDao {

    @Select("select is as uid,user.name as admin,msg,img as adminImg from (select * from message where roomId = #{roomId})a,user where user.uid = a.uid")
    public ArrayList<Message> getMsg(int roomId);

}
