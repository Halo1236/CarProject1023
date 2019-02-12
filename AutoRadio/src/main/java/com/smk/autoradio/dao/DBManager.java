package com.smk.autoradio.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.utils.Logutil;

import java.util.List;

public class DBManager implements IDBManager {

    private static final String TAG = Logutil.makeTagLog(DBManager.class);

    private static DBManager _INSTANCE;
    private Context mCtx;
    private DBHelper mDBHelper;
    private static SQLiteDatabase mWritableDatabase;

    private DBManager(Context ctx) {
        this.mCtx = ctx;
        this.mDBHelper = new DBHelper(mCtx, DBConfiguration.DATABASE_NAME, null, DBConfiguration.DATABASE_VERSION);
        this.mWritableDatabase = mDBHelper.getWritableDatabase();

    }


    public static DBManager getInstance(Context ctx) {
        if (null == _INSTANCE) {
            synchronized (DBManager.class) {
                if (null == _INSTANCE) {
                    _INSTANCE = new DBManager(ctx);
                }
            }
        }
        return _INSTANCE;
    }

    private SQLiteDatabase getDB() {
        if (null == mWritableDatabase) {
            this.mWritableDatabase = mDBHelper.getWritableDatabase();
        }
        return this.mWritableDatabase;
    }


    @Override
    public long addBatchSearchChannelToDB(List<ChannelInfo> channelInfos) {
        synchronized (mWritableDatabase) {
            long result = -1;
            if (null == channelInfos || channelInfos.isEmpty()) {
                Logutil.e(TAG, "channelInfos is Empty !!!");
                return result;
            }
            try {
                getDB().beginTransaction();
                for (ChannelInfo channelInfo : channelInfos) {
                    ContentValues values = new ContentValues();
                    values.put(DBConfiguration.ChannelConfiguration._CAHNNEL, channelInfo.getChannel());
                    values.put(DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE, channelInfo.getChannelType());
                    values.put(DBConfiguration.ChannelConfiguration._SIGNAL, channelInfo.getSignal());
                    values.put(DBConfiguration.ChannelConfiguration._IS_FAVORITE, channelInfo.isFavorite() ? 1 : 0);
                    result = getDB().insert(DBConfiguration.ChannelConfiguration.TABLE_NAME_FULL_SEARCH_CHANNEL, null, values);
                }
                getDB().setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getDB().endTransaction();
            }
            return result;
        }
    }

    @Override
    public long addSingleChannelInfoToFavorite(ChannelInfo channelInfo) {
        synchronized (mWritableDatabase) {
            if (null == channelInfo) {
                Logutil.e(TAG, "channelInfo is Empty !!!");
                return -1;
            }
            ContentValues values = new ContentValues();
            values.put(DBConfiguration.ChannelConfiguration._CAHNNEL, channelInfo.getChannel());
            values.put(DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE, channelInfo.getChannelType());
            values.put(DBConfiguration.ChannelConfiguration._SIGNAL, channelInfo.getSignal());
            values.put(DBConfiguration.ChannelConfiguration._IS_FAVORITE, channelInfo.isFavorite() ? 1 : 0);
            return getDB().insert(DBConfiguration.ChannelConfiguration.TABLE_NAME_FAVORITE_CHANNEL, null, values);
        }
    }

    @Override
    public Cursor queryAllFullSearchChannel(int channelType) {
        synchronized (mWritableDatabase) {
            return getDB().query(DBConfiguration.ChannelConfiguration.TABLE_NAME_FULL_SEARCH_CHANNEL,
                    null,
                    DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE + " = ?",
                    new String[]{channelType + ""},
                    null,
                    null,
                    null);
        }
    }

    @Override
    public Cursor queryAllFavoriteChannel(int channelType) {
        synchronized (mWritableDatabase) {
            return getDB().query(DBConfiguration.ChannelConfiguration.TABLE_NAME_FAVORITE_CHANNEL,
                    null,
                    DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE + " = ? ",
                    new String[]{channelType + ""},
                    null,
                    null,
                    null);
        }
    }

    @Override
    public Cursor querySpecifyFavoriteChannel(int channelType, int channel) {
        synchronized (mWritableDatabase) {
            return getDB().query(DBConfiguration.ChannelConfiguration.TABLE_NAME_FAVORITE_CHANNEL,
                    null,
                    DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE + " = ? AND "
                            + DBConfiguration.ChannelConfiguration._CAHNNEL + " = ?",
                    new String[]{channelType + "", channel + ""},
                    null,
                    null,
                    null);
        }
    }

    @Override
    public int deleteFullSeachChannel(int channelType) {
        synchronized (mWritableDatabase) {
            return getDB().delete(DBConfiguration.ChannelConfiguration.TABLE_NAME_FULL_SEARCH_CHANNEL,
                    DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE + " = ?",
                    new String[]{channelType + ""});
        }
    }

    @Override
    public int deleteAllFullSeachChannel() {
        synchronized (mWritableDatabase) {
            return getDB().delete(DBConfiguration.ChannelConfiguration.TABLE_NAME_FULL_SEARCH_CHANNEL,
                    null,
                    null);
        }
    }

    @Override
    public int deleteFavoriteChannel(int channelType) {
        synchronized (mWritableDatabase) {
            return getDB().delete(DBConfiguration.ChannelConfiguration.TABLE_NAME_FAVORITE_CHANNEL,
                    DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE + " = ?", new String[]{channelType + ""});
        }
    }

    @Override
    public int deleteAllFavoriteChannel() {
        synchronized (mWritableDatabase) {
            return getDB().delete(DBConfiguration.ChannelConfiguration.TABLE_NAME_FAVORITE_CHANNEL,
                    null, null);
        }
    }
}
