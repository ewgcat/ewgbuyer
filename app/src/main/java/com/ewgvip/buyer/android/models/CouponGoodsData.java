package com.ewgvip.buyer.android.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/10/13.
 * 优惠券领取成功后推荐的可以购买的商品信息
 */
public class CouponGoodsData implements Parcelable {
    private String goods_name;
    private String goods_pic;
    private String goods_price;
    private String goods_id;

    public CouponGoodsData() {
        super();
    }

    public CouponGoodsData(String goods_name, String goods_pic, String goods_price, String goods_id) {
        this.goods_name = goods_name;
        this.goods_pic = goods_pic;
        this.goods_price = goods_price;
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_pic() {
        return goods_pic;
    }

    public void setGoods_pic(String goods_pic) {
        this.goods_pic = goods_pic;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    @Override
    public String toString() {
        return "CouponGoodsData{" +
                "goods_name='" + goods_name + '\'' +
                ", goods_pic='" + goods_pic + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", goods_id='" + goods_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {

        return 0;
    }

    //该方法将类的数据写入外部提供的Parcel中。
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goods_name);
        dest.writeString(goods_pic);
        dest.writeString(goods_price);
    }

    public static final Parcelable.Creator<CouponGoodsData> CREATOR = new Creator<CouponGoodsData>() {
        //实现从source中创建出类的实例的功能
        @Override
        public CouponGoodsData createFromParcel(Parcel source) {
            CouponGoodsData parInfo = new CouponGoodsData();
            parInfo.goods_name = source.readString();
            parInfo.goods_pic = source.readString();
            parInfo.goods_price = source.readString();
            return parInfo;
        }

        //创建一个类型为T，长度为size的数组
        @Override
        public CouponGoodsData[] newArray(int size) {
            return new CouponGoodsData[size];
        }
    };
}