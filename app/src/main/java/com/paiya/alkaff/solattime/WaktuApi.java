package com.paiya.alkaff.solattime;

import com.paiya.alkaff.solattime.model.WaktuData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface WaktuApi
{
    @GET("prayer_times")
    Call<WaktuData> getWaktu(@Query("latitude") float Latitude, @Query("longitude") float Longitude, @Query("timezone") String TimeZone);
}
