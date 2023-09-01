package com.app.brightLightBookStore.model;

import androidx.annotation.NonNull;

public class ChatUsers {
    private String id;
    private String msg;
    private String date;
    private String time;
    private String user_name;
    private boolean status;

    public ChatUsers(){}
    public ChatUsers(String id, String msg, String date, String time, String user_name,boolean status) {
        this.id = id;
        this.msg = msg;
        this.date = date;
        this.time = time;
        this.user_name = user_name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublished() {
        return date;
    }

    public void setPublished(String published) {
        this.date = published;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
