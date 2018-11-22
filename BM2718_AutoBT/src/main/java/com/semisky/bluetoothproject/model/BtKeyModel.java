package com.semisky.bluetoothproject.model;

import android.os.RemoteException;
import android.util.Log;

import com.semisky.autoservice.aidl.IKeyListener;
import com.semisky.autoservice.aidl.IScreenSaveModeChanged;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.autoservice.manager.KeyManager;
import com.semisky.bluetoothproject.model.modelInterface.OnBtKeyListener;
import com.semisky.bluetoothproject.utils.Logger;

/**
 * Created by chenhongrui on 2017/8/23
 * <p>
 * 内容摘要：按键
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtKeyModel {

    private static final String TAG = "BtKeyModel";

    private static BtKeyModel INSTANCE;

    private BtStatusModel btStatusModel;

    private OnBtKeyListener keyListener;

    //是否响应音乐
    private boolean isMusicKeyCode = false;

    //是否响应通话页面
    private boolean isPhoneKeyCode = false;

    public static BtKeyModel getInstance() {
        if (INSTANCE == null) {
            synchronized (BtKeyModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BtKeyModel();
                }
            }
        }
        return INSTANCE;
    }

    private BtKeyModel() {
        setOnKeyManagerListener();
        setScreenStatusListener();
        btStatusModel = BtStatusModel.getInstance();
    }

    private void setScreenStatusListener() {
        IScreenSaveModeChanged iScreenSaveModeChanged = new IScreenSaveModeChanged.Stub() {
            @Override
            public void onScereenSaveModeChange(boolean flag) throws RemoteException {
                if (flag) {
                    if (cancelSearchInterface != null) {
                        cancelSearchInterface.cancelSearchDevice();
                    }
                }
            }
        };

        AutoManager.getInstance().registerScreenSaveModeListener(iScreenSaveModeChanged);
    }

    private boolean isMusicKeyCode() {
        return isMusicKeyCode;
    }

    void setMusicKeyCode(boolean musicKeyCode) {
        isMusicKeyCode = musicKeyCode;
    }

    public boolean isPhoneKeyCode() {
        return isPhoneKeyCode;
    }

    public void setPhoneKeyCode(boolean phoneKeyCode) {
        isPhoneKeyCode = phoneKeyCode;
    }

    /**
     * 按钮
     * action 0 = 短按 ；1 = 长按 ； 2 = 长按放开
     */
    private void setOnKeyManagerListener() {
        IKeyListener iKeyListener = new IKeyListener.Stub() {
            @Override
            public void onKey(int keyCode, int action) {
                int btIncallState = AutoManager.getInstance().getBtIncallState();
                Logger.d(TAG, "keyCode: " + keyCode + " action: " + action);
                Logger.d(TAG, "onKey: " + isMusicKeyCode());
                Logger.d(TAG, "onKey: isPhoneKeyCode " + isPhoneKeyCode);
                Logger.d(TAG, "onKey: isMusicKeyCode " + isMusicKeyCode);
                Logger.d(TAG, "onKey: btIncallState " + btIncallState);
                //蓝牙模式下，短按接听，非蓝牙模式，短按下一曲
                switch (keyCode) {
                    case KeyManager.KEYCODE_PHONE_ANSWER:
                        if (isPhoneKeyCode && action == KeyManager.ACTION_PRESS) {
                            if (btStatusModel.isCallPhone()) {//来电
                                if (keyListener != null) {
                                    keyListener.connectCall();
                                }
                            }
                        }
                        break;

                    case KeyManager.KEYCODE_PHONE_HANGUP:
                        if (isPhoneKeyCode && action == KeyManager.ACTION_PRESS) {
                            if (btStatusModel.isCallPhone()) {//挂断
                                if (keyListener != null) {
                                    keyListener.hangupCall();
                                }
                            }
                        }
                        break;

                    case KeyManager.KEYCODE_CHANNEL_UP:
                        if (action == KeyManager.ACTION_PRESS || action == KeyManager.ACTION_LONG_PRESS) {
                            boolean musicFocus = BtMusicAudioFocusModel.getINSTANCE().isMusicFocus();
                            Log.d(TAG, "onKey:musicFocus " + musicFocus);
                            if (isMusicKeyCode && musicFocus) {
                                //蓝牙音乐播放中
                                if (keyListener != null) {
                                    keyListener.nextSong();
                                    Log.d(TAG, "onKey: nextSong ");
                                }
                            } else {
                                Log.e(TAG, "onKey: 失去短暂焦点不响应按键");
                            }
                        }
                        break;

                    case KeyManager.KEYCODE_CHANNEL_DOWN:
                        if (action == KeyManager.ACTION_PRESS || action == KeyManager.ACTION_LONG_PRESS) {
                            boolean musicFocus = BtMusicAudioFocusModel.getINSTANCE().isMusicFocus();
                            Log.d(TAG, "onKey:musicFocus " + musicFocus);
                            if (isMusicKeyCode && musicFocus) {
                                if (keyListener != null) {
                                    keyListener.previousSong();
                                    Log.d(TAG, "onKey: previousSong ");
                                }
                            } else {
                                Log.e(TAG, "onKey: 失去短暂焦点不响应按键");
                            }
                        }
                        break;
                    case KeyManager.KEYCODE_MODE:
                    case KeyManager.KEYCODE_NAVI:
                    case KeyManager.KEYCODE_HOME:
                    case KeyManager.KEYCODE_AM_FM:
                        if (action == KeyManager.ACTION_LONG_PRESS || action == KeyManager.ACTION_PRESS) {
                            if (cancelSearchInterface != null) {
                                cancelSearchInterface.cancelSearchDevice();
                            }
                        }
                        break;
                }
            }
        };

        BtRegisterHelper.getInstance().registerOnKeyListener(iKeyListener);
    }

    /**
     * 注册监听
     *
     * @param listener listener
     */
    public void registerKeyListener(OnBtKeyListener listener) {
        this.keyListener = listener;
    }

    /**
     * 取消监听
     */
    public void unRegisterKeyListener() {
        this.keyListener = null;
    }

    private CancelSearchInterface cancelSearchInterface;

    public interface CancelSearchInterface {
        void cancelSearchDevice();
    }

    public void setCancelSearchInterface(CancelSearchInterface cancelSearchInterface) {
        this.cancelSearchInterface = cancelSearchInterface;
    }
}
