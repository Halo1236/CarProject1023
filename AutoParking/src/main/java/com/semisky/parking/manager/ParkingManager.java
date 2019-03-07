package com.semisky.parking.manager;

import android.content.Context;
import android.content.Intent;

import com.semisky.parking.utils.Logger;

/**
 * 360全景管理类
 * Created by Administrator on 2018/8/11.
 */

public class ParkingManager {
    /**
     * 倒车应用常量
     */
    public class Definition {
        public static final String ACTION_SERVICE_PARKING = "com.semisky.service.ACTION_PARKING_CONTROL";// 倒车服务意图
        public static final String CMD_PARAM = "cmd";// 意图参数的键
        public static final int CMD_INVALID = -1;// 无效意图命令
        public static final int CMD_AVM_OFF = 10;// 用户手动关闭AVM意图
        public static final int CMD_AVM_ON = 11;// 非R档进入AVM
        public static final int CMD_DVR_OFF = 20;// DVR关闭意图
        public static final int CMD_DVR_ON = 21;// DVR开启意图

    }

    private static ParkingManager _INSTANCE;
    private Context mContext;

    private ParkingManager(Context ctx) {
        this.mContext = ctx;
    }


    public static ParkingManager getInstance(Context ctx) {
        if (null == _INSTANCE) {
            _INSTANCE = new ParkingManager(ctx);
        }
        return _INSTANCE;
    }

    public void sendParkingCMD(int cmd) {
        Intent intent = new Intent();
        intent.setAction(Definition.ACTION_SERVICE_PARKING);
        intent.putExtra(Definition.CMD_PARAM, cmd);
        mContext.startService(intent);
    }


}
