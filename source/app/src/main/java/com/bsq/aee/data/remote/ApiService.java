package com.bsq.aee.data.remote;

import com.bsq.aee.data.model.api.ResponseWrapper;
import com.bsq.aee.data.model.api.request.LoginRequest;
import com.bsq.aee.data.model.api.response.LoginResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("v1/account/login")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<LoginResponse>> login(@Body LoginRequest request);

}
