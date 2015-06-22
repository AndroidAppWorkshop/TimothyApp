package com.timothy.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.timothy.Adapter.CartAdapter;
import com.timothy.R;
import library.timothy.Resources.NameResources;
import library.timothy.Shopping.Cart;
import library.timothy.Shopping.ProductRepository;


public class CartActivity extends Activity implements View.OnClickListener{

    private ProductRepository productRepository = new ProductRepository();
    public CartAdapter cartAdapter;
    TextView price;
    Cart cart;
    EditText discount;
    Button confirmMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart = getIntent().getParcelableExtra(NameResources.Key.ParcelKey);

        confirmMeal=(Button)findViewById(R.id.button);
        confirmMeal.setOnClickListener(this);
        discount=(EditText)findViewById(R.id.editText2);
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
        int price=0;
        String disprice=discount.getText().toString();
        if(disprice.length()>0)
        {
            price=Integer.valueOf(disprice);
        }
//        Intent it = new Intent(this, CartActivity.class);
//        it.putExtra("price",price);
//        it.putExtra(NameResources.Key.ParcelKey , cart);
//        startActivity(it);
    }
}
