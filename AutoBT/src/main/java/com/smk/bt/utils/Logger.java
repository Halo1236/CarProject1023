package com.smk.bt.utils;

import android.util.Log;

/**
 *
 */
public class Logger {
    private static final String PREFIX_LOG = "Jonhliu_";
    private static final boolean V = true;
    private static final boolean I = true;
    private static final boolean D = true;
    private static final boolean W = true;
    private static final boolean E = true;

    public static String makeLogTag(Class clz) {
        return PREFIX_LOG + clz.getSimpleName();
    }

    public static void v(String tag, String msg) {
        if (V) Log.v(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (I) Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (D) Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (W) Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (E) Log.e(tag, msg);
    }
}
