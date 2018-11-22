package com.semisky.nforeservice.callback;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IInterface;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.util.Log;

public abstract class DoCallback<E extends IInterface>{	

    protected String TAG;
    
    protected RemoteCallbackList<E> mRemoteCallbacks;

    HandlerThread mHandlerThread;
    /**
     * Callback Queue Handler
     */
    protected Handler mHandler;
    
    /**
     * Set Log TAG.
     */
    protected abstract String getLogTag();
    
    public DoCallback(){
        TAG = "Ui" + getLogTag();
        this.mRemoteCallbacks = new RemoteCallbackList<E>();
        
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        
        mHandler = initCallbackHandler();
        
        Log.v(TAG, "" + TAG + "() init");
    }
    
    /**
     * Add callback into RemoteCallbackList
     * @param callback
     * @return success or not.
     */
    public boolean register(E cb)  {
        return mRemoteCallbacks.register(cb);
    }
    
    /**
     * Delete callback from RemoteCallbackList
     * @param callback
     * @return success or not.
     */
    public boolean unregister(E cb)  {
        return mRemoteCallbacks.unregister(cb);
    }
    
    public void kill() {
        mRemoteCallbacks.kill();
        mRemoteCallbacks = null;
    }
    
    /**
     * Add Message into callback message queue.
     */
    protected void enqueueMessage(Message msg) {
        mHandler.sendMessage(msg);
    }
    
    protected Message getMessage(int what) {
        return mHandler.obtainMessage(what);
    }
    
    /**
     * Callback Queue Handler
     */
    private Handler initCallbackHandler() {
        if (mHandler == null) {
            if (mHandlerThread == null) {
                Log.e(TAG,"mHandlerThread is null !!");
                return null;
            }
            return new Handler(mHandlerThread.getLooper()) {
                public void handleMessage(Message msg) {
                    // Log.v(TAG, "handleMessage : " + msg.what);
                    dequeueMessage(msg);            
                };
            };
        }
        else {
            return mHandler;
        }
    }
    
    
    /**
     * Get callback message out of queue and process callback.
     */
    protected abstract void dequeueMessage(Message msg);
    
    /**
     * Check callback is null or not to avoid when system kill callback
     * object may lead null pointer exception.
     */
    protected void checkCallbacksValid(int index) {
        if (mRemoteCallbacks == null) {
            return;
        }
        E cb = mRemoteCallbacks.getBroadcastItem(index);
        if (cb == null) {
            Log.e(TAG,"Callback " + cb + " is null !! unregister here.");
            mRemoteCallbacks.unregister(cb);
        }
    }
    
    protected boolean isCallbackValid() {
        if (mRemoteCallbacks == null) {
            Log.e(TAG, "Remote Callbacks is null !!");
            return false;
        }
        return true;
    }

}
