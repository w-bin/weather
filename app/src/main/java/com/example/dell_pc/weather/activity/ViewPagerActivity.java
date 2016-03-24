package com.example.dell_pc.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dell_pc.weather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbin on 2016/3/24.
 */
public class ViewPagerActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View view1, view2, view3, view4;
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList;//view数组
    private ImageView[] points;  //底部point数组
    private int currentIndex;   //当前页面
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_layout);

        backButton = (Button) findViewById(R.id.introduction_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPagerActivity.this, MyBaseActivity.class);
                startActivity(intent);
                finish();
            }
        });
        backButton.setVisibility(View.GONE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout_1, null);
        view2 = inflater.inflate(R.layout.layout_2, null);
        view3 = inflater.inflate(R.layout.layout_3, null);
        view4 = inflater.inflate(R.layout.layout_4, null);

        viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewPager.setAdapter(getPagerAdapter());
        viewPager.setOnPageChangeListener(this);
        initPoint();
    }

    //初始化底部的点
    private void initPoint() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.viewpager_ll);
        points = new ImageView[viewList.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = (ImageView) linearLayout.getChildAt(i);
            points[i].setEnabled(true);
            points[i].setOnClickListener(this);
            points[i].setTag(i);
        }
        currentIndex = 0;
        points[currentIndex].setEnabled(false);
    }

    private PagerAdapter getPagerAdapter() {
        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        return pagerAdapter;
    }


    //点击底部的点触发
    @Override
    public void onClick(View v) {
        if ((int) v.getTag() < 0 || (int) v.getTag() >= points.length) {
            return;
        }
        int position = (int) v.getTag();
        setCurrentView(position);
        setCurrentPoint(position);
    }

    //设置当前页面位置
    private void setCurrentView(int position) {
        if (position < 0 || position >= viewList.size()) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    //设置当前小点位置
    private void setCurrentPoint(int position) {
        if (position < 0 || position >= points.length || position == currentIndex) {
            return;
        }
        points[currentIndex].setEnabled(true);
        points[position].setEnabled(false);
        currentIndex = position;

        //返回按钮显示与否
        if (currentIndex == viewList.size() - 1) {
            backButton.setVisibility(View.VISIBLE);
        } else {
            backButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    //新的界面被选中时调用
    @Override
    public void onPageSelected(int position) {
        setCurrentPoint(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}