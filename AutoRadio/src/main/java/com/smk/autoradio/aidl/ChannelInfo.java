package com.smk.autoradio.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class ChannelInfo implements Parcelable {
    private int channel;// 频道
    private int channelType;// 频道类型
    private int signal;// 信号强度
    private int soundtrack;// 声道：立体声，单声道
    private boolean isFavorite;//是收藏数据

    public ChannelInfo(int channel, int channelType, int signal, int soundtrack, boolean isFavorite) {
        this.channel = channel;
        this.channelType = channelType;
        this.signal = signal;
        this.soundtrack = soundtrack;
        this.isFavorite = isFavorite;
    }

    public ChannelInfo() {

    }


    public static final Creator<ChannelInfo> CREATOR = new Creator<ChannelInfo>() {
        @Override
        public ChannelInfo createFromParcel(Parcel in) {
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.channel = in.readInt();
            channelInfo.channelType = in.readInt();
            channelInfo.signal = in.readInt();
            channelInfo.soundtrack = in.readInt();
            channelInfo.isFavorite = in.readByte() != 0;
            return channelInfo;
        }

        @Override
        public ChannelInfo[] newArray(int size) {
            return new ChannelInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(channel);
        dest.writeInt(channelType);
        dest.writeInt(signal);
        dest.writeInt(soundtrack);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }


    @Override
    public String toString() {
        return "ChannelInfo{" +
                "channel=" + channel +
                ", channelType=" + channelType +
                ", signal=" + signal +
                ", soundtrack=" + soundtrack +
                ", isFavorite=" + isFavorite +
                '}';
    }


    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int getSoundtrack() {
        return soundtrack;
    }

    public void setSoundtrack(int soundtrack) {
        this.soundtrack = soundtrack;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
