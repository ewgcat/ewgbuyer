package com.ewgvip.buyer.android.models;

/**
 * Created by Administrator on 2016/7/18.
 */
public class GradeNameBean {
        public String icon;
        public String name;

        public GradeNameBean(String icon, String name) {
                this.icon = icon;
                this.name = name;
        }

        @Override
        public String toString() {
                return "GradeNameBean{" +
                        "icon='" + icon + '\'' +
                        ", name='" + name + '\'' +
                        '}';
        }
}
