package com.semisky.bluetoothproject.presenter.viewInterface;

import com.semisky.bluetoothproject.entity.MusicSongStatus;

/**
 * Created by chenhongrui on 2018/8/15
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface BtMusicPlayStatusInterface {

    /**
     * 歌曲信息
     */
    void songStatus(MusicSongStatus musicSongStatus);

    /**
     * 播放中
     */
    void startPlaying();

    /**
     * 停止播放
     */
    void stopPlay();

    /**
     * 暂停播放
     */
    void pausePlay();

    /**
     * 播放
     */
    void startPlay();
}
