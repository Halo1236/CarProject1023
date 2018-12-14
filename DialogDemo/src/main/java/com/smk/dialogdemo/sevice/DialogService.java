package com.smk.dialogdemo.sevice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.smk.dialogdemo.views.BTCallFloatWindow;
import com.smk.dialogdemo.views.DialogFactory;
import com.smk.dialogdemo.views.SmallDialog;

public class DialogService extends Service {
    private static final String TAG = "DialogService";
    public static final String ACTION_DIALOG_CONTROL = "com.smk.service.ACTION_DIALOG_CONTROL";
    public static final String PARAM_CMD = "cmd";

    private static final int CMD_CALL_STATUS_OUTGOING = 1;
    private static final int CMD_CALL_STATUS_INCOMING = 2;
    private static final int CMD_CALL_STATUS_ACTIVI = 3;


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
            switch (cmd) {
                case CMD_CALL_STATUS_OUTGOING:
//                    showSmallDialog();
                    showBTCallFloatWindow();
                    break;
                case CMD_CALL_STATUS_INCOMING:
//                    dismissSmallDialog();
                    dismissBTCallFloatWindow();
                    break;
                case CMD_CALL_STATUS_ACTIVI:
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

    private void showBTCallFloatWindow() {
        if (null == mBTCallFloatWindow) {
            mBTCallFloatWindow = new BTCallFloatWindow(this);
        }


        if (!mBTCallFloatWindow.isShowing()) {
            mBTCallFloatWindow.show();
            Log.i(TAG, "showBTCallFloatWindow() ...");
        }
    }

    private void dismissBTCallFloatWindow() {
        if (null == mBTCallFloatWindow) {
            return;
        }
        if (mBTCallFloatWindow.isShowing()) {
            mBTCallFloatWindow.dismiss();
            Log.i(TAG, "dismissBTCallFloatWindow() ...");
        }
    }
}
