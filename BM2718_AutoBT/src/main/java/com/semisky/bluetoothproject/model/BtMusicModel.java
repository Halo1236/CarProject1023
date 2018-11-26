package com.semisky.bluetoothproject.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.nforetek.bt.res.NfDef;
import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.entity.MusicSongStatus;
import com.semisky.bluetoothproject.presenter.viewInterface.BtMusicPlayStatusInterface;
import com.semisky.bluetoothproject.responseinterface.BtMusicStatusResponse;
import com.semisky.bluetoothproject.utils.BtUtils;
import com.semisky.bluetoothproject.utils.Logger;

/**
 * Created by chenhongrui on 2018/8/15
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtMusicModel implements BtMusicStatusResponse {

    private static final String TAG = Logger.makeTagLog(BtMusicModel.class);

    private Context mContext;

    private BtMusicModel() {
        this.mContext = BtApplication.getContext();
    }

    public static BtMusicModel getInstance() {
        return BtMusicModelHolder.instance;
    }

    private static class BtMusicModelHolder {
        private static final BtMusicModel instance = new BtMusicModel();
    }

    private BtMusicPlayStatusInterface btMusicPlayStatusInterface;

    public void setBtMusicPlayStatus(BtMusicPlayStatusInterface btMusicPlayStatus) {
        this.btMusicPlayStatusInterface = btMusicPlayStatus;
    }

    @Override
    public void onAvrcpStateChanged(String address, int prevState, int newState) {

    }

    @Override
    public void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId) {
        //换了蓝牙模块，此块废弃
//        Logger.d(TAG, "retAvrcp13PlayStatus:songLen " + songLen + " songPos " + songPos + " statusId " + statusId);
//        switch (statusId) {
//            case NfDef.AVRCP_PLAYING_STATUS_ID_STOPPED:
//                if (btMusicPlayStatusInterface != null) {
//                    btMusicPlayStatusInterface.stopPlay();
//                }
//                break;
//            case NfDef.AVRCP_PLAYING_STATUS_ID_PLAYING:
//                boolean topActivityName = BtUtils.getTopActivityName(mContext, mContext.getPackageName());
//                BtConstant.FragmentFlag fragmentFlag = BtStatusModel.getInstance().getFragmentFlag();
//                boolean isMusicView = fragmentFlag == BtConstant.FragmentFlag.MUSIC;
//                Log.d(TAG, "onAvrcp13EventPlaybackStatusChanged: isMusicView " + isMusicView);
//                Log.d(TAG, "onAvrcp13EventPlaybackStatusChanged: topActivityName " + topActivityName);
//                //1.当前是否处于蓝牙APP首页 2.当前是否处于蓝牙音乐
//                if ((topActivityName && isMusicView)) {
//                    if (btMusicPlayStatusInterface != null) {
//                        btMusicPlayStatusInterface.startPlaying();
//                        BtMusicAudioFocusModel.getINSTANCE().applyAudioFocus();
//                    }
//                }
//                break;
//            case NfDef.AVRCP_PLAYING_STATUS_ID_PAUSED:
//                //按照需求 当拥有音频焦点暂停时，不恢复播放。反之恢复播放(说明抢了其他的音源)
//                boolean hasAudioFocus = BtMusicAudioFocusModel.getINSTANCE().isHasAudioFocus();
//                if (btMusicPlayStatusInterface != null && !hasAudioFocus) {
//                    //如果当前没播放，则开始播放
//                    btMusicPlayStatusInterface.startPlay();
//                }
//                break;
//            case NfDef.AVRCP_PLAYING_STATUS_ID_FWD_SEEK:
//                break;
//            case NfDef.AVRCP_PLAYING_STATUS_ID_REW_SEEK:
//                break;
//
//            default:
//                break;
//        }
    }

    @Override
    public void retAvrcp13ElementAttributesPlaying(int[] metadataAtrributeIds, String[] texts) {
        MusicSongStatus musicSongStatus = new MusicSongStatus();
        if (metadataAtrributeIds != null) {
            for (int i = 0; i < metadataAtrributeIds.length; i++) {
                if (metadataAtrributeIds[i] == NfDef.AVRCP_META_ATTRIBUTE_ID_TITLE) {
                    String title = texts[i];
                    if (TextUtils.isEmpty(title)) {
                        musicSongStatus.setSongName((mContext.getString(R.string.cx62_bt_unknown_title)));
                    } else {
                        musicSongStatus.setSongName(title);
                    }

                } else if (metadataAtrributeIds[i] == NfDef.AVRCP_META_ATTRIBUTE_ID_ALBUM) {
                    String album = texts[i];
                    if (TextUtils.isEmpty(album)) {
                        musicSongStatus.setAlbum(mContext.getString(R.string.cx62_bt_unknown_album));
                    } else {
                        musicSongStatus.setAlbum(album);
                    }

                } else if (metadataAtrributeIds[i] == NfDef.AVRCP_META_ATTRIBUTE_ID_ARTIST) {
                    String artist = texts[i];
                    if (TextUtils.isEmpty(artist)) {
                        musicSongStatus.setArtist(mContext.getString(R.string.cx62_bt_unknown_artist));
                    } else {
                        musicSongStatus.setArtist(artist);
                    }

                }
            }
        }

//        Logger.d(TAG, "retAvrcp13ElementAttributesPlaying:SongName " + musicSongStatus.getSongName());
//        Logger.d(TAG, "retAvrcp13ElementAttributesPlaying:Album " + musicSongStatus.getAlbum());
//        Logger.d(TAG, "retAvrcp13ElementAttributesPlaying:Artist " + musicSongStatus.getArtist());

        if (btMusicPlayStatusInterface != null) {
            btMusicPlayStatusInterface.songStatus(musicSongStatus);
        }
    }

    @Override
    public void onAvrcp13EventPlaybackStatusChanged(byte statusId) {
        switch (statusId) {
            case NfDef.AVRCP_PLAYING_STATUS_ID_STOPPED:
                Logger.d(TAG, "onAvrcp13EventPlaybackStatusChanged: 停止播放");
                BtStatusModel.getInstance().setPlayMusic(false);
                if (btMusicPlayStatusInterface != null) {
                    btMusicPlayStatusInterface.stopPlay();
                }
                break;
            case NfDef.AVRCP_PLAYING_STATUS_ID_PLAYING:
                //判断当前是否处于蓝牙页面，否则不响应操作
                Logger.d(TAG, "onAvrcp13EventPlaybackStatusChanged: 播放中");
                BtStatusModel.getInstance().setPlayMusic(true);
                boolean topActivityName = BtUtils.getTopActivityName(mContext, mContext.getPackageName());
                BtConstant.FragmentFlag fragmentFlag = BtStatusModel.getInstance().getFragmentFlag();
                boolean isMusicView = fragmentFlag == BtConstant.FragmentFlag.MUSIC;
                Log.d(TAG, "onAvrcp13EventPlaybackStatusChanged: isMusicView " + isMusicView);
                Log.d(TAG, "onAvrcp13EventPlaybackStatusChanged: topActivityName " + topActivityName);
                //1.当前是否处于蓝牙APP首页 2.当前是否处于蓝牙音乐
                if ((topActivityName && isMusicView)) {
                    if (btMusicPlayStatusInterface != null) {
                        btMusicPlayStatusInterface.startPlaying();
                    }
                    //第一次连接时，进入蓝牙音乐界面，通过手机播放音乐，retAvrcp13PlayStatus获取不了当前播放状态
                    boolean hasAudioFocus = BtMusicAudioFocusModel.getINSTANCE().isHasAudioFocus();
                    if (!hasAudioFocus) {
                        BtMusicAudioFocusModel.getINSTANCE().applyAudioFocus();
                    }
                    BtMusicAudioFocusModel.getINSTANCE().setPauseMusic(false);
                } else {
                    Log.e(TAG, "onAvrcp13EventPlaybackStatusChanged: 非蓝牙音乐页面不响应播放");
                }

                break;
            case NfDef.AVRCP_PLAYING_STATUS_ID_PAUSED:
                Logger.d(TAG, "onAvrcp13EventPlaybackStatusChanged: 暂停");
                BtStatusModel.getInstance().setPlayMusic(false);
                if (btMusicPlayStatusInterface != null) {
                    btMusicPlayStatusInterface.pausePlay();
                }

                //打电话也会走暂停回调
                if (!BtStatusModel.getInstance().isCallPhone()) {
                    BtMusicAudioFocusModel.getINSTANCE().setPauseMusic(true);
                }
                break;
            case NfDef.AVRCP_PLAYING_STATUS_ID_FWD_SEEK:
                break;
            case NfDef.AVRCP_PLAYING_STATUS_ID_REW_SEEK:
                break;

            default:
                break;
        }
    }


}
