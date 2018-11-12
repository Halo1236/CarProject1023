package com.semisky.btcarkit.view.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.semisky.btcarkit.R;
import com.semisky.btcarkit.aidl.BTDeviceInfo;

import java.util.ArrayList;
import java.util.List;

public class BTDeviceListAdapter extends BaseAdapter {
    private List<BTDeviceInfo> mList;

    private Context mContext;

    public BTDeviceListAdapter(Context ctx) {
        this.mContext = ctx;
        this.mList = new ArrayList<BTDeviceInfo>();
    }

    public void refreshDataToAdapter(List<BTDeviceInfo> btDeviceInfos) {
        if (null != btDeviceInfos && null != mList) {
            synchronized (mList) {
                mList.clear();
                mList.addAll(btDeviceInfos);
                notifyDataSetChanged();
            }
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
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bt_device_list, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        showBtDeviceList(holder, position);
        return convertView;
    }

    void showBtDeviceList(ViewHolder holder, int pos) {
        String deviceName = mList.get(pos).getName();
        String deviceAddress = mList.get(pos).getAddress();
        String deviceRssi = mList.get(pos).getRssi();

        holder.tv_device_name.setText(null != deviceName && deviceName.length() > 0 ? deviceName : "Unkown");
        holder.tv_device_address.setText(null != deviceAddress ? deviceAddress : "Unkown");
        holder.tv_device_rssi.setText(null != deviceRssi ? deviceRssi : "Unkown");
    }


    class ViewHolder {
        private TextView
                tv_device_name,
                tv_device_address,
                tv_device_rssi;

        // Constrctor
        public ViewHolder(View v) {
            tv_device_name = (TextView) v.findViewById(R.id.tv_device_name);
            tv_device_address = (TextView) v.findViewById(R.id.tv_device_address);
            tv_device_rssi = (TextView) v.findViewById(R.id.tv_device_rssi);
        }
    }
}
