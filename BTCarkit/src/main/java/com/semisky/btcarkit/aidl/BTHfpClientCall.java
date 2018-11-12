package com.semisky.btcarkit.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LiuYong on 2018/10/8.
 */

public final class BTHfpClientCall implements Parcelable {
    public static final int CALL_STATE_ACTIVE = 0;
    public static final int CALL_STATE_HELD = 1;
    public static final int CALL_STATE_DIALING = 2;
    public static final int CALL_STATE_ALERTING = 3;
    public static final int CALL_STATE_INCOMING = 4;
    public static final int CALL_STATE_WAITING = 5;
    public static final int CALL_STATE_HELD_BY_RESPONSE_AND_HOLD = 6;
    public static final int CALL_STATE_TERMINATED = 7;

    private final int mId;
    private int mState;
    private String mNumber;
    private String mName;
    private boolean mMultiParty;
    private final boolean mOutgoing;

    public BTHfpClientCall(int mId, int mState, String mNumber, String mName, boolean mMultiParty, boolean mOutgoing) {
        this.mId = mId;
        this.mState = mState;
        this.mNumber = mNumber;
        this.mName = mName;
        this.mMultiParty = mMultiParty;
        this.mOutgoing = mOutgoing;
    }

    public static final Creator<BTHfpClientCall> CREATOR = new Creator<BTHfpClientCall>() {
        @Override
        public BTHfpClientCall createFromParcel(Parcel in) {
            return new BTHfpClientCall(in.readInt(), in.readInt(), in.readString(), in.readString(), (in.readByte() == 1), (in.readByte() == 1));
        }

        @Override
        public BTHfpClientCall[] newArray(int size) {
            return new BTHfpClientCall[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mState);
        dest.writeString(mNumber);
        dest.writeString(mName);
        dest.writeByte((byte) (mMultiParty ? 1 : 0));
        dest.writeByte((byte) (mOutgoing ? 1 : 0));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("BTHfpClientCall{mId: ");
        builder.append(this.mId);
        builder.append(", mState: ");
        switch (this.mState) {
            case 0:
                builder.append("ACTIVE");
                break;
            case 1:
                builder.append("HELD");
                break;
            case 2:
                builder.append("DIALING");
                break;
            case 3:
                builder.append("ALERTING");
                break;
            case 4:
                builder.append("INCOMING");
                break;
            case 5:
                builder.append("WAITING");
                break;
            case 6:
                builder.append("HELD_BY_RESPONSE_AND_HOLD");
                break;
            case 7:
                builder.append("TERMINATED");
                break;
            default:
                builder.append(this.mState);
        }

        builder.append(", mNumber: ");
        builder.append(this.mNumber);
        builder.append(", mName: ");
        builder.append(this.mName);
        builder.append(", mMultiParty: ");
        builder.append(this.mMultiParty);
        builder.append(", mOutgoing: ");
        builder.append(this.mOutgoing);
        builder.append("}");
        return builder.toString();
    }

    public int getId() {
        return mId;
    }

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public boolean isMultiParty() {
        return mMultiParty;
    }

    public void setMultiParty(boolean mMultiParty) {
        this.mMultiParty = mMultiParty;
    }

    public boolean isOutgoing() {
        return mOutgoing;
    }


}
