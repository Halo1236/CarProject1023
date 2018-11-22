package com.semisky.nforeservice.callback;


import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.UiCallbackBluetooth;

public final class DoCallbackBluetooth extends DoCallback<UiCallbackBluetooth>{

    private final int onBluetoothServiceReady = 0;
    private final int onAdapterStateChanged = 1;
    private final int onAdapterDiscoverableModeChanged = 2;
    private final int onAdapterDiscoveryStarted = 3;
    private final int onAdapterDiscoveryFinished = 4;
    private final int retPairedDevices = 5;
    private final int onDeviceFound = 6;
    private final int onDeviceBondStateChanged = 7;
    private final int onDeviceUuidsUpdated = 8;
    private final int onLocalAdapterNameChanged = 9;
    private final int onDeviceOutOfRange = 10;
    
    private final int onHfpStateChanged = 101;
    private final int onA2dpStateChanged = 102;
    private final int onAvrcpStateChanged = 103;
    
    private final int onBtRoleModeChanged = 201;
    private final int onBtAutoConnectStateChanged = 202;
    
    @Override
    protected String getLogTag() {
        return "DoCallbackBluetooth";
    }

    public void onBluetoothServiceReady(){
        Log.v(TAG, "onBluetoothServiceReady()");
        Message m = Message.obtain(mHandler, onBluetoothServiceReady);
        enqueueMessage(m);
    }
    
    public synchronized void onHfpStateChanged(String address, int prevState, int newState){
        Log.v(TAG, "onHfpStateChanged() : " + prevState + " -> " + newState);
        Message m = getMessage(onHfpStateChanged);
        m.arg1 = prevState;
        m.arg2 = newState;
        m.obj = address;
        enqueueMessage(m);
    }
    
    public synchronized void onA2dpStateChanged(String address, int prevState, int newState){
        Log.v(TAG, "onA2dpStateChanged() : " + prevState + " -> " + newState);
        Message m = Message.obtain(mHandler, onA2dpStateChanged);
        m.arg1 = prevState;
        m.arg2 = newState;
        m.obj = address;
        enqueueMessage(m);
    }
    
    public synchronized void onAvrcpStateChanged(String address, int prevState, int newState){
        Log.v(TAG, "onAvrcpStateChanged(): " + prevState + " -> " + newState);
        Message m = getMessage(onAvrcpStateChanged);
        m.arg1 = prevState;
        m.arg2 = newState;
        m.obj = address;
        enqueueMessage(m);
    }
    
