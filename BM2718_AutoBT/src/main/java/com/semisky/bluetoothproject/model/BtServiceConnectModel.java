package com.semisky.bluetoothproject.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.manager.BtServiceManager;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;
import com.semisky.bluetoothproject.presenter.BtUICommandMethodCallback;
import com.semisky.bluetoothproject.responseinterface.Cx62BtIServiceResponse;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;

/**
 * Created by chenhongrui on 2018/7/26
 * <p>
 * 内容摘要: 蓝牙服务连接管理
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtServiceConnectModel implements Cx62BtIServiceResponse {

    private static String TAG = Logger.makeTagLog(BtServiceConnectModel.class);
    private BtBaseUiCommandMethod btBaseUiCommandMethod;
    private Context context;

    private BtServiceConnectModel() {
        this.context = BtApplication.getContext();
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
    }

    private static class BtServiceConnectModelHolder {
        @SuppressLint("StaticFieldLeak")
        private static final BtServiceConnectModel instance = new BtServiceConnectModel();
    }

    public static BtServiceConnectModel getInstance() {
        return BtServiceConnectModelHolder.instance;
    }

    @Override
    public void onServiceConnected() {
        boolean autoConnStateSP = BtSPUtil.getInstance().getAutoConnStateSP(context);
        btBaseUiCommandMethod.setAutoConnect(autoConnStateSP);
        //设置蓝牙设备搜索显示
        btBaseUiCommandMethod.setBtDiscoverableTimeout(0);
        boolean btEnabled = btBaseUiCommandMethod.isBtEnabled();
        Logger.d(TAG, "setBtEnable: btEnabled " + btEnabled);
        if (!btEnabled) {
            boolean b = btBaseUiCommandMethod.setBtEnable(true);
            Logger.d(TAG, "setBtEnable:setBtEnable " + b);
        }

        //设置SystemUI蓝牙图标
        BtMiddleSettingManager.getInstance().setBtState(true);
        Logger.d(TAG, ":onServiceConnected1 ");
        BtServiceManager.getInstance().startService();
        Logger.d(TAG, ":onServiceConnected2 ");
    }
}
