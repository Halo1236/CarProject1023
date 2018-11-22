package com.smk.bt.model;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.smk.bt.utils.Logger;

import java.lang.reflect.Method;

public class BTPairModel {
    private static final String TAG = Logger.makeLogTag(BTPairModel.class);
    private static BTPairModel _INSTANCE;
    private static final String PAIR_PWD_DEF = "0000";

    private BTPairModel() {

    }

    public static BTPairModel getInstance() {
        if (null == _INSTANCE) {
            _INSTANCE = new BTPairModel();
        }
        return _INSTANCE;
    }

    public void handlerPairingRequest(Intent intent) {
        final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.ERROR);
        Logger.e(TAG, "handlerPairingRequest() type : " + type);

        // 用户将被提示确认屏幕上显示的密钥，或者应用程序将确认用户的密钥
        if (type == BluetoothDevice.PAIRING_VARIANT_PASSKEY_CONFIRMATION) {
            Logger.e(TAG, "handlerPairingRequest() PAIRING_VARIANT_PASSKEY_CONFIRMATION");
            try {
                device.setPairingConfirmation(true);
                device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //用户将被提示输入PIN或应用程序将为用户输入PIN
        else if (type == BluetoothDevice.PAIRING_VARIANT_PIN) {
            Logger.e(TAG, "handlerPairingRequest() PAIRING_VARIANT_PIN");
            try {
                Method setPinMethod = device.getClass().getDeclaredMethod("setPin", new Class[]{byte[].class});
                Boolean returnValue = (Boolean) setPinMethod.invoke(device.getClass(), new Object[]{PAIR_PWD_DEF.getBytes()});
                Logger.e(TAG, "returnValue : " + returnValue);
                device.createBond();
                device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);// 取消密钥框
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Logger.e(TAG, "Unkown paring type" + type);
        }
    }

}
