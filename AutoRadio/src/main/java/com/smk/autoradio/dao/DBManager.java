package com.smk.autoradio.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.smk.autoradio.utils.Logutil;

public class DBManager {

    private static final String TAG = Logutil.makeTagLog(DBManager.class);

    private static DBManager _INSTANCE;
    private Context mCtx;
    private DBHelper mDBHelper;
    private static SQLiteDatabase mWritableDatabase;

    private DBManager(Context ctx) {
        this.mCtx = ctx;
        this.mDBHelper = new DBHelper(mCtx,DBConfiguration.DATABASE_NAME,null,DBConfiguration.DATABASE_VERSION);

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

    private synchronized SQLiteDatabase getDB() {
        if(null == mWritableDatabase){
            this.mWritableDatabase = mDBHelper.getWritableDatabase();
        }
        return this.mWritableDatabase;
    }






}
