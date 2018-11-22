package com.semisky.bluetoothproject.model;

import android.util.Log;

import com.nforetek.bt.res.NfDef;
import com.semisky.bluetoothproject.presenter.viewInterface.DeviceSearchInterface;
import com.semisky.bluetoothproject.responseinterface.BtDeviceSearchResponse;
import com.semisky.bluetoothproject.utils.Logger;

import java.util.Arrays;

import static com.nforetek.bt.res.NfDef.BOND_BONDED;
import static com.nforetek.bt.res.NfDef.BOND_BONDING;
import static com.nforetek.bt.res.NfDef.BOND_NONE;

/**
 * Created by chenhongrui on 2018/8/8
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtDeviceSearchModel implements BtDeviceSearchResponse {

    private static final String TAG = Logger.makeTagLog(BtDeviceSearchModel.class);

    private BtDeviceSearchModel() {
    }

    private static class BtDeviceSearchModellHolder {
        private static final BtDeviceSearchModel instance = new BtDeviceSearchModel();
    }

    public static BtDeviceSearchModel getInstance() {
        return BtDeviceSearchModellHolder.instance;
    }

    private DeviceSearchInterface disposableObserver;

    public void setListener(DeviceSearchInterface listener) {
        this.disposableObserver = listener;
    }

    public void unSetListener() {
        disposableObserver = null;
    }

    @Override
    public void onAdapterStateChanged(int prevState, int newState) {
        Logger.d(TAG, "onAdapterStateChanged: prevState " + " -> " + prevState + " newState " + newState);
        if (disposableObserver != null) {
            if (newState == NfDef.BT_STATE_ON) {
                //蓝牙已开启
                Logger.d(TAG, "onAdapterStateChanged:BT_STATE_ON ");
                disposableObserver.btStatusOpen();
            } else if (newState == NfDef.BT_STATE_OFF) {
                //蓝牙已关闭
                Logger.d(TAG, "onAdapterStateChanged:BT_STATE_OFF ");
                disposableObserver.btStatusClose();
            } else if (newState == NfDef.BT_STATE_TURNING_ON) {
                //正在开启蓝牙
                Logger.d(TAG, "onAdapterStateChanged:BT_STATE_TURNING_ON ");
                disposableObserver.btStatusOpening();
            } else if (newState == NfDef.BT_STATE_TURNING_OFF) {
                //正在关闭蓝牙
                Logger.d(TAG, "onAdapterStateChanged:BT_STATE_TURNING_OFF ");
                disposableObserver.btStatusClosing();
            }
        }
    }

    @Override
    public void onAdapterDiscoveryStarted() {
        if (disposableObserver != null) {
            Logger.d(TAG, "onAdapterDiscoveryStarted: ");
            disposableObserver.onAdapterDiscoveryStarted();
        }
    }

    @Override
    public void onAdapterDiscoveryFinished() {
        if (disposableObserver != null) {
            Logger.d(TAG, "onAdapterDiscoveryFinished: ");
            disposableObserver.onAdapterDiscoveryFinished();
        }
    }

    @Override
    public void onDeviceFound(String address, final String name, byte category) {
        if (disposableObserver != null) {
            Logger.d(TAG, "onDeviceFound:address " + address + " name " + name + " category " + category);
            disposableObserver.onDeviceFound(address, name, category);
        }
    }

    /**
     * <br>State changed from <b>BOND_NONE</b> to <b>BOND_BONDING</b> means device is pairing.
     * <br>State changed from <b>BOND_BONDING</b> to <b>BOND_BONDED</b> means device is paired.
     * <br>State changed from <b>BOND_BONDING</b> to <b>BOND_NONE</b> means device is pair failed.
     * <br>State changed from <b>BOND_BONDED</b> to <b>BOND_NONE</b> means device is unpaired.
     */
    @Override
    public void onDeviceBondStateChanged(String address, String name, int prevState, int newState) {
        Logger.d(TAG, "onDeviceBondStateChanged: prevState " + prevState + " -> newState " + newState);
        if (prevState == BOND_NONE && newState == BOND_BONDING) {
            Logger.d(TAG, "onDeviceBondStateChanged: 设备开始配对");
        } else if (prevState == BOND_BONDING && newState == BOND_BONDED) {
            Logger.d(TAG, "onDeviceBondStateChanged: 配对成功");
        } else if (prevState == BOND_BONDING && newState == BOND_NONE) {
            Logger.d(TAG, "onDeviceBondStateChanged: 配对失败");
        } else if (prevState == BOND_BONDED && newState == BOND_NONE) {
            Logger.d(TAG, "onDeviceBondStateChanged: 无法配对");
        }
    }

    @Override
    public void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category) {
        Logger.d(TAG, "retPairedDevices: elements " + elements + " address " + Arrays.toString(address)
                + " name " + Arrays.toString(name) + " category " + Arrays.toString(category));
        if (elements > 0) {
            if (disposableObserver != null) {
                disposableObserver.onPairedDevices(elements, address, name, supportProfile, category);
            }
        }
    }

    public void setBtOpen(boolean isOpen) {
        Log.d(TAG, "setBtOpen: ");
        if (disposableObserver != null) {
            disposableObserver.isBtConnect(isOpen);
        }
    }
}
