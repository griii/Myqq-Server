package com.guorui.demo.bean;

public class Infor {
  private int id;
  private String img;
  private int account;
  private String name;
  private String finalMsg;
  private Boolean isRead;
  private int fromUid;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getImg() {
    return this.img;
  }
  
  public void setImg(String img) {
    this.img = img;
  }
  
  public int getAccount() {
    return this.account;
  }
  
  public void setAccount(int account) {
    this.account = account;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getFinalMsg() {
    return this.finalMsg;
  }
  
  public void setFinalMsg(String finalMsg) {
    this.finalMsg = finalMsg;
  }
  
  public Boolean getRead() {
    return this.isRead;
  }
  
  public void setRead(Boolean read) {
    this.isRead = read;
  }
}
