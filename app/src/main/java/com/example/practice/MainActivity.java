package com.example.practice;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] strA = { " NetImage ", " MenuClass "};
    ListView LV;
    DrawerLayout DL;
    Artgine Artgine = new Artgine();
    Menu mMenu;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        private final int[] icon = {R.drawable.actionbartab_1, R.drawable.actionbartab_2, R.drawable.actionbartab3 , R.drawable.actionbar_pushicon };

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
            }else
            return new PushNotificationActivity();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main , menu);
        mMenu = menu;
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
                Fra = new NetImage();
                ft1.replace(R.id.DraOut, Fra).commit();
                break;
            case 1:
                it = new Intent(this, MenuListActivity.class);
                startActivity(it);
                break;
            default:
                break;
        }
    }
}
