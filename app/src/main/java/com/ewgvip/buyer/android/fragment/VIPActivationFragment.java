package com.ewgvip.buyer.android.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.bt_activation;
import static com.ewgvip.buyer.android.R.id.et_invitation_code;
import static com.ewgvip.buyer.android.R.id.et_mobile;
import static com.ewgvip.buyer.android.R.id.et_username;
import static com.ewgvip.buyer.android.R.id.et_weixin;
import static com.ewgvip.buyer.android.R.id.ll_invitation_code;
import static com.ewgvip.buyer.android.R.id.ll_vipleader_info;

/**
 * VIP激活
 */
public class VIPActivationFragment extends Fragment {

    @BindView(R.id.vipleader_img)
    SimpleDraweeView vipleaderImg;
    @BindView(R.id.tv_vipleader_truename)
    TextView tvVipleaderTruename;
    @BindView(R.id.tv_vipleader_invitation_code)
    TextView tvVipleaderInvitationCode;
    @BindView(ll_vipleader_info)
    LinearLayout llVipleaderInfo;
    @BindView(et_username)
    EditText etUsername;
    @BindView(et_weixin)
    EditText etWeixin;
    @BindView(et_mobile)
    EditText etMobile;
    @BindView(R.id.ll_mobile)
    LinearLayout llMobile;
    @BindView(R.id.tv_vip_select_sex)
    TextView tvVipSelectSex;

    @BindView(et_invitation_code)
    EditText etInvitationCode;

    @BindView(ll_invitation_code)
    LinearLayout llInvitationCode;
    @BindView(R.id.tv_e_vip_agree_doc)
    TextView tvEVipAgreeDoc;

