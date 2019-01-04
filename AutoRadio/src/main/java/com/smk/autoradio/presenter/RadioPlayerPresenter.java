package com.smk.autoradio.presenter;

import com.smk.autoradio.manager.RadioPlayerProxyManager;
import com.smk.autoradio.views.activity.IRadioPlayerView;

public class RadioPlayerPresenter<V extends IRadioPlayerView> extends BasePresenter<V> {

    private RadioPlayerProxyManager mRadioPlayerProxyManager;

    public RadioPlayerPresenter() {
        mRadioPlayerProxyManager = RadioPlayerProxyManager.getInstance();
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

    @Override
    public void detachView() {
        super.detachView();
    }
}
