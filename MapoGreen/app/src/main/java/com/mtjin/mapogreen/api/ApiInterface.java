package com.mtjin.mapogreen.api;

import com.mtjin.mapogreen.model.address_search.AddressSearch;
import com.mtjin.mapogreen.model.research.SearchResult;
import com.mtjin.mapogreen.model.category_search.CategoryResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v2/local/search/keyword.json")
    Call<CategoryResult> getSearchLocation(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("size") int size
    );

    @GET("v2/local/search/category.json")
    Call<CategoryResult> getSearchCategory(
            @Header("Authorization") String token,
            @Query("category_group_code") String category_group_code,
            @Query("x") String x,
            @Query("y") String y,
            @Query("radius") int radius
    );

    @GET("v2/local/search/address.json")
    Call<AddressSearch> getSearchAddress(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("size") int size
    );

}
