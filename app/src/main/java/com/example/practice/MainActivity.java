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

public class MainActivity extends FragmentActivity implements OnPageChangeListener, AdapterView.OnItemClickListener {

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    String[] strA = {" PageNotification  " , "  Menu List " };
    ListView LV;
    DrawerLayout DL;
    Artgine Artgine = new Artgine();
    Menu mMenu;
    ViewPager myViewPager;
    ActionBar actionBar;
    int[] icon = {R.drawable.cute, R.drawable.good, R.drawable.train};
    String[] TABName = {"CUTE", "QOO", "TRAIN"};
    FragmentManager fm;
    private myadapter myadapter;
    RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        DL = (DrawerLayout) findViewById(R.id.DraOut);
        LV = (ListView) findViewById(R.id.LV);
        LV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, strA));
        LV.setOnItemClickListener(this);
        myViewPager = (ViewPager) findViewById(R.id.viewpager);

        //Arrays = new ServerRequest().execute(url).get();
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);//遮蔽標題

        for (int i = 0; i < 3; i++) {
            actionBar.addTab(actionBar.newTab().setText(TABName[i]).setIcon(icon[i]).setTabListener(ontab));
        }

        fm = getSupportFragmentManager();
        myadapter = new myadapter(fm, mFragments);
        myViewPager.setAdapter(myadapter);
        myViewPager.setOnPageChangeListener(this);
    }

    private void init() {
        mRequestQueue = Volley.newRequestQueue(this);
        fragment_1 fag = new fragment_1();
        fragment_2 fag2 = new fragment_2();
        fragment_3 fag3 = new fragment_3();
        mFragments.add(fag);
        mFragments.add(fag2);
        mFragments.add(fag3);
    }

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
//--------------------viewpager監聽滑動(頁)事件--------------------------------------

    @Override
    public void onPageSelected(int arg0) {
        actionBar.setSelectedNavigationItem(arg0);

    }
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    //---------------------Tab 的監聽器-----------------------------------------------------
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        android.support.v4.app.FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.addToBackStack("HOME");
        Fragment Fra;
        Intent it;
        switch (position) {

            case 1 :
                it = new Intent(this , MenuListActivity.class );
                startActivity(it);
                break;
            case 0:
                it = new Intent(this , PushNotificationActivity.class);
                startActivity(it);
                break;
            default:
                it = new Intent(this , PushNotificationActivity.class);
                startActivity(it);
                break;
        }

    }

    /**
     * 警告：一定不能在这三个回调中为fragment转换调用commit()——系统会自动为你调用，
     * 如果你自己再调可能会抛	出异常，另外，也不能将这些fragment转换加入back stack。
     */

    private ActionBar.TabListener ontab = new ActionBar.TabListener() {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            if (myViewPager != null) {
                myViewPager.setCurrentItem(tab.getPosition());
            }
            tab.getIcon().setAlpha(255);
        }
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            tab.getIcon().setAlpha(100);
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

    };

    //用	FragmentPagerAdapter 來裝其他的fragment
    class myadapter extends FragmentPagerAdapter {
        List<Fragment> frag;
        Fragment Fra;
        String json[];

        public myadapter(FragmentManager fm, List<Fragment> lv) {
            super(fm);
            frag = lv;
        }

        @Override
        public Fragment getItem(int i) {
            Log.d("uni", "get item is call   " + i);

            switch (i) {
                default:
                    Fragment Fra = frag.get(i);
                    return Fra;
            }
        }

        @Override
        public int getCount() {//總�?�數
            Log.d("uni", "get count is call");
            return 3;
        }
    }
}
/**
 * 	1.FragmentPagerAdapter他是舊版的用法所以你所有的fragment都要用android.support.v4.app.Fragment;
 * 	 	 不可以用新版的android.app.Fragment;
 * 	2.步驟創ViewPager的setAdapter去裝FragmentPagerAdapter底下的fragment
 * 	3.Tab 的監聽器 與 viewpager監聽滑動  要互相監聽這樣才可以達到點選tab時會呼叫viewpager換頁(相反以此類推)
 * 	4.Tab 監聽中的onTabSelected 記得ViewPager你要先創建初始化findViewById(R.id.viewpager)
 * 		不然他在addtab的時候會發現viewpager 是null 會造成錯誤,因為set監聽器他會跑進去結果就會跑到myViewPager此時還沒有viewpager所以爆掉
 * 	5.記得去開網路權限 * 	
 */
