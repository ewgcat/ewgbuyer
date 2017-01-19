package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.tv_vip_leader_username;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyVipLeaderFragment extends Fragment {


    @BindView(R.id.vip_leader_img)
    SimpleDraweeView vipLeaderImg;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(tv_vip_leader_username)
    TextView tvVipLeaderUsername;
    @BindView(R.id.tv_vip_leader_wetchat)
    TextView tvVipLeaderWetchat;
    @BindView(R.id.tv_vip_leader_qq)
    TextView tvVipLeaderQq;
    @BindView(R.id.tv_vip_leader_phone)
    TextView tvVipLeaderPhone;
    @BindView(R.id.tv_vip_leader_create_time)
    TextView tvVipLeaderCreateTime;
    @BindView(R.id.tv_vip_leader_create_by_way)
    TextView tvVipLeaderCreateByWay;
    private BaseActivity mActivity;
    private View rootView;



    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_vip_leader, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    private void init() {
        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/buyer_parent_one.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("ret").equals("true")) {
                        String result = jsonObject.toString();
                        if (result.contains("photo_url")) {
                            vipLeaderImg.setImageURI(Uri.parse(jsonObject.get("photo_url").toString()));
                        }
                        if (result.contains("nick_name")) {
                            tvNickname.setText(jsonObject.get("nick_name").toString());
                        }
                        if (result.contains("user_name")) {
                            tvVipLeaderUsername.setText(jsonObject.get("user_name").toString());
                        }
                        if (result.contains("wechat")) {
                            tvVipLeaderWetchat.setText(jsonObject.get("wechat").toString());
                        }
                        if (result.contains("qq")) {
                            tvVipLeaderQq.setText(jsonObject.get("qq").toString());
                        }
                        if (result.contains("mobile")) {
                            tvVipLeaderPhone.setText(jsonObject.get("mobile").toString());
                        }
                        if (result.contains("vip_link_time")) {
                            tvVipLeaderCreateTime.setText(jsonObject.get("vip_link_time").toString());
                        }
                        if (result.contains("vip_link_type_name")) {
                            tvVipLeaderCreateByWay.setText(jsonObject.get("vip_link_type_name").toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        getFocus();
    }

    //主界面获取焦点
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Intent intent = new Intent();
                getFragmentManager().popBackStack();
                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 100, intent);
                }
                return true;
            }
            return false;
        });
    }


    @OnClick(R.id.iv_back)
    public void onClick(View view) {
        if (FastDoubleClickTools.isFastDoubleClick()) {
            mActivity.onBackPressed();
            mActivity.hide_keyboard(view);
        }
    }
}
