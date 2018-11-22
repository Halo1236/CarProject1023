package com.semisky.bluetoothproject.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

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
public class DialogTips extends Dialog {

    private TextView tv_dialog_message;
    public DialogTips(Context context) {
        super(context, R.style.MessageDialog);
        initView(context);
    }
    public void setTextId(int textId){
        if (tv_dialog_message != null){
            tv_dialog_message.setText(textId);
        }
    }
    private void initView(Context context) {
        LayoutInflater lf = LayoutInflater.from(context);
        View view = lf.inflate(R.layout.tips_dialog, null);
        tv_dialog_message = view.findViewById(R.id.tv_dialog_message);

        setContentView(view);
        setCancelable(false);
    }

    Handler _handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
        }

    };

    @Override
    public void show() {
        super.show();
        _handler.sendMessageDelayed(_handler.obtainMessage(1),3000);
    }

}
