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

    private FrameLayout llActivityBottom;

    private BtServiceManager btServiceManager;

    private BtSettingFragment mBtSettingFragment;
    private BtDeviceSearchFragment mBtDeviceSearchFragment;

    private BtNumberKeyboardFragment mBtNumberKeyboardFragment;
    private BtTelephoneContactFragment mBtTelephoneContactFragment;
    private BtCallRecordsFragment mBtCallRecordsFragment;
    private BtMusicFragment mBtMusicFragment;

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

        BtMiddleSettingManager.getInstance().setAppStatusInForeground(
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

    public void initDevice() {
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

        if (mBtDeviceSearchFragment == null) {
            mBtDeviceSearchFragment = new BtDeviceSearchFragment();
        }
        switchFragment(mBtDeviceSearchFragment);
    }

    private void initMainView() {
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
            showFragment(BtConstant.FragmentFlag.MUSIC);
        } else {
            showFragment(btStatusModel.getFragmentFlag());
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
//                finish(); 7627
//                overridePendingTransition(0, 0);
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

        BtMiddleSettingManager.getInstance().setAppStatusInBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy: ");
//        BtMiddleSettingManager.getInstance().setAppStatusInDestroy();
        BtMusicAudioFocusModel.getINSTANCE().unRegisterAudioLessListener(onBtAudioEventListener);
        btServiceManager.unbindService();
        unregisterReceiver(mCustomBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibDevicesSetting:
                Logger.d(TAG, "ibDevicesSetting: " + ibDevicesSetting);
                if (mBtSettingFragment == null) {
                    mBtSettingFragment = new BtSettingFragment();
                }
                switchFragment(mBtSettingFragment);
                break;
            case R.id.ibDevicesSelector:
                Logger.d(TAG, "ibDevicesSetting: " + ibDevicesSetting);
                if (mBtDeviceSearchFragment == null) {
                    mBtDeviceSearchFragment = new BtDeviceSearchFragment();
                }
                switchFragment(mBtDeviceSearchFragment);
                break;
            case R.id.ibMainKeyboard:
                showFragment(BtConstant.FragmentFlag.KEYBOARD);
                break;
            case R.id.ibMainContact:
                Logger.d(TAG, "ibDevicesSetting: " + ibDevicesSetting);
                showFragment(BtConstant.FragmentFlag.CONTACT);
                break;
            case R.id.ibMainRecords:
                showFragment(BtConstant.FragmentFlag.CALL);
                break;
            case R.id.ibMainMusic:
                showFragment(BtConstant.FragmentFlag.MUSIC);
                break;
            case R.id.ibMainSetting:
                showFragment(BtConstant.FragmentFlag.SET);
                break;

            default:

                break;
        }
    }

    private Fragment currentFragment;

    private void showFragment(BtConstant.FragmentFlag fragmentFlag) {
        switch (fragmentFlag) {
            case KEYBOARD:
                Logger.d(TAG, "mBtNumberKeyboardFragment: " + mBtNumberKeyboardFragment);
                if (mBtNumberKeyboardFragment == null) {
                    mBtNumberKeyboardFragment = new BtNumberKeyboardFragment();
                }
                switchFragment(mBtNumberKeyboardFragment);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.KEYBOARD);
                BtMiddleSettingManager.getInstance().setBtMusicStatus(false);
                break;
            case CONTACT:
                Logger.i(TAG, "-------------mBtTelephoneContactFragment:" + mBtTelephoneContactFragment);
                if (mBtTelephoneContactFragment == null) {
                    mBtTelephoneContactFragment = new BtTelephoneContactFragment();
                }
                switchFragment(mBtTelephoneContactFragment);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.CONTACT);
                BtMiddleSettingManager.getInstance().setBtMusicStatus(false);
                break;
            case CALL:
                Logger.d(TAG, "mBtCallRecordsFragment: " + mBtCallRecordsFragment);
                if (mBtCallRecordsFragment == null) {
                    mBtCallRecordsFragment = new BtCallRecordsFragment();
                }
                switchFragment(mBtCallRecordsFragment);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.CALL);
                BtMiddleSettingManager.getInstance().setBtMusicStatus(false);
                break;
            case MUSIC:
                Logger.d(TAG, "mBtMusicFragment: " + mBtMusicFragment);
                if (mBtMusicFragment == null) {
                    mBtMusicFragment = new BtMusicFragment();
                }
                switchFragment(mBtMusicFragment);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.MUSIC);
                BtMiddleSettingManager.getInstance().setBtMusicStatus(true);
                break;
            case SET:
                Logger.d(TAG, "mBtSettingFragment: " + mBtSettingFragment);
                if (mBtSettingFragment == null) {
                    mBtSettingFragment = new BtSettingFragment();
                }
                switchFragment(mBtSettingFragment);
                btStatusModel.setFragmentFlag(BtConstant.FragmentFlag.SET);
                BtMiddleSettingManager.getInstance().setBtMusicStatus(false);
                break;
        }
    }

    private void switchFragment(Fragment targetFragment) {
        if (targetFragment == currentFragment) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            if (null != currentFragment) {
                transaction.hide(currentFragment);
            }
            transaction
                    .add(R.id.home_fragment, targetFragment)
                    .commitAllowingStateLoss();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commitAllowingStateLoss();
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
