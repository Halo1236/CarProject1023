package com.smk.autoradio.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.aidl.IRadioProxyService;
import com.smk.autoradio.aidl.IRadioStatusListener;
import com.smk.autoradio.service.callback.DoRadioStatusCallback;
import com.smk.autoradio.service.player.IRadioPlayer;
import com.smk.autoradio.service.player.RadioPlayer;
import com.smk.autoradio.utils.Logutil;

import java.util.List;

public class RadioService extends Service {
    private static final String TAG = Logutil.makeTagLog(RadioService.class);
    private RadioProxyServiceImpl mRadioProxyServiceImpl;


    @Override
    public void onCreate() {
        super.onCreate();
        if (null == mRadioProxyServiceImpl) {
            this.mRadioProxyServiceImpl = new RadioProxyServiceImpl();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mRadioProxyServiceImpl;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected class RadioProxyServiceImpl extends IRadioProxyService.Stub {
        private DoRadioStatusCallback mDoRadioStatusCallback;
        private IRadioPlayer mIRadioPlayer;

        public RadioProxyServiceImpl() {
            this.mIRadioPlayer = new RadioPlayer();
            this.mDoRadioStatusCallback = new DoRadioStatusCallback();
            this.mIRadioPlayer.registerOnRadioPlayerStatusListener(mOnRadioPlayerStatusListener);
        }

        private IRadioPlayer.OnRadioPlayerStatusListener mOnRadioPlayerStatusListener
                = new IRadioPlayer.OnRadioPlayerStatusListener() {
            @Override
            public void onFullSearchChannelStart() {
                mDoRadioStatusCallback.onFullSearchChannelStart();
            }

            @Override
            public void onFullSearchChannelReport(int channel) {
                mDoRadioStatusCallback.onFullSearchChannelReport(channel);
            }

            @Override
            public void onFullSearchChannelEnd() {
                mDoRadioStatusCallback.onFullSearchChanelEnd();
            }

            @Override
            public void onStrongChannelSearchStart() {
                mDoRadioStatusCallback.onStrongChannelSearchStart();
            }

            @Override
            public void onStrongChannelSearchReport(int channel) {
                mDoRadioStatusCallback.onStrongChannelSearchReport(channel);
            }

            @Override
            public void onStrongChannelSearchEnd() {
                mDoRadioStatusCallback.onStrongChannelSearchEnd();
            }

            @Override
            public void onLoadFullSearchChannelList(List<ChannelInfo> channelList) {
                mDoRadioStatusCallback.onLoadFullSearchChannelList(channelList);
            }

            @Override
            public void onLoadFavoriteChannelList(List<ChannelInfo> channelList) {
                mDoRadioStatusCallback.onLoadFavoriteChannelList(channelList);
            }

            @Override
            public void onLoadFullSearchAndFavoriteChannelList(List<ChannelInfo> channelList) {
                mDoRadioStatusCallback.onLoadFullSearchAndFavoriteChannelList(channelList);
            }

            @Override
            public void onChannelTypeChanged(int channelType) {
                mDoRadioStatusCallback.onChannelTypeChanged(channelType);
            }

            @Override
            public void onChannelRangeChanged(int channelValueMin, int channelValueMax) {
                mDoRadioStatusCallback.onChannelRangeChanged(channelValueMin, channelValueMax);
            }

            @Override
            public void onChannelSoundtrackTypeChanged(int soundtrackType) {
                mDoRadioStatusCallback.onChannelSoundtrackTypeChanged(soundtrackType);
            }

            @Override
            public void onChannelDxLocTypeChanged(int channelDxLocType) {
                mDoRadioStatusCallback.onChannelDxLocTypeChanged(channelDxLocType);
            }
        };


        @Override
        public void registerOnRadioStatusListener(IRadioStatusListener l) throws RemoteException {
            mDoRadioStatusCallback.register(l);
        }

        @Override
        public void unregisterOnRadioStatusListener(IRadioStatusListener l) throws RemoteException {
            mDoRadioStatusCallback.unregister(l);
        }

        @Override
        public void reqRestoreChannelPlay() throws RemoteException {
            mIRadioPlayer.reqRestoreChannelPlay();
        }

        @Override
        public int getChannelType() throws RemoteException {
            return mIRadioPlayer.getChannelType();
        }

        @Override
        public int getChannel() throws RemoteException {
            return mIRadioPlayer.getChannel();
        }

        @Override
        public void reqSwitchBand() throws RemoteException {
            mIRadioPlayer.reqSwitchBand();
        }

        @Override
        public void reqPrevStrongChannel() throws RemoteException {
            mIRadioPlayer.reqPrevStrongChannel();
        }

        @Override
        public void reqNextStroneChannel() throws RemoteException {
            mIRadioPlayer.reqNextStroneChannel();
        }

        @Override
        public void reqStepIncreaseChannel() throws RemoteException {
            mIRadioPlayer.reqStepIncreaseChannel();
        }

        @Override
        public void reqStepDecreaseChannel() throws RemoteException {
            mIRadioPlayer.reqStepDecreaseChannel();
        }

        @Override
        public void reqPlayChannel(int channelType, int channel) throws RemoteException {
            mIRadioPlayer.reqPlayChannel(channelType, channel);
        }

        @Override
        public void reqFullSearch() throws RemoteException {
            mIRadioPlayer.reqFullSearch();
        }

        @Override
        public void reqSettingEQ() throws RemoteException {
            mIRadioPlayer.reqSettingEQ();
        }

        @Override
        public void reqAddCurrentChannelToFavorite() throws RemoteException {
            mIRadioPlayer.reqAddCurrentChannelToFavorite();
        }

        @Override
        public void reqGetFavoriteList() throws RemoteException {
            mIRadioPlayer.reqGetFavoriteList();
        }

        @Override
        public void reqGetFullSearchList() throws RemoteException {
            mIRadioPlayer.reqGetSearchList();
        }

        @Override
        public void reqGetFullSearchAndFavoriteChannelList() throws RemoteException {

        }

        @Override
        public void reqSwitchDxOrLoc() throws RemoteException {
            mIRadioPlayer.reqSwitchDxOrLoc();
        }

        @Override
        public int getSoundtrackType() throws RemoteException {
            return mIRadioPlayer.getSoundtrackType();
        }

        @Override
        public int getMinChannelValue() throws RemoteException {
            return mIRadioPlayer.getMinChannelValue();
        }

        @Override
        public int getMaxChannelValue() throws RemoteException {
            return mIRadioPlayer.getMaxChannelValue();
        }
    }


}
