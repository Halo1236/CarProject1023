package com.smk.autoradio.application;

import android.app.Application;
import android.content.Context;

import com.smk.autoradio.model.RadioProxyModel;
import com.smk.autoradio.utils.Logutil;

public class RadioApplication extends Application {
    private static final String TAG = Logutil.makeTagLog(RadioApplication.class);
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = this.getApplicationContext();
        initBindRadioService();
    }

    public static Context getContext(){
        return mCtx;
    }

    private void initBindRadioService(){
        Logutil.i(TAG,"initBindRadioService() ...");
        RadioProxyModel.getInstance().init(mCtx).bindRadioService();
    }
}
