package com.ewgvip.buyer.android.models;

import java.util.List;

/**
 * 商品二级分类
 * Created by lgx on 2015/10/15.
 */
public class GoodsClassSecond {
    private int id;
    private String name;
    private List<GoodsClassThird> list;
    private int maxSize;

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<GoodsClassThird> getList() {
        return list;
    }

    public void setList(List<GoodsClassThird> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
