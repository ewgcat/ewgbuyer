package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.layout.BadgeView;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.user_info;

/**
 * 个人中心
 */
public class UserCenterFragment extends Fragment {

    //用户信息
    @BindView(user_info)
    LinearLayout userInfo;
    @BindView(R.id.user_detail)
    RelativeLayout userDetail;
    @BindView(R.id.user_img)
    SimpleDraweeView userImg;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.user_level)
    TextView userLevel;
    //关注
    @BindView(R.id.tv_like_goods)
    TextView tvLikeGoods;
    @BindView(R.id.tv_like_shop)
    TextView tvLikeShop;
    @BindView(R.id.tv_like_hoby)
    TextView tvLikeHoby;
    // 未读消息数量提示
    private View barge;
    private View rootView;
    private BaseActivity mActivity;
    public static BadgeView badge;
    private static UserCenterFragment fragment = null;
    String str = "";
    private View rl_tiyanvip;

    public UserCenterFragment() {
    }

    //静态工厂方法
    public static UserCenterFragment getInstance() {
        if (fragment == null) {
            fragment = new UserCenterFragment();
        }
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.barge = null;
        this.badge = null;
        this.fragment = null;
        this.mActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_user_center, container, false);
        barge = rootView.findViewById(R.id.barge);
        badge = new BadgeView(mActivity, barge);
        ButterKnife.bind(this, rootView);
        rl_tiyanvip = rootView.findViewById(R.id.rl_tiyanvip);



        return rootView;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initUnReadMessageNum();
            init();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initUnReadMessageNum();
        init();
    }

    //初始化用户信息
    private void init() {
        if (mActivity.islogin()) {
            // 加载用户信息
            Map paraMap = mActivity.getParaMap();
            Random random = new Random(100);
            paraMap.put("random", "" + random);
            RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/index.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    try {
                        Log.i("test",jsonObject.toString());
                        if (!str.equals(jsonObject.toString())&&jsonObject.get("ret").toString().equals("true")) {
                            str=jsonObject.toString();
                            mActivity.setCache("userbalance", jsonObject.getString("balance") + "");
                            mActivity.setCache("amount_income", jsonObject.getString("amount_income") + "");
                            mActivity.setCache("extract_cash", jsonObject.getString("extract_cash") + "");
                            mActivity.setCache("grade_name", jsonObject.getString("grade_name") + "");
                            mActivity.setCache("promotion_income", jsonObject.getString("promotion_income") + "");

                            mActivity.setCache("integral", jsonObject.getString("integral") + "");
                            tvLikeGoods.setText(jsonObject.getString("favoutite_goods_count") + "\n关注的商品");
                            tvLikeShop.setText(jsonObject.getString("favoutite_store_count") + "\n关注的店铺");
                            if (jsonObject.has("footPoint_count")) {
                                tvLikeHoby.setText(jsonObject.getString("footPoint_count") + "\n足迹");
                            } else {
                                tvLikeHoby.setText(0 + "\n足迹");
                            }
                            // 加载头像
                            String urlString = jsonObject.getString("photo_url");
                            userImg.setImageURI(Uri.parse(urlString));
                            mActivity.setCache("user_image_photo_url", urlString);
                            // 加载姓名
                            String userName = jsonObject.getString("user_name").toString();
                            mActivity.setCache("user_name", userName);

                            String vip_expired_time="";

                            if (jsonObject.has("vip_expired_time")){
                              vip_expired_time = jsonObject.getString("vip_expired_time").toString();
                            }

                            if (TextUtils.isEmpty(vip_expired_time.trim())){
                                userLevel.setText(jsonObject.getString("level_name"));
                            }else {
                                userLevel.setText( jsonObject.getString("level_name")+"\n有效期至"+vip_expired_time);
                            }
                            username.setText(userName);

                            userDetail.setVisibility(View.VISIBLE);

                            if (jsonObject.has("vip_experience")) {
                                if (jsonObject.getString("vip_experience").equals("EXPERIENCING")
                                        || jsonObject.getString("vip_experience").equals("ALREADY_EXPERIENCE")
                                        || jsonObject.getString("level_name").equals("银卡会员")
                                        || jsonObject.getString("level_name").equals("金卡会员")
                                        || jsonObject.getString("level_name").equals("钻卡会员")) {
                                    rl_tiyanvip.setVisibility(View.GONE);
                                } else {
                                    rl_tiyanvip.setVisibility(View.VISIBLE);
                                }
                            }
                            //保存邀请码、二维码图片路径 到本地首选项
                            String invitation_code = jsonObject.getString("invitation_code").toString();
                            String qrcode_img_path = jsonObject.getString("qrcode_img_path").toString();
                            String level_name = jsonObject.getString("level_name").toString();

                            SharedPreferences.Editor editor = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                            if (jsonObject.has("mobile")) {
                                String mobile = jsonObject.getString("mobile").toString();
                                editor.putString("mobile", mobile);
                            }else {
                                editor.putString("mobile", "");
                            }
                            if (jsonObject.has("true_name")) {
                                String true_name = jsonObject.getString("true_name").toString();
                                editor.putString("true_name", true_name);
                            }else {
                                editor.putString("true_name", "");
                            }
                            if (jsonObject.has("vip_experience_slogan")){
                                editor.putString("vip_experience_slogan", jsonObject.getString("vip_experience_slogan"));
                            }else {
                                editor.putString("vip_experience_slogan","");
                            }
                            editor.putString("invitation_code", invitation_code);
                            editor.putString("qrcode_img_path", qrcode_img_path);
                            editor.putString("level_name", level_name);
                            editor.commit();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Uri uri = Uri.parse("res://com.ewgvip.buyer.android/" + R.mipmap.handimage);
            userImg.setImageURI(uri);
            tvLikeGoods.setText("关注的商品");
            tvLikeShop.setText("关注的店铺");
            tvLikeHoby.setText("足迹");
            // 加载姓名
            username.setText("");
            userLevel.setText("");
            userDetail.setVisibility(View.GONE);
        }
    }

    // 未读消息数量提示提醒
    public static void setbadge(int num) {
        if (badge.isShown())
            badge.hide();
        if (num > 0) {
            badge.setText("" + num);
            badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badge.toggle();
        }
    }

    //未读消息请求
    private void initUnReadMessageNum() {
        if (mActivity.islogin()) {
            Map paramap = mActivity.getParaMap();
            paramap.put("beginCount", "0");
            paramap.put("selectCount", "100");
            RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/message.htm", paramap, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    try {
                        //Log.i("test",jsonObject.toString());
                        if (jsonObject != null && jsonObject.getString("ret").equals("true")) {
                            JSONArray msg_list = jsonObject.getJSONArray("msg_list");
                            int lenght = msg_list.length();
                            int unreadmessageNum = 0;
                            if (lenght > 0) {
                                for (int i = 0; i < lenght; i++) {
                                    JSONObject obj = msg_list.getJSONObject(i);
                                    String status = obj.getString("status");
                                    if (status.equals("0")) {
                                        unreadmessageNum += 1;
                                    }
                                }
                            }
                            if (unreadmessageNum > 0) {
                                setbadge(unreadmessageNum);
                            } else {
                                setbadge(0);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    //点击事件监听
    @OnClick({R.id.setting, R.id.message, user_info, R.id.tv_like_goods
            , R.id.tv_like_shop, R.id.tv_like_hoby, R.id.order_all
            , R.id.rl_payment, R.id.rl_send_goods
            , R.id.rl_take_delivery, R.id.rl_assess
            , R.id.rl_customer_service, R.id.rl_wallet
            , R.id.rl_balance, R.id.rl_integration
            , R.id.rl_coupon, R.id.rl_free
            , R.id.rl_group_life, R.id.addressManage
            , R.id.account_security, R.id.service_center
            , R.id.vip_center, R.id.rl_redpackage
            ,  R.id.rl_exchangevip, R.id.rl_tiyanvip, R.id.rl_spread_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting://设置
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_setting();
                }
                break;
            case R.id.message://消息
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_message_list();
                }
                break;
            case user_info://跳转用户信息的修改页面
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_user_center_information_change();
                }
                break;
            case R.id.tv_like_goods://跳转到关注的商品页面
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", 0);
                    mActivity.go_myconcern(bundle);
                }
                break;
            case R.id.tv_like_shop:// 跳转到关注的店铺页面
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", 1);
                    mActivity.go_myconcern(bundle);
                }
                break;
            case R.id.tv_like_hoby:// 跳转到足迹
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_footpoint();
                    }
                }
                break;
            case R.id.order_all://跳转到订单
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", 0);
                    mActivity.go_orders_all(bundle);
                }
                break;
            case R.id.rl_payment://待付款
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", 1);
                    mActivity.go_orders_all(bundle);
                }
                break;
            case R.id.rl_send_goods://待发货
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", 2);
                    mActivity.go_orders_all(bundle);
                }
                break;
            case R.id.rl_take_delivery://待收货
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", 3);
                    mActivity.go_orders_all(bundle);
                }
                break;
            case R.id.rl_assess://待评价
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_order2evaluate();
                }
                break;
            case R.id.rl_customer_service://退款售后
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_order_goods_return_list();
                }
                break;
            case R.id.rl_wallet://我的钱包
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_wallet();
                }
                break;
            case R.id.rl_balance://余额
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_balance_detail();
                }
                break;
            case R.id.rl_integration://积分
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_integral_detail();
                }
                break;
            case R.id.rl_coupon://优惠券
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_coupons();
                }
                break;
            case R.id.rl_free://我的试用 0元购
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_order_free_list();
                }
                break;
            case R.id.rl_group_life://生活惠 团购
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_order_group_list();
                }
                break;
            case R.id.addressManage://地址管理
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    mActivity.go_address_list(bundle, mActivity.getCurrentfragment());
                }
                break;
            case R.id.account_security://账户安全
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_account_security();
                }
                break;
            case R.id.service_center://服务中心
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_service_center();
                }
                break;
            case R.id.vip_center://会员中心
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_vip_center();
                }
                break;
            case R.id.rl_redpackage://我的红包
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_my_red_package();
                }
                break;
            case R.id.rl_exchangevip://兑换会员
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_exchangevip();
                }
                break;
             case R.id.rl_tiyanvip://体验会员
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_tiyanvip();
                }
                break;
            case R.id.rl_spread_center://体验会员
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_spreadcenter();
                }
                break;

            default:
                break;
        }
    }
}
