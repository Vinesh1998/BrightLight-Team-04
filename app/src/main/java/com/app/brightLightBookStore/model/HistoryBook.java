package com.app.brightLightBookStore.model;

import androidx.annotation.NonNull;

public class HistoryBook {
    private String id;
    private String book_id;
    private String user_id;
    private String auth_name;
    private String name;
    private Double purchase_amt;
    private String image;
    private Double rental_amt;
    private String purchase_date;
    private int cart_qty;
    private int days;
    private boolean flag_return;

    public HistoryBook(){}
    public HistoryBook(String id, String user_id, String book_id, String auth_name, String book_name,
                       String image, Double purchase_amt, Double rental_amt, String purchase_date,
                       int cart_qty, int days, boolean flag_return)
    {
        this.id = id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.auth_name = auth_name;
        this.name = book_name;
        this.cart_qty = cart_qty;
        this.purchase_date = purchase_date;
        this.image = image;
        this.rental_amt = rental_amt;
        this.days = days;
        this.purchase_amt = purchase_amt;
        this.flag_return = flag_return;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuth_name() {
        return auth_name;
    }

    public void setAuth_name(String auth_name) {
        this.auth_name = auth_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCart_qty() {
        return cart_qty;
    }

    public void setCart_qty(int cart_qty) {
        this.cart_qty = cart_qty;
    }

    public Double getPurchase_amt() {
        return purchase_amt;
    }

    public void setPurchase_amt(Double purchase_amt) {
        this.purchase_amt = purchase_amt;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public Double getRental_amt() {
        return rental_amt;
    }

    public void setRental_amt(Double rental_amt) {
        this.rental_amt = rental_amt;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isFlag_return() {
        return flag_return;
    }

    public void setFlag_return(boolean flag_return) {
        this.flag_return = flag_return;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
