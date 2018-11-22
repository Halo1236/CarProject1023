package com.semisky.bluetoothproject.model;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.utils.Logger;

import static com.semisky.autoservice.manager.AudioManager.STREAM_BT_PHONE;

/**
 * Created by chenhongrui on 2017/8/24
 * <p>
 * 内容摘要：蓝牙电话音频焦点管理
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtPhoneAudioFocusModel {

    private static final String TAG = "BtPhoneAudioFocusModel";

    private Context mContext;

    //系统的AudioManager
    private AudioManager.OnAudioFocusChangeListener mFocusChangeListener;

    private com.semisky.autoservice.manager.AudioManager audioManager;

    //系统的AudioManager
    private AudioManager systemAudioManager;

    private static BtPhoneAudioFocusModel INSTANCE;

    //音频焦点状态
    private boolean hasAudioFocus = false;
    //释放申请音频通道
    private boolean isOpenStreamVolume = false;

    public static BtPhoneAudioFocusModel getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (BtPhoneAudioFocusModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BtPhoneAudioFocusModel();
                }
            }
        }
        return INSTANCE;
    }

    private BtPhoneAudioFocusModel() {
        this.mContext = BtApplication.getContext();
        audioManager = com.semisky.autoservice.manager.AudioManager.getInstance();
        systemAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        AudioFocusCallBack();
    }

    private boolean isHasAudioFocus() {
        Log.d(TAG, "isHasAudioFocus: " + hasAudioFocus);
        return hasAudioFocus;
    }

    /**
     * 申请音频焦点&打开音频流
     */
    public void applyAudioFocus() {
        Log.d(TAG, "applyAudioFocus ：hasAudioFocus: " + hasAudioFocus);
//        BtMiddleSettingManager.getInstance().setAppStatusInRequested();
        if (!isHasAudioFocus()) {
            openPhoneStreamVolume();
            hasAudioFocus = requestFocus();//申请音频焦点
            BtKeyModel.getInstance().setPhoneKeyCode(true);
        }
        Log.d(TAG, "applyAudioFocus ：hasAudioFocus " + hasAudioFocus);
    }

    public void abandonAudioFocus() {
        if (isHasAudioFocus()) {
//        BtMiddleSettingManager.getInstance().setAppStatusInAbandon();
            hasAudioFocus = !abandonFocus();// 注销音频焦点
            BtKeyModel.getInstance().setPhoneKeyCode(false);
            closePhoneStreamVolume();
        }
    }

    /**
     * 音频焦点回调
     * 1.导航走的是duck -3 -》 1
     * 2.蓝牙电话走的是暂时失去焦点 -2 -》 1
     * 蓝牙电话接听时先发送的广播1 再失去焦点
     * 蓝牙电话挂断时先发送广播0 再重获焦点
     * 3.carlife语言识别同上
     * 4.屏幕同上
     */
    private void AudioFocusCallBack() {
        mFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS://永久失去焦点 -1
                        Log.d(TAG, "AUDIOFOCUS_LOSS" + focusChange);
                        BtKeyModel.getInstance().setPhoneKeyCode(false);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://暂时失去焦点 -2
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT" + focusChange);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://duck机制 -3
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK" + focusChange);
//                        RadioVolumeModel.getInstance(mContext).regulateVolumeDown();//降低音量
//                        RadioVolumeModel.getInstance(mContext).regulateVol = true;
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN://重获焦点 1
                        Log.d(TAG, "AUDIOFOCUS_GAIN" + focusChange);
//                        if (!RadioStatusManage.getInstance(mContext).isClickMute()) {
//                        openStreamVolume();
//                        }
//                        if (RadioVolumeModel.getInstance(mContext).regulateVol) {
//                            RadioVolumeModel.getInstance(mContext).regulateVolumeUp();//增加音量
//                            RadioVolumeModel.getInstance(mContext).regulateVol = false;
//                        }
                        break;
                }
            }
        };
    }

    /**
     * 申请音频焦点
     *
     * @return true 申请成功
     */
    private boolean requestFocus() {
        return mFocusChangeListener != null && AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                systemAudioManager.requestAudioFocus(mFocusChangeListener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    /**
     * 释放音频焦点
     *
     * @return true 释放成功
     */
    private boolean abandonFocus() {
        return mFocusChangeListener != null && AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                systemAudioManager.abandonAudioFocus(mFocusChangeListener);
    }

    /**
     * 打开音乐音频流
     */
    private void openPhoneStreamVolume() {
        Logger.d(TAG, "openPhoneStreamVolume: ");
        audioManager.openStreamVolume(STREAM_BT_PHONE);
    }

    private void closePhoneStreamVolume() {
        Logger.d(TAG, "closePhoneStreamVolume: ");
        audioManager.closeStreamVolume(STREAM_BT_PHONE);
    }

}
