package com.example.dell_pc.weather.activity;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell_pc.weather.R;
import com.example.dell_pc.weather.model.CountryControllerListViewItem;
import com.example.dell_pc.weather.model.WeatherDB;
import com.example.dell_pc.weather.util.HttpCallbackListener;
import com.example.dell_pc.weather.util.HttpUtil;
import com.example.dell_pc.weather.util.LogUtil;
import com.example.dell_pc.weather.util.Utility;

/**
 * Created by dell-pc on 2016/3/10.
 */
public class WeatherShowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView titleText;
    private TextView cityNameText;
    private TextView publishTimeText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;

    private SwipeRefreshLayout swipeRefreshLayout;
    private android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    swipeRefreshLayout.setRefreshing(true);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String weatherCode = preferences.getString("weather_code", "");
                    if (!TextUtils.isEmpty(weatherCode)) {
                        queryWeatherInfo(weatherCode);
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_show_layout, container, false);
        init(view);
        return view;
    }

    protected void init(View view) {
        titleText = (TextView) getActivity().findViewById(R.id.title_text);
        cityNameText = (TextView) view.findViewById(R.id.city_name);
        publishTimeText = (TextView) view.findViewById(R.id.publish_time);
        weatherDespText = (TextView) view.findViewById(R.id.weather_desp);
        temp1Text = (TextView) view.findViewById(R.id.temp1);
        temp2Text = (TextView) view.findViewById(R.id.temp2);
        currentDateText = (TextView) view.findViewById(R.id.current_date);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.weather_show_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.blue);

        String countryCode = getActivity().getIntent().getStringExtra("country_code");
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
        LogUtil.e(WeatherShowFragment.class.toString(), "queryWeatherCode" + address);
        queryFromServer(address, "countryCode");
    }

    //查询天气代号所对应的天气
    public void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        LogUtil.e(WeatherShowFragment.class.toString(), "queryWeatherInfo" + address);
        queryFromServer(address, "weatherCode");
    }

    //根据传入的地址和类型去向服务器查询天气代号或者天气信息
    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                LogUtil.e(WeatherShowFragment.class.toString(), "onFinish!!" + response);
                if ("countryCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    String provinceName;
                    Utility.handleWeatherResponse(getActivity(), response,getActivity().getIntent().getStringExtra("province_name"),
                            getActivity().getIntent().getStringExtra("city_name"));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        MyBaseActivity.title="天气详情";
        titleText.setText("天气详情");
        String provinceName=preferences.getString("province_name","");
        String cityName=preferences.getString("city_name", "");
        String countryName=preferences.getString("country_name","");
        String temp1=preferences.getString("temp1","");
        String temp2=preferences.getString("temp2","");
        String weatherDesp=preferences.getString("weather_desp","");
        String publishTime=preferences.getString("publish_time","");
        String currentDate=preferences.getString("current_date","");

        cityNameText.setText(provinceName+"-"+cityName+"-"+countryName);
        temp1Text.setText(temp1);
        temp2Text.setText(temp2);
        weatherDespText.setText(weatherDesp);
        publishTimeText.setText(publishTime + "发布");
        currentDateText.setText(currentDate);
        swipeRefreshLayout.setRefreshing(false);        //将进度条关闭

        //如果是从CountryController 跳转而来，则将获取的天气信息存储到数据库
        if (getActivity().getIntent().getBooleanExtra("isFromCountryController", false)) {
            CountryControllerListViewItem item = new CountryControllerListViewItem(provinceName,cityName,countryName,
                    preferences.getString("weather_code",""),temp1,temp2,weatherDesp,publishTime,currentDate);
            WeatherDB weatherDB=MyBaseActivity.weatherDB;
            weatherDB.saveCountryController(item);
            getActivity().getIntent().putExtra("isFromCountryController", false);
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }

}
