package com.semisky.parking.manager;

public class BackCarTrackManager {

    private static BackCarTrackManager _INSTANCE;
    private OnBackCarTrackListener mOnBackCarTrackListener;

    public static final int MAX_CAN_BACK_CAR_TRACE = 15600;// 最大倒车轨迹角度值
    public static final int STEP_BY_BACK_CAR_TRACE = 156;
    public static final int MAX_BACK_CAR_TRACE_MIDDLE = (MAX_CAN_BACK_CAR_TRACE/2)+STEP_BY_BACK_CAR_TRACE;

    public static final int TYPE_BACK_CAR_TRACE_LEFT = 1;
    public static final int TYPE_BACK_CAR_TRACE_MIDDLE = 2;
    public static final int TYPE_BACK_CAR_TRACE_RIGHT = 3;

    private BackCarTrackManager(){

    }

    public static BackCarTrackManager getInstance(){
        if(null == _INSTANCE){
            _INSTANCE = new BackCarTrackManager();
        }
        return _INSTANCE;
    }

    public interface OnBackCarTrackListener{
        void onBackCarTrackChanged(int backCarTrackType,String imgUrl);
    }

    public void registerOnBackCarTrackListener(OnBackCarTrackListener l){
        this.mOnBackCarTrackListener = l;
    }

    public void unregisterOnBackCarTrackListener(){
        this.mOnBackCarTrackListener = null;
    }

    private void notifyBackCarTrackChanged(int backCarTrackType,String imgUrl){
        if(null != mOnBackCarTrackListener){
            mOnBackCarTrackListener.onBackCarTrackChanged(backCarTrackType,imgUrl);
        }
    }


    public void handlerBackCarTraceData(int backCarTraceData){
    }

}
