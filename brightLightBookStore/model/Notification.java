package com.app.brightLightBookStore.model;

import androidx.annotation.NonNull;

public class Notification {
    private String id;
    private String name;
    private String book_name;
    private Double amt;
    private String date;
    private boolean marked;

    public Notification(){}
    public Notification(String id, String name, String book_name, Double amt, String date, boolean marked) {
        this.id = id;
        this.name = name;
        this.book_name = book_name;
        this.amt = amt;
        this.date = date;
        this.marked = marked;
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


    public String getPublished() {
        return date;
    }

    public void setPublished(String published) {
        this.date = published;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
