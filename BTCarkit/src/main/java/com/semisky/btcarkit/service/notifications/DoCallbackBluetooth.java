package com.semisky.btcarkit.service.notifications;

import android.os.Bundle;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.RemoteException;

import com.semisky.btcarkit.aidl.BTDeviceInfo;
import com.semisky.btcarkit.aidl.ISmkCallbackBluetooth;
import com.semisky.btcarkit.utils.Logutil;

import java.util.List;

public class DoCallbackBluetooth extends DoBaseCallback<ISmkCallbackBluetooth> {
    private final int onAdapterStateChanged = 0;
    private final int onDeviceDiscoveryStarted = 1;
    private final int onDeviceFound = 2;
    private final int onDeviceDiscoveryFinished = 3;


    @Override
    protected String getLogTag() {
        return Logutil.makeTagLog(DoCallbackBluetooth.class);
    }

    public synchronized void onAdapterStateChanged(int oldState,int newState){
        Message m = getMessage(onAdapterStateChanged);
        m.arg1 = oldState;
        m.arg2 = newState;
        onEnqueueMessage(m);
    }

    public synchronized void onDeviceDiscoveryStarted(){
        Message m = getMessage(onDeviceDiscoveryStarted);
        onEnqueueMessage(m);
    }

    public synchronized void onDeviceFound(List<BTDeviceInfo> btDeviceInfos){
        Message m = getMessage(onDeviceFound);
        m.obj = btDeviceInfos;
        onEnqueueMessage(m);
    }

    public synchronized void onDeviceDiscoveryFinished(){
        Message m = getMessage(onDeviceDiscoveryFinished);
        onEnqueueMessage(m);
    }

    private void notifyAdapterStateChanged(int oldState,int newState){
        Logutil.i(TAG,"notifyAdapterStateChanged() oldState="+oldState+",newState="+newState);
        synchronized (mRemoteCallbackLists){
            final int n = mRemoteCallbackLists.beginBroadcast();
            for(int i = 0;i < n ;i++){
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onAdapterStateChanged(oldState,newState);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }

    private void notifyDeviceDiscoveryStarted(){
        Logutil.i(TAG,"notifyAdapterStateChanged() ...");
        synchronized (mRemoteCallbackLists){
            final int n = mRemoteCallbackLists.beginBroadcast();
            for(int i = 0;i < n ;i++){
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onDeviceDiscoveryStarted();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }

    private void notifyDeviceFound(List<BTDeviceInfo> btDeviceInfos){
        Logutil.i(TAG,"notifyAdapterStateChanged() ...");
        synchronized (mRemoteCallbackLists){
            final int n = mRemoteCallbackLists.beginBroadcast();
            for(int i = 0;i < n ;i++){
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onDeviceFound(btDeviceInfos);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }

    private void notifyDeviceDiscoveryFinished(){
        Logutil.i(TAG,"notifyAdapterStateChanged() ...");
        synchronized (mRemoteCallbackLists){
            final int n = mRemoteCallbackLists.beginBroadcast();
            for(int i = 0;i < n ;i++){
                try {
                    mRemoteCallbackLists.getBroadcastItem(i).onDeviceDiscoveryFinished();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mRemoteCallbackLists.finishBroadcast();
        }
    }

    @Override
    protected void onDequeueMessage(Message msg) {

        switch (msg.what){
            case onAdapterStateChanged:
                notifyAdapterStateChanged(msg.arg1,msg.arg2);
                break;
            case onDeviceDiscoveryStarted:
                notifyDeviceDiscoveryStarted();
                break;
            case onDeviceFound:
                notifyDeviceFound((List<BTDeviceInfo>) msg.obj);
                break;
            case onDeviceDiscoveryFinished:
                notifyDeviceDiscoveryFinished();
                break;
        }
    }


}
