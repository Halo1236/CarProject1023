package com.smkbt.application;

import android.app.Application;
import android.content.Context;

import com.smkbt.manager.BTRoutingServiceManager;

public class BTApplication extends Application {
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = this.getApplicationContext();

        // Bind Romate bluetooth service
        BTRoutingServiceManager
                .getInstance()
                .attatchCtx(mCtx)
                .startService()
                .bindService();
    }

    public static Context getCtx(){
        return mCtx;
    }
}
