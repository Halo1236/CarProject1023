package com.smk.bt.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.smk.bt.utils.Logger;


public class BTService extends Service {
    private static final String TAG = Logger.makeLogTag(BTService.class);
    private BTServiceImpl mBTServiceImpl;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i(TAG,"onCreate() ...");
        mBTServiceImpl = new BTServiceImpl(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i(TAG,"onStartCommand() ...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.i(TAG,"onBind() ...");
        return mBTServiceImpl;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.i(TAG,"onUnbind() ...");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mBTServiceImpl.onDestroy();
        super.onDestroy();
        Logger.i(TAG,"onDestroy() ...");
    }
}
