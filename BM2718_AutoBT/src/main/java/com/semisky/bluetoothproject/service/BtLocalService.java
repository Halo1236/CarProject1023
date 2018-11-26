package com.semisky.bluetoothproject.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.semisky.autoservice.aidl.INaviStatusListener;
import com.semisky.autoservice.aidl.IPowerModeChanged;
import com.semisky.autoservice.manager.AutoConstants;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.bluetoothproject.IBtMethodInterface;
import com.semisky.bluetoothproject.IBtStatusListener;
import com.semisky.bluetoothproject.MainActivity;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.model.BtBackCarModel;
import com.semisky.bluetoothproject.model.BtCallStatusModel;
import com.semisky.bluetoothproject.model.BtContactsDownloadModel;
import com.semisky.bluetoothproject.model.BtDeviceSearchModel;
import com.semisky.bluetoothproject.model.BtHfpModel;
import com.semisky.bluetoothproject.model.BtKeyModel;
import com.semisky.bluetoothproject.model.BtMusicAudioFocusModel;
import com.semisky.bluetoothproject.model.BtPhoneAudioFocusModel;
import com.semisky.bluetoothproject.model.BtStatusModel;
import com.semisky.bluetoothproject.model.modelInterface.OnBackCarStateChangeListener;
import com.semisky.bluetoothproject.model.modelInterface.OnBtKeyListener;
import com.semisky.bluetoothproject.model.modelInterface.OnCallStatusListener;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;
import com.semisky.bluetoothproject.presenter.viewInterface.BTContactAndRecordInterface;
import com.semisky.bluetoothproject.presenter.viewInterface.HFPStatusInterface;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;
import com.semisky.bluetoothproject.view.BtContactDownloadDialog;
import com.semisky.bluetoothproject.view.CallFullScreenView;
import com.semisky.bluetoothproject.view.CallViewDialog;

import java.io.Serializable;

import static com.semisky.bluetoothproject.constant.BtConstant.ACTION_MODE_START;
import static com.semisky.bluetoothproject.constant.BtConstant.ACTION_START_ACTIVITY;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.ACTIVE;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.DIALING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.INCOMING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.TERMINATED;
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
import static com.semisky.bluetoothproject.constant.BtConstant.MODE_START_KEY;
import static com.semisky.bluetoothproject.constant.BtConstant.SERIALIZABLE_DATA;
import static com.semisky.bluetoothproject.constant.BtConstant.START_ACTIVITY_VALUE;

/**
 * 本地蓝牙服务
 */
public class BtLocalService extends Service {

    private static final String TAG = Logger.makeTagLog(BtLocalService.class);

    private BTMethodBinder btMethod = new BTMethodBinder();

    private BtCallStatusModel btCallStatusModel;

    private Handler methodHandler;
    private BtBaseUiCommandMethod commandMethod;
    private CallViewDialog callViewDialog;
    private BtContactDownloadDialog btContactDownloadDialog;
    private BtStatusModel btStatusModel;

    private BtContactsDownloadModel btContactsDownloadModel;
    private BtDeviceSearchModel btDeviceSearchModel;
    private BtHfpModel btHfpModel;
    private IBtStatusListener BtStatusListener;
    private CallFullScreenView callFullScreenView;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate: ");
        btHfpModel = BtHfpModel.getInstance();
        btDeviceSearchModel = BtDeviceSearchModel.getInstance();
        btContactsDownloadModel = BtContactsDownloadModel.getInstance();
        commandMethod = BtBaseUiCommandMethod.getInstance();
        commandMethod.start();
        btCallStatusModel = BtCallStatusModel.getInstance();
        btStatusModel = BtStatusModel.getInstance();

        methodHandler = commandMethod.getMethodHandler();
        callViewDialog = new CallViewDialog(BtLocalService.this);
        callViewDialog.setBtBaseUiCommandMethod(commandMethod);

        btContactDownloadDialog = new BtContactDownloadDialog(BtLocalService.this);
        btContactDownloadDialog.setBtBaseUiCommandMethod(commandMethod);

        callFullScreenView = new CallFullScreenView(BtLocalService.this);
        callFullScreenView.setBtBaseUiCommandMethod(commandMethod);

