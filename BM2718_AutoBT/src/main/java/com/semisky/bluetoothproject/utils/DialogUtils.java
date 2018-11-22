package com.semisky.bluetoothproject.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;

/**
 * Created by chenhongrui on 2018/7/31
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class DialogUtils {

    private static final String TAG = "DialogUtils";

    public DialogUtils() {
    }

    public void initDialog(Context context) {
    }

    /**
     * 显示信息带有取消按钮的dialog
     *
     * @param context        上下文
     * @param message
     * @param cancelListener 按键回调
     */
    public static AlertDialog createDialog(Context context, String message, View.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.cancel_button_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();

        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(cancelListener);

        return dialog;
    }

    /**
     * 显示信息带有确认和取消按钮的dialog
     *
     * @param context 上下文
     * @param message 消息
     * @param confirm 确认
     */
    public static AlertDialog createDialogConfirm(Context context, String message,
                                                  View.OnClickListener confirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.cancel_confirm_button_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();

        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_dialog_confirm);

        btnConfirm.setOnClickListener(confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvMessage.setText(message);

        return dialog;
    }

    /**
     * 只显示信息的dialog
     *
     * @param context 上下文
     * @param message 信息
     */
    public static AlertDialog createDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.only_message_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);

        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);

        return builder.create();
    }

    /**
     * 带有消息和标题，确认和取消按钮的dialog
     *
     * @param context 上下文
     * @param title   标题
     * @param message 消息
     * @param confirm 确认
     */
    public static AlertDialog createTitleDialog(Context context, String title, String message,
                                                View.OnClickListener confirm) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.title_message_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog = builder.create();

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);

        Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_dialog_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(confirm);

        tvTitle.setText(title);
        tvMessage.setText(message);

        return alertDialog;
    }

    /**
     * 带有两行消息和确认取消按钮dialog
     *
     * @param context  上下文
     * @param message  第一行消息
     * @param message2 第二行消息
     * @param cancel   取消
     * @param confirm  确认
     */
    public static AlertDialog createDialog(Context context, String message, String message2,
                                           View.OnClickListener confirm, View.OnClickListener cancel) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.second_message_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);

        TextView tvMessage1 = view.findViewById(R.id.tv_dialog_message1);
        TextView tvMessage2 = view.findViewById(R.id.tv_dialog_message2);

        Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_dialog_confirm);

        btnCancel.setOnClickListener(cancel);
        btnConfirm.setOnClickListener(confirm);

        tvMessage1.setText(message);
        tvMessage2.setText(message2);

        return builder.create();
    }
}
