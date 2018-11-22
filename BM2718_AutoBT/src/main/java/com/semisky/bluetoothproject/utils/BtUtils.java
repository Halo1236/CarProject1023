package com.semisky.bluetoothproject.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Looper;

/**
 * Created by chenhongrui on 2018/8/23
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtUtils {

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }


    /**
     * 判断当前activity是否是packageName
     *
     * @return true属于
     */
    public static boolean getTopActivityName(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
        String topPackageName = topActivity.getPackageName();
        return topPackageName.equals(packageName);
    }

}