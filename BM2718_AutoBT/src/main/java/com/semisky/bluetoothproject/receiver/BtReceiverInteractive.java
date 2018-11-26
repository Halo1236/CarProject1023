package com.semisky.bluetoothproject.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.semisky.bluetoothproject.presenter.BtUICommandMethodCallback;
import com.semisky.bluetoothproject.utils.Logger;

import java.lang.reflect.Method;

public class BtReceiverInteractive extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();

    private final String ANDROID_START = "com.semisky.preStartHome";
    private final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static boolean isFirst = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        String action = intent.getAction();
        Logger.d(TAG, "InteractiveReceiver = " + action);

        if (action == null) {
            return;
        }

        if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            BluetoothDevice device =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT,
                    BluetoothDevice.ERROR);
            Logger.e(TAG, "Piggy Check type: " + type);
            if (type == BluetoothDevice.PAIRING_VARIANT_PASSKEY_CONFIRMATION) {
                Logger.e(TAG, "PAIRING_VARIANT_PASSKEY_CONFIRMATION");
                try {
                    device.setPairingConfirmation(true);
                    device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (type == BluetoothDevice.PAIRING_VARIANT_PIN) {
                Logger.e(TAG, "PAIRING_VARIANT_PIN");
                try {
                    setPin(device.getClass(), device, strPsw);
                    device.createBond();
                    device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Logger.e(TAG, "Unkown paring type" + type);
            }
        } else if ((action.equals(ANDROID_START) || action.equals(BOOT_COMPLETED))
                && isFirst) {
            isFirst = false;
            BtUICommandMethodCallback.getInstance().startBTMusicService(context);
        }
    }

    private static final String strPsw = "0000";

    public void setPin(Class btClass, BluetoothDevice btDevice,
                       String str) throws Exception {
        try {
            Method setPinMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Boolean returnValue = (Boolean) setPinMethod.invoke(btDevice,
                    new Object[]{str.getBytes()});
            Logger.e("returnValue", "" + returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
