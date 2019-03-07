package com.semisky.parking.application;

import android.app.Application;
import android.content.Context;

import com.semisky.parking.utils.Logger;

/**
 * Created by Administrator on 2018/8/11.
 */

public class ParkingApplication extends Application {
    private static final String TAG = Logger.makeLogTag(ParkingApplication.class);
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i(TAG,"onCreate() ...");
        mContext = this.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
