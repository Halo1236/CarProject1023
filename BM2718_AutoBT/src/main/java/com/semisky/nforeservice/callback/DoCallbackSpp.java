package com.semisky.nforeservice.callback;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.UiCallbackSpp;

public final class DoCallbackSpp extends DoCallback<UiCallbackSpp>{

    private final int onSppServiceReady = 0;
    private final int onSppStateChanged = 1;
    private final int onSppErrorResponse = 2;
    private final int retSppConnectedDeviceAddressList = 3;
    private final int onSppDataReceived = 4;
    private final int onSppSendData = 5;
    private final int onSppAppleIapAuthenticationRequest = 6;

    @Override
    protected String getLogTag() {
        return "NfDoCallbackSpp";
    }

    public void onSppServiceReady(){
        Log.v(TAG, "onSppServiceReady()");
        Message m = Message.obtain(mHandler, onSppServiceReady);
        enqueueMessage(m);
    }

    public void onSppStateChanged(String address, String deviceName, int prevState, int newState) {
        Log.v(TAG, "onSppStateChanged() " + address);
        Message m = getMessage(onSppStateChanged);
        m.obj = address;
        m.arg1 = prevState;
        m.arg2 = newState;
        Bundle b = new Bundle();        
        b.putString("deviceName", deviceName);
        m.setData(b);
        enqueueMessage(m);
    }

    public void onSppErrorResponse(String address, int errorCode) {
        Log.v(TAG, "onSppErrorResponse() " + address);
        Message m = getMessage(onSppErrorResponse);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("errorCode", errorCode);
        m.setData(b);
        enqueueMessage(m);
    }

    public void retSppConnectedDeviceAddressList(int totalNum, String[] addressList, String[] nameList) {
        Log.v(TAG, "retPbapDatabaseQueryNameByPartialNumber()");
        Message m = getMessage(retSppConnectedDeviceAddressList);
        Bundle b = new Bundle();
        b.putInt("totalNum", totalNum);
        b.putStringArray("addressList", addressList);
        b.putStringArray("nameList", nameList);
        m.setData(b);
        enqueueMessage(m);
    }

    public void onSppDataReceived(String address, byte[] receivedData) {
        Log.v(TAG, "onSppDataReceived() " + address);
        Message m = getMessage(onSppDataReceived);
        m.obj = address;
        Bundle b = new Bundle();
        b.putByteArray("receivedData", receivedData);
        m.setData(b);
        enqueueMessage(m);
    }

    public void onSppSendData(String address, int length) {
        Log.v(TAG, "onSppSendData() " + address);
        Message m = getMessage(onSppSendData);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("sendDataLength", length);
        m.setData(b);
        enqueueMessage(m);
    }

    public void onSppAppleIapAuthenticationRequest(String address) {
        Log.v(TAG, "onSppAppleIapAuthenticationRequest() " + address);
        Message m = getMessage(onSppAppleIapAuthenticationRequest);
        m.obj = address;
        Bundle b = new Bundle();
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
            case onSppServiceReady:
                callbackOnSppServiceReady();
                break;
            case onSppStateChanged:
                callbackOnSppStateChanged(address, b.getString("deviceName"), prevState, newState);
                break;
            case onSppErrorResponse:
                callbackOnSppErrorResponse(address, b.getInt("errorCode"));
                break;
            case retSppConnectedDeviceAddressList:
                callbackRetSppConnectedDeviceAddressList(b.getInt("totalNum"), b.getStringArray("addressList"), b.getStringArray("nameList"));
                break;
            case onSppDataReceived:
                callbackOnSppDataReceived(address, b.getByteArray("receivedData"));
                break;
            case onSppSendData:
                callbackOnSppSendData(address, b.getInt("sendDataLength"));
                break;
            case onSppAppleIapAuthenticationRequest:
                callbackOnSppAppleIapAuthenticationRequest(address);
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }
    }

    private synchronized void callbackOnSppServiceReady(){
        Log.v(TAG, "callbackOnSppServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onSppServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onSppServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onSppServiceReady CallBack Finish.");
    }

    private synchronized void callbackOnSppStateChanged(String address, String deviceName, int prevState, int newState){
        Log.d(TAG, "callbackOnSppStateChanged() " + address + " state: " + prevState + "->" + newState);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onSppStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onSppStateChanged(address, deviceName, prevState, newState);
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

    private synchronized void callbackOnSppErrorResponse(String address, int errorCode){
        Log.d(TAG, "callbackOnSppErrorResponse() " + address + " errorCode: " + errorCode);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onSppErrorResponse beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onSppErrorResponse(address, errorCode);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onSppErrorResponse CallBack Finish.");
    }

    private synchronized void callbackRetSppConnectedDeviceAddressList(int totalNum, String[] addressList, String[] nameList){
        Log.d(TAG, "callbackRetSppConnectedDeviceAddressList() totalNum: " + totalNum);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retSppConnectedDeviceAddressList beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retSppConnectedDeviceAddressList(totalNum, addressList, nameList);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retSppConnectedDeviceAddressList CallBack Finish.");
    }

    private synchronized void callbackOnSppDataReceived(String address, byte[] receivedData){
        Log.d(TAG, "callbackOnSppDataReceived() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onSppDataReceived beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onSppDataReceived(address, receivedData);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onSppDataReceived CallBack Finish.");
    }

    private synchronized void callbackOnSppSendData(String address, int length){
        Log.d(TAG, "callbackOnSppSendData() " + address + length);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "callbackOnSppSendData beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onSppSendData(address, length);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"callbackOnSppSendData CallBack Finish.");
    }

    private synchronized void callbackOnSppAppleIapAuthenticationRequest(String address){
        Log.d(TAG, "callbackOnSppAppleIapAuthenticationRequest() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onSppAppleIapAuthenticationRequest beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onSppAppleIapAuthenticationRequest(address);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onSppAppleIapAuthenticationRequest CallBack Finish.");
    }

}
