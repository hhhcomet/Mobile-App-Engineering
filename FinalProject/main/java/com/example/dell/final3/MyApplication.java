package com.example.dell.final3;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

//written by xiuqi Ye

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}