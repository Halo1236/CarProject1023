package com.smk.dialogdemo.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.smk.dialogdemo.R;

public class BTCallFullScreenFloatLayout extends FrameLayout {

    public BTCallFullScreenFloatLayout(Context context) {
        super(context);
        View.inflate(context, R.layout.float_window_bt_call_fullscreen,this);
    }

}
