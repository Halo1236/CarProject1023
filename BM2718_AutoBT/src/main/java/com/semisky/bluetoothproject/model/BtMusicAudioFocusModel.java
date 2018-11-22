package com.semisky.bluetoothproject.model;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.model.modelInterface.OnBtAudioEventListener;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;
import com.semisky.bluetoothproject.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.semisky.autoservice.manager.AudioManager.STREAM_BT_MUSIC;

/**
 * Created by chenhongrui on 2017/8/24
 * <p>
 * 内容摘要：蓝牙音乐音频焦点管理
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtMusicAudioFocusModel {

    private static final String TAG = Logger.makeTagLog(BtMusicAudioFocusModel.class);

    private Context mContext;

    //系统的AudioManager
    private AudioManager.OnAudioFocusChangeListener mFocusChangeListener;

    private com.semisky.autoservice.manager.AudioManager audioManager;

    private List<OnBtAudioEventListener> eventListener = new ArrayList<>();

    //系统的AudioManager
    private AudioManager systemAudioManager;

    private static BtMusicAudioFocusModel INSTANCE;

    //音频焦点状态
    private boolean hasAudioFocus = false;

    //释放申请音频通道
    private boolean isOpenStreamVolume = false;

    private BtBaseUiCommandMethod btBaseUiCommandMethod;

    //是否暂停了音乐
    private boolean isPauseMusic = false;

    /**
     * 当前是否处于播放状态(根据音频焦点判断)
     */
    private boolean isMusicFocus = false;

    public static BtMusicAudioFocusModel getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (BtMusicAudioFocusModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BtMusicAudioFocusModel();
                }
            }
        }
        return INSTANCE;
    }

    public boolean isMusicFocus() {
        return isMusicFocus;
    }

    private boolean isPauseMusic() {
        Log.d(TAG, "isPauseMusic: " + isPauseMusic);
        return isPauseMusic;
    }

    public void setPauseMusic(boolean pauseMusic) {
        Log.d(TAG, "setPauseMusic: " + pauseMusic);
        isPauseMusic = pauseMusic;
    }

    private BtMusicAudioFocusModel() {
        this.mContext = BtApplication.getContext();
        audioManager = com.semisky.autoservice.manager.AudioManager.getInstance();
        systemAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
        AudioFocusCallBack();
    }

    public boolean isHasAudioFocus() {
        Log.d(TAG, "isHasAudioFocus: " + hasAudioFocus);
        return hasAudioFocus;
    }

    /**
     * 申请音频焦点&打开音频流
     */
    public void applyAudioFocus() {
        Log.d(TAG, "applyAudioFocus ：hasAudioFocus: " + hasAudioFocus);
        initPlaySetting();
        if (!isHasAudioFocus()) {
            hasAudioFocus = requestFocus();//申请音频焦点
//            BtMiddleSettingManager.getInstance().setAppStatusInRequested();
        }
        BtKeyModel.getInstance().setMusicKeyCode(true);
        informAutoAudioFocusRequest();//通知申请音频流成功
        Log.d(TAG, "applyAudioFocus ：hasAudioFocus " + hasAudioFocus);
    }

    public void abandonAudioFocus() {
        if (isHasAudioFocus()) {
//        BtMiddleSettingManager.getInstance().setAppStatusInAbandon();
            hasAudioFocus = !abandonFocus();// 注销音频焦点
            isMusicFocus = false;
            closeMusicStreamVolume();
        }
    }

    private void initPlaySetting() {
        isMusicFocus = true;
        openMusicStreamVolume();
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
                        informAudioFocusLess();//发送丢失音频消息
                        BtKeyModel.getInstance().setMusicKeyCode(false);
                        abandonAudioFocus();
                        pauseMusic();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://暂时失去焦点 -2
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT" + focusChange);
                        informLossTransient();
                        closeMusicStreamVolume();
                        BtKeyModel.getInstance().setMusicKeyCode(false);//不响应方控
                        isMusicFocus = false;
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://duck机制 -3
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK" + focusChange);
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN://重获焦点 1
                        Log.d(TAG, "AUDIOFOCUS_GAIN" + focusChange);
                        informAutoAudioFocusRequest();//通知重获音频焦点
                        BtKeyModel.getInstance().setMusicKeyCode(true);//响应方控
                        if (!isPauseMusic()) {
                            initPlaySetting();
                            btBaseUiCommandMethod.play();
                            btBaseUiCommandMethod.muteA2dpRender(false);
                        }
                        break;
                }
            }
        };
    }

    private void pauseMusic() {
        btBaseUiCommandMethod.muteA2dpRender(true);
        btBaseUiCommandMethod.pause();
    }

    /**
     * 通知音频焦点丢失
     */
    private void informAudioFocusLess() {
        if (eventListener.size() > 0) {
            for (OnBtAudioEventListener listener : eventListener) {
                listener.onAudioLess();
            }
        }
    }

    /**
     * 暂时失去焦点
     */
    private void informLossTransient() {
        if (eventListener.size() > 0) {
            for (OnBtAudioEventListener listener : eventListener) {
                listener.onAudioLossTransient();
            }
        }
    }

    /**
     * 通知重获音频焦点/音频焦点申请成功
     */
    private void informAutoAudioFocusRequest() {
        if (eventListener.size() > 0) {
            for (OnBtAudioEventListener listener : eventListener) {
                listener.onAudioRequest();
            }
        }
    }

    public void registerAudioLessListener(OnBtAudioEventListener listener) {
        if (!eventListener.contains(listener)) {
            eventListener.add(listener);
        }
    }

    public void unRegisterAudioLessListener(OnBtAudioEventListener listener) {
        if (eventListener.contains(listener)) {
            eventListener.remove(listener);
        }
    }

    /**
     * 申请音频焦点
     *
     * @return true 申请成功
     */
    private boolean requestFocus() {
        return mFocusChangeListener != null && AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                systemAudioManager.requestAudioFocus(mFocusChangeListener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
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
    private void openMusicStreamVolume() {
        Logger.d(TAG, "openMusicStreamVolume: ");
        audioManager.openStreamVolume(STREAM_BT_MUSIC);
        btBaseUiCommandMethod.muteA2dpRender(false);
    }

    private void closeMusicStreamVolume() {
        Logger.d(TAG, "closeMusicStreamVolume: ");
        audioManager.closeStreamVolume(STREAM_BT_MUSIC);
    }
}