        initListener();
    }

    private void initListener() {
        Logger.d(TAG, "initListener: ");
        initCallDialogShow();
        initBTHFPListener();
        initContactDownloadListener();
        initKeyListener();
        initAccOffListener();
    }

    /**
     * 响应按键功能
     */
    private void initKeyListener() {
        OnBtKeyListener onBtKeyListener = new OnBtKeyListener() {
            @Override
            public void connectCall() {
                sendMessageToBTNative(REQ_HFP_ANSWER_CALL, null);
            }

            @Override
            public void hangupCall() {
                sendMessageToBTNative(REQ_HFP_TERMINATE_CURRENT_CALL, null);
            }

            @Override
            public void previousSong() {
                sendMessageToBTNative(REQ_BT_MUSIC_LAST, null);
            }

            @Override
            public void nextSong() {
                sendMessageToBTNative(REQ_BT_MUSIC_NEXT, null);
            }
        };

        BtKeyModel.getInstance().registerKeyListener(onBtKeyListener);
    }

    /**
     * 联系人和通讯录下载监听
     */
    private void initContactDownloadListener() {
        BTContactAndRecordInterface btContactAndRecordInterface = new BTContactAndRecordInterface() {
            @Override
            public void contactDownloadFail() {

            }

            @Override
            public void contactDownloadStart() {
//                btContactDownloadDialog.initCancelButtonView(getString(R.string.cx62_bt_dialog_sync_sim));
//                btContactDownloadDialog.showDialog();
            }

            @Override
            public void contactDownloadCompleted() {
//                btContactDownloadDialog.dismissContactDialog();
            }

            @Override
            public void callRecordDownloadFail() {

            }

            @Override
            public void callRecordDownloadStart() {
//                btContactDownloadDialog.initCancelButtonView(getString(R.string.cx62_bt_dialog_sync_calllogs));
//                btContactDownloadDialog.showDialog();
            }

            @Override
            public void callRecordDownloadCompleted() {
//                btContactDownloadDialog.dismissContactDialog();

            }

            @Override
            public void callAllForContactStart() {
//                btContactDownloadDialog.initCancelButtonView(getString(R.string.cx62_bt_dialog_sync_sim));
//                btContactDownloadDialog.showDialog();
            }

            @Override
            public void callAllForContactCompleted() {
//                btContactDownloadDialog.dismissContactDialog();
                Logger.d(TAG, "initContactDownloadListener--------------callAllForContactCompleted");
                //下载联系人成功，开始下载通话记录
                sendMessageToBTNative(REQ_BT_DOWNLOAD_CALLLOG, null);
                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.ALL_IN_RECORD);
            }

            @Override
            public void callAllForRecordStart() {
//                btContactDownloadDialog.initCancelButtonView(getString(R.string.cx62_bt_dialog_sync_calllogs));
//                btContactDownloadDialog.showDialog();
            }

            @Override
            public void callAllForRecordCompleted() {
//                btContactDownloadDialog.dismissContactDialog();
            }

            @Override
            public void callAccessPermissions() {
                Logger.d(TAG, "initContactDownloadListener--------------callAccessPermissions");
                checkAutoSyncContact();
            }
        };

        btContactsDownloadModel.setbtContactAndRecord(btContactAndRecordInterface);
    }

    private void initAccOffListener() {
        IPowerModeChanged iPowerModeChanged = new IPowerModeChanged.Stub() {
            @Override
            public void onPowerModeChange(int i) {
                switch (i) {
                    case AutoConstants.PowerMode.WORKING:
                        Logger.d(TAG, "onPowerModeChange: 连接acc " + btStatusModel.isAccOff());
                        boolean autoConnStateSP = BtSPUtil.getInstance().getAutoConnStateSP(BtLocalService.this);
                        Logger.d(TAG, "onPowerModeChange: 是否开启自动连接选项 " + autoConnStateSP);
                        if (btStatusModel.isAccOff() && autoConnStateSP) {
                            //主动连接
                            String address = BtSPUtil.getInstance().getLastConnectAddressSP(BtLocalService.this);
                            sendMessageToBTNative(REQ_BT_CONNECT_HFP_A2DP, address);
                            btStatusModel.setAccOff(false);
                        }

                        break;
                    case AutoConstants.PowerMode.MINUTE_15:

                        break;
                    case AutoConstants.PowerMode.STANDBY:
                        Logger.d(TAG, "onPowerModeChange: 断开acc");
                        //主动断开蓝牙
                        btStatusModel.setAccOff(true);
                        btStatusModel.setAccOnRecoverMusic(true);
                        sendMessageToBTNative(CANCEL_BT_DISCOVERY, null);
                        sendMessageToBTNative(REQ_BT_SET_BREAK_CONNECT, null);
                        break;
                    case AutoConstants.PowerMode.SHUTDOWN:

                        break;
                }
            }
        };

        AutoManager.getInstance().registerPowermodeChangeListener(iPowerModeChanged);
    }

    /**
     * 蓝牙连接状态监听
     */
    private void initBTHFPListener() {
        HFPStatusInterface hfpStatusInterface = new HFPStatusInterface() {
            @Override
            public void stateDisconnecting(String address) {

            }

            @Override
            public void stateConnected(String address) {
                //模拟请求权限，如果数据请求成功，说明开启了权限，然后可以进行自动同步功能
                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.PERMISSION);
                sendMessageToBTNative(CHECK_PERMISSION, null);
            }

            @Override
            public void stateConnecting(String address) {

            }

            @Override
            public void stateDisconnect(String address) {
                btStatusModel.setBTConnect(false);
                if (BtStatusListener != null) {
                    try {
                        //通知activity刷新fragment
                        BtStatusListener.stateDisconnect(address);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                if (!btStatusModel.isAccOff()) {
                    //断开蓝牙重置
                    btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.KEYBOARD);
                }

                //关闭铃声
                callViewDialog.stopRing();

                //关闭通话页面
                if (callViewDialog != null && callViewDialog.isShowing()) {
                    callViewDialog.dismiss();
                }

                //释放音频焦点
//                BtMusicAudioFocusModel.getINSTANCE().abandonAudioFocus();

                commandMethod.muteA2dpRender(true);

                //通话中断开蓝牙
                BtPhoneAudioFocusModel.getINSTANCE().abandonAudioFocus();
            }
        };

        btHfpModel.setHFPStatusInterface(hfpStatusInterface);
    }

    /**
     * 通讯页面dialog
     */
    private void initCallDialogShow() {
        OnCallStatusListener listener = new OnCallStatusListener() {
            @Override
            public void callStatusIncomming(int id) {
                Logger.d(TAG, "callStatusIncomming: ");
                if (btStatusModel.isNaviView()) {
                    callFullScreenView.setCallStatus(INCOMING, id);
                } else {
                    callViewDialog.setCallStatus(INCOMING, id);
                }

                checkCarBackStatus();
            }

            @Override
            public void callStatusDialing(int id) {
                Logger.d(TAG, "callStatusDialing: ");
                if (btStatusModel.isNaviView()) {
                    callFullScreenView.setCallStatus(DIALING, id);
                } else {
                    callViewDialog.setCallStatus(DIALING, id);
                }
                checkCarBackStatus();
            }

            @Override
            public void callStatusActive(int id) {
                Logger.d(TAG, "callStatusActive: ");
                if (btStatusModel.isNaviView()) {
                    callFullScreenView.setCallStatus(ACTIVE, id);
                } else {
                    callViewDialog.setCallStatus(ACTIVE, id);
                }
                checkCarBackStatus();
            }

            @Override
            public void callStatusTerminated() {
                Logger.d(TAG, "callStatusTerminated: ");
                isHideDialog = false;
                if (btStatusModel.isNaviView()) {
                    callFullScreenView.setCallStatus(TERMINATED, 0);
                } else {
                    callViewDialog.setCallStatus(TERMINATED, 0);
                }
//                if (btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.ALL_IN_CONTACT ||
//                        btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.CONTACT) {
//                    btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.ONCE_RECORD_CONTACT);
//                } else {
                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.ONCE_RECORD);
//                }
                sendMessageToBTNative(REQ_BT_ONCE_CALLLOG, null);
            }
        };

        btCallStatusModel.setOnCallStatusListener(listener);

        /**
         * 动态监听导航页面状态
         */
        AutoManager.getInstance().registerNaviStatusListener(new INaviStatusListener.Stub() {
            @Override
            public void onNaviStatusChange(int type, boolean isShow) {
                if (type == AutoConstants.NaviType.DEFAULT_NAVI) {
                    Logger.d(TAG, "onNaviStatusChange: " + isShow);
                    btStatusModel.setNaviView(isShow);
                    if (!isShow) {
                        switch (btStatusModel.getCallStatus()) {
                            case INCOMING:
                                callViewDialog.setCallStatus(INCOMING, callFullScreenView.getId());
                                break;
                            case DIALING:
                                callViewDialog.setCallStatus(DIALING, callFullScreenView.getId());
                                break;
                            case ACTIVE:
                                //设置跳转前的时间
                                long chronometerBase = callFullScreenView.getChronometerBase();
                                callViewDialog.switchCallView(chronometerBase, callFullScreenView.getId());
                                break;
                        }
                        callFullScreenView.removeView();
                    }
                }
            }
        });

        OnBackCarStateChangeListener onBackCarStateChangeListener = new OnBackCarStateChangeListener() {
            @Override
            public void onBackCarEnter() {
                //在通话中如果进入倒车，就把声音切换到手机端 7973
//                if (btStatusModel.isCallPhone()) {
//                    commandMethod.reqHfpAudioTransferToPhone();
//                }
                if (callViewDialog.isShowing()) {
                    callViewDialog.setHide();
                    isHideDialog = true;
                }
            }

            @Override
            public void onBackCarQuit() {
//                if (btStatusModel.isCallPhone()) {
//                    commandMethod.reqHfpAudioTransferToCarkit();
//                }
                Logger.d(TAG, "onBackCarQuit:isHideDialog " + isHideDialog);
                if (isHideDialog) {
                    callViewDialog.setShow();
                    isHideDialog = false;
                }
            }
        };

        BtBackCarModel.getInstance().setOnCarStateChangeListener(onBackCarStateChangeListener);
    }

    /**
     * 是否因为了倒车隐藏了通话页面
     */
    private boolean isHideDialog;

    /**
     * 如果当前正在倒车，就隐藏通页面
     */
    private void checkCarBackStatus() {
        boolean backMode = AutoManager.getInstance().isBackMode();
        Logger.d(TAG, "checkCarBackStatus:backMode " + backMode);
        if (backMode) {
            callViewDialog.setHide();
            isHideDialog = true;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return btMethod;
    }

    private void sendMessageToBTNative(int apiID, Serializable data) {
        if (null != methodHandler) {
            Message message = methodHandler.obtainMessage();
            message.what = apiID;

            if (null != data) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(SERIALIZABLE_DATA, data);
                message.setData(bundle);
            }
            methodHandler.removeMessages(apiID);
            methodHandler.sendMessageDelayed(message, 200);
        }
    }

    private class BTMethodBinder extends IBtMethodInterface.Stub {
        @Override
        public void startBtDiscovery() {
            sendMessageToBTNative(START_BT_DISCOVERY, null);
        }

        @Override
        public void cancelBtDiscovery() throws RemoteException {
            sendMessageToBTNative(CANCEL_BT_DISCOVERY, null);
        }

        @Override
        public void reqHfpDialCall(String number) {
            sendMessageToBTNative(REQ_HFP_DIAL_CALL, number);
        }

        @Override
        public void reqBtConnectHfpA2dp(String address) throws RemoteException {
            sendMessageToBTNative(REQ_BT_CONNECT_HFP_A2DP, address);
        }

        @Override
        public void reqBtUnpair(String address) throws RemoteException {
            sendMessageToBTNative(REQ_BT_UN_PAIR, address);
        }

        @Override
        public void reqPbapDownloadConnect() throws RemoteException {
            Logger.d(TAG, "reqPbapDownloadConnect");
            sendMessageToBTNative(REQ_BT_DOWNLOAD_CONNECT, null);
        }

        @Override
        public void reqPbapDownloadedCallLog() throws RemoteException {
            sendMessageToBTNative(REQ_BT_DOWNLOAD_CALLLOG, null);
        }

        @Override
        public void playSong() throws RemoteException {
            sendMessageToBTNative(REQ_BT_MUSIC_PLAY, null);
        }

        @Override
        public void pauseSong() throws RemoteException {
            sendMessageToBTNative(REQ_BT_MUSIC_PAUSE, null);
        }

        @Override
        public void playNext() throws RemoteException {
            sendMessageToBTNative(REQ_BT_MUSIC_NEXT, null);
        }

        @Override
        public void playLast() throws RemoteException {
            sendMessageToBTNative(REQ_BT_MUSIC_LAST, null);
        }

        @Override
        public void breakConnect() throws RemoteException {
            sendMessageToBTNative(REQ_BT_SET_BREAK_CONNECT, null);
        }

        @Override
        public void isBtConnect() throws RemoteException {
            btDeviceSearchModel.setBtOpen(commandMethod.isBtEnabled());
        }

        @Override
        public void setBtEnable(boolean status) throws RemoteException {
            sendMessageToBTNative(SET_BT_ENABLE, status);
        }

        @Override
        public void reqBtPairedDevices() throws RemoteException {
            sendMessageToBTNative(REQ_BT_PAIRE_DDEVICES, null);
        }

        @Override
        public void initBTStatus() throws RemoteException {
            btStatusModel.setAddress(commandMethod.getHfpConnectedAddress());
            btStatusModel.setPhoneName(commandMethod.getBtRemoteDeviceName(commandMethod.getHfpConnectedAddress()));
        }

        @Override
        public void reqAvrcp13GetElementAttributesPlaying() throws RemoteException {
            sendMessageToBTNative(REQ_AVRCP13_GET_PLAYING, null);
        }

        @Override
        public void setAutoConnect(boolean autoConnect) throws RemoteException {
            sendMessageToBTNative(SET_AUTO_CONNECT, autoConnect);
        }

        @Override
        public void setOnBTStatusListener(IBtStatusListener listener) throws RemoteException {
            BtStatusListener = listener;
        }

        @Override
        public boolean isConnected() throws RemoteException {
            return commandMethod.isConnected();
        }

        @Override
        public void reqPbapDownloadInterrupt() throws RemoteException {
            sendMessageToBTNative(REQ_PBAP_DOWNLOAD_INTERRUPT, null);
        }

        @Override
        public void reqAvrcp13GetPlayStatus() throws RemoteException {
            sendMessageToBTNative(REQ_AVRCP_13_GET_PLAY_STATUS, null);
        }
    }

    /**
     * 检查需不需自动同步通讯录
     * 1.自动同步开关是否打开
     * 2.连接的是不是新手机
     * 3.之前有没有手动同步了通讯录
     * 4.手机端是否授权
     */
    private void checkAutoSyncContact() {
        boolean autoSyncBookStateSP = BtSPUtil.getInstance().getAutoSyncBookStateSP(this);
//        boolean newDevices = btStatusModel.isNewDevice();
//        boolean syncContactAndCallLog = !BtSPUtil.getInstance().isSyncContactAndCallLog(this);
        Logger.d(TAG, "checkAutoSyncContact: autoSyncBookStateSP " + autoSyncBookStateSP);
//        Logger.d(TAG, "checkAutoSyncContact: newDevices " + newDevices);
//        Logger.d(TAG, "checkAutoSyncContact: syncContactAndCallLog " + syncContactAndCallLog);
        if (autoSyncBookStateSP /*&& newDevices && syncContactAndCallLog*/) {
            btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.ALL_IN_CONTACT);
            Logger.d(TAG, "sendMessageToBTNative:  REQ_BT_DOWNLOAD_CONNECT");
            sendMessageToBTNative(REQ_BT_DOWNLOAD_CONNECT, null);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        Logger.d(TAG, "startService: ");
        return super.startService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG, "onStartCommand: ");
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_MODE_START:
                        int intExtra = intent.getIntExtra(MODE_START_KEY, -1);
                        Logger.d(TAG, "onStartCommand: " + intExtra);
                        if (intExtra == AutoConstants.AppStatus.RUN_FOREGROUND) {
                            Logger.d(TAG, "onStartCommand: 前台启动蓝牙音乐");
                            Intent startActivity = new Intent(this, MainActivity.class);
                            startActivity.putExtra(ACTION_START_ACTIVITY, START_ACTIVITY_VALUE);
                            startActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startActivity);
                        } else if (intExtra == AutoConstants.AppStatus.RUN_BACKGROUND) {
                            Logger.d(TAG, "onStartCommand: 后台启动蓝牙音乐");
                            sendMessageToBTNative(REQ_BT_MUSIC_PLAY, null);
                            BtMiddleSettingManager.getInstance().setBtMusicStatus(false);
                            BtMusicAudioFocusModel.getINSTANCE().applyAudioFocus();
                        }
                        break;
                }

            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