    private int sex;
    //对话框
    Dialog alertDialog;
    private SharedPreferences preferences;
    private String myLeaderInvitationCode;
    private BaseActivity mActivity;
    private View rootView;
    private static VIPActivationFragment fragment = null;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView=null;
        this.alertDialog=null;
        this.mActivity=null;
        this.myLeaderInvitationCode=null;
        this.preferences=null;
        this.fragment=null;
    }

    //静态工厂方法
    public static VIPActivationFragment getInstance() {
        if (fragment == null) {
            fragment = new VIPActivationFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_vipactivation, container, false);
        ButterKnife.bind(this, rootView);
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        myLeaderInvitationCode = preferences.getString("MyLeaderInvitationCode", "");


        mActivity.setCache("scan_invitation_code", "");
        if (TextUtils.isEmpty(myLeaderInvitationCode)) {
            llVipleaderInfo.setVisibility(View.GONE);
            llInvitationCode.setVisibility(View.VISIBLE);
            init();
        } else {
            llVipleaderInfo.setVisibility(View.VISIBLE);
            llInvitationCode.setVisibility(View.GONE);
            getVipLeaderInfoByInvitationCode(myLeaderInvitationCode);
        }
        return rootView;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            etInvitationCode.setText(mActivity.getCache("scan_invitation_code"));
        } else {
            etInvitationCode.setText("");
        }
    }

    //请求上级邀请码，并获取上级信息
    private void init() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(getResources().getString(R.string.http_url) + "/app/buyer/buyer_parent_one.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("ret").equals("true")) {
                        String invitationCode = jsonObject.getString("invitation_code");
                        getVipLeaderInfoByInvitationCode(invitationCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //根据上级邀请码获取上级信息
    private void getVipLeaderInfoByInvitationCode(final String myLeaderInvitationCode) {
        if (!TextUtils.isEmpty(myLeaderInvitationCode)) {
            Map map = mActivity.getParaMap();
            map.put("invitation_code", myLeaderInvitationCode);
            RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(getResources().getString(R.string.http_url) + "/app/vip_my_parent_info.htm",map, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    try {
                        String ret = jsonObject.get("ret").toString();
                        if (ret.equals("1")) {
                            String trueName = "未找到该用户名";
                            if (jsonObject.has("true_name") && !TextUtils.isEmpty(jsonObject.get("true_name").toString().trim())) {
                                trueName = jsonObject.get("true_name").toString();
                            } else if (jsonObject.has("nick_name") && !TextUtils.isEmpty(jsonObject.get("nick_name").toString().trim())) {
                                trueName = jsonObject.get("nick_name").toString();
                            } else if (jsonObject.has("user_name") && !TextUtils.isEmpty(jsonObject.get("user_name").toString().trim())) {
                                trueName = jsonObject.get("user_name").toString();
                            }

                            String photoUrl = jsonObject.get("photo_url").toString();
                            TextView tv_vipleader_truename = (TextView) rootView.findViewById(R.id.tv_vipleader_truename);
                            TextView tv_vipleader_invitation_code = (TextView) rootView.findViewById(R.id.tv_vipleader_invitation_code);
                            SimpleDraweeView vipleader_img = (SimpleDraweeView) rootView.findViewById(R.id.vipleader_img);
                            tv_vipleader_truename.setText("姓名：" + trueName);
                            tv_vipleader_invitation_code.setText("邀请码：" + myLeaderInvitationCode);
                            etInvitationCode.setText(myLeaderInvitationCode);
                            vipleader_img.setImageURI(Uri.parse(photoUrl));
                            llVipleaderInfo.setVisibility(View.VISIBLE);
                        } else if (ret.equals("0")) {
                            Toast.makeText(mActivity, "找不到这个邀请码的用户 ", Toast.LENGTH_SHORT).show();
                            llVipleaderInfo.setVisibility(View.GONE);
                        } else if (ret.equals("-1")) {
                            Toast.makeText(mActivity, "未识别该二维码 ", Toast.LENGTH_SHORT).show();
                            llVipleaderInfo.setVisibility(View.GONE);
                        } else if (ret.equals("-2")) {
                            Toast.makeText(mActivity, "这个邀请码对应多个用户 ", Toast.LENGTH_SHORT).show();
                            llVipleaderInfo.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //显示性别对话框
    private void showSelectGenderAlertDialog() {
        String[] arr = {"男", "女", "保密"};
        alertDialog = MyDialog.showAlert(mActivity, "选择性别", arr, (adapterView, view, i, l) -> {

            if (i == 0) {
                sex = 1;
                tvVipSelectSex.setText("男");
            } else if (i == 1) {
                sex = 0;
                tvVipSelectSex.setText("女");
            } else if (i == 2) {
                sex = -1;
                tvVipSelectSex.setText("保密");
            }
            alertDialog.dismiss();
        });
    }

    //点击事件监听
    @OnClick({R.id.iv_back, R.id.bt_activation
            , R.id.scan_invitation_code,R.id.tv_vip_select_sex
            ,R.id.tv_e_vip_agree_doc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                getFragmentManager().popBackStack();
                mActivity.hide_keyboard(view);
                break;
            case R.id.scan_invitation_code://扫描二维码
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_to_qrscan2fragment();
                }
                break;
            case R.id.tv_vip_select_sex://选择性别
                if (FastDoubleClickTools.isFastDoubleClick()){
                    showSelectGenderAlertDialog();
                }
                break;
             case R.id.tv_e_vip_agree_doc://会员协议
                if (FastDoubleClickTools.isFastDoubleClick()){
                    Bundle bundle = new Bundle();
                    bundle.putString("type", WebFragment.JOIN_VIP_DOC);
                    mActivity.go_web(bundle);
                }
                break;
            case bt_activation://激活
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    activateVip();
                }
                break;
        }
    }

    //激活Vip
    public void activateVip() {
        String trueName = etUsername.getText().toString().trim();
        String wechat = etWeixin.getText().toString().trim();
        String vipsex = tvVipSelectSex.getText().toString().trim();
        final String mobile = etMobile.getText().toString().trim();
        final String invitationCode;
        if (TextUtils.isEmpty(myLeaderInvitationCode)) {
            invitationCode = etInvitationCode.getText().toString().trim();
        } else {
            invitationCode = myLeaderInvitationCode;
        }
        if (TextUtils.isEmpty(trueName)) {
            Toast.makeText(mActivity, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(wechat)) {
            Toast.makeText(mActivity, "微信号不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(vipsex)) {
            Toast.makeText(mActivity, "请选择性别", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(mobile)) {
        }
        final Map paraMap = mActivity.getParaMap();

        if (vipsex.equals("男")) {
            sex = 1;
        } else if (vipsex.equals("女")) {
            sex = 0;
        } else if (vipsex.equals("保密")) {
            sex = -1;
        }
        paraMap.put("trueName", trueName);
        paraMap.put("wechat ", wechat);
        paraMap.put("sex", sex + "");
        paraMap.put("invitationCode", invitationCode);

        if (!TextUtils.isEmpty(mobile)) {
            Map map = mActivity.getParaMap();
            map.put("mobile", mobile);
            RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/verify_mobile.htm", map, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    try {
                        Log.i("test", jsonObject.toString());
                        String code = jsonObject.getString("code");
                        if ("true".equals(code)) {
                            paraMap.put("mobile ", mobile);
                            RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/vip_save_set1.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                                        @Override
                                        public void onNext(JSONObject jsonObject) {
                                            try {
                                                String ret = jsonObject.get("ret").toString();
                                                if (ret.equals("1")) {
                                                    Toast.makeText(mActivity, "保存成功 ", Toast.LENGTH_SHORT).show();
                                                    preferences.edit().putString("vipIsJoin", "true").commit();
                                                    preferences.edit().putString("MyLeaderInvitationCode", invitationCode).commit();
                                                    toNextUI();
                                                } else if (ret.equals("-2")) {
                                                    Toast.makeText(mActivity, "邀请码为空 ", Toast.LENGTH_SHORT).show();
                                                } else if (ret.equals("-3")) {
                                                    Toast.makeText(mActivity, "邀请码错误 ", Toast.LENGTH_SHORT).show();
                                                } else if (ret.equals("-4")) {
                                                    Toast.makeText(mActivity, "系统异常 ", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        } else {
                            paraMap.put("mobile ", "");
                            RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/vip_save_set1.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                                @Override
                                public void onNext(JSONObject jsonObject) {
                                    try {
                                        String ret = jsonObject.get("ret").toString();
                                        if (ret.equals("1")) {
                                            Toast.makeText(mActivity, "保存成功 ", Toast.LENGTH_SHORT).show();
                                            preferences.edit().putString("vipIsJoin", "true").commit();
                                            preferences.edit().putString("MyLeaderInvitationCode", invitationCode).commit();
                                            toNextUI();
                                        } else if (ret.equals("-2")) {
                                            Toast.makeText(mActivity, "邀请码为空 ", Toast.LENGTH_SHORT).show();
                                        } else if (ret.equals("-3")) {
                                            Toast.makeText(mActivity, "邀请码错误 ", Toast.LENGTH_SHORT).show();
                                        } else if (ret.equals("-4")) {
                                            Toast.makeText(mActivity, "系统异常 ", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            paraMap.put("mobile ", "");
            RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/vip_save_set1.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    try {
                        String ret = jsonObject.get("ret").toString();
                        if (ret.equals("1")) {
                            Toast.makeText(mActivity, "保存成功 ", Toast.LENGTH_SHORT).show();
                            preferences.edit().putString("vipIsJoin", "true").commit();
                            preferences.edit().putString("MyLeaderInvitationCode", invitationCode).commit();
                            toNextUI();
                        } else if (ret.equals("-2")) {
                            Toast.makeText(mActivity, "邀请码为空 ", Toast.LENGTH_SHORT).show();
                        } else if (ret.equals("-3")) {
                            Toast.makeText(mActivity, "邀请码错误 ", Toast.LENGTH_SHORT).show();
                        } else if (ret.equals("-4")) {
                            Toast.makeText(mActivity, "系统异常 ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //激活后跳转到指定界面
    private void toNextUI() {
        Bundle bundle = getArguments();
        String nextToUi = bundle.getString("next_to_ui");
        if (nextToUi.equals("go_vip_level_up")) {
            mActivity.go_vip_level_up();
        } else if (nextToUi.equals("go_my_spread")) {
            mActivity.go_my_spread();
        } else if (nextToUi.equals("go_my_vip_leader")) {
            mActivity.go_my_vip_leader();
        } else if (nextToUi.equals("go_my_vip_reward_manage")) {
            mActivity.go_my_vip_reward_manage();
        } else if (nextToUi.equals("go_my_vip_team_manage")) {
            mActivity.go_my_vip_team_manage();
        } else if (nextToUi.equals("go_my_commission")) {
            mActivity.go_my_commission();
        } else if (nextToUi.equals("go_my_poster")) {
            mActivity.go_my_poster();
        } else if (nextToUi.equals("go_my_authorization")) {
            mActivity.go_my_authorization();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getFragmentManager().popBackStack();
    }
}
