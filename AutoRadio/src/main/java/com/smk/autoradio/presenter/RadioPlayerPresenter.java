package com.smk.autoradio.presenter;

import com.smk.autoradio.model.RadioProxyModel;
import com.smk.autoradio.utils.Logutil;
import com.smk.autoradio.views.activity.IRadioPlayerView;

public class RadioPlayerPresenter<V extends IRadioPlayerView> extends BasePresenter<V> {
    private static final String TAG = Logutil.makeTagLog(RadioPlayerPresenter.class);

    private RadioProxyModel mRadioProxyModel;

    public RadioPlayerPresenter() {
        mRadioProxyModel = RadioProxyModel.getInstance();
        mRadioProxyModel.registerOnServieConnectStateListener(mOnServieConnectStateListener);
    }

    @Override
    public void detachView() {
        super.detachView();
        mRadioProxyModel.unregisterOnServieConnectStateListener(mOnServieConnectStateListener);
        mOnServieConnectStateListener = null;
        mRadioProxyModel = null;
    }

    private RadioProxyModel.OnServieConnectStateListener mOnServieConnectStateListener = new RadioProxyModel.OnServieConnectStateListener() {
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
        mRadioProxyModel.reqCheckConnectStatus();
    }


    public void reqRestoreChannelPlay() {
        if (isBindView()) {
            mRadioProxyModel.reqRestoreChannelPlay();
        }
    }

    public void reqPrevStrongChannel() {
        if (isBindView()) {
            mRadioProxyModel.reqPrevStrongChannel();
        }
    }

    public void reqNextStroneChannel() {
        if (isBindView()) {
            mRadioProxyModel.reqNextStroneChannel();
        }
    }

    public void reqSwitchBand() {
        if (isBindView()) {
            mRadioProxyModel.reqSwitchBand();
        }
    }

    public void reqSettingEQ() {
        if (isBindView()) {
            mRadioProxyModel.reqSettingEQ();
        }
    }

    // 请求切换远近程
    public void reqSwitchDxOrLoc() {
        if (isBindView()) {
            mRadioProxyModel.reqSwitchDxOrLoc();
        }
    }

    // 请求全搜
    public void reqFullSearch() {
        if (isBindView()) {
            mRadioProxyModel.reqFullSearch();
        }
    }


}
