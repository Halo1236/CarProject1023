package com.semisky.btcarkit.service.notifications;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IInterface;
import android.os.Message;
import android.os.RemoteCallbackList;

import com.semisky.btcarkit.utils.Logutil;

public abstract class DoBaseCallback<E extends IInterface> {
    protected String TAG;
    protected RemoteCallbackList<E> mRemoteCallbackLists;
    protected HandlerThread mHandlerThread;
    protected Handler mHandler;

    public DoBaseCallback(){
        TAG = getLogTag();
        this.mRemoteCallbackLists = new RemoteCallbackList<E>();
        this.mHandlerThread = new HandlerThread(getLogTag());
        this.mHandlerThread.start();
        this.mHandler = makeCallbackHandler();
    }

    private Handler makeCallbackHandler(){
        if(null == mHandler){
            if(null == mHandlerThread){
                Logutil.e(TAG,"mHandlerThread == NULL !!!");
            }
            return new Handler(mHandlerThread.getLooper()){
                @Override
                public void handleMessage(Message msg) {
                    onDequeueMessage(msg);
                }
            };
        }
        else {
            return mHandler;
        }
    }

    protected void checkCallbackValid(int index){
        if(null == mRemoteCallbackLists){
            return;
        }
        E callback = mRemoteCallbackLists.getBroadcastItem(index);
        if(null == callback){
            Logutil.e(TAG,"callback"+callback+" is null !!!");
            this.mRemoteCallbackLists.unregister(callback);
        }
    }
    public void kill(){
        this.mRemoteCallbackLists.kill();
        mRemoteCallbackLists = null;
    }
    public boolean register(E callback){
        return mRemoteCallbackLists.register(callback);
    }
    public boolean unregister(E callback){
        return this.mRemoteCallbackLists.unregister(callback);
    }
    protected Message getMessage(int what){
        return this.mHandler.obtainMessage(what);
    }
    protected void onEnqueueMessage(Message msg){
        this.mHandler.sendMessage(msg);
    }

    /**
     * Get callback message out of queue and process callbacks
     * @param msg
     */
    protected abstract void onDequeueMessage(Message msg);
    /**
     * Set Log TAG
     */
    protected abstract String getLogTag();




}
