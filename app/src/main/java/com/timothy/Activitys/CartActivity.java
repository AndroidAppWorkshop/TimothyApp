package com.timothy.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.Map;

import library.timothy.Resources.NameResources;
import library.timothy.Resources.UriResources;
import library.timothy.Shopping.Cart;
import library.timothy.Shopping.ProductRepository;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart = getIntent().getParcelableExtra(NameResources.Key.ParcelKey);

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
            it.putExtra(NameResources.Key.ParcelKey , cart);
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
            JSONEncode(productInCart);
            JSONObject orderBody = new JSONObject();
            orderBody.put("Disprice", disprice);
            orderBody.put("Realprice", realprice);
            orderBody.put("Cart",cartarray);

            Log.i("JSON String",orderBody.toString());

            BaseApplication.getInstance().addToRequestQueue(
                    new JsonObjectRequest(Request.Method.POST, UriResources.Server.LogIn,orderBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent it = new Intent(CartActivity.this, SendActivity.class);
                                    it.putExtra("realprice",realprice);
                                    it.putExtra("disprice",disprice);
                                    it.putExtra(NameResources.Key.ParcelKey , cart);
                                    startActivity(it);
                                    finish();

                                    }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(CartActivity.this, "Send fail:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private void JSONEncode( Map<String, Integer> productInCart )  throws JSONException {
        cartarray  = new JSONArray();
        for (Map.Entry<String,Integer> entry :productInCart.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            String productId = entry.getKey();
            int count = entry.getValue();
            jsonObject.put("Id", productId);
            jsonObject.put("Count",count);
           cartarray.put(jsonObject);
        }

    }

}

