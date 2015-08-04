package library.timothy.Order;

import org.json.JSONException;
import org.json.JSONObject;

import library.timothy.Resources.StringResources;

/**
 * Created by h94u04 on 2015/8/1.
 */
public class Order extends OrderDetail {

    String OrderID;

    Order(JSONObject jsonObject) throws JSONException {
        super(jsonObject.getJSONArray(StringResources.Key.OrderDetail));
        this.OrderID = jsonObject.getString(StringResources.Key.OrderID) ;
    }

    public String getOrderID() {
        return  OrderID ;
    }
}
