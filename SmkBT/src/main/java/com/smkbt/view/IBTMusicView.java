package com.smkbt.view;

public interface IBTMusicView {

    void onChangeTitle(String title);
    void onChangeArtist(String artist);
    void onChangeAlbum(String album);
    void onChangeProgress(int progress);
    void onChangeCurrentTime(String curTime);
    void onChangeTotalTime(String totalTime);
}
