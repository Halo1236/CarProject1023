package com.semisky.btcarkit.service.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.semisky.btcarkit.aidl.BTConst;
import com.semisky.btcarkit.aidl.IBTRoutingCommand;
import com.semisky.btcarkit.aidl.ISmkCallbackA2dp;
import com.semisky.btcarkit.aidl.ISmkCallbackAvrcp;
import com.semisky.btcarkit.aidl.ISmkCallbackBluetooth;
import com.semisky.btcarkit.aidl.ISmkCallbackHfp;
import com.semisky.btcarkit.utils.Logutil;

import java.util.ArrayList;
import java.util.List;

public class BTRoutingCommandManager extends IBTRoutingCommand.Stub {
    private static final String TAG = Logutil.makeTagLog(BTRoutingCommandManager.class);
    private static final String ACTION_BT_SERVICE = "com.semisky.service.ACTION_BT_START";
    private static BTRoutingCommandManager _INSTANCE;
    private Context mContext;

    private IBTRoutingCommand mIBTRoutingCommand;
    private volatile List<OnServiceStateListener> mCallbacks = new ArrayList<OnServiceStateListener>();

    private BTRoutingCommandManager() {
    }

    public interface OnServiceStateListener {
        void onServiceConnected();
    }

    public BTRoutingCommandManager registerCallback(OnServiceStateListener listener) {
        Logutil.i(TAG, "registerCallback() ..." + listener);
        if (null != mCallbacks && !mCallbacks.contains(listener)) {
            synchronized (mCallbacks) {
                mCallbacks.add(listener);
            }
        }
        Logutil.i(TAG, "registerCallback() ..." + (null != mCallbacks ? mCallbacks.size() : 0));
        return this;
    }

    public void unregisterCallback(OnServiceStateListener listener) {
        if (null != mCallbacks && mCallbacks.contains(listener)) {
            synchronized (mCallbacks) {
                mCallbacks.remove(listener);
            }
        }
    }

    // 通知服务已连接
    void notifyServiceConnected() {
        Logutil.i(TAG, "notifyServiceConnected() ..." + (null != mCallbacks ? mCallbacks.size() : 0));
        if (null != mCallbacks && mCallbacks.size() > 0) {

            synchronized (mCallbacks) {
                for (OnServiceStateListener listener : mCallbacks) {
                    listener.onServiceConnected();
                }
            }
        }
    }

    public static BTRoutingCommandManager getInstance() {
        if (null == _INSTANCE) {
            synchronized (BTRoutingCommandManager.class) {
                if (null == _INSTANCE) {
                    _INSTANCE = new BTRoutingCommandManager();
                }
            }
        }
        return _INSTANCE;
    }

    /**
     * 启动蓝牙服务
     */
    public BTRoutingCommandManager startService() {
        if (!isBindContext()) {
            Logutil.e(TAG, "startService() Context is null !!!");
            return this;
        }
        mContext.startService(new Intent(ACTION_BT_SERVICE));
        return this;
    }

    /**
     * 绑定服务
     */
    public BTRoutingCommandManager bindService() {
        if (!isBindContext()) {
            return this;
        }
        if (isBindService()) {
            Logutil.e(TAG, "bindService() bound service !!!");
            return this;
        }
        Intent it = new Intent(ACTION_BT_SERVICE);
        mContext.bindService(it, mConn, Context.BIND_AUTO_CREATE);
        return this;
    }

    // 是否绑定服务
    boolean isBindService() {
        Logutil.i(TAG, "isBindService() ..." + (null != this.mIBTRoutingCommand));
        return (null != this.mIBTRoutingCommand);
    }

    /**
     * 解绑服务
     */
    public void unbindService() {
        if (!isBindContext()) {
            Logutil.e(TAG, "unbindService() Context is null !!!");
            return;
        }
        if (!isBindService()) {
            Logutil.e(TAG, "unbindService() Not bind service !!!");
            return;
        }
        mContext.unbindService(mConn);
    }

    // 是否绑定上下文
    boolean isBindContext() {
        return (null != this.mContext);
    }

