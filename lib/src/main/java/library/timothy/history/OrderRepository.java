package library.timothy.history;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import library.timothy.Resources.StringResources;


public class OrderRepository {

    private static final Map<String, HistoryOrder> orders = new LinkedHashMap<String, HistoryOrder>();

    public static void refreshData(JSONArray jsonArray) {
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

                    Product product=new Product(detailObject.getString(StringResources.Key.OrderID),getDetailName(detailObject),
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

        return  (detailObject.isNull(StringResources.Key.ProductName)? "" : detailObject.getString(StringResources.Key.ProductName)   )+
                (detailObject.isNull(StringResources.Key.ComboName)  ? "" : detailObject.getString(StringResources.Key.ComboName)     )+
                (detailObject.isNull(StringResources.Key.ComboDrinkName)  ? "" : detailObject.getString(StringResources.Key.ComboDrinkName));
    }

    public static Map<String, HistoryOrder> getOrdersMap() {
        return orders;
    }

    public static List<HistoryOrder> getOrders() {
        List<HistoryOrder> list = new LinkedList<HistoryOrder>();
        list.addAll(orders.values());
        return list;
    }


}
