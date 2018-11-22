package com.semisky.bluetoothproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;

/**
 * Created by chenhongrui on 2018/8/14
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class KeyboardGradViewAdapter extends BaseAdapter {

    private static final String TAG = "KeyBoardAdapter";

    private Context mContext;
    private String[] valueList;

    public KeyboardGradViewAdapter(Context mContext, String[] valueList) {
        Log.d(TAG, "valueList: " + valueList.length);
        this.mContext = mContext;
        this.valueList = valueList;
    }

    @Override
    public int getCount() {
        return valueList.length;
    }

    @Override
    public Object getItem(int position) {
        return valueList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_keyboard_view, null);
            viewHolder = new ViewHolder();
            viewHolder.tvKeyboardItem = convertView.findViewById(R.id.tvKeyboardItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvKeyboardItem.setText(valueList[position]);

        return convertView;
    }

    public class ViewHolder {
        TextView tvKeyboardItem;
    }

}
