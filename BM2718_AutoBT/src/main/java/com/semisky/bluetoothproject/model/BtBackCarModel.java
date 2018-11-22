package com.semisky.bluetoothproject.model;

import android.os.RemoteException;

import com.semisky.autoservice.aidl.IBackModeChanged;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.bluetoothproject.model.modelInterface.OnBackCarStateChangeListener;
import com.semisky.bluetoothproject.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhongrui on 2017/8/24
 * <p>
 * 内容摘要：倒车管理
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtBackCarModel {

    private static final String TAG = Logger.makeTagLog(BtBackCarModel.class);

    private static BtBackCarModel instance;
    private List<OnBackCarStateChangeListener> mCarStateListenerList = null;

    private BtBackCarModel() {
        mCarStateListenerList = new ArrayList<>();
        initListener();
    }

    private void initListener() {
        IBackModeChanged iBackModeChanged = new IBackModeChanged.Stub() {
            @Override
            public void onBackModeChange(boolean flag) throws RemoteException {
                //在通话中如果进入倒车，就把声音切换到手机端 7973
//                if (flag) {
//                    Logger.d(TAG, "onBackModeChange: 进入倒车");
//                    notifyObserverEnterBackCar();
//                } else {
//                    Logger.d(TAG, "onBackModeChange: 退出倒车");
//                    notifyObserverQuitBackCar();
//                }
            }
        };

        AutoManager.getInstance().registerBackModeListener(iBackModeChanged);
    }

    public static BtBackCarModel getInstance() {
        if (instance == null) {
            synchronized (BtBackCarModel.class) {
                if (instance == null) {
                    instance = new BtBackCarModel();
                }
            }
        }
        return instance;
    }

    /**
     * 注册倒车状态监听
     */
    public void setOnCarStateChangeListener(OnBackCarStateChangeListener listener) {
        if (null != mCarStateListenerList && !mCarStateListenerList.contains(listener)) {
            mCarStateListenerList.add(listener);
        }
    }

    /**
     * 反注册倒车状态监听
     */
    public void unRegisterOnBtStateChangeListener(OnBackCarStateChangeListener listener) {
        if (null != mCarStateListenerList && mCarStateListenerList.contains(listener)) {
            mCarStateListenerList.remove(listener);
        }
    }

    /**
     * 进入倒车
     */
    private void notifyObserverEnterBackCar() {
        if (null != mCarStateListenerList) {
            for (OnBackCarStateChangeListener l : mCarStateListenerList) {
                l.onBackCarEnter();
            }
        }
    }

    /**
     * 退出倒车
     */
    private void notifyObserverQuitBackCar() {
        if (null != mCarStateListenerList) {
            for (OnBackCarStateChangeListener l : mCarStateListenerList) {
                l.onBackCarQuit();
            }
        }
    }
}
