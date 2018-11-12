package com.semisky.btcarkit.view.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semisky.btcarkit.R;
import com.semisky.btcarkit.prenster.BTMusicPlayerPresenter;
import com.semisky.btcarkit.utils.Logutil;

public class BTMusicPlayerFragment extends BaseFragment<IBTMusicPlayerView, BTMusicPlayerPresenter<IBTMusicPlayerView>> implements IBTMusicPlayerView, View.OnClickListener {
    private TextView
            tv_title,
            tv_artist,
            tv_album,
            tv_ad2dp_state,
            tv_avrcp_state;
    private Button
            btn_prev,
            btn_switch,
            btn_next;


    @Override
    protected String getTagLog() {
        return Logutil.makeTagLog(BTMusicPlayerFragment.class);
    }

    @Override
    protected BTMusicPlayerPresenter<IBTMusicPlayerView> createPresenter() {
        return new BTMusicPlayerPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_musicplayer;
    }

    @Override
    protected void initView() {
        this.tv_title = (TextView) mContentView.findViewById(R.id.tv_title);
        this.tv_artist = (TextView) mContentView.findViewById(R.id.tv_artist);
        this.tv_album = (TextView) mContentView.findViewById(R.id.tv_album);
        this.tv_ad2dp_state = (TextView) mContentView.findViewById(R.id.tv_ad2dp_state);
        this.tv_avrcp_state = (TextView) mContentView.findViewById(R.id.tv_avrcp_state);

        this.btn_prev = (Button) mContentView.findViewById(R.id.btn_prev);
        this.btn_switch = (Button) mContentView.findViewById(R.id.btn_switch);
        this.btn_next = (Button) mContentView.findViewById(R.id.btn_next);
    }

    @Override
    protected void setListener() {
        this.btn_prev.setOnClickListener(this);
        this.btn_switch.setOnClickListener(this);
        this.btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                Logutil.i(TAG, "PREV ...");
                if(isBindPresenter()){
                    mPresenter.onPrevProgram();
                }
                break;
            case R.id.btn_switch:
                Logutil.i(TAG, "SWITCH ...");
                if(isBindPresenter()){
                    mPresenter.onPlayOrPause();
                }
                break;
            case R.id.btn_next:
                Logutil.i(TAG, "NEXT ...");
                if(isBindPresenter()){
                    mPresenter.onNextProgram();
                }
                break;
        }
    }

    @Override
    public void onSongNameChanged(String title) {
        if (null == title) {
            title = getString(R.string.bt_music_title_def_text);
        }
        tv_title.setText(title);
    }

    @Override
    public void onSongArtistChanged(String artist) {
        if (null == artist) {
            artist = getString(R.string.bt_music_artist_def_text);
        }
        tv_artist.setText(artist);
    }

    @Override
    public void onSongAlbumChanged(String album) {
        if (null == album) {
            album = getString(R.string.bt_music_album_def_text);
        }
        tv_album.setText(album);
    }

    @Override
    public void onPlayStateChange(boolean isPlaying) {
        int resId = isPlaying ? R.string.bt_music_play_program_text : R.string.bt_music_pause_program_text;
        btn_switch.setText(resId);
    }

    @Override
    public void onA2dpStateChanged( int newState) {
        String text = getString(R.string.bt_a2dp_state_text);
        tv_ad2dp_state.setText(text + " [" + newState +"]");
    }

    @Override
    public void onAvrcpMediaPlayStateChanged(int state) {
        String text = getString(R.string.bt_avrcp_state_text);
        tv_avrcp_state.setText(text + " [" + state +"]");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Logutil.i(TAG, "onHiddenChanged() hidden : " + hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logutil.i(TAG, "onResume()");
        if(isBindPresenter()){
            mPresenter.onRefreshBtMusicInfos();
        }
    }
}
