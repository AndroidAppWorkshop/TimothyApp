package com.timothy.Menu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;


public class Cart implements Parcelable{
    private  Map<String, Integer> productInCart = new HashMap<String, Integer>();


    public static Parcelable.Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel source) {

            Cart cart = new Cart();
            source.readMap(cart.productInCart,ClassLoader.getSystemClassLoader());
            return cart;
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };



    public  void addToCart(String productId, int count) {
        Integer countInMap = productInCart.get(productId);
        if (countInMap == null) {
            countInMap = 0;
        }
        countInMap += count;
        if (countInMap < 0) {
            countInMap = 0;
        }
        productInCart.put(productId, countInMap);
    }

    public  int getProductCountInCart(String productId) {
        Integer countInMap = productInCart.get(productId);
        if (countInMap == null) {
            countInMap = 0;
            productInCart.put(productId, countInMap);
        }
        return countInMap;
    }

    public  Map<String, Integer> getProductInCart() {
        return productInCart;
    }

    public  int calculateSumPrice() {
        int sum = 0;
        ProductRepository productRepository = new ProductRepository();

        for (Map.Entry<String, Integer> entry : productInCart.entrySet()) {
            String productId = entry.getKey();
            int count = entry.getValue();

            ProductVo productVo = productRepository.findProductById(productId);
            if (productVo != null) {
                sum += productVo.getPrice() * count;
            }
        }
        return sum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
           parcel.writeMap(productInCart);
    }
}
