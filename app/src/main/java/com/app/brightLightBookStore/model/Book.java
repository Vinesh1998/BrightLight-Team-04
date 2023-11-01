package com.app.brightLightBookStore.model;

import androidx.annotation.NonNull;

public class Book {
    private String id;
    private String auth_name;
    private String book_name;
    private String shortDescription;
    private Double purchase_amt;
    private String image;
    private Double rental_amt;
    private String rating;
    private String published;
    private String genre;
    private int count;
    private int likes;
    private String created_date;

    public Book(){}

    public String getAuth_name() {
        return auth_name;
    }

    public void setAuth_name(String auth_name) {
        this.auth_name = auth_name;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Book(String id, String book_name, String auth_name, String genre, String image,
                Double purchase_amt, Double rental_amt, String shortDescription,
                String rating, String published, int count, int likes, String created_date) {
        this.id = id;
        this.auth_name = auth_name;
        this.book_name = book_name;
        this.shortDescription = shortDescription;
        this.image = image;
        this.rental_amt = rental_amt;
        this.purchase_amt = purchase_amt;
        this.rating = rating;
        this.count = count;
        this.genre = genre;
        this.published = published;
        this.created_date = created_date;
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPurchase_amt() {
        return purchase_amt;
    }

    public void setPurchase_amt(Double purchase_amt) {
        this.purchase_amt = purchase_amt;
    }

    public Double getRental_amt() {
        return rental_amt;
    }

    public void setRental_amt(Double rental_amt) {
        this.rental_amt = rental_amt;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
