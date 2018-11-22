package com.semisky.bluetoothproject.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.res.NfDef;
import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.entity.CallLogEntity;
import com.semisky.bluetoothproject.entity.ContactsEntity;
import com.semisky.bluetoothproject.entity.DevicesListEntity;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;
import com.semisky.bluetoothproject.presenter.viewInterface.DeviceSearchInterface;
import com.semisky.bluetoothproject.presenter.viewInterface.HFPAudioStateInterface;
import com.semisky.bluetoothproject.presenter.viewInterface.HFPStatusInterface;
import com.semisky.bluetoothproject.responseinterface.Cx62BtIHfpResponse;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.util.Log.d;

/**
 * Created by chenhongrui on 2018/7/27
 * <p>
 * 内容摘要: 处理蓝牙连接状态，HFP相关
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtHfpModel implements Cx62BtIHfpResponse {

    private static String TAG = Logger.makeTagLog(BtHfpModel.class);
    private BtStatusModel btStatusModel;
    private Context context;
    private BtBaseUiCommandMethod btBaseUiCommandMethod;
    private Handler handler;
    private String address;

    private BtHfpModel() {
        this.context = BtApplication.getContext();
        btStatusModel = BtStatusModel.getInstance();
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
        handler = new Handler();
    }

    private static class BtHfpModelHolder {
        @SuppressLint("StaticFieldLeak")
        private static final BtHfpModel instance = new BtHfpModel();
    }

    public static BtHfpModel getInstance() {
        return BtHfpModelHolder.instance;
    }

    private HFPStatusInterface hfpStatusInterface;

    public void setHFPStatusInterface(HFPStatusInterface hfpStatusInterface) {
        this.hfpStatusInterface = hfpStatusInterface;
    }

    private HFPAudioStateInterface hfpAudioStateInterface;

    public void setHFPAudioStateInterface(HFPAudioStateInterface hfpAudioStateInterface) {
        this.hfpAudioStateInterface = hfpAudioStateInterface;
    }

    private DeviceSearchInterface disposableObserver;

    public void setListener(DeviceSearchInterface listener) {
        this.disposableObserver = listener;
    }

    public void unSetListener() {
        disposableObserver = null;
    }

    /**
     * 蓝牙连接状态回调
     *
     * @param address   address
     * @param prevState prevState
     * @param newState  newState
     *                  STATE_NOT_INITIALIZED</b> (int) 100
     *                  STATE_READY</b>			  (int) 110
     *                  STATE_CONNECTING</b>	  (int) 120
     *                  STATE_CONNECTED</b>		  (int) 140
     */
    @Override
    public void onHfpStateChanged(String address, int prevState, int newState) {
        if (prevState == NfDef.STATE_CONNECTED && newState == NfDef.STATE_DISCONNECTING) {
            informDisconnecting(address);
        } else if (prevState != NfDef.STATE_CONNECTED && newState == NfDef.STATE_CONNECTED) {
            Logger.d(TAG, "onHfpStateChanged: 已连接");
            informConnected(address);
        } else if (prevState == NfDef.STATE_READY && newState == NfDef.STATE_CONNECTING) {
            informConnecting(address);
        } else if (prevState > NfDef.STATE_READY && newState == NfDef.STATE_READY) {
            Logger.d(TAG, "onHfpStateChanged:已断开 ");
            informDisconnect(address);
        }
    }

    /**
     * 断开中
     */
    private void informDisconnecting(String address) {
        if (hfpStatusInterface != null) {
            hfpStatusInterface.stateDisconnecting(address);
        }

        if (disposableObserver != null) {
            disposableObserver.stateDisconnecting(address);
        }
    }

    /**
     * 已连接
     */
    private void informConnected(String address) {
        if (hfpStatusInterface != null) {
            hfpStatusInterface.stateConnected(address);
        }

        this.address = address;
        new Thread(checkConnect).start();
        initStatus(address);
        saveDeviceInformation(address);
    }

    /**
     * 连接中
     */
    private void informConnecting(String address) {
        if (hfpStatusInterface != null) {
            hfpStatusInterface.stateConnecting(address);
        }

        if (disposableObserver != null) {
            disposableObserver.stateConnecting(address);
        }
    }

    /**
     * 已断开
     */
    private void informDisconnect(String address) {
        if (hfpStatusInterface != null) {
            hfpStatusInterface.stateDisconnect(address);
        }

        if (disposableObserver != null) {
            disposableObserver.stateDisconnect(address);
        }
    }

    private Runnable checkConnect = new Runnable() {
        @Override
        public void run() {
            boolean isReally = btBaseUiCommandMethod.checkBtServiceConnect();
            if (isReally) {
                removeCallBackRunnable();
                if (disposableObserver != null) {
                    disposableObserver.stateConnected(address);
                }
                Logger.d(TAG, "checkBtServiceConnect: 连接成功");
//                BtCallStatusModel.getInstance().recoverCallView(); 7822 暂时废除
                checkACCStatus();
            } else {
                Logger.d(TAG, "checkBtServiceConnect: 判断连接中..");
                handler.postDelayed(checkConnect, 100);
            }
        }
    };

    /**
     * 1.如果蓝牙连接上了，判断当前是否是ACC OFF状态，则断开连接
     * 2.判断是否需要恢复蓝牙音乐
     */
    private void checkACCStatus() {
        if (btStatusModel.isAccOff()) {
            btBaseUiCommandMethod.reqBtDisconnectAll();
            Logger.d(TAG, "checkACCStatus: 当前ACC OFF状态");
            return;
        }

        if (btStatusModel.isAccOnRecoverMusic()) {
            btStatusModel.setAccOnRecoverMusic(false);

            //如果蓝牙拥有音频焦点。就恢复播放，不考虑暂停情况
            if (BtMusicAudioFocusModel.getINSTANCE().isHasAudioFocus()) {
                Logger.d(TAG, "checkACCStatus: 当前拥有音频焦点，播放蓝牙音乐");
                btBaseUiCommandMethod.play();
            }

        }
    }


    public void removeCallBackRunnable() {
        Logger.d(TAG, "removeCallBackRunnable: ");
        handler.removeCallbacks(checkConnect);
    }


    /**
     * 保存设备信息
     */
    private void saveDeviceInformation(String address) {
        String localName = btBaseUiCommandMethod.getBtRemoteDeviceName(address);
        ArrayList<DevicesListEntity> pairedDevicesData =
                BtSPUtil.getInstance().getPairedDevicesData(context);
        if (pairedDevicesData.size() > 0) {
            for (int i = 0; i < pairedDevicesData.size(); i++) {
                if (pairedDevicesData.get(i).getAddress().equals(address)) {
                    pairedDevicesData.remove(i);
                }
            }
            pairedDevicesData.add(new DevicesListEntity(
                    address, localName, DevicesListEntity.DeviceConnectStatus.CONNECTED, System.currentTimeMillis()));
            pairedDevicesData = pairedDevicesDataSort(pairedDevicesData);
            BtSPUtil.getInstance().putPairedDevicesData(context, pairedDevicesData);
        } else {
            ArrayList<DevicesListEntity> devicesListEntities = new ArrayList<>();
            devicesListEntities.add(new DevicesListEntity(
                    address, localName, DevicesListEntity.DeviceConnectStatus.CONNECTED, System.currentTimeMillis()));
            BtSPUtil.getInstance().putPairedDevicesData(context, devicesListEntities);
        }
    }

    private ArrayList<DevicesListEntity> pairedDevicesDataSort(ArrayList<DevicesListEntity> pairedDevicesData) {

        for (DevicesListEntity devicesListEntity : pairedDevicesData) {
            d(TAG, "排序前: " + devicesListEntity.getDeviceName() + "---" + devicesListEntity.getTimestamp());
        }

        d(TAG, "----------------------: ");

        if (pairedDevicesData.size() > 0) {
            Collections.sort(pairedDevicesData, new Comparator<DevicesListEntity>() {
                @Override
                public int compare(DevicesListEntity o1, DevicesListEntity o2) {
                    //按照时间戳进行降序
                    return Long.compare(o2.getTimestamp(), o1.getTimestamp());
                }
            });
        }

        if (pairedDevicesData.size() > 6) {
            for (int i = 0; i < pairedDevicesData.size(); i++) {
                if (i > 5) {
                    pairedDevicesData.remove(i);
                    i--;
                }
            }
        }

        d(TAG, "----------------------: ");

        for (DevicesListEntity devicesListEntity : pairedDevicesData) {
            d(TAG, "排序后: " + devicesListEntity.getDeviceName() + "---" + devicesListEntity.getTimestamp());
        }

        return pairedDevicesData;
    }

    private void initStatus(String address) {
        boolean newDevices = BtSPUtil.getInstance().isNewDevices(context, address);
        Logger.d(TAG, "stateConnected: newDevices " + newDevices);
        if (newDevices) {
            btStatusModel.setNewDevice(true);
        } else {
            btStatusModel.setNewDevice(false);
        }

        btStatusModel.setBTConnect(true);
        BtSPUtil.getInstance().setSyncContactsStateSP(context, false);
        BtSPUtil.getInstance().setSyncCallLogStateSP(context, false);
        LitePal.deleteAllAsync(CallLogEntity.class);
        LitePal.deleteAllAsync(ContactsEntity.class);
        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.PERMISSION);

        BtSPUtil.getInstance().setLastConnectAddressSP(context, address);
    }

    /**
     * 监听蓝牙电话状态 来去电挂断
     */
    @Override
    public void onHfpCallChanged(String address, NfHfpClientCall call) {
        Log.d(TAG, "onHfpCallChanged: " + address + " NfHfpClientCall " + call);
    }

    @Override
    public void onHfpAudioStateChanged(String address, int prevState, int newState) {
        Logger.d(TAG, "onHfpAudioStateChanged: " + address + " prevState " + prevState + " newState " + newState);
        //140 -> 110 私密模式
        //120 -> 140 免提模式
        if (prevState == NfDef.STATE_CONNECTED && newState == NfDef.STATE_READY) {
            Logger.d(TAG, "onHfpAudioStateChanged: 私密模式");
            if (hfpAudioStateInterface != null) {
                hfpAudioStateInterface.privateStatus();
            }
        } else if (prevState == NfDef.STATE_CONNECTING && newState == NfDef.STATE_CONNECTED) {
            Logger.d(TAG, "onHfpAudioStateChanged: 免提模式");
            if (hfpAudioStateInterface != null) {
                hfpAudioStateInterface.handFreeStatus();
            }
        }
    }

    @Override
    public void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue,
                                            int minValue) {
        Log.d(TAG, "onHfpRemoteBatteryIndicator: " + address + " currentValue " + currentValue +
                " maxValue " + maxValue + " minValue " + minValue);
    }

    @Override
    public void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength,
                                          int minStrength) {
        Log.d(TAG, "onHfpRemoteSignalStrength: " + address + " currentStrength " + currentStrength +
                " maxStrength " + maxStrength + " minStrength " + minStrength);

    }

}
