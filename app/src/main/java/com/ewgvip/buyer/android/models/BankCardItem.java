package com.ewgvip.buyer.android.models;

/**
 * Created by Administrator on 2016/7/23.
 */
public class BankCardItem {


    /**
     * addTime : 2016-07-19 15:02:15
     * bank_code :
     * bank_img : https://apimg.alipay.com/combo.png?d=cashier&t=CCB
     * bank_name :
     * card_number :
     * id : 556
     * mobile :
     * user_name :
     */

    public String addTime;
    public String bank_code;
    public String bank_img;
    public String bank_name;
    public String card_number;
    public String id;
    public String mobile;
    public String user_name;

    public BankCardItem(String id, String card_number, String bank_name) {
        this.id = id;
        this.card_number = card_number;
        this.bank_name = bank_name;
    }

    public BankCardItem(String addTime, String bank_code, String bank_img, String bank_name, String card_number, String id, String mobile, String user_name) {
        this.addTime = addTime;
        this.bank_code = bank_code;
        this.bank_img = bank_img;
        this.bank_name = bank_name;
        this.card_number = card_number;
        this.id = id;
        this.mobile = mobile;
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "BankCardItem{" +
                "addTime='" + addTime + '\'' +
                ", bank_code='" + bank_code + '\'' +
                ", bank_img='" + bank_img + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", card_number='" + card_number + '\'' +
                ", id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
