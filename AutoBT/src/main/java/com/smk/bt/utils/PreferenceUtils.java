package com.smk.bt.utils;

public class PreferenceUtils {
    private static int mAppFlag = -1;

    public static void cacheAppFlag(int mAppFlag){
        mAppFlag = mAppFlag;
    }

    public static int getCacheAppFlag(){
        return mAppFlag;
    }
}
