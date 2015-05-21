package me.leolin.android.viewpager.in.listview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class CartActivity extends Activity {

    private ProductRepository productRepository = new ProductRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        List<String> productDisplayList = new LinkedList<String>();

        Map<String, Integer> productInCart = Cart.getProductInCart();

        for (Map.Entry<String, Integer> entry : productInCart.entrySet()) {
            String productId = entry.getKey();
            int count = entry.getValue();
            if (count > 0) {
                ProductVo productVo = productRepository.findProductById(productId);
                productDisplayList.add(productVo.getName() + ":" + count);
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productDisplayList);
        ListView listView = (ListView) findViewById(R.id.cartlistview);
        listView.setAdapter(arrayAdapter);
    }


}
