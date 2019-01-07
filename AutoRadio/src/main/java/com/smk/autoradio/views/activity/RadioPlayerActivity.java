package com.smk.autoradio.views.activity;

import android.view.View;
import android.widget.Button;

import com.smk.autoradio.R;
import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.presenter.RadioPlayerPresenter;

import java.util.List;

public class RadioPlayerActivity extends BaseActivity<IRadioPlayerView, RadioPlayerPresenter<IRadioPlayerView>> implements IRadioPlayerView, View.OnClickListener {

    private Button
            btn_switch_band,
            btn_set_eq,
            btn_scan,
            btn_switch_loc_or_dx,
            btn_prev,
            btn_next;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_radio_player;
    }

    @Override
    protected void initViews() {
        btn_switch_band = (Button) findViewById(R.id.btn_switch_band);
        btn_set_eq = (Button) findViewById(R.id.btn_set_eq);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_switch_loc_or_dx = (Button) findViewById(R.id.btn_switch_loc_or_dx);
        btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_switch_band.setOnClickListener(this);
        btn_set_eq.setOnClickListener(this);
        btn_scan.setOnClickListener(this);
        btn_switch_loc_or_dx.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        btn_next.setOnClickListener(this);

    }

    @Override
    protected RadioPlayerPresenter<IRadioPlayerView> createPresenter() {
        return new RadioPlayerPresenter();
    }

    @Override
    protected void initData() {
        if (isBindPresenter()) {
            mPrestener.initRadio();
        }
    }

    @Override
    public void onClick(View v) {
        if (!isBindPresenter()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_switch_band:
                mPrestener.reqSwitchBand();
                break;
            case R.id.btn_set_eq:
                mPrestener.reqSettingEQ();
                break;
            case R.id.btn_scan:
                mPrestener.reqFullSearch();
                break;
            case R.id.btn_switch_loc_or_dx:
                mPrestener.reqSwitchDxOrLoc();
                break;
            case R.id.btn_prev:
                mPrestener.reqPrevStrongChannel();
                break;
            case R.id.btn_next:
                mPrestener.reqNextStroneChannel();
                break;
        }
    }

    @Override
    public void changeChannelType(int type) {

    }

    @Override
    public void changeFavorite(boolean isFavorite) {

    }

    @Override
    public void changeSoundtrack(int soundtrackType) {

    }

    @Override
    public void changeDxLocType(int type) {

    }

    @Override
    public void changeChannelList(List<ChannelInfo> channelList) {

    }
}
