package com.example.tvshowapplication.entity;

import java.util.List;

//电视节目
public class TVProgram {
    private String playTime;
    private String meridiem;
    private TVStation tvStationInfo;//电视台信息
    private List<String> tvPrograms;//节目信息

    public TVProgram() {
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getMeridiem() {
        return meridiem;
    }

    public void setMeridiem(String meridiem) {
        this.meridiem = meridiem;
    }

    public TVStation getTvStationInfo() {
        return tvStationInfo;
    }

    public void setTvStationInfo(TVStation tvStationInfo) {
        this.tvStationInfo = tvStationInfo;
    }

    public List<String> getTvPrograms() {
        return tvPrograms;
    }

    public void setTvPrograms(List<String> tvPrograms) {
        this.tvPrograms = tvPrograms;
    }
}
