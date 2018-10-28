package com.noxstudio.fruitcas.android.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyRetrofitInter {

    @GET("/index.php?")
    Call<User> getUrl(@Query("country") String country,@Query("tz") int timeZone);

    @GET("/index.php?")
    Call<User> getUrlWithoutId(@Query("id") int id,@Query("country") String country,@Query("tz") int timeZone);
}
