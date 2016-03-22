package com.example.dell_pc.weather.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.weather.R;
import com.example.dell_pc.weather.model.WeatherDB;
import com.example.dell_pc.weather.util.ImgUtil;
import com.example.dell_pc.weather.util.LogUtil;

/**
 * Created by dell_pc on 2016/3/11.
 */
public class MyBaseActivity extends ActionBarActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    static WeatherDB weatherDB;
    private TextView titleText;
    public static String title = "天气APP";

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base_activity);
        weatherDB = WeatherDB.getInstance(this);
        initView();
    }

    private void initView() {
        titleText = (TextView) findViewById(R.id.title_text);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();    //调用onPrepareOptionsMenu
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();    //调用onPrepareOptionsMenu
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView.setItemIconTintList(null);   //设置菜单图标恢复本来的颜色
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.item_1:
                        Test0Fragment fragment = new Test0Fragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        break;
                    case R.id.item_2:
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyBaseActivity.this).edit();
                        editor.putBoolean("city_selected", false);
                        editor.commit();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyBaseActivity.this);
                        MainListFragment mainListFragment = new MainListFragment();
                        FragmentManager fragmentManager1 = getFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.content_frame, mainListFragment).commit();
                        break;
                    case R.id.item_3:
                        WeatherShowFragment weatherShowFragment = new WeatherShowFragment();
                        FragmentManager fragmentManager2 = getFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.content_frame, weatherShowFragment).commit();
                        break;
                    case R.id.item_0:
                        CountryControllerFragment countryControllerFragment = new CountryControllerFragment();
                        FragmentManager fragmentManager3 = getFragmentManager();
                        fragmentManager3.beginTransaction().replace(R.id.content_frame, countryControllerFragment).commit();
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        View view=navigationView.getHeaderView(0);
        ImageView headerImg= (ImageView) view.findViewById(R.id.navigation_header_img);
        String imgPath=PreferenceManager.getDefaultSharedPreferences(this).getString("personal_img","");
        if(!TextUtils.isEmpty(imgPath)){
            LogUtil.e(MyBaseActivity.class+"","set head img!!");
            headerImg.setImageBitmap(ImgUtil.getImage(imgPath));
        }
        TextView personalName= (TextView) view.findViewById(R.id.navigation_header_name);
        personalName.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("personal_name","无"));
        headerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBaseActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpen = drawerLayout.isDrawerOpen(navigationView);
        LogUtil.e(MyBaseActivity.class + "", isDrawerOpen + "");
        if (isDrawerOpen) {
            title = titleText.getText().toString();
            titleText.setText("请选择");
        } else {
            titleText.setText(title);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast.makeText(MyBaseActivity.this, id + "", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                finish();
            }
        }
        return false;
    }
}
