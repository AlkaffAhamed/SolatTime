package com.paiya.alkaff.solattime.model;

import com.google.gson.annotations.SerializedName;

public class DateData
{
    @SerializedName("to")
    String data;

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
