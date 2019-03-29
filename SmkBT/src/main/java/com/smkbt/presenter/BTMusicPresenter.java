package com.smkbt.presenter;

import com.smkbt.manager.BTRoutingServiceManager;
import com.smkbt.view.IBTMusicView;

public class BTMusicPresenter<V extends IBTMusicView> extends BasePresenter<V> {


    public BTMusicPresenter(){
        BTRoutingServiceManager.getInstance().registerOnServiceStateListener(mOnServiceStateListener);
    }

    public void checkConnect(){
        BTRoutingServiceManager.getInstance().checkConnect();
    }


    BTRoutingServiceManager.OnServiceStateListener mOnServiceStateListener = new BTRoutingServiceManager.OnServiceStateListener(){
        @Override
        public void onChanagedState(boolean isConnected) {
            if(isConnected){
                // TO DO
                // 检查连接A2DP连接状态
                // 如何A2DP连接成功，请求播放要求(相关判断由统一由蓝牙服务处理，请求的结果由回调接口反馈)
            }
        }
    };


    // 上一曲
    public void reqAvrcpBackward(){
        if(isBindView()){
            BTRoutingServiceManager.getInstance().reqAvrcpBackward();
        }
    }

    // 下一曲
    public void reqAvrcpForward(){
        if(isBindView()){
            BTRoutingServiceManager.getInstance().reqAvrcpBackward();
        }
    }


    @Override
    public void detachView() {
        // TO DO 释放相关资源，如注册监听接口、缓存的数据等
        super.detachView();
    }
}
