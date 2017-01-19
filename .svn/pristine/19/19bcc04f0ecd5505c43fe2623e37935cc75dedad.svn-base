package com.ewgvip.buyer.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.List;

/**
 * Created by Administrator on 2015/10/23.
 */
public class FragmentTabPagerAdapter extends FragmentStatePagerAdapter {

    List<String> title_list;
    List<Fragment> fragment_list;

    public FragmentTabPagerAdapter(FragmentManager fm, List<Fragment> fragment_list, List<String> title_list) {
        super(fm);
        this.title_list = title_list;
        this.fragment_list = fragment_list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return title_list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title_list.get(position);
    }
}

