package com.semisky.bluetoothproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.semisky.auto.bm2718.aidl.IBtAutoTestControl;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;

public class BtAutoTestService extends Service {

    private BtBaseUiCommandMethod btBaseUiCommandMethod;

    private BtAutoTestControl btAutoTestControl = new BtAutoTestControl();

    @Override
    public void onCreate() {
        super.onCreate();
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return btAutoTestControl;
    }

    private class BtAutoTestControl extends IBtAutoTestControl.Stub {

        @Override
        public void musicLast() throws RemoteException {
            btBaseUiCommandMethod.prev();
        }

        @Override
        public void musicNext() throws RemoteException {
            btBaseUiCommandMethod.next();
        }

        @Override
        public void musicPlay() throws RemoteException {
            btBaseUiCommandMethod.play();
        }

        @Override
        public void musicPause() throws RemoteException {
            btBaseUiCommandMethod.pause();
        }

        @Override
        public void callAnswer() throws RemoteException {
            btBaseUiCommandMethod.reqHfpAnswerCall(0);
        }

        @Override
        public void callHangup() throws RemoteException {
            btBaseUiCommandMethod.reqHfpTerminateCurrentCall();
        }

        @Override
        public void callPhone(String number) throws RemoteException {
            btBaseUiCommandMethod.reqHfpDialCall(number);
        }

        @Override
        public String getBTAddress() throws RemoteException {
            return btBaseUiCommandMethod.getHfpConnectedAddress();
        }
    }
}
