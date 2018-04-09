package com.tajiang.ceo.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 宿舍-1B101/1C205
 * Created by work on 2016/7/8.
 */
public class ApartmentZone {

    private String id;
    private String name;
    private String schoolId;
    private String createdAt;
    private String updatedAt;

    private ArrayList<Building> list;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    public ArrayList<Building> getList() {
        return list;
    }

    public void setList(ArrayList<Building> list) {
        this.list = list;
    }

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
