package com.guorui.demo.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class User implements UserDetails {

    private int id;
    private String name;
    private String img;
    private String account;
    private String password;
    private String role;
    private String friends;
    private String rooms;
    private MyFriend[] friendsList;
    private MyRoom[] roomsList;
    private String fontFamily;

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    private Collection<? extends GrantedAuthority> authorities;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }




    public User(int id, String name, String img, String account, String password, String role,MyFriend[] friends,MyRoom[] rooms,String fontFamily) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.account = account;
        this.password = password;
        this.role = role;
        this.friendsList = friends;
        this.roomsList = rooms;
        this.fontFamily = fontFamily;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", friends='" + friends + '\'' +
                ", rooms='" + rooms + '\'' +
                ", friendsList=" + Arrays.toString(friendsList) +
                ", roomsList=" + Arrays.toString(roomsList) +
                ", authorities=" + authorities +
                '}';
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public MyFriend[] getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(MyFriend[] friendsList) {
        this.friendsList = friendsList;
    }

    public MyRoom[] getRoomsList() {
        return roomsList;
    }

    public void setRoomsList(MyRoom[] roomsList) {
        this.roomsList = roomsList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role));
        System.out.println("获取用户权限..." + authorities);
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(){

    }


    public User(int id, String name, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
