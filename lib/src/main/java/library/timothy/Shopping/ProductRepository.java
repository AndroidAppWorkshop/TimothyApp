package library.timothy.Shopping;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.StringResources;

public class ProductRepository {

   public static final Map<String, Category> categoryVos = new LinkedHashMap<String, Category>();

    public static final List<Category> categorys = new LinkedList<Category>();

    public List<Category> getAllCategories() {
        List list = new LinkedList();
        list.addAll(categoryVos.values());
        return list;
    }

        public Product findProductById(String productId) {
            for (Category category : categorys) {
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
                String categoryId = jsonObject.getString(StringResources.Key.CategoryID);
                Category category;
                if (categoryVos.containsKey(categoryId)) {
                    category = categoryVos.get(categoryId);
                } else {
                    category = new Category(categoryId);
                    categoryVos.put(categoryId, category);
                }
                category.setName(categoryId);


                Product product = new Product(jsonObject.getString(StringResources.Key.ProductID),
                            jsonObject.getString(StringResources.Key.Productname),
                            jsonObject.getInt(StringResources.Key.ProductPrice) ,
                            jsonObject.getString(StringResources.Key.Image));
                            category.getProducts().add(product);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCategories();
    }

    public  static void  addCategories() {
        categorys.addAll(categoryVos.values());
    }



}
