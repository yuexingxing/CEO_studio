package com.tajiang.ceo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admins on 2017/3/13.
 */

public class Stall implements Parcelable {

    private  String id;//档口id
    private  String name;//档口名称
    private  String phone;//档口电话
    private  String storeId;//食堂id
    private  String notice;//食堂公告

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.storeId);
        dest.writeString(this.notice);
    }

    public Stall() {
    }

    protected Stall(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.storeId = in.readString();
        this.notice = in.readString();
    }

    public static final Parcelable.Creator<Stall> CREATOR = new Parcelable.Creator<Stall>() {
        @Override
        public Stall createFromParcel(Parcel source) {
            return new Stall(source);
        }

        @Override
        public Stall[] newArray(int size) {
            return new Stall[size];
        }
    };
}
