package com.semisky.parking.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.semisky.autoservice.manager.CarCtrlManager;
import com.semisky.parking.manager.BackCarTrackManager;
import com.semisky.parking.manager.ParkingManager.Definition;
import com.semisky.parking.model.AudioFocusControlModel;
import com.semisky.parking.utils.Logger;
import com.semisky.parking.view.ParkingDialog;

/**
 * 360全景泊车服务
 * Created by Administrator on 2018/8/11.
 */

public class ParkingService extends Service {
    private static final String TAG = Logger.makeLogTag(ParkingService.class);
    private int mCurrentParkingCmd = Definition.CMD_INVALID;
    private ParkingDialog mParkingDialog = null;
    private AudioFocusControlModel mAudioFocusControlModel;
    private Handler _handler = new Handler(Looper.getMainLooper());


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i(TAG, "onCreate() ...");
        this.mAudioFocusControlModel = AudioFocusControlModel.getInstance(this);
        BackCarTrackManager.getInstance().registerOnBackCarTrackListener(mOnBackCarTrackListener);
        BackCarTrackManager.getInstance().registerHandler(_handler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i(TAG, "onStartCommand() ...");
        handlerIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy() ...");
    }

    /**
     * 处理服务意图
     *
     * @param intent
     */
    private void handlerIntent(Intent intent) {
        if (null == intent) {
            return;
        }
        String action = intent.getAction();
        int cmd = intent.getIntExtra(Definition.CMD_PARAM, Definition.CMD_INVALID);
        int angle = intent.getIntExtra("angle", Definition.CMD_INVALID);
        Logger.i(TAG, "handlerIntent() action = " + action + ",cmd = " + cmd + ", angle = " + angle);

        if (Definition.ACTION_SERVICE_PARKING.equals(action)) {

            this.mCurrentParkingCmd = cmd;
            switch (cmd) {
                case Definition.CMD_AVM_OFF:// 退出AVM
//                    Logger.i(TAG, "CMD_AVM_OFF ...");
//                    dismissParkingDialog();
                    _handler.removeCallbacksAndMessages(null);
                    _handler.postDelayed(mTimeoutRunTaskRunnable, 200);
                    break;
                case Definition.CMD_AVM_ON:// 进入AVM
                    Logger.i(TAG, "CMD_AVM_ON ...");
                    showParkingDialog();
                    break;
                case Definition.CMD_DVR_OFF:
                    Logger.i(TAG, "CMD_DVR_OFF ...");
                    dismissParkingDialog();
                    break;
                case Definition.CMD_DVR_ON:
                    Logger.i(TAG, "CMD_DVR_ON ...");
                    showParkingDialog();
                    break;
                case Definition.CMD_BACK_CAR_LINE_UPDATE:
                    Logger.i(TAG, "CMD_BACK_CAR_LINE_UPDATE ...");
                    BackCarTrackManager.getInstance().handlerBackCarTrackData(angle);
                    break;
                case Definition.CMD_AUTO_LEFT_TO_RIGHT:
                    BackCarTrackManager.getInstance().testFromLeftToRight();
                    break;
                case Definition.CMD_AUTO_RIGHT_TO_LEFT:
                    BackCarTrackManager.getInstance().testFromRightToLeft();
                    break;
            }
        }
    }

    private Runnable mTimeoutRunTaskRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.i(TAG, "CMD_AVM_OFF ...");
            dismissParkingDialog();
        }
    };

    /**
     * 显示全景泊车弹窗
     */
    private void showParkingDialog() {
        if (null == mParkingDialog) {
            mParkingDialog = new ParkingDialog(this);
            mParkingDialog.setOnTouchXYPositionListener(mOnTouchXYPositionListener);
            mParkingDialog.setOnShowStateListener(mOnShowStateListener);
        }
        if (!mParkingDialog.isShowing()) {
            mParkingDialog.show();
        }
    }

    /**
     * 关闭全景泊车弹窗
     */
    private void dismissParkingDialog() {
        if (null != mParkingDialog && mParkingDialog.isShowing()) {
            mParkingDialog.dismiss();
        }
    }

    private ParkingDialog.OnTouchXYPositionListener mOnTouchXYPositionListener = new ParkingDialog.OnTouchXYPositionListener() {
        @Override
        public void onChangerXYPosition(int touchType, long xPos, long yPos) {
            Logger.i(TAG, "==================");
            Logger.i(TAG, "onChangerXYPosition() touchType=" + touchType);
            Logger.i(TAG, "onChangerXYPosition() xPos=" + xPos);
            Logger.i(TAG, "onChangerXYPosition() yPos=" + yPos);
            Logger.i(TAG, "==================");
            CarCtrlManager.getInstance().sendTouchXY(touchType, xPos, yPos);
        }
    };

    private ParkingDialog.OnShowStateListener mOnShowStateListener = new ParkingDialog.OnShowStateListener() {
        @Override
        public void onChangeShowState(boolean isShowing) {
            Logger.i(TAG, "==================");
            Logger.i(TAG, "onChangeShowState() isShowing=" + isShowing);
            Logger.i(TAG, "==================");
            // 弹窗显示
            if (isShowing) {
                mAudioFocusControlModel.onRequestAudioFocus();
                CarCtrlManager.getInstance().setAVMStatus(true);
            }
            // 弹窗消失
            else {
                mAudioFocusControlModel.onAbandonAudioFocus();
                CarCtrlManager.getInstance().setAVMStatus(false);
            }
        }
    };

    private BackCarTrackManager.OnBackCarTrackListener mOnBackCarTrackListener = new BackCarTrackManager.OnBackCarTrackListener() {
        @Override
        public void onBackCarTrackChanged(int backCarTrackType, int index) {
            Toast.makeText(ParkingService.this, "backCarTrackType=" + backCarTrackType + ", index=" + index, Toast.LENGTH_SHORT).show();
            if (null == mParkingDialog) {
                return;
            }
            switch (backCarTrackType) {
                case BackCarTrackManager.TYPE_TRACE_LEFT:
                    mParkingDialog.updateTranck("/storage/udisk0/udisk00/BackCarTraceResource/trace_left/" + (50 - index) + ".bmp");
                    break;
                case BackCarTrackManager.TYPE_TRACE_MIDDLE:
                    mParkingDialog.updateTranck("/storage/udisk0/udisk00/BackCarTraceResource/trace_middle/" + index + ".bmp");
                    break;
                case BackCarTrackManager.TYPE_TRACE_RIGHT:
                    mParkingDialog.updateTranck("/storage/udisk0/udisk00/BackCarTraceResource/trace_right/" + index + ".bmp");
                    break;
            }
        }
    };


}
