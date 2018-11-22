package com.semisky.nforeservice.callback;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.UiCallbackMap;

public final class DoCallbackMap extends DoCallback<UiCallbackMap>{
	
    private final int onMapServiceReady = 0;
    private final int onMapStateChanged = 1;
    private final int retMapDownloadedMessage = 2;
    private final int onMapNewMessageReceivedEvent = 3;
    private final int onMapDownloadNotify = 4;
    private final int retMapDatabaseAvailable = 5;
    private final int retMapDeleteDatabaseByAddressCompleted = 6;
    private final int retMapCleanDatabaseCompleted = 7;
    private final int retMapSendMessageCompleted = 8;
    private final int retMapDeleteMessageCompleted = 9;
    private final int retMapChangeReadStatusCompleted = 10;
    private final int onMapMemoryAvailableEvent = 11;
    private final int onMapMessageSendingStatusEvent = 12;
    private final int onMapMessageDeliverStatusEvent = 13;
    private final int onMapMessageShiftedEvent = 14;
    private final int onMapMessageDeletedEvent = 15;
    
    @Override
    protected String getLogTag() {
        return "DoCallbackMap";
    }

    public void onMapServiceReady(){
        Log.d(TAG, "onMapServiceReady()");
        Message m = Message.obtain(mHandler, onMapServiceReady);
        enqueueMessage(m);
    }

    public void onMapStateChanged(String address, int prevState, int newState, int reason) {
        Log.d(TAG, "onMapStateChanged() " + address);
        Message m = getMessage(onMapStateChanged);
        m.obj = address;
        m.arg1 = prevState;
        m.arg2 = newState;
        Bundle b = new Bundle();
        b.putInt("reason", reason);
        m.setData(b);
        enqueueMessage(m);
    }

    public void retMapDownloadedMessage(String address, String handle, String senderName, String senderNumber, String date, String recipientAddr, int type, int folder, boolean readStatus, String subject, String message) {
        Log.d(TAG, "retMapDownloadedMessage() " + address);
        Message m = getMessage(retMapDownloadedMessage);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("handle", handle);
        b.putString("senderName", senderName);
        b.putString("senderNumber", senderNumber);
        b.putString("date", date);
        b.putString("recipientAddr", recipientAddr);
        b.putInt("type", type);
        b.putInt("folder", folder);
        b.putBoolean("readStatus", readStatus);
        b.putString("subject", subject);
        b.putString("message", message);

        m.setData(b);
        enqueueMessage(m);
    }

    public void onMapNewMessageReceivedEvent(String address, String handle, String sender, String message) {
        Log.d(TAG, "onMapNewMessageReceivedEvent()");
        Message m = getMessage(onMapNewMessageReceivedEvent);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("handle", handle);
        b.putString("sender", sender);
        b.putString("message", message);
        m.setData(b);
        enqueueMessage(m);
    }

    public void onMapDownloadNotify(String address, int folder, int totalMessages, int currentMessages) {
        Log.d(TAG, "onMapDownloadNotify() " + address);
        Message m = getMessage(onMapDownloadNotify);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("folder", folder);
        b.putInt("totalMessages", totalMessages);
        b.putInt("currentMessages", currentMessages);
        m.setData(b);
        enqueueMessage(m);
    }

    public void retMapDatabaseAvailable() {
        Log.d(TAG, "retMapDatabaseAvailable()");
        Message m = getMessage(retMapDatabaseAvailable);
        enqueueMessage(m);
    }

