package com.timothy.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.timothy.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.NameResources;
import library.timothy.Shopping.Cart;
import library.timothy.Shopping.Product;
import library.timothy.Shopping.ProductRepository;

public class SendActivity extends Activity {
    Cart cart;
    ListView listView;
    TextView disprice,realprice;
    Button gotomenue;
    private ProductRepository productRepository = new ProductRepository();
    int setdisprice=0,setrealprice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        cart = getIntent().getParcelableExtra(NameResources.Key.ParcelKey);
        setdisprice=getIntent().getIntExtra("disprice", 0);
        setrealprice=getIntent().getIntExtra("realprice", 0);

        listView=(ListView)findViewById(R.id.listview);
        disprice=(TextView)findViewById(R.id.textView6);
        realprice=(TextView)findViewById(R.id.textView4);
        gotomenue=(Button)findViewById(R.id.button2);
        List<String> productDisplayList = new LinkedList<String>();

        Map<String, Integer> productInCart = cart.getProductInCart();
        for (Map.Entry<String, Integer> entry : productInCart.entrySet()) {
            String productId = entry.getKey();
            int count = entry.getValue();
            if (count > 0) {
                Product product = productRepository.findProductById(productId);
                productDisplayList.add(product.getName() + ":" + count);
            }
        }

        disprice.setText(String.valueOf(setdisprice));
        realprice.setText(String.valueOf(setrealprice));
        gotomenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(SendActivity.this,MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
                finish();
            }
        });

    }


}