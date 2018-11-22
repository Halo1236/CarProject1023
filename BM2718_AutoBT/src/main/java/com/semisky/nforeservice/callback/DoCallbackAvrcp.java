package com.semisky.nforeservice.callback;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.UiCallbackAvrcp;


public final class DoCallbackAvrcp extends DoCallback<UiCallbackAvrcp>{
    
    private final int onAvrcpServiceReady = 0;
    private final int onAvrcpStateChanged = 1;
    private final int retAvrcp13CapabilitiesSupportEvent = 2;
    private final int retAvrcp13PlayerSettingAttributesList = 3;
    private final int retAvrcp13PlayerSettingValuesList = 4;
    private final int retAvrcp13PlayerSettingCurrentValues = 5;
    private final int retAvrcp13SetPlayerSettingValueSuccess = 6;
    private final int retAvrcp13ElementAttributesPlaying = 7;
    private final int retAvrcp13PlayStatus = 8;
    private final int onAvrcp13EventPlaybackStatusChanged = 9;
    private final int onAvrcp13EventTrackChanged = 10;
    private final int onAvrcp13EventTrackReachedEnd = 11;
    private final int onAvrcp13EventTrackReachedStart = 12;
    private final int onAvrcp13EventPlaybackPosChanged = 13;
    private final int onAvrcp13EventBatteryStatusChanged = 14;
    private final int onAvrcp13EventSystemStatusChanged = 15;
    private final int onAvrcp13EventPlayerSettingChanged = 16;
    private final int onAvrcp14EventNowPlayingContentChanged = 19;
    private final int onAvrcp14EventAvailablePlayerChanged = 20;
    private final int onAvrcp14EventAddressedPlayerChanged = 21;
    private final int onAvrcp14EventUidsChanged = 22;
    private final int onAvrcp14EventVolumeChanged = 23;
    private final int retAvrcp14SetAddressedPlayerSuccess = 24;
    private final int retAvrcp14SetBrowsedPlayerSuccess = 25;
    private final int retAvrcp14FolderItems = 26;
    private final int retAvrcp14MediaItems = 27;
    private final int retAvrcp14ChangePathSuccess = 28;
    private final int retAvrcp14ItemAttributes = 29;
    private final int retAvrcp14PlaySelectedItemSuccess = 30;
    private final int retAvrcp14SearchResult = 31;
    private final int retAvrcp14AddToNowPlayingSuccess = 32;
    private final int retAvrcp14SetAbsoluteVolumeSuccess = 33;
    private final int onAvrcpErrorResponse = 34;
    private final int retAvrcpUpdateSongStatus = 35;
    private final int onAvrcp13RegisterEventWatcherSuccess = 36;
    private final int onAvrcp13RegisterEventWatcherFail = 37;
    
	@Override
    protected String getLogTag() {
        return "DoCallbackAvrcp";
    }

    public void onAvrcpServiceReady(){
        Log.v(TAG, "onAvrcpServiceReady()");
        Message m = Message.obtain(mHandler, onAvrcpServiceReady);
        enqueueMessage(m);
    }
	
