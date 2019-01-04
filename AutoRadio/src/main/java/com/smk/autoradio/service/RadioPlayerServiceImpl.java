package com.smk.autoradio.service;

import android.os.RemoteException;

import com.semisky.autoservice.aidl.IBackModeChanged;
import com.smk.autoradio.aidl.IRadioPlayer;
import com.smk.autoradio.aidl.IRadioStatusChangeListener;

public class RadioPlayerServiceImpl extends IRadioPlayer.Stub {


    public RadioPlayerServiceImpl(){

    }
    // 倒车监听
    private IBackModeChanged.Stub mIBackModeChanged = new IBackModeChanged.Stub(){
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

    }

    @Override
    public int getChannelType() throws RemoteException {
        return 0;
    }

    @Override
    public int getChannel() throws RemoteException {
        return 0;
    }

    @Override
    public void reqSwitchBand() throws RemoteException {

    }

    @Override
    public void reqPrevStrongChannel() throws RemoteException {

    }

    @Override
    public void reqNextStroneChannel() throws RemoteException {

    }

    @Override
    public void reqStepIncreaseChannel() throws RemoteException {

    }

    @Override
    public void reqStepDecreaseChannel() throws RemoteException {

    }

    @Override
    public void reqPlayChannel(int channel) throws RemoteException {

    }

    @Override
    public void reqFullSearch() throws RemoteException {

    }

    @Override
    public void reqSettingEQ() throws RemoteException {

    }

    @Override
    public void reqAddCurrentChannelToFavorite() throws RemoteException {

    }

    @Override
    public void reqGetFavoriteList() throws RemoteException {

    }

    @Override
    public void reqGetSearchList() throws RemoteException {

    }

    @Override
    public void reqSwitchDxOrLoc() throws RemoteException {

    }

    @Override
    public int getSoundtrackType() throws RemoteException {
        return 0;
    }
}
