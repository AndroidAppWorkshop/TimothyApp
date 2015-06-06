package com.timothy.Menu;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    private static final Map<String, CategoryVo> categoryVos = new LinkedHashMap<String, CategoryVo>();

    public List<CategoryVo> getAllCategories() {
        List list = new LinkedList();
        list.addAll(categoryVos.values());
        return list;
    }


        public ProductVo  findProductById(String productId) {
            for (CategoryVo categoryVo : categoryVos.values()) {
                for (ProductVo productVo : categoryVo.getProductVos()) {
                if (productId.equals(productVo.getId())) {
                    return productVo;
                }
            }
        }
        return null;
    }


    public static void refreshdata(JSONArray jsonArray) {
        categoryVos.clear();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String categoryId = jsonObject.getString("CategoryID");
                CategoryVo categoryVo;
                if (categoryVos.containsKey(categoryId)) {
                    categoryVo = categoryVos.get(categoryId);
                } else {
                    categoryVo = new CategoryVo(categoryId);
                    categoryVos.put(categoryId, categoryVo);
                }
                categoryVo.setName(categoryId);


                ProductVo productVo = new ProductVo(jsonObject.getString("Id"), jsonObject.getString("Name"), jsonObject.getInt("Price"));
                categoryVo.getProductVos().add(productVo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
