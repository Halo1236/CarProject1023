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
    public static void saveLastNationalRegion(int nationalRegion){
        if(null != getSP()){
            getSP().edit().putInt("nationalRegion",nationalRegion).commit();
        }
    }

    // 获取断点收音机区域
    public static int getLastNationalRegion(){
        if(null != getSP()){
            return getSP().getInt("nationalRegion",RadioConst.NATIONAL_REGING_INVALID);
        }
        return RadioConst.NATIONAL_REGING_INVALID;
    }
}
