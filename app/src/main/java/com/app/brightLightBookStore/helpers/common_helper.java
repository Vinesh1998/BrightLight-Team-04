package com.app.brightLightBookStore.helpers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class common_helper {
    public static DecimalFormat df = new DecimalFormat("##");
    public static boolean getAdminLogin(String email, String password){
        return email.equals("admin@gmail.com") && password.equals("Admin@123");
    }

    public static String[] getGenres(){
        String[] genres = {"Adventure", "Biography", "Fantasy", "History", "Poetry", "Social science"};
        return  genres;
    }
//    public static List<String> getGenres()
//    {
//        List<String> geners = new ArrayList<>();
//        geners.add("");
//        geners.add("");
//        geners.add("");
//        geners.add("");
//        geners.add("");
//        return geners;
//    }
}
