package com.smk.bt.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smk.bt.R;
import com.smk.bt.bean.BTDeviceInfo;

import java.util.ArrayList;
import java.util.List;

public class BTDeviceListAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private List<BTDeviceInfo> mBTDeviceInfoList;
    private OnDeviceListItemListener mOnDeviceListItemListener;

    public BTDeviceListAdapter(Context ctx) {
        this.mContext = ctx;
        this.mBTDeviceInfoList = new ArrayList<BTDeviceInfo>();
    }

    public void updateData(List<BTDeviceInfo> btDeviceInfos) {
        if (null != btDeviceInfos) {
            mBTDeviceInfoList.clear();
            mBTDeviceInfoList.addAll(btDeviceInfos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mBTDeviceInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBTDeviceInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_device_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

            holder.iv_device_delete.setTag(position);
            holder.iv_device_pair.setTag(position);
            holder.iv_device_delete.setOnClickListener(this);
            holder.iv_device_pair.setOnClickListener(this);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_device_name.setText(mBTDeviceInfoList.get(position).getName());
        holder.iv_device_delete.setVisibility(mBTDeviceInfoList.get(position).isFavorite() ? View.VISIBLE : View.INVISIBLE);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (null == mOnDeviceListItemListener) {
            return;
        }
        int pos = (Integer) v.getTag();
        if (pos > 0 && pos < mBTDeviceInfoList.size()) {
            switch (v.getId()) {
                case R.id.iv_device_delete:
                    mOnDeviceListItemListener.onBtDeviceDelete(mBTDeviceInfoList.get(pos).getAddress());
                    break;
                case R.id.iv_device_pair:
                    mOnDeviceListItemListener.onBtDevicePair(mBTDeviceInfoList.get(pos).getAddress());
                    break;
            }
        }
    }

    class ViewHolder {
        private TextView tv_device_name;
        private ImageView
                iv_device_pair,
                iv_device_delete;

        public ViewHolder(View v) {
            tv_device_name = (TextView) v.findViewById(R.id.tv_device_name);
            iv_device_pair = (ImageView) v.findViewById(R.id.iv_device_pair);
            iv_device_delete = (ImageView) v.findViewById(R.id.iv_device_delete);
        }

    }

    public interface OnDeviceListItemListener {
        void onBtDevicePair(String address);

        void onBtDeviceDelete(String address);
    }

    public void registerCallback(OnDeviceListItemListener listener) {
        this.mOnDeviceListItemListener = listener;
    }

}
