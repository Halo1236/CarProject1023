package com.semisky.bluetoothproject.view.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.entity.MusicSongStatus;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.presenter.BtMusicPresenter;
import com.semisky.bluetoothproject.presenter.viewInterface.BtMusicPlayStatusInterface;

/**
 * Created by chenhongrui on 2018/8/1
 * <p>
 * 内容摘要:蓝牙音乐fragment
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtMusicFragment extends BaseFragment<BtMusicPlayStatusInterface, BtMusicPresenter>
        implements BtMusicPlayStatusInterface, View.OnClickListener {

    private static final String TAG = "BtMusicFragment";

    private TextView tvSongName;
    private TextView tvSongAlbum;
    private TextView tvSongArtist;
    private ImageButton ibMusicPlay;
    private ImageButton ibMusicPause;

    @Override
    protected BtMusicPresenter createPresenter() {
        return new BtMusicPresenter();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            fragmentHide();
        } else {
            fragmentShow();
        }
    }

    protected void fragmentHide() {
        Log.d(TAG, "fragmentHide: ");
    }

    protected void fragmentShow() {
        Log.d(TAG, "fragmentShow: ");
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_music_fragment));
        initMusicData();
    }

    @Override
    protected void loadData() {
        Log.d(TAG, "loadData: ");
        initMusicData();
    }

    @Override
    protected void initView(View view) {
        ibMusicPlay = view.findViewById(R.id.ibMusicPlay);
        ibMusicPause = view.findViewById(R.id.ibMusicPause);
        ImageButton ibMusicLast = view.findViewById(R.id.ibMusicLast);
        ImageButton ibMusicNext = view.findViewById(R.id.ibMusicNext);
        tvSongName = view.findViewById(R.id.tvSongName);
        tvSongAlbum = view.findViewById(R.id.tvSongAlbum);
        tvSongArtist = view.findViewById(R.id.tvSongArtist);
        ibMusicPause = view.findViewById(R.id.ibMusicPause);

        ibMusicPlay.setOnClickListener(this);
        ibMusicLast.setOnClickListener(this);
        ibMusicNext.setOnClickListener(this);
        ibMusicPause.setOnClickListener(this);
    }

    @Override
    protected int getResourceId() {
        return R.layout.bt_music_fragment;
    }

    @Override
    public void songStatus(final MusicSongStatus musicSongStatus) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvSongName.setText(musicSongStatus.getSongName());
                tvSongAlbum.setText(musicSongStatus.getAlbum());
                tvSongArtist.setText(musicSongStatus.getArtist());
            }
        });
    }

    @Override
    public void startPlaying() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ibMusicPlay.setVisibility(View.INVISIBLE);
                ibMusicPause.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void stopPlay() {

    }

    @Override
    public void pausePlay() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ibMusicPlay.setVisibility(View.VISIBLE);
                ibMusicPause.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void startPlay() {
        mPresenter.playSong();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ibMusicPlay.setVisibility(View.VISIBLE);
                ibMusicPause.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void initMusicData() {
        mPresenter.initListener();
        mPresenter.reqAvrcp13GetElementAttributesPlaying();
        mPresenter.reqAvrcp13GetPlayStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        initMusicData();
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_music_fragment));
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibMusicPlay:
                mPresenter.playSong();
                break;

            case R.id.ibMusicLast:
                mPresenter.playLast();
                break;

            case R.id.ibMusicNext:
                mPresenter.playNext();
                break;

            case R.id.ibMusicPause:
                mPresenter.pauseSong();
                break;
        }
    }
}
