package com.yucelt.moviedb.utilities;

import android.annotation.SuppressLint;

import com.yucelt.moviedb.ui.MainActivity;

public class Config {
    public static String TAG = "Config";
    @SuppressLint("StaticFieldLeak")
    private static MainActivity contextMainActivity;

    public static MainActivity getContextMainActivity() {
        return contextMainActivity;
    }

    public static void setContextMainActivity(MainActivity contextMainActivity) {
        Config.contextMainActivity = contextMainActivity;
    }
}
