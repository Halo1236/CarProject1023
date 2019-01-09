package com.smk.autoradio.service.player;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;

import com.semisky.autoservice.aidl.INationalRegionChanged;
import com.semisky.autoservice.aidl.IRadioSearchListener;
import com.semisky.autoservice.aidl.IRadioSeekListener;
import com.semisky.autoservice.manager.AutoConstants;
import com.semisky.autoservice.manager.RadioManager;
import com.smk.autoradio.constants.RadioConst;
import com.smk.autoradio.utils.Logutil;
import com.smk.autoradio.utils.PreferencesUtil;

import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_PLAY_AM;
import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_PLAY_FM;
import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_RESTORE_PLAY;
import static com.smk.autoradio.constants.RadioConst.RadioCmd.CMD_STOP_SEARCH;

public class RadioPlayer implements IRadioPlayer {

    private static final String TAG = Logutil.makeTagLog(RadioPlayer.class);
    private OnRadioPlayerStatusListener mOnRadioPlayerStatusListener;
    private int mCurrentChannelType = RadioConst.CHANNEL_TYPE_FM1;// 当前频道类型
    private boolean mIsFullSearchChannel = false;// 是否在全搜
    private boolean mIsStrongChannelSearch = false;// 是否在强台搜索


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

            switch (type) {
                case RadioManager.CMD_FM_SEARCH_REPORT:// 全搜时，上报FM频段信息
                    if (null != mOnRadioPlayerStatusListener) {
                        mOnRadioPlayerStatusListener.onFullSearchChannelReport(freq);
                    }
                    break;
                case RadioManager.CMD_AM_SEARCH_REPORT:// 全搜时，上报AM频段信息
                    if (null != mOnRadioPlayerStatusListener) {
                        mOnRadioPlayerStatusListener.onFullSearchChannelReport(freq);
                    }
                    break;
            }
            if (freq == 0) {// 频道全搜完成
                if (null != mOnRadioPlayerStatusListener) {
                    mIsFullSearchChannel = false;// 全搜结束标识
                    mOnRadioPlayerStatusListener.onFullSearchChannelEnd();
                }
            }
        }
    };

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
        sendMessage(CMD_RESTORE_PLAY, 0);
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

    }

    @Override
    public void reqAddCurrentChannelToFavorite() {

    }

    @Override
    public void reqGetFavoriteList(int channelType) {

    }

    @Override
    public void reqGetSearchList(int channelType) {

    }

    @Override
    public void reqSwitchDxOrLoc() {

    }

    @Override
    public int getSoundtrackType() {
        return 0;
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

    private void sendMessage(int what, long delayMillis) {
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
                case CMD_PLAY_FM:
                    break;
                case CMD_PLAY_AM:
                    break;
                case CMD_STOP_SEARCH:
                    RadioManager.getInstance().StopSearch();
                    break;
            }
            return false;
        }
    }

    // 恢复收音机播放
    private void restorePlay() {

        int lastNationalRegion = PreferencesUtil.getLastNationalRegion();// 断点收音机区域标识
        int nationalRegion = RadioManager.getInstance().getNationalRegion();// 中间件收音机区域标识
        // 检查收音机区域是否合法，如果不合格赋值默认收音机区域
        nationalRegion = (nationalRegion >= AutoConstants.NationalRegion.CHINA &&
                nationalRegion <= AutoConstants.NationalRegion.RUSSIA
                ? nationalRegion : AutoConstants.NationalRegion.CHINA);

        boolean isFirstOpenRadio = PreferencesUtil.isFirstOpenRadio();// 是否首次打开收音机

        // 首次使用收音机
        if (isFirstOpenRadio) {
            fullSearchChannel(RadioConst.CHANNEL_TYPE_FM1);// 开始全搜
            PreferencesUtil.saveFirstOpenRadioFlag(false);// 将首次使用收音机置为FALSE
            PreferencesUtil.saveLastNationalRegion(nationalRegion);
            return;
        }

        // 收音机区域变化
        boolean isNationalRegionChanged = lastNationalRegion == nationalRegion;// 是不收音机区域改变
        if (isNationalRegionChanged) {

            return;
        }

        // 恢复播放断点收音机
    }


    // 全搜
    private void fullSearchChannel(int channelType) {
        switch (channelType) {
            case RadioConst.CHANNEL_TYPE_FM1:
                RadioManager.getInstance().FMSearch(0);
                break;
            case RadioConst.CHANNEL_TYPE_FM2:
                RadioManager.getInstance().FMSearch(0);
                break;
            case RadioConst.CHANNEL_TYPE_AM:
                RadioManager.getInstance().AMSearch(0);
                break;
        }

        if (channelType >= RadioConst.CHANNEL_TYPE_FM1 && channelType <= RadioConst.CHANNEL_TYPE_AM) {
            if (null != mOnRadioPlayerStatusListener) {
                this.mIsFullSearchChannel = true;// 全搜开始标识
                mOnRadioPlayerStatusListener.onFullSearchChannelStart();
            }
        }
    }

    // 播放指定频道
    private void playChannel(int channelType,int channel){
        switch (channelType) {
            case RadioConst.CHANNEL_TYPE_FM1:
                RadioManager.getInstance().FMSearch(channel);
                break;
            case RadioConst.CHANNEL_TYPE_FM2:
                RadioManager.getInstance().FMSearch(channel);
                break;
            case RadioConst.CHANNEL_TYPE_AM:
                RadioManager.getInstance().AMSearch(channel);
                break;
        }
    }

    // 下个强台
    private void nextStrongChannel(int channel){
        RadioManager.getInstance().SeekUp(channel);
    }

    // 上个强台
    private void prevStrongChannel(int channel){
        RadioManager.getInstance().SeekDown(channel);
    }


    private void handlerRadioInterupt(){
        Logutil.i(TAG,"handlerRadioInterupt() mIsFullSearchChannel : "+ mIsFullSearchChannel);
        Logutil.i(TAG,"handlerRadioInterupt() mIsStrongChannelSearch : "+ mIsStrongChannelSearch);
        if(mIsFullSearchChannel || mIsStrongChannelSearch){
            sendMessage(CMD_STOP_SEARCH,0);
        }
    }


}
