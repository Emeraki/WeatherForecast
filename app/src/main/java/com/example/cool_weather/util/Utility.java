package com.example.cool_weather.util;

import android.text.TextUtils;

import com.example.cool_weather.db.City;
import com.example.cool_weather.db.Country;
import com.example.cool_weather.db.Province;
import com.example.cool_weather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 闫航 on 2018/9/15.
 */

public class Utility {

    /*
    解析省份信息
     */
    public static boolean handleProvinceResponse(String response){

        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvience = new JSONArray(response);
                for(int i=0;i<allProvience.length();i++){
                    JSONObject provienceObject = allProvience.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provienceObject.getString("name"));
                    province.setProvinceCode(provienceObject.getInt("id"));
                    province.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;

    }



    /*
    解析城市信息
     */
    public static boolean handleCityResponse(String response,int provienceId){

        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCity = new JSONArray(response);
                for(int i=0;i<allCity.length();i++){
                    JSONObject cityObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provienceId);
                    city.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;

    }



    /*
    解析乡镇信息
     */
    public static boolean handleCountryResponse(String response,int cityId){

        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCountry = new JSONArray(response);
                for(int i=0;i<allCountry.length();i++){
                    JSONObject countryObject = allCountry.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryName(countryObject.getString("name"));
                    country.setWeatherId(countryObject.getString("weather_id"));
                    country.setCityId(cityId);
                    country.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;

    }

    /*
    返回数据解析成weather实体类
     */
    public static Weather handleWeatherResponce(String responce){
        try {

            JSONObject jsonObject = new JSONObject(responce);

            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");

            String weatherContent = jsonArray.getJSONObject(0).toString();

            return new Gson().fromJson(weatherContent,Weather.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
