package library.timothy.Shopping;


import java.util.LinkedList;
import java.util.List;

public class Combo {

        private String id;
        private List<ComboDetail> details = new LinkedList<ComboDetail>();

        public String getId() {
            return id;
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
    }


