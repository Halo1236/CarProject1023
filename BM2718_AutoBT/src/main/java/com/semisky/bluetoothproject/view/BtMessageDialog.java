package com.semisky.bluetoothproject.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;

import java.lang.ref.WeakReference;

/**
 * Created by chenhongrui on 2018/8/15
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtMessageDialog extends Dialog {

    private Context mContext;

    private CallHandler callHandler;

    public BtMessageDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cancel_confirm_button_dialog, null);
        setContentView(view);
        callHandler = new CallHandler(this);
    }

    private TextView tvMessage;

    public void setMessage(String message) {
        if (tvMessage != null) {
            tvMessage.setText(message);
        }
    }

    private void dismissDialog() {
        if (isShowing()) {
            dismiss();
        }
    }

    public void onAdapterDiscoveryStarted(ClickCancelListener clickCancelListener) {
        dismissDialog();
        show();
    }

    public void stateConnected() {
        dismissDialog();
        initMessageView(mContext.getString(R.string.cx62_bt_dialog_connect_success));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    callHandler.sendEmptyMessage(dismissDialog);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        show();
    }

    /**
     * 当点击了确认
     */
    public interface ClickConfirmListener {
        void clickConfirm();
    }

    /**
     * 当点击了取消
     */
    public interface ClickCancelListener {
        void clickCancel();
    }

    public interface DialogDisMissListener {
        void dialogDisMissCallback(DialogInterface dialog);
    }

    public void initOnlyMessageDialog(String message, final DialogDisMissListener dialogDisMissListener) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.only_message_dialog, null);
        setContentView(view);

        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogDisMissListener.dialogDisMissCallback(dialog);
            }
        });

    }

    public void initCancelConfirmButtonView(String message, @NonNull final ClickConfirmListener confirmListener) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cancel_confirm_button_dialog, null);
        setContentView(view);

        Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_dialog_confirm);

        tvMessage = view.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmListener.clickConfirm();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void initCancelConfirmButtonView(String message, @NonNull final ClickConfirmListener confirmListener,
                                            final ClickCancelListener clickCancelListener, final DialogDisMissListener dialogDisMissListener) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cancel_confirm_button_dialog, null);
        setContentView(view);

        Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_dialog_confirm);

        tvMessage = view.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmListener.clickConfirm();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCancelListener.clickCancel();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogDisMissListener.dialogDisMissCallback(dialog);
            }
        });

    }

    public void initCancelButtonView(String message, @NonNull final ClickCancelListener cancelListener
            , @NonNull final DialogDisMissListener dialogDisMissListener) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cancel_button_dialog, null);
        setContentView(view);

        Button btnCancel = view.findViewById(R.id.btn_cancel);

        tvMessage = view.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelListener.clickCancel();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogDisMissListener.dialogDisMissCallback(dialog);
            }
        });
    }

    private String message;

    public void initMessageView(String message) {
        this.message = message;
        callHandler.sendEmptyMessage(initMessageView);
    }

    private static final int initMessageView = 0x01;
    private static final int dismissDialog = 0x02;

    private static class CallHandler extends Handler {
        WeakReference<BtMessageDialog> mReference;

        CallHandler(BtMessageDialog dialog) {
            this.mReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            BtMessageDialog dialogView = mReference.get();

            switch (msg.what) {
                case initMessageView:
                    View view = LayoutInflater.from(dialogView.mContext).inflate(R.layout.only_message_dialog, null);
                    dialogView.setContentView(view);

                    dialogView.tvMessage = view.findViewById(R.id.tv_dialog_message);
                    dialogView.tvMessage.setText(dialogView.message);
                    break;
                case dismissDialog:
                    if (dialogView.isShowing()) {
                        dialogView.dismiss();
                    }
                    break;
            }
        }
    }
}
