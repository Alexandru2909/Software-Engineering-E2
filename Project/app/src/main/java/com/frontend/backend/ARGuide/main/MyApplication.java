package com.frontend.backend.ARGuide.main;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;
    public static String path;
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}