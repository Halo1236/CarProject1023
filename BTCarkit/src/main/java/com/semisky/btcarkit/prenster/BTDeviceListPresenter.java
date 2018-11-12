package com.semisky.btcarkit.prenster;

import android.os.RemoteException;

import com.semisky.btcarkit.aidl.BTConst;
import com.semisky.btcarkit.aidl.BTDeviceInfo;
import com.semisky.btcarkit.aidl.BTHfpClientCall;
import com.semisky.btcarkit.aidl.ISmkCallbackA2dp;
import com.semisky.btcarkit.aidl.ISmkCallbackBluetooth;
import com.semisky.btcarkit.aidl.ISmkCallbackHfp;
import com.semisky.btcarkit.service.manager.BTRoutingCommandManager;
import com.semisky.btcarkit.utils.Logutil;
import com.semisky.btcarkit.view.fragment.IBTDeviceListView;

import java.util.List;

public class BTDeviceListPresenter<V extends IBTDeviceListView> extends BasePresenter<V> {
    private static final String TAG = Logutil.makeTagLog(BTDeviceListPresenter.class);

    public BTDeviceListPresenter() {
        BTRoutingCommandManager.getInstance().registerCallback(mOnServiceStateListener);
        BTRoutingCommandManager.getInstance().registerBluetoothCallback(mISmkCallbackBluetooth);
        BTRoutingCommandManager.getInstance().registerHfpCallback(mISmkCallbackHfp);
        BTRoutingCommandManager.getInstance().registerA2dpCallback(mISmkCallbackA2dp);
    }

    private BTRoutingCommandManager.OnServiceStateListener mOnServiceStateListener = new BTRoutingCommandManager.OnServiceStateListener() {
        @Override
        public void onServiceConnected() {
            BTRoutingCommandManager.getInstance().registerBluetoothCallback(mISmkCallbackBluetooth);
            BTRoutingCommandManager.getInstance().registerHfpCallback(mISmkCallbackHfp);
            BTRoutingCommandManager.getInstance().registerA2dpCallback(mISmkCallbackA2dp);
        }
    };

    private ISmkCallbackBluetooth.Stub mISmkCallbackBluetooth = new ISmkCallbackBluetooth.Stub() {
        @Override
        public void onAdapterStateChanged(int oldState, int newState) throws RemoteException {

        }

        @Override
        public void onDeviceDiscoveryStarted() throws RemoteException {
            refreshScanDeviceState("开始扫描设备...");
        }

        @Override
        public void onDeviceFound(List<BTDeviceInfo> btDeviceInfos) throws RemoteException {
            refreshScanDeviceState("正在扫描 ...");
            refreshScanDeviceList(btDeviceInfos);
        }

        @Override
        public void onDeviceDiscoveryFinished() throws RemoteException {
            refreshScanDeviceState("扫描完成!!!");
        }
    };

    private ISmkCallbackHfp.Stub mISmkCallbackHfp = new ISmkCallbackHfp.Stub() {
        @Override
        public void onHfpStateChanged(String address, int oldState, int newState) throws RemoteException {
            Logutil.i(TAG, "onHfpStateChanged() oldState=" + oldState + " , newState = " + newState);
            if (oldState < BTConst.HFP_STATE_CONNECTED && newState == BTConst.HFP_STATE_CONNECTED) {
                Logutil.i(TAG, "HFP 已连接 ！！！");
                frefreshDeviceHfpState("HFP[已连接]");
            } else if (oldState >= BTConst.HFP_STATE_CONNECTED && newState <= BTConst.HFP_STATE_READY) {
                Logutil.i(TAG, "HFP 已断开 ！！！");
                frefreshDeviceHfpState("HFP[已断开]");
            }
        }

        @Override
        public void onHfpCallStateChanged(String address, BTHfpClientCall call) throws RemoteException {

        }
    };

    private ISmkCallbackA2dp.Stub mISmkCallbackA2dp = new ISmkCallbackA2dp.Stub() {
        @Override
        public void onA2dpStateChanged(int oldState, int newState) throws RemoteException {
            Logutil.i(TAG, "onA2dpStateChanged() oldState=" + oldState + " , newState = " + newState);
            if (oldState < BTConst.A2DP_STATE_CONNECTED && newState == BTConst.A2DP_STATE_CONNECTED) {
                Logutil.i(TAG, "A2DP 已连接 ！！！");
                frefreshDeviceA2dpState("A2DP[已连接]");
            } else if (oldState >= BTConst.A2DP_STATE_CONNECTED && newState <= BTConst.A2DP_STATE_READY) {
                frefreshDeviceA2dpState("A2DP[已断开]");
            }
        }
    };


    // 刷新扫描设备
    void refreshScanDeviceList(final List<BTDeviceInfo> btDeviceInfos) {
        _handler.post(new Runnable() {
            @Override
            public void run() {
                if (isBindView()) {
                    mViewRef.get().onRefreshDeviceList(btDeviceInfos);
                }
            }
        });
    }

    // 刷新扫描状态
    void refreshScanDeviceState(final String state) {
        _handler.post(new Runnable() {
            @Override
            public void run() {
                if (isBindView()) {
                    mViewRef.get().onScanStateChanged(state);
                }
            }
        });
    }

    // 设备HFP状态更新
    void frefreshDeviceHfpState(final String state) {
        _handler.post(new Runnable() {
            @Override
            public void run() {
                mViewRef.get().onNotifyHfpStateChanged(state);
            }
        });
    }

    // 设备HFP状态更新
    void frefreshDeviceA2dpState(final String state) {
        _handler.post(new Runnable() {
            @Override
            public void run() {
                mViewRef.get().onNotifyA2dpStateChanged(state);
            }
        });
    }

    @Override
    public void onDetachView() {
        BTRoutingCommandManager.getInstance().unregisterA2dpCallback(mISmkCallbackA2dp);
        BTRoutingCommandManager.getInstance().unregisterHfpCallback(mISmkCallbackHfp);
        BTRoutingCommandManager.getInstance().unregisterBluetoothCallback(mISmkCallbackBluetooth);
        BTRoutingCommandManager.getInstance().unregisterCallback(mOnServiceStateListener);
        super.onDetachView();
    }

    /**
     * 扫描蓝牙设备
     */
    public void reqStartBtDeviceDiscovery() {
        BTRoutingCommandManager.getInstance().startBtDeviceDiscovery();
    }

    /**
     * 断开当前连接设备
     */
    public void reqBtDisconnectAll() {
        BTRoutingCommandManager.getInstance().reqBtDisconnectAll();
    }

    /**
     * 请求连接新的蓝牙设备
     *
     * @param address
     */
    public void reqBtConnectHfpA2dp(String address) {
        boolean isHfpConnected = BTRoutingCommandManager.getInstance().isHfpConnected();
        Logutil.i(TAG, "reqBtConnectHfpA2dp() ... isHfpConnected=" + isHfpConnected);

        BTRoutingCommandManager.getInstance().reqBtConnectHfpA2dp(address);
    }
}
