package com.timothy.Menu;

//單品class
public class Product {
    private String id;
    private String name;
    private int price;
    private String image;
    public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price= price;
        //this.image=ImageUrl;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getimage() {
        return image;
    }
}