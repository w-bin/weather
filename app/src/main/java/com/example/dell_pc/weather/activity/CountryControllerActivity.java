package com.example.dell_pc.weather.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell_pc.weather.R;
import com.example.dell_pc.weather.model.CountryControllerAdapter;
import com.example.dell_pc.weather.model.CountryControllerListViewItem;
import com.example.dell_pc.weather.model.WeatherDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/3/15.
 */
public class CountryControllerActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ListView listView;
    private CountryControllerAdapter adapter;
    private List<CountryControllerListViewItem> itemList = new ArrayList<CountryControllerListViewItem>();
    private Button button;
    private TextView titleText;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int REFRESH=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==REFRESH){
                swipeRefreshLayout.setRefreshing(false);
            }
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
        titleText.setText("城市管理");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.country_controller_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.blue);

        button = (Button) view.findViewById(R.id.country_controller_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("isFromCountryController", true);
                MainActivityCopy mainActivityCopy = new MainActivityCopy();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mainActivityCopy).commit();
            }
        });

        listView = (ListView) view.findViewById(R.id.country_controller_listView);
        WeatherDB weatherDB = WeatherDB.getInstance(getActivity());
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
                                WeatherDB db = WeatherDB.getInstance(getActivity());
                                db.deleteCountryController(itemList.get(position).getCountryName());
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
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(REFRESH,2000);
    }
}
