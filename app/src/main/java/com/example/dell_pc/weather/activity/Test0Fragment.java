package com.example.dell_pc.weather.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dell_pc.weather.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dell-pc on 2016/3/12.
 */
public class Test0Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> list=new ArrayList<String>(Arrays.asList("Java", "Javascript", "C++", "Ruby", "Json",
            "HTML"));
    private android.os.Handler handler=new android.os.Handler(){
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    list.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;

            }
        };
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.test0_layout,container,false);
        init(view);
        return view;
    }

    public void init(View view){

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.white,R.color.blue,R.color.green,R.color.black);

        listView=(ListView)view.findViewById(R.id.listView);
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }

}
