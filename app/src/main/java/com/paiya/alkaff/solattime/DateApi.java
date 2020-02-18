package com.paiya.alkaff.solattime;

import com.paiya.alkaff.solattime.model.DateData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DateApi
{
    @GET("calendar")
    Call<DateData> getDate(@Query("day") int day, @Query("month") int mth, @Query("year") int year, @Query("convert_to") int convert_to);
}
