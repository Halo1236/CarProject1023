package com.semisky.btcarkit.prenster;

import android.os.RemoteException;

import com.semisky.btcarkit.aidl.BTConst;
import com.semisky.btcarkit.aidl.ISmkCallbackA2dp;
import com.semisky.btcarkit.aidl.ISmkCallbackAvrcp;
import com.semisky.btcarkit.service.manager.BTRoutingCommandManager;
import com.semisky.btcarkit.utils.Logutil;
import com.semisky.btcarkit.view.fragment.IBTMusicPlayerView;

public class BTMusicPlayerPresenter<V extends IBTMusicPlayerView> extends BasePresenter<V> implements IBTMusicPlayerPresenter {
    private final String TAG = Logutil.makeTagLog(BTMusicPlayerPresenter.class);

    private BTRoutingCommandManager.OnServiceStateListener mOnServiceStateListener = new BTRoutingCommandManager.OnServiceStateListener() {
        @Override
        public void onServiceConnected() {
            Logutil.i(TAG, "onServiceConnected() ,,,,,");
            BTRoutingCommandManager.getInstance().registerA2dpCallback(mISmkCallbackA2dp);
            BTRoutingCommandManager.getInstance().registerAvrcpCallback(mISmkCallbackAvrcp);
            if (isBindView()) {
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onRefreshBtMusicInfos();
                    }
                });
            }
        }
    };

    private ISmkCallbackA2dp.Stub mISmkCallbackA2dp = new ISmkCallbackA2dp.Stub() {
        @Override
        public void onA2dpStateChanged(final int oldState, final int newState) throws RemoteException {
            Logutil.i(TAG, "onA2dpStateChanged() oldState : " +oldState+ " , newState : " + newState);
            _handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isBindView()) {
                        mViewRef.get().onA2dpStateChanged(newState);
                    }
                }
            });
        }
    };

    private ISmkCallbackAvrcp.Stub mISmkCallbackAvrcp = new ISmkCallbackAvrcp.Stub() {
        @Override
        public void onAvrcpStateChanged(String address, int oldState, final int newState) throws RemoteException {
            _handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isBindView()) {
                        mViewRef.get().onAvrcpMediaPlayStateChanged(newState);
                    }
                }
            });
        }

        @Override
        public void onAvrcpPlayStateChanged(final int oldState, final int newState) throws RemoteException {
            Logutil.i(TAG, "onAvrcpPlayStateChanged() oldState=" + oldState + ",newState=" + newState);
            if (isBindView()) {
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isBindView()) {
                            mViewRef.get().onAvrcpMediaPlayStateChanged(newState);
                            mViewRef.get().onPlayStateChange(BTConst.AVRCP_PLAYING_STATE_ID_PLAYING == newState);
                        }
                    }
                });
            }
        }

        @Override
        public void onAvrcpMediaMetadataChanged(final int[] metadataIds, final String[] metadataValues) throws RemoteException {
            if (isBindView()) {
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isBindView()) {
                            String title = null;
                            String artist = null;
                            String album = null;
                            if (null != metadataIds && null != metadataValues) {
                                for (int i = 0; i < metadataIds.length; i++) {
                                    switch (i) {
                                        case BTConst.AVRCP_METADATA_ID_TITLE:
                                            title = metadataValues[i];
                                            break;
                                        case BTConst.AVRCP_METADATA_ID_ARTIST:
                                            artist = metadataValues[i];
                                            break;
                                        case BTConst.AVRCP_METADATA_ID_ALBUM:
                                            album = metadataValues[i];
                                            break;
                                    }
                                }
                            }
                            mViewRef.get().onSongNameChanged(title);
                            mViewRef.get().onSongArtistChanged(artist);
                            mViewRef.get().onSongAlbumChanged(album);
                        }
                    }
                });
            }
        }
    };

    // Constrctor
    public BTMusicPlayerPresenter() {
        Logutil.i(TAG, "init() ...");
        BTRoutingCommandManager.getInstance().registerCallback(mOnServiceStateListener);
        BTRoutingCommandManager.getInstance().registerA2dpCallback(mISmkCallbackA2dp);
        BTRoutingCommandManager.getInstance().registerAvrcpCallback(mISmkCallbackAvrcp);
    }

    @Override
    public void onPrevProgram() {
        BTRoutingCommandManager.getInstance().reqAvrcpBackward();
    }

    @Override
    public void onNextProgram() {
        BTRoutingCommandManager.getInstance().reqAvrcpForward();
    }

    @Override
    public void onPlayOrPause() {
        BTRoutingCommandManager.getInstance().reqAvrcpPlayOrPause();
    }

    @Override
    public void onRefreshBtMusicInfos() {
        Logutil.i(TAG, "onRestoreBtMusicInfos() ...");
        if (!isBindView()) {
            Logutil.i(TAG, "unbind view ...");
            return;
        }
        int a2dpState = BTRoutingCommandManager.getInstance().getA2dpConnectionState();
        int avrcpMediaPlayState = BTRoutingCommandManager.getInstance().getAvrcpMediaPlayState();
        String title = BTRoutingCommandManager.getInstance().getBtMusicTitle();
        String artist = BTRoutingCommandManager.getInstance().getBtMusicArtist();
        String album = BTRoutingCommandManager.getInstance().getBtMusicAlbum();
        boolean isPlaying = (BTConst.AVRCP_PLAYING_STATE_ID_PLAYING == avrcpMediaPlayState);

        Logutil.i(TAG, "=========");
        Logutil.i(TAG, "onRestoreBtMusicInfos() a2dpState = " + a2dpState);
        Logutil.i(TAG, "onRestoreBtMusicInfos() avrcpMediaPlayState = " + avrcpMediaPlayState);
        Logutil.i(TAG, "onRestoreBtMusicInfos() isPlaying = " + isPlaying);
        Logutil.i(TAG, "onRestoreBtMusicInfos() title = " + title);
        Logutil.i(TAG, "onRestoreBtMusicInfos() a2dpState = " + artist);
        Logutil.i(TAG, "onRestoreBtMusicInfos() a2dpState = " + album);
        Logutil.i(TAG, "=========");

        mViewRef.get().onA2dpStateChanged(a2dpState);
        mViewRef.get().onAvrcpMediaPlayStateChanged(avrcpMediaPlayState);
        mViewRef.get().onPlayStateChange(isPlaying);
        mViewRef.get().onSongNameChanged(title);
        mViewRef.get().onSongArtistChanged(artist);
        mViewRef.get().onSongAlbumChanged(album);
    }

    @Override
    public void onDetachView() {
        super.onDetachView();
        BTRoutingCommandManager.getInstance().unregisterAvrcpCallback(mISmkCallbackAvrcp);
        BTRoutingCommandManager.getInstance().unregisterA2dpCallback(mISmkCallbackA2dp);
        BTRoutingCommandManager.getInstance().unregisterCallback(mOnServiceStateListener);
    }
}
