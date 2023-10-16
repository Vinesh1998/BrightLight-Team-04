package com.app.brightLightBookStore.helpers;

import java.text.DecimalFormat;

public class common_helper {
    public static DecimalFormat df = new DecimalFormat("##");
    public static boolean getAdminLogin(String email, String password){
        return email.equals("admin@gmail.com") && password.equals("admin123");
    }
}
