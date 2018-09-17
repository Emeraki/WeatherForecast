package com.example.cool_weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 闫航 on 2018/9/16.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String information;
    }

    public class CarWash{
        @SerializedName("txt")
        public String information;
    }

    public class Sport{
        @SerializedName("txt")
        public String information;
    }

}
