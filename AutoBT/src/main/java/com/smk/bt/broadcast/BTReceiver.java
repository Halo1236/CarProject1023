package com.smk.bt.broadcast;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smk.bt.model.BTPairModel;
import com.smk.bt.utils.Logger;

public class BTReceiver extends BroadcastReceiver {
    private static final String TAG = Logger.makeLogTag(BTReceiver.class);
    private final String ACTION_ANDROID_START = "com.semisky.preStartHome";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Logger.v(TAG, "onReceive() action : " + action);
        // 预先启动蓝牙服务
         if(ACTION_ANDROID_START.equals(action) || Intent.ACTION_BOOT_COMPLETED.equals(action)){

        }
        // 蓝牙配对意图处理
        else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
             BTPairModel.getInstance().handlerPairingRequest(intent);
         }
    }
}
