package com.example.tvshowapplication.entity;


//电视台
public class TVStation {

    private Integer tvStationId;
    private String tvStationName;

    public TVStation() {
    }

    public Integer getTvStationId() {
        return tvStationId;
    }

    public void setTvStationId(Integer tvStationId) {
        this.tvStationId = tvStationId;
    }

    public String getTvStationName() {
        return tvStationName;
    }

    public void setTvStationName(String tvStationName) {
        this.tvStationName = tvStationName;
    }
}
