package com.tajiang.ceo.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 */
public class Province {
    private String areaName;
    private String areaId;
    private String areaParentId;
    private List<City> list;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaParentId() {
        return areaParentId;
    }

    public void setAreaParentId(String areaParentId) {
        this.areaParentId = areaParentId;
    }

    public List<City> getList() {
        return list;
    }

    public void setList(List<City> list) {
        this.list = list;
    }
}
