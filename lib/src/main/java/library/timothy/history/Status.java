package library.timothy.history;

import library.timothy.Resources.StringResources;

/**
 * Created by h94u04 on 2015/7/12.
 */
public class Status extends Product{
    private int totalprice =0;
    private int discount =0;
    public Status(int totalprice , int discount) {
        this.totalprice = totalprice;
        this.discount = discount;
    }
    @Override
    public String getName() {
        return StringResources.Text.AllPrice +totalprice + StringResources.Text.Discount + discount;
    }

}
