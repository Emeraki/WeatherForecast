package com.example.cool_weather.gson;

/**
 * Created by 闫航 on 2018/9/16.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
