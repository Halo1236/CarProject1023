package com.smk.dialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SmartDialog extends Dialog implements View.OnClickListener {

    public static final int MARGIN_TOP = 1;
    public static final int MARGIN_BOTTOM = 2;
    public static final int MARGIN_LEFT = 3;
    public static final int MARGIN_RIGHT = 4;

    public static final int ALIGN_TOP = 1;
    public static final int ALIGN_BOTTOM = 2;
    public static final int ALIGN_LEFT = 3;
    public static final int ALIGN_RIGHT = 4;
    public static final int ALIGN_CENTER_VERTICAL = 5;
    public static final int ALIGN_CENTER_HORIZONTAL = 6;
    public static final int ALIGN_CENTER_IN_PARENT = 7;

    public static final int TITLE_FIRST = 1;
    public static final int TITLE_SECOND = 2;
    public static final int BUTTON_FIRST = 3;
    public static final int BUTTON_SECOND = 4;

    private View.OnClickListener mFisrtButtonClickListener;
    private View.OnClickListener mSecondButtonClickListener;

    private Handler _handler;

    private RelativeLayout
            layout_root,
            layout_title,
            layout_button;
    private TextView
            tv_title_first,
            tv_title_second;
    private Button
            btn_first,
            btn_second;

    private long mDelayTime = 0;
    private boolean mIsTimeoutDismiss = false;

    public SmartDialog(Context context) {
        super(context, R.style.SmartDialog_Theme);
        _handler = new Handler(Looper.getMainLooper());
        View contentView = View.inflate(context, R.layout.dialog_smart, null);
        setContentView(contentView);
        initViews();
    }

    private void initViews() {
        layout_root = (RelativeLayout) findViewById(R.id.layout_root);
        layout_title = (RelativeLayout) findViewById(R.id.layout_title);
        layout_button = (RelativeLayout) findViewById(R.id.layout_button);

        tv_title_first = (TextView) findViewById(R.id.tv_title_first);
        tv_title_second = (TextView) findViewById(R.id.tv_title_second);
        btn_first = (Button) findViewById(R.id.btn_first);
        btn_second = (Button) findViewById(R.id.btn_second);
    }

    private Runnable mTimeoutRunTaskRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_first:
                if (null != mFisrtButtonClickListener) {
                    mFisrtButtonClickListener.onClick(v);
                }
                super.dismiss();
                break;
            case R.id.btn_second:
                if (null != mSecondButtonClickListener) {
                    mSecondButtonClickListener.onClick(v);
                }
                super.dismiss();
                break;
        }
    }

    @Override
    public void show() {
        if(mIsTimeoutDismiss){
            _handler.removeCallbacksAndMessages(null);
            _handler.postDelayed(mTimeoutRunTaskRunnable,mDelayTime);
        }
        super.show();
    }

    @Override
    public void dismiss() {
        _handler.removeCallbacksAndMessages(null);
        super.dismiss();
    }

    /**
     * 设置弹窗背景全局尺寸
     *
     * @param width
     * @param height
     * @return
     */
    public SmartDialog setRootLayoutSize(int width, int height) {
        RelativeLayout.LayoutParams rootLayoutParams = (RelativeLayout.LayoutParams) layout_root.getLayoutParams();
        rootLayoutParams.width = width;
        rootLayoutParams.height = height;
        layout_root.setLayoutParams(rootLayoutParams);
        return this;
    }

    public SmartDialog setRootLayoutBackground(int resId) {
        layout_root.setBackgroundResource(resId);
        return this;
    }

    public SmartDialog setTitleMargin(int titleId, int marginKey, int marginValue) {
        RelativeLayout.LayoutParams titleLayoutParams = null;
        TextView currentTitle = null;

        if (TITLE_FIRST == titleId) {
            currentTitle = tv_title_first;
        } else if (TITLE_SECOND == titleId) {
            currentTitle = tv_title_second;
        }

        if (null == currentTitle) {
            return this;
        }

        titleLayoutParams = (RelativeLayout.LayoutParams) currentTitle.getLayoutParams();
        switch (marginKey) {
            case MARGIN_TOP:
                titleLayoutParams.topMargin = marginValue;
                break;
            case MARGIN_BOTTOM:
                titleLayoutParams.bottomMargin = marginValue;
                break;
            case MARGIN_LEFT:
                titleLayoutParams.leftMargin = marginValue;
                break;
            case MARGIN_RIGHT:
                titleLayoutParams.rightMargin = marginValue;
                break;
        }
        currentTitle.setLayoutParams(titleLayoutParams);
        return this;
    }

    public SmartDialog setTitleLayoutAlignParent(int alignType) {
        RelativeLayout.LayoutParams layoutTitleParams = (RelativeLayout.LayoutParams) layout_title.getLayoutParams();
        switch (alignType) {
            case ALIGN_TOP:
                layoutTitleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case ALIGN_BOTTOM:
                layoutTitleParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case ALIGN_LEFT:
                layoutTitleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case ALIGN_RIGHT:
                layoutTitleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            case ALIGN_CENTER_VERTICAL:
                layoutTitleParams.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
            case ALIGN_CENTER_HORIZONTAL:
                layoutTitleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
            case ALIGN_CENTER_IN_PARENT:
                layoutTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                break;
        }
        return this;
    }

    public SmartDialog setButtonLayoutMargin(int marginKey, int marginValue) {
        RelativeLayout.LayoutParams layoutButtonLayoutParams = (RelativeLayout.LayoutParams) layout_button.getLayoutParams();

        switch (marginKey) {
            case MARGIN_TOP:
                layoutButtonLayoutParams.topMargin = marginValue;
                break;
            case MARGIN_BOTTOM:
                layoutButtonLayoutParams.bottomMargin = marginValue;
                break;
            case MARGIN_LEFT:
                layoutButtonLayoutParams.leftMargin = marginValue;
                break;
            case MARGIN_RIGHT:
                layoutButtonLayoutParams.rightMargin = marginValue;
                break;
        }
        layout_button.setLayoutParams(layoutButtonLayoutParams);
        return this;
    }

    public SmartDialog setButtonLayoutAlignParent(int marginKey) {
        RelativeLayout.LayoutParams layoutButtonLayoutParams = (RelativeLayout.LayoutParams) layout_button.getLayoutParams();

        switch (marginKey) {
            case ALIGN_TOP:
                layoutButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case ALIGN_BOTTOM:
                layoutButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case ALIGN_LEFT:
                layoutButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case ALIGN_RIGHT:
                layoutButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
        }
        layout_button.setLayoutParams(layoutButtonLayoutParams);
        return this;
    }

    public SmartDialog setButtonMargin(int btnId, int marginKey, int marginValue) {
        Button curBtn = null;
        RelativeLayout.LayoutParams btnLayoutParams = null;
        if (BUTTON_FIRST == btnId) {
            curBtn = btn_first;
        } else if (BUTTON_SECOND == btnId) {
            curBtn = btn_second;
        }

        if (null == curBtn) {
            return this;
        }

        btnLayoutParams = (RelativeLayout.LayoutParams) curBtn.getLayoutParams();
        switch (marginKey) {
            case MARGIN_TOP:
                btnLayoutParams.topMargin = marginValue;
                break;
            case MARGIN_BOTTOM:
                btnLayoutParams.bottomMargin = marginValue;
                break;
            case MARGIN_LEFT:
                btnLayoutParams.leftMargin = marginValue;
                break;
            case MARGIN_RIGHT:
                btnLayoutParams.rightMargin = marginValue;
                break;
        }
        if (null != btnLayoutParams) {
            curBtn.setLayoutParams(btnLayoutParams);
        }
        return this;
    }

    public SmartDialog setButtonAlignParent(int btnId, int marginKey) {
        RelativeLayout.LayoutParams btnLayoutParams = null;
        Button curBtn = null;

        if (BUTTON_FIRST == btnId) {
            curBtn = btn_first;
        } else if (BUTTON_SECOND == btnId) {
            curBtn = btn_second;
        }

        if (null == curBtn) {
            return this;
        }

        btnLayoutParams = (RelativeLayout.LayoutParams) curBtn.getLayoutParams();
        switch (marginKey) {
            case ALIGN_TOP:
                btnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case ALIGN_BOTTOM:
                btnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case ALIGN_LEFT:
                btnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case ALIGN_RIGHT:
                btnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            case ALIGN_CENTER_HORIZONTAL:
                btnLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
        }
        if (null != btnLayoutParams) {
            curBtn.setLayoutParams(btnLayoutParams);
        }
        return this;
    }


    public SmartDialog setTitleText(int titleId, String text) {
        switch (titleId) {
            case TITLE_FIRST:
                tv_title_first.setText(text);
                break;
            case TITLE_SECOND:
                tv_title_second.setText(text);
                break;
        }
        return this;
    }

    public SmartDialog setTitleTextSize(int titleId, int textSize) {
        switch (titleId) {
            case TITLE_FIRST:
                tv_title_first.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                break;
            case TITLE_SECOND:
                tv_title_second.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                break;
        }
        return this;
    }

    public SmartDialog setButtonText(int titleId, String text) {
        switch (titleId) {
            case BUTTON_FIRST:
                btn_first.setText(text);
                break;
            case BUTTON_SECOND:
                btn_second.setText(text);
                break;
        }
        return this;
    }

    public SmartDialog setButtonTextSize(int titleId, int textSize) {
        switch (titleId) {
            case BUTTON_FIRST:
                btn_first.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                break;
            case BUTTON_SECOND:
                btn_second.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                break;
        }
        return this;
    }

    public SmartDialog setButtonCallback(int btnId, View.OnClickListener l) {
        switch (btnId) {
            case BUTTON_FIRST:
                btn_first.setOnClickListener(this);
                this.mFisrtButtonClickListener = l;
                break;
            case BUTTON_SECOND:
                btn_second.setOnClickListener(this);
                this.mSecondButtonClickListener = l;
                break;
        }
        return this;
    }



    public SmartDialog setButtonBackground(int buttonId, int resId) {
        switch (buttonId) {
            case BUTTON_FIRST:
                btn_first.setBackgroundResource(resId);
                break;
            case BUTTON_SECOND:
                btn_second.setBackgroundResource(resId);
                break;
        }
        return this;
    }

    public SmartDialog setTitleVisibility(int titleId, int visibility) {
        switch (titleId) {
            case TITLE_FIRST:
                tv_title_first.setVisibility(visibility);
                break;
            case TITLE_SECOND:
                tv_title_second.setVisibility(visibility);
                break;
        }
        return this;
    }

    public SmartDialog setButtonVisibility(int btnId, int visibility) {
        switch (btnId) {
            case BUTTON_FIRST:
                btn_first.setVisibility(visibility);
                break;
            case BUTTON_SECOND:
                btn_second.setVisibility(visibility);
                break;
        }
        return this;
    }

    public SmartDialog setButtonLayoutVisibility(int visibility) {
        layout_button.setVisibility(visibility);
        return this;
    }

    public SmartDialog setTimeout(long delayTime){
        this.mDelayTime = delayTime;
        return this;
    }

    public SmartDialog enableTimeoutDismiss(boolean enable){
        this.mIsTimeoutDismiss = enable;
        return this;
    }


}
