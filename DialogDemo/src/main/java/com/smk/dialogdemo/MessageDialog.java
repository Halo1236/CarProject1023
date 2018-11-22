package com.smk.dialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MessageDialog extends Dialog implements View.OnClickListener, DialogInterface.OnCancelListener {
    private View.OnClickListener mPositiveButtonClickListener;
    private View.OnClickListener mNegativeButtonClickListener;
    private String mTitle;
    private String mMessage;
    private boolean mIsClickNegativeButton = false;

    private View
            layout_row1,
            layout_row2;
    private TextView
            tv_title,
            tv_message;
    private Button
            btn_positive,
            btn_negative;


    public MessageDialog(Context context) {
        super(context, R.style.MessageDialog_DialogTheme);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_message, null);
        setContentView(contentView);
        initViews();
    }

    void initViews() {
        layout_row1 = findViewById(R.id.layout_row1);
        layout_row2 = findViewById(R.id.layout_row2);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_message = (TextView) findViewById(R.id.tv_message);
        btn_positive = (Button) findViewById(R.id.btn_positive);
        btn_negative = (Button) findViewById(R.id.btn_negative);

        layout_row2.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);
        tv_message.setVisibility(View.INVISIBLE);
        btn_positive.setVisibility(View.GONE);
        btn_negative.setVisibility(View.GONE);
    }

    public MessageDialog setTitle(String title) {
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        return this;
    }

    public MessageDialog setMessage(String message) {
        tv_message.setVisibility(View.VISIBLE);
        tv_message.setText(message);
        return this;
    }

    public MessageDialog setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
        this.mPositiveButtonClickListener = listener;
        btn_positive.setOnClickListener(this);
        layout_row2.setVisibility(View.VISIBLE);
        btn_positive.setVisibility(View.VISIBLE);
        tv_message.setVisibility(View.VISIBLE);
        btn_positive.setText(positiveButtonText);
        return this;
    }

    public MessageDialog setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
        this.mNegativeButtonClickListener = listener;
        btn_negative.setOnClickListener(this);
        layout_row2.setVisibility(View.VISIBLE);
        btn_negative.setVisibility(View.VISIBLE);
        btn_negative.setText(negativeButtonText);
        setOnCancelListener(this);
        return this;
    }

    /**
     * 只有不显示message时，设置message是不占用位置
     *
     * @param enable
     * @return
     */
    public MessageDialog enableMessageTextSpace(boolean enable) {
        if (enable) {
            tv_message.setVisibility(View.INVISIBLE);
        } else {
            tv_message.setVisibility(View.GONE);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_positive:
                if (null != mPositiveButtonClickListener) {
                    mPositiveButtonClickListener.onClick(v);
                }
                break;
            case R.id.btn_negative:
                if (null != mNegativeButtonClickListener) {
                    mNegativeButtonClickListener.onClick(v);
                    mIsClickNegativeButton = true;
                    this.cancel();
                }
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (!mIsClickNegativeButton && null != mNegativeButtonClickListener) {
            mNegativeButtonClickListener.onClick(btn_negative);
        }
        mIsClickNegativeButton = false;
    }


}
