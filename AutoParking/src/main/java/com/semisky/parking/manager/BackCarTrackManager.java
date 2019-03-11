package com.semisky.parking.manager;

public class BackCarTrackManager {

    private static BackCarTrackManager _INSTANCE;
    private OnBackCarTrackListener mOnBackCarTrackListener;

    public static final int TRACK_ANGEL_MAX = 15600;// 最大倒车轨迹角度值
    public static final int TRACK_ANGLE_STEP = 156;// 轨迹角度最小单元
    public static final int MIDDLE_TRACK_ANGLE_MIN = (TRACK_ANGEL_MAX / 2) - TRACK_ANGLE_STEP;// 轨迹中间角度值最小值
    public static final int MIDDLE_TRACK_ANGLE_MAX = (TRACK_ANGEL_MAX / 2) + TRACK_ANGLE_STEP;// 轨迹中间角度值最大值

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


    public void handlerBackCarTraceData(int trackAngle) {
        if (trackAngle < 0) {
            trackAngle = 0;
        }
        if (trackAngle > TRACK_ANGEL_MAX) {
            trackAngle = TRACK_ANGEL_MAX;
        }

        if (trackAngle >= MIDDLE_TRACK_ANGLE_MIN || trackAngle <= MIDDLE_TRACK_ANGLE_MAX) {
            // 中间倒车轨迹角度
            notifyBackCarTrackChanged(TYPE_TRACE_MIDDLE, 1);
        } else if (trackAngle < MIDDLE_TRACK_ANGLE_MIN) {
            // 左边倒车轨迹角度
            notifyBackCarTrackChanged(TYPE_TRACE_LEFT, trackAngle / TRACK_ANGLE_STEP);
        } else if (trackAngle > MIDDLE_TRACK_ANGLE_MAX) {
            // 右边倒车轨迹角度
            notifyBackCarTrackChanged(TYPE_TRACE_RIGHT, (trackAngle - TRACK_ANGEL_MAX / 2) / 156);
        }
    }

}
