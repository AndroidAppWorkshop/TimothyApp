package library.timothy.Order;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by h94u04 on 2015/8/4.
 */

public class OrderList {

    Map<String, Order> OrderList = new HashMap<>();
    List<String> KeyList = new ArrayList<>();

    public library.timothy.Order.OrderList mapNewData(JSONArray jsonArray) {
        reset();
        int size = jsonArray.length();
        try {
            for (int index = 0; index < size; index++) {
                Order OrderGroup = new Order(jsonArray.getJSONObject(index));
                String OrderID = OrderGroup.getOrderID();
                OrderList.put( OrderID , OrderGroup );
                KeyList.add( OrderID );
            }
        }
        catch (JSONException e) { e.printStackTrace();}
        return this;
    }
    public Map<String, Order> getOrderList() {
        return this.OrderList;
    }
    public List<String> getKeyList() {
        return this.KeyList;
    }
    public void reset(){
        KeyList.clear();
        OrderList.clear();
    }
}
