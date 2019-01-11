package com.smk.autoradio.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.smk.autoradio.application.RadioApplication;
import com.smk.autoradio.constants.RadioConst;

public class PreferencesUtil {
    private static final String TAG = Logutil.makeTagLog(PreferencesUtil.class);
    private static final String SHARE_NAME = "RadioPreferences";

    private static SharedPreferences getSP() {
        if (null != RadioApplication.getContext()) {
            return RadioApplication.getContext().getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        }
        return null;
    }

    // 保存首次进入收音机标识
    public static void saveFirstOpenRadioFlag(boolean isFirstOpenRadio) {
        if (null != getSP()) {
            getSP().edit().putBoolean("isFirstOpenRadio", isFirstOpenRadio).commit();
        }
    }

    // 是否首次打开收音机
    public static boolean isFirstOpenRadio() {
        if (null != getSP()) {
            return getSP().getBoolean("isFirstOpenRadio", true);
        }
        return true;
    }

    // 保存断点频道类型
    public static void saveLastChannelType(int channelType) {
        if (null != getSP()) {
            getSP().edit().putInt("channelType", channelType).commit();
        }
    }

    // 获取断点频道类型
    public static int getLastChannelType() {
        if (null != getSP()) {
            return getSP().getInt("channelType", RadioConst.CHANNEL_TYPE_FM1);
        }
        return RadioConst.CHANNEL_TYPE_FM1;
    }

    // 保存断点收音机区域
    public static void saveLastNationalRegion(int nationalRegion) {
        if (null != getSP()) {
            getSP().edit().putInt("nationalRegion", nationalRegion).commit();
        }
    }

    // 获取断点收音机区域
    public static int getLastNationalRegion() {
        if (null != getSP()) {
            return getSP().getInt("nationalRegion", RadioConst.NATIONAL_REGING_INVALID);
        }
        return RadioConst.NATIONAL_REGING_INVALID;
    }

    //保存频道
    public static void saveLastChannel(int channelType, int channel) {
        if (null != getSP()) {
            switch (channelType) {
                case RadioConst.CHANNEL_TYPE_FM:
                    getSP().edit().putInt("channel_fm", channel).commit();
                    break;
                case RadioConst.CHANNEL_TYPE_FM1:
                    getSP().edit().putInt("channel_fm1", channel).commit();
                    break;
                case RadioConst.CHANNEL_TYPE_FM2:
                    getSP().edit().putInt("channel_fm2", channel).commit();
                    break;
                case RadioConst.CHANNEL_TYPE_AM:
                    getSP().edit().putInt("channel_am", channel).commit();
                    break;
            }

        }
    }

    // 获取频道
    public static int getLastChannel(int channelType) {
        int channel = -1;
        if (null != getSP()) {
            switch (channelType) {
                case RadioConst.CHANNEL_TYPE_FM:
                    channel = getSP().getInt("channel_fm", RadioConst.CHANNEL_INVALID);
                    break;
                case RadioConst.CHANNEL_TYPE_FM1:
                    channel = getSP().getInt("channel_fm1", RadioConst.CHANNEL_INVALID);
                    break;
                case RadioConst.CHANNEL_TYPE_FM2:
                    channel = getSP().getInt("channel_fm2", RadioConst.CHANNEL_INVALID);
                    break;
                case RadioConst.CHANNEL_TYPE_AM:
                    channel = getSP().getInt("channel_am", RadioConst.CHANNEL_INVALID);
                    break;
            }
        }
        return channel;
    }
}
