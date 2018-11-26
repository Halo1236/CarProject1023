package com.semisky.bluetoothproject.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.res.NfDef;
import com.semisky.autoservice.manager.AutoConstants;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.utils.Logger;

/**
 * Created by chenhongrui on 2018/9/27
 * <p>
 * 内容摘要:用于下发状态给中间键
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtMiddleSettingManager {

    private static final String TAG = Logger.makeTagLog(BtMiddleSettingManager.class);

    private AutoManager autoManager;
    private Context context;

    private BtMiddleSettingManager() {
        context = BtApplication.getContext();
        autoManager = AutoManager.getInstance();
    }

    private static class MiddleSettingHolder {
        @SuppressLint("StaticFieldLeak")
        private static final BtMiddleSettingManager instance = new BtMiddleSettingManager();
    }

    public static BtMiddleSettingManager getInstance() {
        return MiddleSettingHolder.instance;
    }


    /**
     * 蓝牙连接状态回调
     *
     * @param address   address
     * @param prevState prevState
     * @param newState  newState
     *                  STATE_NOT_INITIALIZED</b> (int) 100
     *                  STATE_READY</b>			  (int) 110
     *                  STATE_CONNECTING</b>	  (int) 120
     *                  STATE_CONNECTED</b>		  (int) 140
     */
    public void onHfpStateChanged(String address, int prevState, int newState) {
        if (prevState == NfDef.STATE_CONNECTED && newState == NfDef.STATE_DISCONNECTING) {
            //断开中
            setBtConnectionState(AutoConstants.BtConnectionState.STATE_DISCONNECTING);
        } else if (prevState != NfDef.STATE_CONNECTED && newState == NfDef.STATE_CONNECTED) {
            //已连接
//            setBtConnectionState(AutoConstants.BtConnectionState.STATE_CONNECTED);//等全部连接成功再设置
        } else if (prevState == NfDef.STATE_READY && newState == NfDef.STATE_CONNECTING) {
            //连接中
            setBtConnectionState(AutoConstants.BtConnectionState.STATE_CONNECTING);
        } else if (prevState > NfDef.STATE_READY && newState == NfDef.STATE_READY) {
            //已断开
            setBtConnectionState(AutoConstants.BtConnectionState.STATE_DISCONNECTED);
        }
    }

    public void onHfpCallChanged(String address, NfHfpClientCall call) {
        int state = call.getState();
        switch (state) {
            case NfHfpClientCall.CALL_STATE_HELD://保留状态
                setBtIncallState(NfHfpClientCall.CALL_STATE_HELD);
                break;
            case NfHfpClientCall.CALL_STATE_INCOMING://来电
                setBtIncallState(NfHfpClientCall.CALL_STATE_INCOMING);
                break;
            case NfHfpClientCall.CALL_STATE_WAITING://第三方来电
                setBtIncallState(NfHfpClientCall.CALL_STATE_WAITING);
                break;
            case NfHfpClientCall.CALL_STATE_DIALING://去电
                setBtIncallState(NfHfpClientCall.CALL_STATE_DIALING);
                break;
            case NfHfpClientCall.CALL_STATE_ALERTING://拨号
                setBtIncallState(NfHfpClientCall.CALL_STATE_ALERTING);
                break;
            case NfHfpClientCall.CALL_STATE_ACTIVE://接通
                setBtIncallState(NfHfpClientCall.CALL_STATE_ACTIVE);
                break;
            case NfHfpClientCall.CALL_STATE_TERMINATED://挂断
//                setBtIncallState(NfHfpClientCall.CALL_STATE_TERMINATED);由于三方通话，单独处理
                break;
        }
    }

    public void setCallTerminated() {
        setBtIncallState(NfHfpClientCall.CALL_STATE_TERMINATED);
    }

    public void onAdapterStateChanged(int prevState, int newState) {
        if (newState == NfDef.BT_STATE_ON) {
            //蓝牙已开启
            setBtState(true);
        } else if (newState == NfDef.BT_STATE_OFF) {
            //蓝牙已关闭
        } else if (newState == NfDef.BT_STATE_TURNING_ON) {
            //正在开启蓝牙
        } else if (newState == NfDef.BT_STATE_TURNING_OFF) {
            //正在关闭蓝牙
            setBtState(false);
        }
    }

    /**
     * 设置蓝牙图标连接或未连接状态
     */
    public void setBtConnectionState(int state) {
        Logger.d(TAG, "setBtConnectionState: " + state);
        autoManager.setBtConnectionState(state);
    }

    public void setAppStatusInForeground(String name) {
        autoManager.setAppStatus(AutoConstants.PackageName.CLASS_BLUETOOTH,
                name, AutoConstants.AppStatus.RUN_FOREGROUND);
    }

    public void setAppStatusInBackground() {
        autoManager.setAppStatus(AutoConstants.PackageName.CLASS_BLUETOOTH,
                AutoConstants.defaultTitle, AutoConstants.AppStatus.RUN_BACKGROUND);
    }

    public void setAppStatusInDestroy() {
        autoManager.setAppStatus(AutoConstants.PackageName.CLASS_BLUETOOTH,
                context.getString(R.string.bt_app_name), AutoConstants.AppStatus.DESTROY);
    }

    public void setAppStatusInRequested() {
        autoManager.setAppStatus(AutoConstants.PackageName.CLASS_BLUETOOTH,
                context.getString(R.string.bt_app_name), AutoConstants.AppStatus.REQUESTED_AUDIOFOCUS);
    }

    public void setAppStatusInAbandon() {
        autoManager.setAppStatus(AutoConstants.PackageName.CLASS_BLUETOOTH,
                context.getString(R.string.bt_app_name), AutoConstants.AppStatus.ABANDON_AUDIOFOCUS);
    }

    /**
     * 设置蓝牙音乐前后台
     *
     * @param status true 前台
     */
    public void setBtMusicStatus(boolean status) {
        Log.d(TAG, "setBtMusicStatus: " + status);
        autoManager.setBtMusicStatus(status);
    }

    /**
     * 设置蓝牙开启状态
     */
    public void setBtState(boolean state) {
        Logger.d(TAG, "setBtState: " + state);
        if (state) {
            autoManager.setBtState(AutoConstants.BtState.STATE_ON);
        } else {
            autoManager.setBtState(AutoConstants.BtState.STATE_OFF);
        }
    }

    /**
     * 设置蓝牙通话状态
     */
    public void setBtIncallState(int callStateHeld) {
        Logger.d(TAG, "setBtIncallState: " + callStateHeld);
        autoManager.setBtIncallState(callStateHeld);
    }
}
