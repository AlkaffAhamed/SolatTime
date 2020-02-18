package com.paiya.alkaff.solattime.model;

import com.google.gson.annotations.SerializedName;

public class WaktuData
{
    @SerializedName("results")
    public Results results;

    public class Results
    {
        @SerializedName("Fajr")
        public String t1;

        @SerializedName("Duha")
        public String t2;

        @SerializedName("Dhuhr")
        public String t3;

        @SerializedName("Asr")
        public String t4;

        @SerializedName("Maghrib")
        public String t5;

        @SerializedName("Isha")
        public String t6;

    }
}
