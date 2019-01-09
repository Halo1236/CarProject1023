package com.smk.autoradio.service.callback;

import android.os.Handler;
import android.os.IInterface;
import android.os.Message;
import android.os.RemoteCallbackList;

public abstract class DoBaseCallback<E extends IInterface> {
    protected String TAG;
    protected RemoteCallbackList<E> mRemoteCallbackList;
    protected Handler mHandler;

    protected abstract String getLogTag();

    public DoBaseCallback() {
        initCallbackHandler(); // 初始化Handler
    }

    // 初始化Handler
    private Handler initCallbackHandler() {
        if (null == mHandler) {
            return new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    enqueueMessage(msg);
                }
            };
        }
        return mHandler;
    }

    // 消息出队列
    protected abstract void dequeueMessage(Message msg);

    // 消息加入到队列
    protected void enqueueMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    // 获取一个Message
    protected Message getMessage(int what) {
        return mHandler.obtainMessage(what);
    }

    // 释放远程回调资源
    public void kill() {
        this.mRemoteCallbackList.kill();
        this.mRemoteCallbackList = null;
    }

    // 注册监听接口
    public boolean register(E cb){
        return mRemoteCallbackList.register(cb);
    }

    // 反注册监听接口
    public boolean unregister(E cb){
        return mRemoteCallbackList.unregister(cb);
    }

}
