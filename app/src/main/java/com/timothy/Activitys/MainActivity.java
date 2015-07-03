package com.timothy.Activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.astuetz.PagerSlidingTabStrip;
import com.timothy.Adapter.DrawerAdapter;
import com.timothy.Fragments.MenuListFragment;
import com.timothy.Fragments.fragment_2;
import com.timothy.Fragments.fragment_3;
import com.timothy.Fragments.NetImage;
import com.timothy.Fragments.PushNotificationFragment;
import com.timothy.R;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView LV;
    DrawerLayout DL;
    Menu mMenu;
    ViewPager viewPager;
    Fragment Fra = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DL = (DrawerLayout) findViewById(R.id.DraOut);
        LV = (ListView) findViewById(R.id.LV);
        LV.setAdapter(new DrawerAdapter(this, R.layout.drawerlist , getList()));
        LV.setOnItemClickListener(this);
        DL.setDrawerShadow(R.drawable.drashadow, GravityCompat.END );
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);

    }

    private List<DrawerAdapter.DrawerItem> getList() {
        List<DrawerAdapter.DrawerItem> list = new ArrayList<>();
        String[] DraName= { " NetImage " , "HistoryActivity Page" , "Order Page" };
        int[] DraIcon = {R.drawable.testicon , R.drawable.menuicon ,R.drawable.testicon};
        for (int position = 0; position < DraName.length; position++) {
            list.add(new DrawerAdapter.DrawerItem( DraName[position] , DraIcon[position]));
        }
        return list;
    }

    class PagerAdapter extends FragmentPagerAdapter  implements PagerSlidingTabStrip.IconTabProvider {

        private final int[] icon = {R.drawable.actionbartab_1, R.drawable.actionbartab_2, R.drawable.actionbartab3 , R.drawable.actionbar_pushicon };

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MenuListFragment();
            } else if (position == 1) {
                return new fragment_2();
            } else if (position == 2) {
                return new fragment_3();
            }
            return new PushNotificationFragment();
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

        switch (position) {

            case 0:
                Fra = new NetImage();
                ft1.replace(R.id.DraOut , Fra).commit();
                break;
            case 1:
                Intent it = new Intent(this , HistoryActivity.class);
                startActivity(it);
                break;
            case 2:
                Intent intentNextAction = new Intent(this, OrderActivity.class);
                intentNextAction.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intentNextAction.putExtra("Content", "message");
                startActivity(intentNextAction);
                break;
            default:
                break;
        }
        DL.closeDrawer(Gravity.END);
    }
}