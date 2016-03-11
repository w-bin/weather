package com.example.dell_pc.weather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dell_pc.weather.db.WeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/3/7.
 */
public class WeatherDB {
    public static final String DB_NAME = "my_weather6";
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
            values.put(WeatherOpenHelper.PROVINCE_ID, city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    //从数据库读取某省下所有的城市信息
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if(cursor!=null&&cursor.moveToFirst()){
            do {
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.CITY_NAME)));
                city.setCityCode(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.CITY_CODE)));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex(WeatherOpenHelper.PROVINCE_ID)));
                list.add(city);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
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
            values.put(WeatherOpenHelper.CITY_ID, country.getCityId());
            db.insert("Country", null, values);
        }
    }

    //从数据库读取某市下所有的城市信息
    public List<Country> loadCountries(int cityId) {
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("Country", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if(cursor!=null&&cursor.moveToFirst()){
            do {
                Country country=new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.COUNTRY_NAME)));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex(WeatherOpenHelper.COUNTRY_CODE)));
                country.setCityId(cursor.getInt(cursor.getColumnIndex(WeatherOpenHelper.CITY_ID)));
                list.add(country);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return list;
    }
}
