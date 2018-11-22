package com.semisky.nforeservice.callback;


import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.aidl.UiCallbackHfp;

public final class DoCallbackHfp extends DoCallback<UiCallbackHfp>{
	
    private final int onHfpServiceReady = 0;
    private final int onHfpStateChanged = 1;
    private final int onHfpAudioStateChanged = 2;
    private final int onHfpVoiceDial = 3;
    private final int onHfpErrorResponse = 4;
    private final int retHfpRemoteNetworkOperator = 5;
    private final int retHfpRemoteSubscriberNumber = 6;
    private final int onHfpRemoteTelecomService = 7;
    private final int onHfpRemoteRoamingStatus = 8;
    private final int onHfpRemoteBatteryIndicator = 9;
    private final int onHfpRemoteSignalStrength = 10;
    private final int onHfpCallChanged = 11;
    
    private final int retPbapDatabaseQueryNameByNumber = 101;
    
    @Override
    protected String getLogTag() {
        return "DoCallbackHfp";
    }
    
    public void onHfpServiceReady(){
        Log.v(TAG, "onHfpServiceReady()");
        Message m = Message.obtain(mHandler, onHfpServiceReady);
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
    
    
	public synchronized void onHfpStateChanged(String address, int prevState, int newState){
        Log.v(TAG, "onHfpStateChanged() : " + prevState + " -> " + newState);
        Message m = getMessage(onHfpStateChanged);
        m.arg1 = prevState;
        m.arg2 = newState;
        m.obj = address;
        enqueueMessage(m);
    }

    public synchronized void onHfpAudioStateChanged(String address, int prevState, int newState) {
        Log.v(TAG, "onHfpScoStateChanged() " + address);
        Message m = getMessage(onHfpAudioStateChanged);
        m.obj = address;
        m.arg1 = prevState;
        m.arg2 = newState;
        enqueueMessage(m);
    }

    public synchronized void onHfpVoiceDial(String address,boolean isVoiceDialOn) {
        Log.v(TAG, "onHfpVoiceDial() " + address);
        Message m = getMessage(onHfpVoiceDial);
        m.obj = address;
        Bundle b = new Bundle();
        b.putBoolean("isVoiceDialOn", isVoiceDialOn);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onHfpErrorResponse(String address, int code) {
        Log.v(TAG, "onHfpErrorResponse() " + address);
        Message m = getMessage(onHfpErrorResponse);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("code", code);
        m.setData(b);
        enqueueMessage(m);
    }

//    public synchronized void retHfpRemoteNetworkOperator(String address, String mode, int format, String operator) {
//        Log.v(TAG, "retHfpRemoteNetworkOperator() " + address);
//        Message m = getMessage(retHfpRemoteNetworkOperator);
//        m.obj = address;
//        Bundle b = new Bundle();
//        b.putString("mode", mode);
//        b.putInt("format", format);
//        b.putString("operator", operator);
//        m.setData(b);
//        enqueueMessage(m);
//    }
//
//    public synchronized void retHfpRemoteSubscriberNumber(String address, String number, int type, int service) {
//        Log.v(TAG, "retHfpRemoteSubscriberNumber() " + address);
//        Message m = getMessage(retHfpRemoteSubscriberNumber);
//        m.obj = address;
//        Bundle b = new Bundle();
//        b.putString("number", number);
//        b.putInt("type", type);
//        b.putInt("service", service);
//        m.setData(b);
//        enqueueMessage(m);
//    }

    public synchronized void onHfpRemoteTelecomService(String address, boolean isTelecomServiceOn) {
        Log.v(TAG, "onHfpRemoteTelecomService() " + address);
        Message m = getMessage(onHfpRemoteTelecomService);
        m.obj = address;
        Bundle b = new Bundle();
        b.putBoolean("isTelecomServiceOn", isTelecomServiceOn);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onHfpRemoteRoamingStatus(String address, boolean isRoamingOn) {
        Log.v(TAG, "onHfpRemoteRoamingStatus() " + address);
        Message m = getMessage(onHfpRemoteRoamingStatus);
        m.obj = address;
        Bundle b = new Bundle();
        b.putBoolean("isRoamingOn", isRoamingOn);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue) {
        Log.v(TAG, "onHfpRemoteBatteryIndicator() " + address);
        Message m = getMessage(onHfpRemoteBatteryIndicator);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("currentValue", currentValue);
        b.putInt("maxValue", maxValue);
        b.putInt("minValue", minValue);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength) {
        Log.v(TAG, "onHfpRemoteSignalStrength() " + address);
        Message m = getMessage(onHfpRemoteSignalStrength);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("currentStrength", currentStrength);
        b.putInt("maxStrength", maxStrength);
        b.putInt("minStrength", minStrength);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onHfpCallChanged(String address, NfHfpClientCall call) {
        Log.v(TAG, "onHfpCallChanged() " + address);
        Message m = getMessage(onHfpCallChanged);
        m.obj = address;
        Bundle b = new Bundle();
        b.putParcelable("list", call);
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
            case onHfpServiceReady:
                callbackOnHfpServiceReady();
                break;   
            case onHfpStateChanged:
                callbackOnHfpStateChanged(address, prevState, newState);
                break;
            case onHfpAudioStateChanged:
                callbackOnHfpAudioStateChanged(address, prevState, newState);
                break;
            case onHfpVoiceDial:
                callbackOnHfpVoiceDial(address, b.getBoolean("isVoiceDialOn"));
                break;
            case onHfpErrorResponse:
                callbackOnHfpErrorResponse(address, b.getInt("code"));
                break;
//            case retHfpRemoteNetworkOperator:
//                callbackRetHfpRemoteNetworkOperator(address, b.getString("mode"), b.getInt("format"), b.getString("operator"));
//                break;
//            case retHfpRemoteSubscriberNumber:
//                callbackRetHfpRemoteSubscriberNumber(address, b.getString("number"), b.getInt("type"), b.getInt("service"));
//                break;
            case onHfpRemoteTelecomService:
                callbackOnHfpRemoteTelecomService(address, b.getBoolean("isTelecomServiceOn"));
                break;
            case onHfpRemoteRoamingStatus:
                callbackOnHfpRemoteRoamingStatus(address, b.getBoolean("isRoamingOn"));
                break;
            case onHfpRemoteBatteryIndicator:
                callbackOnHfpRemoteBatteryIndicator(address, b.getInt("currentValue"), b.getInt("maxValue"), b.getInt("minValue"));
                break;
            case onHfpRemoteSignalStrength:
                callbackOnHfpRemoteSignalStrength(address, b.getInt("currentStrength"), b.getInt("maxStrength"), b.getInt("minStrength"));
                break;
            case onHfpCallChanged:
                callbackOnHfpCallChanged(address, (NfHfpClientCall)b.getParcelable("list"));
                break;
                
            case retPbapDatabaseQueryNameByNumber:
                callbackRetPbapDatabaseQueryNameByNumber(address, b.getString("target"), b.getString("name"), b.getBoolean("isSuccess"));
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }
    }

    private synchronized void callbackOnHfpServiceReady(){
        Log.v(TAG, "callbackOnHfpServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpServiceReady CallBack Finish.");
    }
	
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
    
	private synchronized void callbackOnHfpAudioStateChanged(String address, int prevState, int newState){
        Log.d(TAG, "callbackOnHfpAudioStateChanged() " + address + " state: " + prevState + "->" + newState);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpAudioStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpAudioStateChanged(address, prevState, newState);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpAudioStateChanged CallBack Finish.");
    }
    
	private synchronized void callbackOnHfpVoiceDial(String address, boolean isVoiceDialOn){
        Log.d(TAG, "callbackOnHfpVoiceDial() " + address + " isVoiceDialOn: " + isVoiceDialOn);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpVoiceDial beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpVoiceDial(address, isVoiceDialOn);
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
    
	private synchronized void callbackOnHfpErrorResponse(String address, int code){
        Log.d(TAG, "callbackOnHfpErrorResponse() " + address + " code: " + code);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpErrorResponse beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpErrorResponse(address, code);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpErrorResponse CallBack Finish.");
    }
    
//	private synchronized void callbackRetHfpRemoteNetworkOperator(String address, String mode, int format, String operator){
//        Log.d(TAG, "callbackRetHfpRemoteNetworkOperator() " + address + " mode: " + mode + " format: " + format + " operator: " + operator);
//        synchronized(mRemoteCallbacks){
//            Log.v(TAG, "retHfpRemoteNetworkOperator beginBroadcast()");
//            final int n = mRemoteCallbacks.beginBroadcast();
//            for (int i = 0; i < n; i++) {
//                try {
//                    mRemoteCallbacks.getBroadcastItem(i).retHfpRemoteNetworkOperator(address, mode, format, operator);
//                } catch (RemoteException e) {
//                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
//                }
//            }
//            mRemoteCallbacks.finishBroadcast();         
//        }
//        Log.v(TAG,"retHfpRemoteNetworkOperator CallBack Finish.");
//    }
//    
//	private synchronized void callbackRetHfpRemoteSubscriberNumber(String address, String number, int type, int service){
//        Log.d(TAG, "callbackRetHfpRemoteSubscriberNumber() " + address + " number: " + number + " type: " + type + " service: " + service);
//        synchronized(mRemoteCallbacks){
//            Log.v(TAG, "retHfpRemoteSubscriberNumber beginBroadcast()");
//            final int n = mRemoteCallbacks.beginBroadcast();
//            for (int i = 0; i < n; i++) {
//                try {
//                    mRemoteCallbacks.getBroadcastItem(i).retHfpRemoteSubscriberNumber(address, number, type, service);
//                } catch (RemoteException e) {
//                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
//                }
//            }
//            mRemoteCallbacks.finishBroadcast();         
//        }
//        Log.v(TAG,"retHfpRemoteSubscriberNumber CallBack Finish.");
//    }
    
	private synchronized void callbackOnHfpRemoteTelecomService(String address, boolean isTelecomServiceOn){
        Log.d(TAG, "callbackOnHfpRemoteTelecomService() " + address + " isTelecomServiceOn: " + isTelecomServiceOn);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpRemoteTelecomService beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpRemoteTelecomService(address, isTelecomServiceOn);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpRemoteTelecomService CallBack Finish.");
    }
    
	private synchronized void callbackOnHfpRemoteRoamingStatus(String address, boolean isRoamingOn){
        Log.d(TAG, "callbackOnHfpRemoteRoamingStatus() " + address + " isRoamingOn: " + isRoamingOn);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpRemoteRoamingStatus beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpRemoteRoamingStatus(address, isRoamingOn);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpRemoteRoamingStatus CallBack Finish.");
    }
    
	private synchronized void callbackOnHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue){
        Log.d(TAG, "callbackOnHfpRemoteBatteryIndicator() " + address + " currentValue: " + currentValue + " (" + minValue + "-" + maxValue + ")");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpRemoteBatteryIndicator beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpRemoteBatteryIndicator(address, currentValue, maxValue, minValue);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpRemoteBatteryIndicator CallBack Finish.");
    }
    
	private synchronized void callbackOnHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength){
        Log.d(TAG, "callbackOnHfpRemoteSignalStrength() " + address + " currentStrength: " + currentStrength + " (" + maxStrength + "-" + minStrength + ")");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpRemoteSignalStrength beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpRemoteSignalStrength(address, currentStrength, maxStrength, minStrength);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpRemoteSignalStrength CallBack Finish.");
    }
    
	private synchronized void callbackOnHfpCallChanged(String address, NfHfpClientCall call){
        Log.d(TAG, "callbackOnHfpCallChanged() " + address);
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHfpCallChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHfpCallChanged(address, call);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHfpCallChanged CallBack Finish.");
    }
	
	
	// Customize

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
}
