package library.timothy.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import library.timothy.Resources.StringResources;

public class OrderDetail {

    Map<String,String> child = new HashMap<>();
    public OrderDetail(JSONArray jsonArray) throws JSONException {
        int size = jsonArray.length();
        JSONObject jo ;
        String productName;
        String comboName;
        String comboDrinkName;
        String quantity;
        for (int index = 0; index < size ; index++) {

            jo = jsonArray.getJSONObject(index);

            comboName = jo.getString(StringResources.Key.ComboName);

            productName = jo.isNull(StringResources.Key.ProductName) ? "" : jo.getString(StringResources.Key.ProductName);  

            comboDrinkName = jo.isNull(StringResources.Key.ComboDrinkName) ? "" : jo.getString(StringResources.Key.ComboDrinkName);

            quantity =  jo.getString(StringResources.Key.Quantity);
            String name
                    = new StringBuilder()
                        .append(comboDrinkName)
                        .append(comboName)
                        .append(productName)
                        .toString();

            if(name.isEmpty() || name.equals(StringResources.Key.Null)){
                if(size > 1) continue ;
                name = StringResources.Text.Empty ;
            }else {
                name = name + " x " + quantity;
            }
            child.put(index+"", name );
        }
    }

    public Map<String, String> getChild() {
        return child;
    }
}
