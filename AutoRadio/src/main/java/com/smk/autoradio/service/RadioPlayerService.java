package com.smk.autoradio.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RadioPlayerService extends Service {

    RadioPlayerServiceImpl mRadioPlayerServiceImpl;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mRadioPlayerServiceImpl = new RadioPlayerServiceImpl();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
