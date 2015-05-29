package com.example.practice.MenuListClass;


import com.example.practice.ToolsAndTable.UrlValue;

import java.util.LinkedList;
import java.util.List;

public class ProductRepository {

    private List<CategoryVo> categoryVos = new LinkedList<CategoryVo>();

    public ProductRepository() {
        CategoryVo drinkCategories = new CategoryVo("cat_1", "飲料");
        drinkCategories.getProductVos().add(new ProductVo("prod_1-1", "咖啡", 45, UrlValue.ListImageWilson));
        drinkCategories.getProductVos().add(new ProductVo("prod_1-2", "紅茶", 20,UrlValue.ListImageJason));
        drinkCategories.getProductVos().add(new ProductVo("prod_1-3", "奶茶", 40,UrlValue.ListImageJason));
        drinkCategories.getProductVos().add(new ProductVo("prod_1-4", "果汁", 60,UrlValue.ListImageWilson));
        drinkCategories.getProductVos().add(new ProductVo("prod_1-5", "香精", 90,UrlValue.ListImageWilson));

        CategoryVo foodCategories = new CategoryVo("cat_2", "餐點");
        foodCategories.getProductVos().add(new ProductVo("prod_2-1", "牛排", 120,UrlValue.ListImageWilson));
        foodCategories.getProductVos().add(new ProductVo("prod_2-2", "雞排", 90,UrlValue.ListImageWilson));
        foodCategories.getProductVos().add(new ProductVo("prod_2-3", "豬排", 90,UrlValue.ListImageWilson));
        foodCategories.getProductVos().add(new ProductVo("prod_2-4", "鐵板麵", 60,UrlValue.ListImageWilson));
        foodCategories.getProductVos().add(new ProductVo("prod_2-5", "牛肉麵", 80,UrlValue.ListImageWilson));

        CategoryVo sweetCategories = new CategoryVo("cat_3", "甜點");
        sweetCategories.getProductVos().add(new ProductVo("prod_3-1", "蛋糕", 100,UrlValue.ListImageAlice));
        sweetCategories.getProductVos().add(new ProductVo("prod_3-2", "冰淇淋", 80,UrlValue.ListImageAlice));
        sweetCategories.getProductVos().add(new ProductVo("prod_3-3", "布丁", 15,UrlValue.ListImageAlice));
        sweetCategories.getProductVos().add(new ProductVo("prod_3-4", "蛋塔", 25,UrlValue.ListImageAlice));
        sweetCategories.getProductVos().add(new ProductVo("prod_3-5", "蘋果派", 40,UrlValue.ListImageAlice));

        categoryVos.add(drinkCategories);
        categoryVos.add(foodCategories);
        categoryVos.add(sweetCategories);
    }
    public List<CategoryVo> getAllCategories() {
        return categoryVos;
    }
}
//1.新增一個乾淨的List<ProductVo> productVos,drink內的List<ProductVo> 開始新增它
//2.新增完後把這個add進去 List<CategoryVo> categoryVos