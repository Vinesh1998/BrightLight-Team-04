package com.app.brightLightBookStore.model;

import androidx.annotation.NonNull;

public class User {
    private String id;
    private String name;
    private String email;
    private int age;
    private String address;
    private String mobile;
    private String gender;
    public User(){}
    public User(String id, String name, String email, String mobile, String address, String gender, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.mobile = mobile;
        this.gender = gender;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
