package com.example.dell_pc.weather.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dell_pc.weather.R;

import java.util.List;

/**
 * Created by dell-pc on 2016/3/14.
 */
public class CountryControllerAdapter extends ArrayAdapter<CountryControllerListViewItem> {
    private int resourceId;

    public CountryControllerAdapter(Context context, int textViewResourceId, List<CountryControllerListViewItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CountryControllerListViewItem item=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView cityName=(TextView)view.findViewById(R.id.item_country_name);
        TextView temp1=(TextView)view.findViewById(R.id.item_temp1);
        TextView temp2=(TextView)view.findViewById(R.id.item_temp2);
        TextView weatherDesp=(TextView)view.findViewById(R.id.item_weather_desp);
        TextView publishTime=(TextView)view.findViewById(R.id.item_publish_time);

        cityName.setText(item.getCountryName());
        temp1.setText(item.getTemp1());
        temp2.setText(item.getTemp2());
        weatherDesp.setText(item.getWeatherDesp());
        publishTime.setText(item.getPublishTime());
        return view;
    }
}
