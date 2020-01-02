package com.mtjin.mapogreen.api;

import com.mtjin.mapogreen.model.research.SearchResult;
import com.mtjin.mapogreen.model.category_search.CategoryResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v2/local/search/keyword.json")
    Call<SearchResult> getResearchLocal(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("category_group_code") String category_group_code,
            @Query("x") String x,
            @Query("y") String y,
            @Query("radius") Integer radius
    );

    @GET("v2/local/search/category.json")
    Call<CategoryResult> getResearchCategory(
            @Header("Authorization") String token,
            @Query("category_group_code") String category_group_code,
            @Query("x") String x,
            @Query("y") String y,
            @Query("radius") int radius
    );
}
