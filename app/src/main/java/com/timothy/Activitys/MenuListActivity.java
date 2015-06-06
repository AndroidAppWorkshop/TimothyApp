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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.timothy.Menu.Cart;
import com.timothy.Menu.CartActivity;
import com.timothy.Menu.CategoryVo;
import com.timothy.Cache.LruBitmapCache;

import com.timothy.Menu.ProductRepository;
import com.timothy.Menu.ProductVo;
import com.timothy.R;
import library.timothy.Resources.UriResources;

import org.json.JSONArray;

import java.util.List;


public class MenuListActivity extends Activity implements View.OnClickListener {

    private ProductRepository productRepository = new ProductRepository();//做分類
    private final Cart cart =new Cart();//new 購物車
    private int total=0;
    private TextView textViewPriceSum;
    private TextView textViewTotal;
    private ProgressBar progressBar;
    private Button commit;
    private ListView listView;
    RequestQueue mQueue;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulayout);
        listView = (ListView) findViewById(R.id.listview);
        textViewPriceSum = (TextView) findViewById(R.id.textViewTotalPrice);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        commit = (Button)findViewById(R.id.button1);
        commit.setOnClickListener(this);
        mQueue = Volley.newRequestQueue(this);

        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest request =new JsonArrayRequest(UriResources.Server.Product,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                progressBar.setVisibility(View.GONE);
                productRepository.refreshdata(jsonArray);
                renderlistview();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MenuListActivity.this,volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

         mQueue.add(request);
    }

    private void renderlistview() {
        listView.setAdapter(new ListViewAdapter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cart.getProductInCart().clear();
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this , CartActivity.class );
        it.putExtra("Data",cart);
        startActivity(it);
    }

    private class ListViewAdapter extends BaseAdapter {

        private List<CategoryVo> categories = productRepository.getAllCategories();

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

            viewHolder.textViewVategoryTitle.setText(categoryVo.getName());

            ProductPagerAdapter adapter = new ProductPagerAdapter(categoryVo.getProductVos());

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

                int start = 3 * pagerPosition + 0;// 012,345,678
                int end = 3 * pagerPosition + 2;
                int[] pagerItemProductViewIds = {R.id.pagerItem1, R.id.pagerItem2, R.id.pagerItem3};
                for (int i = start; i <= end; i++) {
                    View productView = pagerContainer.findViewById(pagerItemProductViewIds[i - 3 * pagerPosition]);

                    ProductVo productVo = null;
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


                textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(productId)));

//                image = (NetworkImageView) productView.findViewById(R.id.image);
//                image.setImageUrl(productVo.getimage() , ImageLoader );

                Button btnAdd = (Button) productView.findViewById(R.id.btnAdd);

                  btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cart.addToCart(productId, 1);
                            textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(productId)));
                            textViewPriceSum.setText(String.valueOf(cart.calculateSumPrice()));
                            textViewTotal.setText(String.valueOf(cart.calculateSumCount()));
                        }
                    });

                    Button btnDecrease = (Button) productView.findViewById(R.id.btnDecrease);
                    btnDecrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            cart.addToCart(productId, -1);
                            textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(productId)));
                            textViewPriceSum.setText(String.valueOf(cart.calculateSumPrice()));
                            textViewTotal.setText(String.valueOf(cart.calculateSumCount()));
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
