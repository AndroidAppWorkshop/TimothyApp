package library.timothy.history;

import java.util.LinkedList;
import java.util.List;


public class Order {
    private String id;
    private List<Product> products = new LinkedList<Product>();

    public Order(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
