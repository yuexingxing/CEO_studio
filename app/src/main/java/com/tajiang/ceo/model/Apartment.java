package com.tajiang.ceo.model;

public class Apartment {

    private  String id;//宿舍id
    private  String schoolId;//学校id
    private  String name;//宿舍名称
    private  String apartmentName;//宿舍详细名称
    private  String zonesId;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private int state;

    public String getZonesId() {
        return zonesId;
    }

    public void setZonesId(String zonesId) {
        this.zonesId = zonesId;
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

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }
}