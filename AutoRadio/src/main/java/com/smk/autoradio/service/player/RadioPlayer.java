package com.smk.autoradio.service.player;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;

import com.semisky.autoservice.aidl.INationalRegionChanged;
import com.semisky.autoservice.aidl.IRadioSearchListener;
import com.semisky.autoservice.aidl.IRadioSeekListener;
import com.semisky.autoservice.manager.RadioManager;
import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.application.RadioApplication;
import com.smk.autoradio.constants.RadioConst;
import com.smk.autoradio.model.SmkIVIManager;
import com.smk.autoradio.utils.Logutil;
import com.smk.autoradio.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_FULL_SEARCH;
import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_NEXT_STRONG_CHANNEL;
import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_PLAY_CHANNEL;
import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_PREV_STRONG_CHANNEL;
import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_RESTORE_PLAY;
import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_STOP_SEARCH;

public class RadioPlayer implements IRadioPlayer {

    private static final String TAG = Logutil.makeTagLog(RadioPlayer.class);
    private OnRadioPlayerStatusListener mOnRadioPlayerStatusListener;
    private List<ChannelInfo> mFullSearchChannelInfoList = new ArrayList<ChannelInfo>();

    private int mCurrentNationalRegion = -1;// 当前收音机区域
    private int mCurrentChannelType = -1;// 当前频道类型
    private int mCurrentSoundtrack = RadioConst.SOUNDTRACK_TYPE_INVALID;// 当前声道类型
    private int mChannelMin = -1;// 频段最小值
    private int mChannelMax = -1;// 频段最大值

    private boolean mIsFullSearchChannel = false;// 是否在全搜
    private boolean mIsStrongChannelSearch = false;// 是否在强台搜索
    private boolean mIsInterruptSearch = false;// 是否打断搜索


    public RadioPlayer() {
        initRadioHandlerThread();
        RadioManager.getInstance().registerOnSearchResultListener(mIRadioSearchListener);
        RadioManager.getInstance().registerNationalRegionChangedtListener(mINationalRegionChanged);
        RadioManager.getInstance().registerOnSeekResultListener(mIRadioSeekListener);
    }


    // 全搜状态监听
    private IRadioSearchListener.Stub mIRadioSearchListener = new IRadioSearchListener.Stub() {
        @Override
        public void onSearchResult(int type, int freq, int signal, int soundtrack) throws RemoteException {

            // 全搜过程中
            if (RadioManager.CMD_FM_SEARCH_REPORT == type || RadioManager.CMD_AM_SEARCH_REPORT == type) {
                addChannelToList(new ChannelInfo(freq, mCurrentNationalRegion, mCurrentChannelType, signal, false));
                if (null != mOnRadioPlayerStatusListener) {
                    mOnRadioPlayerStatusListener.onFullSearchChannelReport(freq);
                }
            }
            // 频道全搜完成
            if (freq == 0) {
                // 手动打断全搜，结束搜索事件
                if (mIsInterruptSearch || mFullSearchChannelInfoList.isEmpty()) {
                    removeChannelList();
                    // 播放之前的那个频道
                    int lastChannel = PreferencesUtil.getLastChannel(mCurrentChannelType);
                    // 检查频段是否合法
                    lastChannel = SmkIVIManager.getInstance().reviseChannel(mCurrentNationalRegion,mCurrentChannelType,lastChannel);
                    sendMsgPlayChannel(mCurrentChannelType,lastChannel);// 播放指定频道
                    sendCmdToHandlerCenter(RadioConst.RadioCmd.CMD_UNMUTE,0);// 解静音
                }
                // 正常频道搜索结束
                else {

                }
                if (null != mOnRadioPlayerStatusListener) {
                    mIsFullSearchChannel = false;// 全搜结束标识
                    mOnRadioPlayerStatusListener.onFullSearchChannelEnd();
                }
            }
        }
    };

    private void addChannelToList(ChannelInfo channelInfo) {
        synchronized (mFullSearchChannelInfoList) {
            mFullSearchChannelInfoList.add(channelInfo);
        }
    }

    private void removeChannelList() {
        synchronized (mFullSearchChannelInfoList) {
            mFullSearchChannelInfoList.clear();
        }
    }

    // 收音机区域改变监听
    private INationalRegionChanged.Stub mINationalRegionChanged = new INationalRegionChanged.Stub() {
        @Override
        public void onNationalRegion(int i) throws RemoteException {

        }
    };

