package com.timothy.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import com.timothy.Adapter.CartAdapter;
import com.timothy.R;
import library.timothy.Resources.NameResources;
import library.timothy.Shopping.Cart;
import library.timothy.Shopping.ProductRepository;


public class CartActivity extends Activity {

    private ProductRepository productRepository = new ProductRepository();
    public CartAdapter cartAdapter;
    TextView price;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart = getIntent().getParcelableExtra(NameResources.Key.ParcelKey);

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
            this.setResult(RESULT_OK , it);
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
