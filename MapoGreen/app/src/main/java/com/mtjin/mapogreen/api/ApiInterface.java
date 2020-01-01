package com.mtjin.mapogreen.api;

import com.mtjin.mapogreen.model.ResearchResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("coord2regioncode.json?")
    Call<ResearchResult> getResearchLocal(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("category_group_code") String category_group_code,
            @Query("x") String x,
            @Query("y") String y,
            @Query("radius") String radius ,
            @Query("sort") String sort
    );

}
