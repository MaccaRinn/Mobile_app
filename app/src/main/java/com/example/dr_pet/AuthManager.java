package com.example.dr_pet;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
