package com.example.practice;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
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

public class MainActivity extends FragmentActivity implements OnPageChangeListener, AdapterView.OnItemClickListener {

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    String[] strA  = {"Page01 ","Page 02"};
    ListView LV;
    DrawerLayout DL;
    Artgine Artgine =new Artgine();
    Context cont;
    Menu mMenu;
    ViewPager myViewPager;
    ActionBar.Tab tab1, tab2, tab3;
    ActionBar actionBar;
    String[] Arrays;
    int[] icon={R.drawable.cute,R.drawable.good,R.drawable.train};
    String [] url=
            { "http://data.kaohsiung.gov.tw/Opendata/DownLoad.aspx?Type=2&CaseNo1=BA&CaseNo2=1&FileType=2&Lang=C&FolderType=O",
                    "http://data.kaohsiung.gov.tw/Opendata/DownLoad.aspx?Type=2&CaseNo1=AV&CaseNo2=1&FileType=1&Lang=C&FolderType",
                    "http://data.kaohsiung.gov.tw/Opendata/DownLoad.aspx?Type=2&CaseNo1=AF&CaseNo2=2&FileType=2&Lang=C&FolderType=O"
            };
    FragmentManager fm;
    private myadapter myadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        DL = (DrawerLayout) findViewById(R.id.DraOut);
        LV = (ListView)findViewById(R.id.LV);
        LV.setAdapter(new ArrayAdapter<String>(this , android.R.layout.simple_expandable_list_item_1 , strA));
        LV.setOnItemClickListener(this);
        init();
        myViewPager=(ViewPager)findViewById(R.id.viewpager);
        try
        {
            Arrays=new mytask().execute(url).get();

        }
        catch (InterruptedException e){e.printStackTrace();}
        catch ( ExecutionException  e){e.printStackTrace();}


        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);//遮蔽標題

        tab1 = actionBar.newTab().setText("CUTE").setIcon(icon[0]).setTabListener(ontab);
        tab2 = actionBar.newTab().setText("QOO").setIcon(icon[1]).setTabListener(ontab);
        tab3 = actionBar.newTab().setText("TRAIN").setIcon(icon[2]).setTabListener(ontab);


        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);


        fm =getSupportFragmentManager();
        myadapter=new myadapter(fm,Arrays,mFragments);
        myViewPager.setAdapter(myadapter);
        myViewPager.setOnPageChangeListener(this);
    }

    private void init() {
        fragment_1 fag=new fragment_1();
        fragment_2 fag2=new fragment_2();
        fragment_3 fag3=new fragment_3();
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == mMenu.getItem(0).getItemId()){
            if(!DL.isDrawerOpen(Gravity.END))
                {DL.openDrawer(Gravity.END);}
            else
                {DL.closeDrawer(Gravity.END);}

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
        switch(position)
        {
            case 0:
                android.support.v4.app.FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
                ft0.addToBackStack("HOME");
                ft0.replace(R.id.DraOut , Artgine ).commit() ;
                break;
            case 1:
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("BACK");

                ft.replace(R.id.DraOut , Artgine ).commit() ;
            break;
        }
    }
    /**
	 * 警告：一定不能在这三个回调中为fragment转换调用commit()——系统会自动为你调用，
	 * 如果你自己再调可能会抛	出异常，另外，也不能将这些fragment转换加入back stack。
	 */

    private ActionBar.TabListener ontab = new  ActionBar.TabListener()
    {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            if (myViewPager != null)
            {
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
    class myadapter  extends FragmentPagerAdapter
    {	List<Fragment> frag;
        String json[];
        public myadapter(FragmentManager fm,String array[],List<Fragment> lv) {
            super(fm);
            json=array;
            frag=lv;
        }

        @Override
        public Fragment getItem(int i) {
            Log.d("uni", "get item is call   "+ i);
            Bundle bundle=new Bundle();
            //Fragment fragment;
            switch (i)
            {
                case 0 :
                    frag.get(i);
                    bundle.putString("json",json[i]);
                    frag.get(i).setArguments(bundle);
                    return  frag.get(i);
                case 1 :
                    frag.get(i);
                    bundle.putString("json",json[i]);
                    frag.get(i).setArguments(bundle);
                    return  frag.get(i);
                case 2 :
                    frag.get(i);
                    bundle.putString("json",json[i]);
                    frag.get(i).setArguments(bundle);
                    return  frag.get(i);
                default:
                    return new fragment_1();
            }
        }

        @Override
        public int getCount() {//總�?�數
            Log.d("uni", "get count is call");
            return 3;
        }

    }
    class mytask extends AsyncTask<String, Void, String[]>
    {
        HttpClient httpClient =new DefaultHttpClient();

        String[] result;
        protected String[] doInBackground(String... params)
        {	 result=new String[params.length];
            try
            {
                for(int i=0;i<params.length;i++)
                {
                    HttpGet get = new HttpGet(params[i]);

                    HttpResponse response = httpClient.execute(get);

                    HttpEntity resEntity=response.getEntity();

                    result[i] = EntityUtils.toString(resEntity,"utf-8");

                    Log.e("result",result[i]);
                }

            } catch (Exception e)
            {
                return null;
            }
            return result;
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
