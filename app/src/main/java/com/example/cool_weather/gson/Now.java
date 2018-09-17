package com.example.cool_weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 闫航 on 2018/9/16.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String information;
    }

}
