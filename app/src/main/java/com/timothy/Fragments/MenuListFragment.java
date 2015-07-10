package com.timothy.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.timothy.Activitys.CartActivity;
import com.timothy.Cache.LruBitmapCache;
import com.timothy.Core.BaseApplication;
import com.timothy.R;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import library.timothy.Resources.Name;
import library.timothy.Resources.UriResources;
import library.timothy.Shopping.Cart;
import library.timothy.Shopping.Category;
import library.timothy.Shopping.Combo;
import library.timothy.Shopping.ComboDetail;
import library.timothy.Shopping.ComboRepository;
import library.timothy.Shopping.Product;
import library.timothy.Shopping.ProductRepository;


public class MenuListFragment extends Fragment implements View.OnClickListener {

    private ProductRepository productRepository = new ProductRepository();
    private Cart cart = new Cart();
    private TextView textViewPriceSum;
    private TextView textViewTotal;
    private ProgressBar progressBar;
    private Button commit;
    private ListView listView;
    private SharedPreferences sharedPreferences;
    private String apiKey;
    private ListViewAdapter listViewAdapter;
    View view;
    private   ViewPager viewPagerCombo;
    private ComboPagerAdapter comboPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.menulayout, null);
        return  view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewPagerCombo=(ViewPager)view.findViewById(R.id.comboPager);

        listView = (ListView) view.findViewById(R.id.listview);
        textViewPriceSum = (TextView) view.findViewById(R.id.textViewTotalPrice);
        textViewTotal = (TextView) view.findViewById(R.id.textViewTotal);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        commit = (Button) view.findViewById(R.id.button1);
        commit.setOnClickListener(this);

        progressBar.setVisibility(View.VISIBLE);

        sharedPreferences = this.getActivity().getSharedPreferences(Name.Key.Apikey, Context.MODE_PRIVATE);

        apiKey = sharedPreferences.getString(Name.Key.Apikey, null);

        loadProducts();
    }

    private void loadProducts() {
        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.GET,
                        UriResources.Server.Product,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                progressBar.setVisibility(View.GONE);
                                productRepository.refreshdata(jsonArray);
                                loadCombo();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
//                       headers.put("Accept", "application/json");
                        headers.put(Name.Key.Apikey, apiKey);
                        return headers;
                    }
                });
    }

    private void loadCombo() {
        progressBar.setVisibility(View.VISIBLE);
        BaseApplication.getInstance().addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.GET,
                        UriResources.Server.Combo,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                progressBar.setVisibility(View.GONE);
                                ComboRepository.refreshData(jsonArray);
                                rendercombo();
                                renderlistview();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();//
                        headers.put(Name.Key.Apikey, apiKey);
                        return headers;
                    }
                });
    }

    private void renderlistview() {
        listViewAdapter = new ListViewAdapter();
        listView.setAdapter(listViewAdapter);
    }
    private void rendercombo() {
        comboPagerAdapter=new ComboPagerAdapter();
        viewPagerCombo.setAdapter(comboPagerAdapter);
    }


    @Override
    public void onClick(View v) {
        Intent it = new Intent(getActivity(), CartActivity.class);
        it.putExtra(Name.Key.ParcelKey , cart);
        startActivityForResult( it , Name.Index.ActivityResult );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( listViewAdapter != null )
        {
            cart = data.getExtras().getParcelable(Name.Key.ParcelKey);

            SetingSum(cart);

            listViewAdapter.notifyDataSetChanged();
        }
    }


    private class ComboPagerAdapter extends PagerAdapter {


        private  List<Combo>  combos;
        public ComboPagerAdapter() {
            combos = ComboRepository.getCombos();
        }

        @Override
        public int getCount() {
            return combos.size() / 3 + 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int  pagerPosition) {
            View inflate = getActivity().getLayoutInflater().inflate(R.layout.pageritemcombo_container, null);
            LinearLayout pagerContainer = (LinearLayout) inflate.findViewById(R.id.pagerContainer);

            int start = 3 * pagerPosition + 0;
            int end = 3 * pagerPosition + 2;
            int[] pagerItemProductViewIds = {R.id.pagerItem1, R.id.pagerItem2, R.id.pagerItem3};
            for (int i = start; i <= end; i++) {
                View productView = pagerContainer.findViewById(pagerItemProductViewIds[i - 3 * pagerPosition]);

                final Combo combosvo;
                try {
                    combosvo = combos.get(i);
                } catch (Exception e) {
                    productView.setVisibility(View.INVISIBLE);
                    continue;
                }


                TextView textViewProductName = (TextView) productView.findViewById(R.id.textViewProductName);
                textViewProductName.setText(combosvo.getName());


                final TextView textViewProductCount = (TextView) productView.findViewById(R.id.textViewProductCount);
                final String productId =combosvo.getId();

                textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(productId)));//fix

                Button btnDrink=(Button)productView.findViewById(R.id.btnDrink);
                Button btnMeat = (Button) productView.findViewById(R.id.btnMeat);

                btnDrink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogsetview, null);
                        listView=(ListView)view.findViewById(R.id.listView1);
                        listView.setAdapter(new DrinkAdapter(combosvo));

                        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Order");
                        dialog.setView(view);
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int combocount=cart.checkComboCount(combosvo);
                                if (combocount>0)
                                {
                                    textViewProductCount.setText(String.valueOf(combocount));
                                    SetingSum(cart);
                                }
                                else
                                {
                                    textViewProductCount.setText(String.valueOf(combocount));
                                    SetingSum(cart);
                                    Toast.makeText(getActivity(), "套餐數量不符合,請重新檢查", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                        dialog.show();
                    }
                });

                btnMeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogsetview, null);
                        listView=(ListView)view.findViewById(R.id.listView1);
                        listView.setAdapter(new MeatAdapter(combosvo));

                        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Order");
                        dialog.setView(view);
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {


                            }
                        });
                        dialog.show();
                    }
                });

            }

            container.addView(inflate);
            return inflate;
        }
    }

    public class DrinkAdapter extends BaseAdapter
    {
        private Combo comboVo;
        private List<ComboDetail> details;
        public  DrinkAdapter(Combo comboVo) {
            this.comboVo = comboVo;
            this.details=comboVo.getDrinkDetails();
        }

        @Override
        public int getCount() {

            return details.size();
        }

        @Override
        public Object getItem(int arg0) {

            return details.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView =getActivity().getLayoutInflater().inflate(R.layout.dialoglistview_adapter, null);
                viewHolder = new ViewHolder();
                viewHolder.product = (TextView) convertView.findViewById(R.id.product);
                viewHolder.textViewProductCount=(TextView)convertView.findViewById(R.id.textViewProductCount);
                viewHolder.textViewProductPrice=(TextView)convertView.findViewById(R.id.textViewProductPrice);
                viewHolder.add= (Button) convertView.findViewById(R.id.add);
                viewHolder.sub= (Button) convertView.findViewById(R.id.sub);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final String productId=details.get(position).getProductId();
            viewHolder.product.setText(details.get(position).getName());
            viewHolder.textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(details.get(position).getProductId())));
            viewHolder.textViewProductPrice.setText(String.valueOf(details.get(position).getPrice()));
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.addToCart(productId, 1);
                    viewHolder.textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(details.get(position).getProductId())));
                    SetingSum(cart);
                }
            });


            viewHolder.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.addToCart(productId, -1);
                    viewHolder.textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(details.get(position).getProductId())));
                    SetingSum(cart);
                }
            });
            return convertView;

        }
        private class ViewHolder
        {
            TextView product,textViewProductPrice,textViewProductCount;
            Button add,sub;
        }
    }

    public class MeatAdapter extends BaseAdapter
    {
        private Combo comboVo;
        private List<ComboDetail> details;
        public  MeatAdapter(Combo comboVo) {
            this.comboVo = comboVo;
            this.details=comboVo.getDetails();
        }

        @Override
        public int getCount() {

            return details.size();
        }

        @Override
        public Object getItem(int arg0) {

            return details.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView =getActivity().getLayoutInflater().inflate(R.layout.dialoglistview_adapter, null);
                viewHolder = new ViewHolder();
                viewHolder.product = (TextView) convertView.findViewById(R.id.product);
                viewHolder.textViewProductCount=(TextView)convertView.findViewById(R.id.textViewProductCount);
                viewHolder.textViewProductPrice=(TextView)convertView.findViewById(R.id.textViewProductPrice);
                viewHolder.add= (Button) convertView.findViewById(R.id.add);
                viewHolder.sub= (Button) convertView.findViewById(R.id.sub);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String productId=details.get(position).getProductId();
            viewHolder.product.setText(details.get(position).getName());
            viewHolder.textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(details.get(position).getProductId())));
            viewHolder.textViewProductPrice.setText(String.valueOf(details.get(position).getPrice()));

            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.addToCart(productId, 1);
                    viewHolder.textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(details.get(position).getProductId())));
                    SetingSum(cart);

                }
            });



            viewHolder.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.addToCart(productId, -1);
                    viewHolder.textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(details.get(position).getProductId())));
                    SetingSum(cart);

                }
            });
            return convertView;

        }
        private class ViewHolder
        {
            TextView product,textViewProductPrice,textViewProductCount;;
            Button add,sub;
        }
    }

    private class ListViewAdapter extends BaseAdapter {

        private List<Category> categories = productRepository.getAllCategories();

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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.listitem_category, null);
                viewHolder = new ViewHolder();
                viewHolder.textViewVategoryTitle = (TextView) convertView.findViewById(R.id.textViewCategoryTitle);
                viewHolder.viewPagerProduct = (ViewPager) convertView.findViewById(R.id.productPager);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Category category = categories.get(position);

            viewHolder.textViewVategoryTitle.setText(category.getName());

            ProductPagerAdapter adapter = new ProductPagerAdapter(category.getProducts());

            viewHolder.viewPagerProduct.setAdapter(adapter);

            return convertView;
        }

        private class ViewHolder {

            TextView textViewVategoryTitle;
            ViewPager viewPagerProduct;
        }
    }

    private class ProductPagerAdapter extends PagerAdapter {

        private List<Product> products;

        public ProductPagerAdapter(List<Product> products) {
            this.products = products;
        }

        NetworkImageView image;
        RequestQueue queue;
        LruBitmapCache cache;
        com.android.volley.toolbox.ImageLoader ImageLoader;

        @Override
        public Object instantiateItem(ViewGroup container, int pagerPosition) {
            cache = new LruBitmapCache();
            ImageLoader = new ImageLoader(queue, cache);
            View inflate = getActivity().getLayoutInflater().inflate(R.layout.pageritem_container, null);
            LinearLayout pagerContainer = (LinearLayout) inflate.findViewById(R.id.pagerContainer);

            int start = 3 * pagerPosition + 0;
            int end = 3 * pagerPosition + 2;
            int[] pagerItemProductViewIds = {R.id.pagerItem1, R.id.pagerItem2, R.id.pagerItem3};
            for (int i = start; i <= end; i++) {
                View productView = pagerContainer.findViewById(pagerItemProductViewIds[i - 3 * pagerPosition]);

                Product product = null;
                try {
                    product = products.get(i);
                } catch (Exception e) {
                    productView.setVisibility(View.INVISIBLE);
                    continue;
                }

                TextView textViewProductName = (TextView) productView.findViewById(R.id.textViewProductName);
                textViewProductName.setText(product.getName());

                TextView textViewProductPrice = (TextView) productView.findViewById(R.id.textViewProductPrice);
                final int price = product.getPrice();
                textViewProductPrice.setText(String.valueOf(price));

                final TextView textViewProductCount = (TextView) productView.findViewById(R.id.textViewProductCount);
                final String productId = product.getId();

                textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(productId)));

                NetworkImageView imageView = (NetworkImageView)productView.findViewById(R.id.image);
                imageView.setImageUrl(product.getimage() , BaseApplication.getInstance().getImageLoader() );

                Button btnAdd = (Button) productView.findViewById(R.id.btnAdd);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cart.addToCart(productId, 1);

                       // checkCombo();
                        textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(productId)));
                        SetingSum(cart);
                    }
                });

                Button btnDecrease = (Button) productView.findViewById(R.id.btnDecrease);
                btnDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cart.addToCart(productId, -1);
                        textViewProductCount.setText(String.valueOf(cart.getProductCountInCart(productId)));
                        SetingSum(cart);
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
    private void SetingSum(Cart cart)
    {
        textViewPriceSum.setText(String.valueOf(cart.calculateSumPrice()));
        textViewTotal.setText(String.valueOf(cart.calculateSumCount()));
    }

//    private void checkCombo() {
//        final String availableComboId = cart.getAvailableComboId();
//        if (availableComboId != null) {
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Package upgrade")
//                    .setMessage("Do you want to upgrade to the package?")
//                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            cart.addToCart(availableComboId, 1);
//
//                            Combo comboVo = ComboRepository.getCombosMap().get(availableComboId);
//                            for (ComboDetail detailVo : comboVo.getDetails()) {
//                                cart.addToCart(detailVo.getProductId(), 0 - detailVo.getQuantity());
//                            }
//
//                            listViewAdapter.notifyDataSetChanged();
//                            textViewPriceSum.setText(String.valueOf(cart.calculateSumPrice()));
//                            textViewTotal.setText(String.valueOf(cart.calculateSumCount()));
//                            checkCombo();
//                        }
//                    })
//                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .show();
//        }

//    }
}
