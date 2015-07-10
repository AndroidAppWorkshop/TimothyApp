package com.timothy.Adapter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.timothy.Activitys.CartActivity;
import com.timothy.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import library.timothy.Shopping.Cart;
import library.timothy.Shopping.Product;
import library.timothy.Shopping.ProductRepository;

public class CartAdapter extends BaseAdapter
{
    private ProductRepository productRepository = new ProductRepository();
    private LayoutInflater adapterLayoutInflater;
    private CartActivity cartActivity;
    Map<String, Integer> productInCart ;
    List<String> productposition = new LinkedList<String>();
    List<String> productDisplayList = new LinkedList<String>();
    Cart cart;

     public CartAdapter(CartActivity cartActivity, Cart cart) {
        adapterLayoutInflater = LayoutInflater.from(cartActivity);
        this.cartActivity=cartActivity;
        this.cart=cart;
         listview();
    }
    public void listview()
    {
        productDisplayList.clear();
        productposition.clear();
        productInCart= cart.getProductInCart();
        for (Map.Entry<String, Integer> entry : productInCart.entrySet() )
        {
            String productId = entry.getKey();
            int count = entry.getValue();
            if (count > 0) {
                Product product = productRepository.findProductById(productId);//
                productDisplayList.add(product.getName() + ":" + count);
                productposition.add(product.getId());
            }
        }
    }

    @Override
    public int getCount() {
        return productDisplayList.size();
    }

    @Override
    public Object getItem(int i) {
        return productDisplayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = adapterLayoutInflater.inflate(R.layout.cart_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.product = (TextView) convertView.findViewById(R.id.product);

            viewHolder.add= (Button) convertView.findViewById(R.id.add);
            viewHolder.sub= (Button) convertView.findViewById(R.id.sub);
            viewHolder.delete= (Button) convertView.findViewById(R.id.delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.product.setText(productDisplayList.get(position));
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cart.addToCart(String.valueOf(productposition.get(position)), 1);
            listview();
            cartActivity.refresh();
        }
        });
        viewHolder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addToCart(String.valueOf(productposition.get(position)), -1);
                listview();
                cartActivity.refresh();

            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 AlertDialog.Builder builder=new AlertDialog.Builder(cartActivity);
                builder.setMessage("Confirm Delete") .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        cart.deletetoCart(String.valueOf(productposition.get(position)), 0);
                        listview();
                        cartActivity.refresh();

                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
            }
        });


        return  convertView;
    }

    private class ViewHolder {

        TextView product;
        Button add,sub,delete;

    }


}




