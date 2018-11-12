package com.semisky.btcarkit.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.semisky.btcarkit.utils.Logutil;

public class BTService extends Service {
    private static final String TAG = Logutil.makeTagLog(BTService.class);
    private BTRoutingCommandImpl mBTRoutingCommandImpl;

    @Override
    public void onCreate() {
        super.onCreate();
        Logutil.i(TAG,"========onCreate()========");
        this.mBTRoutingCommandImpl = new BTRoutingCommandImpl();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logutil.i(TAG,"========onBind()========");
        return mBTRoutingCommandImpl;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logutil.i(TAG,"========onStartCommand()========");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logutil.i(TAG,"========onUnbind()========");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Logutil.i(TAG,"========onDestroy()========");
        if(null != mBTRoutingCommandImpl){
            mBTRoutingCommandImpl.onRelease();
        }
        super.onDestroy();
    }
}
