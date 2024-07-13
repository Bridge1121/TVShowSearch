package com.example.tvshowapplication.entity;

//频道
public class TVChannel {
    private Integer tvChannelId;
    private String tvChannel;

    public TVChannel() {
    }

    public Integer getTvChannelId() {
        return tvChannelId;
    }

    public void setTvChannelId(Integer tvChannelId) {
        this.tvChannelId = tvChannelId;
    }

    public String getTvChannel() {
        return tvChannel;
    }

    public void setTvChannel(String tvChannel) {
        this.tvChannel = tvChannel;
    }
}
