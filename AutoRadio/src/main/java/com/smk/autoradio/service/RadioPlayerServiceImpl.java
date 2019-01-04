package com.smk.autoradio.service;

import android.os.RemoteException;

import com.semisky.autoservice.aidl.IBackModeChanged;
import com.smk.autoradio.aidl.IRadioPlayer;
import com.smk.autoradio.aidl.IRadioStatusChangeListener;
import com.smk.autoradio.utils.Logutil;

public class RadioPlayerServiceImpl extends IRadioPlayer.Stub {
    private static final String TAG = Logutil.makeTagLog(RadioPlayerServiceImpl.class);


    public RadioPlayerServiceImpl() {

    }

    // 倒车监听
    private IBackModeChanged.Stub mIBackModeChanged = new IBackModeChanged.Stub() {
        @Override
        public void onBackModeChange(boolean b) throws RemoteException {

        }
    };


    @Override
    public void registerOnRadioStatusChangeListener(IRadioStatusChangeListener l) throws RemoteException {

    }

    @Override
    public void unregisterOnRadioStatusChangeListener(IRadioStatusChangeListener l) throws RemoteException {

    }

    @Override
    public void reqRestoreChannelPlay() throws RemoteException {
        Logutil.i(TAG, "reqRestoreChannelPlay()");
    }

    @Override
    public int getChannelType() throws RemoteException {
        Logutil.i(TAG, "getChannelType()");
        return 0;
    }

    @Override
    public int getChannel() throws RemoteException {
        Logutil.i(TAG, "getChannel()");
        return 0;
    }

    @Override
    public void reqSwitchBand() throws RemoteException {
        Logutil.i(TAG, "reqSwitchBand()");
    }

    @Override
    public void reqPrevStrongChannel() throws RemoteException {
        Logutil.i(TAG, "reqPrevStrongChannel()");
    }

    @Override
    public void reqNextStroneChannel() throws RemoteException {
        Logutil.i(TAG, "reqNextStroneChannel()");
    }

    @Override
    public void reqStepIncreaseChannel() throws RemoteException {
        Logutil.i(TAG, "reqStepIncreaseChannel()");
    }

    @Override
    public void reqStepDecreaseChannel() throws RemoteException {
        Logutil.i(TAG, "reqStepDecreaseChannel()");
    }

    @Override
    public void reqPlayChannel(int channel) throws RemoteException {
        Logutil.i(TAG, "reqPlayChannel()" + channel);
    }

    @Override
    public void reqFullSearch() throws RemoteException {
        Logutil.i(TAG, "reqFullSearch()");
    }

    @Override
    public void reqSettingEQ() throws RemoteException {
        Logutil.i(TAG, "reqSettingEQ()");
    }

    @Override
    public void reqAddCurrentChannelToFavorite() throws RemoteException {
        Logutil.i(TAG, "reqAddCurrentChannelToFavorite()");
    }

    @Override
    public void reqGetFavoriteList() throws RemoteException {
        Logutil.i(TAG, "reqGetFavoriteList()");
    }

    @Override
    public void reqGetSearchList() throws RemoteException {
        Logutil.i(TAG, "reqGetSearchList()");
    }

    @Override
    public void reqSwitchDxOrLoc() throws RemoteException {
        Logutil.i(TAG, "reqSwitchDxOrLoc()");
    }

    @Override
    public int getSoundtrackType() throws RemoteException {
        Logutil.i(TAG, "getSoundtrackType()");
        return 0;
    }
}
