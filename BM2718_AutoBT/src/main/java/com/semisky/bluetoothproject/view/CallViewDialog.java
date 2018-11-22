package com.semisky.bluetoothproject.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.res.NfDef;
import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.adapter.KeyboardGradViewAdapter;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.entity.CallNameActive;
import com.semisky.bluetoothproject.model.BtHfpModel;
import com.semisky.bluetoothproject.model.BtStatusModel;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;
import com.semisky.bluetoothproject.presenter.viewInterface.HFPAudioStateInterface;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import static com.semisky.autoservice.manager.AudioManager.STREAM_BT_RING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.ACTIVE;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.DIALING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.INCOMING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.TERMINATED;

/**
 * Created by chenhongrui on 2018/8/8
 * <p>
 * 内容摘要: 通话页面dialog
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CallViewDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private static final String TAG = Logger.makeTagLog(CallViewDialog.class);

    private Context mContext;
    private CallHandler callHandler;
    private BtBaseUiCommandMethod btBaseUiCommandMethod;
    private BtStatusModel btStatusModel;

    private Chronometer chronometer;
    private TextView tvCallNumber, tvCallStatus;
    private FrameLayout frameLayout;

    private static final int SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED = 0x00002000;

    private ImageButton ibActiveMute;
    private ImageButton ibActiveKeyboard;
    private ImageButton ibActiveAudioStream;
    private TextView tvCallImport, tvCallName;
    private LinearLayout llCallMessage, llCallKeyboard;

    private BtConstant.CallStatus callStatusNow = BtConstant.CallStatus.NULL;

    private boolean isFirstCall = true;

    /**
     * 第三方来电
     */
    private String secondNumber, secondName;

    /**
     * 正常来电
     */
    private String firstNumber, firstName;

    public CallViewDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        this.mContext = context;
        callHandler = new CallHandler(this);
        btStatusModel = BtStatusModel.getInstance();
        initView();
        initHFPStatusListener();
    }

    public void setBtBaseUiCommandMethod(BtBaseUiCommandMethod commandMethod) {
        this.btBaseUiCommandMethod = commandMethod;
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.call_view_dialog, null);
        setContentView(view);
        initShareView();

        chronometer = findViewById(R.id.Chronometer);
        tvCallNumber = findViewById(R.id.tvCallNumber);
        tvCallName = findViewById(R.id.tvCallName);
        tvCallStatus = findViewById(R.id.tvCallStatus);
        frameLayout = findViewById(R.id.callFrameLayout);
    }

    private void initShareView() {
        RelativeLayout relativeLayout = findViewById(R.id.rl_dialog_Layout);
        relativeLayout.setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED);

        WindowManager.LayoutParams param = Objects.requireNonNull(getWindow()).getAttributes();
        getWindow().setAttributes(param);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND
                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private static final int CALLING_TERMINATED = 0;
    private static final int CALLING_ACTIVE = 1;
    private static final int DIALOG_DISMISS = 2;
    private static final int CALLING_INCOMING = 4;
    private static final int CALLING_BT_TERMINATED = 5;
    private static final int CALLING_DIALING = 6;

    private static final int CALLING_HOLD = 7;
    private static final int CALLING_WAITING = 11;
    private static final int QUERY_NAME_FOR_STATUS = 8;
    private static final int RECOVER_STATUS = 9;
    private static final int AUTO_ANSWER = 10;
    private static final int REFRESH_HFP_AUDIO_STATUS = 12;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibIncomingAnswer:
                btBaseUiCommandMethod.reqHfpAnswerCall(0);
                break;
            case R.id.ibIncomingHangup:
                callHangup();
                break;
            case R.id.ibDialingHangup:
                callHangup();
                break;
            case R.id.ibActiveHangup:
                callHangup();
                break;
            case R.id.ibActiveMute:
                boolean hfpMicMute = btBaseUiCommandMethod.isHfpMicMute();
                Log.d(TAG, "muteHfpMic:hfpMicMute " + hfpMicMute);
                if (hfpMicMute) {
                    btBaseUiCommandMethod.muteHfpMic(false);
                    ibActiveMute.setImageResource(R.drawable.icon_call_mic);
                } else {
                    btBaseUiCommandMethod.muteHfpMic(true);
                    ibActiveMute.setImageResource(R.drawable.icon_call_mic_mute);
                }
                break;
            case R.id.ibActiveKeyboard:
                if (llCallMessage.getVisibility() == View.VISIBLE) {
                    llCallMessage.setVisibility(View.INVISIBLE);
                    llCallKeyboard.setVisibility(View.VISIBLE);
                    ibActiveKeyboard.setBackgroundResource(R.drawable.dialog_button_call_red_background);
                } else {
                    if (tvCallImport != null) {
                        tvCallImport.setText("");
                    }
                    llCallMessage.setVisibility(View.VISIBLE);
                    llCallKeyboard.setVisibility(View.INVISIBLE);
                    ibActiveKeyboard.setBackgroundResource(R.drawable.dialog_button_call_background);
                }
                break;
            case R.id.ibActiveAudioStream:
                if (btBaseUiCommandMethod.getHfpAudioConnectionState() == NfDef.STATE_CONNECTED) {
                    btBaseUiCommandMethod.reqHfpAudioTransferToPhone();
                    ibActiveAudioStream.setImageResource(R.drawable.icon_call_phone);
                } else {
                    btBaseUiCommandMethod.reqHfpAudioTransferToCarkit();
                    ibActiveAudioStream.setImageResource(R.drawable.icon_call_car);
                }
                break;
        }
    }

    private void callHangup() {
        btBaseUiCommandMethod.reqHfpTerminateCurrentCall();
//        callHandler.sendEmptyMessage(DIALOG_DISMISS);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView item = view.findViewById(R.id.tvKeyboardItem);
        String amount = tvCallImport.getText().toString().trim();
        String number = item.getText().toString().trim();
        if (number.equals("0+")) {
            number = "0";
        }
        amount = amount + number;
        tvCallImport.setText(amount);
        btBaseUiCommandMethod.reqHfpSendDtmf(number);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 7) {
            String amount = tvCallImport.getText().toString().trim();
            String number = "+";
            amount = amount + number;
            tvCallImport.setText(amount);
        }
        return true;
    }

    private static class CallHandler extends Handler {
        WeakReference<CallViewDialog> mReference;

        CallHandler(CallViewDialog dialog) {
            this.mReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CallViewDialog dialogView = mReference.get();

            List<NfHfpClientCall> callList = dialogView.btBaseUiCommandMethod.getHfpCallList();
            switch (msg.what) {
                case CALLING_TERMINATED:
                    //第三方挂断也会走这里
                    if (callList.size() == 0) {
                        //挂断后将通话时间归零
                        dialogView.stopCallTime();
                        dialogView.tvCallStatus.setText(dialogView.mContext.getString(R.string.cx62_bt_calling_state_over));
                        dialogView.setTextColor();
                        dialogView.callHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 1500);
                    }
                    break;
                case CALLING_BT_TERMINATED:
                    if (callList.size() == 0) {
                        //通话中断开蓝牙将页面退到后台
                        dialogView.callHandler.sendEmptyMessage(DIALOG_DISMISS);
                    }
                    break;
                case CALLING_INCOMING:
                    if (!dialogView.isShowing()) {
                        dialogView.show();
                    }
                    dialogView.setChronometerView();
                    if (dialogView.firstName.equals("")) {
                        dialogView.tvCallName.setText(dialogView.firstName);
                    }
                    dialogView.tvCallNumber.setText(dialogView.firstNumber);
                    dialogView.tvCallStatus.setText(dialogView.mContext.getString(R.string.cx62_bt_calling_state_incoming));
                    dialogView.frameLayout.removeAllViews();
                    dialogView.frameLayout.addView(LayoutInflater.from(dialogView.mContext).inflate(R.layout.call_incoming_btn_view, null));
                    dialogView.initIncomingClickListener();
                    break;
                case CALLING_DIALING:
                    if (!dialogView.isShowing()) {
                        dialogView.show();
                    }
                    dialogView.setChronometerView();
                    dialogView.tvCallName.setText(dialogView.firstName);
                    dialogView.tvCallNumber.setText(dialogView.firstNumber);
                    dialogView.tvCallStatus.setText(dialogView.mContext.getString(R.string.cx62_bt_calling_state_dialing));
                    dialogView.frameLayout.removeAllViews();
                    dialogView.frameLayout.addView(LayoutInflater.from(dialogView.mContext).inflate(R.layout.call_dialing_btn_view, null));
                    dialogView.initDialingClickListener();
                    break;
                case CALLING_ACTIVE:
                    if (!dialogView.isShowing()) {
                        dialogView.show();
                    }
                    //接通电话启动计时器
                    dialogView.startCallTime(msg.getData());
                    dialogView.tvCallStatus.setText(dialogView.mContext.getString(R.string.cx62_bt_calling_state_active));
                    dialogView.frameLayout.removeAllViews();
                    dialogView.frameLayout.addView(LayoutInflater.from(dialogView.mContext).inflate(R.layout.call_active_btn_view, null));
                    dialogView.initActiveClickListener();
                    break;
                case DIALOG_DISMISS:
                    if (dialogView.isShowing()) {
                        dialogView.dismiss();
                        dialogView.recoverSetting();
                    }
                    break;

                case CALLING_HOLD:
                    dialogView.tvCallName.setText(dialogView.firstName);
                    dialogView.tvCallNumber.setText(dialogView.firstNumber);
                    break;

                case CALLING_WAITING:
                    dialogView.tvCallName.setText(dialogView.secondName);
                    dialogView.tvCallNumber.setText(dialogView.secondNumber);
                    break;

                case QUERY_NAME_FOR_STATUS:
                    dialogView.queryNameForStatus();
                    break;

                case RECOVER_STATUS:
                    dialogView.recoverStatus();
                    break;

                case AUTO_ANSWER:
                    dialogView.btBaseUiCommandMethod.reqHfpAnswerCall(0);
                    break;

                case REFRESH_HFP_AUDIO_STATUS:
                    if (msg.getData().getBoolean("HFP_AUDIO_STATUS")) {
                        if (dialogView.ibActiveAudioStream != null) {
                            dialogView.ibActiveAudioStream.setImageResource(R.drawable.icon_call_phone);
                        }
                    } else {
                        if (dialogView.ibActiveAudioStream != null) {
                            dialogView.ibActiveAudioStream.setImageResource(R.drawable.icon_call_car);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 恢复设置
     */
    private void recoverSetting() {
        Logger.d(TAG, "recoverSetting: ");

        if (llCallMessage != null) {
            llCallMessage.setVisibility(View.VISIBLE);
        }

        if (llCallKeyboard != null) {
            llCallKeyboard.setVisibility(View.INVISIBLE);
        }

        if (chronometer != null) {
            chronometer.setVisibility(View.INVISIBLE);
        }

        if (ibActiveMute != null) {
            ibActiveMute.setImageResource(R.drawable.icon_call_mic);
        }

        if (ibActiveAudioStream != null) {
            ibActiveAudioStream.setImageResource(R.drawable.icon_call_car);
        }

        if (tvCallImport != null) {
            tvCallImport.setText("");
        }

        chronometer.setBase(SystemClock.elapsedRealtime());
        btBaseUiCommandMethod.muteHfpMic(false);
        btBaseUiCommandMethod.reqHfpAudioTransferToCarkit();
    }

    private void initActiveClickListener() {
        ImageButton ibActiveHangup = findViewById(R.id.ibActiveHangup);
        ibActiveMute = findViewById(R.id.ibActiveMute);
        ibActiveKeyboard = findViewById(R.id.ibActiveKeyboard);
        ibActiveAudioStream = findViewById(R.id.ibActiveAudioStream);
        llCallMessage = findViewById(R.id.llCallMessage);
        llCallKeyboard = findViewById(R.id.llCallKeyboard);

        ibActiveHangup.setOnClickListener(this);
        ibActiveMute.setOnClickListener(this);
        ibActiveKeyboard.setOnClickListener(this);
        ibActiveAudioStream.setOnClickListener(this);

        GridView gvCallKeyboard = findViewById(R.id.gvCallKeyboard);
        tvCallImport = findViewById(R.id.tvCallImport);

        gvCallKeyboard.setOnItemClickListener(this);
        gvCallKeyboard.setOnItemLongClickListener(this);
        gvCallKeyboard.setAdapter(new KeyboardGradViewAdapter(getContext(), BtConstant.ArrayList.keyboardData));

        boolean hfpMicMute = btBaseUiCommandMethod.isHfpMicMute();
        Log.d(TAG, "initActiveClickListener: " + hfpMicMute);
        if (hfpMicMute) {
            ibActiveMute.setImageResource(R.drawable.icon_call_mic_mute);
        } else {
            ibActiveMute.setImageResource(R.drawable.icon_call_mic);
        }

        int hfpAudioConnectionState = btBaseUiCommandMethod.getHfpAudioConnectionState();
        if (hfpAudioConnectionState == NfDef.STATE_CONNECTED) {//车机接通
            ibActiveAudioStream.setImageResource(R.drawable.icon_call_car);
            btBaseUiCommandMethod.reqHfpAudioTransferToCarkit();
        } else if (hfpAudioConnectionState == NfDef.STATE_READY) {//手机接通
            btBaseUiCommandMethod.reqHfpAudioTransferToPhone();
            ibActiveAudioStream.setImageResource(R.drawable.icon_call_phone);
        }
    }

    private void initDialingClickListener() {
        ImageButton ibDialingHangup = findViewById(R.id.ibDialingHangup);

        ibDialingHangup.setOnClickListener(this);
    }

    private void setTextColor() {
        tvCallNumber.setTextColor(Color.GRAY);
        tvCallStatus.setTextColor(Color.GRAY);
        chronometer.setTextColor(Color.GRAY);
    }

    private void setChronometerView() {
        chronometer.setVisibility(View.INVISIBLE);
    }

    private void startCallTime(Bundle data) {
        long baseTime = data.getLong("baseTime");
        Log.d(TAG, "startCallTime: " + baseTime);
        if (baseTime == 0) {
            chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
        } else {
            chronometer.setBase(baseTime);
        }
        chronometer.start();
        chronometer.setVisibility(View.VISIBLE);
    }

    private void stopCallTime() {
        chronometer.stop();
    }

    public void setCallStatus(BtConstant.CallStatus callStatus, int id) {
        Log.d(TAG, "setCallStatus: 调用方法 " + callStatus.name());
        String number = mContext.getString(R.string.cx62_bt_unknown);

        switch (callStatus) {
            case INCOMING:
                callStatusNow = INCOMING;
                number = getCallInformation(id);
                callHandler.sendEmptyMessage(CALLING_INCOMING);
                boolean inBandRingtoneSupport = btBaseUiCommandMethod.isHfpInBandRingtoneSupport();
                Logger.d(TAG, "inBandRingtoneSupport: " + inBandRingtoneSupport);
                if (!inBandRingtoneSupport) {
                    playRing();
                }
                checkAutoAnswer();
                Logger.d(TAG, "setCallStatus: 来电" + number);
                break;
            case DIALING:
                callStatusNow = DIALING;
                getCallInformation(id);
                callHandler.sendEmptyMessage(CALLING_DIALING);
                Logger.d(TAG, "setCallStatus: 去电" + number);
                break;
            case ACTIVE:
                callStatusNow = ACTIVE;
                getCallInformation(id);
                if (isFirstCall) {
                    callHandler.sendEmptyMessage(CALLING_ACTIVE);
                    isFirstCall = false;
                }
                stopRing();
                Logger.d(TAG, "setCallStatus: 接通 " + id);
                break;
            case TERMINATED:
                callStatusNow = TERMINATED;
                callHandler.sendEmptyMessage(CALLING_TERMINATED);
                callHandler.sendEmptyMessage(RECOVER_STATUS);
                stopRing();
                Logger.d(TAG, "setCallStatus: 挂断");
                isFirstCall = true;
                break;
        }
    }

    private String getCallInformation(int id) {
        if (id == 2) {
            Logger.d(TAG, "getCallInformation: 接通第三方");
            CallNameActive secondCallInformation = btStatusModel.getSecondCallInformation();
            secondName = secondCallInformation.getName();
            secondNumber = secondCallInformation.getNumber();
            callHandler.sendEmptyMessage(CALLING_WAITING);
            return secondNumber;
        } else if (id == 1) {
            Logger.d(TAG, "getCallInformation: 接通第一方");
            CallNameActive firstCallInformation = btStatusModel.getFirstCallInformation();
            firstName = firstCallInformation.getName();
            firstNumber = firstCallInformation.getNumber();
            callHandler.sendEmptyMessage(CALLING_HOLD);
            return firstNumber;
        }

        return "";
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.d(TAG, "dismiss: ");
        isFirstCall = true;
    }

    public void switchCallView(long chronometerBase) {
        callStatusNow = ACTIVE;
        sendChronometerBaseMessage(chronometerBase);
        stopRing();
        Logger.d(TAG, "switchCallView: 接通");
    }

    public void stopRing() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                try {
                    mediaPlayer.stop();
                    fileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private MediaPlayer mediaPlayer;
    private AssetFileDescriptor fileDescriptor;

    /**
     * 播报铃声
     */
    private void playRing() {
        com.semisky.autoservice.manager.AudioManager.getInstance().openStreamVolume(STREAM_BT_RING);
        try {
            mediaPlayer = new MediaPlayer();
            AssetManager assets = mContext.getAssets();
            fileDescriptor = assets.openFd("Pyxis.ogg");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动接听功能
     */
    private void checkAutoAnswer() {
        boolean autoAnswerStateSP = BtSPUtil.getInstance().getAutoAnswerStateSP(mContext);
        Log.d(TAG, "checkAutoAnswer: " + autoAnswerStateSP);
        if (autoAnswerStateSP && callStatusNow == INCOMING) {
            callHandler.sendEmptyMessageDelayed(AUTO_ANSWER, 5000);
        }
    }

    private void sendChronometerBaseMessage(long baseTime) {
        Message message = new Message();
        message.what = CALLING_ACTIVE;
        Bundle bundle = new Bundle();
        bundle.putLong("baseTime", baseTime);
        message.setData(bundle);
        callHandler.sendMessage(message);
    }

    private void recoverStatus() {
        btStatusModel.setCallNumber(mContext.getString(R.string.cx62_bt_unknown));
        btStatusModel.setCallName(mContext.getString(R.string.cx62_bt_unknown));
    }

    private void initHFPStatusListener() {
        HFPAudioStateInterface hfpAudioStateInterface = new HFPAudioStateInterface() {
            @Override
            public void privateStatus() {
                sendMessageForHFPStatus(true);
            }

            @Override
            public void handFreeStatus() {
                sendMessageForHFPStatus(false);
            }
        };

        BtHfpModel.getInstance().setHFPAudioStateInterface(hfpAudioStateInterface);
    }

    private void sendMessageForHFPStatus(boolean flag) {
        if (callStatusNow != TERMINATED) {
            Message message = new Message();
            message.what = REFRESH_HFP_AUDIO_STATUS;
            Bundle bundle = new Bundle();
            bundle.putBoolean("HFP_AUDIO_STATUS", flag);
            message.setData(bundle);
            callHandler.sendMessage(message);
        }
    }

    /**
     * 从常量类拿去姓名
     */
    private void queryNameForStatus() {
        String callName = btStatusModel.getCallName();
        if (callName != null) {
            tvCallName.setText(callName);
        }
    }

    private void initIncomingClickListener() {
        ImageButton ibIncomingAnswer = findViewById(R.id.ibIncomingAnswer);
        ImageButton ibIncomingHangup = findViewById(R.id.ibIncomingHangup);

        ibIncomingAnswer.setOnClickListener(this);
        ibIncomingHangup.setOnClickListener(this);
    }
}
