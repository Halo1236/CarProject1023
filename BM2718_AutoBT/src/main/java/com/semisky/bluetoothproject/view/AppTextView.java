package com.semisky.bluetoothproject.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.semisky.bluetoothproject.utils.StringUtil;

/**
 * Created by chenhongrui on 2018/8/6
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AppTextView extends android.support.v7.widget.AppCompatTextView implements LanguageChangeView {

    private int textId;//文字id
    private int hintId;//hint的id
    private int arrResId, arrResIndex;

    public AppTextView(Context context) {
        super(context);
        init(context, null);
    }

    public AppTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AppTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            String textValue = attrs.getAttributeValue(ANDROIDXML, "text");
            if (!(textValue == null || textValue.length() < 2)) {
                //如果是 android:text="@string/testText"
                //textValue会长这样 @156878785,去掉@号就是资源id
                textId = StringUtil.string2int(textValue.substring(1, textValue.length()));
            }

            String hintValue = attrs.getAttributeValue(ANDROIDXML, "hint");
            if (!(hintValue == null || hintValue.length() < 2)) {
                hintId = StringUtil.string2int(hintValue.substring(1, hintValue.length()));
            }
        }
    }

    @Override
    public void setTextById(int strId) {
        this.textId = strId;
        setText(strId);
    }

    @Override
    public void setTextWithString(String text) {
        this.textId = 0;
        setText(text);
    }

    @Override
    public void setTextByArrayAndIndex(int arrId, int arrIndex) {
        arrResId = arrId;
        arrResIndex = arrIndex;
        String[] strs = getContext().getResources().getStringArray(arrId);
        setText(strs[arrIndex]);
    }

    @Override
    public void reLoadLanguage() {
        try {
            if (textId > 0) {
                setText(textId);
            } else if (arrResId > 0) {
                String[] strs = getContext().getResources().getStringArray(arrResId);
                setText(strs[arrResIndex]);
            }

            if (hintId > 0) {
                setHint(hintId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
