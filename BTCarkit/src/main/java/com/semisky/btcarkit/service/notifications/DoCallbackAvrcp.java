package com.semisky.btcarkit.service.notifications;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;

import com.semisky.btcarkit.aidl.ISmkCallbackAvrcp;
import com.semisky.btcarkit.utils.Logutil;

import java.util.Arrays;

public class DoCallbackAvrcp extends DoBaseCallback<ISmkCallbackAvrcp> {
    private final int onAvrcpStateChanged = 0;
    private final int onAvrcpPlayStateChanged = 1;
    private final int onAvrcpMediaMetadataChanged = 2;


    public synchronized void onAvrcpStateChanged(String address, int oldState, int newState) {
        Message m = getMessage(onAvrcpStateChanged);
        m.arg1 = oldState;
        m.arg2 = newState;
        m.obj = address;
        onEnqueueMessage(m);
    }

    public void onAvrcpPlayStateChanged(int oldState, int newState) {
        Message m = getMessage(onAvrcpPlayStateChanged);
        m.arg1 = oldState;
        m.arg2 = newState;
        onEnqueueMessage(m);
    }

    public void onAvrcpMediaMetadataChanged(int[] metadataIds, String[] metadataValues) {
        Message m = getMessage(onAvrcpMediaMetadataChanged);
        Bundle b = new Bundle();
        b.putIntArray("metadataIds", metadataIds);
        b.putStringArray("metadataValues", metadataValues);
        m.setData(b);
        onEnqueueMessage(m);
    }

    @Override
    protected void onDequeueMessage(Message msg) {
        Bundle b = msg.getData();

        switch (msg.what) {
            case onAvrcpStateChanged:
                notifyAvrcpStateChanged((String) msg.obj, msg.arg1, msg.arg2);
                break;
            case onAvrcpPlayStateChanged:
                notifyAvrcpPlayStateChanged(msg.arg1, msg.arg2);
                break;
            case onAvrcpMediaMetadataChanged:
                notifyAvrcpMediaMetadataChanged(b.getIntArray("metadataIds"), b.getStringArray("metadataValues"));
                break;
        }
    }

    @Override
    protected String getLogTag() {
        return Logutil.makeTagLog(DoCallbackAvrcp.class);
    }

    private void notifyAvrcpStateChanged(String address, int oldState, int newState) {
        Logutil.i(TAG, "onAvrcpStateChanged() oldState=" + oldState + ",newState=" + newState);
        synchronized (mRemoteCallbackLists) {
            final int n = mRemoteCallbackLists.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onAvrcpStateChanged(address, oldState, newState);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }

    private void notifyAvrcpPlayStateChanged(int oldState, int newState) {
        Logutil.i(TAG, "onAvrcpPlayStateChanged() oldState=" + oldState + ",newState=" + newState);
        synchronized (mRemoteCallbackLists) {
            final int n = mRemoteCallbackLists.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onAvrcpPlayStateChanged(oldState, newState);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }

    private void notifyAvrcpMediaMetadataChanged(int[] metadataIds, String[] metadataValues) {
        Arrays.toString(metadataValues);
        Logutil.i(TAG, "notifyAvrcpMediaMetadataChanged() metadataIds : "
                + (null != metadataIds ? Arrays.toString(metadataIds) : "null") + "\nmetadataValues : "
                + (null != metadataValues ? Arrays.toString(metadataValues) : "null"));
        synchronized (mRemoteCallbackLists) {
            final int n = mRemoteCallbackLists.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onAvrcpMediaMetadataChanged(metadataIds, metadataValues);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }
}