    public void retMapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) {
        Log.d(TAG, "retMapDeleteDatabaseByAddressCompleted()");
        Message m = getMessage(retMapDeleteDatabaseByAddressCompleted);
        m.obj = address;
        Bundle b = new Bundle();
        b.putBoolean("isSuccess", isSuccess);
        m.setData(b);
        enqueueMessage(m);
    }

    public void retMapCleanDatabaseCompleted(boolean isSuccess) {
        Log.d(TAG, "retMapCleanDatabaseCompleted()");
        Message m = getMessage(retMapCleanDatabaseCompleted);
        Bundle b = new Bundle();
        b.putBoolean("isSuccess", isSuccess);
        m.setData(b);
        enqueueMessage(m);
    }

    public void retMapSendMessageCompleted(String address, String target, int state) {
        Log.d(TAG, "retMapSendMessageCompleted() " + address);
        Message m = getMessage(retMapSendMessageCompleted);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("target", target);
        b.putInt("message", state);
        m.setData(b);
        enqueueMessage(m);
    }

    public void retMapDeleteMessageCompleted(String address, String handle, int reason) {
        Log.d(TAG, "retMapDeleteMessageCompleted() " + address);
        Message m = getMessage(retMapDeleteMessageCompleted);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("handle", handle);
        b.putInt("reason", reason);
        m.setData(b);
        enqueueMessage(m);
    }

    public void retMapChangeReadStatusCompleted(String address, String handle, int reason) {
        Log.d(TAG, "retMapChangeReadStatusCompleted() " + address);
        Message m = getMessage(retMapChangeReadStatusCompleted);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("handle", handle);
        b.putInt("reason", reason);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onMapMemoryAvailableEvent(String address, int structure, boolean available) {
        Log.d(TAG, "onMapMemoryAvailableEvent() " + address);
        Message m = getMessage(onMapMemoryAvailableEvent);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("structure", structure);
        b.putBoolean("available", available);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onMapMessageSendingStatusEvent(String address, String handle, boolean isSuccess) {
        Log.d(TAG, "onMapMessageSendingStatusEvent() " + address);
        Message m = getMessage(onMapMessageSendingStatusEvent);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("handle", handle);
        b.putBoolean("isSuccess", isSuccess);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onMapMessageDeliverStatusEvent(String address, String handle, boolean isSuccess) {
        Log.d(TAG, "onMapMessageDeliverStatusEvent() " + address);
        Message m = getMessage(onMapMessageDeliverStatusEvent);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("handle", handle);
        b.putBoolean("isSuccess", isSuccess);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onMapMessageShiftedEvent(String address, String handle, int type, int newFolder, int oldFolder) {
        Log.d(TAG, "onMapMessageShiftedEvent() " + address);
        Message m = getMessage(onMapMessageShiftedEvent);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("handle", handle);
        b.putInt("type", type);
        b.putInt("newFolder", newFolder);
        b.putInt("oldFolder", oldFolder);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onMapMessageDeletedEvent(String address, String handle, int type, int folder) {
        Log.d(TAG, "onMapMessageDeletedEvent() " + address);
        Message m = getMessage(onMapMessageDeletedEvent);
        m.obj = address;
        Bundle b = new Bundle();
        b.putString("handle", handle);
        b.putInt("type", type);
        b.putInt("folder", folder);
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
            case onMapServiceReady:
                callbackOnMapServiceReady();
                break;
            case onMapStateChanged:
                callbackOnMapStateChanged(address, prevState, newState, b.getInt("reason"));
                break;
            case retMapDownloadedMessage:
                callbackRetMapDownloadedMessage(address, b.getString("handle"), b.getString("senderName"), b.getString("senderNumber"), b.getString("date"), b.getString("recipientAddr"), b.getInt("type"), b.getInt("folder"), b.getBoolean("readStatus"), b.getString("subject"), b.getString("message"));
                break;
            case onMapNewMessageReceivedEvent:
                callbackOnMapNewMessageReceived(address, b.getString("handle"), b.getString("sender"), b.getString("message"));
                break;
            case onMapDownloadNotify:
                callbackOnMapDownloadNotify(address, b.getInt("folder"), b.getInt("totalMessages"), b.getInt("currentMessages"));
                break;
            case retMapDatabaseAvailable:
                callbackRetMapDatabaseAvailable();
                break;
            case retMapDeleteDatabaseByAddressCompleted:
                callbackRetMapDeleteDatabaseByAddressCompleted(address, b.getBoolean("isSuccess"));
                break;
            case retMapCleanDatabaseCompleted:
                callbackRetMapCleanDatabaseCompleted(b.getBoolean("isSuccess"));
                break;
            case retMapSendMessageCompleted:
                callbackRetMapSendMessageCompleted(address, b.getString("target"), b.getInt("state"));
                break;
            case retMapDeleteMessageCompleted:
                callbackRetMapDeleteMessageCompleted(address, b.getString("handle"), b.getInt("reason"));
                break;
            case retMapChangeReadStatusCompleted:
                callbackRetMapChangeReadStatusCompleted(address, b.getString("handle"), b.getInt("reason"));
                break;
                
            case onMapMemoryAvailableEvent:
                callbackOnMapMemoryAvailableEvent(address, b.getInt("structure"), b.getBoolean("available"));
                break;
                
            case onMapMessageSendingStatusEvent:
                callbackOnMapMessageSendingStatusEvent(address, b.getString("handle"), b.getBoolean("isSuccess"));
                break;
                
            case onMapMessageDeliverStatusEvent:
                callbackOnMapMessageDeliverStatusEvent(address, b.getString("handle"), b.getBoolean("isSuccess"));
                break;
                
            case onMapMessageShiftedEvent:
                callbackOnMapMessageShiftedEvent(address, b.getString("handle"), b.getInt("type"), b.getInt("newFolder"), b.getInt("oldFolder"));
                break;
                
            case onMapMessageDeletedEvent:
                callbackOnMapMessageDeletedEvent(address, b.getString("handle"), b.getInt("type"), b.getInt("folder"));
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }
    }

    private synchronized void callbackOnMapServiceReady(){
        Log.v(TAG, "callbackOnMapServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapServiceReady CallBack Finish.");
    }

    private synchronized void callbackOnMapStateChanged(String address, int prevState, int newState, int reason){
        Log.v(TAG, "callbackOnMapStateChanged() " + address + " state: " + prevState + "->" + newState);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapStateChanged(address, prevState, newState, reason);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapStateChanged CallBack Finish.");
    }

    private synchronized void callbackRetMapDownloadedMessage(String address, String handle, String senderName, String senderNumber, String date, String recipientAddr, int type, int folder, boolean isRead, String subject, String message){
        Log.v(TAG, "callbackRetMapDownloadedMessage() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retMapDownloadedMessage beginBroadcast()");

            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retMapDownloadedMessage(address, handle, senderName, senderNumber, recipientAddr, date, type, folder, isRead, subject, message);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retMapDownloadedMessage CallBack Finish.");
    }

    private synchronized void callbackOnMapNewMessageReceived(String address, String handle, String sender, String message){
        Log.v(TAG, "callbackOnMapNewMessageReceived() " + address + " handle: " + handle);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapNewMessageReceived beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapNewMessageReceivedEvent(address, handle, sender, message);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapNewMessageReceived CallBack Finish.");
    }

    private synchronized void callbackOnMapDownloadNotify(String address, int folder, int totalMessages, int currentMessages){
        Log.v(TAG, "callbackOnMapDownloadNotify() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapDownloadNotify beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapDownloadNotify(address, folder, totalMessages, currentMessages);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapDownloadNotify CallBack Finish.");
    }

    private synchronized void callbackRetMapDatabaseAvailable(){
        Log.v(TAG, "callbackRetMapDatabaseAvailable()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retMapDatabaseAvailable beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retMapDatabaseAvailable();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retMapDatabaseAvailable CallBack Finish.");
    }

    private synchronized void callbackRetMapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess){
        Log.v(TAG, "callbackRetMapDeleteDatabaseByAddressCompleted() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retMapDeleteDatabaseByAddressCompleted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retMapDeleteDatabaseByAddressCompleted(address, isSuccess);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retMapDeleteDatabaseByAddressCompleted CallBack Finish.");
    }

    private synchronized void callbackRetMapCleanDatabaseCompleted(boolean isSuccess){
        Log.v(TAG, "callbackRetMapCleanDatabaseCompleted() " + isSuccess);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retMapCleanDatabaseCompleted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retMapCleanDatabaseCompleted(isSuccess);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retMapCleanDatabaseCompleted CallBack Finish.");
    }

    private synchronized void callbackRetMapSendMessageCompleted(String address, String target, int state){
        Log.v(TAG, "callbackRetMapSendMessageCompleted() " + state);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "callbackRetMapSendMessageCompleted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retMapSendMessageCompleted(address, target, state);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retMapSendMessageCompleted CallBack Finish.");
    }

    private synchronized void callbackRetMapDeleteMessageCompleted(String address, String handle, int reason){
        Log.v(TAG, "callbackRetMapDeleteMessageCompleted() " + reason);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "callbackRetMapDeleteMessageCompleted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retMapDeleteMessageCompleted(address, handle, reason);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retMapDeleteMessageCompleted CallBack Finish.");
    }

    private synchronized void callbackRetMapChangeReadStatusCompleted(String address, String handle, int reason){
        Log.v(TAG, "callbackRetMapReadStatusCompleted() " + reason);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "callbackRetMapReadStatusCompleted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retMapChangeReadStatusCompleted(address, handle, reason);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retMapReadStatusCompleted CallBack Finish.");
    }
    
    private synchronized void callbackOnMapMemoryAvailableEvent(String address, int structure, boolean available){
        Log.v(TAG, "callbackOnMapMemoryAvailableEvent() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapMemoryAvailableEvent beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapMemoryAvailableEvent(address, structure, available);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapMemoryAvailableEvent CallBack Finish.");
    }
    
    private synchronized void callbackOnMapMessageSendingStatusEvent(String address, String handle, boolean isSuccess){
        Log.v(TAG, "callbackOnMapMessageSendingStatusEvent() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapMessageSendingStatusEvent beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapMessageSendingStatusEvent(address, handle, isSuccess);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapMessageSendingStatusEvent CallBack Finish.");
    }
    
    private synchronized void callbackOnMapMessageDeliverStatusEvent(String address, String handle, boolean isSuccess){
        Log.v(TAG, "callbackOnMapMessageSendingStatusEvent() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapMessageDeliverStatusEvent beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapMessageDeliverStatusEvent(address, handle, isSuccess);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapMessageDeliverStatusEvent CallBack Finish.");
    }
    
    private synchronized void callbackOnMapMessageShiftedEvent(String address, String handle, int type, int newFolder, int oldFolder){
        Log.v(TAG, "callbackOnMapMessageShiftedEvent() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapMessageShiftedEvent beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapMessageShiftedEvent(address, handle, type, newFolder, oldFolder);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapMessageShiftedEvent CallBack Finish.");
    }
    
    private synchronized void callbackOnMapMessageDeletedEvent(String address, String handle, int type, int folder){
        Log.v(TAG, "callbackOnMapMessageDeletedEvent() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onMapMessageDeletedEvent beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onMapMessageDeletedEvent(address, handle, type, folder);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onMapMessageDeletedEvent CallBack Finish.");
    }

}
