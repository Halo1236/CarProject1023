package com.semisky.bluetoothproject.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Created by chenhongrui on 2018/8/15
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtContactDownloadDialog extends Dialog {

    private static final String TAG = "BtContactDownloadDialog";

    private Context mContext;
    private BtBaseUiCommandMethod btBaseUiCommandMethod;
    private Button btnCancel;
    private TextView tvMessage;
    private ContactHandler contactHandler;

    public BtContactDownloadDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        this.mContext = context;
        contactHandler = new ContactHandler(this);
        View view = LayoutInflater.from(mContext).inflate(R.layout.cancel_button_dialog, null);
        setContentView(view);
        btnCancel = view.findViewById(R.id.btn_cancel);
        tvMessage = view.findViewById(R.id.tv_dialog_message);
        initShareView();
    }

    public void setBtBaseUiCommandMethod(BtBaseUiCommandMethod btBaseUiCommandMethod) {
        this.btBaseUiCommandMethod = btBaseUiCommandMethod;
    }

    private static final int SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED = 0x00002000;

    private void initShareView() {
        LinearLayout linearLayout = findViewById(R.id.ll_dialog_Layout);
        linearLayout.setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED);

        WindowManager.LayoutParams param = Objects.requireNonNull(getWindow()).getAttributes();
        getWindow().setAttributes(param);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND
                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setType(WindowManager.LayoutParams.TYPE_PRIORITY_PHONE);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void dismissContactDialog() {
        contactHandler.sendEmptyMessage(DISMISS_DIALOG);
    }

    public void showDialog() {
        contactHandler.sendEmptyMessage(SHOW_DIALOG);
    }

    public void initCancelButtonView(String message) {
        tvMessage.setText(message);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btBaseUiCommandMethod.reqPbapDownloadInterrupt();
                dismiss();
            }
        });
    }

    private static final int SHOW_DIALOG = 0x01;
    private static final int DISMISS_DIALOG = 0x02;

    private static class ContactHandler extends Handler {
        WeakReference<BtContactDownloadDialog> mReference;

        ContactHandler(BtContactDownloadDialog dialog) {
            this.mReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            BtContactDownloadDialog dialogView = mReference.get();

            switch (msg.what) {
                case SHOW_DIALOG:
                    dialogView.show();
                    break;
                case DISMISS_DIALOG:
                    if (dialogView.isShowing()) {
                        dialogView.dismiss();
                    }
                    break;
            }
        }
    }
}
