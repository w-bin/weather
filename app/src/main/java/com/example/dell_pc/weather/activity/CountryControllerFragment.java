package com.example.dell_pc.weather.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell_pc.weather.R;
import com.example.dell_pc.weather.model.CountryControllerAdapter;
import com.example.dell_pc.weather.model.CountryControllerListViewItem;
import com.example.dell_pc.weather.model.WeatherDB;
import com.example.dell_pc.weather.util.LogUtil;
import com.example.dell_pc.weather.util.Utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dell-pc on 2016/3/15.
 */
public class CountryControllerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView listView;
    private CountryControllerAdapter adapter;
    private List<CountryControllerListViewItem> itemList = new ArrayList<CountryControllerListViewItem>();
    private FloatingActionButton button;
    private TextView titleText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WeatherDB weatherDB;

    private static final int REFRESHING = 1;    //正在刷新，没有全部刷新完成
    private static final int REFRESHED = 2;       //最后一个刷新，刷新结束
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESHING) {
                adapter.updateView(listView.getChildAt((int) msg.arg1), (CountryControllerListViewItem) msg.obj, (int) msg.arg1);
                LogUtil.e(CountryControllerFragment.class + "", (int) msg.arg1 + " update!!!!!!!!!!!!!");
            } else if (msg.what == REFRESHED) {
                adapter.updateView(listView.getChildAt((int) msg.arg1), (CountryControllerListViewItem) msg.obj, (int) msg.arg1);
                LogUtil.e(CountryControllerFragment.class + "", (int) msg.arg1 + " update!!!!!!!!!!!!!");
                swipeRefreshLayout.setRefreshing(false);
            }
            itemList.set(msg.arg1, (CountryControllerListViewItem) msg.obj);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_controller_layout, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        titleText = (TextView) getActivity().findViewById(R.id.title_text);
        MyBaseActivity.title = "城市管理";
        titleText.setText("城市管理");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.country_controller_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.blue);

        button = (FloatingActionButton) view.findViewById(R.id.country_controller_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("isFromCountryController", true);
                MainListFragment mainListFragment = new MainListFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, mainListFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        listView = (ListView) view.findViewById(R.id.country_controller_listView);
        weatherDB = MyBaseActivity.weatherDB;
        itemList = weatherDB.loadCountryControllerItem();
        adapter = new CountryControllerAdapter(getActivity(), R.layout.country_controller_listview_item, itemList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getActivity()).setTitle("请确认")
                        .setMessage("确认删除该信息？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                weatherDB.deleteCountryController(itemList.get(position).getCountryName());
                                itemList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryControllerListViewItem item = itemList.get(position);
                getActivity().getIntent().putExtra("country_code", "");
                getActivity().getIntent().putExtra("weather_code", item.getWeatherCode());
                getActivity().getIntent().putExtra("province_name", item.getProvinceName());
                getActivity().getIntent().putExtra("city_name", item.getCityName());
                WeatherShowFragment fragment = new WeatherShowFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public void onRefresh() {
        List<CountryControllerListViewItem> list = weatherDB.loadCountryControllerItem();
        LogUtil.e(CountryControllerFragment.class + "", itemList.size() + "");
        for (int i = 0; i < itemList.size(); i++) {
            query(i, itemList.get(i).getWeatherCode());
            LogUtil.e(CountryControllerFragment.class + "", i + "!!!");
        }
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    private void query(int position, String weatherCode) {
        final String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        final Message message = new Message();
        if (position == itemList.size() - 1) {
            message.what = REFRESHED;
        } else {
            message.what = REFRESHING;
        }
        message.arg1 = position;

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                LogUtil.e(CountryControllerFragment.class + "", "hello!");
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    // WeatherDB weatherDB=WeatherDB.getInstance(getActivity());
                    CountryControllerListViewItem item = itemList.get(message.arg1);
                    message.obj = Utility.handlerUpdateWeatherResponse(weatherDB, response.toString(),
                            item.getProvinceName(), item.getCityName());
                    LogUtil.e(CountryControllerFragment.class + "", "hello end");
                    handler.sendMessage(message);
                } catch (Exception e) {

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }
}
