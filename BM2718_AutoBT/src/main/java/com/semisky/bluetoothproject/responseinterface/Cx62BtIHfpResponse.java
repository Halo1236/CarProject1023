package com.semisky.bluetoothproject.responseinterface;

import com.nforetek.bt.aidl.NfHfpClientCall;

/**
 * Created by luoyin on 17-10-12.
 */

public interface Cx62BtIHfpResponse {

    /**
     * 蓝牙连接状态回调
     *
     * @param address   address
     * @param prevState STATE_NOT_INITIALIZED</b> (int) 100
     *                  STATE_READY</b>			  (int) 110
     *                  STATE_CONNECTING</b>	  (int) 120
     *                  STATE_CONNECTED</b>		  (int) 140
     * @param newState
     */
    void onHfpStateChanged(String address, int prevState, int newState);

    /**
     * 蓝牙电话call回调
     *
     * @param address address
     * @param call    call
     */
    void onHfpCallChanged(String address, NfHfpClientCall call);

    void onHfpAudioStateChanged(String address, int prevState, int newState);

    void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue);

    void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength);
}
