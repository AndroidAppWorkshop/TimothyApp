package com.timothy.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.timothy.Adapter.CartAdapter;
import com.timothy.Core.BaseApplication;
import com.timothy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.StringResources;
import library.timothy.Resources.UriResources;
import library.timothy.Shopping.Cart;
import library.timothy.Shopping.Combo;
import library.timothy.Shopping.ComboDetail;
import library.timothy.Shopping.ComboRepository;
import library.timothy.Shopping.ProductRepository;
/**
 * 購物車Activity載入將購買的選項
 * 具有向Server端送出訂單的Funtion
**/
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
    //生命週期 於被呼叫時優先執行之一
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart = getIntent().getParcelableExtra(StringResources.Key.Data);
        sharedPreferences = this.getSharedPreferences(StringResources.Key.ApiKey, Context.MODE_PRIVATE);
        apiKey = sharedPreferences.getString(StringResources.Key.ApiKey, null);
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
    //刷新總價與List
    public void refresh()
    {
        cartAdapter.notifyDataSetChanged();
        price.setText(String.valueOf(cart.calculateSumPrice()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent it = new Intent();
            it.putExtra(StringResources.Key.Data, cart);
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

        if(realpriceinput.length()>0) {
            realprice=Integer.valueOf(realpriceinput);
            disprice=totalprice-realprice;
        }
        else {
            realprice=totalprice;
        }
        Send(realprice, disprice, cart.getProductInCart());
    }
    //呼叫後向Server端POST送出訂單
    private void Send(final int realprice, final int disprice,  Map<String, Integer> productInCart )
    {

        try {
            progressBar.setVisibility(View.VISIBLE);
            JSONEncode(productInCart);
            JSONObject orderBody = new JSONObject();
            orderBody.put(StringResources.Key.Discount, disprice);
            orderBody.put(StringResources.Key.TotalPrice, realprice);
            orderBody.put(StringResources.Key.CartDetail, cartarray);

            Log.i(String.valueOf(R.string.jsonstring), orderBody.toString());

            BaseApplication.getInstance().addToRequestQueue(
                    new JsonObjectRequest
                            (Request.Method.POST,
                            UriResources.Server.Shopping,orderBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if (!response.optString(StringResources.Key.False).equals(StringResources.Key.Failure)) {
                                        Toast.makeText(CartActivity.this,getResources().getString(R.string.OrderNumber)+response.optString(StringResources.Key.True), Toast.LENGTH_SHORT).show();
                                        Intent it = new Intent(CartActivity.this, SendActivity.class);
                                        it.putExtra(StringResources.Key.Realprice, realprice);
                                        it.putExtra(StringResources.Key.Disprice, disprice);
                                        it.putExtra(StringResources.Key.Data, cart);
                                        startActivity(it);
                                        finish();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                    else
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(CartActivity.this,response.optString(StringResources.Key.False), Toast.LENGTH_SHORT).show();
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
                            headers.put(StringResources.Key.ApiKey, apiKey);
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
                if(CheckComboID(productId))
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(StringResources.Key.ComboProductID, productId);
                    jsonObject.put(StringResources.Key.Quantity, count);
                    cartarray.put(jsonObject);
                }
                else if(CheckDrinkID(productId))
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(StringResources.Key.ComboDrinkID, productId);
                    jsonObject.put(StringResources.Key.Quantity, count);
                    cartarray.put(jsonObject);
                }
                else
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(StringResources.Key.ProductId, productId);
                    jsonObject.put(StringResources.Key.Quantity, count);
                    cartarray.put(jsonObject);
                }

            }

        }

    }

    public  Boolean CheckComboID(String productId)
    {
        List<Combo> allcombos = ComboRepository.getCompareCombos();

        for (Combo combo : allcombos) {
            List<ComboDetail> detailsMeat = combo.getDetails();

            for (ComboDetail detail : detailsMeat) {
                if(detail.getProductId().equals(productId))
                {
                    return true;
                }

            }
        }
        return false;
    }

    public  Boolean CheckDrinkID(String productId)
    {
        List<Combo> allcombos = ComboRepository.getCompareCombos();

        for (Combo combo : allcombos) {
            List<ComboDetail> detailsDrink = combo.getDrinkDetails();

            for (ComboDetail detail : detailsDrink) {
                if(detail.getProductId().equals(productId))
                {
                    return true;
                }

            }
        }
        return false;
    }





}

