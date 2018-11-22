package com.semisky.bluetoothproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.entity.DevicesListEntity;

import java.util.List;

/**
 * Created by chenhongrui on 2017/12/19
 * <p>
 * 内容摘要：收藏 List adapter
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class DeviceListItemAdapter extends ArrayAdapter<DevicesListEntity> {

    private static final String TAG = "DeviceListItemAdapter";

    private int resourceID;
    private Context context;
    private List<DevicesListEntity> ListData;

    public synchronized void setList(List<DevicesListEntity> list) {
        this.ListData = list;
        Log.d(TAG, "setList: " + list.size());
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        ListData.remove(position);
        notifyDataSetChanged();
    }

    public DeviceListItemAdapter(@NonNull Context context, int resource, @NonNull List<DevicesListEntity> objects) {
        super(context, resource, objects);
        ListData = objects;
        resourceID = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DevicesListEntity data = ListData.get(position);

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.relativeLayout = view.findViewById(R.id.rl_background);
            viewHolder.tvDeviceName = view.findViewById(R.id.tv_device_name);
            viewHolder.ivConnect = view.findViewById(R.id.iv_connect);
            viewHolder.ivDelete = view.findViewById(R.id.iv_delete);

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvDeviceName.setText(data.getDeviceName());

        DevicesListEntity.DeviceConnectStatus status = data.getStatus();
        switch (status) {
            case CONNECTED:
                viewHolder.ivConnect.setVisibility(View.VISIBLE);
                viewHolder.ivDelete.setVisibility(View.VISIBLE);
                viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (connectListener != null) {
                            connectListener.onConnectClick(data.getAddress());
                        }
                    }
                });

                viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (connectListener != null) {
                            connectListener.onReqBtUnpair(data.getAddress(), position);
                        }
                    }
                });

                break;
            case CONNECTING:

                break;
            case NOT_CONNECT:
                viewHolder.ivConnect.setVisibility(View.VISIBLE);
                viewHolder.ivDelete.setVisibility(View.GONE);
                viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (connectListener != null) {
                            connectListener.onConnectClick(data.getAddress());
                        }
                    }
                });
                break;
        }
        return view;
    }

    private class ViewHolder {
        TextView tvDeviceName;
        ImageView ivConnect, ivDelete;
        RelativeLayout relativeLayout;
    }

    public interface onDevicesConnectListener {
        void onConnectClick(String address);

        void onReqBtUnpair(String address, int position);
    }

    private onDevicesConnectListener connectListener;

    public void setOnDevicesConnectListener(onDevicesConnectListener connectListener) {
        this.connectListener = connectListener;
    }

    public interface onItemDeleteListener {
        void onDeleteClick(int position, int freq);
    }

    private onItemDeleteListener deleteListener;

    void setOnItemDeleteListener(onItemDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }
}
