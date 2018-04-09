package com.tajiang.ceo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 宿舍楼-1栋、2栋
 * Created by work on 2016/7/7.
 */
public class Building implements Parcelable {

    public static final int BUILDING_STATE_OPENED = 0;//默认状态为0打开
    public static final int BUILDING_STATE_CLOSED = 1;

    private String id;
    private String schoolId;
    private String name;
    private String zonesId;
    private int state = BUILDING_STATE_OPENED; //默认状态为0打开

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZonesId() {
        return zonesId;
    }

    public void setZonesId(String zonesId) {
        this.zonesId = zonesId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.schoolId);
        dest.writeString(this.name);
        dest.writeString(this.zonesId);
        dest.writeInt(this.state);
    }

    public Building() {
    }

    protected Building(Parcel in) {
        this.id = in.readString();
        this.schoolId = in.readString();
        this.name = in.readString();
        this.zonesId = in.readString();
        this.state = in.readInt();
    }

    public static final Parcelable.Creator<Building> CREATOR = new Parcelable.Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel source) {
            return new Building(source);
        }

        @Override
        public Building[] newArray(int size) {
            return new Building[size];
        }
    };
}
