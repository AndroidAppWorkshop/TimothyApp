package library.timothy.Shopping;


import java.util.LinkedList;
import java.util.List;

public class Combo {

        private String id;
        private String name;
        private String Image;
        private List<ComboDetail> details = new LinkedList<ComboDetail>();
        private List<ComboDetail> drinkDetails = new LinkedList<ComboDetail>();

        public String getName() {
        return name;
         }

        public void setName(String name) {
        this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getImage() {
            return Image;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<ComboDetail> getDetails() {
            return details;
        }

        public void setDetails(List<ComboDetail> details) {
            this.details = details;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        public List<ComboDetail> getDrinkDetails() {
        return drinkDetails;
    }

        public void setDrinkDetails(List<ComboDetail> details) {
        this.drinkDetails = drinkDetails;
    }
    }


