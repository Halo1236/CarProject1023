package com.smk.autoradio.application;

import android.app.Application;
import android.content.Context;

public class RadioApplication extends Application {
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = this.getApplicationContext();
    }

    public static Context getContext(){
        return mCtx;
    }
}
