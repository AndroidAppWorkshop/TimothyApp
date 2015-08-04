package library.timothy.History;

import java.util.LinkedList;
import java.util.List;


public class HistoryOrder {
    private String orderid;
    private String status;
    //    private int totalprice,discount;
    private List<Product> products = new LinkedList<>();

    public HistoryOrder(String orderid, String status) {
        this.orderid = orderid;
        this.status = status;
//        this.totalprice=totalprice;
//        this.discount=discount;
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
//    public int gettotalprice() {return totalprice;}
//
//    public void settotalprice(int totalprice) {this.totalprice = totalprice;}
//
//    public int getdiscount() { return discount;}
//
//    public void setdiscount(int discount) {this.discount = discount;}
}
