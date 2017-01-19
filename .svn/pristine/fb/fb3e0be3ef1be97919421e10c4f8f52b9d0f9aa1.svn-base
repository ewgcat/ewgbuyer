package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.check_kk;
import static com.ewgvip.buyer.android.R.id.check_yy;
import static com.ewgvip.buyer.android.R.id.check_zz;
import static com.ewgvip.buyer.android.R.id.iv_kk;
import static com.ewgvip.buyer.android.R.id.iv_yy;
import static com.ewgvip.buyer.android.R.id.iv_zz;
import static com.ewgvip.buyer.android.R.id.ll_iv_kk;
import static com.ewgvip.buyer.android.R.id.ll_iv_yy;
import static com.ewgvip.buyer.android.R.id.ll_iv_zz;
import static com.ewgvip.buyer.android.R.id.ll_vipleader_info;
import static com.ewgvip.buyer.android.R.id.tv_pay_money;
import static com.ewgvip.buyer.android.R.id.tv_vipleader_invitation_code;
import static com.ewgvip.buyer.android.R.id.tv_vipleader_truename;
import static com.ewgvip.buyer.android.R.id.vipleader_img;

/**
 *
 */
public class MyVipLevelUpFragment extends Fragment {


    @BindView(vipleader_img)
    SimpleDraweeView vipleaderImg;
    @BindView(tv_vipleader_truename)
    TextView tvVipleaderTruename;
    @BindView(tv_vipleader_invitation_code)
    TextView tvVipleaderInvitationCode;
    @BindView(ll_vipleader_info)
    LinearLayout llVipleaderInfo;
    @BindView(R.id.tv_current_level)
    TextView tvCurrentLevel;
    @BindView(iv_yy)
    ImageView ivYy;
    @BindView(ll_iv_yy)
    LinearLayout llIvYy;
    @BindView(check_yy)
    ImageView checkYy;
    @BindView(iv_kk)
    ImageView ivKk;
    @BindView(ll_iv_kk)
    LinearLayout llIvKk;
    @BindView(check_kk)
    ImageView checkKk;
    @BindView(iv_zz)
    ImageView ivZz;
    @BindView(ll_iv_zz)
    LinearLayout llIvZz;
    @BindView(check_zz)
    ImageView checkZz;
    @BindView(tv_pay_money)
    TextView tvPayMoney;
    @BindView(R.id.ll_pay_money)
    LinearLayout llPayMoney;

    private BaseActivity mActivity;
    private View rootView;

    private String up_price_1;
    private String up_price_2;
    private String up_price_3;
    private String current_vip_grade;
    private String myLeaderInvitationCode;

