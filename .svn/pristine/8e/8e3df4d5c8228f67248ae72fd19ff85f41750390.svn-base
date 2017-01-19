package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 卡兑换会员
 */
public class TiYanVipFragment extends Fragment {


    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_smscode)
    EditText etSmscode;
    @BindView(R.id.bt_getsmscode)
    Button btGetsmscode;
    @BindView(R.id.ll_smscode)
    LinearLayout llSmscode;
    @BindView(R.id.tvtiyantequan)
    TextView tvtiyantequan;



    private int second = 60;
    private BaseActivity mActivity;

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (second > 0) {
                btGetsmscode.setText(--second + "秒后重新发送");
                btGetsmscode.setEnabled(false);
                mHandler.sendEmptyMessageDelayed(1, 1000);
            } else {
                second = 60;
                btGetsmscode.setEnabled(true);
                btGetsmscode.setText("重新发送验证码");
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeMessages(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        View rootview = inflater.inflate(R.layout.fragment_tiyanvip, container, false);
        ButterKnife.bind(this, rootview);

        String trueName = mActivity.getCache("trueName");
        String vip_experience_slogan = mActivity.getCache("vip_experience_slogan");
        String mobile = mActivity.getCache("mobile");
        if (!TextUtils.isEmpty(trueName.trim())) {
            etName.setText(trueName);
            etName.setFocusable(false);
            etName.setEnabled(false);
        }
         if (!TextUtils.isEmpty(vip_experience_slogan.trim())) {
             tvtiyantequan.setText(vip_experience_slogan);
        }else {
             tvtiyantequan.setVisibility(View.GONE);
         }
        if (!TextUtils.isEmpty(mobile.trim())) {
            etMobile.setText(mobile);
            etMobile.setFocusable(false);
            etMobile.setEnabled(false);
            llSmscode.setVisibility(View.GONE);
        }


        return rootview;
    }


    private void getSmscode() {
        String etmoblie = etMobile.getText().toString().trim();
        if (!etmoblie.matches("1[3|4|5|7|8|][0-9]{9}")) {
            Toast.makeText(mActivity, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            Map paraMap = mActivity.getParaMap();
            paraMap.put("mobile", etmoblie);
            RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/vip_mobile_sms.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    int code;
                    try {
                        Log.i("test", jsonObject.toString());
                        code = jsonObject.getInt("code");
                        if (code == 100) {
                            second = 60;
                            mHandler.sendEmptyMessageDelayed(1, 0);
                        } else if (code == 200) {
                            Toast.makeText(mActivity, "短信发送失败", Toast.LENGTH_SHORT).show();
                        } else if (code == 600) {
                            Toast.makeText(mActivity, "此手机号已注册", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private void getTiYanVip() {
        Map map = mActivity.getParaMap();
        String name = etName.getText().toString().trim();
        String etmobile = etMobile.getText().toString().trim();

        if (llSmscode.getVisibility() == View.VISIBLE) {
            String code = etSmscode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                CommonUtil.showSafeToast(mActivity, "请输入验证码");
                return;
            }
            map.put("mcode", code);
        }
        Random random = new Random(100);
        map.put("random", "" + random);

        if (TextUtils.isEmpty(name)) {
            CommonUtil.showSafeToast(mActivity, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(etmobile)) {
            CommonUtil.showSafeToast(mActivity, "请输入手机号");
            return;
        }

        if (!etmobile.matches("1[3|4|5|7|8|][0-9]{9}")) {
            Toast.makeText(mActivity, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            map.put("trueName", name);
            map.put("mobile", etmobile);


            RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/vip_experience.htm", map, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    String code = null;
                    try {
                        code = jsonObject.getString("code");
                        if (code.equals("100")) {
                            Toast.makeText(mActivity, "申请体验会员成功", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        } else if (code.equals("200")) {
                            Toast.makeText(mActivity, "未登录", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("300")) {
                            Toast.makeText(mActivity, "参数错误", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("400")) {
                            Toast.makeText(mActivity, "验证码错误", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("500")) {
                            Toast.makeText(mActivity, "手机号码已被使用", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("600")) {
                            Toast.makeText(mActivity, "不符合申请体验会员要求", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @OnClick({R.id.iv_back, R.id.cancle, R.id.bt_getsmscode, R.id.bt_gettiyanvip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    getFragmentManager().popBackStack();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.cancle:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    getFragmentManager().popBackStack();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.bt_getsmscode:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    getSmscode();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.bt_gettiyanvip:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    getTiYanVip();
                    mActivity.hide_keyboard(view);
                }
                break;
        }
    }
}
