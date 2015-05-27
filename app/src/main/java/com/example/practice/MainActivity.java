package com.example.practice;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    String[] strA = {" PageNotification  ", " NetImage ", " MenuClass "};
    ListView LV;
    DrawerLayout DL;
    Artgine Artgine = new Artgine();
    Menu mMenu;
    ViewPager viewPager;

    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init();
        DL = (DrawerLayout) findViewById(R.id.DraOut);
        LV = (ListView) findViewById(R.id.LV);
        LV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, strA));
        LV.setOnItemClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);

    }
    class PagerAdapter extends FragmentPagerAdapter  implements PagerSlidingTabStrip.IconTabProvider {

        private final int[] icon = {R.drawable.cute, R.drawable.good, R.drawable.train};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new fragment_1();
            } else if (position == 1) {
                return new fragment_2();
            } else if (position == 2) {
                return new fragment_3();
            }
            return null;
        }

        @Override
        public int getCount() {
            return icon.length;
        }


        @Override
        public int getPageIconResId(int position) {
            return icon[position];
        }

    }

//    private void init() {
//        mRequestQueue = Volley.newRequestQueue(this);
//        fragment_1 fag = new fragment_1();
//        fragment_2 fag2 = new fragment_2();
//        fragment_3 fag3 = new fragment_3();
//        mFragments.add(fag);
//        mFragments.add(fag2);
//        mFragments.add(fag3);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == mMenu.getItem(0).getItemId()) {
            if (!DL.isDrawerOpen(Gravity.END)) {
                DL.openDrawer(Gravity.END);
            } else {
                DL.closeDrawer(Gravity.END);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        android.support.v4.app.FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.addToBackStack("HOME");
        Fragment Fra;
        Intent it;
        switch (position) {
            case 0:
                it = new Intent(this, PushNotificationActivity.class);
                startActivity(it);
                break;
            case 1:
                Fra = new NetImage();
                ft1.replace(R.id.DraOut, Fra).commit();
                break;
            case 2:
                it = new Intent(this, MenuListActivity.class);
                startActivity(it);
                break;
            default:
                it = new Intent(this, PushNotificationActivity.class);
                startActivity(it);
                break;
        }

    }

}