    // 强频段改变监听（上或下一个强频段）
    private IRadioSeekListener.Stub mIRadioSeekListener = new IRadioSeekListener.Stub() {
        @Override
        public void onSeekResult(int type, int freq, int signal, int soundtrack) throws RemoteException {
            switch (type) {
                case RadioManager.CMD_FM_SEARCH_REPORT:
                    break;
                case RadioManager.CMD_AM_SEARCH_REPORT:
                    break;
            }
            if (freq == 0) {// 强台搜完成

            }
        }
    };


    @Override
    public void registerOnRadioPlayerStatusListener(OnRadioPlayerStatusListener l) {
        mOnRadioPlayerStatusListener = l;
    }


    @Override
    public void unregisterOnRadioPlayerStatusListener() {

    }

    @Override
    public void reqRestoreChannelPlay() {
        sendCmdToHandlerCenter(CMD_RESTORE_PLAY, 0);
    }

    @Override
    public int getChannelType() {
        return this.mCurrentChannelType;
    }

    @Override
    public int getChannel() {
        return 0;
    }

    @Override
    public void reqSwitchBand() {

    }

    @Override
    public void reqPrevStrongChannel() {

    }

    @Override
    public void reqNextStroneChannel() {

    }

    @Override
    public void reqStepIncreaseChannel() {

    }

    @Override
    public void reqStepDecreaseChannel() {

    }

    @Override
    public void reqPlayChannel(int channelType, int channel) {

    }

    @Override
    public void reqFullSearch() {

    }

    @Override
    public void reqSettingEQ() {
        Intent i = new Intent();
        i.setClassName("xxx", "xxx");
        RadioApplication.getContext().startActivity(i);
    }

    @Override
    public void reqAddCurrentChannelToFavorite() {

    }

    @Override
    public void reqGetFavoriteList() {

    }

    @Override
    public void reqGetSearchList() {

    }

    @Override
    public void reqGetFullSearchAndFavoriteChannelList() {

    }

    @Override
    public void reqSwitchDxOrLoc() {

    }

    @Override
    public int getSoundtrackType() {
        return this.mCurrentSoundtrack;
    }

    @Override
    public int getMinChannelValue() {
        return this.mChannelMin;
    }

    @Override
    public int getMaxChannelValue() {
        return this.mChannelMax;
    }

    // utils
    private RadioHandlerThread mRadioHandlerThread;
    private Handler mHandler;

    private void initRadioHandlerThread() {
        if (null == mRadioHandlerThread) {
            mRadioHandlerThread = new RadioHandlerThread("RadioHandlerThread");
            mRadioHandlerThread.start();
            if (null == mHandler) {
                mHandler = new Handler(mRadioHandlerThread.getLooper(), mRadioHandlerThread);
            }
        }
    }

    // 发送消息播放指定频道
    private void sendMsgPlayChannel(int ChannelType,int channel){
        Message msg = mHandler.obtainMessage(RadioConst.RadioCmd.CMD_PLAY_CHANNEL);
        msg.arg1 = ChannelType;
        msg.arg2 = channel;
        mHandler.sendMessageDelayed(msg,0);
    }

    // 请求全搜消息
    private void sendMessageFullSearchChannel(int channelType) {
        Message msg = mHandler.obtainMessage();
        msg.what = RadioConst.RadioCmd.CMD_FULL_SEARCH;
        msg.arg1 = channelType;
        mHandler.sendMessageDelayed(msg, 0);

    }

    // 发送指令到处理中心
    private void sendCmdToHandlerCenter(int what, long delayMillis) {
        mHandler.removeMessages(what);
        mHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    private final class RadioHandlerThread extends HandlerThread implements Handler.Callback {

        public RadioHandlerThread(String name) {
            super(name);
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CMD_RESTORE_PLAY:
                    restorePlay();
                    break;
                case CMD_PLAY_CHANNEL:
                    SmkIVIManager.getInstance().reqPlayChannel(msg.arg1,msg.arg2);
                    break;
                case CMD_STOP_SEARCH:
                    mIsInterruptSearch = true;// 打断搜台标识
                    SmkIVIManager.getInstance().reqStopSearch();
                    break;
                case CMD_FULL_SEARCH:
                    if (!isChannelSearching()) {
                        SmkIVIManager.getInstance().reqFullSearchChannel(msg.arg1);
                        if (null != mOnRadioPlayerStatusListener) {
                            mIsFullSearchChannel = true;// 全搜开始标识
                            mOnRadioPlayerStatusListener.onFullSearchChannelStart();
                        }
                    }
                    break;
                case CMD_PREV_STRONG_CHANNEL:
                    if (!isChannelSearching()) {
                        SmkIVIManager.getInstance().reqPrevStrongChannel(msg.arg1);
                    }
                    break;
                case CMD_NEXT_STRONG_CHANNEL:
                    if (!isChannelSearching()) {
                        SmkIVIManager.getInstance().reqNextStrongChannel(msg.arg1);
                    }
                    break;
            }
            return false;
        }
    }

