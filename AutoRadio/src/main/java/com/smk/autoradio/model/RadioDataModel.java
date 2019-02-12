package com.smk.autoradio.model;


import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.application.RadioApplication;
import com.smk.autoradio.dao.DBManager;
import com.smk.autoradio.dao.IDBManager;

import java.util.List;

public class RadioDataModel implements IRadioDataModel {
    IDBManager mDBManager;

    public RadioDataModel() {
        this.mDBManager = DBManager.getInstance(RadioApplication.getContext());
    }

    @Override
    public long addBatchSearchChannelToDB(List<ChannelInfo> channelInfos) {
        return mDBManager.addBatchSearchChannelToDB(channelInfos);
    }

    @Override
    public long addSingleChannelInfoToFavorite(ChannelInfo channelInfo) {
        return mDBManager.addSingleChannelInfoToFavorite(channelInfo);
    }
}
