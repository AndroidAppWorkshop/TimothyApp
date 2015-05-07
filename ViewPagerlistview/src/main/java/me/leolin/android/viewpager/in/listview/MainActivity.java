package me.leolin.android.viewpager.in.listview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ListViewAdapter());//自訂listview
    }

    private class ListViewAdapter extends BaseAdapter {

        private String[] data = {//上排
                "飲料區", "甜點區", "主餐區",
        };

        private String[][] subDatas = {//每一排
                {"咖啡", "珍珠奶茶", "紅茶"},
                {"蛋糕", "奶酪", "布丁"},
                {"豬排", "羊排", "牛排"},
        };

    int[][] icon= {
                   {R.mipmap.coffee, R.mipmap.milketra, R.mipmap.redtea},
                   {R.mipmap.cake,R.mipmap.pi,R.mipmap.pudding},
                   {R.mipmap.pig,R.mipmap.sheep,R.mipmap.steak}
                  };

        @Override
        public int getCount() {
            return data.length;
        }//取得到底有多少列的方法

        @Override
        public Object getItem(int position) {
            return data[position];
        }//取得某一列的內容

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {//修改某一列View的內容
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listitem_sample, null);
                viewHolder = new ViewHolder();//去取屬性(每列的)
                viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);//初始化取回來元件
                viewHolder.viewPager = (ViewPager) convertView.findViewById(R.id.viewPager);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textViewTitle.setText(data[position]);
//-----------------------------------------(自訂listview)-------------------------------------------------------------
            viewHolder.viewPager.setAdapter(new PagerAdapter() {//對viewpager元件做設置
                @Override
                public Object instantiateItem(ViewGroup container, int pagerPosition) {
                    //將要顯示的圖片或其他元件放到這,進行緩存,將要顯示的元件初始化加入viewgroup然後返回
                    View inflate = getLayoutInflater().inflate(R.layout.pageritem_sample, null);//取得這個layout
                    TextView textView = (TextView) inflate.findViewById(R.id.textView);
                    ImageView imageView=(ImageView)inflate.findViewById(R.id.imageView);
                    textView.setText(subDatas[position][pagerPosition]);
                    imageView.setBackgroundResource(icon[position][pagerPosition]);
                    container.addView(inflate);
                    return inflate;
                }

                @Override//獲取滑動元件的數量
                public int getCount() {
                    return subDatas[position].length;
                }

                @Override//判斷是否顯示同一張圖片
                public boolean isViewFromObject(View view, Object o) {
                    return view.equals(o);
                }

                @Override//只緩存3張圖片,滑動超出緩存範圍則調用將圖片銷毀
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            });

            return convertView;
        }
//-----------------------------------------(滑動viewpager)-------------------------------------------------------------

        private class ViewHolder {//宣告成一個類別讓該列的set屬性去讀取
            TextView textViewTitle;
            ViewPager viewPager;
        }
    }


}
