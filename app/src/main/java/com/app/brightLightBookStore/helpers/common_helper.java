package com.app.brightLightBookStore.helpers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class common_helper {
    public static DecimalFormat df = new DecimalFormat("##");
    public static boolean getAdminLogin(String email, String password){
        return email.equals("admin@gmail.com") && password.equals("Admin@123");
    }
    public static String[] getGenres(){
        String[] genres = {"New collection","Adventure", "Biography", "Fantasy", "History", "Poetry", "Social science"};
        return  genres;
    }
    public static String[] getAdminGenres(){
        String[] genres = {"Adventure", "Biography", "Fantasy", "History", "Poetry", "Social science"};
        return  genres;
    }

    public static int getDays(String from_date, String to_date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        Date Date1 = null,Date2 = null;
        try{
            Date1 = sdf.parse(from_date);
            Date2 = sdf.parse(to_date);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return (int) ((Date2.getTime() - Date1.getTime())/(24*60*60*1000));
    }

}
