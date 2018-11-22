package com.smk.dialogdemo;

import android.app.Dialog;
import android.content.Context;
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
    public static final int CENTER_HORIZONTAL = 5;

    public static final int ALIGN_TOP = 1;
    public static final int ALIGN_BOTTOM = 2;
    public static final int ALIGN_LEFT = 3;
    public static final int ALIGN_RIGHT = 4;

    public static final int TITLE_FIRST = 1;
    public static final int TITLE_SECOND = 2;
    public static final int BUTTON_FIRST = 3;
    public static final int BUTTON_SECOND = 4;

    private View.OnClickListener mFisrtButtonClickListener;
    private View.OnClickListener mSecondButtonClickListener;

    private int mFirstButtonMarginLeft = 0;
    private int mSecondButtonMarginLeft = 0;

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

    public SmartDialog(Context context) {
        super(context, R.style.SmartDialog_Theme);
        View contentView = View.inflate(context, R.layout.dialog_smart, null);
        setContentView(contentView);
        initViews();
        initSetDefaultLayoutParams();
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

    void initSetDefaultLayoutParams() {

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

    public SmartDialog setFirstTitleMargin(int marginKey, int marginValue) {
        RelativeLayout.LayoutParams firstTitleLayoutParams = (RelativeLayout.LayoutParams) tv_title_first.getLayoutParams();
        switch (marginKey) {
            case MARGIN_TOP:
                firstTitleLayoutParams.topMargin = marginValue;
                break;
            case MARGIN_BOTTOM:
                firstTitleLayoutParams.bottomMargin = marginValue;
                break;
            case MARGIN_LEFT:
                firstTitleLayoutParams.leftMargin = marginValue;
                break;
            case MARGIN_RIGHT:
                firstTitleLayoutParams.rightMargin = marginValue;
                break;
        }
        tv_title_first.setLayoutParams(firstTitleLayoutParams);
        return this;
    }

    public SmartDialog setSecondTitleMargin(int marginKey, int marginValue) {
        RelativeLayout.LayoutParams layoutTitleLayoutParams = (RelativeLayout.LayoutParams) tv_title_second.getLayoutParams();
        switch (marginKey) {
            case MARGIN_TOP:
                layoutTitleLayoutParams.topMargin = marginValue;
                break;
            case MARGIN_BOTTOM:
                layoutTitleLayoutParams.bottomMargin = marginValue;
                break;
            case MARGIN_LEFT:
                layoutTitleLayoutParams.leftMargin = marginValue;
                break;
            case MARGIN_RIGHT:
                layoutTitleLayoutParams.rightMargin = marginValue;
                break;
        }
        tv_title_second.setLayoutParams(layoutTitleLayoutParams);
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

    public SmartDialog setButtonLayoutAlign(int marginKey, int marginValue) {
        RelativeLayout.LayoutParams layoutButtonLayoutParams = (RelativeLayout.LayoutParams) layout_button.getLayoutParams();

        switch (marginKey) {
            case ALIGN_TOP:
                layoutButtonLayoutParams.topMargin = marginValue;
                layoutButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case ALIGN_BOTTOM:
                layoutButtonLayoutParams.bottomMargin = marginValue;
                layoutButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case ALIGN_LEFT:
                layoutButtonLayoutParams.leftMargin = marginValue;
                layoutButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case ALIGN_RIGHT:
                layoutButtonLayoutParams.rightMargin = marginValue;
                layoutButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
        }
        layout_button.setLayoutParams(layoutButtonLayoutParams);
        return this;
    }

    public SmartDialog setFirstButtonMargin(int marginKey, int marginValue) {
        RelativeLayout.LayoutParams firstButtonLayoutParams = (RelativeLayout.LayoutParams) btn_first.getLayoutParams();
        switch (marginKey) {
            case MARGIN_TOP:
                firstButtonLayoutParams.topMargin = marginValue;
                break;
            case MARGIN_BOTTOM:
                firstButtonLayoutParams.bottomMargin = marginValue;
                break;
            case MARGIN_LEFT:
                firstButtonLayoutParams.leftMargin = marginValue;
                break;
            case MARGIN_RIGHT:
                firstButtonLayoutParams.rightMargin = marginValue;
                break;
            case CENTER_HORIZONTAL:
                firstButtonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
        }
        btn_first.setLayoutParams(firstButtonLayoutParams);
        return this;
    }

    public SmartDialog setSecondButtonMargin(int marginKey, int marginValue) {
        RelativeLayout.LayoutParams secondButtonLayoutParams = (RelativeLayout.LayoutParams) btn_second.getLayoutParams();
        switch (marginKey) {
            case MARGIN_TOP:
                secondButtonLayoutParams.topMargin = marginValue;
                break;
            case MARGIN_BOTTOM:
                secondButtonLayoutParams.bottomMargin = marginValue;
                break;
            case MARGIN_LEFT:
                secondButtonLayoutParams.leftMargin = marginValue;
                break;
            case MARGIN_RIGHT:
                secondButtonLayoutParams.rightMargin = marginValue;
                break;
            case CENTER_HORIZONTAL:
                secondButtonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
        }
        btn_second.setLayoutParams(secondButtonLayoutParams);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_first:
                if (null != mFisrtButtonClickListener) {
                    mFisrtButtonClickListener.onClick(v);
                    super.dismiss();
                }
                break;
            case R.id.btn_second:
                if (null != mSecondButtonClickListener) {
                    mSecondButtonClickListener.onClick(v);
                    super.dismiss();
                }
                break;
        }
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


}
