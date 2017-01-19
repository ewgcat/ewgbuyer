package com.ewgvip.buyer.android.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;

import java.util.List;

/**
 * Created by Administrator on 2015/10/28.
 */
public class StoreIndexAdapter extends FragmentStatePagerAdapter {

    BaseActivity mActivity;
    List<String> title_list;
    List<Fragment> fragment_list;
    List<Bitmap> bitmaps_list;

    public StoreIndexAdapter(FragmentManager fm, BaseActivity mActivity, List<String> title_list, List<Fragment> fragment_list) {
        super(fm);
        this.mActivity = mActivity;
        this.title_list = title_list;
        this.fragment_list = fragment_list;
        this.bitmaps_list = bitmaps_list;
    }

    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title_list.get(position);
    }

}
