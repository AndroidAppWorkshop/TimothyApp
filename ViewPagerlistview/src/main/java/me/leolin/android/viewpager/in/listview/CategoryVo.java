package me.leolin.android.viewpager.in.listview;


import java.util.LinkedList;
import java.util.List;

//分區
public class CategoryVo {
    private String id;
    private String name;
    private List<ProductVo> productVos = new LinkedList<ProductVo>();

    public CategoryVo(String id, String name) {
        this.id = id;
        this.name = name;
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

    public List<ProductVo> getProductVos() {
        return productVos;
    }

    public void setProductVos(List<ProductVo> productVos) {
        this.productVos = productVos;
    }
}