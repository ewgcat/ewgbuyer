package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.iv_user_photo;
import static com.ewgvip.buyer.android.R.id.tv_date;
import static com.ewgvip.buyer.android.R.id.tv_money_count;
import static com.ewgvip.buyer.android.R.id.tv_name;
import static com.ewgvip.buyer.android.R.id.tv_red_package_amount;
import static com.ewgvip.buyer.android.R.id.tv_red_package_message;
import static com.ewgvip.buyer.android.R.id.tv_total_money;

/**
 * 红包余额页面
 */
public class MyRedPackageBalanceFragment extends Fragment {

    @BindView(tv_red_package_message)
    TextView tvRedPackageMessage;
    @BindView(tv_money_count)
    TextView tvMoneyCount;
    @BindView(tv_total_money)
    TextView tvTotalMoney;
    @BindView(iv_user_photo)
    ImageView ivUserPhoto;
    @BindView(tv_name)
    TextView tvName;
    @BindView(tv_date)
    TextView tvDate;
    @BindView(tv_red_package_amount)
    TextView tvRedPackageAmount;
    private MainActivity mActivity;
    private View rootView;

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity=null;
        this.rootView=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_red_package_balance, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();

        Glide.with(mActivity).load(bundle.getString("iv_user_photo")).into(ivUserPhoto);
        tvRedPackageMessage.setText(bundle.getString("tv_red_package_message"));
        tvName.setText(bundle.getString("tv_name"));
        tvDate.setText( bundle.getString("tv_date"));
        tvMoneyCount.setText(mActivity.moneytodouble(bundle.getString("tv_money_count")) + "元");
        tvRedPackageAmount.setText(mActivity.moneytodouble(bundle.getString("tv_money_count")) + "元");
        tvTotalMoney.setText("一个红包共" + mActivity.moneytodouble(bundle.getString("tv_money_count")) + "元");

        return rootView;
    }

    @OnClick({R.id.iv_back, R.id.go_to_yu_e})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.go_to_yu_e:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_balance_detail();
                }
                break;
        }
    }
}
