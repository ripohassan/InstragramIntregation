package com.mcc.instagramintegration.Api;

import com.mcc.instagramintegration.model.Datum;
import com.mcc.instagramintegration.model.Images;
import com.mcc.instagramintegration.model.InstaModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET(HttpParams.MEDIA)
    Call<InstaModel> getALLImages(@Query(HttpParams.API_END_POINT) String access_token);
}
