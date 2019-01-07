package com.smk.autoradio.presenter;

import com.smk.autoradio.manager.RadioPlayerProxyManager;
import com.smk.autoradio.utils.Logutil;
import com.smk.autoradio.views.activity.IRadioPlayerView;

public class RadioPlayerPresenter<V extends IRadioPlayerView> extends BasePresenter<V> {
    private static final String TAG = Logutil.makeTagLog(RadioPlayerPresenter.class);

    private RadioPlayerProxyManager mRadioPlayerProxyManager;

    public RadioPlayerPresenter() {
        mRadioPlayerProxyManager = RadioPlayerProxyManager.getInstance();
        mRadioPlayerProxyManager.registerOnServieConnectStateListener(mOnServieConnectStateListener);
    }

    @Override
    public void detachView() {
        super.detachView();
        mRadioPlayerProxyManager.unregisterOnServieConnectStateListener(mOnServieConnectStateListener);
        mOnServieConnectStateListener = null;
        mRadioPlayerProxyManager = null;
    }

    private RadioPlayerProxyManager.OnServieConnectStateListener mOnServieConnectStateListener = new RadioPlayerProxyManager.OnServieConnectStateListener() {
        @Override
        public void onServiceConnected() {
            Logutil.i(TAG,"onServiceConnected() ...");
            reqRestoreChannelPlay();
        }

        @Override
        public void onServiceDisconnected() {
            Logutil.i(TAG,"onServiceDisconnected() ...");
        }
    };

    public void initRadio() {
        mRadioPlayerProxyManager.reqCheckConnectStatus();
    }


    public void reqRestoreChannelPlay() {
        if (isBindView()) {
            mRadioPlayerProxyManager.reqRestoreChannelPlay();
        }
    }

    public void reqPrevStrongChannel() {
        if (isBindView()) {
            mRadioPlayerProxyManager.reqPrevStrongChannel();
        }
    }

    public void reqNextStroneChannel() {
        if (isBindView()) {
            mRadioPlayerProxyManager.reqNextStroneChannel();
        }
    }

    public void reqSwitchBand() {
        if (isBindView()) {
            mRadioPlayerProxyManager.reqSwitchBand();
        }
    }

    public void reqSettingEQ() {
        if (isBindView()) {
            mRadioPlayerProxyManager.reqSettingEQ();
        }
    }

    // 请求切换远近程
    public void reqSwitchDxOrLoc() {
        if (isBindView()) {
            mRadioPlayerProxyManager.reqSwitchDxOrLoc();
        }
    }

    // 请求全搜
    public void reqFullSearch() {
        if (isBindView()) {
            mRadioPlayerProxyManager.reqFullSearch();
        }
    }


}
