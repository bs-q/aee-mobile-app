package com.bsq.aee.data.remote;

import com.bsq.aee.data.model.api.ResponseListObj;
import com.bsq.aee.data.model.api.ResponseWrapper;
import com.bsq.aee.data.model.api.request.CheckAccountRequest;
import com.bsq.aee.data.model.api.request.CreateAccountRequest;
import com.bsq.aee.data.model.api.request.CreatePostRequest;
import com.bsq.aee.data.model.api.request.LoginRequest;
import com.bsq.aee.data.model.api.request.LoginWithGoogleRequest;
import com.bsq.aee.data.model.api.request.ReplyRequest;
import com.bsq.aee.data.model.api.response.LoginResponse;
import com.bsq.aee.data.model.api.response.PostResponse;
import com.bsq.aee.data.model.api.response.ProfileResponse;
import com.bsq.aee.data.model.api.response.ReplyResponse;
import com.bsq.aee.data.model.api.response.UniversityResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/login")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<LoginResponse>> login(@Body LoginRequest request);

    @POST("auth/login-google")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<LoginResponse>> loginWithGoogle(@Body LoginWithGoogleRequest request);

    @GET("u/profile")
    Observable<ResponseWrapper<ProfileResponse>> profile();

    @GET("u/discussion/get-posts")
    Observable<ResponseListObj<PostResponse>> getPosts(@Query("size") Integer size,
                                                       @Query("currentPage") Integer page);

    @GET("u/discussion/get-replies")
    Observable<ResponseListObj<ReplyResponse>> getReplies(@Query("size") Integer size,
                                                          @Query("page") Integer page,
                                                          @Query("postId") long id);

    @GET("u/list-university")
    Observable<ResponseListObj<UniversityResponse>> getUniversities(@Query("size") Integer size,
                                                                    @Query("page") Integer page);

    @POST("u/discussion/reply-post")
    Observable<ResponseWrapper<String>> reply(@Body ReplyRequest request);

    @POST("u/discussion/create-post")
    Observable<ResponseWrapper<String>> createPost(@Body CreatePostRequest request);

    @POST("auth/check-register")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<String>> checkRegister(@Body CheckAccountRequest request);


    @POST("auth/register")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<String>> register(@Body CreateAccountRequest request);

}
