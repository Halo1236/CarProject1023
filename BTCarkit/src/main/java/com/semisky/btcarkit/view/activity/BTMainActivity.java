package com.semisky.btcarkit.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.semisky.btcarkit.R;
import com.semisky.btcarkit.service.manager.BTRoutingCommandManager;
import com.semisky.btcarkit.view.fragment.BTCallRecordFragment;
import com.semisky.btcarkit.view.fragment.BTContactsFragment;
import com.semisky.btcarkit.view.fragment.BTDeviceListFragment;
import com.semisky.btcarkit.view.fragment.BTDialpadFragment;
import com.semisky.btcarkit.view.fragment.BTMusicPlayerFragment;
import com.semisky.btcarkit.view.fragment.BTSettingFragment;

public class BTMainActivity extends FragmentActivity implements View.OnClickListener {

    // Fragments flag
    private static final int FRAGMENT_DEVICE_LIST_FLAG = 0;// 设备列表标识
    private static final int FRAGMENT_DIALPAD_FLAG = 1;// 拨号界面标识
    private static final int FRAGMENT_CONTACTS_FLAG = 2;// 联系人界面标识
    private static final int FRAGMENT_CALLRECORD_FLAG = 3;// 通话记录界面标识
    private static final int FRAGMENT_MUSIC_FLAG = 4;// 蓝牙音乐界面标识
    private static final int FRAGMENT_SETTING_FLAG = 5;// 设置界面标识
    private static final int FRAGMENT_MIN_FLAG = FRAGMENT_DEVICE_LIST_FLAG;
    private static final int FRAGMENT_MAX_FLAG = FRAGMENT_SETTING_FLAG;
    private static final int FRAGMENT_DEF_FLAG = FRAGMENT_DEVICE_LIST_FLAG;
    private static final int FRAGMENT_TATAL = 6;// Fragment 页面总数

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Fragment[] mFragments = new Fragment[FRAGMENT_TATAL];
    private static String[] mFragmentTag = new String[FRAGMENT_TATAL];

