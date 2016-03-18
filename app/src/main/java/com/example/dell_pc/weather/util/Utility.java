package com.example.dell_pc.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.dell_pc.weather.model.City;
import com.example.dell_pc.weather.model.Country;
import com.example.dell_pc.weather.model.CountryControllerListViewItem;
import com.example.dell_pc.weather.model.Province;
import com.example.dell_pc.weather.model.WeatherDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by dell-pc on 2016/3/7.
 */
public class Utility {

    //解析和处理服务器返回的省级数据
    public synchronized static boolean handleProvinceResponse(WeatherDB weatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceName(array[1]);
                    province.setProvinceCode(array[0]);
                    weatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    //解析和处理服务器返回的市级数据
    public static boolean handleCitiesResponse(WeatherDB weatherDB, String response, Province province) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityName(array[1]);
                    city.setCityCode(array[0]);
                    city.setProvinceName(province.getProvinceName());
                    city.setProvinceId(province.getId());
                    weatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    //解析和处理服务器返回的县级数据
    public static boolean handleCountriesResponse(WeatherDB weatherDB, String response, City city) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountries = response.split(",");
            if (allCountries != null && allCountries.length > 0) {
                for (String c : allCountries) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountryName(array[1]);
                    country.setCountryCode(array[0]);
                    country.setCityName(city.getCityName());
                    country.setProvinceName(city.getProvinceName());
                    country.setCityId(city.getId());
                    weatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

    //解析服务器返回的JSON数据，并将解析出的数据存储到本地
    public static void handleWeatherResponse(Context context, String response, String provinceName, String cityName) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String countryName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, provinceName, cityName, countryName, weatherCode, temp1, temp2, weatherDesp, publishTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //将服务器返回的所有天气信息存储到SharedPreferences文件中
    public static void saveWeatherInfo(Context context, String provinceName, String cityName, String countryName,
                                       String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        if (!TextUtils.isEmpty(provinceName)) {     //不为空才进行覆盖
            editor.putString("province_name", provinceName);
            editor.putString("city_name", cityName);
        }
        editor.putString("country_name", countryName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", getDate());
        editor.commit();
    }

    public static CountryControllerListViewItem handlerUpdateWeatherResponse(WeatherDB weatherDB, String response,
                                                                             String provinceName, String cityName) {
        try {
            LogUtil.e(Utility.class + "", response);
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String countryName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            String updateTime = Utility.getDate();
            CountryControllerListViewItem item = new CountryControllerListViewItem(provinceName, cityName, countryName, weatherCode,
                    temp1, temp2, weatherDesp, publishTime, updateTime);
            LogUtil.e(Utility.class + "", "start return item");
            weatherDB.updateCountryWeather(item);
            LogUtil.e(Utility.class + "", "return item");
            return item;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(Utility.class + "", "return null");
        return null;
    }

    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return format.format(System.currentTimeMillis());
    }
}
