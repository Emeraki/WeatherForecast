package com.example.cool_weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 闫航 on 2018/9/16.
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Suggestion suggestion;

    public Now now;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
