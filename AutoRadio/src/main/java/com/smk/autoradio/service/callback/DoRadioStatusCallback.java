package com.smk.autoradio.service.callback;

import android.os.Message;

import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.aidl.IRadioStatusListener;
import com.smk.autoradio.utils.Logutil;

import java.util.List;


public class DoRadioStatusCallback extends DoBaseCallback<IRadioStatusListener> {
    // message ids
    private final int onFullSearchChannelStart = 0;
    private final int onFullSearchChannelReport = 1;
    private final int onFullSearchChanelEnd = 2;
    private final int onStrongChannelSearchStart = 3;
    private final int onStrongChannelSearchReport = 4;
    private final int onStrongChannelSearchEnd = 5;
    private final int onLoadFullSearchChannelList = 6;
    private final int onLoadFavoriteChannelList = 7;
    private final int onLoadFullSearchAndFavoriteChannelList = 8;
    private final int onChannelTypeChanged = 9;
    private final int onChannelRangeChanged = 10;
    private final int onChannelSoundtrackTypeChanged = 11;
    private final int onChannelDxLocTypeChanged = 12;


    public DoRadioStatusCallback() {
        super.TAG = getLogTag();
    }

    @Override
    protected String getLogTag() {
        return Logutil.makeTagLog(DoRadioStatusCallback.class);
    }

    @Override
    protected void dequeueMessage(Message msg) {
        switch (msg.what) {
            case onFullSearchChannelStart:
                notifyFullSearchChannelStart();
                break;
            case onFullSearchChannelReport:
                notifyFullSearchChannelReport(msg.arg1);
                break;
            case onFullSearchChanelEnd:
                notifyFullSearchChanelEnd();
                break;
            case onStrongChannelSearchStart:
                notifyStrongChannelSearchStart();
                break;
            case onStrongChannelSearchReport:
                notifyStrongChannelSearchReport(msg.arg1);
                break;
            case onStrongChannelSearchEnd:
                notifyStrongChannelSearchEnd();
                break;
            case onLoadFullSearchChannelList:
                notifyLoadFullSearchChannelList((List<ChannelInfo>) msg.obj);
                break;
            case onLoadFavoriteChannelList:
                notifyLoadFavoriteChannelList((List<ChannelInfo>) msg.obj);
                break;
            case onLoadFullSearchAndFavoriteChannelList:
                notifyLoadFullSearchAndFavoriteChannelList((List<ChannelInfo>) msg.obj);
                break;
            case onChannelTypeChanged:
                notifyChannelTypeChanged(msg.arg1);
                break;
            case onChannelRangeChanged:
                break;
            case onChannelSoundtrackTypeChanged:
                notifyChannelSoundtrackTypeChanged(msg.arg1);
                break;
            case onChannelDxLocTypeChanged:
                notifyChannelDxLocTypeChanged(msg.arg1);
                break;
        }
    }

    public synchronized void onFullSearchChannelStart() {
        Message msg = super.getMessage(onFullSearchChannelStart);
        super.enqueueMessage(msg);
    }

    public synchronized void onFullSearchChannelReport(int channel) {
        Message msg = super.getMessage(onFullSearchChannelReport);
        msg.arg1 = channel;
        super.enqueueMessage(msg);
    }

    public synchronized void onFullSearchChanelEnd() {
        Message msg = super.getMessage(onFullSearchChanelEnd);
        super.enqueueMessage(msg);
    }

    public synchronized void onStrongChannelSearchStart() {
        Message msg = super.getMessage(onStrongChannelSearchStart);
        super.enqueueMessage(msg);
    }

    public synchronized void onStrongChannelSearchReport(int channel) {
        Message msg = super.getMessage(onStrongChannelSearchReport);
        msg.arg1 = channel;
        super.enqueueMessage(msg);
    }

    public synchronized void onStrongChannelSearchEnd() {
        Message msg = super.getMessage(onStrongChannelSearchEnd);
        super.enqueueMessage(msg);
    }

    public synchronized void onLoadFullSearchChannelList(List<ChannelInfo> channelList) {
        Message msg = super.getMessage(onLoadFullSearchChannelList);
        msg.obj = channelList;
        super.enqueueMessage(msg);
    }

    public synchronized void onLoadFavoriteChannelList(List<ChannelInfo> channelList) {
        Message msg = super.getMessage(onLoadFavoriteChannelList);
        msg.obj = channelList;
        super.enqueueMessage(msg);
    }

    public synchronized void onLoadFullSearchAndFavoriteChannelList(List<ChannelInfo> channelList){
        Message msg = super.getMessage(onLoadFavoriteChannelList);
        msg.obj = channelList;
        super.enqueueMessage(msg);
    }

