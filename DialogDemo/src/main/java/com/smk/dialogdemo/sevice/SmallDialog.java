package com.smk.dialogdemo.sevice;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.smk.dialogdemo.R;

public class SmallDialog extends Dialog{
    private Button btn_cancel;


    public SmallDialog(Context context) {
        super(context,R.style.SmallDialog_Theme);
        View mContentView = View.inflate(context, R.layout.dialog_small,null);
        initBaseConfig();
        setContentView(mContentView);
        initViews();
    }

    private void initBaseConfig(){
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.TOP;
        getWindow().setAttributes(params);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initViews() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
