package com.smk.dialogdemo.sevice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.smk.dialogdemo.R;
import com.smk.dialogdemo.views.BTCallFloatWindow;
import com.smk.dialogdemo.views.DialogFactory;
import com.smk.dialogdemo.views.SmallDialog;

public class DialogService extends Service {
    private static final String TAG = "DialogService";
    public static final String ACTION_DIALOG_CONTROL = "com.smk.service.ACTION_DIALOG_CONTROL";
    public static final String PARAM_CMD = "cmd";

    private static final int CMD_CALL_STATUS_OUTGOING = 1;
    private static final int CMD_CALL_STATUS_INCOMING = 2;
    private static final int CMD_CALL_STATUS_ACTIVE = 3;
    private static final int CMD_CALL_STATUS_TERMINATED = 4;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return START_NOT_STICKY;
        }
        String action = intent.getAction();
        if (ACTION_DIALOG_CONTROL.equals(action)) {
            int cmd = intent.getIntExtra(PARAM_CMD, -1);
            Log.i(TAG, "onStartCommand() CMD : " + cmd);
            switch (cmd) {
                case CMD_CALL_STATUS_OUTGOING:// 去電
//                    showSmallDialog();
                    showBTCallFloatWindow(true, BTCallFloatWindow.FLOAT_WINDOW_TYPE_DIALING, "张三", "13714461436");
                    break;
                case CMD_CALL_STATUS_INCOMING:// 來電
//                    dismissSmallDialog();
                    showBTCallFloatWindow(true, BTCallFloatWindow.FLOAT_WINDOW_TYPE_INCOMING, "张三", "13714461436");
                    break;
                case CMD_CALL_STATUS_ACTIVE:// 接通電話
                    showBTCallFloatWindow(true, BTCallFloatWindow.FLOAT_WINDOW_TYPE_ACTIVE, "张三", "13714461436");
                    break;
                case CMD_CALL_STATUS_TERMINATED:// 掛斷電話
                    dismissBTCallFloatWindow();
                    break;
                default:
                    Log.i(TAG, "Unkown Command !!!");
                    break;
            }

        }
        return START_NOT_STICKY;
    }

    private SmallDialog smallDialog;

    private void showSmallDialog() {
        if (null == smallDialog) {
            smallDialog = DialogFactory.createSmallDialog(this);
        }
        if (!smallDialog.isShowing()) {
            smallDialog.show();
            Log.i(TAG, "showSmallDialog() ...");
        }
    }

    private void dismissSmallDialog() {
        if (null != smallDialog && smallDialog.isShowing()) {
            smallDialog.dismiss();
            Log.i(TAG, "dismissSmallDialog() ...");
        }
    }

    private BTCallFloatWindow mBTCallFloatWindow;

    private void showBTCallFloatWindow(boolean isFullScreen, int callState, String phoneName, String phoneNumber) {
        if (null == mBTCallFloatWindow) {
            mBTCallFloatWindow = new BTCallFloatWindow(this);
        }

        String callStateStr = getString(R.string.float_window_call_state_unknown_text);
        int floatWindowMode = isFullScreen ? BTCallFloatWindow.FLOAT_WINDOW_MODE_FULL : BTCallFloatWindow.FLOAT_WINDOW_MODE_SMALL;

        switch (callState) {
            case BTCallFloatWindow.FLOAT_WINDOW_TYPE_ACTIVE:
                callStateStr = getString(R.string.float_window_call_state_active_text);
                break;
            case BTCallFloatWindow.FLOAT_WINDOW_TYPE_DIALING:
                callStateStr = getString(R.string.float_window_call_state_dialing_text);
                break;
            case BTCallFloatWindow.FLOAT_WINDOW_TYPE_INCOMING:
                callStateStr = getString(R.string.float_window_call_state_incoming_text);
                break;
        }
        mBTCallFloatWindow.setCallName(phoneName)
                .setCallNumber(phoneNumber)
                .setCallStatus(callStateStr)
                .setmCurrentFloatWindowMode(floatWindowMode)
                .setmCurrentFloatWindowType(callState)
                .refresh()
                .show();
    }

    private void dismissBTCallFloatWindow() {
        if (null == mBTCallFloatWindow) {
            return;
        }
        mBTCallFloatWindow.dismiss();
        Log.i(TAG, "dismissBTCallFloatWindow() ...");
    }
}
