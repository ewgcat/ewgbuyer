package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.MainActivity;

/**
 * Created by lenovo on 2017/1/13.
 */
public class SpreadCardLevelManageFragment extends Fragment {

    private MainActivity mActivity;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_poster, container, false);

        return rootView;

    }
}
