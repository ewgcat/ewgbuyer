package com.ewgvip.buyer.android.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ewgvip.buyer.android.layout.MyViewPager;

/**
 * Created by Administrator on 2015/10/30.
 */
public class ChatFacePagerAdapter extends PagerAdapter {
    private View[] views;

    private MyViewPager viewPager;

    public ChatFacePagerAdapter(View[] views, MyViewPager vp_goodDetail) {
        this.views = views;
        this.viewPager = vp_goodDetail;
    }

    @Override
    public int getCount() {
        return views.length;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object); // 父类要求必须实现此方法.
        // 不实现会抛异常.
        viewPager.removeView(views[position]);
    }

    /**
     * 当需要加载item时出发此方法 position 就是将要被加载的item的索引
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views[position];
        viewPager.addView(view);
        return view;
    }
}
