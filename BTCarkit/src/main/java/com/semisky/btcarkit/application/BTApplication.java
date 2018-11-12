package com.semisky.btcarkit.application;

import android.app.Application;

import com.semisky.btcarkit.service.manager.BTRoutingCommandManager;
import com.semisky.btcarkit.utils.Logutil;

public class BTApplication extends Application {
    private static final String TAG = Logutil.makeTagLog(BTApplication.class);

    @Override
    public void onCreate() {
        super.onCreate();
        Logutil.i(TAG,"onCreate() ...");
        startBtService();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Logutil.i(TAG,"onTerminate() ...");
        unbindBtService();

    }

    private void startBtService(){
        BTRoutingCommandManager
                .getInstance()
                .onAttatch(this)
                .startService()
                .bindService();
    }

    private void unbindBtService(){
        BTRoutingCommandManager.getInstance().unbindService();
    }

}
