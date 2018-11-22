package com.semisky.nforeservice.callback;


import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.NfPbapContact;
import com.nforetek.bt.aidl.UiCallbackPbap;

public final class DoCallbackPbap extends DoCallback<UiCallbackPbap>{
	
    private final int onPbapServiceReady = 0;
    private final int onPbapStateChanged = 1;
    private final int retPbapDatabaseQueryNameByNumber = 2;
    private final int retPbapDatabaseQueryNameByPartialNumber = 3;
    private final int retPbapDatabaseAvailable = 4;
    private final int retPbapDeleteDatabaseByAddressCompleted = 5;
    private final int retPbapCleanDatabaseCompleted = 6;
    private final int retPbapDownloadedContact = 7;
    private final int retPbapDownloadedCallLog = 8;
    private final int onPbapDownloadNotify = 9;
    
    @Override
    protected String getLogTag() {
        return "DoCallbackPbap";
    }

    public void onPbapServiceReady(){
        Log.v(TAG, "onPbapServiceReady()");
        Message m = Message.obtain(mHandler, onPbapServiceReady);
        enqueueMessage(m);
    }

    
    public synchronized void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts) {
        Log.v(TAG, "onPbapStateChanged() " + address);
        Message m = getMessage(onPbapStateChanged);
        m.obj = address;
        m.arg1 = prevState;
        m.arg2 = newState;
        Bundle b = new Bundle();
        b.putInt("reason", reason);
        b.putInt("counts", counts);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) {
        Log.v(TAG, "retPbapDatabaseQueryNameByNumber() " + address);
        Message m = getMessage(retPbapDatabaseQueryNameByNumber);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("target", target);
        b.putString("name", name);
        b.putBoolean("isSuccess", isSuccess);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void retPbapDatabaseQueryNameByPartialNumber(String address, String target, String[] names, String[] numbers, boolean isSuccess) {
        Log.v(TAG, "retPbapDatabaseQueryNameByPartialNumber() " + address);
        Message m = getMessage(retPbapDatabaseQueryNameByPartialNumber);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("target", target);
        b.putStringArray("names", names);
        b.putStringArray("numbers", numbers);
        b.putBoolean("isSuccess", isSuccess);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void retPbapDatabaseAvailable(String address) {
        Log.v(TAG, "retPbapDatabaseAvailable() " + address);
        Message m = getMessage(retPbapDatabaseAvailable);
        m.obj = address;
        enqueueMessage(m);
    }

    public synchronized void retPbapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) {
        Log.v(TAG, "retPbapDeleteDatabaseByAddressCompleted() " + address);
        Message m = getMessage(retPbapDeleteDatabaseByAddressCompleted);
        m.obj = address;
        Bundle b = new Bundle();
        b.putBoolean("isSuccess", isSuccess);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void retPbapCleanDatabaseCompleted(boolean isSuccess) {
        Log.v(TAG, "retPbapCleanDatabaseCompleted() isSuccess: " + isSuccess);
        Message m = getMessage(retPbapCleanDatabaseCompleted);
        Bundle b = new Bundle();
        b.putBoolean("isSuccess", isSuccess);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void retPbapDownloadedContact(NfPbapContact contact){
        Log.v(TAG, "retPbapDownloadedContact()");
        Message m = getMessage(retPbapDownloadedContact);
        Bundle b = new Bundle();
        b.putParcelable("contact", contact);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void retPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName, String number, int type, String timestamp){
        Log.v(TAG, "retPbapDownloadedCallLog()");
        Message m = getMessage(retPbapDownloadedCallLog);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("firstName", firstName);
        b.putString("middleName", middleName);
        b.putString("lastName", lastName);
        b.putString("number", number);
        b.putString("timestamp", timestamp);
        b.putInt("type", type);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts) {
        Log.v(TAG, "onPbapDownloadNotify()");
        Message m = getMessage(onPbapDownloadNotify);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("storage", storage);
        b.putInt("totalContacts", totalContacts);
        b.putInt("downloadedContacts", downloadedContacts);
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
            case onPbapServiceReady:
                callbackOnPbapServiceReady();
                break;     
            case onPbapStateChanged:
                callbackOnPbapStateChanged(address, prevState, newState, b.getInt("reason"), b.getInt("counts"));
                break;
            case retPbapDatabaseQueryNameByNumber:
                callbackRetPbapDatabaseQueryNameByNumber(address, b.getString("target"), b.getString("name"), b.getBoolean("isSuccess"));
                break;
            case retPbapDatabaseQueryNameByPartialNumber:
                callbackRetPbapDatabaseQueryNameByPartialNumber(address, b.getString("target"), b.getStringArray("names"), b.getStringArray("numbers"), b.getBoolean("isSuccess"));
                break;
            case retPbapDatabaseAvailable:
                callbackRetPbapDatabaseAvailable(address);
                break;
            case retPbapDeleteDatabaseByAddressCompleted:
                callbackRetPbapDeleteDatabaseByAddressCompleted(address, b.getBoolean("isSuccess"));
                break;
            case retPbapCleanDatabaseCompleted:
                callbackRetPbapCleanDatabaseCompleted(b.getBoolean("isSuccess"));
                break;
            case retPbapDownloadedContact:
                callbackRetPbapDownloadedContact((NfPbapContact)b.getParcelable("contact"));
                break;
            case retPbapDownloadedCallLog:
                callbackRetPbapDownloadedCallLog(b.getString("address"), b.getString("firstName"), b.getString("middleName"), b.getString("lastName"), b.getString("number"), b.getInt("type"), b.getString("timestamp"));
                break;
            case onPbapDownloadNotify:
                callbackOnPbapDownloadNotify(address, b.getInt("storage"), b.getInt("totalContacts"), b.getInt("downloadedContacts"));
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }
    }

    private synchronized void callbackOnPbapServiceReady(){
        Log.v(TAG, "callbackOnPbapServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onPbapServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onPbapServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onPbapServiceReady CallBack Finish.");
    }

    private synchronized void callbackOnPbapStateChanged(String address, int prevState, int newState, int reason, int counts){
        Log.d(TAG, "callbackOnPbapStateChanged() " + address + " state: " + prevState + "->" + newState);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onPbapStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onPbapStateChanged(address, prevState, newState, reason, counts);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onPbapStateChanged CallBack Finish.");
    }

    private synchronized void callbackRetPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess){
        Log.d(TAG, "callbackRetPbapDatabaseQueryNameByNumber() " + address + " target: " + target + " name: " + name + " isSuccess: " + isSuccess);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retPbapDatabaseQueryNameByNumber beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retPbapDatabaseQueryNameByNumber(address, target, name, isSuccess);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retPbapDatabaseQueryNameByNumber CallBack Finish.");
    }

    private synchronized void callbackRetPbapDatabaseQueryNameByPartialNumber(String address, String target, String[] names, String[] numbers, boolean isSuccess){
        Log.d(TAG, "callbackRetPbapDatabaseQueryNameByPartialNumber() " + address + " target: " + target + " isSuccess: " + isSuccess);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retPbapDatabaseQueryNameByPartialNumber beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retPbapDatabaseQueryNameByPartialNumber(address, target, names, numbers, isSuccess);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retPbapDatabaseQueryNameByPartialNumber CallBack Finish.");
    }

    private synchronized void callbackRetPbapDatabaseAvailable(String address) {
        Log.d(TAG, "callbackRetPbapDatabaseAvailable() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retPbapDatabaseAvailable beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retPbapDatabaseAvailable(address);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retPbapDatabaseAvailable CallBack Finish.");
    }

    private synchronized void callbackRetPbapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess){
        Log.d(TAG, "callbackRetPbapDeleteDatabaseByAddressCompleted() " + address + " isSuccess: " + isSuccess);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retPbapDeleteDatabaseByAddressCompleted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retPbapDeleteDatabaseByAddressCompleted(address, isSuccess);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retPbapDeleteDatabaseByAddressCompleted CallBack Finish.");
    }

    private synchronized void callbackRetPbapCleanDatabaseCompleted(boolean isSuccess){
        Log.d(TAG, "callbackRetPbapCleanDatabaseCompleted() isSuccess: " + isSuccess);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retPbapCleanDatabaseCompleted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retPbapCleanDatabaseCompleted(isSuccess);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retPbapCleanDatabaseCompleted CallBack Finish.");
    }
    
    private synchronized void callbackRetPbapDownloadedContact(NfPbapContact contact){
        Log.d(TAG, "callbackRetPbapDownloadedContact()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retPbapDownloadedContact beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retPbapDownloadedContact(contact);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retPbapDownloadedContact CallBack Finish.");
    }
    
    private synchronized void callbackRetPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName, String number, int type, String timestamp){
        Log.d(TAG, "callbackRetPbapDownloadedCallLog()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retPbapDownloadedCallLog beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retPbapDownloadedCallLog(address, firstName, middleName, lastName, number, type, timestamp);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retPbapDownloadedCallLog CallBack Finish.");
    }
    
    private synchronized void callbackOnPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts){
        Log.d(TAG, "callbackOnPbapDownloadNotify() " + address + " storage: " + storage + " downloaded: " + downloadedContacts + "/" + totalContacts);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "callbackOnPbapDownloadNotify beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onPbapDownloadNotify(address, storage, totalContacts, downloadedContacts);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"callbackOnPbapDownloadNotify CallBack Finish.");
    }
    
}
