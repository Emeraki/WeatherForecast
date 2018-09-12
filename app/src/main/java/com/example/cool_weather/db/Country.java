package com.example.cool_weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 闫航 on 2018/9/12.
 */

public class Country extends DataSupport {

    private int id;

    private String countryName;

    private String weatherId;

    private int cityId;

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCountryName(){
        return this.countryName;
    }

    public void setCountryName(String countryName){
        this.countryName = countryName;
    }

    public String getWeatherId(){
        return this.weatherId;
    }

    public void setWeatherId(String weatherId){
        this.weatherId = weatherId;
    }

    public int getCityId(){
        return this.cityId;
    }

    public void setCityId(int cityId){
        this.cityId = cityId;
    }

}
