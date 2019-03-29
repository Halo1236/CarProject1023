package com.testservice.application;

import android.app.Application;
import android.content.Context;

public class BTApplication extends Application {
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = this.getApplicationContext();
    }

    public static Context getCtx(){
        return mCtx;
    }

    
}
