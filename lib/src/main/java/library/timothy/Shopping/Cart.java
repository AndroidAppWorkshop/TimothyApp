package library.timothy.Shopping;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Cart implements Parcelable{
    private  Map<String, Integer> productInCart = new HashMap<String, Integer>();


    public static Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel source) {

            Cart cart = new Cart();
            source.readMap(cart.productInCart,ClassLoader.getSystemClassLoader());

            return cart;
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    public  void addToCart(String productId, int count) {
        Integer countInMap = productInCart.get(productId);
        if (countInMap == null) {
            countInMap = 0;
        }
        countInMap += count;
        if (countInMap < 0) {
            countInMap = 0;
        }
        productInCart.put(productId, countInMap);
    }

    public void deletetoCart(String productId, int count)
    {
        Integer countInMap =count;
        productInCart.put(productId, countInMap);
    }

    public  int getProductCountInCart(String productId) {
        Integer countInMap = productInCart.get(productId);
        if (countInMap == null) {
            countInMap = 0;
            productInCart.put(productId, countInMap);
        }
        return countInMap;
    }

    public  Map<String, Integer> getProductInCart() {
        return productInCart;
    }

    public  int calculateSumCount(){
        int count=0;
        int allDrink=0;
        for (Map.Entry<String, Integer> entry : productInCart.entrySet()) {
            int countInMap = entry.getValue();
            count+=countInMap;
        }
        List<ComboDetail> detailsDrink = ComboRepository.getCompareCombos().get(0).getDrinkDetails();
        for (ComboDetail detail : detailsDrink) {
            Integer countInProduct = productInCart.get(detail.getProductId());
            if (countInProduct != null) {
                allDrink += countInProduct.intValue();
            }
        }

        return count-allDrink;
    }

    public  int calculateSumPrice() {
        int sum = 0;
        ProductRepository productRepository = new ProductRepository();

        for (Map.Entry<String, Integer> entry : productInCart.entrySet()) {
            String productId = entry.getKey();
            int count = entry.getValue();

            Product product = productRepository.findProductById(productId);
            if (product != null) {
                sum += product.getPrice() * count;
            }
        }
        return sum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
           parcel.writeMap(productInCart);
    }

//    public  String getAvailableComboId() {
//        List<Combo> combos = ComboRepository.getCombos();
//        for (Combo combo : combos) {
//            List<ComboDetail> details = combo.getDetails();
//
//            boolean available = true;
//            for (ComboDetail detail : details) {
//                Integer countInProduct = productInCart.get(detail.getProductId());
//                Integer countInDetail = detail.getQuantity();
//
//                if (countInDetail.intValue() > countInProduct.intValue()) {
//                    available = false;
//                    break;
//                }
//            }
//
//            if (available) {
//                return combo.getId();
//            }
//        }
//
//        return null;
//    }


        public  int ComboCount(Combo combosvo) {
            int Meat = 0, Drink = 0;
            List<ComboDetail> comboDetails = combosvo.getDetails();
               for (ComboDetail detail : comboDetails) {
                       Integer countInProduct = productInCart.get(detail.getProductId());
                       if (countInProduct != null) {
                           Meat += countInProduct.intValue();
                       }
                    }
            if (Meat > 0) {return Meat;}

            return 0;

        }

        public  int ComboSetup(Combo combosvo) {
            int allMeat = 0, allDrink = 0;
            List<Combo> allcombos = ComboRepository.getCompareCombos();

                for (Combo combo : allcombos) {
                    List<ComboDetail> detailsMeat = combo.getDetails();

                    for (ComboDetail detail : detailsMeat) {
                        Integer countInProduct = productInCart.get(detail.getProductId());
                        if (countInProduct != null) {
                            allMeat += countInProduct.intValue();
                        }
                    }
                }
              if (combosvo.getId() == "Drink") {
                  List<ComboDetail> detailsDrink = combosvo.getDetails();
                  for (ComboDetail detail : detailsDrink) {
                      Integer countInProduct = productInCart.get(detail.getProductId());
                      if (countInProduct != null) {
                          allDrink += countInProduct.intValue();
                      }
                  }
                  if(allMeat!=allDrink)
                  {
                     return allMeat-allDrink;
                  }
              }

            return 0;
        }


}

