package com.smk.bt.views.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smk.bt.R;
import com.smk.bt.utils.Logger;

import java.util.ArrayList;
import java.util.List;


public class BTDialpadAdapter extends BaseAdapter {

    private static final String TAG = Logger.makeLogTag(BTDialpadAdapter.class);

    private Context mContext;
    private List<String> mList;

    public BTDialpadAdapter(Context mContext) {
        this.mContext = mContext;
        this.mList = new ArrayList<String>();
    }

    public void updateData(List<String> list) {
        if(null !=  list){
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_keyboard_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvKeyboardItem.setText(mList.get(position));

        return convertView;
    }

    public class ViewHolder {
        private TextView tvKeyboardItem;

        public ViewHolder(View v) {
            tvKeyboardItem = v.findViewById(R.id.tvKeyboardItem);
        }
    }

}
