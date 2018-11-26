package com.semisky.bluetoothproject.view;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.entity.CallNameActive;
import com.semisky.bluetoothproject.entity.ContactsEntity;
import com.semisky.bluetoothproject.model.BtStatusModel;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;

import org.litepal.LitePal;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import static com.semisky.autoservice.manager.AudioManager.STREAM_BT_RING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.ACTIVE;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.DIALING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.INCOMING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.TERMINATED;

/**
 * Created by chenhongrui on 2018/10/8
 * <p>
 * 内容摘要: 导航页面悬浮通话页面
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CallFullScreenView implements View.OnClickListener {

    private static final String TAG = Logger.makeTagLog(CallFullScreenView.class);

    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private View callView;

    private BtConstant.CallStatus callStatusNow = BtConstant.CallStatus.NULL;
    private String number;
    private Context mContext;
    private CallViewHandler callHandler;
    private BtStatusModel btStatusModel;

    private TextView tvScreenName;
    private TextView tvScreenNumber;
    private TextView tvScreenState;
    private BtBaseUiCommandMethod btBaseUiCommandMethod;
    private ImageButton ibCallHangup;
    private ImageButton ibCallAnswer;
    private Chronometer chronometer;

    private boolean isAddView;

    private boolean isFirstCall = true;

    /**
     * 第三方来电
     */
    private String secondNumber, secondName;

    /**
     * 正常来电
     */
    private String firstNumber, firstName;

    /**
     * 当前正在通话的id
     */
    private int id;

    public CallFullScreenView(Context context) {
        this.mContext = context;
        callHandler = new CallViewHandler(this);
        init();
    }

    private void init() {
        btStatusModel = BtStatusModel.getInstance();

        mParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //设置window type
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        mParams.gravity = Gravity.START | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mParams.x = 0;
        mParams.y = 0;

        //设置悬浮窗口长宽数据
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        callView = layoutInflater.inflate(R.layout.full_screen_call_view, null);
        initView();
    }

    public int getId() {
        Logger.d(TAG, "getId: " + id);
        return id;
    }

    public void setId(int id) {
        Logger.d(TAG, "setId: " + id);
        this.id = id;
    }

    public void setCallStatus(BtConstant.CallStatus callStatus, int id) {
        String number = mContext.getString(R.string.cx62_bt_unknown);
        setId(id);

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
                Logger.d(TAG, "setCallStatus: 来电");
                break;
            case DIALING:
                callStatusNow = DIALING;
                getCallInformation(id);
                callHandler.sendEmptyMessage(CALLING_DIALING);
                Logger.d(TAG, "setCallStatus: 去电");
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
            case WAITING:
                callStatusNow = ACTIVE;

                break;
        }
    }

    private String getCallInformation(int id) {
        if (id == 2) {
            CallNameActive secondCallInformation = btStatusModel.getSecondCallInformation();
            secondName = secondCallInformation.getName();
            secondNumber = secondCallInformation.getNumber();
            callHandler.sendEmptyMessage(CALLING_WAITING);
            Logger.d(TAG, "getCallInformation: 接通第三方 firstName " + secondName + " secondNumber " + secondNumber);
            return secondNumber;
        } else if (id == 1) {
            CallNameActive firstCallInformation = btStatusModel.getFirstCallInformation();
            firstName = firstCallInformation.getName();
            firstNumber = firstCallInformation.getNumber();
            callHandler.sendEmptyMessage(CALLING_HOLD);
            Logger.d(TAG, "getCallInformation: 接通第一方 firstName " + firstName + " secondNumber " + firstNumber);
            return firstNumber;
        }

        return "";
    }

    private void stopRing() {
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

    private void initView() {
        tvScreenName = callView.findViewById(R.id.tvScreenName);
        tvScreenNumber = callView.findViewById(R.id.tvScreenNumber);
        tvScreenState = callView.findViewById(R.id.tvScreenState);

        ibCallHangup = callView.findViewById(R.id.ibCallHangup);
        ibCallAnswer = callView.findViewById(R.id.ibCallAnswer);
        chronometer = callView.findViewById(R.id.chronometer);
    }

    public void setBtBaseUiCommandMethod(BtBaseUiCommandMethod btBaseUiCommandMethod) {
        this.btBaseUiCommandMethod = btBaseUiCommandMethod;
    }

    private static final int CALLING_TERMINATED = 0;
    private static final int CALLING_ACTIVE = 1;
    private static final int DIALOG_DISMISS = 2;
    private static final int CALLING_INCOMING = 4;
    private static final int CALLING_BT_TERMINATED = 5;
    private static final int CALLING_DIALING = 6;
    private static final int QUERY_NAME_FOR_DATABASE = 7;
    private static final int QUERY_NAME_FOR_STATUS = 8;
    private static final int RECOVER_STATUS = 9;
    private static final int AUTO_ANSWER = 10;
    private static final int CALLING_WAITING = 11;
    private static final int CALLING_HOLD = 12;

    private static class CallViewHandler extends Handler {
        WeakReference<CallFullScreenView> mReference;

        CallViewHandler(CallFullScreenView dialog) {
            this.mReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CallFullScreenView callView = mReference.get();
            List<NfHfpClientCall> callList = callView.btBaseUiCommandMethod.getHfpCallList();

            switch (msg.what) {
                case CALLING_TERMINATED:
                    //第三方挂断也会走这里
                    if (callList.size() == 0) {
                        //挂断后将通话时间归零
                        callView.stopCallTime();
                        callView.setTextColor();
                        callView.tvScreenState.setText(callView.mContext.getString(R.string.cx62_bt_calling_state_over));
                        callView.callHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 1500);
                    }
                    break;
                case CALLING_BT_TERMINATED:
                    if (callList.size() == 0) {
                        //通话中断开蓝牙将页面退到后台
                        callView.callHandler.sendEmptyMessage(DIALOG_DISMISS);
                    }
                    break;
                case CALLING_INCOMING:
                    callView.addView();
                    callView.tvScreenState.setText(callView.mContext.getString(R.string.cx62_bt_calling_state_incoming));
                    callView.initIncomingClickListener();
                    break;
                case CALLING_DIALING:
                    callView.addView();
                    callView.tvScreenState.setText(callView.mContext.getString(R.string.cx62_bt_calling_state_dialing));
                    callView.initDialingClickListener();
                    break;
                case CALLING_ACTIVE:
                    //接通电话启动计时器
                    callView.startCallTime();
                    callView.tvScreenState.setText(callView.mContext.getString(R.string.cx62_bt_calling_state_active));
                    callView.initActiveClickListener();
                    break;
                case DIALOG_DISMISS:
                    callView.removeView();
                    break;

                case QUERY_NAME_FOR_DATABASE:
                    Bundle data = msg.getData();
                    String number = data.getString("number");
                    callView.queryNameForDatabase(number);
                    break;

                case QUERY_NAME_FOR_STATUS:
                    callView.queryNameForStatus();
                    break;

                case RECOVER_STATUS:
                    callView.recoverStatus();
                    break;

                case CALLING_HOLD:
                    callView.tvScreenName.setText(callView.firstName);
                    callView.tvScreenNumber.setText(callView.firstNumber);
                    break;

                case CALLING_WAITING:
                    callView.tvScreenName.setText(callView.secondName);
                    callView.tvScreenNumber.setText(callView.secondNumber);
                    break;

                case AUTO_ANSWER:
                    callView.btBaseUiCommandMethod.reqHfpAnswerCall(0);
                    break;
            }
        }
    }

    public long getChronometerBase() {
        return chronometer.getBase();
    }

    private void setTextColor() {
        tvScreenName.setTextColor(Color.GRAY);
        tvScreenNumber.setTextColor(Color.GRAY);
        tvScreenState.setTextColor(Color.GRAY);
        chronometer.setTextColor(Color.GRAY);
    }

    public void removeView() {
        if (isAddView) {
            mWindowManager.removeView(callView);
            isAddView = false;
        }
    }

    private void addView() {
        if (!isAddView) {
            mWindowManager.addView(callView, mParams);
            isAddView = true;
        }
    }

    private void initActiveClickListener() {
        ibCallHangup.setVisibility(View.VISIBLE);
        ibCallAnswer.setVisibility(View.INVISIBLE);
        chronometer.setVisibility(View.VISIBLE);

        ibCallHangup.setOnClickListener(this);
    }

    private void initDialingClickListener() {
        ibCallHangup.setVisibility(View.VISIBLE);
        ibCallAnswer.setVisibility(View.INVISIBLE);
        chronometer.setVisibility(View.INVISIBLE);

        ibCallHangup.setOnClickListener(this);
    }

    private void initIncomingClickListener() {
        ibCallHangup.setVisibility(View.VISIBLE);
        ibCallAnswer.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.INVISIBLE);

        ibCallHangup.setOnClickListener(this);
        ibCallAnswer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibCallAnswer:
                btBaseUiCommandMethod.reqHfpAnswerCall(0);
                break;
            case R.id.ibCallHangup:
                callHangup();
                break;
        }
    }

    private void callHangup() {
        btBaseUiCommandMethod.reqHfpTerminateCurrentCall();
        callHandler.sendEmptyMessage(DIALOG_DISMISS);
    }

    /**
     * 来电去数据库查询联系人名字
     */
    private void queryNameForDatabase(String number) {
        boolean isSaveContact = BtSPUtil.getInstance().getSyncContactsStateSP(mContext);
        Logger.d(TAG, "QUERY_NAME_FOR_DATABASE: number " + number);
        if (isSaveContact && number != null) {
            List<ContactsEntity> contactsEntities = LitePal.select("fullName", "number")
                    .where("number = ?", number).find(ContactsEntity.class);
            if (contactsEntities.size() > 0) {
                for (ContactsEntity contactsEntity : contactsEntities) {
                    Logger.d(TAG, "QUERY_NAME_FOR_DATABASE:FullName " + contactsEntity.getFullName());
                    tvScreenName.setText(contactsEntity.getFullName());
                }
            }
        } else {
            tvScreenName.setText(mContext.getString(R.string.cx62_bt_unknown));
        }
    }

    private void startCallTime() {
        chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
        chronometer.start();
        chronometer.setVisibility(View.VISIBLE);
    }

    private void stopCallTime() {
        chronometer.stop();
    }

    /**
     * 从常量类拿去姓名
     */
    private void queryNameForStatus() {
        String callName = btStatusModel.getCallName();
        if (callName != null) {
            tvScreenName.setText(callName);
        }
    }

    private void recoverStatus() {
        btStatusModel.setCallNumber(mContext.getString(R.string.cx62_bt_unknown));
        btStatusModel.setCallName(mContext.getString(R.string.cx62_bt_unknown));
    }

}
