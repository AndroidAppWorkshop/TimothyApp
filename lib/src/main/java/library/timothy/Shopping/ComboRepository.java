package library.timothy.Shopping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComboRepository {

    private static final Map<String, Combo> combos = new HashMap<String, Combo>();
    private static Category combomeal;
    public ComboRepository ()
    {
        combomeal = new Category("combo");
        combomeal.getProducts().add(new Product("combo_1-1", "Fried Rice", 45,null));
        combomeal.getProducts().add(new Product("combo_1-2", "Noodles", 20,null));
        combomeal.getProducts().add(new Product("combo_1-3", "Chafing dish", 40,null));
        combomeal.getProducts().add(new Product("combo_1-4", "Baked Rice", 60,null));
        combomeal.getProducts().add(new Product("combo_1-5", "Prawn", 90,null));

        Combo  combo = new Combo();
        combo.setId("combo_1-1");
        combo.getDetails().add(new ComboDetail("aa"));
        combo.getDetails().add(new ComboDetail("bb"));
        combo.getDetails().add(new ComboDetail("cc"));
        combo.getDetails().add(new ComboDetail("dd"));
        combos.put("combo_1-1", combo);

        Combo  combo1 = new Combo();
        combo1.setId("combo_1-2");
        combo1.getDetails().add(new ComboDetail("ee0"));
        combo1.getDetails().add(new ComboDetail("ff0"));
        combo1.getDetails().add(new ComboDetail("gg0"));
        combo1.getDetails().add(new ComboDetail("hh0"));
        combos.put("combo_1-2",combo1);

        Combo  combo2 = new Combo();
        combo2.setId("combo_1-3");
        combo2.getDetails().add(new ComboDetail("ee1"));
        combo2.getDetails().add(new ComboDetail("ff1"));
        combo2.getDetails().add(new ComboDetail("gg1"));
        combo2.getDetails().add(new ComboDetail("hh1"));
        combos.put("combo_1-3",combo2);

        Combo  combo3 = new Combo();
        combo3.setId("combo_1-4");
        combo3.getDetails().add(new ComboDetail("ee2"));
        combo3.getDetails().add(new ComboDetail("ff2"));
        combo3.getDetails().add(new ComboDetail("gg2"));
        combo3.getDetails().add(new ComboDetail("hh2"));
        combos.put("combo_1-4",combo3);


        Combo  combo4 = new Combo();
        combo4.setId("combo_1-5");
        combo4.getDetails().add(new ComboDetail("ee3"));
        combo4.getDetails().add(new ComboDetail("ff3"));
        combo4.getDetails().add(new ComboDetail("gg3"));
        combo4.getDetails().add(new ComboDetail("hh3"));
        combos.put("combo_1-5",combo4);


    }


    public static void refreshData(JSONArray jsonArray) {
        combos.clear();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Category category;
                String categoryId="0";
                if (ProductRepository.categoryVos.containsKey(categoryId)) {
                    category = ProductRepository.categoryVos.get(categoryId);
                } else {
                    category = new Category(categoryId);
                    ProductRepository.categoryVos.put(categoryId, category);
                }
                category.setName(categoryId);

                Product product = new Product(jsonObject.getString("Id"), jsonObject.getString("Name"), jsonObject.getInt("Price") , jsonObject.getString("imageUrl"));
                category.getProducts().add(product);

                Combo comboVo = new Combo();
                String comboId = jsonObject.getString("Id");
                comboVo.setId(comboId);

                JSONArray detailsArray = jsonObject.getJSONArray("Detail");

                for (int j = 0; j < detailsArray.length(); j++) {
                    JSONObject detailJsonObject = detailsArray.getJSONObject(j);

                    ComboDetail detailVo = new ComboDetail();

                    detailVo.setProductId(detailJsonObject.getString("Id"));
                    detailVo.setQuantity(detailJsonObject.getInt("Quantity"));

                    comboVo.getDetails().add(detailVo);
                }

                combos.put(comboId, comboVo);
            }


        } catch (JSONException e) {
        }
    }

    public static Map<String, Combo> getCombosMap() {
        return combos;
    }

    public static List<Combo> getCombos() {
        List<Combo> list = new LinkedList<Combo>();
        list.addAll(combos.values());
        return list;
    }

    public static Category getCategoryVo()
    {
        return combomeal;
    }
}
