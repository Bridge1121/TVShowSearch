package com.example.tvshowapplication;



import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //获取所有地区和分类电视
    @GET("getAreaString")
    Call<String> getAreaString();

    //获取对应地区或分类电视的电视台
    @GET("getTVstationString")
    Call<String> getTVstationString(@Query("theAreaID") Integer theAreaID);

    //获取对应电视台的电视频道
    @GET("getTVchannelString")
    Call<String> getTVchannelString(@Query("theTVstationID") Integer theTVstationID);

    //获取对应电视频道的电视节目，日期不传默认当天，userID也可以不传
    @GET("getTVprogramString")
    Call<String> getTVprogramString(@Query("theTVchannelID") Integer theTVchannelID,@Query("theDate") String theDate,@Query("userID") String userID);



}
