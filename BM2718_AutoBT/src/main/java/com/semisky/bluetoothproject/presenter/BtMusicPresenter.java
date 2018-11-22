package com.semisky.bluetoothproject.presenter;

import com.semisky.bluetoothproject.model.BtMusicAudioFocusModel;
import com.semisky.bluetoothproject.model.BtMusicModel;
import com.semisky.bluetoothproject.presenter.viewInterface.BtMusicPlayStatusInterface;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtMusicPresenter extends BasePresenter<BtMusicPlayStatusInterface> {

    private BtMusicModel btMusicModel;
    private BtBaseUiCommandMethod btBaseUiCommandMethod;

    public BtMusicPresenter() {
        this.btMusicModel = BtMusicModel.getInstance();
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
    }

    public void initListener() {
        btMusicModel.setBtMusicPlayStatus(getViewRfr());
    }

    public void playSong() {
        btBaseUiCommandMethod.play();
        BtMusicAudioFocusModel.getINSTANCE().applyAudioFocus();
    }

    public void pauseSong() {
        btBaseUiCommandMethod.pause();
    }

    public void playNext() {
        btBaseUiCommandMethod.next();
    }

    public void playLast() {
        btBaseUiCommandMethod.prev();
    }

    public void reqAvrcp13GetElementAttributesPlaying() {
        btBaseUiCommandMethod.reqAvrcp13GetElementAttributesPlaying();
    }

    public void reqAvrcp13GetPlayStatus() {
        btBaseUiCommandMethod.reqAvrcp13GetPlayStatus();
    }
}
