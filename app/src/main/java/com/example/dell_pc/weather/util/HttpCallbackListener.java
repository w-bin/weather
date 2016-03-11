package com.example.dell_pc.weather.util;

/**
 * Created by dell-pc on 2016/3/7.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
