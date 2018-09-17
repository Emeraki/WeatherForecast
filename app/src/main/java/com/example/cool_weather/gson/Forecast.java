package com.example.cool_weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 闫航 on 2018/9/16.
 */

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String information;
    }

}
