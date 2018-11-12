package com.semisky.btcarkit.view.fragment;

public interface IBTMusicPlayerView {
    /**
     * AVRCP音乐播放状态改变
     *
     * @param state
     */
    void onAvrcpMediaPlayStateChanged(int state);

    /**
     * A2DP状态变化
     *
     * @param newState
     */
    void onA2dpStateChanged(int newState);

    /**
     * 歌曲名改变
     *
     * @param title
     */
    void onSongNameChanged(String title);

    /**
     * 歌手名改变
     *
     * @param artist
     */
    void onSongArtistChanged(String artist);

    /**
     * 专辑名称改变
     *
     * @param album
     */
    void onSongAlbumChanged(String album);

    /**
     * 播放状态改变
     *
     * @param isPlaying 播放状态 true:play ,false: pause or stop
     */
    void onPlayStateChange(boolean isPlaying);


}
