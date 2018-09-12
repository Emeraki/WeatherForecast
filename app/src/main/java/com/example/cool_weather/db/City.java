package com.example.cool_weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 闫航 on 2018/9/6.
 */

public class City extends DataSupport {

    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCityName(){
        return this.cityName;
    }

    public void setCityName(String cityName){
        this.cityName = cityName;
    }

    public int getCityCode(){
        return this.cityCode;
    }

    public void setCityCode(int cityCode){
        this.cityCode = cityCode;
    }

    public int getProvinceId(){
        return this.provinceId;
    }

    public void setProvinceId(int provinceId){
        this.provinceId = provinceId;
    }

}
