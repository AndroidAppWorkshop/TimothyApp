package me.leolin.android.viewpager.in.listview;

import java.util.HashMap;
import java.util.Map;


public class Cart {
    private static Map<String, Integer> productInCart = new HashMap<String, Integer>();


    public static void addToCart(String productId, int count) {
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

    public static int getProductCountInCart(String productId) {
        Integer countInMap = productInCart.get(productId);
        if (countInMap == null) {
            countInMap = 0;
            productInCart.put(productId, countInMap);
        }
        return countInMap;
    }

    public static Map<String, Integer> getProductInCart() {
        return productInCart;
    }

    public static int calculateSumPrice() {
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
}
