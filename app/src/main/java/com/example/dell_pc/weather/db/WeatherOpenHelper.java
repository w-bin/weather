package com.example.dell_pc.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell-pc on 2016/3/7.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper {

    public static final String PROVINCE_NAME = "province_name";
    public static final String PROVINCE_CODE = "province_code";

    public static final String CITY_NAME = "city_name";
    public static final String CITY_CODE = "city_code";
    public static final String PROVINCE_ID = "province_id";

    public static final String COUNTRY_NAME = "country_name";
    public static final String COUNTRY_CODE = "country_code";
    public static final String CITY_ID = "city_id";

    private static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement, "
            + PROVINCE_NAME + " text, "
            + PROVINCE_CODE + " text)";

    private static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + CITY_NAME + " text, "
            + CITY_CODE + " text, "
            + PROVINCE_NAME + " text, "
            + PROVINCE_ID + " integer)";

    private static final String CREATE_COUNTRY = "create table Country ("
            + "id integer primary key autoincrement, "
            + COUNTRY_NAME + " text, "
            + COUNTRY_CODE + " text, "
            + CITY_NAME + " text, "
            + PROVINCE_NAME + " text, "
            + CITY_ID + " integer)";

    private static final String CREATE_COUNTRY_CONTROLLER = "create table CountryController ("
            + "id integer primary key autoincrement, "
            + "province_name text, "
            + "city_name text, "
            + "country_name text, "
            + "weather_code text, "
            + "temp1 text, "
            + "temp2 text, "
            + "weather_desp text, "
            + "publish_time text, "
            + "update_time text, "
            + "day_img text, "
            + "night_img text)";

    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);
        db.execSQL(CREATE_COUNTRY_CONTROLLER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL(CREATE_COUNTRY_CONTROLLER);
    }
}
