package com.smk.bt.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.smk.bt.bean.BTDeviceInfo;

import java.util.ArrayList;
import java.util.List;

public class BTDeviceListAdapter extends BaseAdapter {
    private Context mContext;
    private List<BTDeviceInfo> mBTDeviceInfoList;

    public BTDeviceListAdapter(Context ctx) {
        this.mContext = ctx;
        this.mBTDeviceInfoList = new ArrayList<BTDeviceInfo>();
    }

    public void updateData(List<BTDeviceInfo> btDeviceInfos) {
        if (null != btDeviceInfos) {
            mBTDeviceInfoList.addAll(btDeviceInfos);
            mBTDeviceInfoList.clear();
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
        if(null == convertView){
//            convertView = LayoutInflater.from(mContext).inflate();
                    holder = new ViewHolder(convertView);
        }

        return null;
    }

    class ViewHolder{

        public ViewHolder(View v){

        }

    }
}
