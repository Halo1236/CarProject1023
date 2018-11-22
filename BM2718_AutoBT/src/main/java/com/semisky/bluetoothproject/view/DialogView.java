package com.semisky.bluetoothproject.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;

/**
 * Created by chenhongrui on 2018/9/17
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class DialogView extends Dialog {

    private View view;

    public DialogView(@NonNull Context context) {
        super(context, R.style.MessageDialog);
        view = View.inflate(context, R.layout.title_message_dialog, null);
        setContentView(view);
    }

    /**
     * 带有消息和标题，确认和取消按钮的dialog
     *
     * @param title   标题
     * @param message 消息
     * @param confirm 确认
     */
    public void createTitleDialog(String title, String message,
                                  View.OnClickListener confirm) {

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);

        Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_dialog_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(confirm);

        tvTitle.setText(title);
        tvMessage.setText(message);
    }

}
