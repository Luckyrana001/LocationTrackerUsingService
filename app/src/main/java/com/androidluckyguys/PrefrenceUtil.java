package com.androidluckyguys;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lucky on 30/01/2017.
 */

public class PrefrenceUtil {

    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime){
        SharedPreferences sharedPreference = context.getSharedPreferences("Pref", MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }
    public static boolean isFirstTimeAskingPermission(Context context, String permission){
        return context.getSharedPreferences("Pref", MODE_PRIVATE).getBoolean(permission, true);
    }
}

