package com.semisky.btcarkit.service.notifications;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;

import com.semisky.btcarkit.aidl.ISmkCallbackA2dp;
import com.semisky.btcarkit.utils.Logutil;

public class DoCallbackA2dp extends DoBaseCallback<ISmkCallbackA2dp> {

    private final int onA2dpStateChanged = 0;

    @Override
    protected void onDequeueMessage(Message msg) {
        Bundle b = msg.getData();
        String address = (String) msg.obj;
        int oldState = msg.arg1;
        int newState = msg.arg2;

        switch (msg.what) {
            case onA2dpStateChanged:
                notifyA2dpStateChanged(oldState, newState);
                break;
        }
    }

    @Override
    protected String getLogTag() {
        return TAG = Logutil.makeTagLog(DoCallbackA2dp.class);
    }

    public synchronized void onA2dpStateChanged(int oldState, int newState) {
        Message m = getMessage(onA2dpStateChanged);
        m.arg1 = oldState;
        m.arg2 = newState;
        onEnqueueMessage(m);
    }

    private void notifyA2dpStateChanged(int oldState, int newState) {
        Logutil.i(TAG, "notifyA2dpStateChanged() oldState=" + oldState + ",newState=" + newState);
        synchronized (mRemoteCallbackLists) {
            final int n = mRemoteCallbackLists.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onA2dpStateChanged(oldState, newState);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }
}
