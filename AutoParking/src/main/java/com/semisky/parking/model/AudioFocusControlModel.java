package com.semisky.parking.model;

import android.content.Context;
import android.media.AudioManager;

import com.semisky.parking.application.ParkingApplication;
import com.semisky.parking.utils.Logger;

/**
 * Created by Administrator on 2018/8/11.
 */

public class AudioFocusControlModel implements AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = Logger.makeLogTag(AudioFocusControlModel.class);
    private boolean mIsRequestAudioFocus = false;
    private AudioManager mAudioManager;
    private Context mContext;
    private static AudioFocusControlModel _INSTANCE;

    public static AudioFocusControlModel getInstance(Context ctx) {
        if (null == _INSTANCE) {
            _INSTANCE = new AudioFocusControlModel(ctx);
        }
        return _INSTANCE;
    }

    private AudioFocusControlModel(Context ctx) {
        this.mContext = ctx;
        this.mAudioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 注册申请音频焦点
     */
    public void onRequestAudioFocus() {
        Logger.i(TAG, "onRequestAudioFocus() mIsRequestAudioFocus =" + mIsRequestAudioFocus);
        if (!mIsRequestAudioFocus) {
            int result = this.mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (AudioManager.AUDIOFOCUS_REQUEST_FAILED == result) {
                Logger.i(TAG, "onRequestAudioFocus() AUDIOFOCUS_REQUEST_FAILED ...");
            } else if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result) {
                Logger.i(TAG, "onRequestAudioFocus() AUDIOFOCUS_REQUEST_GRANTED ...");
                mIsRequestAudioFocus = true;
            }
        }
    }

    /**
     * 反注册音频焦点
     */
    public void onAbandonAudioFocus() {
        if (mIsRequestAudioFocus) {
            this.mAudioManager.abandonAudioFocus(this);
            mIsRequestAudioFocus = false;
            Logger.i(TAG, "onAbandonAudioFocus ...");
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                Logger.i(TAG, "AUDIOFOCUS_GAIN ...");
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                Logger.i(TAG, "AUDIOFOCUS_LOSS ...");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Logger.i(TAG, "AUDIOFOCUS_LOSS_TRANSIENT ...");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Logger.i(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ...");
                break;

        }
    }
}
