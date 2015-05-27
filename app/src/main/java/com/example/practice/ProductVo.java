package com.example.practice;

//單品class
public class ProductVo {
    private String id;
    private String name;
    private int price;
    private int image;
    public ProductVo(String id, String name, int price,int image) {
        this.id = id;
        this.name = name;
        this.price= price;
        this.image=image;
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

    public int getimage() {
        return image;
    }

}