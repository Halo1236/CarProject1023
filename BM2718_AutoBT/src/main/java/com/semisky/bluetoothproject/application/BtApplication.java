package com.semisky.bluetoothproject.application;

import android.content.Context;

import com.semisky.bluetoothproject.presenter.BtUICommandMethodCallback;

import org.litepal.LitePalApplication;

public class BtApplication extends LitePalApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        BtUICommandMethodCallback.getInstance().startBTMusicService(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
