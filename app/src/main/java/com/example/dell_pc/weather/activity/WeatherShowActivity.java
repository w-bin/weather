package com.example.dell_pc.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.dell_pc.weather.R;
import com.example.dell_pc.weather.util.HttpCallbackListener;
import com.example.dell_pc.weather.util.HttpUtil;
import com.example.dell_pc.weather.util.LogUtil;
import com.example.dell_pc.weather.util.Utility;

/**
 * Created by dell-pc on 2016/3/10.
 */
public class WeatherShowActivity extends Activity {

    private TextView titleText;
    private TextView cityNameText;
    private TextView publishTimeText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_show_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.left_menu);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId=item.getItemId();
                switch (menuItemId){
                    case R.id.menuItem_refresh:
                        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(WeatherShowActivity.this);
                        String weatherCode=preferences.getString("weather_code","");
                        if(!TextUtils.isEmpty(weatherCode)){
                            queryWeatherInfo(weatherCode);
                        }
                        break;
                    case R.id.menuItem_change:
                        Intent intent=new Intent(WeatherShowActivity.this,MainActivity.class);
                        intent.putExtra("from_weather_activity", true);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menuItem_test:
                        Intent intent0=new Intent(WeatherShowActivity.this,TestActivity.class);
                        startActivity(intent0);
                        finish();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        titleText = (TextView) findViewById(R.id.title_text);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishTimeText = (TextView) findViewById(R.id.publish_time);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);

        String countryCode = getIntent().getStringExtra("country_code");
        if (!TextUtils.isEmpty(countryCode)) {
            publishTimeText.setText("同步中");
            queryWeatherCode(countryCode);
        } else {
            showWeather();
        }
    }

    //查询县级代号对应的天气代号
    private void queryWeatherCode(String countryCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
        LogUtil.e(WeatherShowActivity.class.toString(), "queryWeatherCode" + address);
        queryFromServer(address, "countryCode");
    }

    //查询天气代号所对应的天气
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        LogUtil.e(WeatherShowActivity.class.toString(), "queryWeatherInfo" + address);
        queryFromServer(address, "weatherCode");
    }

    //根据传入的地址和类型去向服务器查询天气代号或者天气信息
    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                LogUtil.e(WeatherShowActivity.class.toString(), "onFinish!!" + response);
                if ("countryCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(WeatherShowActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishTimeText.setText("同步失败");
                    }
                });
            }
        });
    }

    //从SharedPreferences文件中读取存储的天气信息，并显示到界面上
    private void showWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        titleText.setText(preferences.getString("city_name", ""));
        cityNameText.setText(preferences.getString("city_name", ""));
        temp1Text.setText(preferences.getString("temp1", ""));
        temp2Text.setText(preferences.getString("temp2", ""));
        weatherDespText.setText(preferences.getString("weather_desp", ""));
        publishTimeText.setText(preferences.getString("publish_time", "") + "发布");
        currentDateText.setText(preferences.getString("current_date", ""));
    }
}
