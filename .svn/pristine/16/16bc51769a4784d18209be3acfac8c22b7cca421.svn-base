package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    List<View> viewLists;

    public ViewPagerAdapter(Context context, List<String> imglist) {
        viewLists = new ArrayList();
        for (String  imgUrl : imglist) {
            SimpleDraweeView view = new SimpleDraweeView(context);
            Uri uri = Uri.parse(imgUrl);
            view.setImageURI(uri);
            viewLists.add(view);
        }
    }

    public ViewPagerAdapter(MainActivity mActivity, List<String> imglist) {
        viewLists = new ArrayList();
        for (String imgUrl : imglist) {
            SimpleDraweeView view = new SimpleDraweeView(mActivity);
            Uri uri = Uri.parse(imgUrl);
            view.setImageURI(uri);
            viewLists.add(view);
        }
    }

    @Override
    public int getCount() { // 获得size

        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View view, int position, Object object) // 销毁Item
    {
        ((ViewPager) view).removeView(viewLists.get(position));
    }

    @Override
    public Object instantiateItem(View view, int position) // 实例化Item
    {
        ((ViewPager) view).addView(viewLists.get(position));

        return viewLists.get(position);
    }
}
