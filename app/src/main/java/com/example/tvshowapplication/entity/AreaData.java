package com.example.tvshowapplication.entity;

//地区和分类电视
public class AreaData {
    private Integer areaId;
    private String area;
    private String zone;

    public AreaData() {
    }

//    public AreaData(Integer areaId, String area, String zone) {
//        this.areaId = areaId;
//        this.area = area;
//        this.zone = zone;
//    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
