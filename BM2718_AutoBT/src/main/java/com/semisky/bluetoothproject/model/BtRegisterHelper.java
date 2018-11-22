package com.semisky.bluetoothproject.model;

import android.util.Log;

import com.semisky.autoservice.aidl.IKeyListener;
import com.semisky.autoservice.manager.KeyManager;
import com.semisky.autoservice.manager.RadioManager;

/**
 * Created by chenhongrui on 2017/8/16
 * <p>
 * 内容摘要：用于和中间层注册监听
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtRegisterHelper {

    private static final String TAG = "BtRegisterHelper";

    private static BtRegisterHelper instance;
    private RadioManager mRadioManager;

    //中间层监听按键
    private com.semisky.autoservice.manager.KeyManager middleKeyManager;

    public static BtRegisterHelper getInstance() {
        if (instance == null) {
            synchronized (BtRegisterHelper.class) {
                if (instance == null) {
                    instance = new BtRegisterHelper();
                }
            }
        }
        return instance;
    }

    private BtRegisterHelper() {
        mRadioManager = RadioManager.getInstance();
        middleKeyManager = KeyManager.getInstance();
    }

    /**
     * 设置按钮监听
     *
     * @param onKeyListener 按钮listener
     */
    public void registerOnKeyListener(IKeyListener onKeyListener) {
        Log.d(TAG, "registerOnKeyListener: 设置按钮监听");
        middleKeyManager.setOnKeyListener(onKeyListener);
    }

    /**
     * 取消按钮监听
     *
     * @param onKeyListener 按钮listener
     */
    public void unregisterOnKeyListener(IKeyListener onKeyListener) {
        Log.d(TAG, "unregisterOnKeyListener: 取消");
        middleKeyManager.unregisterOnKeyListener(onKeyListener);
    }
}
