package com.techamove.Firebase;

import android.app.Application;


public class MyApplication extends Application {

    public static final String TAG = "MyApplication";
    private static MyApplication mInstance;

    public static final boolean DEVELOPER_MODE = false;


    @Override
    public void onCreate() {
        super.onCreate();

    }





    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


}
