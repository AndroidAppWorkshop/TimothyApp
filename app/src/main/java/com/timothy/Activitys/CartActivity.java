package com.timothy.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.timothy.Adapter.CartAdapter;
import com.timothy.Core.BaseApplication;
import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import library.timothy.Resources.Name;
import library.timothy.Resources.UriResources;
import library.timothy.Shopping.Cart;
import library.timothy.Shopping.ProductRepository;
import library.timothy.history.OrderRepository;


public class CartActivity extends Activity implements View.OnClickListener{

    private static final String LOG_TAG = CartActivity.class.getSimpleName();
    private ProductRepository productRepository = new ProductRepository();
    public CartAdapter cartAdapter;
    TextView price;
    Cart cart;
    EditText actualreceipts;
    Button confirmMeal;
    JSONArray cartarray;
    int totalprice,realprice,disprice;
    private SharedPreferences sharedPreferences;
    private String apiKey;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart = getIntent().getParcelableExtra(Name.Key.KeyParcel);
        sharedPreferences = this.getSharedPreferences(Name.Key.Apikey, Context.MODE_PRIVATE);
        apiKey = sharedPreferences.getString(Name.Key.Apikey, null);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        confirmMeal=(Button)findViewById(R.id.button);
        confirmMeal.setOnClickListener(this);
        actualreceipts=(EditText)findViewById(R.id.editText2);
        ListView listView = (ListView) findViewById(R.id.cartlistview);
        cartAdapter=new CartAdapter(CartActivity.this,cart);
        listView.setAdapter(cartAdapter);
        price=(TextView)findViewById(R.id.textView2);
        price.setText(String.valueOf(cart.calculateSumPrice()));

    }
    public void refresh()
    {
        cartAdapter.notifyDataSetChanged();
        price.setText(String.valueOf(cart.calculateSumPrice()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent it = new Intent();
            it.putExtra(Name.Key.KeyParcel , cart);
            this.setResult( RESULT_OK , it );
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view)
    {
        totalprice=cart.calculateSumPrice();
        realprice=0;  disprice=0;

        String realpriceinput=actualreceipts.getText().toString();

        if(realpriceinput.length()>0)
        {
            realprice=Integer.valueOf(realpriceinput);
            disprice=totalprice-realprice;
        }
        else
        {
            realprice=totalprice;
        }
        Send(realprice, disprice, cart.getProductInCart());
    }

    private void Send(final int realprice, final int disprice,  Map<String, Integer> productInCart )
    {

        try {
            progressBar.setVisibility(View.VISIBLE);
            JSONEncode(productInCart);
            JSONObject orderBody = new JSONObject();
            orderBody.put(Name.Key.KeyDisprice, disprice);
            orderBody.put(Name.Key.KeyRealprice, realprice);
            orderBody.put( Name.Key.KeyCart , cartarray);

            Log.i(String.valueOf(R.string.jsonstring), orderBody.toString());

            BaseApplication.getInstance().addToRequestQueue(
                    new JsonObjectRequest
                            (Request.Method.POST,
                            UriResources.Server.shopping,orderBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(getResources().getString(R.string.Reponse), response.optString(Name.Key.KeyTrue));
                                    if (response.optString(Name.Key.KeyTrue).equals(Name.Key.KeySuccess)) {
                                        Intent it = new Intent(CartActivity.this, SendActivity.class);
                                        it.putExtra(Name.Key.KeyRealprice, realprice);
                                        it.putExtra(Name.Key.KeyDisprice, disprice);
                                        it.putExtra(Name.Key.KeyParcel, cart);
                                        startActivity(it);
                                        finish();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(CartActivity.this,getResources().getString(R.string.sendfail)+volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put(Name.Key.Apikey, apiKey);
                            return headers;
                        }
                    });

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private void JSONEncode( Map<String, Integer> productInCart )  throws JSONException {
        cartarray  = new JSONArray();
        for (Map.Entry<String,Integer> entry :productInCart.entrySet()) {
            String productId = entry.getKey();
            int count = entry.getValue();
            if(count>0)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Name.Key.KeyId, productId);
                jsonObject.put(Name.Key.KeyCount , count);
                cartarray.put(jsonObject);
            }

        }

    }

}