    public synchronized void onBtRoleModeChanged(int mode){
        Log.v(TAG, "onBtRoleModeChanged(): " + mode);
        Message m = getMessage(onBtRoleModeChanged);
        Bundle b = new Bundle();
        b.putInt("mode", mode);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onBtAutoConnectStateChanged(String address, int prevState, int newState){
        Log.v(TAG, "onBtAutoConnectStateChanged(): " + address);
        Message m = getMessage(onBtAutoConnectStateChanged);
        m.arg1 = prevState;
        m.arg2 = newState;
        m.obj = address;
        enqueueMessage(m);
    }
    
    
    
    public synchronized void onAdapterStateChanged(int prevState, int newState){
        Log.v(TAG, "onAdapterStateChanged(): " + prevState + "->" + newState);
        Message m = getMessage(onAdapterStateChanged);
        m.arg1 = prevState;
        m.arg2 = newState;
        enqueueMessage(m);
    }
    
    public synchronized void onAdapterDiscoverableModeChanged(int prevState, int newState) {
        Log.v(TAG, "onAdapterDiscoverableModeChanged() State: " + prevState + "->" + newState);
        Message m = getMessage(onAdapterDiscoverableModeChanged);
        m.arg1 = prevState;
        m.arg2 = newState;
        enqueueMessage(m);
    }
    
    public synchronized void onAdapterDiscoveryStarted() {
        Log.v(TAG, "onAdapterDiscoveryStarted()");
        Message m = getMessage(onAdapterDiscoveryStarted);        
        enqueueMessage(m);
    }
    
    public synchronized void onAdapterDiscoveryFinished() {
        Log.v(TAG, "onAdapterDiscoveryFinished()");
        Message m = getMessage(onAdapterDiscoveryFinished);
        enqueueMessage(m);
    }
        
    public synchronized void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category) {
        Log.v(TAG, "retPairedDevices() " + address);
        Message m = getMessage(retPairedDevices);
        Bundle b = new Bundle();
        b.putInt("elements", elements);
        b.putStringArray("address", address);
        b.putStringArray("name", name);
        b.putIntArray("supportProfile", supportProfile);
        b.putByteArray("category", category);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onDeviceFound(String address, String name, byte category) {
        Log.v(TAG, "onDeviceFound() " + address);
        Message m = getMessage(onDeviceFound);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putByte("category", category);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onDeviceBondStateChanged(String address, String name, int prevState, int newState) {
        Log.v(TAG, "onDeviceBondStateChanged() " + address);
        Message m = getMessage(onDeviceBondStateChanged);
        m.obj = address;
        m.arg1 = prevState;
        m.arg2 = newState;
        Bundle b = new Bundle();
        b.putString("name", name);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onDeviceUuidsUpdated(String address, String name, int supportProfile) {
        Log.v(TAG, "onDeviceUuidsUpdated() " + address);
        Message m = getMessage(onDeviceUuidsUpdated);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putInt("supportProfile", supportProfile);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onLocalAdapterNameChanged(String name) {
        Log.v(TAG, "onLocalAdapterNameChanged() name: " + name);
        Message m = getMessage(onLocalAdapterNameChanged);
        Bundle b = new Bundle();
        b.putString("name", name);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onDeviceOutOfRange(String address) {
        Log.v(TAG, "onDeviceOutOfRange() " + address);
        Message m = getMessage(onDeviceOutOfRange);
        Bundle b = new Bundle();
        b.putString("address", address);
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
            case onBluetoothServiceReady:
                callbackOnBluetoothServiceReady();
                break;
            case onAdapterStateChanged:
                callbackOnAdapterStateChanged(prevState, newState);
                break;
            case onAdapterDiscoverableModeChanged:
                callbackOnAdapterDiscoverableModeChanged(prevState, newState);
                break;
            case onAdapterDiscoveryStarted:
                callbackOnAdapterDiscoveryStarted();
                break;
            case onAdapterDiscoveryFinished:
                callbackOnAdapterDiscoveryFinished();
                break;
            case retPairedDevices: 
                callbackRetPairedDevices(b.getInt("elements"), b.getStringArray("address"), b.getStringArray("name"), b.getIntArray("supportProfile"), b.getByteArray("category"));
                break;
            case onDeviceFound:
                callbackOnDeviceFound(address, b.getString("name"), b.getByte("category"));
                break;
            case onDeviceBondStateChanged:
                callbackOnDeviceBondStateChanged(address, b.getString("name"), prevState, newState);
                break;
            case onDeviceUuidsUpdated:
                callbackOnDeviceUuidsUpdated(address, b.getString("name"), b.getInt("supportProfile"));
                break;
            case onLocalAdapterNameChanged:
                callbackOnLocalAdapterNameChanged(b.getString("name"));
                break;
            case onDeviceOutOfRange:
                callbackOnDeviceOutOfRange(b.getString("address"));
                break;      
                
            case onHfpStateChanged:
                callbackOnHfpStateChanged(address, prevState, newState);
                break;
            case onA2dpStateChanged:
                callbackOnA2dpStateChanged(address, prevState, newState);
                break;
            case onAvrcpStateChanged:
                callbackOnAvrcpStateChanged(address, prevState, newState);
                break;
            case onBtRoleModeChanged:
                callbackOnBtRoleModeChanged(b.getInt("mode"));
                break;
            case onBtAutoConnectStateChanged:
                callbackOnBtAutoConnectStateChanged(address, prevState, newState);
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }
    }

    private synchronized void callbackOnBluetoothServiceReady(){
        Log.v(TAG, "callbackOnBluetoothServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onBluetoothServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onBluetoothServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onBluetoothServiceReady CallBack Finish.");
    }

    private synchronized void callbackOnAdapterStateChanged(int prevState, int newState){
        Log.d(TAG, "callbackOnAdapterStateChanged() State: " + prevState + "->" + newState);

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAdapterStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onAdapterStateChanged broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAdapterStateChanged(prevState, newState);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onAdapterStateChanged CallBack Finish.");
    }

    private synchronized void callbackOnAdapterDiscoverableModeChanged(int prevState, int newState){
        Log.d(TAG, "callbackOnAdapterDiscoverableModeChanged() State: " + prevState + "->" + newState);

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAdapterDiscoverableModeChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onAdapterDiscoverableModeChanged broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAdapterDiscoverableModeChanged(prevState, newState);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onAdapterStateChanged CallBack Finish.");
    }

    private synchronized void callbackOnAdapterDiscoveryStarted(){
        Log.d(TAG, "callbackOnAdapterDiscoveryStarted()");

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAdapterDiscoveryStarted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onAdapterDiscoveryStarted broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAdapterDiscoveryStarted();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onAdapterDiscoveryStarted CallBack Finish.");
    }

    private synchronized void callbackOnAdapterDiscoveryFinished(){
        Log.d(TAG, "callbackOnAdapterDiscoveryFinished()");

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAdapterDiscoveryFinished beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onAdapterDiscoveryFinished broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAdapterDiscoveryFinished();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onAdapterDiscoveryFinished CallBack Finish.");
    }

    private synchronized void callbackRetPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category){
        Log.d(TAG, "callbackRetPairedDevices() elements: " + elements);

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retPairedDevices beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "retPairedDevices broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retPairedDevices(elements, address, name, supportProfile, category);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"retPairedDevices CallBack Finish.");
    }

    private synchronized void callbackOnDeviceFound(String address, String name, byte category){
        Log.d(TAG, "callbackOnDeviceFound() " + address + " name: " + name + " category: " + category);

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onDeviceFound beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onDeviceFound broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onDeviceFound(address, name, category);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onDeviceFound CallBack Finish.");
    }

    private synchronized void callbackOnDeviceBondStateChanged(String address, String name, int prevState, int newState){
        Log.d(TAG, "callbackOnDeviceBondStateChanged() " + address + " name: " + name + " State: " + prevState + "->" + newState);

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onDeviceBondStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onDeviceBondStateChanged broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onDeviceBondStateChanged(address, name, prevState, newState);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onDeviceBondStateChanged CallBack Finish.");
    }

    private synchronized void callbackOnDeviceUuidsUpdated(String address, String name, int supportProfile){
        Log.d(TAG, "callbackOnDeviceUuidsUpdated() " + address + " name: " + name + " supportProfile: " + supportProfile);

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onDeviceUuidsUpdated beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onDeviceUuidsUpdated broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onDeviceUuidsUpdated(address, name, supportProfile);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onDeviceUuidsUpdated CallBack Finish.");
    }

    private synchronized void callbackOnLocalAdapterNameChanged(String name){
        Log.d(TAG, "callbackOnLocalAdapterNameChanged() name: " + name);

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onLocalAdapterNameChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onLocalAdapterNameChanged broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onLocalAdapterNameChanged(name);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onLocalAdapterNameChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnDeviceOutOfRange(String address){
        Log.d(TAG, "callbackOnDeviceOutOfRange() " + address);

        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onDeviceOutOfRange beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            Log.v(TAG, "onDeviceOutOfRange broadcast count : " + n);
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onDeviceOutOfRange(address);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }

            mRemoteCallbacks.finishBroadcast();            
        }
        Log.v(TAG,"onDeviceOutOfRange CallBack Finish.");
    }
    
    
    
    // Customize
    
    private synchronized void callbackOnHfpStateChanged(String address, int prevState, int newState){
        Log.d(TAG, "callbackOnHfpStateChanged() " + address + " state: " + prevState + "->" + newState);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpStateChanged(address, prevState, newState);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpStateChanged CallBack Finish.");
    }    

    private synchronized void callbackOnA2dpStateChanged(String address, int prevState, int newState){
        Log.v(TAG, "callbackOnA2dpStateChanged() : " + prevState + " -> " + newState);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onA2dpStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onA2dpStateChanged(address, prevState, newState);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onA2dpStateChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcpStateChanged(String address, int prevState, int newState){
        Log.d(TAG, "callbackOnAvrcpStateChanged() " + address + " state: " + prevState + " -> " + newState);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcpStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcpStateChanged(address, prevState, newState);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcpStateChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnBtRoleModeChanged(int mode){
        Log.d(TAG, "callbackOnBtRoleModeChanged() " + mode);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onBtRoleModeChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onBtRoleModeChanged(mode);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onBtRoleModeChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnBtAutoConnectStateChanged(String address, int prevState, int newState){
        Log.d(TAG, "callbackOnBtAutoConnectStateChanged() " + address + " state: " + prevState + " -> " + newState);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onBtAutoConnectStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onBtAutoConnectStateChanged(address, prevState, newState);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onBtAutoConnectStateChanged CallBack Finish.");
    }
}
