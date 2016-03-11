package com.example.dell_pc.weather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.dell_pc.weather.R;

/**
 * Created by dell-pc on 2016/3/11.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.test_layout);
    }
}
