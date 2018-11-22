package com.semisky.nforeservice.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.INfCallbackA2dp;
import com.nforetek.bt.aidl.INfCallbackAvrcp;
import com.nforetek.bt.aidl.INfCallbackBluetooth;
import com.nforetek.bt.aidl.INfCallbackHfp;
import com.nforetek.bt.aidl.INfCallbackPbap;
import com.nforetek.bt.aidl.INfCommandA2dp;
import com.nforetek.bt.aidl.INfCommandAvrcp;
import com.nforetek.bt.aidl.INfCommandBluetooth;
import com.nforetek.bt.aidl.INfCommandHfp;
import com.nforetek.bt.aidl.INfCommandPbap;
import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.aidl.NfPbapContact;
import com.nforetek.bt.aidl.UiCallbackA2dp;
import com.nforetek.bt.aidl.UiCallbackAvrcp;
import com.nforetek.bt.aidl.UiCallbackBluetooth;
import com.nforetek.bt.aidl.UiCallbackGattServer;
import com.nforetek.bt.aidl.UiCallbackHfp;
import com.nforetek.bt.aidl.UiCallbackHid;
import com.nforetek.bt.aidl.UiCallbackMap;
import com.nforetek.bt.aidl.UiCallbackOpp;
import com.nforetek.bt.aidl.UiCallbackPbap;
import com.nforetek.bt.aidl.UiCallbackSpp;
import com.nforetek.bt.aidl.UiCommand;
import com.nforetek.bt.res.NfDef;
import com.semisky.bluetoothproject.model.BtServiceConnectModel;
import com.semisky.bluetoothproject.utils.Logger;
import com.semisky.nforeservice.callback.DoCallbackA2dp;
import com.semisky.nforeservice.callback.DoCallbackAvrcp;
import com.semisky.nforeservice.callback.DoCallbackBluetooth;
import com.semisky.nforeservice.callback.DoCallbackHfp;
import com.semisky.nforeservice.callback.DoCallbackPbap;

import java.lang.reflect.Method;
import java.util.List;

//import android.content.Context;
//import android.content.SharedPreferences;

public class BtBaseService extends Service {
    private String TAG = Logger.makeTagLog(BtBaseService.class);
    ;

    private String mVersionName = "";

    private BtServiceConnectModel btServiceConnectModel;

    private INfCommandHfp mCommandHfp;
    private INfCommandA2dp mCommandA2dp;
    private INfCommandAvrcp mCommandAvrcp;
    private INfCommandPbap mCommandPbap;
    //    private INfCommandHid mCommandHid;
    private INfCommandBluetooth mCommandBluetooth;
//    private INfCommandSpp mCommandSpp;
//    private INfCommandMap mCommandMap;
//    private INfCommandOpp mCommandOpp;
//    private INfCommandGattServer mCommandGattServer;

    private DoCallbackA2dp mDoCallbackA2dp;
    private DoCallbackAvrcp mDoCallbackAvrcp;
    private DoCallbackHfp mDoCallbackHfp;
    private DoCallbackPbap mDoCallbackPbap;
    //    private DoCallbackHid mDoCallbackHid;
    private DoCallbackBluetooth mDoCallbackBluetooth;
//    private DoCallbackSpp mDoCallbackSpp;
//    private DoCallbackMap mDoCallbackMap;
//    private DoCallbackOpp mDoCallbackOpp;
//    private DoCallbackGattServer mDoCallbackGattServer;

    //    private int mHfpNewState = 0;
    //    private int mHfpPrevState = 0;
    //    private long mHfpStateChangeCurrentTimeMillis = 0;
    //
    //    private String mTargetAddress = NfDef.DEFAULT_ADDRESS;

    private int testCount = 0;
    //private String mAutoConnectAddress = NfDef.DEFAULT_ADDRESS;

    @Override
    public void onCreate() {
        Log.i(TAG, " +++ onCreate +++ ");

        btServiceConnectModel = BtServiceConnectModel.getInstance();

        mDoCallbackA2dp = new DoCallbackA2dp();
        mDoCallbackAvrcp = new DoCallbackAvrcp();
        mDoCallbackHfp = new DoCallbackHfp();
        mDoCallbackPbap = new DoCallbackPbap();

//        mDoCallbackHid = new DoCallbackHid();
        mDoCallbackBluetooth = new DoCallbackBluetooth();
//        mDoCallbackSpp = new DoCallbackSpp();
//        mDoCallbackMap = new DoCallbackMap();
//        mDoCallbackOpp = new DoCallbackOpp();
//        mDoCallbackGattServer = new DoCallbackGattServer();


        Log.v(TAG, "bindA2dpService");
        bindService(new Intent(NfDef.CLASS_SERVICE_A2DP), this.mConnection, BIND_AUTO_CREATE);
        Log.v(TAG, "bindAvrcpService");
        bindService(new Intent(NfDef.CLASS_SERVICE_AVRCP), this.mConnection, BIND_AUTO_CREATE);
        Log.v(TAG, "bindHfpService");
        bindService(new Intent(NfDef.CLASS_SERVICE_HFP), this.mConnection, BIND_AUTO_CREATE);
        Log.v(TAG, "bindPbapService");
        bindService(new Intent(NfDef.CLASS_SERVICE_PBAP), this.mConnection, BIND_AUTO_CREATE);
//        Log.v(TAG, "bindHidService");
//        bindService(new Intent(NfDef.CLASS_SERVICE_HID), this.mConnection, BIND_AUTO_CREATE);
        Log.v(TAG, "bindBluetoothService");
        bindService(new Intent(NfDef.CLASS_SERVICE_BLUETOOTH), this.mConnection, BIND_AUTO_CREATE);
//        Log.v(TAG,"bindSppService");
//        bindService(new Intent(NfDef.CLASS_SERVICE_SPP), this.mConnection, BIND_AUTO_CREATE);
//        Log.v(TAG,"bindMapService");
//        bindService(new Intent(NfDef.CLASS_SERVICE_MAP), this.mConnection, BIND_AUTO_CREATE);
//        Log.v(TAG,"bindOppService");
//        bindService(new Intent(NfDef.CLASS_SERVICE_OPP), this.mConnection, BIND_AUTO_CREATE);
//        Log.v(TAG,"bindGattService");
//        bindService(new Intent(NfDef.CLASS_SERVICE_GATT_SERVER), this.mConnection, BIND_AUTO_CREATE);


        PackageInfo mPackageInfo = null;

        try {
            mPackageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Exception when getting package information !!!");
        }

        if (mPackageInfo != null) {
            mVersionName = mPackageInfo.versionName;
            TAG = "NfDemo_BtService_" + mVersionName;
        } else {
            Log.e(TAG, "In onCreate() : mPackageInfo is null");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand Received start id " + startId + " : " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            if (mCommandHfp != null) {
                mCommandHfp.unregisterHfpCallback(mCallbackHfp);
            }
            if (mCommandA2dp != null) {
                mCommandA2dp.unregisterA2dpCallback(mCallbackA2dp);
            }
            if (mCommandAvrcp != null) {
                mCommandAvrcp.unregisterAvrcpCallback(mCallbackAvrcp);
            }
            if (mCommandPbap != null) {
                mCommandPbap.unregisterPbapCallback(mCallbackPbap);
            }
//            if (mCommandHid != null) {
//                mCommandHid.unregisterHidCallback(mCallbackHid);
//            }
            if (mCommandBluetooth != null) {
                mCommandBluetooth.unregisterBtCallback(mCallbackBluetooth);
            }

//            if (mCommandSpp!= null) {
//                mCommandSpp.unregisterSppCallback(mCallbackSpp);
//            }
//
//            if (mCommandMap!= null) {
//                mCommandMap.unregisterMapCallback(mCallbackMap);
//            }
//
//            if (mCommandOpp != null) {
//                mCommandOpp.unregisterOppCallback(mCallbackOpp);
//            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "start unbind service");
        unbindService(mConnection);
        Log.e(TAG, "end unbind service");

        /** Unregister all callbacks */
        if (mDoCallbackHfp != null)
            mDoCallbackHfp.kill();
        if (mDoCallbackA2dp != null)
            mDoCallbackA2dp.kill();
        if (mDoCallbackAvrcp != null)
            mDoCallbackAvrcp.kill();
        if (mDoCallbackPbap != null)
            mDoCallbackPbap.kill();
//        if (mDoCallbackHid != null)
//            mDoCallbackHid.kill();
        if (mDoCallbackBluetooth != null)
            mDoCallbackBluetooth.kill();
//        if (mDoCallbackMap != null)
//            mDoCallbackMap.kill();
//        if (mDoCallbackOpp != null)
//            mDoCallbackOpp.kill();
//        if (mDoCallbackGattServer != null)
//            mDoCallbackGattServer.kill();

        Log.e(TAG, " --- onDestroy --- ");
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind() Intent : " + intent);
        return mBinder;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "ready onServiceConnected");

            Log.v(TAG, "Piggy Check className : " + className);