    public synchronized void onAvrcpStateChanged(String address, int prevState, int newState){
        Log.v(TAG, "onAvrcpStateChanged() " + address + " state: " + prevState + " -> " + newState);
        Message m = getMessage(onAvrcpStateChanged);
        m.obj = address;
        m.arg1 = prevState;
        m.arg2 = newState;        
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp13CapabilitiesSupportEvent(byte[] eventIds) {
        Log.v(TAG, "retAvrcp13CapabilitiesSupportEvent() ");
        Message m = getMessage(retAvrcp13CapabilitiesSupportEvent);
        
        Bundle b = new Bundle();
        b.putByteArray("eventIds", eventIds);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp13PlayerSettingAttributesList(byte[] attributeIds) {
        Log.v(TAG, "retAvrcp13PlayerSettingAttributesList() ");
        Message m = getMessage(retAvrcp13PlayerSettingAttributesList);
        
        Bundle b = new Bundle();
        b.putByteArray("attributeIds", attributeIds);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp13PlayerSettingValuesList(byte attributeId, byte[] valueIds) {
        Log.v(TAG, "retAvrcp13PlayerSettingValuesList() ");
        Message m = getMessage(retAvrcp13PlayerSettingValuesList);
        
        Bundle b = new Bundle();
        b.putByte("attributeId", attributeId);
        b.putByteArray("valueIds", valueIds);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp13PlayerSettingCurrentValues(byte[] attributeIds, byte[] valueIds) {
        Log.v(TAG, "retAvrcp13PlayerSettingCurrentValues() ");
        Message m = getMessage(retAvrcp13PlayerSettingCurrentValues);
        
        Bundle b = new Bundle();
        b.putByteArray("attributeIds", attributeIds);
        b.putByteArray("valueIds", valueIds);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp13SetPlayerSettingValueSuccess() {
        Log.v(TAG, "retAvrcp13SetPlayerSettingValueSuccess() ");
        Message m = getMessage(retAvrcp13SetPlayerSettingValueSuccess);
        
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp13ElementAttributesPlaying(int[] metadataAtrributeIds, String[] texts) {
        Log.v(TAG, "retAvrcp13ElementAttributesPlaying() ");
        Message m = getMessage(retAvrcp13ElementAttributesPlaying);
        
        Bundle b = new Bundle();
        b.putIntArray("metadataAtrributeIds", metadataAtrributeIds);
        b.putStringArray("texts", texts);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId) {
        Log.v(TAG, "retAvrcp13PlayStatus() ");
        Message m = getMessage(retAvrcp13PlayStatus);
        
        Bundle b = new Bundle();
        b.putLong("songLen", songLen);
        b.putLong("songPos", songPos);
        b.putByte("statusId", statusId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onAvrcp13EventPlaybackStatusChanged(byte statusId) {
        Log.v(TAG, "onAvrcp13EventPlaybackStatusChanged() ");
        Message m = getMessage(onAvrcp13EventPlaybackStatusChanged);
        Bundle b = new Bundle();
        b.putByte("statusId", statusId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onAvrcp13EventTrackChanged(long elementId) {
        Log.v(TAG, "onAvrcp13EventTrackChanged() ");
        Message m = getMessage(onAvrcp13EventTrackChanged);
        
        Bundle b = new Bundle();
        b.putLong("elementId", elementId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onAvrcp13EventTrackReachedEnd() {
        Log.v(TAG, "onAvrcp13EventTrackReachedEnd() ");
        Message m = getMessage(onAvrcp13EventTrackReachedEnd);
        
        enqueueMessage(m);
    }
    
    public synchronized void onAvrcp13EventTrackReachedStart() {
        Log.v(TAG, "onAvrcp13EventTrackReachedStart() ");
        Message m = getMessage(onAvrcp13EventTrackReachedStart);
        
        enqueueMessage(m);
    }

    public synchronized void onAvrcp13EventPlaybackPosChanged(long songPos) {
        Log.v(TAG, "onAvrcp13EventPlaybackPosChanged() ");
        Message m = getMessage(onAvrcp13EventPlaybackPosChanged);
        
        Bundle b = new Bundle();
        b.putLong("songPos", songPos);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onAvrcp13EventBatteryStatusChanged(byte statusId) {
        Log.v(TAG, "onAvrcp13EventBatteryStatusChanged() ");
        Message m = getMessage(onAvrcp13EventBatteryStatusChanged);
        
        Bundle b = new Bundle();
        b.putByte("statusId", statusId);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onAvrcp13EventSystemStatusChanged(long statusId) {
        Log.v(TAG, "onAvrcp13EventSystemStatusChanged() ");
        Message m = getMessage(onAvrcp13EventSystemStatusChanged);
        
        Bundle b = new Bundle();
        b.putLong("statusId", statusId);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onAvrcp13EventPlayerSettingChanged(byte[] attributeIds, byte[] valueIds) {
        Log.v(TAG, "onAvrcp13EventPlayerSettingChanged() ");
        Message m = getMessage(onAvrcp13EventPlayerSettingChanged);
        
        Bundle b = new Bundle();
        b.putByteArray("attributeIds", attributeIds);
        b.putByteArray("valueIds", valueIds);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onAvrcp14EventNowPlayingContentChanged() {
        Log.v(TAG, "onAvrcp14EventNowPlayingContentChanged() ");
        Message m = getMessage(onAvrcp14EventNowPlayingContentChanged);
        
        enqueueMessage(m);
    }

    public synchronized void onAvrcp14EventAvailablePlayerChanged() {
        Log.v(TAG, "onAvrcp14EventAvailablePlayerChanged() ");
        Message m = getMessage(onAvrcp14EventAvailablePlayerChanged);
        
        enqueueMessage(m);
    }

    public synchronized void onAvrcp14EventAddressedPlayerChanged(int playerId, int uidCounter) {
        Log.v(TAG, "onAvrcp14EventAddressedPlayerChanged() ");
        Message m = getMessage(onAvrcp14EventAddressedPlayerChanged);
        
        Bundle b = new Bundle();
        b.putInt("playerId", playerId);
        b.putInt("uidCounter", uidCounter);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onAvrcp14EventUidsChanged(int uidCounter) {
        Log.v(TAG, "onAvrcp14EventUidsChanged() ");
        Message m = getMessage(onAvrcp14EventUidsChanged);
        
        Bundle b = new Bundle();
        b.putInt("uidCounter", uidCounter);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void onAvrcp14EventVolumeChanged(byte volume) {
        Log.v(TAG, "onAvrcp14EventVolumeChanged() ");
        Message m = getMessage(onAvrcp14EventVolumeChanged);
        
        Bundle b = new Bundle();
        b.putByte("volume", volume);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void retAvrcp14SetAddressedPlayerSuccess() {
        Log.v(TAG, "retAvrcp14SetAddressedPlayerSuccess() ");
        Message m = getMessage(retAvrcp14SetAddressedPlayerSuccess);
        
        enqueueMessage(m);
    }

    public synchronized void retAvrcp14SetBrowsedPlayerSuccess(String[] path, int uidCounter, long itemCount) {
        Log.v(TAG, "retAvrcp14SetBrowsedPlayerSuccess() ");
        Message m = getMessage(retAvrcp14SetBrowsedPlayerSuccess);
        
        Bundle b = new Bundle();
        b.putStringArray("path", path);
        b.putInt("uidCounter", uidCounter);
        b.putLong("itemCount", itemCount);
        m.setData(b);
        enqueueMessage(m);
    }

    public synchronized void retAvrcp14FolderItems(int uidCounter, long itemCount) {
        Log.v(TAG, "retAvrcp14FolderItems() ");
        Message m = getMessage(retAvrcp14FolderItems);
        
        Bundle b = new Bundle();
        b.putInt("uidCounter", uidCounter);
        b.putLong("itemCount", itemCount);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp14MediaItems(int uidCounter, long itemCount) {
        Log.v(TAG, "retAvrcp14MediaItems() ");
        Message m = getMessage(retAvrcp14MediaItems);
        
        Bundle b = new Bundle();
        b.putInt("uidCounter", uidCounter);
        b.putLong("itemCount", itemCount);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp14ChangePathSuccess(long itemCount) {
        Log.v(TAG, "retAvrcp14ChangePathSuccess() ");
        Message m = getMessage(retAvrcp14ChangePathSuccess);
        
        Bundle b = new Bundle();
        b.putLong("itemCount", itemCount);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp14ItemAttributes(int[] metadataAtrributeIds, String[] texts) {
        Log.v(TAG, "retAvrcp14ItemAttributes() ");
        Message m = getMessage(retAvrcp14ItemAttributes);
        
        Bundle b = new Bundle();
        b.putIntArray("metadataAtrributeIds", metadataAtrributeIds);
        b.putStringArray("texts", texts);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp14PlaySelectedItemSuccess() {
        Log.v(TAG, "retAvrcp14PlaySelectedItemSuccess() ");
        Message m = getMessage(retAvrcp14PlaySelectedItemSuccess);
        
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp14SearchResult(int uidCounter, long itemCount) {
        Log.v(TAG, "retAvrcp14SearchResult() ");
        Message m = getMessage(retAvrcp14SearchResult);
        
        Bundle b = new Bundle();
        b.putInt("uidCounter", uidCounter);
        b.putLong("itemCount", itemCount);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp14AddToNowPlayingSuccess() {
        Log.v(TAG, "retAvrcp14AddToNowPlayingSuccess() ");
        Message m = getMessage(retAvrcp14AddToNowPlayingSuccess);
        
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcp14SetAbsoluteVolumeSuccess(byte volume) {
        Log.v(TAG, "retAvrcp14SetAbsoluteVolumeSuccess() ");
        Message m = getMessage(retAvrcp14SetAbsoluteVolumeSuccess);
        
        Bundle b = new Bundle();
        b.putByte("volume", volume);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onAvrcpErrorResponse(int opId, int reason, byte eventId) {
        Log.v(TAG, "onAvrcpErrorResponse() ");
        Message m = getMessage(onAvrcpErrorResponse);
        
        Bundle b = new Bundle();
        b.putInt("opId", opId);
        b.putInt("reason", reason);
        b.putByte("eventId", eventId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void retAvrcpUpdateSongStatus(String artist, String album, String title) {
        Log.v(TAG, "retAvrcpUpdateSongStatus() ");
        Message m = getMessage(retAvrcpUpdateSongStatus);
        
        Bundle b = new Bundle();
        b.putString("artist", artist);
        b.putString("album", album);
        b.putString("title", title);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onAvrcp13RegisterEventWatcherSuccess(byte eventId) {
        Log.v(TAG, "onAvrcp13RegisterEventWatcherSuccess() ");
        Message m = getMessage(onAvrcp13RegisterEventWatcherSuccess);
        
        Bundle b = new Bundle();
        b.putByte("eventId", eventId);
        m.setData(b);
        enqueueMessage(m);
    }
    
    public synchronized void onAvrcp13RegisterEventWatcherFail(byte eventId) {
        Log.v(TAG, "onAvrcp13RegisterEventWatcherFail() ");
        Message m = getMessage(onAvrcp13RegisterEventWatcherFail);
        
        Bundle b = new Bundle();
        b.putByte("eventId", eventId);
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
            case onAvrcpServiceReady:
                callbackOnAvrcpServiceReady();
                break;      
            case onAvrcpStateChanged:
                callbackOnAvrcpStateChanged(address, prevState, newState);
                break;
            case retAvrcp13CapabilitiesSupportEvent:
                callbackRetAvrcp13CapabilitiesSupportEvent(b.getByteArray("eventIds"));
                break;
            case retAvrcp13PlayerSettingAttributesList:
                callbackRetAvrcp13PlayerSettingAttributesList(b.getByteArray("attributeIds"));
                break;
            case retAvrcp13PlayerSettingValuesList:
                callbackRetAvrcp13PlayerSettingValuesList(b.getByte("attributeId"), b.getByteArray("valueIds"));
                break;
            case retAvrcp13PlayerSettingCurrentValues:
                callbackRetAvrcp13PlayerSettingCurrentValues(b.getByteArray("attributeIds"), b.getByteArray("valueIds"));
                break;
            case retAvrcp13SetPlayerSettingValueSuccess:
                callbackRetAvrcp13SetPlayerSettingValueSuccess();
                break;
            case retAvrcp13ElementAttributesPlaying:
                callbackRetAvrcp13ElementAttributesPlaying(b.getIntArray("metadataAtrributeIds"), b.getStringArray("texts"));
                break;
            case retAvrcp13PlayStatus:
                callbackRetAvrcp13PlayStatus(b.getLong("songLen"), b.getLong("songPos"), b.getByte("statusId"));
                break;
            case onAvrcp13EventPlaybackStatusChanged:
                callbackOnAvrcp13EventPlaybackStatusChanged(b.getByte("statusId"));
                break;
            case onAvrcp13EventTrackChanged:
                callbackOnAvrcp13EventTrackChanged(b.getLong("elementId"));
                break;
            case onAvrcp13EventTrackReachedEnd:
                callbackOnAvrcp13EventTrackReachedEnd();
                break;
            case onAvrcp13EventTrackReachedStart:
                callbackOnAvrcp13EventTrackReachedStart();
                break;
            case onAvrcp13EventPlaybackPosChanged:
                callbackOnAvrcp13EventPlaybackPosChanged(b.getLong("songPos"));
                break;
            case onAvrcp13EventBatteryStatusChanged:
                callbackOnAvrcp13EventBatteryStatusChanged(b.getByte("statusId"));
                break;
            case onAvrcp13EventSystemStatusChanged:
                callbackOnAvrcp13EventSystemStatusChanged(b.getByte("statusId"));
                break;
            case onAvrcp13EventPlayerSettingChanged:
                callbackOnAvrcp13EventPlayerSettingChanged(b.getByteArray("attributeIds"), b.getByteArray("valueIds"));
                break;
            case onAvrcp14EventNowPlayingContentChanged:
                callbackOnAvrcp14EventNowPlayingContentChanged();
                break;
            case onAvrcp14EventAvailablePlayerChanged:
                callbackOnAvrcp14EventAvailablePlayerChanged();
                break;
            case onAvrcp14EventAddressedPlayerChanged:
                callbackOnAvrcp14EventAddressedPlayerChanged(b.getInt("playerId"), b.getInt("uidCounter"));
                break;
            case onAvrcp14EventUidsChanged:
                callbackOnAvrcp14EventUidsChanged(b.getInt("uidCounter"));
                break;
            case onAvrcp14EventVolumeChanged:
                callbackOnAvrcp14EventVolumeChanged(b.getByte("volume"));
                break;
            case retAvrcp14SetAddressedPlayerSuccess:
                callbackRetAvrcp14SetAddressedPlayerSuccess();
                break;
            case retAvrcp14SetBrowsedPlayerSuccess:
                callbackRetAvrcp14SetBrowsedPlayerSuccess(b.getStringArray("path"), b.getInt("uidCounter"), b.getLong("itemCount"));
                break;
            case retAvrcp14FolderItems:
                callbackRetAvrcp14FolderItems(b.getInt("uidCounter"), b.getLong("itemCount"));
                break;
            case retAvrcp14MediaItems:
                callbackRetAvrcp14MediaItems(b.getInt("uidCounter"), b.getLong("itemCount"));
                break;
            case retAvrcp14ChangePathSuccess:
                callbackRetAvrcp14ChangePathSuccess(b.getLong("itemCount"));
                break;
            case retAvrcp14ItemAttributes:
                callbackRetAvrcp14ItemAttributes(b.getIntArray("metadataAtrributeIds"), b.getStringArray("texts"));
                break;
            case retAvrcp14PlaySelectedItemSuccess:
                callbackRetAvrcp14PlaySelectedItemSuccess();
                break;
            case retAvrcp14SearchResult:
                callbackRetAvrcp14SearchResult(b.getInt("uidCounter"), b.getLong("itemCount"));
                break;
            case retAvrcp14AddToNowPlayingSuccess:
                callbackRetAvrcp14AddToNowPlayingSuccess();
                break;
            case retAvrcp14SetAbsoluteVolumeSuccess:
                callbackRetAvrcp14SetAbsoluteVolumeSuccess(b.getByte("volume"));
                break;
            case onAvrcpErrorResponse:
                callbackOnAvrcpErrorResponse(b.getInt("opId"), b.getInt("reason"), b.getByte("eventId"));
                break;
            case retAvrcpUpdateSongStatus:
                callbackRetAvrcpUpdateSongStatus(b.getString("artist"), b.getString("album"), b.getString("title"));
                break;                

            case onAvrcp13RegisterEventWatcherSuccess:
                callbackOnAvrcp13RegisterEventWatcherSuccess(b.getByte("eventId"));
                break;
            case onAvrcp13RegisterEventWatcherFail:
                callbackOnAvrcp13RegisterEventWatcherFail(b.getByte("eventId"));
                break;
            default:
                Log.e(TAG, "unhandle Message : " + msg.what);
                break;
        }
    }

    private synchronized void callbackOnAvrcpServiceReady(){
        Log.v(TAG, "callbackOnAvrcpServiceReady()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcpServiceReady beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcpServiceReady();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcpServiceReady CallBack Finish.");
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
    
    private synchronized void callbackRetAvrcp13CapabilitiesSupportEvent(byte[] eventIds){
        Log.d(TAG, "callbackRetAvrcp13CapabilitiesSupportEvent() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp13CapabilitiesSupportEvent beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp13CapabilitiesSupportEvent(eventIds);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp13CapabilitiesSupportEvent CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp13PlayerSettingAttributesList(byte[] attributeIds){
        Log.d(TAG, "callbackRetAvrcp13PlayerSettingAttributesList() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp13PlayerSettingAttributesList beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp13PlayerSettingAttributesList(attributeIds);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp13PlayerSettingAttributesList CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp13PlayerSettingValuesList(byte attributeId, byte[] valueIds){
        Log.d(TAG, "callbackRetAvrcp13PlayerSettingValuesList() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp13PlayerSettingValuesList beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp13PlayerSettingValuesList(attributeId, valueIds);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp13PlayerSettingValuesList CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp13PlayerSettingCurrentValues(byte[] attributeIds, byte[] valueIds){
        Log.d(TAG, "callbackRetAvrcp13PlayerSettingCurrentValues() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp13PlayerSettingCurrentValues beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp13PlayerSettingCurrentValues(attributeIds, valueIds);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp13PlayerSettingCurrentValues CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp13SetPlayerSettingValueSuccess(){
        Log.d(TAG, "callbackRetAvrcp13SetPlayerSettingValueSuccess() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp13SetPlayerSettingValueSuccess beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp13SetPlayerSettingValueSuccess();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp13SetPlayerSettingValueSuccess CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp13ElementAttributesPlaying(int[] metadataAtrributeIds, String[] texts){
        Log.d(TAG, "callbackRetAvrcp13ElementAttributesPlaying() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp13ElementAttributesPlaying beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp13ElementAttributesPlaying(metadataAtrributeIds, texts);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp13ElementAttributesPlaying CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp13PlayStatus(long songLen, long songPos, byte statusId){
        Log.d(TAG, "callbackRetAvrcp13PlayStatus() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp13PlayStatus beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp13PlayStatus(songLen, songPos, statusId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp13PlayStatus CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13EventPlaybackStatusChanged(byte statusId){
        Log.d(TAG, "callbackOnAvrcp13EventPlaybackStatusChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13EventPlaybackStatusChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13EventPlaybackStatusChanged(statusId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13EventPlaybackStatusChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13EventTrackChanged(long elementId){
        Log.d(TAG, "callbackOnAvrcp13EventTrackChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13EventTrackChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13EventTrackChanged(elementId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13EventTrackChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13EventTrackReachedEnd(){
        Log.d(TAG, "callbackOnAvrcp13EventTrackReachedEnd() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13EventTrackReachedEnd beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13EventTrackReachedEnd();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13EventTrackReachedEnd CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13EventTrackReachedStart(){
        Log.d(TAG, "callbackOnAvrcp13EventTrackReachedStart() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13EventTrackReachedStart beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13EventTrackReachedStart();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13EventTrackReachedStart CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13EventPlaybackPosChanged(long songPos){
        Log.d(TAG, "callbackOnAvrcp13EventPlaybackPosChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13EventPlaybackPosChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13EventPlaybackPosChanged(songPos);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13EventPlaybackPosChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13EventBatteryStatusChanged(byte statusId){
        Log.d(TAG, "callbackOnAvrcp13EventBatteryStatusChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13EventBatteryStatusChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13EventBatteryStatusChanged(statusId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13EventBatteryStatusChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13EventSystemStatusChanged(byte statusId){
        Log.d(TAG, "callbackOnAvrcp13EventSystemStatusChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13EventSystemStatusChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13EventSystemStatusChanged(statusId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13EventSystemStatusChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13EventPlayerSettingChanged(byte[] attributeIds, byte[] valueIds){
        Log.d(TAG, "callbackOnAvrcp13EventPlayerSettingChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13EventPlayerSettingChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13EventPlayerSettingChanged(attributeIds, valueIds);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13EventPlayerSettingChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp14EventNowPlayingContentChanged(){
        Log.d(TAG, "callbackOnAvrcp14EventNowPlayingContentChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp14EventNowPlayingContentChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp14EventNowPlayingContentChanged();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp14EventNowPlayingContentChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp14EventAvailablePlayerChanged(){
        Log.d(TAG, "callbackOnAvrcp14EventAvailablePlayerChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp14EventAvailablePlayerChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp14EventAvailablePlayerChanged();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp14EventAvailablePlayerChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp14EventAddressedPlayerChanged(int playerId, int uidCounter){
        Log.d(TAG, "callbackOnAvrcp14EventAddressedPlayerChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp14EventAddressedPlayerChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp14EventAddressedPlayerChanged(playerId, uidCounter);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp14EventAddressedPlayerChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp14EventUidsChanged(int uidCounter){
        Log.d(TAG, "callbackOnAvrcp14EventUidsChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp14EventUidsChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp14EventUidsChanged(uidCounter);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp14EventUidsChanged CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp14EventVolumeChanged(byte volume){
        Log.d(TAG, "callbackOnAvrcp14EventVolumeChanged() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp14EventVolumeChanged beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp14EventVolumeChanged(volume);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp14EventVolumeChanged CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14SetAddressedPlayerSuccess(){
        Log.d(TAG, "retAvrcp14SetAddressedPlayerSuccess() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14SetAddressedPlayerSuccess beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14SetAddressedPlayerSuccess();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14SetAddressedPlayerSuccess CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14SetBrowsedPlayerSuccess(String[] path, int uidCounter, long itemCount){
        Log.d(TAG, "callbackRetAvrcp14SetBrowsedPlayerSuccess() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14SetBrowsedPlayerSuccess beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14SetBrowsedPlayerSuccess(path, uidCounter, itemCount);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14SetBrowsedPlayerSuccess CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14FolderItems(int uidCounter, long itemCount){
        Log.d(TAG, "callbackRetAvrcp14FolderItems() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14FolderItems beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14FolderItems(uidCounter, itemCount);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14FolderItems CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14MediaItems(int uidCounter, long itemCount){
        Log.d(TAG, "callbackRetAvrcp14MediaItems() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14MediaItems beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14MediaItems(uidCounter, itemCount);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14MediaItems CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14ChangePathSuccess(long itemCount){
        Log.d(TAG, "callbackRetAvrcp14ChangePathSuccess() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14ChangePathSuccess beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14ChangePathSuccess(itemCount);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14ChangePathSuccess CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14ItemAttributes(int[] metadataAtrributeIds, String[] texts){
        Log.d(TAG, "callbackRetAvrcp14ItemAttributes() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14ItemAttributes beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14ItemAttributes(metadataAtrributeIds, texts);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14ItemAttributes CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14PlaySelectedItemSuccess(){
        Log.d(TAG, "callbackRetAvrcp14PlaySelectedItemSuccess() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14PlaySelectedItemSuccess beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14PlaySelectedItemSuccess();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14PlaySelectedItemSuccess CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14SearchResult(int uidCounter, long itemCount){
        Log.d(TAG, "callbackRetAvrcp14SearchResult() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14SearchResult beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14SearchResult(uidCounter, itemCount);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14SearchResult CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14AddToNowPlayingSuccess(){
        Log.d(TAG, "callbackRetAvrcp14AddToNowPlayingSuccess() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14AddToNowPlayingSuccess beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14AddToNowPlayingSuccess();
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14AddToNowPlayingSuccess CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcp14SetAbsoluteVolumeSuccess(byte volume){
        Log.d(TAG, "callbackRetAvrcp14SetAbsoluteVolumeSuccess() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcp14SetAbsoluteVolumeSuccess beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcp14SetAbsoluteVolumeSuccess(volume);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcp14SetAbsoluteVolumeSuccess CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcpErrorResponse(int opId, int reason, byte eventId){
        Log.d(TAG, "callbackOnAvrcpErrorResponse() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcpErrorResponse beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcpErrorResponse(opId, reason, eventId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcpErrorResponse CallBack Finish.");
    }
    
    private synchronized void callbackRetAvrcpUpdateSongStatus(String artist, String album, String title){
        Log.d(TAG, "callbackRetAvrcpUpdateSongStatus() ");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "retAvrcpUpdateSongStatus beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).retAvrcpUpdateSongStatus(artist, album, title);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"retAvrcpUpdateSongStatus CallBack Finish.");
    }
    

    private synchronized void callbackOnAvrcp13RegisterEventWatcherSuccess(byte eventId){
        Log.v(TAG, "callbackOnAvrcp13RegisterEventWatcherSuccess()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13RegisterEventWatcherSuccess beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13RegisterEventWatcherSuccess(eventId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13RegisterEventWatcherSuccess CallBack Finish.");
    }
    
    private synchronized void callbackOnAvrcp13RegisterEventWatcherFail(byte eventId){
        Log.v(TAG, "callbackOnAvrcp13RegisterEventWatcherFail()");
        synchronized(mRemoteCallbacks){
            Log.v(TAG, "onAvrcp13RegisterEventWatcherFail beginBroadcast()");
            final int n = mRemoteCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mRemoteCallbacks.getBroadcastItem(i).onAvrcp13RegisterEventWatcherFail(eventId);
                } catch (RemoteException e) {
                    Log.e(TAG, "Callback count: " + n + " Current index = " + i);
                } catch (NullPointerException e1) {
                    checkCallbacksValid(i);
                }
            }
            mRemoteCallbacks.finishBroadcast();         
        }
        Log.v(TAG,"onAvrcp13RegisterEventWatcherFail CallBack Finish.");
    }
}
