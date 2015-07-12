package library.timothy.history;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class OrderRepository {

    private static final Map<String, Order> orders = new LinkedHashMap<String, Order>();

    public static void refreshData(JSONArray jsonArray) {
        orders.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String orderid=jsonObject.getString("orderID");
                Order order=new Order(jsonObject.getString("orderID"),
                        (jsonObject.getString("status") == "undone")?"未核銷":"已核銷");
                order.getProducts().add(new Status(jsonObject.getInt("totalprice"),jsonObject.getInt("discount")));
                JSONArray detailsArray = jsonObject.getJSONArray("orderDetail");

                for (int j = 0; j < detailsArray.length(); j++) {
                    JSONObject detailObject = detailsArray.getJSONObject(j);

                    Product product=new Product(detailObject.getString("orderID"),detailObject.getString("productName"),
                            detailObject.getInt("quantity"));
                    order.getProducts().add(product);
                }
                orders.put(orderid,order);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Order> getOrdersMap() {
        return orders;
    }

    public static List<Order> getOrders() {
        List<Order> list = new LinkedList<Order>();
        list.addAll(orders.values());
        return list;
    }


}
