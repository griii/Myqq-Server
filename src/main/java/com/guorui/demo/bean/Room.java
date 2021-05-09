package com.guorui.demo.bean;

import java.util.ArrayList;
import java.util.Date;

public class Room {

    private String name;
    private String msg;
    private int roomId;
    private Date lastDate;

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                ", roomId=" + roomId +
                ", lastDate=" + lastDate +
                ", img='" + img + '\'' +
                ", tips=" + tips +
                '}';
    }

    private String img;
    private int tips;


    public int getTips() {
        return tips;
    }

    public void setTips(int tips) {
        this.tips = tips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
