package com.timothy.Menu;


import com.timothy.Tools.UriResources;

import java.util.LinkedList;
import java.util.List;

public class ProductRepository {

    private List<CategoryVo> categoryVos = new LinkedList<CategoryVo>();

    public ProductRepository() {
        CategoryVo drinkCategories = new CategoryVo("cat_1", "飲料");
        drinkCategories.getProductVos().add(new ProductVo("prod_1-1", "咖啡", 45, UriResources.Test.ListImage2));
        drinkCategories.getProductVos().add(new ProductVo("prod_1-2", "紅茶", 20, UriResources.Test.ListImage1));
        drinkCategories.getProductVos().add(new ProductVo("prod_1-3", "奶茶", 40, UriResources.Test.ListImage1));
        drinkCategories.getProductVos().add(new ProductVo("prod_1-4", "果汁", 60, UriResources.Test.ListImage2));
        drinkCategories.getProductVos().add(new ProductVo("prod_1-5", "香精", 90, UriResources.Test.ListImage2));

        CategoryVo foodCategories = new CategoryVo("cat_2", "餐點");
        foodCategories.getProductVos().add(new ProductVo("prod_2-1", "牛排", 120, UriResources.Test.ListImage2));
        foodCategories.getProductVos().add(new ProductVo("prod_2-2", "雞排", 90, UriResources.Test.ListImage2));
        foodCategories.getProductVos().add(new ProductVo("prod_2-3", "豬排", 90, UriResources.Test.ListImage2));
        foodCategories.getProductVos().add(new ProductVo("prod_2-4", "鐵板麵", 60, UriResources.Test.ListImage2));
        foodCategories.getProductVos().add(new ProductVo("prod_2-5", "牛肉麵", 80, UriResources.Test.ListImage2));

        CategoryVo sweetCategories = new CategoryVo("cat_3", "甜點");
        sweetCategories.getProductVos().add(new ProductVo("prod_3-1", "蛋糕", 100, UriResources.Test.ListImage3));
        sweetCategories.getProductVos().add(new ProductVo("prod_3-2", "冰淇淋", 80, UriResources.Test.ListImage3));
        sweetCategories.getProductVos().add(new ProductVo("prod_3-3", "布丁", 15, UriResources.Test.ListImage3));
        sweetCategories.getProductVos().add(new ProductVo("prod_3-4", "蛋塔", 25, UriResources.Test.ListImage3));
        sweetCategories.getProductVos().add(new ProductVo("prod_3-5", "蘋果派", 40, UriResources.Test.ListImage3));

        categoryVos.add(drinkCategories);
        categoryVos.add(foodCategories);
        categoryVos.add(sweetCategories);
    }
    public List<CategoryVo> getAllCategories() {
        return categoryVos;
    }
}
