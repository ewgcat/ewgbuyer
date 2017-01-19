package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.glideutils.GlideCircleTransform;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * VIP中心
 */
public class VIPCenterFragment extends Fragment {

    @BindView(R.id.vip_user_img)
    ImageView vipUserImg;
    @BindView(R.id.tv_vip_name)
    TextView tvVipName;
    @BindView(R.id.tv_vip_level)
    TextView tvVipLevel;
    @BindView(R.id.tv_vip_team_count)
    TextView tvVipTeamCount;
    @BindView(R.id.tv_vip_upgrade_bonus)
    TextView tvVipUpgradeBonus;
    @BindView(R.id.tv_vip_reward_yue)
    TextView tvVipRewardYue;

    private BaseActivity mActivity;
    private View rootView;
    private String vipIsJoin="false";
    private static VIPCenterFragment fragment = null;

    public VIPCenterFragment() {
    }

    //静态工厂方法
    public static VIPCenterFragment getInstance() {
        if (fragment == null) {
            fragment = new VIPCenterFragment();
        }
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView=null;
        this.mActivity=null;
        this.vipIsJoin=null;
        this.fragment=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_vip_center, container, false);
        ButterKnife.bind(this, rootView);
        isVipActivated();
        return rootView;
    }

    //可见就加载用户信息
    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    //服务器请求是否激活
    private void isVipActivated() {
        final SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        if (!TextUtils.isEmpty(user_id.trim()) && !TextUtils.isEmpty(token.trim())) {

            Map paraMap = mActivity.getParaMap();
            Random random = new Random(100);
            paraMap.put("random", random + "");
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request =new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/vip_set.htm", jsonObject -> {
                try {
                    vipIsJoin = jsonObject.get("state").toString();
                    if ("disable".equals(vipIsJoin)) {
                        Toast.makeText(mActivity, "您的VIP服务已禁用", Toast.LENGTH_SHORT).show();
                        CommonUtil.getHandler().postDelayed(() -> mActivity.onBackPressed(), 1000);
                    } else {
                        preferences.edit().putString("vipIsJoin", vipIsJoin).commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Log.i("test",error.toString()),paraMap);
            mRequestQueue.add(request);


        }
    }

    // 加载用户信息
    public void init() {
        final SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        if (!TextUtils.isEmpty(user_id.trim()) && !TextUtils.isEmpty(token.trim())) {
            Map paraMap = mActivity.getParaMap();
            Random random = new Random(100);
            paraMap.put("random", random + "");
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request =new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/index.htm", jsonObject -> {
                try {
                    if (jsonObject.has("ret")&&jsonObject.get("ret").toString().equals("true")) {
                        String userName = jsonObject.get("user_name").toString();
                        String urlString = (String) jsonObject.get("photo_url");
                        //加载头像
                        Glide.with(mActivity).load(urlString).transform(new GlideCircleTransform(mActivity)).into(vipUserImg);

                        // 加载姓名
                        tvVipName.setText(userName);
                        // 加载用户等级
                        tvVipLevel.setText(jsonObject.get("level_name").toString());
                        // 加载团队人数
                        tvVipTeamCount.setText(jsonObject.get("team_count").toString());
                        // 加载升级奖励
                        tvVipUpgradeBonus.setText("¥" + jsonObject.get("child_reward").toString());
                        // 加载返佣收益
                        tvVipRewardYue.setText("¥" + jsonObject.get("commission").toString());

                        //保存到首选项
                        mActivity.setCache("user_image_photo_url", urlString);
                        mActivity.setCache("userName", userName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Log.i("test",error.toString()),paraMap);
            mRequestQueue.add(request);

        }
    }

    //点击事件监听
    @OnClick({R.id.iv_back, R.id.rl_my_spread
            , R.id.rl_vip_level_up, R.id.rl_my_vip_leader
            , R.id.rl_reward_manage, R.id.rl_team_manage
            , R.id.rl_child_buy_goods_reward, R.id.rl_my_post
            , R.id.rl_auth_license})
    public void onClick(View view) {
        String vipIsJoin = mActivity.getCache("vipIsJoin");
        if (TextUtils.isEmpty(vipIsJoin.trim())){
            vipIsJoin="false";
        }
        switch (view.getId()) {
            case R.id.iv_back:   //左边返回箭头
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.rl_my_spread://推广界面
                if ("true".equals(vipIsJoin)) {//激活了，跳转到推广界面
                    mActivity.go_my_spread();
                } else {//未激活    跳转到激活界面
                    Bundle bundle = new Bundle();
                    bundle.putString("next_to_ui", "go_my_spread");
                    mActivity.go_activite_vip(bundle);
                }
                break;
            case R.id.rl_vip_level_up://Vip升级
                if ("true".equals(vipIsJoin)) {//激活了，跳转到Vip升级
                    mActivity.go_vip_level_up();
                } else {//未激活    跳转到激活界面
                    Bundle bundle = new Bundle();
                    bundle.putString("next_to_ui", "go_vip_level_up");
                    mActivity.go_activite_vip(bundle);
                }
                break;
            case R.id.rl_my_vip_leader://我的上级
                if ("true".equals(vipIsJoin)) {//激活了，跳转到我的上级
                    mActivity.go_my_vip_leader();
                } else {//未激活    跳转到激活界面
                    Bundle bundle = new Bundle();
                    bundle.putString("next_to_ui", "go_my_vip_leader");
                    mActivity.go_activite_vip(bundle);
                }
                break;
            case R.id.rl_reward_manage://奖励管理
                if ("true".equals(vipIsJoin)) {//激活了，跳转奖励管理
                    mActivity.go_my_vip_reward_manage();
                } else {//未激活    跳转到激活界面
                    Bundle bundle = new Bundle();
                    bundle.putString("next_to_ui", "go_my_vip_reward_manage");
                    mActivity.go_activite_vip(bundle);
                }
                break;
            case R.id.rl_team_manage://团队管理
                if ("true".equals(vipIsJoin)) {//激活了，跳转到我的团队管理
                    mActivity.go_my_vip_team_manage();
                } else {//未激活    跳转到激活界面
                    Bundle bundle = new Bundle();
                    bundle.putString("next_to_ui", "go_my_vip_team_manage");
                    mActivity.go_activite_vip(bundle);
                }
                break;
            case R.id.rl_child_buy_goods_reward://返佣收益
                if ("true".equals(vipIsJoin)) {//激活了，跳转到返佣收益
                    mActivity.go_my_commission();
                } else {//未激活   跳转到激活界面
                    Bundle bundle = new Bundle();
                    bundle.putString("next_to_ui", "go_my_commission");
                    mActivity.go_activite_vip(bundle);
                }
                break;
            case R.id.rl_my_post://我的海报
                if ("true".equals(vipIsJoin)) {//激活了，跳转我的海报
                    mActivity.go_my_poster();
                } else {//未激活   跳转到激活界面
                    Bundle bundle = new Bundle();
                    bundle.putString("next_to_ui", "go_my_poster");
                    mActivity.go_activite_vip(bundle);
                }
                break;
            case R.id.rl_auth_license:
                if ("true".equals(vipIsJoin)) {//激活了，跳转我的授权书
                    mActivity.go_my_authorization();
                } else {//未激活   跳转到激活界面
                    Bundle bundle = new Bundle();
                    bundle.putString("next_to_ui", "go_my_authorization");
                    mActivity.go_activite_vip(bundle);
                }
                break;
        }
    }

}
