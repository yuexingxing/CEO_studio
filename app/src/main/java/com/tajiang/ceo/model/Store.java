package com.tajiang.ceo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class  Store implements Parcelable {
    private  String id;//食堂id
    private  String name;//食堂名称
    private  String sellerName;//食堂电话
    private  String schoolId;//学校id
    private  String notice;//食堂公告
    private  int state;//餐厅状态，0关闭，1开启
    private  int sales;//销量
    private  int offline;//上线下线： 0为开启 1为关闭'
    private  List<Stall> stallsList; //食堂底下的档口列表

    public List<Stall> getStallsList() {
        return stallsList;
    }

    public void setStallsList(List<Stall> stallsList) {
        this.stallsList = stallsList;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
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

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getOffline() {
        return offline;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.sellerName);
        dest.writeString(this.schoolId);
        dest.writeString(this.notice);
        dest.writeInt(this.state);
        dest.writeInt(this.sales);
        dest.writeInt(this.offline);
        dest.writeTypedList(this.stallsList);
    }

    public Store() {
    }

    protected Store(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.sellerName = in.readString();
        this.schoolId = in.readString();
        this.notice = in.readString();
        this.state = in.readInt();
        this.sales = in.readInt();
        this.offline = in.readInt();
        this.stallsList = in.createTypedArrayList(Stall.CREATOR);
    }

    public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
}