            Log.e(TAG, "IBinder service: " + service.hashCode());
            try {
                Log.v(TAG, "Piggy Check service : " + service.getInterfaceDescriptor());
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HFP))) {
                Log.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_HFP + ")");
                mCommandHfp = INfCommandHfp.Stub.asInterface(service);
                if (mCommandHfp == null) {
                    Log.e(TAG, "mCommandHfp is null!!");
                    return;
                }
                dumpClassMethod(mCommandHfp.getClass());
                try {
                    mCommandHfp.registerHfpCallback(mCallbackHfp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_A2DP))) {
                Log.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_A2DP + ")");
                mCommandA2dp = INfCommandA2dp.Stub.asInterface(service);
                if (mCommandA2dp == null) {
                    Log.e(TAG, "mCommandA2dp is null !!");
                    return;
                }
                dumpClassMethod(mCommandA2dp.getClass());
                try {
                    mCommandA2dp.registerA2dpCallback(mCallbackA2dp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_AVRCP))) {
                Log.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_AVRCP + ")");
                mCommandAvrcp = INfCommandAvrcp.Stub.asInterface(service);
                if (mCommandAvrcp == null) {
                    Log.e(TAG, "mCommandAvrcp is null !!");
                    return;
                }
                dumpClassMethod(mCommandAvrcp.getClass());
                try {
                    mCommandAvrcp.registerAvrcpCallback(mCallbackAvrcp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_PBAP))) {
                Log.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_PBAP + ")");
                mCommandPbap = INfCommandPbap.Stub.asInterface(service);
                if (mCommandPbap == null) {
                    Log.e(TAG, "mCommandPbap is null !!");
                    return;
                }
                dumpClassMethod(mCommandPbap.getClass());
                try {
                    mCommandPbap.registerPbapCallback(mCallbackPbap);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HID))) {
//                Log.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_HID + ")");
//                mCommandHid = INfCommandHid.Stub.asInterface(service);
//                if (mCommandHid == null) {
//                    Log.e(TAG,"mCommandHid is null !!");
//                    return;
//                }
//                dumpClassMethod(mCommandHid.getClass());
//                try {
//                    mCommandHid.registerHidCallback(mCallbackHid);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_BLUETOOTH))) {
                Log.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_BLUETOOTH + ")");
                mCommandBluetooth = INfCommandBluetooth.Stub.asInterface(service);
                if (mCommandBluetooth == null) {
                    Log.e(TAG, "mCommandBluetooth is null !!");
                    return;
                }
                dumpClassMethod(mCommandBluetooth.getClass());
                try {
                    mCommandBluetooth.registerBtCallback(mCallbackBluetooth);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                // Auto connect

//                Runnable r = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        try {
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            if (mCommandBluetooth.isBtEnabled()) {
//                                String address = getAutoConnectAddress();
//                                if (!address.equals(NfDef.DEFAULT_ADDRESS)) {
//                                    mCommandBluetooth.reqBtConnectHfpA2dp(address);    
//                                }                
//                            }
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                Thread t = new Thread(r);
//                t.start();
            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_SPP))) {
//                Log.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_SPP + ")");
//                mCommandSpp = INfCommandSpp.Stub.asInterface(service);
//                if (mCommandSpp == null) {
//                    Log.e(TAG,"mCommandSpp is null !!");
//                    return;
//                }
//                dumpClassMethod(mCommandSpp.getClass());
//                try {
//                    mCommandSpp.registerSppCallback(mCallbackSpp);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_MAP))) {
//                Log.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_MAP + ")");
//                mCommandMap = INfCommandMap.Stub.asInterface(service);
//                if (mCommandMap == null) {
//                    Log.e(TAG,"mCommandMap is null !!");
//                    return;
//                }
//                dumpClassMethod(mCommandMap.getClass());
//                try {
//                    mCommandMap.registerMapCallback(mCallbackMap);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_OPP))) {
//                Log.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_OPP + ")");
//                mCommandOpp = INfCommandOpp.Stub.asInterface(service);
//                if (mCommandOpp == null) {
//                    Log.e(TAG,"mCommandOpp is null !!");
//                    return;
//                }
//                dumpClassMethod(mCommandOpp.getClass());
//                try {
//                    mCommandOpp.registerOppCallback(mCallbackOpp);
//
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_GATT_SERVER))) {
//                Log.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_GATT_SERVER + ")");
//                mCommandGattServer = INfCommandGattServer.Stub.asInterface(service);
//                if (mCommandGattServer == null) {
//                    Log.e(TAG,"mCommandGattServer is null !!");
//                    return;
//                }
//                dumpClassMethod(mCommandGattServer.getClass());
//                try {
//                    mCommandGattServer.registerGattServerCallback(mCallbackGattServer);
//
//
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }

            btServiceConnectModel.onServiceConnected();

            Log.e(TAG, "end onServiceConnected");
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "ready onServiceDisconnected: " + className);
            if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HFP))) {
                mCommandHfp = null;
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_A2DP))) {
                mCommandA2dp = null;
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_AVRCP))) {
                mCommandAvrcp = null;
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_PBAP))) {
                mCommandPbap = null;
            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HID))) {
//                mCommandHid = null;
//            }
            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_BLUETOOTH))) {
                mCommandBluetooth = null;
            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_SPP))) {
//                mCommandSpp = null;
//            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_MAP))) {
//                mCommandMap = null;
//            }
//            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_OPP))) {
//                mCommandOpp = null;
//            }
            Log.e(TAG, "end onServiceDisconnected");
        }
    };


    /*
     *  For UI command API
     *
     */
    private UiCommand.Stub mBinder = new UiCommand.Stub() {

        @Override
        public boolean isAvrcpServiceReady() throws RemoteException {
            Log.v(TAG, "isAvrcpServiceReady()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.isAvrcpServiceReady();
        }

        @Override
        public boolean isA2dpServiceReady() throws RemoteException {
            Log.v(TAG, "isA2dpServiceReady()");
            if (mCommandA2dp == null) {
                return false;
            }
            return mCommandA2dp.isA2dpServiceReady();
        }

        @Override
        public boolean isSppServiceReady() throws RemoteException {
            return false;
        }

        @Override
        public boolean isBluetoothServiceReady() throws RemoteException {
            Log.v(TAG, "isBluetoothServiceReady()");
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.isBluetoothServiceReady();
        }

        @Override
        public boolean isHfpServiceReady() throws RemoteException {
            Log.v(TAG, "isHfpServiceReady()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.isHfpServiceReady();
        }

        @Override
        public boolean isHidServiceReady() throws RemoteException {
            return false;
        }

        @Override
        public boolean isPbapServiceReady() throws RemoteException {
            Log.v(TAG, "isPbapServiceReady()");
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.isPbapServiceReady();
        }

        @Override
        public boolean isOppServiceReady() throws RemoteException {
            return false;
        }

        @Override
        public boolean isMapServiceReady() throws RemoteException {
            return false;
        }

        @Override
        public String getUiServiceVersionName() throws RemoteException {
            return mVersionName;
        }

        @Override
        public boolean registerA2dpCallback(UiCallbackA2dp cb) throws RemoteException {
            Log.v(TAG, "registerA2dpCallback()");
            return mDoCallbackA2dp.register(cb);
        }

        @Override
        public boolean unregisterA2dpCallback(UiCallbackA2dp cb) throws RemoteException {
            Log.v(TAG, "unregisterA2dpCallback()");
            return mDoCallbackA2dp.unregister(cb);
        }

        @Override
        public int getA2dpConnectionState() throws RemoteException {
            Log.v(TAG, "getA2dpConnectionState()");
            if (mCommandA2dp == null) {
                return NfDef.STATE_NOT_INITIALIZED;
            }
            return mCommandA2dp.getA2dpConnectionState();
        }

        @Override
        public boolean isA2dpConnected() throws RemoteException {
            Log.v(TAG, "isA2dpConnected()");
            if (mCommandA2dp == null) {
                return false;
            }
            return mCommandA2dp.isA2dpConnected();
        }

        @Override
        public String getA2dpConnectedAddress() throws RemoteException {
            Log.v(TAG, "getA2dpConnectedAddress()");
            if (mCommandA2dp == null) {
                return NfDef.DEFAULT_ADDRESS;
            }
            return mCommandA2dp.getA2dpConnectedAddress();
        }

        @Override
        public boolean reqA2dpConnect(String address) throws RemoteException {
            Log.v(TAG, "reqA2dpConnect() " + address);
            if (mCommandA2dp == null) {
                return false;
            }
            return mCommandA2dp.reqA2dpConnect(address);
        }

        @Override
        public boolean reqA2dpDisconnect(String address) throws RemoteException {
            Log.v(TAG, "reqA2dpDisconnect() " + address);
            if (mCommandA2dp == null) {
                return false;
            }
            return mCommandA2dp.reqA2dpDisconnect(address);
        }

        @Override
        public void pauseA2dpRender() throws RemoteException {
            Log.v(TAG, "pauseA2dpRender()");
            if (mCommandA2dp == null) {
                return;
            }
            mCommandA2dp.pauseA2dpRender();
        }

        @Override
        public void startA2dpRender() throws RemoteException {
            Log.v(TAG, "startA2dpRender()");
            if (mCommandA2dp == null) {
                return;
            }
            mCommandA2dp.startA2dpRender();

        }

        @Override
        public boolean setA2dpLocalVolume(float vol) throws RemoteException {
            Log.v(TAG, "setA2dpLocalVolume() " + vol);
            if (mCommandA2dp == null) {
                return false;
            }
            return mCommandA2dp.setA2dpLocalVolume(vol);

        }

        @Override
        public boolean setA2dpStreamType(int type) throws RemoteException {
            Log.v(TAG, "setA2dpStreamType() " + type);
            if (mCommandA2dp == null) {
                return false;
            }
            return mCommandA2dp.setA2dpStreamType(type);

        }

        @Override
        public int getA2dpStreamType() throws RemoteException {
            Log.v(TAG, "getA2dpStreamType()");
            if (mCommandA2dp == null) {
                return -1;
            }
            return mCommandA2dp.getA2dpStreamType();

        }

        @Override
        public boolean registerAvrcpCallback(UiCallbackAvrcp cb) throws RemoteException {
            Log.v(TAG, "registerAvrcpCallback()");
            return mDoCallbackAvrcp.register(cb);
        }

        @Override
        public boolean unregisterAvrcpCallback(UiCallbackAvrcp cb) throws RemoteException {
            Log.v(TAG, "unregisterAvrcpCallback()");
            return mDoCallbackAvrcp.unregister(cb);
        }

        @Override
        public int getAvrcpConnectionState() throws RemoteException {
            Log.v(TAG, "getAvrcpConnectionState()");
            if (mCommandAvrcp == null) {
                return NfDef.STATE_NOT_INITIALIZED;
            }
            return mCommandAvrcp.getAvrcpConnectionState();
        }

        @Override
        public boolean isAvrcpConnected() throws RemoteException {
            Log.v(TAG, "isAvrcpConnected()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.isAvrcpConnected();
        }

        @Override
        public String getAvrcpConnectedAddress() throws RemoteException {
            Log.v(TAG, "getAvrcpConnectedAddress()");
            if (mCommandAvrcp == null) {
                return NfDef.DEFAULT_ADDRESS;
            }
            return mCommandAvrcp.getAvrcpConnectedAddress();
        }

        @Override
        public boolean reqAvrcpConnect(String address) throws RemoteException {
            Log.v(TAG, "reqAvrcpConnect() " + address);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpConnect(address);
        }

        @Override
        public boolean reqAvrcpDisconnect(String address) throws RemoteException {
            Log.v(TAG, "reqAvrcpDisconnect() " + address);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpDisconnect(address);
        }

        @Override
        public boolean isAvrcp13Supported(String address) throws RemoteException {
            Log.v(TAG, "isAvrcp13Supported() " + address);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.isAvrcp13Supported(address);
        }

        @Override
        public boolean isAvrcp14Supported(String address) throws RemoteException {
            Log.v(TAG, "isAvrcp14Supported() " + address);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.isAvrcp14Supported(address);
        }

        @Override
        public boolean reqAvrcpPlay() throws RemoteException {
            Log.v(TAG, "reqAvrcpPlay()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpPlay();
        }

        @Override
        public boolean reqAvrcpStop() throws RemoteException {
            Log.v(TAG, "reqAvrcpStop()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpStop();
        }

        @Override
        public boolean reqAvrcpPause() throws RemoteException {
            Log.v(TAG, "reqAvrcpPause()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpPause();
        }

        @Override
        public boolean reqAvrcpForward() throws RemoteException {
            Log.v(TAG, "reqAvrcpForward()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpForward();
        }

        @Override
        public boolean reqAvrcpBackward() throws RemoteException {
            Log.v(TAG, "reqAvrcpBackward()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpBackward();
        }

        @Override
        public boolean reqAvrcpVolumeUp() throws RemoteException {
            Log.v(TAG, "reqAvrcpVolumeUp()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpVolumeUp();
        }

        @Override
        public boolean reqAvrcpVolumeDown() throws RemoteException {
            Log.v(TAG, "reqAvrcpVolumeDown()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpVolumeDown();
        }

        @Override
        public boolean reqAvrcpStartFastForward() throws RemoteException {
            Log.v(TAG, "reqAvrcpStartFastForward()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpStartFastForward();
        }

        @Override
        public boolean reqAvrcpStopFastForward() throws RemoteException {
            Log.v(TAG, "reqAvrcpStopFastForward()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpStopFastForward();
        }

        @Override
        public boolean reqAvrcpStartRewind() throws RemoteException {
            Log.v(TAG, "reqAvrcpStartRewind()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpStartRewind();
        }

        @Override
        public boolean reqAvrcpStopRewind() throws RemoteException {
            Log.v(TAG, "reqAvrcpStopRewind()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpStopRewind();
        }

        @Override
        public boolean reqAvrcp13GetCapabilitiesSupportEvent() throws RemoteException {
            Log.v(TAG, "reqAvrcp13GetCapabilitiesSupportEvent()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13GetCapabilitiesSupportEvent();
        }

        @Override
        public boolean reqAvrcp13GetPlayerSettingAttributesList()
                throws RemoteException {
            Log.v(TAG, "reqAvrcp13GetPlayerSettingAttributesList()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13GetPlayerSettingAttributesList();
        }

        @Override
        public boolean reqAvrcp13GetPlayerSettingValuesList(byte attributeId)
                throws RemoteException {
            Log.v(TAG, "reqAvrcp13GetPlayerSettingValuesList() attributeId: " + attributeId);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13GetPlayerSettingValuesList(attributeId);
        }

        @Override
        public boolean reqAvrcp13GetPlayerSettingCurrentValues()
                throws RemoteException {
            Log.v(TAG, "reqAvrcp13GetPlayerSettingCurrentValues()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13GetPlayerSettingCurrentValues();
        }

        @Override
        public boolean reqAvrcp13SetPlayerSettingValue(byte attributeId,
                                                       byte valueId) throws RemoteException {
            Log.v(TAG, "reqAvrcp13SetPlayerSettingValue() attributeId: " + attributeId + " valueId: " + valueId);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13SetPlayerSettingValue(attributeId, valueId);
        }

        @Override
        public boolean reqAvrcp13GetElementAttributesPlaying() throws RemoteException {
            Log.v(TAG, "reqAvrcp13GetElementAttributesPlaying()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13GetElementAttributesPlaying();
        }

        @Override
        public boolean reqAvrcp13GetPlayStatus() throws RemoteException {
            Log.v(TAG, "REQ_AVRCP_13_GET_PLAY_STATUS()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13GetPlayStatus();
        }

        @Override
        public boolean reqAvrcpRegisterEventWatcher(byte eventId, long interval)
                throws RemoteException {
            Log.v(TAG, "reqAvrcpRegisterEventWatcher() eventId: " + eventId + " interval: " + interval);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpRegisterEventWatcher(eventId, interval);
        }

        @Override
        public boolean reqAvrcpUnregisterEventWatcher(byte eventId)
                throws RemoteException {
            Log.v(TAG, "reqAvrcpUnregisterEventWatcher() eventId: " + eventId);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcpUnregisterEventWatcher(eventId);
        }

        @Override
        public boolean reqAvrcp13NextGroup() throws RemoteException {
            Log.v(TAG, "reqAvrcp13NextGroup()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13NextGroup();
        }

        @Override
        public boolean reqAvrcp13PreviousGroup() throws RemoteException {
            Log.v(TAG, "reqAvrcp13PreviousGroup()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp13PreviousGroup();
        }

        @Override
        public boolean isAvrcp14BrowsingChannelEstablished() throws RemoteException {
            Log.v(TAG, "isAvrcp14BrowsingChannelEstablished()");
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.isAvrcp14BrowsingChannelEstablished();
        }

        @Override
        public boolean reqAvrcp14SetAddressedPlayer(int playerId)
                throws RemoteException {
            Log.v(TAG, "reqAvrcp14SetAddressedPlayer() playerId: " + playerId);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14SetAddressedPlayer(playerId);
        }

        @Override
        public boolean reqAvrcp14SetBrowsedPlayer(int playerId)
                throws RemoteException {
            Log.v(TAG, "reqAvrcp14SetBrowsedPlayer() playerId: " + playerId);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14SetBrowsedPlayer(playerId);
        }

        @Override
        public boolean reqAvrcp14GetFolderItems(byte scopeId)
                throws RemoteException {
            Log.v(TAG, "reqAvrcp14GetFolderItems() scopeId: " + scopeId);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14GetFolderItems(scopeId);
        }

        @Override
        public boolean reqAvrcp14ChangePath(int uidCounter, long uid, byte direction)
                throws RemoteException {
            Log.v(TAG, "reqAvrcp14ChangePath() uidCounter: " + uidCounter + " uid: " + uid + " direction: " + direction);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14ChangePath(uidCounter, uid, direction);
        }

        @Override
        public boolean reqAvrcp14GetItemAttributes(byte scopeId, int uidCounter,
                                                   long uid) throws RemoteException {
            Log.v(TAG, "reqAvrcp14GetItemAttributes() scopeId: " + scopeId + " uidCounter: " + uidCounter);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14GetItemAttributes(scopeId, uidCounter, uid);
        }

        @Override
        public boolean reqAvrcp14PlaySelectedItem(byte scopeId, int uidCounter,
                                                  long uid) throws RemoteException {
            Log.v(TAG, "reqAvrcp14PlaySelectedItem() scopeId: " + scopeId + " uidCounter: " + uidCounter + " uid: " + uid);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14PlaySelectedItem(scopeId, uidCounter, uid);
        }

        @Override
        public boolean reqAvrcp14Search(String text) throws RemoteException {
            Log.v(TAG, "reqAvrcp14Search() text: " + text);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14Search(text);
        }

        @Override
        public boolean reqAvrcp14AddToNowPlaying(byte scopeId, int uidCounter,
                                                 long uid) throws RemoteException {
            Log.v(TAG, "reqAvrcp14AddToNowPlaying() scopeId: " + scopeId + " uidCounter: " + uidCounter + " uid: " + uid);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14AddToNowPlaying(scopeId, uidCounter, uid);
        }

        @Override
        public boolean reqAvrcp14SetAbsoluteVolume(byte volume)
                throws RemoteException {
            Log.v(TAG, "reqAvrcp14SetAbsoluteVolume() volume: " + volume);
            if (mCommandAvrcp == null) {
                return false;
            }
            return mCommandAvrcp.reqAvrcp14SetAbsoluteVolume(volume);
        }

        @Override
        public boolean registerBtCallback(UiCallbackBluetooth cb) throws RemoteException {
            Log.v(TAG, "registerBtCallback()");
            if (mDoCallbackBluetooth == null) {
                return false;
            }
            return mDoCallbackBluetooth.register(cb);
        }

        @Override
        public boolean unregisterBtCallback(UiCallbackBluetooth cb) throws RemoteException {
            Log.v(TAG, "registerBtCallback()");
            if (mDoCallbackBluetooth == null) {
                return false;
            }
            return mDoCallbackBluetooth.unregister(cb);
        }

        @Override
        public String getNfServiceVersionName() throws RemoteException {
            Log.v(TAG, "getNfServiceVersionName()");
            if (mDoCallbackBluetooth == null) {
                return "";
            }
            return mCommandBluetooth.getNfServiceVersionName();
        }

        @Override
        public boolean setBtEnable(boolean enable) throws RemoteException {
            Log.v(TAG, "setBtEnable() enable: " + enable);
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.setBtEnable(enable);
        }

        @Override
        public boolean setBtDiscoverableTimeout(int timeout) throws RemoteException {
            Log.v(TAG, "setBtDiscoverableTimeout() timeout: " + timeout);
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.setBtDiscoverableTimeout(timeout);
        }

        @Override
        public boolean startBtDiscovery() throws RemoteException {
            Log.v(TAG, "startBtDiscovery()");
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.startBtDiscovery();
        }

        @Override
        public boolean cancelBtDiscovery() throws RemoteException {
            Log.v(TAG, "cancelBtDiscovery()");
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.cancelBtDiscovery();
        }

        @Override
        public boolean reqBtPair(String address) throws RemoteException {
            Log.v(TAG, "reqBtPair() " + address);
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.reqBtPair(address);
        }

        @Override
        public boolean reqBtUnpair(String address) throws RemoteException {
            Log.v(TAG, "reqBtUnpair() " + address);
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.reqBtUnpair(address);
        }

        @Override
        public boolean reqBtPairedDevices() throws RemoteException {
            Log.v(TAG, "reqBtPairedDevices()");
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.reqBtPairedDevices();
        }

        @Override
        public String getBtLocalName() throws RemoteException {
            Log.v(TAG, "getBtLocalName()");
            if (mCommandBluetooth == null) {
                return null;
            }
            return mCommandBluetooth.getBtLocalName();
        }

        @Override
        public String getBtRemoteDeviceName(String address) throws RemoteException {
            Log.v(TAG, "getBtRemoteDeviceName() " + address);
            if (mCommandBluetooth == null) {
                return null;
            }
            return mCommandBluetooth.getBtRemoteDeviceName(address);
        }

        @Override
        public String getBtLocalAddress() throws RemoteException {
            Log.v(TAG, "getBtLocalAddress()");
            if (mCommandBluetooth == null) {
                return NfDef.DEFAULT_ADDRESS;
            }
            return mCommandBluetooth.getBtLocalAddress();
        }

        @Override
        public boolean setBtLocalName(String name) throws RemoteException {
            Log.v(TAG, "setBtLocalName() name: " + name);
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.setBtLocalName(name);
        }

        @Override
        public boolean isBtEnabled() throws RemoteException {
            Log.v(TAG, "isBtEnabled()");
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.isBtEnabled();
        }

        @Override
        public int getBtState() throws RemoteException {
            Log.v(TAG, "getBtState()");
            if (mCommandBluetooth == null) {
                return NfDef.STATE_NOT_INITIALIZED;
            }
            return mCommandBluetooth.getBtState();
        }

        @Override
        public boolean isBtDiscovering() throws RemoteException {
            Log.v(TAG, "isBtDiscovering()");
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.isBtDiscovering();
        }

        @Override
        public boolean isBtDiscoverable() throws RemoteException {
            Log.v(TAG, "isBtDiscoverable()");
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.isBtDiscoverable();
        }

        @Override
        public boolean isBtAutoConnectEnable() throws RemoteException {
            Log.v(TAG, "isBtAutoConnectEnable()");
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.isBtAutoConnectEnable();
        }

        @Override
        public int reqBtConnectHfpA2dp(String address) throws RemoteException {
            Log.v(TAG, "reqBtConnectHfpA2dp() " + address);
            if (mCommandBluetooth == null) {
                return -1;
            }
            return mCommandBluetooth.reqBtConnectHfpA2dp(address);
        }

        @Override
        public int reqBtDisconnectAll() throws RemoteException {
            Log.v(TAG, "reqBtDisconnectAll()");
            if (mCommandBluetooth == null) {
                return -1;
            }
            return mCommandBluetooth.reqBtDisconnectAll();
        }

        @Override
        public int getBtRemoteUuids(String address) throws RemoteException {
            Log.v(TAG, "getBtRemoteUuids() " + address);
            if (mCommandBluetooth == null) {
                return -1;
            }
            return mCommandBluetooth.getBtRemoteUuids(address);
        }

        @Override
        public boolean switchBtRoleMode(int mode) throws RemoteException {
            Log.v(TAG, "switchBtRoleMode() " + mode);
            if (mCommandBluetooth == null) {
                return false;
            }
            return mCommandBluetooth.switchBtRoleMode(mode);
        }

        @Override
        public int getBtRoleMode() throws RemoteException {
            Log.v(TAG, "getBtRoleMode()");
            if (mCommandBluetooth == null) {
                return -1;
            }
            return mCommandBluetooth.getBtRoleMode();
        }

        @Override
        public void setBtAutoConnect(int condition, int period) throws RemoteException {
            Log.v(TAG, "setBtAutoConnect() condition: " + condition + " period: " + period);
            if (mCommandBluetooth == null) {
                return;
            }
            mCommandBluetooth.setBtAutoConnect(condition, period);

        }

        @Override
        public int getBtAutoConnectCondition() throws RemoteException {
            Log.v(TAG, "getBtAutoConnectCondition()");
            if (mCommandBluetooth == null) {
                return -1;
            }
            return mCommandBluetooth.getBtAutoConnectCondition();
        }

        @Override
        public int getBtAutoConnectPeriod() throws RemoteException {
            Log.v(TAG, "getBtAutoConnectPeriod()");
            if (mCommandBluetooth == null) {
                return -1;
            }
            return mCommandBluetooth.getBtAutoConnectPeriod();
        }

        @Override
        public int getBtAutoConnectState() throws RemoteException {
            Log.v(TAG, "getBtAutoConnectState()");
            if (mCommandBluetooth == null) {
                return -1;
            }
            return mCommandBluetooth.getBtAutoConnectState();
        }

        @Override
        public String getBtAutoConnectingAddress() throws RemoteException {
            Log.v(TAG, "getBtAutoConnectingAddress()");
            if (mCommandBluetooth == null) {
                return NfDef.DEFAULT_ADDRESS;
            }
            return mCommandBluetooth.getBtAutoConnectingAddress();
        }

        @Override
        public boolean registerHfpCallback(UiCallbackHfp cb) throws RemoteException {
            Log.v(TAG, "registerHfpCallback()");
            return mDoCallbackHfp.register(cb);
        }

        @Override
        public boolean unregisterHfpCallback(UiCallbackHfp cb) throws RemoteException {
            Log.v(TAG, "unregisterHfpCallback()");
            return mDoCallbackHfp.unregister(cb);
        }

        @Override
        public int getHfpConnectionState() throws RemoteException {
            Log.v(TAG, "getHfpConnectionState()");
            if (mCommandHfp == null) {
                return NfDef.STATE_NOT_INITIALIZED;
            }
            return mCommandHfp.getHfpConnectionState();
        }

        @Override
        public boolean isHfpConnected() throws RemoteException {
            Log.v(TAG, "isHfpConnected()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.isHfpConnected();
        }

        @Override
        public String getHfpConnectedAddress() throws RemoteException {
            Log.v(TAG, "getHfpConnectedAddress()");
            if (mCommandHfp == null) {
                return NfDef.DEFAULT_ADDRESS;
            }
            return mCommandHfp.getHfpConnectedAddress();
        }

        @Override
        public int getHfpAudioConnectionState() throws RemoteException {
            Log.v(TAG, "getHfpAudioConnectionState()");
            if (mCommandHfp == null) {
                return NfDef.STATE_NOT_INITIALIZED;
            }
            return mCommandHfp.getHfpAudioConnectionState();
        }

        @Override
        public boolean reqHfpConnect(String address) throws RemoteException {
            Log.v(TAG, "reqHfpConnect() " + address);
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpConnect(address);
        }

        @Override
        public boolean reqHfpDisconnect(String address) throws RemoteException {
            Log.v(TAG, "reqHfpDisconnect() " + address);
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpDisconnect(address);
        }

        @Override
        public int getHfpRemoteSignalStrength() throws RemoteException {
            Log.v(TAG, "getHfpRemoteSignalStrength()");
            if (mCommandHfp == null) {
                return 0;
            }
            return mCommandHfp.getHfpRemoteSignalStrength();
        }

        @Override
        public List<NfHfpClientCall> getHfpCallList() throws RemoteException {
            Log.v(TAG, "initBTStatus()");
            if (mCommandHfp == null) {
                return null;
            }
            return mCommandHfp.getHfpCallList();
        }

        @Override
        public boolean isHfpRemoteOnRoaming() throws RemoteException {
            Log.v(TAG, "isHfpRemoteOnRoaming()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.isHfpRemoteOnRoaming();
        }

        @Override
        public int getHfpRemoteBatteryIndicator() throws RemoteException {
            Log.v(TAG, "getHfpRemoteBatteryIndicator()");
            if (mCommandHfp == null) {
                return -1;
            }
            return mCommandHfp.getHfpRemoteBatteryIndicator();
        }

        @Override
        public boolean isHfpRemoteTelecomServiceOn() throws RemoteException {
            Log.v(TAG, "isHfpRemoteTelecomServiceOn()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.isHfpRemoteTelecomServiceOn();
        }

        @Override
        public boolean isHfpRemoteVoiceDialOn() throws RemoteException {
            Log.v(TAG, "isHfpRemoteVoiceDialOn()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.isHfpRemoteVoiceDialOn();
        }

        @Override
        public boolean reqHfpDialCall(String number) throws RemoteException {
            Log.v(TAG, "reqHfpDialCall() number: " + number);
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpDialCall(number);
        }

        @Override
        public boolean reqHfpReDial() throws RemoteException {
            Log.v(TAG, "reqHfpReDial()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpReDial();
        }

        @Override
        public boolean reqHfpMemoryDial(String index) throws RemoteException {
            Log.v(TAG, "reqHfpMemoryDial() index: " + index);
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpMemoryDial(index);
        }

        @Override
        public boolean reqHfpAnswerCall(int flag) throws RemoteException {
            Log.v(TAG, "REQ_HFP_ANSWER_CALL() " + flag);
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpAnswerCall(flag);
        }

        @Override
        public boolean reqHfpRejectIncomingCall() throws RemoteException {
            Log.v(TAG, "reqHfpRejectIncomingCall()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpRejectIncomingCall();
        }

        @Override
        public boolean reqHfpTerminateCurrentCall() throws RemoteException {
            Log.v(TAG, "REQ_HFP_TERMINATE_CURRENT_CALL()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpTerminateCurrentCall();
        }

        @Override
        public boolean reqHfpSendDtmf(String number) throws RemoteException {
            Log.v(TAG, "reqHfpSendDtmf() number: " + number);
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpSendDtmf(number);
        }

        @Override
        public boolean reqHfpAudioTransferToCarkit() throws RemoteException {
            Log.v(TAG, "reqHfpAudioTransfer()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpAudioTransferToCarkit();
        }

        @Override
        public boolean reqHfpAudioTransferToPhone() throws RemoteException {
            Log.v(TAG, "reqHfpAudioTransferToPhone()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpAudioTransferToPhone();
        }

        @Override
        public String getHfpRemoteNetworkOperator() throws RemoteException {
            Log.v(TAG, "reqHfpRemoteNetworkOperator()");
            if (mCommandHfp == null) {
                return null;
            }
            return mCommandHfp.getHfpRemoteNetworkOperator();
        }

        @Override
        public String getHfpRemoteSubscriberNumber() throws RemoteException {
            Log.v(TAG, "getHfpRemoteSubscriberNumber()");
            if (mCommandHfp == null) {
                return null;
            }
            return mCommandHfp.getHfpRemoteSubscriberNumber();
        }

        @Override
        public boolean reqHfpVoiceDial(boolean enable) throws RemoteException {
            Log.v(TAG, "reqHfpVoiceDial() enable: " + enable);
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.reqHfpVoiceDial(enable);
        }

        @Override
        public void pauseHfpRender() throws RemoteException {
            Log.v(TAG, "pauseHfpRender()");
            if (mCommandHfp == null) {
                return;
            }
            mCommandHfp.pauseHfpRender();
        }

        @Override
        public void startHfpRender() throws RemoteException {
            Log.v(TAG, "startHfpRender()");
            if (mCommandHfp == null) {
                return;
            }
            mCommandHfp.startHfpRender();
        }

        @Override
        public boolean isHfpMicMute() throws RemoteException {
            Log.v(TAG, "isHfpMicMute()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.isHfpMicMute();
        }

        @Override
        public void muteHfpMic(boolean mute) throws RemoteException {
            Log.v(TAG, "muteHfpMic() " + mute);
            if (mCommandHfp == null) {
                return;
            }
            mCommandHfp.muteHfpMic(mute);
        }

        @Override
        public boolean isHfpInBandRingtoneSupport() throws RemoteException {
            Log.v(TAG, "isHfpInbandRingtoneSupport()");
            if (mCommandHfp == null) {
                return false;
            }
            return mCommandHfp.isHfpInBandRingtoneSupport();
        }

        @Override
        public boolean registerPbapCallback(UiCallbackPbap cb) throws RemoteException {
            Log.v(TAG, "registerPbapCallback()");
            return mDoCallbackPbap.register(cb);
        }

        @Override
        public boolean unregisterPbapCallback(UiCallbackPbap cb) throws RemoteException {
            Log.v(TAG, "registerPbapCallback()");
            return mDoCallbackPbap.unregister(cb);
        }

        @Override
        public int getPbapConnectionState() throws RemoteException {
            Log.v(TAG, "getPbapConnectionState()");
            if (mCommandPbap == null) {
                return NfDef.STATE_NOT_INITIALIZED;
            }
            return mCommandPbap.getPbapConnectionState();
        }

        @Override
        public boolean isPbapDownloading() throws RemoteException {
            Log.v(TAG, "isPbapDownloading()");
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.isPbapDownloading();
        }

        @Override
        public String getPbapDownloadingAddress() throws RemoteException {
            Log.v(TAG, "getPbapDownloadingAddress()");
            if (mCommandPbap == null) {
                return NfDef.DEFAULT_ADDRESS;
            }
            return mCommandPbap.getPbapDownloadingAddress();
        }

        @Override
        public boolean reqPbapDownload(String address, int storage, int property)
                throws RemoteException {
            Log.v(TAG, "reqPbapDownload() " + address + " (" + storage + ")");
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.reqPbapDownload(address, storage, property);
        }

        @Override
        public boolean reqPbapDownloadRange(String address, int storage, int property,
                                            int startPos, int offset) throws RemoteException {
            Log.v(TAG, "reqPbapDownloadRange()");
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.reqPbapDownloadRange(address, storage, property, startPos, offset);
        }

        @Override
        public boolean reqPbapDownloadToDatabase(String address, int storage, int property)
                throws RemoteException {
            Log.v(TAG, "reqPbapDownloadToDatabase() " + address + " storage: " + storage + " property: " + property);
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.reqPbapDownloadToDatabase(address, storage, property);
        }

        @Override
        public boolean reqPbapDownloadRangeToDatabase(String address, int storage,
                                                      int property, int startPos, int offset) throws RemoteException {
            Log.v(TAG, "reqPbapDownloadRangeToDatabase() " + address + " storage: " + storage + " isPhoteRequire: " + property + " startPos: " + startPos + " offset: " + offset);
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.reqPbapDownloadRangeToDatabase(address, storage, property, startPos, offset);
        }

        @Override
        public boolean reqPbapDownloadToContactsProvider(String address, int storage,
                                                         int property) throws RemoteException {
            Log.v(TAG, "reqPbapDownloadToContactsProvider() " + address + " storage: " + storage + " isPhoteRequire: " + property);
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.reqPbapDownloadToContactsProvider(address, storage, property);
        }

        @Override
        public boolean reqPbapDownloadRangeToContactsProvider(String address, int storage,
                                                              int property, int startPos, int offset) throws RemoteException {
            Log.v(TAG, "reqPbapDownloadRangeToContactsProvider() " + address + " storage: " + storage + " isPhoteRequire: " + property + " startPos: " + startPos + " offset: " + offset);
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.reqPbapDownloadRangeToContactsProvider(address, storage, property, startPos, offset);
        }

        @Override
        public void reqPbapDatabaseQueryNameByNumber(String address, String target)
                throws RemoteException {
            Log.v(TAG, "reqPbapDatabaseQueryNameByNumber() " + address + " target: " + target);
            if (mCommandPbap == null) {
                return;
            }
            mCommandPbap.reqPbapDatabaseQueryNameByNumber(address, target);
        }

        @Override
        public void reqPbapDatabaseQueryNameByPartialNumber(String address, String target,
                                                            int findPosition) throws RemoteException {
            Log.v(TAG, "reqPbapDatabaseQueryNameByPartialNumber() " + address + " target: " + target + " findPosition: " + findPosition);
            if (mCommandPbap == null) {
                return;
            }
            mCommandPbap.reqPbapDatabaseQueryNameByPartialNumber(address, target, findPosition);
        }

        @Override
        public void reqPbapDatabaseAvailable(String address) throws RemoteException {
            Log.v(TAG, "reqPbapDatabaseAvailable() " + address);
            if (mCommandPbap == null) {
                return;
            }
            mCommandPbap.reqPbapDatabaseAvailable(address);
        }

        @Override
        public void reqPbapDeleteDatabaseByAddress(String address) throws RemoteException {
            Log.v(TAG, "reqPbapDeleteDatabaseByAddress() " + address);
            if (mCommandPbap == null) {
                return;
            }
            mCommandPbap.reqPbapDeleteDatabaseByAddress(address);
        }

        @Override
        public void reqPbapCleanDatabase() throws RemoteException {
            Log.v(TAG, "reqPbapCleanDatabase()");
            if (mCommandPbap == null) {
                return;
            }
            mCommandPbap.reqPbapCleanDatabase();
        }

        @Override
        public boolean reqPbapDownloadInterrupt(String address) throws RemoteException {
            Log.v(TAG, "REQ_PBAP_DOWNLOAD_INTERRUPT() " + address);
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.reqPbapDownloadInterrupt(address);
        }

        @Override
        public boolean setPbapDownloadNotify(int frequency) throws RemoteException {
            Log.v(TAG, "setPbapDownloadNotify() " + frequency);
            if (mCommandPbap == null) {
                return false;
            }
            return mCommandPbap.setPbapDownloadNotify(frequency);
        }

        @Override
        public boolean registerSppCallback(UiCallbackSpp cb) throws RemoteException {
            return false;
        }

        @Override
        public boolean unregisterSppCallback(UiCallbackSpp cb) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqSppConnect(String address) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqSppDisconnect(String address) throws RemoteException {
            return false;
        }

        @Override
        public void reqSppConnectedDeviceAddressList() throws RemoteException {

        }

        @Override
        public boolean isSppConnected(String address) throws RemoteException {
            return false;
        }

        @Override
        public void reqSppSendData(String address, byte[] sppData) throws RemoteException {

        }

        @Override
        public boolean registerHidCallback(UiCallbackHid cb) throws RemoteException {
            return false;
        }

        @Override
        public boolean unregisterHidCallback(UiCallbackHid cb) throws RemoteException {
            return false;
        }

        @Override
        public int getHidConnectionState() throws RemoteException {
            return 0;
        }

        @Override
        public boolean isHidConnected() throws RemoteException {
            return false;
        }

        @Override
        public String getHidConnectedAddress() throws RemoteException {
            return null;
        }

        @Override
        public boolean reqHidConnect(String address) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqHidDisconnect(String address) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqSendHidMouseCommand(int button, int offset_x, int offset_y, int wheel) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqSendHidVirtualKeyCommand(int key_1, int key_2) throws RemoteException {
            return false;
        }

        @Override
        public boolean registerMapCallback(UiCallbackMap cb) throws RemoteException {
            return false;
        }

        @Override
        public boolean unregisterMapCallback(UiCallbackMap cb) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqMapDownloadSingleMessage(String address, int folder, String handle, int storage) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqMapDownloadMessage(String address, int folder, boolean isContentDownload, int count, int startPos, int storage, String periodBegin, String periodEnd, String sender, String recipient, int readStatus, int typeFilter) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqMapRegisterNotification(String address, boolean downloadNewMessage) throws RemoteException {
            return false;
        }

        @Override
        public void reqMapUnregisterNotification(String address) throws RemoteException {

        }

        @Override
        public boolean isMapNotificationRegistered(String address) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqMapDownloadInterrupt(String address) throws RemoteException {
            return false;
        }

        @Override
        public void reqMapDatabaseAvailable() throws RemoteException {

        }

        @Override
        public void reqMapDeleteDatabaseByAddress(String address) throws RemoteException {

        }

        @Override
        public void reqMapCleanDatabase() throws RemoteException {

        }

        @Override
        public int getMapCurrentState(String address) throws RemoteException {
            return 0;
        }

        @Override
        public int getMapRegisterState(String address) throws RemoteException {
            return 0;
        }

        @Override
        public boolean reqMapSendMessage(String address, String message, String target) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqMapDeleteMessage(String address, int folder, String handle) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqMapChangeReadStatus(String address, int folder, String handle, boolean isReadStatus) throws RemoteException {
            return false;
        }

        @Override
        public boolean setMapDownloadNotify(int frequency) throws RemoteException {
            return false;
        }

        @Override
        public boolean registerOppCallback(UiCallbackOpp cb) throws RemoteException {
            return false;
        }

        @Override
        public boolean unregisterOppCallback(UiCallbackOpp cb) throws RemoteException {
            return false;
        }

        @Override
        public boolean setOppFilePath(String path) throws RemoteException {
            return false;
        }

        @Override
        public String getOppFilePath() throws RemoteException {
            return null;
        }

        @Override
        public boolean reqOppAcceptReceiveFile(boolean accept) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqOppInterruptReceiveFile() throws RemoteException {
            return false;
        }

        @Override
        public void setTargetAddress(String address) throws RemoteException {
            Log.e(TAG, "setTargetAddress(): " + address);
            if (address != null) {
                targetAddress = address;
            }
        }

        @Override
        public String getTargetAddress() throws RemoteException {
            Log.e(TAG, "getTargetAddress(): " + targetAddress);
            return targetAddress;
        }

        @Override
        public void reqAvrcpUpdateSongStatus() throws RemoteException {
            Log.v(TAG, "reqAvrcpUpdateSongStatus()");
            mDoCallbackAvrcp.retAvrcpUpdateSongStatus(artist, album, title);
        }

        @Override
        public boolean isGattServiceReady() throws RemoteException {
            return false;
        }

        @Override
        public boolean registerGattServerCallback(UiCallbackGattServer cb) throws RemoteException {
            return false;
        }

        @Override
        public boolean unregisterGattServerCallback(UiCallbackGattServer cb) throws RemoteException {
            return false;
        }

        @Override
        public int getGattServerConnectionState() throws RemoteException {
            return 0;
        }

        @Override
        public boolean reqGattServerDisconnect(String address) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerBeginServiceDeclaration(int srvcType, ParcelUuid srvcUuid) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerAddCharacteristic(ParcelUuid charUuid, int properties, int permissions) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerAddDescriptor(ParcelUuid descUuid, int permissions) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerEndServiceDeclaration() throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerRemoveService(int srvcType, ParcelUuid srvcUuid) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerClearServices() throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerListen(boolean listen) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerSendResponse(String address, int requestId, int status, int offset, byte[] value) throws RemoteException {
            return false;
        }

        @Override
        public boolean reqGattServerSendNotification(String address, int srvcType, ParcelUuid srvcUuid, ParcelUuid charUuid, boolean confirm, byte[] value) throws RemoteException {
            return false;
        }

        @Override
        public List<ParcelUuid> getGattAddedGattServiceUuidList() throws RemoteException {
            return null;
        }

        @Override
        public List<ParcelUuid> getGattAddedGattCharacteristicUuidList(ParcelUuid srvcUuid) throws RemoteException {
            return null;
        }

        @Override
        public List<ParcelUuid> getGattAddedGattDescriptorUuidList(ParcelUuid srvcUuid, ParcelUuid charUuid) throws RemoteException {
            return null;
        }


    };

    private String targetAddress = NfDef.DEFAULT_ADDRESS;

    /*
     * Hfp callback
     *
     */
    private INfCallbackHfp mCallbackHfp = new INfCallbackHfp.Stub() {

        @Override
        public void onHfpServiceReady() throws RemoteException {
            Log.v(TAG, "onHfpServiceReady()");
            mDoCallbackHfp.onHfpServiceReady();
        }

        @Override
        public void onHfpStateChanged(String address, int prevState, int newState)
                throws RemoteException {
            Log.v(TAG, "onHfpStateChanged() " + address + " state: " + prevState + "->" + newState);
            mDoCallbackHfp.onHfpStateChanged(address, prevState, newState);
            mDoCallbackBluetooth.onHfpStateChanged(address, prevState, newState);
//            if (newState >= NfDef.STATE_CONNECTED) {
//                setAutoConnectAddress(address);
//            }
        }

        @Override
        public void onHfpAudioStateChanged(String address, int prevState, int newState)
                throws RemoteException {
            Log.v(TAG, "onHfpAudioStateChanged() " + address + " state: " + prevState + "->" + newState);
            mDoCallbackHfp.onHfpAudioStateChanged(address, prevState, newState);
        }

        @Override
        public void onHfpVoiceDial(String address, boolean isVoiceDialOn) throws RemoteException {
            Log.v(TAG, "onHfpVoiceDial() " + address + " isVoiceDialOn: " + isVoiceDialOn);
            mDoCallbackHfp.onHfpVoiceDial(address, isVoiceDialOn);
        }

        @Override
        public void onHfpErrorResponse(String address, int code) throws RemoteException {
            Log.v(TAG, "onHfpErrorResponse() " + address + " code: " + code);
            mDoCallbackHfp.onHfpErrorResponse(address, code);
        }

        @Override
        public void onHfpRemoteTelecomService(String address, boolean isTelecomServiceOn)
                throws RemoteException {
            Log.v(TAG, "onHfpRemoteTelecomService() " + address + " isTelecomServiceOn: " + isTelecomServiceOn);
            mDoCallbackHfp.onHfpRemoteTelecomService(address, isTelecomServiceOn);
        }

        @Override
        public void onHfpRemoteRoamingStatus(String address, boolean isRoamingOn)
                throws RemoteException {
            Log.v(TAG, "onHfpRemoteRoamingStatus() " + address + " isRoamingOn: " + isRoamingOn);
            mDoCallbackHfp.onHfpRemoteRoamingStatus(address, isRoamingOn);
        }

        @Override
        public void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue,
                                                int minValue) throws RemoteException {
            Log.v(TAG, "onHfpRemoteBatteryIndicator() " + address + " value: " + currentValue + " (" + minValue + "-" + maxValue + ")");
            mDoCallbackHfp.onHfpRemoteBatteryIndicator(address, currentValue, maxValue, minValue);
        }

        @Override
        public void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength,
                                              int minStrength) throws RemoteException {
            Log.v(TAG, "onHfpRemoteSignalStrength() " + address + " strength: " + currentStrength + " (" + minStrength + "-" + maxStrength + ")");
            mDoCallbackHfp.onHfpRemoteSignalStrength(address, currentStrength, maxStrength, minStrength);
        }

        @Override
        public void onHfpCallChanged(String address, NfHfpClientCall call) throws RemoteException {
            Log.v(TAG, "onHfpCallChanged() " + address);
            mDoCallbackHfp.onHfpCallChanged(address, call);
        }

    };


    /*
     * A2dp Callback
     *
     */

    private INfCallbackA2dp mCallbackA2dp = new INfCallbackA2dp.Stub() {

        @Override
        public void onA2dpServiceReady() throws RemoteException {
            Log.v(TAG, "onA2dpServiceReady()");
            mDoCallbackA2dp.onA2dpServiceReady();
        }

        @Override
        public void onA2dpStateChanged(String address, int prevState, int newState)
                throws RemoteException {
            Log.v(TAG, "onA2dpStateChanged() " + address + " state: " + prevState + "->" + newState);
            mDoCallbackA2dp.onA2dpStateChanged(address, prevState, newState);
            mDoCallbackBluetooth.onA2dpStateChanged(address, prevState, newState);
        }
    };

    String title = "";
    String artist = "";
    String album = "";
    private INfCallbackAvrcp mCallbackAvrcp = new INfCallbackAvrcp.Stub() {

        @Override
        public void onAvrcpServiceReady() throws RemoteException {
            Log.v(TAG, "onAvrcpServiceReady()");
            mDoCallbackAvrcp.onAvrcpServiceReady();
        }

        @Override
        public void onAvrcpStateChanged(String address, int prevState, int newState)
                throws RemoteException {
            Log.v(TAG, "onAvrcpStateChanged() " + address + " state: " + prevState + "->" + newState);
            if (newState >= NfDef.STATE_CONNECTED && prevState < NfDef.STATE_CONNECTED) {
                Log.e(TAG, "reqAvrcpCtRegisterEventWatcher");
                mCommandAvrcp.reqAvrcpRegisterEventWatcher(NfDef.AVRCP_EVENT_ID_TRACK_CHANGED, 0);
                mCommandAvrcp.reqAvrcpRegisterEventWatcher(NfDef.AVRCP_EVENT_ID_PLAYBACK_STATUS_CHANGED, 0);
            } else if (newState == NfDef.STATE_READY) {
                title = "";
                artist = "";
                album = "";
            }
            mDoCallbackAvrcp.onAvrcpStateChanged(address, prevState, newState);
            mDoCallbackBluetooth.onAvrcpStateChanged(address, prevState, newState);
        }

        @Override
        public void retAvrcp13CapabilitiesSupportEvent(byte[] eventIds)
                throws RemoteException {
            Log.v(TAG, "retAvrcp13CapabilitiesSupportEvent()");
            mDoCallbackAvrcp.retAvrcp13CapabilitiesSupportEvent(eventIds);
        }

        @Override
        public void retAvrcp13PlayerSettingAttributesList(byte[] attributeIds)
                throws RemoteException {
            Log.v(TAG, "retAvrcp13PlayerSettingAttributesList()");
            mDoCallbackAvrcp.retAvrcp13PlayerSettingAttributesList(attributeIds);
        }

        @Override
        public void retAvrcp13PlayerSettingValuesList(byte attributeId,
                                                      byte[] valueIds) throws RemoteException {
            Log.v(TAG, "retAvrcp13PlayerSettingValuesList() attributeId: " + attributeId);
            mDoCallbackAvrcp.retAvrcp13PlayerSettingValuesList(attributeId, valueIds);
        }

        @Override
        public void retAvrcp13PlayerSettingCurrentValues(byte[] attributeIds,
                                                         byte[] valueIds) throws RemoteException {
            Log.v(TAG, "retAvrcp13PlayerSettingCurrentValues()");
            mDoCallbackAvrcp.retAvrcp13PlayerSettingCurrentValues(attributeIds, valueIds);
        }

        @Override
        public void retAvrcp13SetPlayerSettingValueSuccess() throws RemoteException {
            Log.v(TAG, "retAvrcp13SetPlayerSettingValueSuccess()");
            mDoCallbackAvrcp.retAvrcp13SetPlayerSettingValueSuccess();
        }

        @Override
        public void retAvrcp13ElementAttributesPlaying(int[] metadataAtrributeIds,
                                                       String[] texts) throws RemoteException {
            Log.v(TAG, "retAvrcp13ElementAttributesPlaying()");

            title = "";
            album = "";
            artist = "";

            for (int i = 0; i < metadataAtrributeIds.length; i++) {
                if (metadataAtrributeIds[i] == NfDef.AVRCP_META_ATTRIBUTE_ID_TITLE) {
                    title = texts[i];
                    Log.v(TAG, "retAvrcp13ElementAttributesPlaying() title: " + title);
                } else if (metadataAtrributeIds[i] == NfDef.AVRCP_META_ATTRIBUTE_ID_ALBUM) {
                    album = texts[i];
                    Log.v(TAG, "retAvrcp13ElementAttributesPlaying() album: " + album);
                } else if (metadataAtrributeIds[i] == NfDef.AVRCP_META_ATTRIBUTE_ID_ARTIST) {
                    artist = texts[i];
                    Log.v(TAG, "retAvrcp13ElementAttributesPlaying() artist: " + artist);
                }
            }

            mDoCallbackAvrcp.retAvrcp13ElementAttributesPlaying(metadataAtrributeIds, texts);
        }

        @Override
        public void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId)
                throws RemoteException {
            Log.v(TAG, "retAvrcp13PlayStatus() songLen: " + songLen + " songPos: " + songPos + " statusId: " + statusId);
            mDoCallbackAvrcp.retAvrcp13PlayStatus(songLen, songPos, statusId);
        }

        @Override
        public void onAvrcp13EventPlaybackStatusChanged(byte statusId)
                throws RemoteException {
            Log.v(TAG, "onAvrcp13EventPlaybackStatusChanged() statusId: " + statusId);
            mDoCallbackAvrcp.onAvrcp13EventPlaybackStatusChanged(statusId);
            switch (statusId) {
                case NfDef.AVRCP_PLAYING_STATUS_ID_STOPPED:
                    Log.v(TAG, "[AVRCP_PLAYING_STATUS_ID_STOPPED]");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_PLAYING:
                    Log.v(TAG, "[AVRCP_PLAYING_STATUS_ID_PLAYING]");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_PAUSED:
                    Log.v(TAG, "[AVRCP_PLAYING_STATUS_ID_PAUSED]");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_FWD_SEEK:
                    Log.v(TAG, "[AVRCP_PLAYING_STATUS_ID_FWD_SEEK]");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_REW_SEEK:
                    Log.v(TAG, "[AVRCP_PLAYING_STATUS_ID_REW_SEEK]");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_ERROR:
                    Log.v(TAG, "[AVRCP_PLAYING_STATUS_ID_ERROR]");
                    break;
            }
        }

        @Override
        public void onAvrcp13EventTrackChanged(long elementId)
                throws RemoteException {
            Log.v(TAG, "onAvrcp13EventTrackChanged() elemendId: " + elementId);
            mCommandAvrcp.reqAvrcp13GetElementAttributesPlaying();
            mDoCallbackAvrcp.onAvrcp13EventTrackChanged(elementId);
        }

        @Override
        public void onAvrcp13EventTrackReachedEnd() throws RemoteException {
            Log.v(TAG, "onAvrcp13EventTrackReachedEnd()");
            mDoCallbackAvrcp.onAvrcp13EventTrackReachedEnd();
        }

        @Override
        public void onAvrcp13EventTrackReachedStart() throws RemoteException {
            Log.v(TAG, "onAvrcp13EventTrackReachedStart()");
            mDoCallbackAvrcp.onAvrcp13EventTrackReachedStart();
        }

        @Override
        public void onAvrcp13EventPlaybackPosChanged(long songPos)
                throws RemoteException {
            Log.v(TAG, "onAvrcp13EventPlaybackPosChanged() songPos: " + songPos);
            mDoCallbackAvrcp.onAvrcp13EventPlaybackPosChanged(songPos);
        }

        @Override
        public void onAvrcp13EventBatteryStatusChanged(byte statusId)
                throws RemoteException {
            Log.v(TAG, "onAvrcp13EventBatteryStatusChanged() statusId: " + statusId);
            mDoCallbackAvrcp.onAvrcp13EventBatteryStatusChanged(statusId);
        }

        @Override
        public void onAvrcp13EventSystemStatusChanged(byte statusId)
                throws RemoteException {
            Log.v(TAG, "onAvrcp13EventSystemStatusChanged() statusId: " + statusId);
            mDoCallbackAvrcp.onAvrcp13EventSystemStatusChanged(statusId);
        }

        @Override
        public void onAvrcp13EventPlayerSettingChanged(byte[] attributeIds,
                                                       byte[] valueIds) throws RemoteException {
            Log.v(TAG, "onAvrcp13EventPlayerSettingChanged()");
            mDoCallbackAvrcp.onAvrcp13EventPlayerSettingChanged(attributeIds, valueIds);
        }

        @Override
        public void onAvrcp14EventNowPlayingContentChanged() throws RemoteException {
            Log.v(TAG, "onAvrcp14EventNowPlayingContentChanged()");
            mDoCallbackAvrcp.onAvrcp14EventNowPlayingContentChanged();
        }

        @Override
        public void onAvrcp14EventAvailablePlayerChanged() throws RemoteException {
            Log.v(TAG, "onAvrcp14EventAvailablePlayerChanged()");
            mDoCallbackAvrcp.onAvrcp14EventAvailablePlayerChanged();
        }

        @Override
        public void onAvrcp14EventAddressedPlayerChanged(int playerId,
                                                         int uidCounter) throws RemoteException {
            Log.v(TAG, "onAvrcp14EventAddressedPlayerChanged() playerId: " + playerId + " uidCounter: " + uidCounter);
            mDoCallbackAvrcp.onAvrcp14EventAddressedPlayerChanged(playerId, uidCounter);
        }

        @Override
        public void onAvrcp14EventUidsChanged(int uidCounter)
                throws RemoteException {
            Log.v(TAG, "onAvrcp14EventUidsChanged() uidCounter: " + uidCounter);
            mDoCallbackAvrcp.onAvrcp14EventUidsChanged(uidCounter);
        }

        @Override
        public void onAvrcp14EventVolumeChanged(byte volume) throws RemoteException {
            Log.v(TAG, "onAvrcp14EventVolumeChanged() volume: " + volume);
            mDoCallbackAvrcp.onAvrcp14EventVolumeChanged(volume);
        }

        @Override
        public void retAvrcp14SetAddressedPlayerSuccess() throws RemoteException {
            Log.v(TAG, "retAvrcp14SetAddressedPlayerSuccess()");
            mDoCallbackAvrcp.retAvrcp14SetAddressedPlayerSuccess();
        }

        @Override
        public void retAvrcp14SetBrowsedPlayerSuccess(String[] path,
                                                      int uidCounter, long itemCount) throws RemoteException {
            String p = "";
            for (int i = 0; i < path.length; i++) {
                p += path[i];
            }
            Log.v(TAG, "retAvrcp14SetBrowsedPlayerSuccess() path: " + p + " uidCounter: " + uidCounter + " itemCount: " + itemCount);
            mDoCallbackAvrcp.retAvrcp14SetBrowsedPlayerSuccess(path, uidCounter, itemCount);
        }

        @Override
        public void retAvrcp14FolderItems(int uidCounter, long itemCount)
                throws RemoteException {
            Log.v(TAG, "retAvrcp14FolderItems() uidCounter: " + uidCounter + " itemCount: " + itemCount);
            mDoCallbackAvrcp.retAvrcp14FolderItems(uidCounter, itemCount);
        }

        @Override
        public void retAvrcp14MediaItems(int uidCounter, long itemCount)
                throws RemoteException {
            Log.v(TAG, "retAvrcp14MediaItems() uidCounter: " + uidCounter + " itemCount: " + itemCount);
            mDoCallbackAvrcp.retAvrcp14MediaItems(uidCounter, itemCount);
        }

        @Override
        public void retAvrcp14ChangePathSuccess(long itemCount)
                throws RemoteException {
            Log.v(TAG, "retAvrcp14ChangePathSuccess() itemCount: " + itemCount);
            mDoCallbackAvrcp.retAvrcp14ChangePathSuccess(itemCount);
        }

        @Override
        public void retAvrcp14ItemAttributes(int[] metadataAtrributeIds,
                                             String[] texts) throws RemoteException {
            Log.v(TAG, "retAvrcp14ItemAttributes()");
            mDoCallbackAvrcp.retAvrcp14ItemAttributes(metadataAtrributeIds, texts);
        }

        @Override
        public void retAvrcp14PlaySelectedItemSuccess() throws RemoteException {
            Log.v(TAG, "retAvrcp14PlaySelectedItemSuccess()");
            mDoCallbackAvrcp.retAvrcp14PlaySelectedItemSuccess();
        }

        @Override
        public void retAvrcp14SearchResult(int uidCounter, long itemCount)
                throws RemoteException {
            Log.v(TAG, "retAvrcp14SearchResult() uidCounter: " + uidCounter + " itemCount: " + itemCount);
            mDoCallbackAvrcp.retAvrcp14SearchResult(uidCounter, itemCount);
        }

        @Override
        public void retAvrcp14AddToNowPlayingSuccess() throws RemoteException {
            Log.v(TAG, "retAvrcp14AddToNowPlayingSuccess()");
            mDoCallbackAvrcp.retAvrcp14AddToNowPlayingSuccess();
        }

        @Override
        public void retAvrcp14SetAbsoluteVolumeSuccess(byte volume)
                throws RemoteException {
            Log.v(TAG, "retAvrcp14SetAbsoluteVolumeSuccess() volume: " + volume);
            mDoCallbackAvrcp.retAvrcp14SetAbsoluteVolumeSuccess(volume);
        }

        @Override
        public void onAvrcpErrorResponse(int opId, int reason, byte eventId)
                throws RemoteException {
            Log.v(TAG, "onAvrcpErrorResponse() opId: " + opId + " reason: " + reason + " eventId: " + eventId);
            mDoCallbackAvrcp.onAvrcpErrorResponse(opId, reason, eventId);
        }

        @Override
        public void onAvrcp13RegisterEventWatcherSuccess(byte eventId) throws RemoteException {
            Log.v(TAG, "onAvrcp13RegisterEventWatcherSuccess() eventId: " + eventId);
            mDoCallbackAvrcp.onAvrcp13RegisterEventWatcherSuccess(eventId);

        }

        @Override
        public void onAvrcp13RegisterEventWatcherFail(byte eventId) throws RemoteException {
            Log.v(TAG, "onAvrcp13RegisterEventWatcherFail() eventId: " + eventId);
            mDoCallbackAvrcp.onAvrcp13RegisterEventWatcherFail(eventId);

        }
    };

    /*
     * Pbap Callback
     *
     */
    private INfCallbackPbap mCallbackPbap = new INfCallbackPbap.Stub() {

        @Override
        public void onPbapServiceReady() throws RemoteException {
            Log.v(TAG, "onPbapServiceReady()");
            mDoCallbackPbap.onPbapServiceReady();
        }

        @Override
        public void onPbapStateChanged(String address, int prevState,
                                       int newState, int reason, int counts) throws RemoteException {
            Log.v(TAG, "onPbapStateChanged() " + address + " state: " + prevState + "->" + newState + " reason: " + reason + " counts: " + counts);
            if (newState == NfDef.STATE_READY) {
                Log.e(TAG, "Piggy Check testCount: " + testCount);
                testCount = 0;
            }
            mDoCallbackPbap.onPbapStateChanged(address, prevState, newState, reason, counts);
        }

        @Override
        public void retPbapDownloadedContact(NfPbapContact contact) throws RemoteException {
            Log.v(TAG, "retPbapDownloadedContact()");
            testCount++;
            mDoCallbackPbap.retPbapDownloadedContact(contact);
        }

        @Override
        public void retPbapDownloadedCallLog(String address, String firstName, String middleName,
                                             String lastName, String number, int type, String timestamp) throws RemoteException {
            Log.v(TAG, "retPbapDownloadedCallLog() " + address + " lastName: " + lastName + " (" + type + ")");
            mDoCallbackPbap.retPbapDownloadedCallLog(address, firstName, middleName, lastName, number, type, timestamp);
        }

        @Override
        public void onPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts) throws RemoteException {
            Log.v(TAG, "onPbapDownloadNotify() " + address + " storage: " + storage + " downloaded: " + downloadedContacts + "/" + totalContacts);
            mDoCallbackPbap.onPbapDownloadNotify(address, storage, totalContacts, downloadedContacts);
        }

        @Override
        public void retPbapDatabaseQueryNameByNumber(String address,
                                                     String target, String name, boolean isSuccess)
                throws RemoteException {
            Log.v(TAG, "retPbapDatabaseQueryNameByNumber() " + address + " target: " + target + " name: " + name + " isSuccess: " + isSuccess);
            mDoCallbackPbap.retPbapDatabaseQueryNameByNumber(address, target, name, isSuccess);
            mDoCallbackHfp.retPbapDatabaseQueryNameByNumber(address, target, name, isSuccess);
        }

        @Override
        public void retPbapDatabaseQueryNameByPartialNumber(String address,
                                                            String target, String[] names, String[] numbers,
                                                            boolean isSuccess) throws RemoteException {
            Log.v(TAG, "retPbapDatabaseQueryNameByPartialNumber() " + address + " target: " + target + " isSuccess: " + isSuccess);
            mDoCallbackPbap.retPbapDatabaseQueryNameByPartialNumber(address, target, names, numbers, isSuccess);
        }

        @Override
        public void retPbapDatabaseAvailable(String address)
                throws RemoteException {
            Log.v(TAG, "retPbapDatabaseAvailable() " + address);
            mDoCallbackPbap.retPbapDatabaseAvailable(address);
        }

        @Override
        public void retPbapDeleteDatabaseByAddressCompleted(String address,
                                                            boolean isSuccess) throws RemoteException {
            Log.v(TAG, "retPbapDeleteDatabaseByAddressCompleted() " + address + " isSuccess: " + isSuccess);
            mDoCallbackPbap.retPbapDeleteDatabaseByAddressCompleted(address, isSuccess);
        }

        @Override
        public void retPbapCleanDatabaseCompleted(boolean isSuccess)
                throws RemoteException {
            Log.v(TAG, "retPbapCleanDatabaseCompleted() isSuccess: " + isSuccess);
            mDoCallbackPbap.retPbapCleanDatabaseCompleted(isSuccess);
        }

    };

    /*
     * Bluetooth callback
     *
     */
    private INfCallbackBluetooth mCallbackBluetooth = new INfCallbackBluetooth.Stub() {

        @Override
        public void onBluetoothServiceReady() throws RemoteException {
            Log.v(TAG, "onBluetoothServiceReady()");
            mDoCallbackBluetooth.onBluetoothServiceReady();
        }

        @Override
        public void onAdapterStateChanged(int prevState, int newState) throws RemoteException {
            Log.v(TAG, "onAdapterStateChanged() state: " + prevState + "->" + newState);
            mDoCallbackBluetooth.onAdapterStateChanged(prevState, newState);

        }

        @Override
        public void onAdapterDiscoverableModeChanged(int prevState, int newState)
                throws RemoteException {
            Log.v(TAG, "onAdapterDiscoverableModeChanged() state: " + prevState + "->" + newState);
            mDoCallbackBluetooth.onAdapterDiscoverableModeChanged(prevState, newState);
        }

        @Override
        public void onAdapterDiscoveryStarted() throws RemoteException {
            Log.v(TAG, "onAdapterDiscoveryStarted()");
            mDoCallbackBluetooth.onAdapterDiscoveryStarted();
        }

        @Override
        public void onAdapterDiscoveryFinished() throws RemoteException {
            Log.v(TAG, "onAdapterDiscoveryFinished()");
            mDoCallbackBluetooth.onAdapterDiscoveryFinished();
        }

        @Override
        public void retPairedDevices(int elements, String[] address, String[] name,
                                     int[] supportProfile, byte[] category) throws RemoteException {
            Log.v(TAG, "retPairedDevices() elements: " + elements);
            mDoCallbackBluetooth.retPairedDevices(elements, address, name, supportProfile, category);
        }

        @Override
        public void onDeviceFound(String address, String name, byte category)
                throws RemoteException {
            Log.v(TAG, "onDeviceFound() " + address + " name: " + name);
            mDoCallbackBluetooth.onDeviceFound(address, name, category);
        }

        @Override
        public void onDeviceBondStateChanged(String address, String name, int prevState,
                                             int newState) throws RemoteException {
            Log.v(TAG, "onDeviceBondStateChanged() " + address + " name: " + name + " state: " + prevState + "->" + newState);
            mDoCallbackBluetooth.onDeviceBondStateChanged(address, name, prevState, newState);

        }

        @Override
        public void onDeviceUuidsUpdated(String address, String name, int supportProfile)
                throws RemoteException {
            Log.v(TAG, "onDeviceUuidsUpdated() " + address + " name: " + name + " supportProfile: " + supportProfile);
            mDoCallbackBluetooth.onDeviceUuidsUpdated(address, name, supportProfile);
        }

        @Override
        public void onLocalAdapterNameChanged(String name) throws RemoteException {
            Log.v(TAG, "onLocalAdapterNameChanged() " + name);
            mDoCallbackBluetooth.onLocalAdapterNameChanged(name);
        }

        @Override
        public void onDeviceOutOfRange(String address) throws RemoteException {
            Log.v(TAG, "onDeviceOutOfRange() " + address);
            mDoCallbackBluetooth.onDeviceOutOfRange(address);
        }

        @Override
        public void onDeviceAclDisconnected(String address) throws RemoteException {
            Log.v(TAG, "onDeviceAclDisconnected() " + address);

        }

        @Override
        public void onBtRoleModeChanged(int mode) throws RemoteException {
            Log.v(TAG, "onBtRoleModeChanged() " + mode);
            mDoCallbackBluetooth.onBtRoleModeChanged(mode);
        }

        @Override
        public void onBtAutoConnectStateChanged(String address, int prevState, int newState)
                throws RemoteException {
            Log.v(TAG, "onBtAutoConnectStateChanged() " + address + " state: " + prevState + " -> " + newState);
            mDoCallbackBluetooth.onBtAutoConnectStateChanged(address, prevState, newState);
        }

    };

    private void dumpClassMethod(Class c) {
        for (Method method : c.getDeclaredMethods()) {
            Log.e(TAG, "Method name: " + method.getName());
        }
    }

}
