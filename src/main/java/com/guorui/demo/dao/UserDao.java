package com.guorui.demo.dao;

import com.guorui.demo.bean.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface UserDao {

    @Update("update user set fontFamily = #{fontFamily} where id = #{id}")
    void updateFontFamily(String fontFamily,int id);

    @Select("select * from user where mail = #{mail}")
    MyFriend selectUidByMail(String mail);

    @Select("select * from infor where fromUid = #{fromUid} and toUid = #{toUid}")
    Infor selectInfor(int fromUid,int toUid);

    @Update("update user set password=#{password} where id = #{id}")
    void rePsw(String password,int id);

    @Select("select code from user where id = #{id}")
    String selectCode(int id);

    @Update("update user set code = #{code} where id = #{id}")
    void updateCode(String code,int id);

    @Select({"select id,name,img from user where account = #{account}"})
    MyFriend getFriendByAccount(String account);

    @Select("select friends from user where id = #{id}")
    String getFriendsById(int id);

    @Select({"select id,img,account,name,finalMsg,isRead from user,(select * from infor where toUid = #{id})a where id = a.fromUid"})
    Infor[] getAllInfor(int id);

    @Select({"select img from user where id = #{id}"})
    String getMyImg(int id);

    @Select({"select name from user where id = #{id}"})
    String getMyName(int id);

    @Select({"select * from room where name = #{name}"})
    MyRoom getSmallRoom(String name);

    @Select("select id from room where name = #{name}")
    int getRoomIdByName(String name);

    @Insert({"insert into room(name,uids,img,lastDate) values(#{name},#{uids},#{img},#{lastDate})"})
    void insertSmallRoom(String name, String uids, String img, Date lastDate);

    @Update({"update user set friends = CONCAT(friends,concat('|',#{friendId})) where id = #{id}"})
    void addFriend(int friendId, int id);

    @Update({"update infor set isRead=1,finalMsg=#{finalMsg} where fromUid=#{fromUid} and toUid=#{toUid}"})
    void updateInfor(int fromUid, int toUid, String finalMsg);

    @Insert({"insert into infor(fromUid,toUid,isRead,finalMsg) values(#{fromUid},#{toUid},0,\"\")"})
    void insertInfor(int fromUid, int toUid);

    @Select({"select * from infor where fromUid = #{fromUid} and toUid = #{toUid}"})
    Infor getInfor(int fromUid, int toUid);

    @Update({"update message set img = #{img} where uid = #{uid};"})
    void updateMyImg2(int uid, String img);

    @Update({"update user set img = #{img} where id = #{uid}"})
    void updateMyImg(int uid, String img);

    @Select("select id,name,img from room where name like #{name}")
    MyRoom[] getMyRoomsByName(String name);

    @Update("update user set lastlogindate = #{date} where id = #{uid}")
    public void updateLastDate(Date date,int uid);

    @Select("select uids from room where id = #{roomId}")
    public String getRoomAuth(int roomId);

    @Select("select roomId,date,msg,uid,a.img,a.name,fontFamily from user,(\n" +
            "select * from \n" +
            "(select roomId,date,msg,uid,img,name from message where roomId\n" +
            " = #{roomId} order by message.date desc limit #{limit},15)a\n" +
            "order by a.date\n" +
            ")a where a.uid = user.id")
    public ArrayList<Message> getMsgByRoomId(int roomId,int limit);

    @Select("select * from user where account=#{account}")
    public User getUserByAccount(String account);

    @Insert("insert into user(account,password,name,img,mail,rooms,friends,fontFamily) values (#{account},#{password},#{name},#{img},#{mail},'1','1','SimHei;1;black')")
    public void insertUser(int account,String password,String name,int img,String mail);

    @Update("update room set img = #{img} where id = #{id}")
    public void updateRoomImg(String img,int id);

    @Select("select name from room where id = #{id}")
    public String getRoomName(int id);

    @Select("<script>\n" +
            "select id,name,img,account from user where id in \n" +
            "<foreach item='item' index='index' collection='array' open='(' separator=',' close=')'>\n" +
            "#{item}\n" +
            "</foreach>\n" +
            "</script>")
    public MyFriend[] getMyFriends(String[] userId);

    @Select("<script>\n" +
            "select id from user where id in \n" +
            "<foreach item='item' index='index' collection='array' open='(' separator=',' close=')'>\n" +
            "#{item}\n" +
            "</foreach>\n" +
            "</script>")
    public int[] getFriendsUids(String[] userId);

    @Select("<script>\n" +
            "select id,name,img from room where id in \n" +
            "<foreach item='item' index='index' collection='array' open='(' separator=',' close=')'>\n" +
            "#{item}\n" +
            "</foreach>\n" +
            "</script>")
    public MyRoom[] getMyRooms(String[] roomId);

    @Select("\n" +
            "select a.name,a.roomId,a.img,message.msg,a.last,a.tips from message,(\n" +
            "select a.name,a.id as roomId,a.img,msg,date,sum(message.date > b.lastlogindate) as tips ,max(message.date) as last from message,\n" +
            "(select name,room.id,img from room where uids LIKE CONCAT(CONCAT('%|', #{uid}),'|%')\n" +
            "order by lastDate desc)a,(select lastlogindate from user where user.id = #{uid})b\n" +
            "where a.id = message.roomId \n" +
            "group by roomId\n" +
            ")a where message.date = a.last order by last desc")
    public ArrayList<Room> getRooms(@Param("uid") int uid);

    @Insert("insert into message(uid,msg,roomId,notFinish,date,img,name) values(#{uid},#{msg},#{roomId},1,#{date},#{img},#{name})")
    public void insertMsgByRoomId(int roomId, int uid, Date date, String msg,String img,String name);

    @Update("update room set uids=CONCAT(uids,concat((select id from user where account = #{account}),'|')) where id = 1")
    public void insertUserToDefaultRoom(int account);

}
