package com.semisky.nforeservice.callback;


import android.os.Bundle;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.UiCallbackGattServer;

public final class DoCallbackGattServer extends DoCallback<UiCallbackGattServer>{

    private final int onGattServiceReady = 0;
    private final int onGattServerStateChanged = 2;
    private final int onGattServerServiceAdded = 3;
    private final int onGattServerServiceDeleted = 4;
    private final int onGattServerCharacteristicReadRequest = 5;
    private final int onGattServerDescriptorReadRequest = 6;
    private final int onGattServerCharacteristicWriteRequest = 7;
    private final int onGattServerDescriptorWriteRequest = 8;
    private final int onGattServerExecuteWrite = 9;

    @Override
    protected String getLogTag() {
        return "DoCallbackGattServer";
    }

    public void onGattServiceReady(){
        Log.d(TAG, "onGattServiceReady()");
        Message m = Message.obtain(mHandler, onGattServiceReady);
        enqueueMessage(m);
    }
    
    public void onGattServerStateChanged(String address, int state){
        Log.d(TAG, "onGattServerStateChanged()");
        Message m = Message.obtain(mHandler, onGattServerStateChanged);
        Bundle b = new Bundle();
        m.obj = address;
        b.putInt("state", state);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onGattServerServiceAdded(int status, int srvcType,
            ParcelUuid srvcId) {
        Log.d(TAG, "onGattServerServiceAdded()");
        Message m = Message.obtain(mHandler, onGattServerServiceAdded);
        Bundle b = new Bundle();
        b.putInt("status", status);
        b.putParcelable("srvcId", srvcId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onGattServerServiceDeleted(int status, int srvcType,
            ParcelUuid srvcId) {
        Log.d(TAG, "onGattServerServiceDeleted()");
        Message m = Message.obtain(mHandler, onGattServerServiceDeleted);
        Bundle b = new Bundle();
        b.putInt("status", status);
        b.putParcelable("srvcId", srvcId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onGattServerCharacteristicReadRequest(String address, int transId,
            int offset, boolean isLong,
            int srvcType,
            ParcelUuid srvcId,
            ParcelUuid charId) {
        Log.d(TAG, "onGattServerCharacteristicReadRequest()");
        Message m = Message.obtain(mHandler, onGattServerCharacteristicReadRequest);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("transId", transId);
        b.putInt("offset", offset);
        b.putBoolean("isLong", isLong);
        b.putInt("srvcType", srvcType);
        b.putParcelable("srvcId", srvcId);
        b.putParcelable("charId", charId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onGattServerDescriptorReadRequest(String address, int transId,
            int offset, boolean isLong,
            int srvcType,
            ParcelUuid srvcId,
            ParcelUuid charId,
            ParcelUuid descrId) {
        Log.d(TAG, "onGattServerDescriptorReadRequest()");
        Message m = Message.obtain(mHandler, onGattServerDescriptorReadRequest);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("transId", transId);
        b.putInt("offset", offset);
        b.putBoolean("isLong", isLong);
        b.putInt("srvcType", srvcType);
        b.putParcelable("srvcId", srvcId);
        b.putParcelable("charId", charId);
        b.putParcelable("descrId", descrId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onGattServerCharacteristicWriteRequest(String address, int transId,
            int offset, 
            boolean isPrep,
            boolean needRsp,
            int srvcType,
            ParcelUuid srvcId,
            ParcelUuid charId,
            byte[] value) {
        Log.d(TAG, "onGattServerCharacteristicWriteRequest()");
        Message m = Message.obtain(mHandler, onGattServerCharacteristicWriteRequest);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("transId", transId);
        b.putInt("offset", offset);
        b.putBoolean("isPrep", isPrep);
        b.putBoolean("needRsp", needRsp);
        b.putInt("srvcType", srvcType);
        b.putParcelable("srvcId", srvcId);
        b.putParcelable("charId", charId);
        b.putByteArray("value", value);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onGattServerDescriptorWriteRequest(String address, int transId,
            int offset, 
            boolean isPrep,
            boolean needRsp,
            int srvcType,
            ParcelUuid srvcId,
            ParcelUuid charId,
            ParcelUuid descrId,
            byte[] value){
        Log.d(TAG, "onGattServerDescriptorWriteRequest()");
        Message m = Message.obtain(mHandler, onGattServerDescriptorWriteRequest);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("transId", transId);
        b.putInt("offset", offset);
        b.putBoolean("isPrep", isPrep);
        b.putBoolean("needRsp", needRsp);
        b.putInt("srvcType", srvcType);
        b.putParcelable("srvcId", srvcId);
        b.putParcelable("charId", charId);
        b.putParcelable("descrId", descrId);
        b.putByteArray("value", value);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public void onGattServerExecuteWrite(String address, int transId, boolean execWrite){
        Log.d(TAG, "onGattServerExecuteWrite()");
        Message m = Message.obtain(mHandler, onGattServerExecuteWrite);
        m.obj = address;
        Bundle b = new Bundle();
        b.putInt("transId", transId);
        b.putBoolean("execWrite", execWrite);
        m.setData(b);
        enqueueMessage(m);
    }


    @Override
    protected void dequeueMessage(Message msg) {
        Bundle b = msg.getData();
        String address = (String)msg.obj;
        switch (msg.what) {
            case onGattServiceReady:
                callbackOnGattServiceReady();
                break;
            case onGattServerStateChanged:
                callbackOnGattServerStateChanged(address, b.getInt("state"));
                break;
            case onGattServerServiceAdded:
                callbackOnGattServerServiceAdded(b.getInt("status"), b.getInt("srvcType"), (ParcelUuid)b.getParcelable("srvcId"));
                break;
            case onGattServerServiceDeleted:
                callbackOnGattServerServiceDeleted(b.getInt("status"), b.getInt("srvcType"), (ParcelUuid)b.getParcelable("srvcId"));
                break;
            case onGattServerCharacteristicReadRequest:
                callbackOnGattServerCharacteristicReadRequest(address, b.getInt("transId"), b.getInt("offset"), b.getBoolean("isLong"), b.getInt("srvcType"), (ParcelUuid)b.getParcelable("srvcId"), (ParcelUuid)b.getParcelable("charId"));
                break;
            case onGattServerDescriptorReadRequest:
                callbackOnGattServerDescriptorReadRequest(address, b.getInt("transId"), b.getInt("offset"), b.getBoolean("isLong"), b.getInt("srvcType"), (ParcelUuid)b.getParcelable("srvcId"), (ParcelUuid)b.getParcelable("charId"), (ParcelUuid)b.getParcelable("descrId"));
                break;
            case onGattServerCharacteristicWriteRequest:
                callbackOnGattServerCharacteristicWriteRequest(address, b.getInt("transId"), b.getInt("offset"), b.getBoolean("isPrep"), b.getBoolean("needRsp"), b.getInt("srvcType"), (ParcelUuid)b.getParcelable("srvcId"), (ParcelUuid)b.getParcelable("charId"), b.getByteArray("value"));
                break;
            case onGattServerDescriptorWriteRequest:
                callbackOnGattServerDescriptorWriteRequest(address, b.getInt("transId"), b.getInt("offset"), b.getBoolean("isPrep"), b.getBoolean("needRsp"), b.getInt("srvcType"), (ParcelUuid)b.getParcelable("srvcId"), (ParcelUuid)b.getParcelable("charId"), (ParcelUuid)b.getParcelable("descrId"), b.getByteArray("value"));
                break;
            case onGattServerExecuteWrite:
                callbackOnGattServerExecuteWrite(address, b.getInt("transId"), b.getBoolean("execWrite"));
                break;
                
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }
    }

    private synchronized void callbackOnGattServiceReady(){
        Log.v(TAG, "callbackOnGattServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServiceReady CallBack Finish.");
    }

    private synchronized void callbackOnGattServerStateChanged(String address, int state){
        Log.v(TAG, "callbackOnGattServerStateChanged()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServerStateChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServerStateChanged(address, state);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServerStateChanged CallBack Finish.");
    }

    private synchronized void callbackOnGattServerServiceAdded(int status, int srvcType, 
            ParcelUuid srvcId){
        Log.v(TAG, "callbackOnGattServerServiceAdded()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServerServiceAdded beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServerServiceAdded(status, srvcType, srvcId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServerServiceAdded CallBack Finish.");
    }
    
    private synchronized void callbackOnGattServerServiceDeleted(int status, int srvcType, ParcelUuid srvcId){
        Log.v(TAG, "callbackOnGattServerServiceDeleted()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServerServiceDeleted beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServerServiceDeleted(status, srvcType, srvcId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServerServiceDeleted CallBack Finish.");
    }

    private synchronized void callbackOnGattServerCharacteristicReadRequest(String address, int transId,
            int offset, boolean isLong,
            int srvcType,
            ParcelUuid srvcId,
            ParcelUuid charId){
        Log.v(TAG, "callbackOnGattServerCharacteristicReadRequest()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServerCharacteristicReadRequest beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServerCharacteristicReadRequest(address, transId, offset, isLong, srvcType, srvcId, charId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServerCharacteristicReadRequest CallBack Finish.");
    }

    private synchronized void callbackOnGattServerDescriptorReadRequest(String address, int transId,
            int offset, boolean isLong,
            int srvcType,
            ParcelUuid srvcId,
            ParcelUuid charId,
            ParcelUuid descrId){
        Log.v(TAG, "callbackOnGattServerDescriptorReadRequest()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServerDescriptorReadRequest beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServerDescriptorReadRequest(address, transId, offset, isLong, srvcType, srvcId, charId, descrId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServerDescriptorReadRequest CallBack Finish.");
    }

    private synchronized void callbackOnGattServerCharacteristicWriteRequest(String address, int transId,
            int offset, 
            boolean isPrep,
            boolean needRsp,
            int srvcType,
            ParcelUuid srvcId,
            ParcelUuid charId,
            byte[] value){
        Log.v(TAG, "callbackOnGattServerCharacteristicWriteRequest()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServerCharacteristicWriteRequest beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServerCharacteristicWriteRequest(address, transId, offset, isPrep, needRsp, srvcType, srvcId, charId, value);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServerCharacteristicWriteRequest CallBack Finish.");
    }

    private synchronized void callbackOnGattServerDescriptorWriteRequest(String address, int transId,
            int offset, 
            boolean isPrep,
            boolean needRsp,
            int srvcType,
            ParcelUuid srvcId,
            ParcelUuid charId,
            ParcelUuid descrId,
            byte[] value){
        Log.v(TAG, "callbackOnGattServerDescriptorWriteRequest()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServerDescriptorWriteRequest beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServerDescriptorWriteRequest(address, transId, offset, isPrep, needRsp, srvcType, srvcId, charId, descrId, value);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServerDescriptorWriteRequest CallBack Finish.");
    }

    private synchronized void callbackOnGattServerExecuteWrite(String address, int transId, boolean execWrite){
        Log.v(TAG, "callbackOnGattServerExecuteWrite()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onGattServerExecuteWrite beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onGattServerExecuteWrite(address, transId, execWrite);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onGattServerExecuteWrite CallBack Finish.");
    }
    
}
