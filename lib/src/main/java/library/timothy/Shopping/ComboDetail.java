package library.timothy.Shopping;


public class ComboDetail {
    private String productId;
    private Integer Quantity;

    public ComboDetail() {}//fix

    public ComboDetail(String id) {
        this.productId=id;
    }//fix

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }
}