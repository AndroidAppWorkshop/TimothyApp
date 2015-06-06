package com.timothy.Menu;


import java.util.LinkedList;
import java.util.List;

//分區
public class Category {
    private String id;
    private String name;
    private List<Product> products = new LinkedList<Product>();

    public Category(String id) {
        this.id = id;
       // this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}