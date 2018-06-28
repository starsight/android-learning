package com.wenjiehe.android_learning.Entry;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GankApi {
    String BASE_URL = "http://gank.io/";

    @GET("api/day/{year}/{month}/{day}")
    Observable<GankInfo> loadData(@Path("year") int year, @Path("month") int month, @Path("day") int day);
}

