package library.timothy.history;


public class Product {
    private String id;
    private String name;
    private int  quantity;

    public Product(String id, String name,int  quantity) {
        this.id = id;
        this.name = name;
        this.quantity=quantity;
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


    public int getquantity() {
        return quantity;
    }

    public void setquantity(int quantity) {
        this.quantity= quantity;
    }
}
