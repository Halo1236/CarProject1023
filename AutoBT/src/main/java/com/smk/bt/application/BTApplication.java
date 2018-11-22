package com.smk.bt.application;

import android.app.Application;

import com.smk.bt.service.manager.BTServiceProxyManager;
import com.smk.bt.utils.Logger;

public class BTApplication extends Application {
    private static final String TAG = Logger.makeLogTag(BTApplication.class);

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v(TAG,"onCreate() ...");
        BTServiceProxyManager.getInstance().init(this.getApplicationContext()).bindBTService();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.e(TAG,"onLowMemory() ...");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Logger.e(TAG,"onTerminate() ...");
    }
}