    /**
     * 注入上下文
     *
     * @param ctx
     */
    public BTRoutingCommandManager onAttatch(Context ctx) {
        this.mContext = ctx;
        return this;
    }

    // 实现服务监听接口
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logutil.i(TAG, "onServiceConnected() ..." + name);
            mIBTRoutingCommand = IBTRoutingCommand.Stub.asInterface(service);
            notifyServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logutil.e(TAG, "onServiceDisconnected() ..." + name);
        }
    };

    /* Bluetooth-----------------------------------------------------------------------------------*/

    @Override
    public void registerBluetoothCallback(ISmkCallbackBluetooth callback) {
        if (isBindService()) {
            try {
                mIBTRoutingCommand.registerBluetoothCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterBluetoothCallback(ISmkCallbackBluetooth callback) {
        if (isBindService()) {
            try {
                mIBTRoutingCommand.unregisterBluetoothCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isBtEnable() {
        if (isBindService()) {
            try {
                return mIBTRoutingCommand.isBtEnable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void setBtEnable(boolean enable) {
        if (isBindService()) {
            try {
                mIBTRoutingCommand.setBtEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startBtDeviceDiscovery() {
        if (isBindService()) {
            try {
                mIBTRoutingCommand.startBtDeviceDiscovery();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqBtConnectHfpA2dp(String address) {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqBtConnectHfpA2dp(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqBtDisconnectAll() {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqBtDisconnectAll();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* HFP-----------------------------------------------------------------------------------*/

    @Override
    public void registerHfpCallback(ISmkCallbackHfp callback) {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.registerHfpCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterHfpCallback(ISmkCallbackHfp callback) {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.unregisterHfpCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getHfpState() {
        if (isBindService()) {
            try {
                return this.mIBTRoutingCommand.getHfpState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public boolean isHfpConnected() {
        if (isBindService()) {
            try {
                return this.mIBTRoutingCommand.isHfpConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void reqHfpDialCall(String phoneNumber) {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqHfpDialCall(phoneNumber);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqHfpTerminateCurrentCall() {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqHfpTerminateCurrentCall();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* A2DP-----------------------------------------------------------------------------------*/

    @Override
    public void registerA2dpCallback(ISmkCallbackA2dp callback) {
        Logutil.i(TAG, "registerA2dpCallback() ...");
        if (isBindService()) {
            try {
                Logutil.i(TAG, "registerA2dpCallback() ...OK");
                this.mIBTRoutingCommand.registerA2dpCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterA2dpCallback(ISmkCallbackA2dp callback) {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.unregisterA2dpCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getA2dpConnectionState() {
        if (isBindService()) {
            try {
                return this.mIBTRoutingCommand.getA2dpConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public boolean isA2dpConnected() {
        if (isBindService()) {
            try {
                return this.mIBTRoutingCommand.isA2dpConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /* AVRCP-----------------------------------------------------------------------------------*/

    @Override
    public void registerAvrcpCallback(ISmkCallbackAvrcp callback) {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.registerAvrcpCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterAvrcpCallback(ISmkCallbackAvrcp callback) {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.unregisterAvrcpCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getAvrcpMediaPlayState() {
        if (isBindService()) {
            try {
                return this.mIBTRoutingCommand.getAvrcpMediaPlayState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public void reqAvrcpBackward() {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqAvrcpBackward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqAvrcpForward() {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqAvrcpForward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqAvrcpPause() {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqAvrcpPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqAvrcpPlay() {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqAvrcpPlay();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqAvrcpPlayOrPause() {
        if (isBindService()) {
            try {
                this.mIBTRoutingCommand.reqAvrcpPlayOrPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBtMusicTitle() {
        if (isBindService()) {
            try {
                return this.mIBTRoutingCommand.getBtMusicTitle();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getBtMusicArtist() {
        if (isBindService()) {
            try {
                return this.mIBTRoutingCommand.getBtMusicArtist();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getBtMusicAlbum() {
        if (isBindService()) {
            try {
                return this.mIBTRoutingCommand.getBtMusicAlbum();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* PBAP-----------------------------------------------------------------------------------*/
}
