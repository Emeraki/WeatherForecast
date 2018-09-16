package com.example.cool_weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 闫航 on 2018/9/15.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

}
