package com.timothy.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.timothy.Menu.CategoryVo;
import com.timothy.Cache.LruBitmapCache;
import com.timothy.Menu.ProductRepository;
import com.timothy.Menu.ProductVo;
import com.timothy.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuListActivity extends Activity implements View.OnClickListener {

    private ProductRepository productRepository = new ProductRepository();//做分類
    private Map<String, Integer> productCountMap = new HashMap<String, Integer>();
    private int totalPrice = 0,total=0;
    private TextView textViewPriceSum;
    private TextView textViewTotal;
    Button commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulayout);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ListViewAdapter());
        textViewPriceSum = (TextView) findViewById(R.id.textViewTotalPrice);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        commit = (Button)findViewById(R.id.button1);
        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this , MainActivity.class );
        startActivity(it);
    }

    private class ListViewAdapter extends BaseAdapter {

        private List<CategoryVo> categories = productRepository.getAllCategories();
        //取得全部商品
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.listitem_category, null);
                viewHolder = new ViewHolder();
                viewHolder.textViewVategoryTitle = (TextView) convertView.findViewById(R.id.textViewCategoryTitle);
                viewHolder.viewPagerProduct = (ViewPager) convertView.findViewById(R.id.productPager);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            CategoryVo categoryVo = categories.get(position);
            //拿第0區產品
            viewHolder.textViewVategoryTitle.setText(categoryVo.getName());
            //設置第0區名子
            ProductPagerAdapter adapter = new ProductPagerAdapter(categoryVo.getProductVos());
            //橫向傳入第0區商品資料
            viewHolder.viewPagerProduct.setAdapter(adapter);

            return convertView;
        }

        private class ViewHolder {

            TextView textViewVategoryTitle;
            ViewPager viewPagerProduct;
        }
    }

    private class ProductPagerAdapter extends PagerAdapter {

        private List<ProductVo> products;

        public ProductPagerAdapter(List<ProductVo> products) {
            this.products = products;
        }
        NetworkImageView image ;
        RequestQueue queue ;
        LruBitmapCache cache;
        ImageLoader ImageLoader;
        @Override
        public Object instantiateItem(ViewGroup container, int pagerPosition) {
            cache = new LruBitmapCache();
            queue = Volley.newRequestQueue(getApplicationContext());
            ImageLoader = new ImageLoader(queue , cache);
            View inflate = getLayoutInflater().inflate(R.layout.pageritem_container, null);
                LinearLayout pagerContainer = (LinearLayout) inflate.findViewById(R.id.pagerContainer);
                //1~3頁
                int start = 3 * pagerPosition + 0;// 012,345,678
                int end = 3 * pagerPosition + 2;
                int[] pagerItemProductViewIds = {R.id.pagerItem1, R.id.pagerItem2, R.id.pagerItem3};
                for (int i = start; i <= end; i++) {
                    View productView = pagerContainer.findViewById(pagerItemProductViewIds[i - 3 * pagerPosition]);
                    //取第一頁的pagerItem
                    ProductVo productVo = null;//try:取到有商品資訊   catch:如果沒有資訊,把該pagerItem隱藏起來,繼續做
                    try {
                        productVo = products.get(i);
                    } catch (Exception e) {
                        productView.setVisibility(View.INVISIBLE);
                        continue;
                    }

                    TextView textViewProductName = (TextView) productView.findViewById(R.id.textViewProductName);
                    textViewProductName.setText(productVo.getName());

                    TextView textViewProductPrice = (TextView) productView.findViewById(R.id.textViewProductPrice);
                    final int price = productVo.getPrice();
                    textViewProductPrice.setText(String.valueOf(price));

                    final TextView textViewProductCount = (TextView) productView.findViewById(R.id.textViewProductCount);
                    final String productId = productVo.getId();
                    Integer count = productCountMap.get(productId);
                if (count == null) {
                    count = 0;
                    productCountMap.put(productId, count);//每個產品編號(id)不同用key-value對應
                }
                textViewProductCount.setText(String.valueOf(count));

                image = (NetworkImageView) productView.findViewById(R.id.image);
                image.setImageUrl(productVo.getimage() , ImageLoader );
                Button btnAdd = (Button) productView.findViewById(R.id.btnAdd);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//第0頁第1個btn
                        Integer count = productCountMap.get(productId);//取數量
                        count = count + 1;
                        productCountMap.put(productId, count);//+1後塞回去
                        textViewProductCount.setText(String.valueOf(count));

                        totalPrice += price;//+價錢
                        textViewPriceSum.setText(String.valueOf(totalPrice));
                        total+=1;
                        textViewTotal.setText(String.valueOf(total));
                    }
                });

                Button btnDecrease = (Button) productView.findViewById(R.id.btnDecrease);
                btnDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer count = productCountMap.get(productId);
                        if (count.intValue() == 0) {
                            return;
                        }

                        count = count - 1;
                        productCountMap.put(productId, count);
                        textViewProductCount.setText(String.valueOf(count));

                        totalPrice -= price;
                        textViewPriceSum.setText(String.valueOf(totalPrice));
                        total-=1;
                        textViewTotal.setText(String.valueOf(total));
                    }
                });

            }


            container.addView(inflate);
            return inflate;
        }

        @Override
        public int getCount() {
            return products.size() / 3 + 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


}
