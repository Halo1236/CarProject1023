package com.smk.autoradio.presenter;

import com.smk.autoradio.model.RadioProxyModel;
import com.smk.autoradio.utils.Logutil;
import com.smk.autoradio.views.activity.IRadioPlayerView;

public class RadioPlayerPresenter<V extends IRadioPlayerView> extends BasePresenter<V> implements IRadioPlayerPresenter {
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
            Logutil.i(TAG, "onServiceConnected() ...");
            reqRestoreChannelPlay();
        }

        @Override
        public void onServiceDisconnected() {
            Logutil.i(TAG, "onServiceDisconnected() ...");
        }
    };

    public void initRadio() {
        mRadioProxyModel.reqCheckConnectStatus();
    }


    @Override
    public void reqRestoreChannelPlay() {
        if (isBindView()) {
            mRadioProxyModel.reqRestoreChannelPlay();
        }
    }

    @Override
    public void reqPrevStrongChannel() {
        if (isBindView()) {
            mRadioProxyModel.reqPrevStrongChannel();
        }
    }

    @Override
    public void reqNextStroneChannel() {
        if (isBindView()) {
            mRadioProxyModel.reqNextStroneChannel();
        }
    }

    @Override
    public void reqStepIncreaseChannel() {
        if (isBindView()) {
            mRadioProxyModel.reqStepIncreaseChannel();
        }
    }

    @Override
    public void reqStepDecreaseChannel() {
        if (isBindView()) {
            mRadioProxyModel.reqStepDecreaseChannel();
        }
    }

    @Override
    public void reqSwitchBand() {
        if (isBindView()) {
            mRadioProxyModel.reqSwitchBand();
        }
    }

    @Override
    public void reqSettingEQ() {
        if (isBindView()) {
            mRadioProxyModel.reqSettingEQ();
        }
    }

    @Override
    public void reqSwitchDxOrLoc() {
        if (isBindView()) {
            mRadioProxyModel.reqSwitchDxOrLoc();
        }
    }

    @Override
    public void reqFullSearch() {
        if (isBindView()) {
            mRadioProxyModel.reqFullSearch();
        }
    }

    @Override
    public void reqGetFullSearchList() {
        if (isBindView()) {
            mRadioProxyModel.reqGetFullSearchList();
        }
    }

    @Override
    public void reqGetFullSearchAndFavoriteChannelList() {
        if (isBindView()) {
            mRadioProxyModel.reqGetFullSearchAndFavoriteChannelList();
        }
    }
}
