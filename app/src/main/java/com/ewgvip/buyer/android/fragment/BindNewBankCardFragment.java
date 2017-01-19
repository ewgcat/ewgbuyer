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
import android.widget.EditText;
import android.widget.ImageView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPost3Request;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BindNewBankCardFragment extends Fragment {

    private static BindNewBankCardFragment fragment;
    private BaseActivity mActivity;
    private View rootView;
    private SharedPreferences preferences;
    private String pay_password;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
    }


    public BindNewBankCardFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (BaseActivity)getActivity();
        rootView = inflater.inflate(R.layout.fragment_bind_new_bank_card, container, false);
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        initView();

        return rootView;
    }

    private void initView() {
        //返回键
        ImageView iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });

        //保存
        rootView.findViewById(R.id.bt_save_bind_bank_card_info).setOnClickListener(v -> save());
    }

    private void save() {

        EditText et_my_name = (EditText) rootView.findViewById(R.id.et_my_name);
        EditText et_my_bank_card_number = (EditText) rootView.findViewById(R.id.et_my_bank_card_number);
        EditText et_pay_passord = (EditText) rootView.findViewById(R.id.et_pay_passord);
        EditText et_bank_bind_phone = (EditText) rootView.findViewById(R.id.et_bank_bind_phone);
        final String user_name = et_my_name.getText().toString().trim();
        final String card_number = et_my_bank_card_number.getText().toString().trim();
        final String passwd = et_pay_passord.getText().toString().trim();
        final String mobile = et_bank_bind_phone.getText().toString().trim();

        if (TextUtils.isEmpty(user_name)){
           CommonUtil.showSafeToast(mActivity,"持卡人姓名不能为空，请输入哦");
            return;
        }

        if (TextUtils.isEmpty(passwd)){
            CommonUtil.showSafeToast(mActivity,"支付密码姓名不能为空，请输入哦");
            return;
        }
        if (TextUtils.isEmpty(mobile)){
            CommonUtil.showSafeToast(mActivity, "手机号不能为空，请输入哦");
            return;
        }
        if (TextUtils.isEmpty(card_number)){
           CommonUtil.showSafeToast(mActivity,"银行卡号不能为空，请输入哦");
            return;
        }else{
            final Map paramap = new HashMap();
            paramap.put("card_number", card_number);
            final RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();


            Request<Boolean> requestVerifyBankCard=new NormalPost3Request(mActivity, getResources().getString(R.string.http_url) + "/verify_bankcard.htm", response -> {
                if (response.toString().equals("true")){
                    if (mobile.matches("1[3|4|5|7|8|][0-9]{9}")){
                        String user_id = preferences.getString("user_id", "");
                        String token = preferences.getString("token", "");
                        Map paraMap = new HashMap();
                        paraMap.put("user_id", user_id);
                        paraMap.put("token", token);
                        paraMap.put("user_name", user_name);
                        paraMap.put("card_number", card_number);
                        paraMap.put("passwd",passwd);
                        paraMap.put("mobile", mobile);

                        Request<JSONObject> request =new NormalPostRequest(mActivity, getResources().getString(R.string.http_url) + "/app/save_bankcard.htm", response1 -> {
                            try {
                                String ret = response1.get("ret").toString();
                                if (ret.equals("1")){
                                    CommonUtil.showSafeToast(mActivity, "保存成功");
                                    mActivity.go_to_my_bank_card();
                                }else  if(ret.equals("-1")){
                                    CommonUtil.showSafeToast(mActivity, "登录异常，请重新登录");
                                    mActivity.go_login();
                                }else  if(ret.equals("-4")){
                                    CommonUtil.showSafeToast(mActivity,"银行卡已经绑定");
                                }else  if(ret.equals("-5")){
                                    CommonUtil.showSafeToast(mActivity,"银行卡号错，请输入正确的银行卡号，再次提交");
                                }else  if(ret.equals("-3")){
                                    CommonUtil.showSafeToast(mActivity,"支付密码错误");
                                }else {
                                    CommonUtil.showSafeToast(mActivity,"信息不全");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> Log.i("test",error.toString()),paraMap);
                        mRequestQueue.add(request);
                    }
                }else {
                    CommonUtil.showSafeToast(mActivity,"银行卡号错误，请检查重新输入哦");
                }
            }, error -> Log.i("test",error.toString()),paramap);
            mRequestQueue.add(requestVerifyBankCard);
        }
    }
}
