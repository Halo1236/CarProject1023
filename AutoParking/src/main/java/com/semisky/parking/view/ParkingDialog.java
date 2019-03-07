package com.semisky.parking.view;

import android.app.Dialog;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.semisky.parking.R;
import com.semisky.parking.utils.Logger;

/**
 * Created by Administrator on 2018/8/11.
 */

public class ParkingDialog extends Dialog implements View.OnTouchListener {
    private static final String TAG = Logger.makeLogTag(ParkingDialog.class);
    private Context mContext;
    private View mContentView;
    View rl_touch_view;

    private final float X = 1;//1280.0f/960.0f;
    private final float Y = 1;//720.0f/566.0f;

    private OnTouchXYPositionListener mOnTouchXYPositionListener;
    private OnShowStateListener mOnShowStateListener;
    private int TOUCH_DOWN = 1;//触摸按下标记
    private int TOUCH_UP = 0;//触摸抬起标记
    private int X_POINTER_INDEX = 1;// X取坐标索引
    private int Y_POINTER_INDEX = 1;// Y取坐标索引


    public interface OnShowStateListener {
        void onChangeShowState(boolean isShowing);
    }

    public void setOnShowStateListener(OnShowStateListener l) {
        this.mOnShowStateListener = l;
    }

    public void unRegisterOnShowStateListener() {
        this.mOnShowStateListener = null;
    }

    private void notifyShowStateChange(boolean isShowing) {
        if (null != mOnShowStateListener) {
            this.mOnShowStateListener.onChangeShowState(isShowing);
        }
    }

    public interface OnTouchXYPositionListener {
        void onChangerXYPosition(int touchType, long xPos, long yPos);
    }


    public void setOnTouchXYPositionListener(OnTouchXYPositionListener l) {
        this.mOnTouchXYPositionListener = l;
    }

    public void unRgisterOnTouchXYPositionListener() {
        this.mOnTouchXYPositionListener = null;
    }

    private void notifyTouchXYPositionChange(int touchType, long xPos, long yPos) {
        if (null != mOnTouchXYPositionListener) {
            mOnTouchXYPositionListener.onChangerXYPosition(touchType, xPos, yPos);
        }
    }

    public ParkingDialog(Context ctx) {
        super(ctx, R.style.DialogStyle);
        this.mContext = ctx;
        init(ctx);
    }

    // 全屏显示Dialog
    private void init(Context context) {
        mContext = context;
        WindowManager.LayoutParams lParams = getWindow().getAttributes();
        lParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        lParams.gravity = Gravity.CENTER;
        lParams.dimAmount = 0f;
        getWindow().setAttributes(lParams);
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);// 全屏显示Dialog样式，不可以监听布局控件触摸事件
        //        this.setCanceledOnTouchOutside(true);// 点击dialog周边会取消弹窗

        LayoutInflater inflater = LayoutInflater.from(context);
        this.mContentView = inflater.inflate(R.layout.dialog_parking, null, false);
        setContentView(this.mContentView);// 设置View
        initViews();
    }

    private void initViews() {
        this.rl_touch_view = findViewById(R.id.rl_touch_view);
        this.rl_touch_view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        long xPos = 0;
        long yPos = 0;
        if (v.getId() == R.id.rl_touch_view) {
            int count = event.getPointerCount();
            if (count > 2) {
                return true;
            }
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    //_mCarCtrlManager.sendTouchXY(1, floatXToLong(event.getX()), floatYToLong(event.getY()));
                    xPos = floatXToLong(event.getX());
                    yPos = floatYToLong(event.getY());
                    notifyTouchXYPositionChange(TOUCH_DOWN, xPos, yPos);
                    Log.i(TAG, "ACTION_DOWN------------xPos:" + xPos + "------yPos:" + yPos);
                    break;
                case MotionEvent.ACTION_UP:
                    //_mCarCtrlManager.sendTouchXY(0, floatXToLong(event.getX()), floatYToLong(event.getY()));
                    xPos = floatXToLong(event.getX());
                    yPos = floatYToLong(event.getY());
                    notifyTouchXYPositionChange(TOUCH_UP, xPos, yPos);
                    Log.i(TAG, "ACTION_UP------------xPos:" + xPos + "------yPos:" + yPos);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    //_mCarCtrlManager.sendTouchXY(1, floatXToLong(event.getX(1)), floatYToLong(event.getY(1)));
                    xPos = floatXToLong(event.getX(X_POINTER_INDEX));
                    yPos = floatYToLong(event.getY(Y_POINTER_INDEX));
                    notifyTouchXYPositionChange(TOUCH_DOWN, xPos, yPos);
                    Log.i(TAG, "ACTION_POINTER_DOWN------------xPos:" + xPos + "------yPos:" + yPos);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    //_mCarCtrlManager.sendTouchXY(0, floatXToLong(event.getX(1)), floatYToLong(event.getY(1)));
                    xPos = floatXToLong(event.getX(X_POINTER_INDEX));
                    yPos = floatYToLong(event.getY(Y_POINTER_INDEX));
                    notifyTouchXYPositionChange(TOUCH_UP, xPos, yPos);
                    Log.i(TAG, "ACTION_POINTER_UP------------xPos:" + xPos + "------yPos:" + yPos);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private long floatXToLong(float f) {
        String temp = String.valueOf(f * X);
        return Long.valueOf(temp.split("\\.")[0]);
    }

    private long floatYToLong(float f) {
        String temp = String.valueOf(f * Y);
        return Long.valueOf(temp.split("\\.")[0]);
    }

    @Override
    public void show() {
        super.show();
        notifyShowStateChange(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        notifyShowStateChange(false);
    }


}
