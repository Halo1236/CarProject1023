package com.smk.bt.service.callbacks;

import com.nforetek.bt.aidl.UiCallbackA2dp;

import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public final class DoCallbackA2dp extends DoCallback<UiCallbackA2dp>{
    
    private final int onA2dpServiceReady = 0;    
    private final int onA2dpStateChanged = 1;
    	
    @Override
    protected String getLogTag() {
        return "DoCallbackA2dp";
    }

    public void onA2dpServiceReady(){
        Log.v(TAG, "onA2dpServiceReady()");
        Message m = Message.obtain(mHandler, onA2dpServiceReady);
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

	@Override
	protected void dequeueMessage(Message msg) {
	    String address = (String)msg.obj;
	    switch (msg.what) {
	    	case onA2dpServiceReady:
                callbackOnA2dpServiceReady();
                break;
            case onA2dpStateChanged:
                callbackOnA2dpStateChanged(address, msg.arg1, msg.arg2);
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }	    
	}

	private synchronized void callbackOnA2dpServiceReady(){
        Log.v(TAG, "callbackOnA2dpServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onA2dpServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onA2dpServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onA2dpServiceReady CallBack Finish.");
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
}
