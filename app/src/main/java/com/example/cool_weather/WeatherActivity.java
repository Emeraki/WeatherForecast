package com.example.cool_weather;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cool_weather.gson.Forecast;
import com.example.cool_weather.gson.Weather;
import com.example.cool_weather.util.HttpUtil;
import com.example.cool_weather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    private String mWeatherId;

    public DrawerLayout drawerLayout;

    private Button navButton;

    private int choose_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        //初始化各个控件
        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carWashText = (TextView)findViewById(R.id.car_wash_text);
        sportText = (TextView)findViewById(R.id.sport_text);
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navButton = (Button)findViewById(R.id.nav_button);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        //缓存数据
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }

        final String weatherString = prefs.getString("weather",null);

        if(weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponce(weatherString);
            mWeatherId = weather.basic.weatherId;

            Log.e("yyyyyyyyyyy",mWeatherId);

            showWeatherInfo(weather);
        }else {
            //无缓存去服务器取数据
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);

            Log.e("wwwwwwwwwww",mWeatherId);

            requestWeather(mWeatherId);
        }

        /*
        下拉刷新
         */
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Log.e("sssssssssssss",mWeatherId);

                requestWeather(mWeatherId);
            }
        });


        /*
        左边弹窗
         */
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    private void initChooseService() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherString = prefs.getString("weather",null);

        if(weatherString != null){

            AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
            builder.setIcon(R.drawable.alarm);
            builder.setTitle("温馨提示");
            builder.setMessage("是否开启自动更新");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(WeatherActivity.this,AutoUpdateService.class);
                    startService(intent);
                    Toast.makeText(WeatherActivity.this,"已开启后台更新天气",Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(WeatherActivity.this,"已关闭后台更新天气",Toast.LENGTH_SHORT).show();

                }
            });
            builder.show();

        }

    }

    private void loadBingPic() {

        String address = "http://guolin.tech/api/bing_pic";

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String bingPic = response.body().string();

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();

                editor.putString("bing_pic",bingPic);
                editor.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });

            }
        });

    }

    public void requestWeather(final String weatherId) {

        Log.e("itiswrong",weatherId);

        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +"&key=e6d980531d074b3da3902da29dc24fc7";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responceText = response.body().string();

                final Weather weather = Utility.handleWeatherResponce(responceText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){

                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responceText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);

                        }else {

                            Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();

                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });

        loadBingPic();

    }

    private void showWeatherInfo(Weather weather) {

        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.more.information;

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();

        for(Forecast forecast : weather.forecastList){

            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);

            TextView dateText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.information);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);

            forecastLayout.addView(view);


        }

        if(weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.information;
        String carWash = "洗车指数：" + weather.suggestion.carWash.information;
        String sport = "运动建议" + weather.suggestion.carWash.information;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);

        initChooseService();

    }



}
