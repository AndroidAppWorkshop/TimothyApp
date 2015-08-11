package library.timothy.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.StringResources;


public class OrderRepository {

    private static final Map<String, HistoryOrder> orders = new LinkedHashMap<String, HistoryOrder>();

    private static final Map<String , Integer> counts= new HashMap<>();
    public static void refreshData(JSONArray jsonArray) {
        counts.clear();
        orders.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String orderid=jsonObject.getString(StringResources.Key.OrderID);
                HistoryOrder order=new HistoryOrder(jsonObject.getString(StringResources.Key.OrderID),
                        (jsonObject.getString(StringResources.Key.Status).equals(StringResources.Key.Undone)) ? StringResources.Text.Undone:StringResources.Text.Done);
                order.getProducts().add(new Status(jsonObject.getInt(StringResources.Key.Totalprice),jsonObject.getInt(StringResources.Key.Discount)));
                JSONArray detailsArray = jsonObject.getJSONArray(StringResources.Key.OrderDetail);

                for (int j = 0; j < detailsArray.length(); j++) {
                    JSONObject detailObject = detailsArray.getJSONObject(j);

                    Product product = new Product(detailObject.getString(StringResources.Key.OrderID), getDetailName(detailObject),
                            detailObject.getInt(StringResources.Key.Quantity));


                    order.getProducts().add(product);
                }
                orders.put(orderid,order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getDetailName(JSONObject detailObject ) throws JSONException {

        String name =
                (detailObject.isNull(StringResources.Key.ProductName) ? ""   : detailObject.getString(StringResources.Key.ProductName)   )+
                        (detailObject.isNull(StringResources.Key.ComboName)   ? ""   : detailObject.getString(StringResources.Key.ComboName)     );

        if(!detailObject.isNull(StringResources.Key.ComboDrinkName))
            return detailObject.getString(StringResources.Key.ComboDrinkName);

        if (counts.containsKey(name))
            counts.put(name, counts.get(name) + detailObject.getInt(StringResources.Key.Quantity));
        else
            counts.put(name , detailObject.getInt(StringResources.Key.Quantity));

        return  name ;
    }

    public static Map<String, HistoryOrder> getOrdersMap() {
        return orders;
    }

    public static List<HistoryOrder> getOrders() {
        List<HistoryOrder> list = new LinkedList<HistoryOrder>();
        list.addAll(orders.values());
        return list;
    }
    public static Map<String , Integer> getCounts(){
        return counts;
    }
}
