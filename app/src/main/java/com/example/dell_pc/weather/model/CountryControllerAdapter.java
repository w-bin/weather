package com.example.dell_pc.weather.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell_pc.weather.R;
import com.example.dell_pc.weather.util.LogUtil;
import com.example.dell_pc.weather.util.Utility;

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
        CountryControllerListViewItem item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.countryName = (TextView) view.findViewById(R.id.item_country_name);
            viewHolder.weatherDesp = (TextView) view.findViewById(R.id.item_weather_desp);
            viewHolder.temp = (TextView) view.findViewById(R.id.item_temp);
            viewHolder.publishTime = (TextView) view.findViewById(R.id.item_publish_time);
            viewHolder.updateTime = (TextView) view.findViewById(R.id.item_update_time);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.item_img);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.countryName.setText(item.getProvinceName() + "-" + item.getCityName() + "-" + item.getCountryName());
        viewHolder.weatherDesp.setText(item.getWeatherDesp());
        viewHolder.temp.setText(item.getTemp2() + " 至 " + item.getTemp1());
        viewHolder.publishTime.setText(item.getPublishTime() + "发布");
        viewHolder.updateTime.setText(item.getUpdateTime());
        String imgName;
        if(Utility.isDay()){        //判断是早上还是晚上，显示不同的图片
            imgName=item.getDayImg();
        }else{
            imgName=item.getNightImg();
        }
        LogUtil.e(CountryControllerAdapter.class + "", imgName.substring(0,imgName.length()-4) + " drawable " + getContext().getPackageName());
        viewHolder.imageView.setImageResource(
                getContext().getResources().getIdentifier(imgName.substring(0,imgName.length()-4), "drawable", getContext().getPackageName()));
        return view;
    }

    //局部更新数据
    public void updateView(View view, CountryControllerListViewItem item, int position) {
        if (view == null) {
            return;
        }
        ViewHolder viewHolder;
        if (view.getTag() != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            viewHolder = new ViewHolder();
            viewHolder.countryName = (TextView) view.findViewById(R.id.item_country_name);
            viewHolder.weatherDesp = (TextView) view.findViewById(R.id.item_weather_desp);
            viewHolder.temp = (TextView) view.findViewById(R.id.item_temp);
            viewHolder.publishTime = (TextView) view.findViewById(R.id.item_publish_time);
            viewHolder.updateTime = (TextView) view.findViewById(R.id.item_update_time);
            viewHolder.imageView=(ImageView)view.findViewById(R.id.item_img);
        }

        LogUtil.e(CountryControllerAdapter.class + "", item.getUpdateTime() + "!!!");
        viewHolder.countryName.setText(item.getProvinceName() + "-" + item.getCityName() + "-" + item.getCountryName());
        viewHolder.weatherDesp.setText(item.getWeatherDesp());
        viewHolder.temp.setText(item.getTemp2() + " 至 " + item.getTemp1());
        viewHolder.publishTime.setText(item.getPublishTime() + "发布");
        viewHolder.updateTime.setText(item.getUpdateTime());
        String imgName;
        if(Utility.isDay()){        //判断是早上还是晚上，显示不同的图片
            imgName=item.getDayImg();
        }else{
            imgName=item.getNightImg();
        }
        viewHolder.imageView.setImageResource(
                getContext().getResources().getIdentifier(imgName.substring(0, imgName.length() - 4), "drawable", getContext().getPackageName()));
    }

    class ViewHolder {
        TextView countryName;
        TextView weatherDesp;
        TextView temp;
        TextView publishTime;
        TextView updateTime;
        ImageView imageView;
    }
}
