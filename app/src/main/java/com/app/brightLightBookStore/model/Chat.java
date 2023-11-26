package com.app.brightLightBookStore.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.snapshot.StringNode;

public class Chat {
    private String id;
    private String msg;
    private String date;
    private String time;
    private String sender_id;
    private String sender_name;
    private String receiver_id;
    private String receiver_name;
    private boolean status;
    private boolean flag;

    public Chat(){}
    public Chat(String id, String msg, String date, String time, String sender_id, String sender_name,
                String receiver_id, String receiver_name, boolean status, boolean flag) {
        this.id = id;
        this.msg = msg;
        this.date = date;
        this.time = time;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.receiver_id = receiver_id;
        this.flag = flag;
        this.receiver_name = receiver_name;
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

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
