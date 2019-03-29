package com.smkbt.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smkbt.R;
import com.smkbt.presenter.BTMusicPresenter;

public class BTMuiscFragment extends BaseFragment< IBTMusicView,BTMusicPresenter<IBTMusicView>> implements IBTMusicView,View.OnClickListener {
    private TextView
            tvTitle,
            tvArtist,
            tvAlbum,
            tvCurTime,
            tvTotalTime;
    private ImageButton
            btn_prev,
            btn_switch,
            btn_next;
    private ProgressBar progressBar;


    @Override
    public void onClick(View v) {
        if(!isBindPresenter()){
            return;
        }
        switch (v.getId()){
            case R.id.btn_prev:
                mPresenter.reqAvrcpBackward();
                break;
            case R.id.btn_switch:
                break;
            case R.id.btn_next:
                mPresenter.reqAvrcpForward();
                break;
        }
    }

    @Override
    protected BTMusicPresenter<IBTMusicView> createPresenter() {
        return new BTMusicPresenter();
    }

    @Override
    protected int getLayoutResId() {
        //TO DO init layout
        return R.layout.fragment_bt_music;
    }

    @Override
    protected void initViews() {
        //TO DO init ui
        btn_prev = (ImageButton)mContentView.findViewById(R.id.btn_next);
        btn_switch = (ImageButton)mContentView.findViewById(R.id.btn_switch);
        btn_next = (ImageButton)mContentView.findViewById(R.id.btn_next);
        btn_prev.setOnClickListener(this);
        btn_switch.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    protected void initState() {
        if(!isBindPresenter()){
            return;
        }
        // 这里是触发BT Music 播放的入口（包括A2DP检查，蓝牙音乐断点播放恢复）
        mPresenter.checkConnect();
    }

    @Override
    public void onChangeTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void onChangeArtist(String artist) {
        tvArtist.setText(artist);
    }

    @Override
    public void onChangeAlbum(String album) {
        tvAlbum.setText(album);
    }

    @Override
    public void onChangeProgress(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void onChangeCurrentTime(String curTime) {
        tvCurTime.setText(curTime);
    }

    @Override
    public void onChangeTotalTime(String totalTime) {
        tvTotalTime.setText(totalTime);
    }
}