    public synchronized void onChannelTypeChanged(int channelType) {
        Message msg = super.getMessage(onChannelTypeChanged);
        msg.arg1 = channelType;
        super.enqueueMessage(msg);
    }

    public synchronized void onChannelSoundtrackTypeChanged(int soundtrackType) {
        Message msg = super.getMessage(onChannelSoundtrackTypeChanged);
        msg.arg1 = soundtrackType;
        super.enqueueMessage(msg);
    }

    public synchronized void onChannelDxLocTypeChanged(int channelDxLocType) {
        Message msg = super.getMessage(onChannelDxLocTypeChanged);
        msg.arg1 = channelDxLocType;
        super.enqueueMessage(msg);
    }

    public synchronized void onChannelRangeChanged(int channelValueMin, int channelValueMax) {
        Message msg = super.getMessage(onChannelRangeChanged);
        msg.arg1 = channelValueMin;
        msg.arg2 = channelValueMax;
        super.enqueueMessage(msg);
    }

    // 全搜开始通知
    private void notifyFullSearchChannelStart() {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onFullSearchChannelStart();
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyFullSearchChannelStart() fail !!!");
            e.printStackTrace();
        }
    }

    // 全搜开始过程中上报频道通知
    private void notifyFullSearchChannelReport(int channel) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onFullSearchChannelReport(channel);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyFullSearchChannelReport() fail !!!");
            e.printStackTrace();
        }
    }

    // 全搜结束通知
    private void notifyFullSearchChanelEnd() {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onFullSearchChanelEnd();
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyFullSearchChanelEnd() fail !!!");
            e.printStackTrace();
        }
    }

    // 强频道搜索开始通知
    private void notifyStrongChannelSearchStart() {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onStrongChannelSearchStart();
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyStrongChannelSearchStart() fail !!!");
            e.printStackTrace();
        }
    }

    // 强频道搜索过程中上报频道通知
    private void notifyStrongChannelSearchReport(int channel) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onStrongChannelSearchReport(channel);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyStrongChannelSearchReport() fail !!!");
            e.printStackTrace();
        }
    }

    // 强频道搜索结束通知
    private void notifyStrongChannelSearchEnd() {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onStrongChannelSearchEnd();
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyStrongChannelSearchEnd() fail !!!");
            e.printStackTrace();
        }
    }

    // 加载全搜频段清单列表，根据频道类型
    private void notifyLoadFullSearchChannelList(List<ChannelInfo> channelList) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onLoadFullSearchChannelList(channelList);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyLoadFullSearchChannelList() fail !!!");
            e.printStackTrace();
        }
    }

    // 加载收藏频段清单列表，根据频道类型
    private void notifyLoadFavoriteChannelList(List<ChannelInfo> channelList) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onLoadFavoriteChannelList(channelList);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyLoadFavoriteChannelList() fail !!!");
            e.printStackTrace();
        }
    }

    private void notifyLoadFullSearchAndFavoriteChannelList(List<ChannelInfo> channelList) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onLoadFullSearchAndFavoriteChannelList(channelList);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyLoadFullSearchAndFavoriteChannelList() fail !!!");
            e.printStackTrace();
        }
    }

    // 频道类型改变通知
    private void notifyChannelTypeChanged(int channelType) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onChannelTypeChanged(channelType);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyChannelTypeChanged() fail !!!");
            e.printStackTrace();
        }
    }

    // 频道范围改变通知
    private void notifyChannelRangeChanged(int channelValueMin, int channelValueMax) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onChannelRangeChanged(channelValueMin, channelValueMax);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyChannelRangeChanged() fail !!!");
            e.printStackTrace();
        }
    }

    // 频道类型改变通知
    private void notifyChannelSoundtrackTypeChanged(int soundtrackType) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onChannelSoundtrackTypeChanged(soundtrackType);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyChannelSoundtrackTypeChanged() fail !!!");
            e.printStackTrace();
        }
    }

    // 远近程类型改变通知
    private void notifyChannelDxLocTypeChanged(int channelDxLocType) {
        try {
            synchronized (super.mRemoteCallbackList) {
                final int N = super.mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    super.mRemoteCallbackList.getBroadcastItem(i).onChannelDxLocTypeChanged(channelDxLocType);
                }
                super.mRemoteCallbackList.finishBroadcast();
            }
        } catch (Exception e) {
            Logutil.e(TAG, "notifyChannelDxLocTypeChanged() fail !!!");
            e.printStackTrace();
        }
    }
}
