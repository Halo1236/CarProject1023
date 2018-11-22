package com.semisky.nforeservice.callback;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.UiCallbackOpp;

public final class DoCallbackOpp extends DoCallback<UiCallbackOpp>{
	
    private final int onOppServiceReady = 0;
    private final int onOppStateChanged = 1;
    private final int onOppReceiveFileInfo = 2;
    private final int onOppReceiveProgress = 3;
    
    @Override
    protected String getLogTag() {
        return "NfDoCallbackOpp";
    }

    public void onOppServiceReady(){
        Log.v(TAG, "onOppServiceReady()");
        Message m = Message.obtain(mHandler, onOppServiceReady);
        enqueueMessage(m);
    }
    
    public void onOppStateChanged(String address, int preState, int currentState, int reason) {
        Log.v(TAG, "onOppStateChanged() " + address);
        Message m = getMessage(onOppStateChanged);
        m.obj = address;
        m.arg1 = preState;
        m.arg2 = currentState;
        Bundle b = new Bundle();        
        b.putInt("reason", reason);
        m.setData(b);
        enqueueMessage(m);
    }

    public void onOppReceiveFileInfo(String fileName, int fileSize, String deviceName, String savePath) {
        Log.v(TAG, "onOppReceiveFileInfo() "+ "fileName: "+ fileName+ "fileSize: "+ fileSize + "deviceName: "+ deviceName + "savePath: "+ savePath);
        
        Message m = getMessage(onOppReceiveFileInfo);
        Bundle b = new Bundle();
        b.putString("fileName", fileName);
        b.putInt("fileSize", fileSize);
        b.putString("deviceName", deviceName);
        b.putString("savePath", savePath);
        m.setData(b);
        enqueueMessage(m);
    }

    public void onOppReceiveProgress(int receivedOffset) {
        Log.v(TAG, "onOppReceiveProgress() "+ "receivedOffset: "+ receivedOffset);
        
        Message m = getMessage(onOppReceiveProgress);
        Bundle b = new Bundle();
        b.putInt("receivedOffset", receivedOffset);
        m.setData(b);
        enqueueMessage(m);
    }
    
    
    @Override
    protected void dequeueMessage(Message msg) {
        Bundle b = msg.getData();
        String address = (String)msg.obj;
        int prevState = msg.arg1;
        int newState = msg.arg2;
        switch (msg.what) {
            case onOppServiceReady:
                callbackOnOppServiceReady();
                break;
            case onOppStateChanged:
            	callbackOnOppStateChanged(address, prevState, newState, b.getInt("reason"));
                break;
            case onOppReceiveFileInfo:
            	callbackOnOppReceiveFileInfo(b.getString("fileName"), b.getInt("fileSize"), b.getString("deviceName"), b.getString("savePath"));
                break;
            case onOppReceiveProgress:
            	callbackOnOppReceiveProgress(b.getInt("receivedOffset"));
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }
    }

    private synchronized void callbackOnOppServiceReady(){
        Log.v(TAG, "callbackOnOppServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onOppServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onOppServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onOppServiceReady CallBack Finish.");
    }

    private synchronized void callbackOnOppStateChanged(String address, int preState, int currentState, int reason){
        Log.d(TAG, "callbackOnOppStateChanged() " + address + " state: " + preState + "->" + currentState+", reason: "+reason);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onOppStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onOppStateChanged(address, preState, currentState, reason);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onSppStateChanged CallBack Finish.");
    }

    private synchronized void callbackOnOppReceiveFileInfo(String fileName, int fileSize, String deviceName, String savePath){
        Log.d(TAG, "callbackOnOppReceiveFileInfo() " + "fileName: "+ fileName+ "fileSize: "+ fileSize + "deviceName: "+ deviceName + "savePath: "+ savePath);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onOppReceiveFileInfo beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onOppReceiveFileInfo(fileName, fileSize, deviceName, savePath);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onOppReceiveFileInfo CallBack Finish.");
    }

    private synchronized void callbackOnOppReceiveProgress(int receivedOffset){
        Log.d(TAG, "callbackOnOppReceiveProgres() " + "receivedOffset: "+ receivedOffset);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onOppReceiveProgress beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onOppReceiveProgress(receivedOffset);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onOppReceiveProgress CallBack Finish.");
    }
    
}
