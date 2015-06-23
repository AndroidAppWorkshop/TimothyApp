package library.timothy.Shopping;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import library.timothy.Shopping.Category;
import library.timothy.Shopping.Product;

public class ProductRepository {

   public static final Map<String, Category> categoryVos = new LinkedHashMap<String, Category>();

    public List<Category> getAllCategories() {
        List list = new LinkedList();
        list.addAll(categoryVos.values());
        return list;
    }

        public Product findProductById(String productId) {
            for (Category category : categoryVos.values()) {
                for (Product product : category.getProducts()) {
                if (productId.equals(product.getId())) {
                    return product;
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
                Category category;
                if (categoryVos.containsKey(categoryId)) {
                    category = categoryVos.get(categoryId);
                } else {
                    category = new Category(categoryId);
                    categoryVos.put(categoryId, category);
                }
                category.setName(categoryId);


                Product product = new Product(jsonObject.getString("Id"), jsonObject.getString("Name"), jsonObject.getInt("Price") , jsonObject.getString("imageUrl"));
                category.getProducts().add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
