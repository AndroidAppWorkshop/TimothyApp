package library.timothy.History;

import java.util.LinkedList;
import java.util.List;


public class HistoryOrder {
    private String orderid;
    private String status;
    private List<Product> products = new LinkedList<>();

    public HistoryOrder(String orderid, String status) {
        this.orderid = orderid;
        this.status = status;
    }

    public String getId() {
        return orderid;
    }

    public void setId(String id) {
        this.orderid = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getstatus() {
        return status;
    }
    public void setstatus(String status) {
        this.status = status;
    }
}