    // 恢复收音机播放
    private void restorePlay() {
        boolean isFirstOpenRadio = PreferencesUtil.isFirstOpenRadio();// 是否首次打开收音机
        Logutil.i(TAG, "restorePlay() isFirstOpenRadio : " + isFirstOpenRadio);
        // 首次使用收音机
        if (isFirstOpenRadio) {
            firstPlayRadio();
            return;
        }

        // 收音机区域改变
        int lastNationalRegion = PreferencesUtil.getLastNationalRegion();// 断点收音机区域标识
        int nationalRegion = SmkIVIManager.getInstance().getNationalRegion();// 中间件收音机区域
        boolean isNationalRegionChanged = (lastNationalRegion != nationalRegion);// 是否收音机区域改变

        Logutil.i(TAG, "restorePlay() lastNationalRegion : " + lastNationalRegion);
        Logutil.i(TAG, "restorePlay() nationalRegion : " + nationalRegion);
        Logutil.i(TAG, "restorePlay() isNationalRegionChanged : " + isNationalRegionChanged);

        if (isNationalRegionChanged) {
            playDefaultFMChannelWithNationalRegionChanaged();
            //1.)将FM1,FM2,AM频道清单清空(数据库及类成员频道集合全部清空)
            //2.)通知界面进度条更新频道范围
            //3.)播放该收音机区域的FM1最小频道
            //4.)频道声道类型、远近程类型、频道类型、收藏更新状态
            return;
        }

        // 恢复播放断点收音机
    }

    // 当然收音机区域改变，播放默认FM频道
    private void playDefaultFMChannelWithNationalRegionChanaged() {

    }

    // 首次播放收音机
    private void firstPlayRadio() {
        this.mCurrentNationalRegion = SmkIVIManager.getInstance().getNationalRegion(); // 初始化收音机区域
        this.mCurrentChannelType = SmkIVIManager.getInstance().getDefaultChannelTypeForNationalRegion(mCurrentNationalRegion);// 默认频道类型为FM
        this.mChannelMin = SmkIVIManager.getInstance().getChannelRangeForNationalRegionAndChannelType(false, mCurrentNationalRegion, mCurrentChannelType);// 获取频道最小值
        this.mChannelMax = SmkIVIManager.getInstance().getChannelRangeForNationalRegionAndChannelType(true, mCurrentNationalRegion, mCurrentChannelType);// 获取频道最大值
        int soundtrackType = SmkIVIManager.getInstance().getSoundtrackType(mCurrentChannelType);// 声道类型
        int dxLocType = SmkIVIManager.getInstance().getDxLocType(mCurrentChannelType);// 远近程类型

        if (null != mOnRadioPlayerStatusListener) {
            mOnRadioPlayerStatusListener.onChannelTypeChanged(mCurrentChannelType);// 频道类型改变通知
            mOnRadioPlayerStatusListener.onChannelRangeChanged(mChannelMin, mChannelMax);// 频道范围改变通知
            mOnRadioPlayerStatusListener.onChannelSoundtrackTypeChanged(soundtrackType);// 声道类型改变通知
            mOnRadioPlayerStatusListener.onChannelDxLocTypeChanged(dxLocType);// 远近程类型改变通知
        }
        PreferencesUtil.saveFirstOpenRadioFlag(false);// 置为不是首次打开收音机标识
        sendMessageFullSearchChannel(mCurrentChannelType);// 启动全搜
    }


    // 是否频道搜索状态
    private boolean isChannelSearching() {
        return (mIsFullSearchChannel || mIsStrongChannelSearch);
    }

}
