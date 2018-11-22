package com.semisky.nforeservice.callback;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.UiCallbackHid;

public final class DoCallbackHid extends DoCallback<UiCallbackHid>{

    private final int onHidServiceReady = 0;
    private final int onHidStateChanged = 1;
    	
    @Override
    protected String getLogTag() {
        return "DoCallbackHid";
    }

    public void onHidServiceReady(){
        Log.v(TAG, "onHidServiceReady()");
        Message m = Message.obtain(mHandler, onHidServiceReady);
        enqueueMessage(m);
    }

	public synchronized void onHidStateChanged(String address, int prevState, int newState, int reason){
        Log.v(TAG, "onHidStateChanged() : " + prevState + " -> " + newState + " ,reason:" + reason);
        Message m = getMessage(onHidStateChanged);
        Bundle b = new Bundle();
        b.putString("address", address);
        b.putInt("prevState", prevState);
        b.putInt("newState", newState);
        b.putInt("reason", reason);
        m.setData(b);
        enqueueMessage(m);
    }

	@Override
	protected void dequeueMessage(Message msg) {
        Bundle b = msg.getData();
	    String address = (String)msg.obj;
	    switch (msg.what) {
            case onHidServiceReady:
                callbackOnHidServiceReady();
                break;
            case onHidStateChanged:
                callbackOnHidStateChanged(b.getString("address"), b.getInt("prevState"), b.getInt("newState"), b.getInt("reason"));
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }	    
	}

    private synchronized void callbackOnHidServiceReady(){
        Log.v(TAG, "callbackOnHidServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onHidServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onHidServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onHidServiceReady CallBack Finish.");
    }
	
	private synchronized void callbackOnHidStateChanged(String address, int prevState, int newState, int reason){
		Log.v(TAG, "callbackOnHidStateChanged() : " + prevState + " -> " + newState + " ,reason: " + reason);
		synchronized(mRemoteCallbacks){
			Log.v(TAG, "onHidStateChanged beginBroadcast()");
			final int n = mRemoteCallbacks.beginBroadcast();
			for (int i = 0; i < n; i++) {
				try {
				    mRemoteCallbacks.getBroadcastItem(i).onHidStateChanged(address, prevState, newState, reason);
				} catch (RemoteException e) {
					Log.e(TAG, "Callback count: " + n + " Current index = " + i);
				} catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
			}
			mRemoteCallbacks.finishBroadcast();			
		}
		Log.v(TAG,"onHidStateChanged CallBack Finish.");
	}
}
