package com.guorui.demo.bean;

public class Event {

    private long timeStamp;
    private String sessionId;
    private int uid;

    public Event(long timeStamp, String sessionId, int uid) {
        this.timeStamp = timeStamp;
        this.sessionId = sessionId;
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
