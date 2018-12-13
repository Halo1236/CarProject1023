package com.smk.dialogdemo.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class BTCallWindowManager {
    private static final String TAG = BTCallWindowManager.class.getCanonicalName();
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private BTCallFullScreenFloatLayout mBTCallFullScreenFloatLayout;
    private boolean mIsShowing = false;
    private static BTCallWindowManager _INSTANCE;

    private BTCallWindowManager() {
    }

    public static BTCallWindowManager getInstance() {
        if (null == _INSTANCE) {
            _INSTANCE = new BTCallWindowManager();
        }
        return _INSTANCE;
    }


    public BTCallWindowManager createBTCallFullScreenFloatLayout(Context ctx) {
        if (isInit()) {
            return this;
        }
        mBTCallFullScreenFloatLayout = new BTCallFullScreenFloatLayout(ctx);
        this.mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        this.mParams = new WindowManager.LayoutParams();
        this.mParams.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        this.mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        this.mParams.format = PixelFormat.RGBA_8888;
        this.mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        this.mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        this.mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.mParams.gravity = Gravity.START | Gravity.TOP;
        this.mParams.y = 0;
        this.mParams.x = 0;
        return this;
    }

    private boolean isInit() {
        return (null != mWindowManager && null != mParams && null != mBTCallFullScreenFloatLayout);
    }


    public synchronized void show() {
        if (!mIsShowing && isInit()) {
            mWindowManager.addView(mBTCallFullScreenFloatLayout, mParams);
            mIsShowing = true;
            Log.i(TAG, "show() ...");
        }
    }

    public synchronized void dismiss() {
        if (mIsShowing && isInit()) {
            mWindowManager.removeViewImmediate(mBTCallFullScreenFloatLayout);
            mIsShowing = false;
            Log.i(TAG, "dismiss() ...");
        }
    }

    public boolean isShowing() {
        return mIsShowing;
    }
}
