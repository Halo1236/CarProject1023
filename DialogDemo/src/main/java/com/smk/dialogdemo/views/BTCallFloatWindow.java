package com.smk.dialogdemo.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.smk.dialogdemo.R;

public class BTCallFloatWindow {
    private static final String TAG = BTCallFloatWindow.class.getCanonicalName();
    private View mBTCallContentView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private boolean mIsShowing = false;
    private boolean mIsFullScreenFloatWindow = true;

    // Full screen float window ui
    private View include_fullscreen_root_layout;
    private RelativeLayout fullscreen_float_window_call_details_layout;
    // Full screen float window dialpad ui
    private View include_fullscreen_dialpad_root_layout;
    // Small screen float window ui
    private View include_smallscreen_root_layout;

    public BTCallFloatWindow(Context ctx) {
        this.mBTCallContentView = View.inflate(ctx, R.layout.float_window_bt_call_main, null);
        this.mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        this.mParams = new WindowManager.LayoutParams();
        this.mParams.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        this.mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        this.mParams.format = PixelFormat.RGBA_8888;
        this.mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        this.mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        this.mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.mParams.gravity = Gravity.START | Gravity.TOP;
        this.mParams.y = 0;
        this.mParams.x = 0;
    }

    private void init() {
        // Full screen float window ui
        include_fullscreen_root_layout = mBTCallContentView.findViewById(R.id.include_fullscreen_root_layout);
        fullscreen_float_window_call_details_layout = mBTCallContentView.findViewById(R.id.fullscreen_float_window_call_details_layout);
        // Full screen float window dialpad ui
        include_fullscreen_dialpad_root_layout = mBTCallContentView.findViewById(R.id.include_fullscreen_dialpad_root_layout);
        // Small screen float window ui
        include_smallscreen_root_layout = mBTCallContentView.findViewById(R.id.include_smallscreen_root_layout);

    }

    private boolean isInit() {
        return (null != mWindowManager && null != mParams && null != mBTCallContentView);
    }


    public synchronized void show() {
        if (!mIsShowing && isInit()) {
            mWindowManager.addView(mBTCallContentView, mParams);
            mIsShowing = true;
            Log.i(TAG, "show() ...");
        }
    }

    public synchronized void dismiss() {
        if (mIsShowing && isInit()) {
            mWindowManager.removeViewImmediate(mBTCallContentView);
            mIsShowing = false;
            Log.i(TAG, "dismiss() ...");
        }
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    /**
     * 配置去电浮窗（全屏浮窗）
     *
     * @return
     */
    public BTCallFloatWindow makeOutgoingByFullScreenFloatWindow() {

        return this;
    }

    /**
     * 配置来电浮窗（全屏浮窗）
     *
     * @return
     */
    public BTCallFloatWindow makeIncomingByFullScreenFloatWindow() {
        return this;
    }

    /**
     * 配置接通电话浮窗（全屏浮窗）
     *
     * @return
     */
    public BTCallFloatWindow makeActiveByFullScreenFloatWindow() {
        return this;
    }

    public BTCallFloatWindow enableFullScreenFloatWindow(boolean enable) {
        this.mIsFullScreenFloatWindow = enable;
        return this;
    }

    public BTCallFloatWindow setCallName(String callName){
        return this;
    }

    public BTCallFloatWindow setCallNumber(String callNumber){

        return this;
    }

    public BTCallFloatWindow setCallStatus(String callStatus){
        return this;
    }

    public BTCallFloatWindow setCallTime(String callTime){
        return this;
    }


}
