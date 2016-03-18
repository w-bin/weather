package com.example.dell_pc.weather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dell_pc.weather.db.WeatherOpenHelper;
import com.example.dell_pc.weather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/3/7.
 */
public class WeatherDB {
    public static final String DB_NAME = "my_weather10";
    public static final int VERSION = 1;
    private static WeatherDB weatherDB;
    private SQLiteDatabase db;

    //将构造方法私有化
    private WeatherDB(Context context) {
        WeatherOpenHelper dbHelper = new WeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    //获取weatherDB的实例
    public synchronized static WeatherDB getInstance(Context context) {
        if (weatherDB == null) {
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }

    //将Province实例存储到数据库
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put(WeatherOpenHelper.PROVINCE_NAME, province.getProvinceName());
            values.put(WeatherOpenHelper.PROVINCE_CODE, province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    //从数据库读取全国所有的省份信息
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.PROVINCE_NAME)));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.PROVINCE_CODE)));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    //将City实例存储到数据库
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put(WeatherOpenHelper.CITY_NAME, city.getCityName());
            values.put(WeatherOpenHelper.CITY_CODE, city.getCityCode());
            values.put(WeatherOpenHelper.PROVINCE_NAME, city.getProvinceName());
            values.put(WeatherOpenHelper.PROVINCE_ID, city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    //从数据库读取某省下所有的城市信息
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.CITY_NAME)));
                city.setCityCode(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.CITY_CODE)));
                city.setProvinceName(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.PROVINCE_NAME)));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex(WeatherOpenHelper.PROVINCE_ID)));
                list.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    //将Country实例存储到数据库
    public void saveCountry(Country country) {
        if (country != null) {
            ContentValues values = new ContentValues();
            values.put(WeatherOpenHelper.COUNTRY_NAME, country.getCountryName());
            values.put(WeatherOpenHelper.COUNTRY_CODE, country.getCountryCode());
            values.put(WeatherOpenHelper.CITY_NAME, country.getCityName());
            values.put(WeatherOpenHelper.PROVINCE_NAME, country.getProvinceName());
            values.put(WeatherOpenHelper.CITY_ID, country.getCityId());
            db.insert("Country", null, values);
        }
    }

    //从数据库读取某市下所有的城市信息
    public List<Country> loadCountries(int cityId) {
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("Country", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.COUNTRY_NAME)));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.COUNTRY_CODE)));
                country.setCityName(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.CITY_NAME)));
                country.setProvinceName(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.PROVINCE_NAME)));
                country.setCityId(cursor.getInt(cursor.getColumnIndex(WeatherOpenHelper.CITY_ID)));
                list.add(country);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    //将某country的天气状况存储到数据库
    public void saveCountryController(CountryControllerListViewItem item) {
        Cursor cursor = db.query("CountryController", null, "country_name=?", new String[]{item.getCountryName()}, null, null, null);
        if (cursor.moveToFirst()) return;    //数据库已经存在过的就不再存储到数据库
        if (item != null) {
            ContentValues values = new ContentValues();
            values.put("province_name",item.getProvinceName());
            values.put("city_name",item.getCityName());
            values.put("country_name", item.getCountryName());
            values.put("weather_code", item.getWeatherCode());
            values.put("temp1", item.getTemp1());
            values.put("temp2", item.getTemp2());
            values.put("weather_desp", item.getWeatherDesp());
            values.put("publish_time", item.getPublishTime());
            values.put("update_time", Utility.getDate());
            db.insert("CountryController", null, values);
        }
    }

    //将某country的天气状况从数据库中删除
    public void deleteCountryController(String countryName) {
        db.delete("CountryController", "country_name=?", new String[]{countryName});
    }

    //从数据库读取选择过的countrise天气情况
    public List<CountryControllerListViewItem> loadCountryControllerItem() {
        List<CountryControllerListViewItem> list = new ArrayList<CountryControllerListViewItem>();
        Cursor cursor = db.query("CountryController", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CountryControllerListViewItem item = new CountryControllerListViewItem();
                item.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                item.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                item.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                item.setWeatherCode(cursor.getString(cursor.getColumnIndex("weather_code")));
                item.setTemp1(cursor.getString(cursor.getColumnIndex("temp1")));
                item.setTemp2(cursor.getString(cursor.getColumnIndex("temp2")));
                item.setWeatherDesp(cursor.getString(cursor.getColumnIndex("weather_desp")));
                item.setPublishTime(cursor.getString(cursor.getColumnIndex("publish_time")));
                item.setUpdateTime(cursor.getString(cursor.getColumnIndex("update_time")));
                list.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    //更新CountryController表里某个数据
    public void updateCountryWeather(CountryControllerListViewItem item) {
        ContentValues values = new ContentValues();
        values.put("temp1", item.getTemp1());
        values.put("temp2", item.getTemp2());
        values.put("weather_desp", item.getWeatherDesp());
        values.put("publish_time", item.getPublishTime());
        values.put("update_time", Utility.getDate());
        db.update("CountryController", values, "country_name=?", new String[]{item.getCountryName()});
    }
}
