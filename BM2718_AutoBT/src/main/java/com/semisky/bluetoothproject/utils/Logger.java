package com.semisky.bluetoothproject.utils;

import android.util.Log;

/**
 * Created by CHR on 2017/4/10.
 */

public class Logger {

    private static final String PREFIX_TAG = "CHR_";
    private static boolean DEBUG = true;

    public static String makeTagLog(Class clz) {
        return PREFIX_TAG + clz.getSimpleName();
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }
}
