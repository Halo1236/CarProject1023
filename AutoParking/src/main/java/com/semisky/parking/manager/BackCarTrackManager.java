package com.semisky.parking.manager;

import android.os.Handler;

import com.semisky.parking.utils.Logger;


public class BackCarTrackManager {

    private static final String TAG = Logger.makeLogTag(BackCarTrackManager.class);
    private static BackCarTrackManager _INSTANCE;
    private OnBackCarTrackListener mOnBackCarTrackListener;
    private Handler _handler;

    public static final int TRACK_ANGEL_MAX = 15600;// 最大倒车轨迹角度值
    public static final int TRACK_ANGLE_STEP = 156;// 轨迹角度最小单元
    public static final int MIDDLE_TRACK_ANGLE_MIN = (TRACK_ANGEL_MAX / 2) - TRACK_ANGLE_STEP;// 轨迹中间角度值最小值
    public static final int MIDDLE_TRACK_ANGLE_MAX = (TRACK_ANGEL_MAX / 2) + TRACK_ANGLE_STEP;// 轨迹中间角度值最大值

    private static final int PHOTO_LEFT_TRACK_COUNT = 50;// 左侧轨迹图片总数
    private static final int PHOTO_RIGHT_TRACK_COUNT = 50;// 右侧轨迹图片总数

    public static final int TYPE_TRACE_LEFT = 1;
    public static final int TYPE_TRACE_MIDDLE = 2;
    public static final int TYPE_TRACE_RIGHT = 3;

    private BackCarTrackManager() {

    }

    public static BackCarTrackManager getInstance() {
        if (null == _INSTANCE) {
            _INSTANCE = new BackCarTrackManager();
        }
        return _INSTANCE;
    }

    public interface OnBackCarTrackListener {
        void onBackCarTrackChanged(int backCarTrackType, int index);
    }

    public void registerOnBackCarTrackListener(OnBackCarTrackListener l) {
        this.mOnBackCarTrackListener = l;
    }

    public void unregisterOnBackCarTrackListener() {
        this.mOnBackCarTrackListener = null;
    }

    private void notifyBackCarTrackChanged(int backCarTrackType, int index) {
        if (null != mOnBackCarTrackListener) {
            mOnBackCarTrackListener.onBackCarTrackChanged(backCarTrackType, index);
        }
    }


    public void handlerBackCarTrackData(int trackAngle) {
        if (null == _handler) {
            return;
        }
        _handler.removeCallbacks(mStartFromLeftToRightRunnabel);
        _handler.removeCallbacks(mStartFromRightToLeftRunnabel);
        executeHandlerBackCarTrackData(trackAngle);
    }

    void executeHandlerBackCarTrackData(int trackAngle) {
        Logger.i(TAG, "handlerBackCarTrackData() trackAngle : " + trackAngle);
        if (trackAngle < TRACK_ANGLE_STEP) {
            trackAngle = TRACK_ANGLE_STEP;
        }
        if (trackAngle > TRACK_ANGEL_MAX) {
            trackAngle = TRACK_ANGEL_MAX;
        }

        if (trackAngle >= MIDDLE_TRACK_ANGLE_MIN && trackAngle <= MIDDLE_TRACK_ANGLE_MAX) {
            // 中间倒车轨迹角度
            notifyBackCarTrackChanged(TYPE_TRACE_MIDDLE, 1);
        } else if (trackAngle < MIDDLE_TRACK_ANGLE_MIN) {
            // 左边倒车轨迹角度
            notifyBackCarTrackChanged(TYPE_TRACE_LEFT, PHOTO_LEFT_TRACK_COUNT - trackAngle / TRACK_ANGLE_STEP);
        } else if (trackAngle > MIDDLE_TRACK_ANGLE_MAX) {
            // 右边倒车轨迹角度
            notifyBackCarTrackChanged(TYPE_TRACE_RIGHT, (trackAngle - TRACK_ANGEL_MAX / 2) / TRACK_ANGLE_STEP);
        }
    }

    public void registerHandler(Handler handler) {
        this._handler = handler;
    }

    public void testFromLeftToRight() {
        if (_handler == null) {
            return;
        }

        mTempTrackAngle = 0;
        _handler.removeCallbacks(mStartFromLeftToRightRunnabel);
        _handler.postDelayed(mStartFromLeftToRightRunnabel, 1000);

    }

    private int mTempTrackAngle = 0;

    private Runnable mStartFromLeftToRightRunnabel = new Runnable() {
        @Override
        public void run() {
            if (mTempTrackAngle > TRACK_ANGEL_MAX) {
                mTempTrackAngle = 0;
                return;
            }
            executeHandlerBackCarTrackData(mTempTrackAngle);
            _handler.postDelayed(this, 350);
            mTempTrackAngle += TRACK_ANGLE_STEP;
        }
    };

    public void testFromRightToLeft() {
        if (_handler == null) {
            return;
        }

        mTempTrackAngle = TRACK_ANGEL_MAX;
        _handler.removeCallbacks(mStartFromRightToLeftRunnabel);
        _handler.postDelayed(mStartFromRightToLeftRunnabel, 1000);

    }

    private Runnable mStartFromRightToLeftRunnabel = new Runnable() {
        @Override
        public void run() {
            if (mTempTrackAngle < 0) {
                mTempTrackAngle = TRACK_ANGEL_MAX;
                return;
            }
            executeHandlerBackCarTrackData(mTempTrackAngle);
            _handler.postDelayed(this, 350);
            mTempTrackAngle -= TRACK_ANGLE_STEP;
        }
    };


}
