package com.example.dell_pc.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dell_pc.weather.activity.MainActivity;

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
            + PROVINCE_ID + " integer)";

    private static final String CREATE_COUNTRY = "create table Country ("
            + "id integer primary key autoincrement, "
            + COUNTRY_NAME + " text, "
            + COUNTRY_CODE + " text, "
            + CITY_ID + " integer)";


    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists my_weather");
        Log.e(MainActivity.class.toString(),"dorp!!!!!!!!!!!!");
        onCreate(db);
    }
}
