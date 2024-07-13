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
    Call<String> getTVstationString(@Query("theAreaID") Long theAreaID);

    //获取对应电视台的电视频道
    @GET("getTVchannelString")
    Call<String> getTVchannelString(@Query("theTVstationID") Long theTVstationID);

    //获取对应电视频道的电视节目，日期不传默认当天，userID也可以不传
    @GET("getTVprogramString")
    Call<String> getTVprogramString(@Query("theTVchannelID") Long theTVchannelID,@Query("theDate") String theDate,@Query("userID") String userID);


//    @GET("link/getAllLink")
//    Call<String> getAllLink();
//
//    @POST("user/register")
//    Call<ResponseResult> register(@Body RequestBody requestBody);
//
//    @GET("user/getAvatar")
//    Call<ResponseResult<String>> getAvatar(@Query("userId") Long userId);
//
//    @GET("user/follow")
//    Call<ResponseResult> follow(@Query("userId")Long userId,@Query("followId")Long followId);
//
//    @GET("user/followerList")
//    Call<ResponseResult<UserInfoResponse>> followerList(@Query("pageNum")Integer pageNum, @Query("pageSize")Integer pageSize,@Query("userId") Long userId);
//
//
//    @DELETE("user/cancelFollow")
//    Call<ResponseResult> cancelFollow(@Query("userId")Long userId,@Query("followId")Long followId);
//
//    @POST("login")
//    Call<ResponseResult<LoginUser>> login(@Body RequestBody requestBody);
//
//    @POST("logout")
//    Call<ResponseResult> logout();
//
//    @GET("comment/commentList")
//    Call<ResponseResult<CustomCommentModel>> getCommentList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("articleId") Long articleId,@Query("currentUserId") Long currentUserId,@Query("isArticle") boolean isArticle);
//
//    @GET("comment/replyList")
//    Call<ResponseResult<PagerRepliesEnableVo>> getReplyList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("commentId") Long commentId,@Query("currentUserId") Long currentUserId);
//
//
//    @POST("comment")
//    Call<ResponseResult> addComment(@Body RequestBody requestBody);
//
//    @GET("comment/isAddComment")
//    Call<ResponseResult<AddPraiseVo>> isAddComment(@Query("currentUserId") Long currentUserId);
//
//    @GET("comment/addPrize")
//    Call<ResponseResult> addPrize(@Query("commentId") Long commentId,@Query("currentUserId") Long currentUserId);
//
//    @DELETE("comment/deletePrize")
//    Call<ResponseResult> deletePrize(@Query("commentId") Long commentId,@Query("currentUserId") Long currentUserId);
//
//    @GET("article/searchArticle")
//    Call<ResponseResult<ArticleResponse>> searchArticle(@Query("pageNum") int pageNum,@Query("pageSize") int pageSize,@Query("content") String content);
//
//    @GET("article/{id}/{userId}")
//    Call<ResponseResult<ArticleDetailVo>> getArticleDetail(@Path("id") Long articleId,@Path("userId") Long currentUserId);
//
//    @GET("article/articleList")
//    Call<ResponseResult<ArticleResponse>> getRecommandArticleList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize,@Query("userId") Long userId);
//
//
//    @GET("article/draftList")
//    Call<ResponseResult<ArticleResponse>> draftArticleList(@Query("pageNum") Integer pageNum,@Query("pageSize") Integer pageSize,@Query("userId") Long userId);
//
//    @GET("article/hotArticleList")
//    Call<ResponseResult<List<Article>>> getHotArticleList();
//
//    @POST("article/add")
//    Call<ResponseResult> add(@Body RequestBody requestBody);
//
//    @GET("article/isAddPraise")
//    Call<ResponseResult<AddPraiseVo>> isAddPraise(@Query("currentUserId")Long currentUserId);
//
//    @GET("article/addViewCount")
//    Call<ResponseResult> addViewCount(@Query("articleId") Long articleId);
//
//    @PUT("article/updateArticle")
//    Call<ResponseResult> updateArticle(@Body RequestBody requestBody);
//
//    @DELETE("article/deleteArticle/{id}")
//    Call<ResponseResult> deleteArticle(@Path("id") Long id);
//
//    @PUT("user/updateUserInfo")
//    Call<Void> updateUserInfo(@Body RequestBody requestBody);
//
//    @GET("category/getCategoryList")
//    Call<ResponseResult<List<CategoryResponse>>> getCategoryList(@Query("userId") String userId);
//
//    @GET("category/addCategory")
//    Call<ResponseResult<Long>> addCategory(@Query("name") String name,@Query("userId") Long userId,@Query("descriptipn") String description);
//
//    @Multipart
//    @POST("upload")
//    Call<ResponseResult<String>> uploadImg(@Part MultipartBody.Part img);
//
//    @Multipart
//    @POST("uploadImages")
//    Call<ResponseResult<String>> uploadImages(@Part MultipartBody.Part[] imgs);
//
//
//    @POST("postings/create")
//    Call<ResponseResult> createUserPosting(@Body RequestBody requestBody);
//
//    @GET("postings/postingList")
//    Call<ResponseResult<UserPostingsResponse>> listByUserId(@Query("pageNum")Integer pageNum,@Query("pageSize")Integer pageSize, @Query("userId")Long userId);
//
//    @GET("postings/list")
//    Call<ResponseResult<UserPostingsResponse>> postingslist(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize,@Query("currentUserId") Long currentUserId);
//
//    @GET("postings/addPrize")
//    Call<ResponseResult> addPostingPrize(@Query("postingId") Long commentId,@Query("currentUserId") Long currentUserId);
//
//    @DELETE("postings/deletePrize")
//    Call<ResponseResult> deletePostingPrize(@Query("postingId") Long commentId,@Query("currentUserId") Long currentUserId);
//
//    @GET("article/like")
//    Call<ResponseResult> like(@Query("articleId") Long articleId,@Query("userId") Long userId);
//
//    @DELETE("article/dislike")
//    Call<ResponseResult> dislike(@Query("articleId") Long articleId,@Query("userId") Long userId);
//
//    @GET("article/star")
//    Call<ResponseResult> star(@Query("articleId") Long articleId,@Query("userId") Long userId);
//
//    @DELETE("article/deleteStar")
//    Call<ResponseResult> deleteStar(@Query("articleId") Long articleId,@Query("userId") Long userId);
//
//    @GET("article/starList")
//    Call<ResponseResult<ArticleResponse>> starList(@Query("pageNum") Integer pageNum,@Query("pageSize") Integer pageSize,@Query("userId") Long userId);
//
//    @GET("article/historyList")
//    Call<ResponseResult<HistoryArticleResponse>> historyList(@Query("pageNum") Integer pageNum, @Query("pageSize") Integer pageSize, @Query("userId") Long userId,@Query("date")String date);
//
//    @GET("search/getHotList")
//    Call<ResponseResult<SearchContentResponseVo>> getHotSearchContentList(@Query("pageNum") Integer pageNum, @Query("pageSize")Integer pageSize);
//
//    @GET("history/addHistory")
//    Call<ResponseResult> addHistory(@Query("articleId") Long articleId,@Query("userId") Long userId);
}