    private String Order_price = "";
    private SharedPreferences preferences;
    private String level_name;
    private String up_price_1_remind;
    private String up_price_2_remind;
    private String up_price_3_remind;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.mActivity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_vip_level_up, container, false);
        ButterKnife.bind(this, rootView);
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);


        return rootView;
    }

    private void initView() {
        tvCurrentLevel.setText("当前等级：" + level_name);
        llIvZz.setBackgroundResource(R.drawable.level_up_select_bg);
        llIvKk.setBackgroundResource(R.drawable.level_up_unselect_bg);
        llIvYy.setBackgroundResource(R.drawable.level_up_unselect_bg);

        ivYy.setOnClickListener(v -> {
            llIvYy.setBackgroundResource(R.drawable.level_up_select_bg);
            llIvKk.setBackgroundResource(R.drawable.level_up_unselect_bg);
            llIvZz.setBackgroundResource(R.drawable.level_up_unselect_bg);
            checkYy.setVisibility(View.VISIBLE);
            checkKk.setVisibility(View.INVISIBLE);
            checkZz.setVisibility(View.INVISIBLE);

            current_vip_grade = "SILVER";
            Order_price = up_price_1;
            if (up_price_1_remind!=null){
                tvPayMoney.setText(up_price_1_remind );
            }

        });
        ivKk.setOnClickListener(v -> {
            llIvKk.setBackgroundResource(R.drawable.level_up_select_bg);
            llIvYy.setBackgroundResource(R.drawable.level_up_unselect_bg);
            llIvZz.setBackgroundResource(R.drawable.level_up_unselect_bg);
            checkYy.setVisibility(View.INVISIBLE);
            checkKk.setVisibility(View.VISIBLE);
            checkZz.setVisibility(View.INVISIBLE);
            Order_price = up_price_2;
            current_vip_grade = "GOLD";
            if (up_price_2_remind!=null){
                tvPayMoney.setText(up_price_2_remind );
            }

        });
        ivZz.setOnClickListener(v -> {
            llIvZz.setBackgroundResource(R.drawable.level_up_select_bg);
            llIvKk.setBackgroundResource(R.drawable.level_up_unselect_bg);
            llIvYy.setBackgroundResource(R.drawable.level_up_unselect_bg);
            checkYy.setVisibility(View.INVISIBLE);
            checkKk.setVisibility(View.INVISIBLE);
            checkZz.setVisibility(View.VISIBLE);
            current_vip_grade = "DIAMOND";
            Order_price = up_price_3;
            if (up_price_3_remind!=null){
                tvPayMoney.setText(up_price_3_remind );
            }

        });
        if (level_name.equals("钻卡会员")) {
            ivYy.setClickable(false);
            ivKk.setClickable(false);
            ivZz.setClickable(false);
        } else if (level_name.equals("普通会员")) {
            ivYy.setClickable(true);
            ivKk.setClickable(true);
            ivZz.setClickable(true);
        } else if (level_name.equals("银卡会员")) {
            ivYy.setClickable(false);
            ivKk.setClickable(true);
            ivZz.setClickable(true);
        } else if (level_name.equals("金卡会员")) {
            ivYy.setClickable(false);
            ivKk.setClickable(false);
            ivZz.setClickable(true);
        }
    }

    //根据邀请码获取上级信息
    private void getVipLeaderInfoByInvitationCode(final String myLeaderInvitationCode) {
        if (!TextUtils.isEmpty(myLeaderInvitationCode)) {
            Map paraMap = mActivity.getParaMap();
            paraMap.put("invitation_code", myLeaderInvitationCode);
            RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/vip_my_parent_info.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    try {
                        String ret = jsonObject.get("ret").toString();
                        if (ret.equals("1")) {
                            String trueName = "未找到该数据";
                            if (jsonObject.has("true_name") && !TextUtils.isEmpty(jsonObject.get("true_name").toString().trim())) {
                                trueName = jsonObject.get("true_name").toString();
                            } else if (jsonObject.has("nick_name") && !TextUtils.isEmpty(jsonObject.get("nick_name").toString().trim())) {
                                trueName = jsonObject.get("nick_name").toString();
                            } else if (jsonObject.has("use_name") && !TextUtils.isEmpty(jsonObject.get("use_name").toString().trim())) {
                                trueName = jsonObject.get("use_name").toString();
                            }
                            String photoUrl = jsonObject.get("photo_url").toString();

                            tvVipleaderTruename.setText("姓名：" + trueName);
                            tvVipleaderInvitationCode.setText("邀请码：" + myLeaderInvitationCode);
                            preferences.edit().putString("MyLeaderInvitationCode", myLeaderInvitationCode).commit();
                            vipleaderImg.setImageURI(Uri.parse(photoUrl));
                            llVipleaderInfo.setVisibility(View.VISIBLE);
                        } else if (ret.equals("0")) {
                            CommonUtil.showSafeToast(mActivity, "找不到这个邀请码的用户");
                        } else if (ret.equals("-1")) {
                            CommonUtil.showSafeToast(mActivity, "未识别该二维码");
                        } else if (ret.equals("-2")) {
                            CommonUtil.showSafeToast(mActivity, "这个邀请码对应多个用户");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //获取升级需要支付金额
    private void getlevel_up_price() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/vip_level_up.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    Log.i("test",jsonObject.toString());
                    String ret = jsonObject.get("ret").toString();
                    String currentvipgrade = jsonObject.get("current_vip_grade").toString();

                    if ("GENERAL".equals(currentvipgrade)){
                        level_name = "普通会员";
                    }else if ("SILVER".equals(currentvipgrade)){
                        level_name = "银卡会员";
                    }else if ("GOLD".equals(currentvipgrade)){
                        level_name = "金卡会员";
                    }else if ("DIAMOND".equals(currentvipgrade)){
                        level_name = "钻卡会员";
                    }
                    if ("1".equals(ret)) {
                        if ("钻卡会员".equals(level_name)) {
                            llPayMoney.setVisibility(View.GONE);
                        } else {
                            if ("普通会员".equals(level_name)) {
                                up_price_1 = "" + jsonObject.getString("up_price_1");
                                up_price_2 = jsonObject.getString("up_price_2") + "";
                                up_price_3 = jsonObject.getString("up_price_3") + "";

                                if (jsonObject.has("up_price_1_remind")){
                                    up_price_1_remind = "" + jsonObject.getString("up_price_1_remind");
                                    up_price_2_remind = "" + jsonObject.getString("up_price_2_remind");
                                    up_price_3_remind = "" + jsonObject.getString("up_price_3_remind");
                                }

                            } else if ("银卡会员".equals(level_name)) {
                                up_price_2 = jsonObject.getString("up_price_2") + "";
                                up_price_3 = jsonObject.getString("up_price_3") + "";
                                if (jsonObject.has("up_price_2_remind")){
                                    up_price_2_remind = "" + jsonObject.getString("up_price_2_remind");
                                    up_price_3_remind = "" + jsonObject.getString("up_price_3_remind");
                                }

                            } else if ("金卡会员".equals(level_name)) {
                                up_price_3 = jsonObject.getString("up_price_3") + "";
                                if (jsonObject.has("up_price_3_remind")){
                                    up_price_3_remind = "" + jsonObject.getString("up_price_3_remind");
                                }

                            }
                            llIvZz.setBackgroundResource(R.drawable.level_up_select_bg);
                            llIvKk.setBackgroundResource(R.drawable.level_up_unselect_bg);
                            llIvYy.setBackgroundResource(R.drawable.level_up_unselect_bg);
                            checkYy.setVisibility(View.INVISIBLE);
                            checkKk.setVisibility(View.INVISIBLE);
                            checkZz.setVisibility(View.VISIBLE);
                            Order_price = up_price_3;
                            current_vip_grade = "DIAMOND";
                            if (jsonObject.has("up_price_3_remind")){
                                tvPayMoney.setText(up_price_3_remind );
                            }


                            }
                        initView();

                    } else if ("-3".equals(ret)) {
                        CommonUtil.showSafeToast(mActivity, "vip服务已被禁用");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //提交升级
    private void commitLevelUp() {
        final String totalprice = Order_price;
        Map paraMap = mActivity.getParaMap();
        paraMap.put("order_type ", "android");
        paraMap.put("current_vip_grade", current_vip_grade);
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/vip_level_up_save.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    String ret = jsonObject.get("ret").toString();
                    if ("1".equals(ret)) {//跳转到在线支付界面
                        Bundle pay_bundle = new Bundle();
                        pay_bundle.putString("order_id", jsonObject.get("id").toString());
                        pay_bundle.putString("order_num", jsonObject.get("order_id").toString());
                        pay_bundle.putString("totalPrice", totalprice);
                        pay_bundle.putString("isLevelUpVip", "true");
                        mActivity.go_pay(pay_bundle, "payonline");
                    } else if ("-1".equals(ret)) {
                        Toast.makeText(mActivity, "等级状态异常", Toast.LENGTH_SHORT).show();
                    } else if ("-2".equals(ret)) {
                        Toast.makeText(mActivity, "系统异常", Toast.LENGTH_SHORT).show();
                    } else if ("-3".equals(ret)) {
                        Toast.makeText(mActivity, "用户等级异常", Toast.LENGTH_SHORT).show();
                    } else if ("-4".equals(ret)) {
                        Toast.makeText(mActivity, "系统异常", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //初始化推荐人信息和升级金额
    private void init() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/buyer_parent_one.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("ret").equals("true")) {
                        if ( jsonObject.has("invitation_code")&&!TextUtils.isEmpty(jsonObject.get("invitation_code").toString().trim())){
                            myLeaderInvitationCode = jsonObject.get("invitation_code").toString();
                            getVipLeaderInfoByInvitationCode(myLeaderInvitationCode);
                        }else {
                            llVipleaderInfo.setVisibility(View.GONE);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getlevel_up_price();
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

    //点击事件监听
    @OnClick({R.id.iv_back, R.id.bt_level_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.bt_level_up:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    commitLevelUp();
                }
                break;
        }
    }
}
