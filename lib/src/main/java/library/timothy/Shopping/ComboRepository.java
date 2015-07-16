package library.timothy.Shopping;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComboRepository {

    private static final Map<String, Combo> combos = new LinkedHashMap<String, Combo>();
    private static final List<Combo> comparelist = new LinkedList<Combo>();


    private final static String[] addcomboname={"打拋","綠咖哩","椰汁咖哩","瑪莎曼",""};

    public static void refreshData(JSONArray jsonArray) {
        combos.clear();
        String comboId=null;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Category category;
                Combo comboVo = new Combo();

                comboId = jsonObject.getString("comboId");
                String comboName=jsonObject.getString("comboName");
                comboVo.setId(comboId);
                comboVo.setName(comboName);

                category=new Category(comboId);
                category.setName(comboName);

                JSONArray detailsMeatArray = jsonObject.getJSONArray("detail");
                JSONArray detailsDrinkArray = jsonObject.getJSONArray("combodrink");

                for (int j = 0; j < detailsMeatArray.length(); j++) {
                    JSONObject detailJsonObject = detailsMeatArray.getJSONObject(j);

                    ComboDetail detailMeat = new ComboDetail();

                    detailMeat.setProductId(detailJsonObject.getString("productID"));
                    detailMeat.setPrice(detailJsonObject.getInt("price"));
                    detailMeat.setName(detailJsonObject.getString("meat")+addcomboname[i]);

                    comboVo.getDetails().add(detailMeat);

                    Product product = new Product(detailJsonObject.getString("productID"), detailJsonObject.getString("meat")+addcomboname[i], detailJsonObject.getInt("price"), null);
                    category.getProducts().add(product);
                }
                for (int k = 0; k< detailsDrinkArray.length(); k++) {
                    JSONObject detailJsonObject = detailsDrinkArray.getJSONObject(k);

                    ComboDetail detailDrink = new ComboDetail();

                    detailDrink.setProductId(detailJsonObject.getString("DrinkID"));
                    detailDrink.setName(detailJsonObject.getString("DrinkName"));

                    comboVo.getDrinkDetails().add(detailDrink);

                    Product product = new Product(detailJsonObject.getString("DrinkID"), detailJsonObject.getString("DrinkName"), 0, null);
                    category.getProducts().add(product);

                }
                ProductRepository.categorys.add(category);
                combos.put(comboId, comboVo);
            }

            adddrinkcombo(comboId);
        } catch (JSONException e) {
        }
    }

    public static void adddrinkcombo(String comboId)
    {
        comparelist.addAll(combos.values());
        Combo comboVo = new Combo();
        comboVo.setId("Drink");
        comboVo.setName("附餐飲料");
        List<ComboDetail> drinkdetails=combos.get(comboId).getDrinkDetails();
        for(int i=0;i<drinkdetails.size();i++)
        {
            ComboDetail detailMeat = new ComboDetail();
            detailMeat.setProductId(drinkdetails.get(i).getProductId());
            detailMeat.setPrice(drinkdetails.get(i).getPrice());
            detailMeat.setName(drinkdetails.get(i).getName());
            comboVo.getDetails().add(detailMeat);
            comboVo.getDrinkDetails().add(detailMeat);
        }
        combos.put("Drink", comboVo);

    }

    public static Map<String, Combo> getCombosMap() {
        return combos;
    }

    public static List<Combo> getCombos() {
        List<Combo> list = new LinkedList<Combo>();
        list.addAll(combos.values());
        return list;
    }

    public static List<Combo> getCompareCombos() {return comparelist;}


}
