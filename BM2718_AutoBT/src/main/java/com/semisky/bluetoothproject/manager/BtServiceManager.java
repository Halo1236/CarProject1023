package com.semisky.bluetoothproject.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.semisky.bluetoothproject.IBtMethodInterface;
import com.semisky.bluetoothproject.IBtStatusListener;
import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.entity.CallLogEntity;
import com.semisky.bluetoothproject.entity.ContactsEntity;
import com.semisky.bluetoothproject.service.BtLocalService;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;

import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import static com.semisky.bluetoothproject.constant.BtConstant.ACTION_BT_START;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtServiceManager {

    private static final String TAG = Logger.makeTagLog(BtServiceManager.class);

    private Context mContext;

    private IBtMethodInterface btMethodInterface;
    private boolean isBindService;

    private BtServiceManager() {
        mContext = BtApplication.getContext();
    }

    public static BtServiceManager getInstance() {
        return BtServiceManagerHolder.INSTANCE;
    }

    private static class BtServiceManagerHolder {
        private static final BtServiceManager INSTANCE = new BtServiceManager();
    }

    public void startService() {
        Log.d(TAG, "reqBtConnectHfpA2dp:startService ");
        Intent startIntent = new Intent(mContext, BtLocalService.class);
        startIntent.setAction(ACTION_BT_START);
        mContext.startService(startIntent);
    }

    public void bindService() {
        Intent bindIntent = new Intent(mContext, BtLocalService.class);
        bindIntent.setAction(ACTION_BT_START);
        isBindService = mContext.bindService(bindIntent, conn, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        if (isBindService) {
            mContext.unbindService(conn);
            isBindService = false;
        }
        btMethodInterface = null;
    }

    private onServiceConnectedListener onServiceConnectedListener;

    public interface onServiceConnectedListener {
        void onServiceConnected();
    }

    public void setOnServiceConnectedListener(onServiceConnectedListener listener) {
        this.onServiceConnectedListener = listener;
    }

    private boolean isBindProxyService() {
        return (null != btMethodInterface);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            btMethodInterface = IBtMethodInterface.Stub.asInterface(service);
            if (onServiceConnectedListener != null) {
                onServiceConnectedListener.onServiceConnected();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mContext = null;
            btMethodInterface = null;
        }
    };

    /**
     * 开始搜索设备
     */
    public void startBtDiscovery() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.startBtDiscovery();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止搜索设备
     */
    public void cancelBtDiscovery() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.cancelBtDiscovery();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拨打电话
     *
     * @param number 电话
     */
    public void reqHfpDialCall(String number) {
        if (isBindProxyService()) {
            try {
                btMethodInterface.reqHfpDialCall(number);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙连接
     *
     * @param address address
     */
    public void reqBtConnectHfpA2dp(String address) {
        Log.d(TAG, "reqBtConnectHfpA2dp:address " + address);
        if (isBindProxyService()) {
            try {
                btMethodInterface.reqBtConnectHfpA2dp(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void reqBtUnpair(String address) {
        if (isBindProxyService()) {
            try {
                btMethodInterface.reqBtUnpair(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载通话记录
     */
    public void reqPbapDownloadedCallLog() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.reqPbapDownloadedCallLog();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载联系人
     */
    public void reqPbapDownloadConnect() {
        if (isBindProxyService()) {
            try {
                Logger.d(TAG, "reqPbapDownloadConnect");
                btMethodInterface.reqPbapDownloadConnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载通讯录和通话记录
     */
    public void reqPbapDownloadAll() {
        if (isBindProxyService()) {
            BtSPUtil.getInstance().setSyncContactsStateSP(mContext, false);
            BtSPUtil.getInstance().setSyncCallLogStateSP(mContext, false);
            LitePal.deleteAll(CallLogEntity.class);
            int count = LitePal.count(ContactsEntity.class);
            Logger.d(TAG, "reqPbapDownloadAll: count " + count);
            LitePal.deleteAllAsync(ContactsEntity.class).listen(new UpdateOrDeleteCallback() {
                @Override
                public void onFinish(int rowsAffected) {
                    try {
                        btMethodInterface.reqPbapDownloadConnect();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            });


        }
    }

    public void playSong() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.playSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseSong() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.pauseSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void playNext() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.playNext();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public void playLast() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.playLast();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void breakConnect() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.breakConnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void isBtConnect() {
        if (isBindProxyService()) {
            try {
                Log.d(TAG, "isBtConnect: ");
                btMethodInterface.isBtConnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBtEnable(boolean status) {
        if (isBindProxyService()) {
            try {
                btMethodInterface.setBtEnable(status);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void reqBtPairedDevices() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.reqBtPairedDevices();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void initBTStatus() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.initBTStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void reqAvrcp13GetElementAttributesPlaying() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.reqAvrcp13GetElementAttributesPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAutoConnect(boolean autoConnect) {
        if (isBindProxyService()) {
            try {
                btMethodInterface.setAutoConnect(autoConnect);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnBTStatusListener(IBtStatusListener listener) {
        if (isBindProxyService()) {
            try {
                btMethodInterface.setOnBTStatusListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        if (isBindProxyService()) {
            try {
                return btMethodInterface.isConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void reqPbapDownloadInterrupt() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.reqPbapDownloadInterrupt();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void reqAvrcp13GetPlayStatus() {
        if (isBindProxyService()) {
            try {
                btMethodInterface.reqAvrcp13GetPlayStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


}
