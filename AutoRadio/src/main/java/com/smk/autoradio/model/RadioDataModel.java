package com.smk.autoradio.model;


import android.database.Cursor;

import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.application.RadioApplication;
import com.smk.autoradio.dao.DBConfiguration;
import com.smk.autoradio.dao.DBManager;
import com.smk.autoradio.dao.IDBManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RadioDataModel implements IRadioDataModel {
    IDBManager mDBManager;

    public RadioDataModel() {
        this.mDBManager = DBManager.getInstance(RadioApplication.getContext());
    }

    @Override
    public long addBatchSearchChannelToDB(List<ChannelInfo> channelInfos) {
        synchronized (mDBManager) {
            return mDBManager.addBatchSearchChannelToDB(channelInfos);
        }
    }

    @Override
    public long addSingleChannelInfoToFavorite(ChannelInfo channelInfo) {
        synchronized (mDBManager) {
            return mDBManager.addSingleChannelInfoToFavorite(channelInfo);
        }
    }

    @Override
    public List<ChannelInfo> getAllFullSearchChannelList(int channelType) {
        synchronized (mDBManager) {
            Cursor cs = mDBManager.queryAllFullSearchChannel(channelType);
            if (null == cs || cs.getCount() <= 0) {
                return Collections.EMPTY_LIST;
            }

            List<ChannelInfo> channelInfoList = new ArrayList<ChannelInfo>();
            while (cs.moveToNext()) {
                ChannelInfo info = getChannelInfoFromCursor(cs);
                channelInfoList.add(info);
            }
            cs.close();
            return channelInfoList;
        }
    }

    @Override
    public List<ChannelInfo> getAllFavoriteChannelList(int channelType) {
        synchronized (mDBManager) {
            Cursor cs = mDBManager.queryAllFavoriteChannel(channelType);
            if (null == cs || cs.getCount() <= 0) {
                return Collections.EMPTY_LIST;
            }
            List<ChannelInfo> channelInfoList = new ArrayList<ChannelInfo>();
            while (cs.moveToNext()) {
                ChannelInfo channelInfo = getChannelInfoFromCursor(cs);
                channelInfoList.add(channelInfo);
            }
            cs.close();
            return channelInfoList;
        }
    }

    ChannelInfo getChannelInfoFromCursor(Cursor cs) {
        int channel = cs.getInt(cs.getColumnIndex(DBConfiguration.ChannelConfiguration._CAHNNEL));
        int channel_type = cs.getInt(cs.getColumnIndex(DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE));
        int signal = cs.getInt(cs.getColumnIndex(DBConfiguration.ChannelConfiguration._SIGNAL));
        int is_favorite = cs.getInt(cs.getColumnIndex(DBConfiguration.ChannelConfiguration._IS_FAVORITE));

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannel(channel);
        channelInfo.setChannelType(channel_type);
        channelInfo.setSignal(signal);
        channelInfo.setFavorite(is_favorite > 0);
        return channelInfo;
    }

    @Override
    public boolean isFavorite(int channelType, int channel) {
        synchronized (mDBManager) {
            Cursor cs = mDBManager.querySpecifyFavoriteChannel(channelType, channel);
            boolean result = (cs != null && cs.getCount() > 0);
            cs.close();
            return result;
        }
    }

    @Override
    public int deleteFullSeachChannel(int channelType) {
        synchronized (mDBManager) {
            return mDBManager.deleteFullSeachChannel(channelType);
        }
    }

    @Override
    public int deleteAllFullSeachChannel() {
        synchronized (mDBManager) {
            return mDBManager.deleteAllFullSeachChannel();
        }
    }

    @Override
    public int deleteFavoriteChannel(int channelType) {
        synchronized (mDBManager) {
            return mDBManager.deleteFavoriteChannel(channelType);
        }
    }

    @Override
    public int deleteAllFavoriteChannel() {
        synchronized (mDBManager) {
            return mDBManager.deleteAllFavoriteChannel();
        }
    }


}
