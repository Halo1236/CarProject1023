package com.semisky.bluetoothproject.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.service.BtLocalService;
import com.semisky.bluetoothproject.utils.Logger;

import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.ACTIVE;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.DIALING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.INCOMING;
import static com.semisky.bluetoothproject.constant.BtConstant.CallStatus.TERMINATED;

/**
 * Created by chenhongrui on 2018/9/25
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CallNotification {

    private static final String TAG = "CallNotification";

    private Context context;
    private NotificationManager notificationManager;
    private BtConstant.CallStatus callStatusNow = BtConstant.CallStatus.NULL;
    private String number;
    private int notifyID = 0x01;

    private PendingIntent hangupIntent, answerIntent;

    public CallNotification(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        hangupIntent = PendingIntent.getService(context, 1, new Intent(context, BtLocalService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        answerIntent = PendingIntent.getService(context, 2, new Intent(context, BtLocalService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCallStatus(BtConstant.CallStatus callStatus, String number) {
        if (number != null) {
            setNumber(number);
        }
        switch (callStatus) {
            case INCOMING:
                callStatusNow = INCOMING;
                setViewOnIncoming();
                Logger.d(TAG, "setCallStatus: 来电");
                break;
            case DIALING:
                callStatusNow = DIALING;
                setViewOnDialing();
                Logger.d(TAG, "setCallStatus: 去电");
                break;
            case ACTIVE:
                callStatusNow = ACTIVE;
                setViewOnActive();
                Logger.d(TAG, "setCallStatus: 接通");
                break;
            case TERMINATED:
                callStatusNow = TERMINATED;
                setViewOnTerminated();
                Logger.d(TAG, "setCallStatus: 挂断");
                break;
        }
    }

    private void setViewOnIncoming() {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remoteview_call_incoming);
        remoteViews.setTextViewText(R.id.tvIncomingName, "name");
        remoteViews.setTextViewText(R.id.tvIncomingNumber, "134123123123");
        remoteViews.setTextViewText(R.id.tvIncomingState, context.getString(R.string.cx62_bt_calling_state_dialing));
        remoteViews.setOnClickPendingIntent(R.id.btnIncomingHangup, hangupIntent);
        remoteViews.setOnClickPendingIntent(R.id.btnAnswer, answerIntent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setCustomContentView(remoteViews); // 设置自定义的RemoteView，需要API最低为24

        notificationManager.notify(notifyID, mBuilder.build());
    }

    private void setViewOnDialing() {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remoteview_call_dialing);
        remoteViews.setTextViewText(R.id.tvDialingName, "name");
        remoteViews.setTextViewText(R.id.tvDialingNumber, "134123123123");
        remoteViews.setTextViewText(R.id.tvDialingState, context.getString(R.string.cx62_bt_calling_state_incoming));
        remoteViews.setOnClickPendingIntent(R.id.btnDialingHangup, hangupIntent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setCustomContentView(remoteViews); // 设置自定义的RemoteView，需要API最低为24

        notificationManager.notify(notifyID, mBuilder.build());
    }

    private void setViewOnActive() {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remoteview_call_active);
        remoteViews.setTextViewText(R.id.tvActiveName, "name");
        remoteViews.setTextViewText(R.id.tvActiveNumber, "134123123123");
        remoteViews.setTextViewText(R.id.tvActiveState, context.getString(R.string.cx62_bt_calling_state_active));

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setCustomContentView(remoteViews); // 设置自定义的RemoteView，需要API最低为24

        notificationManager.notify(notifyID, mBuilder.build());
    }

    private void setViewOnTerminated() {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remoteview_call_terminated);
        remoteViews.setTextViewText(R.id.tvTerminatedName, "name");
        remoteViews.setTextViewText(R.id.tvTerminatedNumber, "134123123123");
        remoteViews.setTextViewText(R.id.tvTerminatedState, context.getString(R.string.cx62_bt_calling_state_over));

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setCustomContentView(remoteViews); // 设置自定义的RemoteView，需要API最低为24

        notificationManager.notify(notifyID, mBuilder.build());
    }
}
