package com.semisky.bluetoothproject.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.iflytek.adapter.ttsservice.ITtsClientListener;
import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.utils.Logger;

/**
 * Created by chenhongrui on 2018/7/26
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtTtsClientModel implements ITtsClientListener {

    private static String TAG = Logger.makeTagLog(BtTtsClientModel.class);
    private Context context;

    private BtTtsClientModel() {
        this.context = BtApplication.getContext();
    }

    private static class BtTtsClientModelHolder {
        @SuppressLint("StaticFieldLeak")
        private static final BtTtsClientModel instance = new BtTtsClientModel();
    }

    public static BtTtsClientModel getInstance() {
        return BtTtsClientModelHolder.instance;
    }

    @Override
    public void onPlayBegin() {

    }

    @Override
    public void onPlayCompleted() {

    }

    @Override
    public void onPlayInterrupted() {

    }

    @Override
    public void onProgressReturn(int i, int i1) {

    }

    @Override
    public void onTtsInited(boolean b, int i) {

    }
}
