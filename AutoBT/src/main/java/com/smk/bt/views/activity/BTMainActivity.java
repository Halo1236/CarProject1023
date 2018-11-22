package com.smk.bt.views.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.smk.bt.R;
import com.smk.bt.presenter.BTMainPresenter;
import com.smk.bt.views.custom.BTBottombar;
import com.smk.bt.views.fragment.BTCallLogListFragment;
import com.smk.bt.views.fragment.BTContractsListFragment;
import com.smk.bt.views.fragment.BTDeviceListFragment;
import com.smk.bt.views.fragment.BTDialpadFragment;
import com.smk.bt.views.fragment.BTMusicPlayerFragment;
import com.smk.bt.views.fragment.BTSettingFragment;

public class BTMainActivity extends BaseActivity<IBTMainView,BTMainPresenter<IBTMainView>> implements IBTMainView,BTBottombar.OnTabChangeListener{

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Fragment[] mFragments = new Fragment[FRAGMENT_TATAL];
    private static String[] mFragmentTag = new String[FRAGMENT_TATAL];

    static {
        mFragmentTag[FRAGMENT_DEVICE_LIST_FLAG] = BTDeviceListFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_DIALPAD_FLAG] = BTDialpadFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_CONTACTS_FLAG] = BTContractsListFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_CALLLOG_FLAG] = BTCallLogListFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_MUSIC_FLAG] = BTMusicPlayerFragment.class.getSimpleName();
        mFragmentTag[FRAGMENT_SETTING_FLAG] = BTSettingFragment.class.getSimpleName();
    }

    // init first fragment flag
    private int mCurrentFragmentFlag = FRAGMENT_DEVICE_LIST_FLAG;

    private BTBottombar mBTBottombar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected BTMainPresenter<IBTMainView> createPresenter() {
        return new BTMainPresenter();
    }


    @Override
    protected void initViews() {
        this.mBTBottombar = findViewById(R.id.btBottombar);

    }

    @Override
    protected void initListeners() {
        mBTBottombar.setOnTabChangeListener(this);
    }

    @Override
    public void handlerIntent(Intent intent) {
        if(isBindPresenter()){
            mPresenter.handlerIntent(intent);
        }
    }

    @Override
    public void initShowFragment(int fragmentFlag) {
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
        mFragments[FRAGMENT_CALLLOG_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_CALLLOG_FLAG]);
        mFragments[FRAGMENT_MUSIC_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_MUSIC_FLAG]);
        mFragments[FRAGMENT_SETTING_FLAG] = mFragmentManager.findFragmentByTag(mFragmentTag[FRAGMENT_SETTING_FLAG]);

        mFragments[FRAGMENT_DEVICE_LIST_FLAG] = (null != mFragments[FRAGMENT_DEVICE_LIST_FLAG] ? mFragments[FRAGMENT_DEVICE_LIST_FLAG] : new BTDeviceListFragment());
        mFragments[FRAGMENT_DIALPAD_FLAG] = (null != mFragments[FRAGMENT_DIALPAD_FLAG] ? mFragments[FRAGMENT_DIALPAD_FLAG] : new BTDialpadFragment());
        mFragments[FRAGMENT_CONTACTS_FLAG] = (null != mFragments[FRAGMENT_CONTACTS_FLAG] ? mFragments[FRAGMENT_CONTACTS_FLAG] : new BTContractsListFragment());
        mFragments[FRAGMENT_CALLLOG_FLAG] = (null != mFragments[FRAGMENT_CALLLOG_FLAG] ? mFragments[FRAGMENT_CALLLOG_FLAG] : new BTCallLogListFragment());
        mFragments[FRAGMENT_MUSIC_FLAG] = (null != mFragments[FRAGMENT_MUSIC_FLAG] ? mFragments[FRAGMENT_MUSIC_FLAG] : new BTMusicPlayerFragment());
        mFragments[FRAGMENT_SETTING_FLAG] = (null != mFragments[FRAGMENT_SETTING_FLAG] ? mFragments[FRAGMENT_SETTING_FLAG] : new BTSettingFragment());

        if (!mFragments[fragmentFlag].isAdded()) {
            mFragmentTransaction.add(R.id.fragment_container, mFragments[fragmentFlag], mFragmentTag[fragmentFlag]).commitAllowingStateLoss();
        }
    }

    @Override
    public void switchFragment(int fragmentFlag) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (!mFragments[fragmentFlag].isAdded()) {
            mFragmentTransaction.add(R.id.fragment_container, mFragments[fragmentFlag], mFragmentTag[fragmentFlag]);
        }
        mFragmentTransaction.hide(mFragments[mCurrentFragmentFlag]).show(mFragments[fragmentFlag]).commitAllowingStateLoss();
        mCurrentFragmentFlag = fragmentFlag;
    }

    @Override
    public void onTabChange(int tabFlag) {
        if(isBindPresenter()){
            switch (tabFlag){
                case BTBottombar.TAB_DEVICE_LIST:
                    mPresenter.switchFragment(FRAGMENT_DEVICE_LIST_FLAG);
                    break;
                case BTBottombar.TAB_DIALPAD:
                    mPresenter.switchFragment(FRAGMENT_DIALPAD_FLAG);
                    break;
                case BTBottombar.TAB_CONSTRACTS_LIST:
                    mPresenter.switchFragment(FRAGMENT_CONTACTS_FLAG);
                    break;
                case BTBottombar.TAB_CALLLOG_LIST:
                    mPresenter.switchFragment(FRAGMENT_CALLLOG_FLAG);
                    break;
                case BTBottombar.TAB_MUSIC:
                    mPresenter.switchFragment(FRAGMENT_MUSIC_FLAG);
                    break;
                case BTBottombar.TAB_SETTINGS:
                    mPresenter.switchFragment(FRAGMENT_SETTING_FLAG);
                    break;
            }
        }
    }

    @Override
    public void showBTConnectedBottombar() {
        mBTBottombar.onShowBTConnectedBottombarEnable(true);
    }

    @Override
    public void showBTDisconnectedBottombar() {
        mBTBottombar.onShowBTConnectedBottombarEnable(false);
    }
}
