package library.timothy.history;

import java.util.LinkedList;
import java.util.List;


public class Order {
    private String orderid;
    private String status;
    private int totalprice,discount;
    private List<Product> products = new LinkedList<Product>();

    public Order(String orderid,String status,int totalprice,int discount) {
        this.orderid = orderid;
        this.status=status;
        this.totalprice=totalprice;
        this.discount=discount;
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

    public int gettotalprice() {
        return totalprice;
    }

    public void settotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public int getdiscount() {
        return discount;
    }

    public void setdiscount(int discount) {
        this.discount = discount;
    }




}
