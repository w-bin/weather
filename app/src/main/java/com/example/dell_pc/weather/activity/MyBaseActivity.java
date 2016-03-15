package com.example.dell_pc.weather.activity;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dell_pc.weather.R;
import com.example.dell_pc.weather.util.LogUtil;

/**
 * Created by dell-pc on 2016/3/11.
 */
public class MyBaseActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base_activity);
        initView();

       // toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.toolbar_menu);   //设置右上角的填充菜单

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
                        MainActivityCopy mainActivityCopy = new MainActivityCopy();
                        FragmentManager fragmentManager1 = getFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.content_frame, mainActivityCopy).commit();
                        break;
                    case R.id.item_3:
                        WeatherShowActivityCopy weatherShowActivityCopy = new WeatherShowActivityCopy();
                        FragmentManager fragmentManager2 = getFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.content_frame, weatherShowActivityCopy).commit();
                        break;
                    case R.id.item_0:
                        CountryControllerActivity countryControllerActivity =new CountryControllerActivity();
                        FragmentManager fragmentManager3=getFragmentManager();
                        fragmentManager3.beginTransaction().replace(R.id.content_frame, countryControllerActivity).commit();
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
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
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpen = drawerLayout.isDrawerOpen(navigationView);
        LogUtil.e(MyBaseActivity.class + "", isDrawerOpen + "");
        //menu.findItem(R.id.action_notification).setVisible(!isDrawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
}
