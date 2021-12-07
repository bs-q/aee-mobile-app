package com.bsq.aee.data.remote;

import com.bsq.aee.data.model.api.ResponseListObj;
import com.bsq.aee.data.model.api.ResponseWrapper;
import com.bsq.aee.data.model.api.request.ChangePasswordRequest;
import com.bsq.aee.data.model.api.request.CheckAccountRequest;
import com.bsq.aee.data.model.api.request.CreateAccountRequest;
import com.bsq.aee.data.model.api.request.CreatePostRequest;
import com.bsq.aee.data.model.api.request.LoginRequest;
import com.bsq.aee.data.model.api.request.LoginWithGoogleRequest;
import com.bsq.aee.data.model.api.request.ReplyRequest;
import com.bsq.aee.data.model.api.response.FieldResponse;
import com.bsq.aee.data.model.api.response.LoginResponse;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.data.model.api.response.ProfileResponse;
import com.bsq.aee.data.model.api.response.ReplyResponse;
import com.bsq.aee.data.model.api.response.UniversityResponse;
import com.bsq.aee.data.model.api.response.news.NewsResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/auth/login")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<LoginResponse>> login(@Body LoginRequest request);

    @POST("api/auth/login-google")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<LoginResponse>> loginWithGoogle(@Body LoginWithGoogleRequest request);

    @GET("api/u/profile")
    Observable<ResponseWrapper<ProfileResponse>> profile();

    @GET("api/u/discussion/get-posts")
    Observable<ResponseListObj<PostResponse>> getPosts(@Query("size") Integer size,
                                                       @Query("currentPage") Integer page);

    @GET("api/u/discussion/get-replies")
    Observable<ResponseListObj<ReplyResponse>> getReplies(@Query("size") Integer size,
                                                          @Query("page") Integer page,
                                                          @Query("postId") long id);

    @GET("api/u/list-university")
    Observable<ResponseListObj<UniversityResponse>> getUniversities(@Query("size") Integer size,
                                                                    @Query("page") Integer page);
    @GET("api/u/list-field/{id}")
    Observable<ResponseWrapper<List<FieldResponse>>> getField(@Path("id") long id);

    @GET("api/u/list-news")
    Observable<ResponseWrapper<List<NewsResponse>>> getNews();

    @GET("api/u/get-favorite")
    Observable<ResponseListObj<FieldResponse>> getFavorite();

    @POST("api/u/add-favorite/{id}")
    Observable<ResponseWrapper<String>> addFavorite(@Path("id") long id);

    @GET("api/u/my-post")
    Observable<ResponseWrapper<List<PostResponse>>> myPost();

    @POST("api/u/change-password")
    Observable<ResponseWrapper<String>> changePassword(@Body ChangePasswordRequest request);

    @GET("api/u/search-university")
    Observable<ResponseWrapper<List<UniversityResponse>>> searchUniversity(@Query("name") String name);

    @GET("api/u/search-post")
    Observable<ResponseWrapper<List<PostResponse>>> searchPost(@Query("title") String title);

    @POST("api/u/discussion/reply-post")
    Observable<ResponseWrapper<String>> reply(@Body ReplyRequest request);

    @POST("api/u/discussion/create-post")
    Observable<ResponseWrapper<String>> createPost(@Body CreatePostRequest request);

    @POST("api/auth/check-register")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<String>> checkRegister(@Body CheckAccountRequest request);


    @POST("api/auth/register")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<String>> register(@Body CreateAccountRequest request);

}
