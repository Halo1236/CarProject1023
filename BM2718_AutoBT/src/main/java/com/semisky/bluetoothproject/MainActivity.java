package com.semisky.bluetoothproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.manager.BtServiceManager;
import com.semisky.bluetoothproject.model.BtMusicAudioFocusModel;
import com.semisky.bluetoothproject.model.BtStatusModel;
import com.semisky.bluetoothproject.model.modelInterface.OnBtAudioEventListener;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;
import com.semisky.bluetoothproject.utils.Logger;
import com.semisky.bluetoothproject.view.fragment.BtCallRecordsFragment;
import com.semisky.bluetoothproject.view.fragment.BtDeviceSearchFragment;
import com.semisky.bluetoothproject.view.fragment.BtMusicFragment;
import com.semisky.bluetoothproject.view.fragment.BtNumberKeyboardFragment;
import com.semisky.bluetoothproject.view.fragment.BtSettingFragment;
import com.semisky.bluetoothproject.view.fragment.BtTelephoneContactFragment;

import static com.semisky.bluetoothproject.constant.BtConstant.ACTION_START_ACTIVITY;
import static com.semisky.bluetoothproject.constant.BtConstant.START_ACTIVITY_VALUE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Logger.makeTagLog(MainActivity.class);

    private final static String[] MAIN_TAGS = {"KEYBOARD", "CONTACT", "CALL", "MUSIC", "SET"};
    private final static String[] DEVICE_TAGS = {"DEVICE"};

    private FrameLayout llActivityBottom;

    private BtServiceManager btServiceManager;

    private BtSettingFragment mBtSettingFragment;
    private BtDeviceSearchFragment mBtDeviceSearchFragment;

    private BtNumberKeyboardFragment mBtNumberKeyboardFragment;
    private BtTelephoneContactFragment mBtTelephoneContactFragment;
    private BtCallRecordsFragment mBtCallRecordsFragment;
    private BtMusicFragment mBtMusicFragment;
    private BtMiddleSettingManager btMiddleSettingManager;

    private BtStatusModel btStatusModel;

    private ImageView ibDevicesSetting;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        btStatusModel = BtStatusModel.getInstance();
        llActivityBottom = findViewById(R.id.llActivityBottom);
        btServiceManager = BtServiceManager.getInstance();
        btMiddleSettingManager = BtMiddleSettingManager.getInstance();

        btServiceManager.startService();
        btServiceManager.bindService();

        registerHomeWatcher();
        initBTMusicListener();

        btServiceManager.setOnServiceConnectedListener(new BtServiceManager.onServiceConnectedListener() {
            @Override
            public void onServiceConnected() {
                checkBTConnect();
                setOnBTStatusListener();
            }
        });
    }

    private void setOnBTStatusListener() {
        IBtStatusListener iBtStatusListener = new IBtStatusListener.Stub() {
            @Override
            public void stateDisconnect(String address) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initDevice();
                    }
                });
            }
        };

        btServiceManager.setOnBTStatusListener(iBtStatusListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBTConnect();

        btMiddleSettingManager.setAppStatusInForeground(
                getResources().getString(R.string.bt_app_name));
    }

    private void checkBTConnect() {
        BtBaseUiCommandMethod commandMethod = BtBaseUiCommandMethod.getInstance();
        boolean btEnabled = commandMethod.checkBtServiceConnect();

        Logger.d(TAG, "onResume: btEnabled " + btEnabled);
        if (btEnabled) {
            initMainView();
        } else {
            initDeviceView();
        }
    }

    private void initDevice() {
        mBtDeviceSearchFragment = null;//主动刷新设备页面
        initDeviceView();
    }

    private void cleanFragment() {
        mBtNumberKeyboardFragment = null;
        mBtTelephoneContactFragment = null;
        mBtCallRecordsFragment = null;
        mBtMusicFragment = null;
    }

    private void initDeviceView() {
        View deviceView = View.inflate(this, R.layout.devices_bottom_view, null);
        cleanFragment();
        llActivityBottom.removeAllViews();
        llActivityBottom.addView(deviceView);

        ImageView ibDevicesSelector = deviceView.findViewById(R.id.ibDevicesSelector);
        ibDevicesSetting = deviceView.findViewById(R.id.ibDevicesSetting);

        ibDevicesSelector.setOnClickListener(this);
        ibDevicesSetting.setOnClickListener(this);

        showDeviceFragment();
    }

    private void showDeviceFragment() {
        if (mBtDeviceSearchFragment == null) {
            Logger.d(TAG, "showDeviceFragment: ");
            mBtDeviceSearchFragment = new BtDeviceSearchFragment();
        }
        switchFragment(mBtDeviceSearchFragment, DEVICE_TAGS[0]);
    }

    private void initMainView() {
        Logger.d(TAG, "initMainView: 加载连接成功页面");
        View mainView = View.inflate(this, R.layout.main_bottom_view, null);
        llActivityBottom.removeAllViews();
        llActivityBottom.addView(mainView);

        ImageView ibMainKeyboard = mainView.findViewById(R.id.ibMainKeyboard);
        ImageView ibMainContact = mainView.findViewById(R.id.ibMainContact);
        ImageView ibMainRecords = mainView.findViewById(R.id.ibMainRecords);
        ImageView ibMainMusic = mainView.findViewById(R.id.ibMainMusic);
        ImageView ibMainSetting = mainView.findViewById(R.id.ibMainSetting);

        ibMainKeyboard.setOnClickListener(this);
        ibMainContact.setOnClickListener(this);
        ibMainRecords.setOnClickListener(this);
        ibMainMusic.setOnClickListener(this);
        ibMainSetting.setOnClickListener(this);

        loadFragment();
    }

    /**
     * fragment记忆
     */
    private void loadFragment() {
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra(ACTION_START_ACTIVITY, 0);
        Log.d(TAG, "loadFragment: " + intExtra);
        if (intExtra == START_ACTIVITY_VALUE) {
            showFragment(BtConstant.FragmentFlag.MUSIC, MAIN_TAGS[3]);
            intent.putExtra(ACTION_START_ACTIVITY, -1);
        } else {
            BtConstant.FragmentFlag fragmentFlag = btStatusModel.getFragmentFlag();
            switch (fragmentFlag) {
                case KEYBOARD:
                    showFragment(BtConstant.FragmentFlag.KEYBOARD, MAIN_TAGS[0]);
                    break;
                case CONTACT:
                    showFragment(BtConstant.FragmentFlag.CONTACT, MAIN_TAGS[1]);
                    break;
                case CALL:
                    showFragment(BtConstant.FragmentFlag.CALL, MAIN_TAGS[2]);
                    break;
                case MUSIC:
                    showFragment(BtConstant.FragmentFlag.MUSIC, MAIN_TAGS[3]);
                    break;
                case SET:
                    showFragment(BtConstant.FragmentFlag.SET, MAIN_TAGS[4]);
                    break;

            }

        }
    }

    private OnBtAudioEventListener onBtAudioEventListener;

    /**
     * 蓝牙音乐失去焦点暂停播放
     */
    private void initBTMusicListener() {
        onBtAudioEventListener = new OnBtAudioEventListener() {
            @Override
            public void onAudioLess() {
                //7627 取消 8051 打开
                //如果当前在蓝牙音乐页面才做finish动作
                if (btStatusModel.getFragmentFlag() == BtConstant.FragmentFlag.MUSIC) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onAudioRequest() {

            }

            @Override
            public void onAudioLossTransient() {
            }
        };

        BtMusicAudioFocusModel.getINSTANCE().registerAudioLessListener(onBtAudioEventListener);
    }


    public void refreshView(boolean status) {
        if (ibDevicesSetting != null) {
            if (status) {
                ibDevicesSetting.setEnabled(true);
                ibDevicesSetting.setImageResource(R.drawable.tag_set_selector);
            } else {
                showDeviceFragment();//防止极限点击到设置页面
                ibDevicesSetting.setEnabled(false);
                ibDevicesSetting.setImageResource(R.drawable.icon_bottom_setting_gray);
            }
        }
    }

    public void initView() {
        initMainView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        Fragment fragmentById = fragmentManager.findFragmentById(R.id.home_fragment);
        if (fragmentById == mBtDeviceSearchFragment) {
            mBtDeviceSearchFragment.dismissDialogFragment();
        }

        btMiddleSettingManager.setAppStatusInBackground();
        setMusicStatus();
    }

    private void setMusicStatus() {
        btMiddleSettingManager.setBtMusicStatus(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy: ");
//        btMiddleSettingManager.setAppStatusInDestroy();
        BtMusicAudioFocusModel.getINSTANCE().unRegisterAudioLessListener(onBtAudioEventListener);
        btServiceManager.unbindService();
        unregisterReceiver(mCustomBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibDevicesSetting:
                if (mBtSettingFragment == null) {
                    mBtSettingFragment = new BtSettingFragment();
                }
                switchFragment(mBtSettingFragment, MAIN_TAGS[4]);
                break;
            case R.id.ibDevicesSelector:
                showDeviceFragment();
                break;
            case R.id.ibMainKeyboard:
                showFragment(BtConstant.FragmentFlag.KEYBOARD, MAIN_TAGS[0]);
                break;
            case R.id.ibMainContact:
                showFragment(BtConstant.FragmentFlag.CONTACT, MAIN_TAGS[1]);
                break;
            case R.id.ibMainRecords:
                showFragment(BtConstant.FragmentFlag.CALL, MAIN_TAGS[2]);
                break;
            case R.id.ibMainMusic:
                showFragment(BtConstant.FragmentFlag.MUSIC, MAIN_TAGS[3]);
                break;
            case R.id.ibMainSetting:
                showFragment(BtConstant.FragmentFlag.SET, MAIN_TAGS[4]);
                break;

            default:

                break;
        }
    }

    private Fragment currentFragment;

    private void showFragment(BtConstant.FragmentFlag fragmentFlag, String tag) {
        switch (fragmentFlag) {
            case KEYBOARD:
                Logger.d(TAG, "mBtNumberKeyboardFragment: " + mBtNumberKeyboardFragment);
                if (mBtNumberKeyboardFragment == null) {
                    mBtNumberKeyboardFragment = new BtNumberKeyboardFragment();
                }
                switchFragment(mBtNumberKeyboardFragment, tag);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.KEYBOARD);
                setMusicStatus();
                break;
            case CONTACT:
                Logger.i(TAG, "-------------mBtTelephoneContactFragment:" + mBtTelephoneContactFragment);
                if (mBtTelephoneContactFragment == null) {
                    mBtTelephoneContactFragment = new BtTelephoneContactFragment();
                }
                switchFragment(mBtTelephoneContactFragment, tag);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.CONTACT);
                setMusicStatus();
                break;
            case CALL:
                Logger.d(TAG, "mBtCallRecordsFragment: " + mBtCallRecordsFragment);
                if (mBtCallRecordsFragment == null) {
                    mBtCallRecordsFragment = new BtCallRecordsFragment();
                }
                switchFragment(mBtCallRecordsFragment, tag);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.CALL);
                setMusicStatus();
                break;
            case MUSIC:
                Logger.d(TAG, "mBtMusicFragment: " + mBtMusicFragment);
                if (mBtMusicFragment == null) {
                    mBtMusicFragment = new BtMusicFragment();
                }
                switchFragment(mBtMusicFragment, tag);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.MUSIC);
                btMiddleSettingManager.setBtMusicStatus(true);
                break;
            case SET:
                Logger.d(TAG, "mBtSettingFragment: " + mBtSettingFragment);
                if (mBtSettingFragment == null) {
                    mBtSettingFragment = new BtSettingFragment();
                }
                switchFragment(mBtSettingFragment, tag);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.SET);
                setMusicStatus();
                break;
        }
    }

    private void switchFragment(Fragment targetFragment, String tag) {
        if (targetFragment == currentFragment) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //&& null == fragmentManager.findFragmentByTag(tag)
        if (!targetFragment.isAdded()
                && !getSupportFragmentManager().getFragments().contains(targetFragment)) {
            if (null != currentFragment) {
                transaction.hide(currentFragment);
            }
            transaction
                    .add(R.id.home_fragment, targetFragment, tag)
                    .commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        }
        currentFragment = targetFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private CustomBroadcastReceiver mCustomBroadcastReceiver;

    private void registerHomeWatcher() {
        if (mCustomBroadcastReceiver == null) {
            mCustomBroadcastReceiver = new CustomBroadcastReceiver();
        }
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mCustomBroadcastReceiver, homeFilter);
    }

    class CustomBroadcastReceiver extends BroadcastReceiver {
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    Log.d(TAG, "homekey");
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        }
    }
}
