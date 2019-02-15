package com.smk.autoradio.views.activity;

import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.presenter.RadioListPresenter;

import java.util.List;

public class RadioListActivity extends BaseActivity<IRadioListView,RadioListPresenter<IRadioListView>> implements IRadioListView{

    @Override
    protected int getLayoutResID() {
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected RadioListPresenter<IRadioListView> createPresenter() {
        return new RadioListPresenter();
    }

    @Override
    public void onChangeChannelList(List<ChannelInfo> channelList) {

    }
}
