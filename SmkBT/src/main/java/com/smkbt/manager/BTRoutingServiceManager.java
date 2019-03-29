package com.smkbt.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.smkbt.aidl.IBTRoutingService;

public class BTRoutingServiceManager {
    private static final String TAG = "BTRoutingServiceManager";
    private static BTRoutingServiceManager _INSTANCE;
    private OnServiceStateListener mOnServiceStateListener;
    private IBTRoutingService mService;
    private Context mCtx;


    public interface OnServiceStateListener{
        void onChanagedState(boolean isConnected);
    }

    public static BTRoutingServiceManager getInstance(){
        if(null == _INSTANCE){
            _INSTANCE = new BTRoutingServiceManager();
        }
        return _INSTANCE;
    }

    private ServiceConnection mBtConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IBTRoutingService.Stub.asInterface(service);
            if(null != mOnServiceStateListener){
                mOnServiceStateListener.onChanagedState(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(null != mOnServiceStateListener){
                mOnServiceStateListener.onChanagedState(false);
            }
        }
    };

    public BTRoutingServiceManager attatchCtx(Context ctx){
        this.mCtx = ctx;
        return this;
    }

    public void registerOnServiceStateListener(OnServiceStateListener l){
        this.mOnServiceStateListener = l;
    }

    public boolean isConnected(){
        return (null != this.mService);
    }

    public void checkConnect(){
        if(null != mOnServiceStateListener){
            mOnServiceStateListener.onChanagedState(isConnected());
        }
    }

    public BTRoutingServiceManager startService(){
        if(isConnected()){
            Log.w(TAG,"Service is connected !!!");
            return this;
        }
        Intent it = new Intent();
        it.setAction("com.smk.ACTION_BT_START");
        mCtx.startService(it);
        return this;
    }

    public BTRoutingServiceManager bindService(){
        if(isConnected()){
            Log.w(TAG,"Service is connected !!!");
            return this;
        }
        Intent it = new Intent();
        it.setAction("com.smk.ACTION_BT_START");
        mCtx.bindService(it,mBtConn,Context.BIND_AUTO_CREATE);
        return this;
    }


    public void reqAvrcpBackward(){
        if(!isConnected()){
            return;
        }
        try {
            mService.reqAvrcpBackward();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void reqAvrcpForward(){
        if(!isConnected()){
            return;
        }
        try {
            mService.reqAvrcpForward();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }




}
