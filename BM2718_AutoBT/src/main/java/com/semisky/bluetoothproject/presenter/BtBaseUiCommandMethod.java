package com.semisky.bluetoothproject.presenter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.aidl.UiCommand;
import com.nforetek.bt.res.NfDef;
import com.semisky.bluetoothproject.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.CANCEL_BT_DISCOVERY;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.CHECK_PERMISSION;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_AVRCP13_GET_PLAYING;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_AVRCP_13_GET_PLAY_STATUS;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_CONNECT_HFP_A2DP;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_DOWNLOAD_CALLLOG;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_DOWNLOAD_CONNECT;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_MUSIC_LAST;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_MUSIC_NEXT;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_MUSIC_PAUSE;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_MUSIC_PLAY;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_ONCE_CALLLOG;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_PAIRE_DDEVICES;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_SET_BREAK_CONNECT;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_BT_UN_PAIR;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_HFP_ANSWER_CALL;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_HFP_DIAL_CALL;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_HFP_TERMINATE_CURRENT_CALL;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.REQ_PBAP_DOWNLOAD_INTERRUPT;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.SET_AUTO_CONNECT;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.SET_BT_ENABLE;
import static com.semisky.bluetoothproject.constant.BtConstant.CommandMethod.START_BT_DISCOVERY;
import static com.semisky.bluetoothproject.constant.BtConstant.SERIALIZABLE_DATA;

/**
 * Created by chenhongrui on 2018/8/6
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：TODO 计划弃用HandlerThread
 * 修改日期
 */
public class BtBaseUiCommandMethod extends HandlerThread {

    private final String TAG = Logger.makeTagLog(BtBaseUiCommandMethod.class);

    private UiCommand mCommand;

    private MethodHandler methodHandler;

    private volatile static BtBaseUiCommandMethod INSTANCE;

    private BtBaseUiCommandMethod(String name) {
        super(name);
    }