    static {
        mFragmentTag[FRAGMENT_DEVICE_LIST_FLAG] = BTDeviceListFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_DIALPAD_FLAG] = BTDialpadFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_CONTACTS_FLAG] = BTContactsFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_CALLRECORD_FLAG] = BTCallRecordFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_MUSIC_FLAG] = BTMusicPlayerFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_SETTING_FLAG] = BTSettingFragment.class.getSimpleName();
    }

    // UI
    private Button
            btn_device_list_tab,
            btn_dialpad_tab,
            btn_contacts_tab,
            btn_callrecord_tab,
            btn_musicplayer_tab,
            btn_setting_tab;

    // init first fragment flag
    private int mCurrentFragmentFlag = FRAGMENT_DEVICE_LIST_FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btmain);
        initViews();
        initListener();
        initShowFragment(FRAGMENT_MUSIC_FLAG);
    }

    private void initViews() {
        btn_device_list_tab = (Button) findViewById(R.id.btn_device_list_tab);
        btn_dialpad_tab = (Button) findViewById(R.id.btn_dialpad_tab);
        btn_contacts_tab = (Button) findViewById(R.id.btn_contacts_tab);
        btn_callrecord_tab = (Button) findViewById(R.id.btn_callrecord_tab);
        btn_musicplayer_tab = (Button) findViewById(R.id.btn_musicplayer_tab);
        btn_setting_tab = (Button) findViewById(R.id.btn_setting_tab);
    }

    private void initListener() {
        btn_device_list_tab.setOnClickListener(this);
        btn_dialpad_tab.setOnClickListener(this);
        btn_contacts_tab.setOnClickListener(this);
        btn_callrecord_tab.setOnClickListener(this);
        btn_musicplayer_tab.setOnClickListener(this);
        btn_setting_tab.setOnClickListener(this);
    }

    /**
     * 初始化显示界面
     *
     * @param fragmentFlag
     */
    private void initShowFragment(int fragmentFlag) {
        if (fragmentFlag < FRAGMENT_MIN_FLAG || fragmentFlag > FRAGMENT_MAX_FLAG) {
            fragmentFlag = FRAGMENT_DEF_FLAG;
        }
        this.mCurrentFragmentFlag = fragmentFlag;
        this.mFragmentManager = this.getSupportFragmentManager();
        this.mFragmentTransaction = this.mFragmentManager.beginTransaction();

        // Fragment tag add to array
        mFragments[FRAGMENT_DEVICE_LIST_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_DEVICE_LIST_FLAG]);
        mFragments[FRAGMENT_DIALPAD_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_DIALPAD_FLAG]);
        mFragments[FRAGMENT_CONTACTS_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_CONTACTS_FLAG]);
        mFragments[FRAGMENT_CALLRECORD_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_CALLRECORD_FLAG]);
        mFragments[FRAGMENT_MUSIC_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_MUSIC_FLAG]);
        mFragments[FRAGMENT_SETTING_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_SETTING_FLAG]);

        mFragments[FRAGMENT_DEVICE_LIST_FLAG] = (null != mFragments[FRAGMENT_DEVICE_LIST_FLAG] ? mFragments[FRAGMENT_DEVICE_LIST_FLAG] : new BTDeviceListFragment());
        mFragments[FRAGMENT_DIALPAD_FLAG] = (null != mFragments[FRAGMENT_DIALPAD_FLAG] ? mFragments[FRAGMENT_DIALPAD_FLAG] : new BTDialpadFragment());
        mFragments[FRAGMENT_CONTACTS_FLAG] = (null != mFragments[FRAGMENT_CONTACTS_FLAG] ? mFragments[FRAGMENT_CONTACTS_FLAG] : new BTContactsFragment());
        mFragments[FRAGMENT_CALLRECORD_FLAG] = (null != mFragments[FRAGMENT_CALLRECORD_FLAG] ? mFragments[FRAGMENT_CALLRECORD_FLAG] : new BTCallRecordFragment());
        mFragments[FRAGMENT_MUSIC_FLAG] = (null != mFragments[FRAGMENT_MUSIC_FLAG] ? mFragments[FRAGMENT_MUSIC_FLAG] : new BTMusicPlayerFragment());
        mFragments[FRAGMENT_SETTING_FLAG] = (null != mFragments[FRAGMENT_SETTING_FLAG] ? mFragments[FRAGMENT_SETTING_FLAG] : new BTSettingFragment());

        if (!mFragments[fragmentFlag].isAdded()) {
            mFragmentTransaction.add(R.id.fragment_container, mFragments[fragmentFlag], mFragmentTag[fragmentFlag]).commitAllowingStateLoss();
        }
    }

    /**
     * 切换界面
     *
     * @param fragmentFlag
     */
    private void switchFragment(int fragmentFlag) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (!mFragments[fragmentFlag].isAdded()) {
            mFragmentTransaction.add(R.id.fragment_container, mFragments[fragmentFlag], mFragmentTag[fragmentFlag]);
        }
        mFragmentTransaction.hide(mFragments[mCurrentFragmentFlag]).show(mFragments[fragmentFlag]).commitAllowingStateLoss();
        mCurrentFragmentFlag = fragmentFlag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_device_list_tab:
                switchFragment(FRAGMENT_DEVICE_LIST_FLAG);
                break;
            case R.id.btn_dialpad_tab:
                switchFragment(FRAGMENT_DIALPAD_FLAG);
                break;
            case R.id.btn_contacts_tab:
                switchFragment(FRAGMENT_CONTACTS_FLAG);
                break;
            case R.id.btn_callrecord_tab:
                switchFragment(FRAGMENT_CALLRECORD_FLAG);
                break;
            case R.id.btn_musicplayer_tab:
                switchFragment(FRAGMENT_MUSIC_FLAG);
                break;
            case R.id.btn_setting_tab:
                switchFragment(FRAGMENT_SETTING_FLAG);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private BTRoutingCommandManager.OnServiceStateListener mOnServiceStateListener = new BTRoutingCommandManager.OnServiceStateListener(){
        @Override
        public void onServiceConnected() {
            Toast.makeText(BTMainActivity.this,"BT SERVICE CONNECTED !!!",Toast.LENGTH_LONG).show();
        }
    };


}
