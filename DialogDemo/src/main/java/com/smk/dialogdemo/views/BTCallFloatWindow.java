package com.smk.dialogdemo.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smk.dialogdemo.R;

/**
 * 电话浮窗
 */
public class BTCallFloatWindow {
    private static final String TAG = BTCallFloatWindow.class.getCanonicalName();

    public static final int FLOAT_WINDOW_MODE_SMALL = 0;// 浮窗小窗模式
    public static final int FLOAT_WINDOW_MODE_FULL = 1;// 浮窗全屏模式

    public static final int FLOAT_WINDOW_TYPE_INVALID = -1;// 无效浮窗类型
    public static final int FLOAT_WINDOW_TYPE_ACTIVE = 100;// 通话浮窗类型
    public static final int FLOAT_WINDOW_TYPE_DIALING = 101;// 拨号浮窗类型
    public static final int FLAGT_WINDOW_TYPE_INCOMING = 102;// 来电浮窗类型

    private View mBTCallContentView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private boolean mIsShowing = false;

    private FullFloatWindowViewHolder mFullFloatWindowViewHolder;
    private SmallFloatWindowViewHolder mSmallFloatWindowViewHolder;

    private int mCurrentFloatWindowMode = FLOAT_WINDOW_MODE_FULL;// 当前浮窗模式
    private int mCurrentFloatWindowType = FLOAT_WINDOW_TYPE_INVALID;

    // Full screen float window ui
    public class FullFloatWindowViewHolder {
        // call layout
        private View include_fullscreen_root_layout;
        // dialpad layout
        private View include_fullscreen_dialpad_root_layout;
        private RelativeLayout fullscreen_float_window_call_details_layout;
        private TextView
                tv_phone_name,
                tv_phone_number,
                tv_phone_status;
        private Chronometer tv_phone_timer;

        FullFloatWindowViewHolder(View v) {
            // dialpad ui
            include_fullscreen_dialpad_root_layout = v.findViewById(R.id.include_fullscreen_dialpad_root_layout);

            // call ui
            include_fullscreen_root_layout = v.findViewById(R.id.include_fullscreen_root_layout);
            fullscreen_float_window_call_details_layout = v.findViewById(R.id.fullscreen_float_window_call_details_layout);
            tv_phone_name = (TextView) v.findViewById(R.id.tv_phone_name);
            tv_phone_number = (TextView) v.findViewById(R.id.tv_phone_number);
            tv_phone_status = (TextView) v.findViewById(R.id.tv_phone_status);
            tv_phone_timer = (Chronometer) v.findViewById(R.id.tv_phone_timer);
        }
    }

    // Small screen float window ui
    public class SmallFloatWindowViewHolder {
        private View include_smallscreen_root_layout;

        SmallFloatWindowViewHolder(View v) {
            include_smallscreen_root_layout = v.findViewById(R.id.include_smallscreen_root_layout);
        }
    }

    public BTCallFloatWindow(Context ctx) {
        this.mBTCallContentView = View.inflate(ctx, R.layout.float_window_bt_call_main, null);
        this.mFullFloatWindowViewHolder = new FullFloatWindowViewHolder(mBTCallContentView);
        this.mSmallFloatWindowViewHolder = new SmallFloatWindowViewHolder(mBTCallContentView);

        this.mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        this.mParams = new WindowManager.LayoutParams();
        this.mParams.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        this.mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        this.mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        this.mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        this.mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.mParams.gravity = Gravity.START | Gravity.TOP;
        this.mParams.format = PixelFormat.RGBA_8888;
        this.mParams.y = 0;
        this.mParams.x = 0;
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

    // 是否浮窗显示
    public boolean isShowing() {
        return mIsShowing;
    }

    // utils
    public void refresh() {
        // 根据浮窗模式改变布局
        changeLayoutWithFloatWindowMode();
        changeUiTimerVisibleWithFloatWindowType();
    }

    // 根据浮窗类型改变计时控件显示状态
    void changeUiTimerVisibleWithFloatWindowType() {
        Log.i(TAG, "refresh() mCurrentFloatWindowType : " + mCurrentFloatWindowType);
        mFullFloatWindowViewHolder.tv_phone_timer.setVisibility(mCurrentFloatWindowType == FLOAT_WINDOW_TYPE_ACTIVE?View.INVISIBLE:View.VISIBLE);
    }

    // 根据浮窗模式改变布局
    void changeLayoutWithFloatWindowMode() {
        Log.i(TAG, "refresh() mCurrentFloatWindowMode : " + mCurrentFloatWindowMode);
        switch (mCurrentFloatWindowMode) {
            case FLOAT_WINDOW_MODE_SMALL:
                mFullFloatWindowViewHolder.include_fullscreen_root_layout.setVisibility(View.INVISIBLE);// 全屏浮窗布局隐藏
                mFullFloatWindowViewHolder.include_fullscreen_dialpad_root_layout.setVisibility(View.INVISIBLE);// 全屏键盘浮窗布局隐藏
                mSmallFloatWindowViewHolder.include_smallscreen_root_layout.setVisibility(View.VISIBLE);// 小窗浮窗布局显示
                break;
            case FLOAT_WINDOW_MODE_FULL:
                mSmallFloatWindowViewHolder.include_smallscreen_root_layout.setVisibility(View.INVISIBLE);// 小窗浮窗布局隐藏
                mFullFloatWindowViewHolder.include_fullscreen_root_layout.setVisibility(View.VISIBLE);// 全屏浮窗布局显示
                mFullFloatWindowViewHolder.include_fullscreen_dialpad_root_layout.setVisibility(View.VISIBLE);// 全屏键盘浮窗布局显示
                break;
        }
    }

    // 设置当前浮窗类型
    public void setmCurrentFloatWindowType(int mCurrentFloatWindowType) {
        this.mCurrentFloatWindowType = mCurrentFloatWindowType;
    }

    // 获取当前浮窗类型
    public int getmCurrentFloatWindowType() {
        return mCurrentFloatWindowType;
    }

    // 设置当前浮窗模式
    public void setmCurrentFloatWindowMode(int mCurrentFloatWindowMode) {
        this.mCurrentFloatWindowMode = mCurrentFloatWindowMode;
    }

    // 获取当前浮窗模式
    public int getmCurrentFloatWindowMode() {
        return mCurrentFloatWindowMode;
    }

    public BTCallFloatWindow setCallName(String callName) {
        mFullFloatWindowViewHolder.tv_phone_name.setText(callName);
        return this;
    }

    public BTCallFloatWindow setCallNumber(String callNumber) {
        mFullFloatWindowViewHolder.tv_phone_number.setText(callNumber);
        return this;
    }

    public BTCallFloatWindow setCallStatus(String callStatus) {
        mFullFloatWindowViewHolder.tv_phone_status.setText(callStatus);
        return this;
    }

    public BTCallFloatWindow setCallTime(String callTime) {
//        mFullFloatWindowViewHolder.tv_phone_timer.setBase();
        return this;
    }


}