    public static BtBaseUiCommandMethod getInstance() {
        if (INSTANCE == null) {
            synchronized (BtBaseUiCommandMethod.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BtBaseUiCommandMethod("MethodHandler");
                }
            }
        }
        return INSTANCE;
    }

    public MethodHandler getMethodHandler() {
        if (methodHandler == null) {
            methodHandler = new MethodHandler(this.getLooper());
        }
        return methodHandler;
    }

    private int mProperty = NfDef.PBAP_PROPERTY_MASK_FN |
            NfDef.PBAP_PROPERTY_MASK_N |
            NfDef.PBAP_PROPERTY_MASK_TEL |
            NfDef.PBAP_PROPERTY_MASK_VERSION |
            NfDef.PBAP_PROPERTY_MASK_ADR |
            NfDef.PBAP_PROPERTY_MASK_EMAIL |
            NfDef.PBAP_PROPERTY_MASK_PHOTO |
            NfDef.PBAP_PROPERTY_MASK_ORG |
            NfDef.PBAP_PROPERTY_MASK_NICKNAME |
            NfDef.PBAP_PROPERTY_MASK_TIME_STAMP;

    public void setmCommand(UiCommand mCommand) {
        this.mCommand = mCommand;
    }

    private class MethodHandler extends Handler {

        MethodHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String address;
            switch (msg.what) {
                case START_BT_DISCOVERY:
                    startBtDiscovery();
                    break;
                case CANCEL_BT_DISCOVERY:
                    cancelBtDiscovery();
                    break;
                case REQ_HFP_DIAL_CALL:
                    String number = (String) msg.getData().getSerializable(SERIALIZABLE_DATA);
                    reqHfpDialCall(number);
                    break;
                case REQ_BT_CONNECT_HFP_A2DP:
                    address = (String) msg.getData().getSerializable(SERIALIZABLE_DATA);
//                    boolean status = reqBtPair(address);
//                    Log.d(TAG, "handleMessage: status " + status);
//                    if (!status) {
                    reqBtConnectHfpA2dp(address);
//                    }
                    break;
                case REQ_BT_UN_PAIR:
                    address = (String) msg.getData().getSerializable(SERIALIZABLE_DATA);
                    reqBtUnpair(address);
                    break;
                case REQ_BT_DOWNLOAD_CONNECT:
                    Logger.d(TAG, "REQ_BT_DOWNLOAD_CONNECT");
                    reqPbapDownloadRange(getHfpConnectedAddress(), NfDef.PBAP_STORAGE_PHONE_MEMORY,
                            mProperty, 0, 2000);
                    break;
                case REQ_BT_DOWNLOAD_CALLLOG:
                    reqPbapDownloadRange(getHfpConnectedAddress(), NfDef.PBAP_STORAGE_CALL_LOGS,
                            mProperty, 0, 2000);
                    break;
                case CHECK_PERMISSION:
                    //模拟请求一条数据，让手机端提示授予权限
                    reqPbapDownloadRange(getHfpConnectedAddress(), NfDef.PBAP_STORAGE_PHONE_MEMORY,
                            mProperty, 0, 1);
                    break;
                case REQ_BT_ONCE_CALLLOG:
                    reqPbapDownloadRange(getHfpConnectedAddress(), NfDef.PBAP_STORAGE_CALL_LOGS,
                            mProperty, 0, 1);
                    break;
                case REQ_BT_MUSIC_PLAY:
                    play();
                    break;
                case REQ_BT_MUSIC_PAUSE:
                    pause();
                    break;
                case REQ_BT_MUSIC_NEXT:
                    next();
                    break;
                case REQ_BT_MUSIC_LAST:
                    prev();
                    break;
                case REQ_BT_SET_BREAK_CONNECT:
                    reqBtDisconnectAll();
                    break;
                case SET_BT_ENABLE:
                    boolean enable = (boolean) msg.getData().getSerializable(SERIALIZABLE_DATA);
                    boolean btEnable = setBtEnable(enable);
                    Logger.d(TAG, "handleMessage: btEnable " + btEnable);
                    break;
                case REQ_BT_PAIRE_DDEVICES:
                    boolean devices = reqBtPairedDevices();
                    Logger.d(TAG, "handleMessage:devices " + devices);
                    break;
                case REQ_AVRCP13_GET_PLAYING:
                    reqAvrcp13GetElementAttributesPlaying();
                    break;
                case SET_AUTO_CONNECT:
                    setAutoConnect((boolean) msg.getData().getSerializable(SERIALIZABLE_DATA));
                    break;
                case REQ_HFP_TERMINATE_CURRENT_CALL:
                    reqHfpTerminateCurrentCall();
                    break;
                case REQ_HFP_ANSWER_CALL:
                    reqHfpAnswerCall(0);
                    break;
                case REQ_PBAP_DOWNLOAD_INTERRUPT:
                    reqPbapDownloadInterrupt();
                    break;
                case REQ_AVRCP_13_GET_PLAY_STATUS:
                    reqAvrcp13GetPlayStatus();
                    break;

                default:
                    break;
            }
        }
    }

    private boolean checkAutoBTService() {
        if (mCommand != null) {
            Logger.d(TAG, "checkAutoBTService: mCommand: " + mCommand);
            return true;
        } else {
            Logger.e(TAG, "checkAutoBTService: mCommand is null!!!!! ");
            return false;
        }
    }

    /**
     * A2DP是否已连接
     */
    public boolean isA2dpConnected() {
        try {
            boolean isA2dpConnected = (checkAutoBTService() && mCommand.isA2dpConnected());
            Logger.d(TAG, "isA2dpConnected=" + isA2dpConnected);
            return isA2dpConnected;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *
     */
    public boolean isBluetoothServiceReady() {
        try {
            boolean bluetoothServiceReady = mCommand.isBluetoothServiceReady();
            Logger.d(TAG, "isBluetoothServiceReady=" + bluetoothServiceReady);
            return bluetoothServiceReady;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * avrcp是否已连接
     */
    public boolean isAvrcpConnected() {
        try {
            boolean isAvrcpConnected = (checkAutoBTService() && mCommand.isAvrcpConnected());
            Logger.d(TAG, "isAvrcpConnected=" + isAvrcpConnected);
            return isAvrcpConnected;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * hfp是否已连接
     */
    public boolean isHfpConnected() {
        try {
            boolean isHfpConnected = (checkAutoBTService() && mCommand.isHfpConnected());
            Logger.d(TAG, "isHfpConnected=" + isHfpConnected);
            return isHfpConnected;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Pbap是否已连接
     */
    public boolean isPbapServiceReady() {
        try {
            boolean isPbapServiceReady = (checkAutoBTService() && mCommand.isPbapServiceReady());
            Logger.d(TAG, "isPbapServiceReady=" + isPbapServiceReady);
            return isPbapServiceReady;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断AVRCP HFP A2DP PBAP
     */
    public boolean checkBtServiceConnect() {
        boolean a2dpServiceReady = isA2dpConnected();
        boolean avrcpServiceReady = isAvrcpConnected();
        boolean pbapServiceReady = isPbapServiceReady();
        boolean hfpServiceReady = isHfpConnected();
        Log.d(TAG, "checkBtServiceConnect:a2dpServiceReady " + a2dpServiceReady);
        Log.d(TAG, "checkBtServiceConnect:avrcpServiceReady " + avrcpServiceReady);
        Log.d(TAG, "checkBtServiceConnect:pbapServiceReady " + pbapServiceReady);
        Log.d(TAG, "checkBtServiceConnect:hfpServiceReady " + hfpServiceReady);

        return a2dpServiceReady && avrcpServiceReady && pbapServiceReady && hfpServiceReady;
    }


    /**
     * 获取音乐信息
     */
    public void reqAvrcp13GetElementAttributesPlaying() {
        Logger.d(TAG, "reqAvrcp13GetElementAttributesPlaying");
        if (isAvrcpConnected()) {
            try {
                Log.d(TAG, "reqAvrcp13GetElementAttributesPlaying: " + mCommand.isAvrcpServiceReady());
                mCommand.reqAvrcp13GetElementAttributesPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取蓝牙播放状态
     */
    public void reqAvrcp13GetPlayStatus() {
        Logger.d(TAG, "REQ_AVRCP_13_GET_PLAY_STATUS");
        if (isAvrcpConnected()) {
            try {
                mCommand.reqAvrcp13GetPlayStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求拨打电话
     *
     * @param number 电话号码
     */
    public boolean reqHfpDialCall(String number) {
        Logger.d(TAG, "NumberQueryNameHandler-reqHfpDialCall-number=" + number);
        if (isHfpConnected()) {
            try {
                return mCommand.reqHfpDialCall(number);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 获取当前手机号码信息
     */
    public List<NfHfpClientCall> getHfpCallList() {
        Logger.d(TAG, "initBTStatus");
        if (isHfpConnected()) {
            try {
                return mCommand.getHfpCallList();
            } catch (RemoteException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    /**
     * @param flag 0:接听当前电话，1：接通第三方来电并保留原通话，切换通话，2：接通第三方来电并挂掉原通话
     */
    public boolean reqHfpAnswerCall(int flag) {
        Logger.d(TAG, "reqHfpAnswerCall");
        if (isHfpConnected()) {
            try {
                return mCommand.reqHfpAnswerCall(flag);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void setAutoConnect(boolean autoConnect) {
        Log.d(TAG, "setAutoConnect: " + autoConnect);
        if (autoConnect) {
            //打开自动连接
            setBtAutoConnect(NfDef.AUTO_CONNECT_WHEN_PAIRED | NfDef.AUTO_CONNECT_WHEN_OOR
                    | NfDef.AUTO_CONNECT_WHEN_BT_ON, 180);
        } else {
            //关闭自动连接
            setBtAutoConnect(0, 0);
        }
    }

    /**
     * 切换电话模式到车机端
     */
    public boolean reqHfpAudioTransferToCarkit() {
        Logger.d(TAG, "reqHfpAudioTransferToCarkit");
        if (isHfpConnected()) {
            try {
                return mCommand.reqHfpAudioTransferToCarkit();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 切换电话模式到手机端(私密模式)
     */
    public boolean reqHfpAudioTransferToPhone() {
        Logger.d(TAG, "reqHfpAudioTransferToPhone");
        if (isHfpConnected()) {
            try {
                return mCommand.reqHfpAudioTransferToPhone();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 三方通话则挂断第三方来电,拒接第三方来电
     */
    public boolean reqHfpRejectIncomingCall() {
        Logger.d(TAG, "reqHfpRejectIncomingCall");
        if (isHfpConnected()) {
            try {
                return mCommand.reqHfpRejectIncomingCall();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 获取当前电话声音状态
     */
    public int getHfpAudioConnectionState() {
        if (isHfpConnected()) {
            try {
                int hfpAudioConnectionState = mCommand.getHfpAudioConnectionState();
                Logger.d(TAG, "getHfpAudioConnectionState: " + hfpAudioConnectionState);
                return hfpAudioConnectionState;
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 挂断当前通话的电话
     */
    public boolean reqHfpTerminateCurrentCall() {
        Logger.d(TAG, "reqHfpTerminateCurrentCall");
        if (isHfpConnected()) {
            try {
                return mCommand.reqHfpTerminateCurrentCall();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 重播电话
     */
    public boolean reqHfpReDial() {
        Logger.d(TAG, "reqHfpReDial");
        if (isHfpConnected()) {
            try {
                return mCommand.reqHfpReDial();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 获取当前电话静音状态
     */
    public boolean isHfpMicMute() {
        if (isHfpConnected()) {
            try {
                boolean hfpMicMute = mCommand.isHfpMicMute();
                Logger.d(TAG, "isHfpMicMute: " + hfpMicMute);
                return hfpMicMute;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 设置静音
     */
    public void muteHfpMic(boolean mute) {
        Logger.d(TAG, "muteHfpMic");
        if (isHfpConnected()) {
            try {
                mCommand.muteHfpMic(mute);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求HFP连接的远程设备发送DTMF。
     */
    public void reqHfpSendDtmf(String number) {
        Logger.d(TAG, "reqHfpSendDtmf");
        if (isHfpConnected()) {
            try {
                mCommand.reqHfpSendDtmf(number);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询A2DP地址
     */
    public String getA2dpConnectedAddress() {
        if (isHfpConnected()) {
            try {
                String connectedAddress = mCommand.getA2dpConnectedAddress();
                Logger.d(TAG, "getA2dpConnectedAddress=connectedAddress=" + connectedAddress);
                return connectedAddress;
            } catch (RemoteException e) {
                Logger.d(TAG, "e==" + e);
                e.printStackTrace();
                return NfDef.DEFAULT_ADDRESS;
            }
        } else {
            Logger.d(TAG, "getA2dpConnectedAddress2");
            return NfDef.DEFAULT_ADDRESS;
        }
    }

    /**
     * 检查本地设备是否与远程设备连接HFP
     */
    public String getHfpConnectedAddress() {
        if (isHfpConnected()) {
            try {
                String connectedAddress = mCommand.getHfpConnectedAddress();
                Logger.d(TAG, "getHfpConnectedAddress1=connectedAddress=" + connectedAddress);
                return connectedAddress;
            } catch (RemoteException e) {
                Logger.d(TAG, "e==" + e);
                e.printStackTrace();
                return NfDef.DEFAULT_ADDRESS;
            }
        } else {
            Logger.d(TAG, "getHfpConnectedAddress2");
            return NfDef.DEFAULT_ADDRESS;
        }
    }

    public String getBtRemoteDeviceName(String address) {
        if (isHfpConnected()) {
            try {
                String deviceName = mCommand.getBtRemoteDeviceName(address);
                Logger.d(TAG, "getBtRemoteDeviceName-deviceName==" + deviceName);
                return deviceName;
            } catch (RemoteException e) {
                Logger.d(TAG, "e==" + e);
                e.printStackTrace();
                return "";
            }
        } else {
            Logger.d(TAG, "getBtRemoteDeviceName-2");
            return "";
        }
    }

    public int getBtRemoteUuids(String address) {
        Logger.d(TAG, "getBtRemoteUuids");
        if (isHfpConnected()) {
            try {
                return mCommand.getBtRemoteUuids(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getHfpConnectionState() {
        Logger.d(TAG, "getHfpConnectionState");
        if (isHfpConnected()) {
            try {
                return mCommand.getHfpConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 蓝牙音乐固定音量([0, 1F])
     */
    private static final float A2DP_DEFAULT_VOLUME = 1F;

    /**
     * 蓝牙音乐降低声音比例
     */
    private static final float A2DP_VOLUME_REDUCE_RATE = 0.2F;

    /**
     * 恢复音量
     */
    public void resumeA2dpVolume() {
        setA2dpLocalVolume(A2DP_DEFAULT_VOLUME);
    }

    /**
     * 降低音量
     */
    public void reduceA2dpVolume() {
        setA2dpLocalVolume(A2DP_VOLUME_REDUCE_RATE);
    }

    private void muteA2dpVolume(boolean mute) {
        muteA2dpRender(mute);
        if (mute) {
            setA2dpLocalVolume(0);
        } else {
            setA2dpLocalVolume(1);
        }
    }

    public void muteA2dpRender(boolean mute) {
        Logger.d(TAG, "muteA2dpRender: " + mute);
        if (isA2dpConnected()) {
            try {
                if (mute) {
                    Logger.d(TAG, "muteA2dpRender: pauseA2dpRender： ");
                    mCommand.pauseA2dpRender();
                } else {
                    Logger.d(TAG, "muteA2dpRender: startA2dpRender： ");
                    mCommand.startA2dpRender();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 这个方法必须在歌曲播放中才起作用
     *
     * @param volume
     */
    private boolean setA2dpLocalVolume(float volume) {
        Logger.d(TAG, "setA2dpLocalVolume-volume=" + volume);

        if (isA2dpConnected()) {
            try {
                if (mCommand.getA2dpConnectionState() == NfDef.STATE_STREAMING) {
                    return mCommand.setA2dpLocalVolume(volume);
                } else {
                    return false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 播放音乐
     */
    public void play() {
        Logger.d(TAG, "req bt music play");
        if (isAvrcpConnected()) {
            try {
                mCommand.reqAvrcpPlay();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停音乐
     */
    public void pause() {
        Logger.d(TAG, "req bt music pause");
        if (isAvrcpConnected()) {
            try {
                mCommand.reqAvrcpPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止音乐
     */
    public void stopMusic() {
        Logger.d(TAG, "req bt music stop");
        if (isAvrcpConnected()) {
            try {
                mCommand.reqAvrcpStop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下一曲
     */
    public void next() {
        Logger.d(TAG, "req bt music next");
        if (isAvrcpConnected()) {
            try {
                mCommand.reqAvrcpForward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上一曲
     */
    public void prev() {
        Logger.d(TAG, "req bt music prev");
        if (isAvrcpConnected()) {
            try {
                mCommand.reqAvrcpBackward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙是否已经连接
     *
     * @return
     */
    public boolean isConnected() {
        return isHfpConnected();
    }

    public int getA2dpConnectionState() {
        Logger.d(TAG, "getA2dpConnectionState");
        if (isA2dpConnected()) {
            try {
                return mCommand.getA2dpConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 断开蓝牙连接
     */
    public int reqBtDisconnectAll() {
        Logger.d(TAG, "reqBtDisconnectAll");
        if (isHfpConnected()) {
            try {
                return mCommand.reqBtDisconnectAll();
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 是否支持来电铃声
     */
    public boolean isHfpInBandRingtoneSupport() {
        Logger.d(TAG, "isHfpInBandRingtoneSupport");
        if (isHfpConnected()) {
            try {
                return mCommand.isHfpInBandRingtoneSupport();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 删除设备
     *
     * @param address address
     */
    public boolean reqBtUnpair(String address) {
        Logger.d(TAG, "reqBtUnpair-address=" + address);
        if (checkAutoBTService()) {
            try {
                return mCommand.reqBtUnpair(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 断开HID连接
     */
    public boolean reqHidDisconnect(String address) {
        Logger.d(TAG, "reqHidDisconnect-address=" + address);
        if (checkAutoBTService()) {
            try {
                //如果未连接，就不用再请求连接，短路与
                return mCommand.isHidConnected() && mCommand.reqHidDisconnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 连接HID
     */
    public boolean reqHidConnect(String address) {
        Logger.d(TAG, "reqHidConnect-address=" + address);
        if (checkAutoBTService()) {
            try {
                //如果已经连接，就不用再请求连接，短路或
                return mCommand.isHidConnected() || mCommand.reqHidConnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 断开HFP
     */
    public boolean reqHfpDisconnect(String address) {
        Logger.d(TAG, "reqHfpDisconnect-address=" + address);
        if (checkAutoBTService()) {
            try {
                return mCommand.isHfpConnected() && mCommand.reqHfpDisconnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 连接FHP
     */
    public boolean reqHfpConnect(String address) {
        Logger.d(TAG, "reqHfpConnect-address=" + address);
        if (checkAutoBTService()) {
            try {
                return mCommand.isHfpConnected() || mCommand.reqHfpConnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 断开A2DP
     */
    public boolean reqA2dpDisconnect(String address) {
        Logger.d(TAG, "reqA2dpDisconnect-address=" + address);
        if (checkAutoBTService()) {
            try {
                return mCommand.isA2dpConnected() && mCommand.reqA2dpDisconnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 连接A2DP
     */
    public boolean reqA2dpConnect(String address) {
        Logger.d(TAG, "reqA2dpConnect-address=" + address);
        if (checkAutoBTService()) {
            try {
                return mCommand.isA2dpConnected() || mCommand.reqA2dpConnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 断开Avrcp
     */
    public boolean reqAvrcpDisconnect(String address) {
        Logger.d(TAG, "reqAvrcpDisconnect-address=" + address);
        if (checkAutoBTService()) {
            try {
                return mCommand.isAvrcpConnected() && mCommand.reqAvrcpDisconnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 连接Avrcp
     */
    public boolean reqAvrcpConnect(String address) {
        Logger.d(TAG, "reqAvrcpConnect-address=" + address);
        if (checkAutoBTService()) {
            try {
                return mCommand.isAvrcpConnected() || mCommand.reqAvrcpConnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 请求连接蓝牙
     *
     * @param address address
     * @return
     */
    public int reqBtConnectHfpA2dp(String address) {
        Logger.d(TAG, "reqBtConnectHfpA2dp-address=" + address);
        if (checkAutoBTService()) {
            try {
                return mCommand.reqBtConnectHfpA2dp(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public boolean reqBtPair(String address) {
        if (checkAutoBTService()) {
            try {
                return mCommand.reqBtPair(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 获取配对名字
     */
    public String getBtLocalName() {
        Logger.d(TAG, "getBtLocalName");
        if (checkAutoBTService()) {
            try {
                return mCommand.getBtLocalName();
            } catch (RemoteException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /**
     * 设置本地蓝牙适配器的名称
     */
    public boolean setBtLocalName(String name) {
        Logger.d(TAG, "setBtLocalName-name=" + name);
        if (checkAutoBTService()) {
            try {
                return mCommand.setBtLocalName(name);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 获取本地连接地址值
     */
    public String getBtLocalAddress() {
        if (checkAutoBTService()) {
            try {
                String localAddress = mCommand.getBtLocalAddress();
                Logger.d(TAG, "getBtLocalAddress---localAddress: " + localAddress);
                return localAddress;
            } catch (RemoteException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /**
     * 请求获取配对设备列表
     */
    public boolean reqBtPairedDevices() {
        Logger.d(TAG, "reqBtPairedDevices");
        if (checkAutoBTService()) {
            try {
                return mCommand.reqBtPairedDevices();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 检查当前是否启用了蓝牙
     */
    public boolean isBtEnabled() {
        Logger.d(TAG, "isBtEnabled");
        if (checkAutoBTService()) {
            try {
                return mCommand.isBtEnabled();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 设置本地蓝牙适配器可在几秒钟内发现特定的持续时间
     */
    public boolean setBtDiscoverableTimeout(int timeout) {
        Logger.d(TAG, "setBtDiscoverableTimeout-timeout=" + timeout);
        if (checkAutoBTService()) {
            try {
                return mCommand.setBtDiscoverableTimeout(timeout);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 设置本地蓝牙适配器以启用或禁用
     */
    public boolean setBtEnable(boolean enable) {
        if (checkAutoBTService()) {
            try {
                Logger.d(TAG, "setBtEnable-enable=" + enable);
                return mCommand.setBtEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 将蓝牙模式切换为其他角色
     *
     * @param mode 1 车机模式
     */
    public boolean switchBtRoleMode(int mode) {
        Logger.d(TAG, "switchBtRoleMode-mode=" + mode);
        if (checkAutoBTService()) {
            try {
                return mCommand.switchBtRoleMode(mode);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 设置nFore自动连接机制条件和周期
     *
     * @param condition 条件
     * @param period    周期
     */
    public void setBtAutoConnect(int condition, int period) {
        Logger.d(TAG, "setBtAutoConnect-condition=" + condition + ",period=" + period);
        if (checkAutoBTService()) {
            try {
                mCommand.setBtAutoConnect(condition, period);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取自动连接状态
     */
    public int getBtAutoConnectState() {
        Logger.d(TAG, "getBtAutoConnectState");
        if (checkAutoBTService()) {
            try {
                return mCommand.getBtAutoConnectState();
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public int getBtAutoConnectCondition() {
        Logger.d(TAG, "getBtAutoConnectCondition");
        if (checkAutoBTService()) {
            try {
                return mCommand.getBtAutoConnectCondition();
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public int getHfpRemoteBatteryIndicator() {
        Logger.d(TAG, "getHfpRemoteBatteryIndicator");
        if (isHfpConnected()) {
            try {
                return mCommand.getHfpRemoteBatteryIndicator();
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public String getHfpRemoteNetworkOperator() {
        Logger.d(TAG, "getHfpRemoteNetworkOperator");
        if (isHfpConnected()) {
            try {
                return mCommand.getHfpRemoteNetworkOperator();
            } catch (RemoteException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public String getHfpRemoteSubscriberNumber() {
        Logger.d(TAG, "getHfpRemoteSubscriberNumber");
        if (checkAutoBTService()) {
            try {
                return mCommand.getHfpRemoteSubscriberNumber();
            } catch (RemoteException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public String getBtAutoConnectingAddress() {
        Logger.d(TAG, "getBtAutoConnectingAddress");
        if (checkAutoBTService()) {
            try {
                return mCommand.getBtAutoConnectingAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /**
     * 请求从远程设备下载带有vCard的电话簿，并将回调传递给用户
     *
     * @param address  有效的蓝牙MAC地址
     * @param storage  存储类型
     * @param property 下载属性掩码
     * @param startPos 下载开始位置
     * @param offset   下载偏移量
     */
    public boolean reqPbapDownloadRange(String address, int storage, int property, int startPos, int offset) {
        Logger.d(TAG, "reqPbapDownloadRange-address=" + address + ",storage=" + storage);
        Logger.d(TAG, "reqPbapDownloadRange-property=" + property + ",startPos=" + startPos + ",offset=" + offset);
        if (isHfpConnected()) {
            try {
//                mCommand.setPbapDownloadNotify(Cx62BtConstant.PBAP.PBAP_DOWNLOAD_NOTIFY);
                return mCommand.reqPbapDownloadRange(address, storage, property, startPos, offset);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 获取Pbap连接状态
     *
     * @return
     */
    public int getPbapConnectionState() {
        Logger.d(TAG, "getPbapConnectionState");
        if (checkAutoBTService()) {
            try {
                return mCommand.getPbapConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 启动远程设备的扫描过程
     */
    public boolean startBtDiscovery() {
        Logger.d(TAG, "startBtDiscovery");
        if (checkAutoBTService()) {
            try {
                return mCommand.startBtDiscovery();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 停止远程设备的扫描过程
     */
    public boolean cancelBtDiscovery() {
        Logger.d(TAG, "startBtDiscovery");
        if (checkAutoBTService()) {
            try {
                return mCommand.cancelBtDiscovery();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 设置PBAP下载通知频率。 默认值为0表示不回调下载通知。
     * 例如，如果频率设置为5，则将通知每5个联系人onPbapDownloadNofity。
     *
     * @param frequency 频率
     */
    public boolean setPbapDownloadNotify(int frequency) {
        boolean isPbapOk = isPbapServiceReady();
        Logger.d(TAG, "setPbapDownloadNotify-isPbapOk=" + isPbapOk);
        if (isPbapOk) {
            try {
                return mCommand.setPbapDownloadNotify(frequency);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 请求中断正在进行的远程设备下载
     */
    public boolean reqPbapDownloadInterrupt() {
        String address = getA2dpConnectedAddress();
        boolean isPbapOk = isPbapServiceReady();
        Logger.d(TAG, "REQ_PBAP_DOWNLOAD_INTERRUPT-isPbapOk=" + isPbapOk);
        if (isPbapOk) {
            try {
                return mCommand.reqPbapDownloadInterrupt(address);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 请求从远程设备下载带有vCard的电话簿，并将回调传递给用户
     *
     * @param address  有效的蓝牙MAC地址
     * @param storage  存储类型
     * @param property 下载属性掩码
     */
    public boolean reqPbapDownload(String address, int storage, int property) {
        boolean isPbapOk = isPbapServiceReady();
        Logger.d(TAG, "reqPbapDownload-isPbapOk=" + isPbapOk);
        if (isPbapOk) {
            try {
                return mCommand.reqPbapDownload(address, storage, property);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean reqPbapDownloadToContactsProvider(String address, int storage, int property) {
        boolean isPbapOk = isPbapServiceReady();
        Logger.d(TAG, "reqPbapDownloadToContactsProvider-isPbapOk=" + isPbapOk);
        if (isPbapOk) {
            try {
                return mCommand.reqPbapDownloadToContactsProvider(address, storage, property);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean reqPbapDownloadRangeToContactsProvider(String address, int storage, int property, int startPos, int offset) {
        boolean isPbapOk = isPbapServiceReady();
        Logger.d(TAG, "reqPbapDownloadRangeToContactsProvider-isPbapOk=" + isPbapOk);
        if (isPbapOk) {
            try {
                return mCommand.reqPbapDownloadRangeToContactsProvider(address, storage, property, startPos, offset);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void reqPbapDatabaseQueryNameByNumber(String address, String target) {
        if (isPbapServiceReady()) {
            try {
                mCommand.reqPbapDatabaseQueryNameByNumber(address, target);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTargetAddress() {
        try {
            return mCommand.getTargetAddress();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
