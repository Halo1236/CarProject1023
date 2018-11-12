package com.semisky.btcarkit.service.notifications;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;

import com.semisky.btcarkit.aidl.BTHfpClientCall;
import com.semisky.btcarkit.aidl.ISmkCallbackHfp;
import com.semisky.btcarkit.utils.Logutil;

public class DoCallbackHfp extends DoBaseCallback<ISmkCallbackHfp> {
    private final int onHfpStateChanged = 0;
    private final int onHfpCallStateChanged = 1;


    public void onHfpStateChanged(String address, int oldState, int newState) {
        Message m = getMessage(onHfpStateChanged);
        m.obj = address;
        m.arg1 = oldState;
        m.arg2 = newState;
        onEnqueueMessage(m);
    }

    public void onHfpCallStateChanged(String address, BTHfpClientCall call) {
        Message m = getMessage(onHfpCallStateChanged);
        m.obj = address;
        Bundle b = new Bundle();
        b.putParcelable("call", call);
        m.setData(b);
        onEnqueueMessage(m);
    }

    @Override
    protected void onDequeueMessage(Message msg) {
        Bundle b = msg.getData();
        switch (msg.what) {
            case onHfpStateChanged:
                notifyHfpStateChanged((String) msg.obj, msg.arg1, msg.arg2);
                break;
            case onHfpCallStateChanged:
                notifyHfpCallStateChanged((String) msg.obj, (BTHfpClientCall) b.getParcelable("call"));
                break;
        }
    }

    @Override
    protected String getLogTag() {
        return Logutil.makeTagLog(DoCallbackHfp.class);
    }

    private void notifyHfpStateChanged(String address, int oldState, int newState) {
        Logutil.i(TAG, "notifyHfpStateChanged() oldState=" + oldState + " , newState=" + newState);
        synchronized (mRemoteCallbackLists) {
            final int N = mRemoteCallbackLists.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onHfpStateChanged(address, oldState, newState);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }

    private void notifyHfpCallStateChanged(String address, BTHfpClientCall call) {
        Logutil.i(TAG, "notifyHfpCallStateChanged() ..." + (null != call ? call.toString() : "null"));
        synchronized (mRemoteCallbackLists) {
            final int N = mRemoteCallbackLists.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onHfpCallStateChanged(address, call);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }
}
