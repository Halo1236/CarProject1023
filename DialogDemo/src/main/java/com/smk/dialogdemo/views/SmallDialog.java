package com.smk.dialogdemo.views;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.smk.dialogdemo.R;

import java.lang.reflect.Method;

public class SmallDialog extends Dialog{
    private Button btn_cancel;
    private RelativeLayout rootLayout;


    public SmallDialog(Context context) {
        super(context,R.style.SmallDialog_Theme);
        View mContentView = View.inflate(context, R.layout.dialog_small,null);
        setContentView(mContentView);
        initViews();
        initBaseConfig();

        getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    private void initBaseConfig(){
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setCanceledOnTouchOutside(false);
    }

    private void initViews() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        rootLayout = (RelativeLayout)findViewById(R.id.root_layout);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rootLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }




}
