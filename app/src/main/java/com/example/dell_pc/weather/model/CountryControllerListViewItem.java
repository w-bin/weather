package com.example.dell_pc.weather.model;

/**
 * Created by dell-pc on 2016/3/14.
 */
public class CountryControllerListViewItem {

    private String provinceName;
    private String cityName;
    private String countryName;
    private String weatherCode;
    private String temp1;
    private String temp2;
    private String weatherDesp;
    private String publishTime;
    private String updateTime;

    public CountryControllerListViewItem() {
    }

    public CountryControllerListViewItem(String provinceName, String cityName, String countryName, String weatherCode,
                                         String temp1, String temp2, String weatherDesp, String publishTime, String updateTime) {
        this.provinceName=provinceName;
        this.cityName=cityName;
        this.countryName = countryName;
        this.weatherCode = weatherCode;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.weatherDesp = weatherDesp;
        this.publishTime = publishTime;
        this.updateTime = updateTime;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public String getWeatherDesp() {
        return weatherDesp;
    }

    public void setWeatherDesp(String weatherDesp) {
        this.weatherDesp = weatherDesp;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
