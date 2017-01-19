package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.SerializableMap;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * 填写订单时选择支付和配送方式
 */
public class Cart2TransPayFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private String paytype = "在线支付";
    private Map transValueMap = new HashMap();
    private Map transStore_idValueMap = new HashMap();
    private Spinner spinner;
    private Map<String, String> shopmoneyMap = new HashMap<String, String>();
    private LayoutInflater inflater;
    private AlertDialog alertDialog;
    private TextView tv_select_time;
    private NumberPicker dayTimeNumber;
    private NumberPicker hourTimeNumber;
    private String cart_ids_temp;
    private String order_addr_id;
    private String store_id_temp;
    private List dayTimeList = new ArrayList();
    private Map orderInformationMap = new HashMap();
    private String orderSelectDay = "";
    private String orderSelectTime = "";
    private String orderTime = "";
    private RadioGroup rg_time_styles;
    private RadioButton cb_select_time;
    private RadioButton cb_anytime;
    private RadioButton cb_only_time;
    private TextView tv_express_method;
    private TextView tv_extract;
    private RelativeLayout rl_extract_information;
    private TextView tv_select_extract_address;
    private String JSONString;
    private Bundle bundleNext;
    private RadioGroup rg_express_method_tag;
    private RadioButton tv_express_method_tag;
    private RadioButton rb_express_method;
    private CheckBox cb_user_delivery_self;
    private LinearLayout ll_user_delivery_self;
    private TextView tv_select_delivery_space;
    private TextView tv_select_delivery_time;

    private String delivery_time = "";
    private String addressId;

    public Cart2TransPayFragment() {

    }

    private static Cart2TransPayFragment cart2TransPayFragment = new Cart2TransPayFragment();

    public static Cart2TransPayFragment getInstance() {
        if (cart2TransPayFragment == null) {
            cart2TransPayFragment = new Cart2TransPayFragment();
        }
        return cart2TransPayFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_cart2_trans_pay, container, false);
        mActivity = (BaseActivity) getActivity();
        cart2TransPayFragment = this;
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.paymentAndDeliveryMethods));//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });


        cb_user_delivery_self = (CheckBox) rootView.findViewById(R.id.cb_user_delivery_self);
        ll_user_delivery_self = (LinearLayout) rootView.findViewById(R.id.ll_user_delivery_self);
        tv_select_delivery_space = (TextView) rootView.findViewById(R.id.tv_select_delivery_space);
        tv_select_delivery_time = (TextView) rootView.findViewById(R.id.tv_select_delivery_time);

        final Bundle bundle = getArguments();
        bundleNext = getArguments();
        cart_ids_temp = bundle.getString("cart_ids_temp");
        order_addr_id = bundle.getString("order_addr_id");
        store_id_temp = bundle.getString("store_id_temp");

        //TODO 快递方式和价格请求
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Map transmap = new HashMap();
        transmap.put("cart_ids", bundle.getString("cart_ids_temp"));
        transmap.put("addr_id", bundle.getString("order_addr_id"));
        transmap.put("store_ids", bundle.getString("store_id_temp"));
        Request<JSONObject> request3 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_cart2_cartsTrans.htm",
                trans_result -> {
                    try {
                        LinearLayout translayout = (LinearLayout) rootView.findViewById(R.id.translayout);

                        JSONArray trans_list = trans_result.getJSONArray("trans_list");
                        int trans_length = trans_list.length();
                        for (int i = 0; i < trans_length; i++) {
                            JSONObject oj = trans_list.getJSONObject(i);
                            JSONArray transInfo_list = oj.getJSONArray("transInfo_list");
                            int transInfo_length = transInfo_list.length();
                            List<String> deliverthods = new ArrayList<String>();
                            final List<String> delivervalues = new ArrayList<String>();
                            for (int j = 0; j < transInfo_length; j++) {
                                JSONObject jo = transInfo_list.getJSONObject(j);
                                String key = jo.getString("key");
                                String value = jo.getString("value");
                                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                                    transValueMap.put(key, value);
                                    // 快递方式 价格 map
                                    deliverthods.add(key);
                                    delivervalues.add(value);
                                }
                                ;
                            }
                            LinearLayout layout = (LinearLayout) Cart2TransPayFragment.this.inflater.inflate(R.layout.item_trans_pay, null).findViewById(R.id.trans_item);
                            TextView tv = (TextView) layout.findViewById(R.id.shopname);
                            spinner = (Spinner) layout.findViewById(R.id.spinner);
                            final String shopname = oj.getString("store_name");
                            final String store_id = oj.getString("store_id");
                            tv.setText("以下商品，由" + shopname + "为您发货");
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, deliverthods);
                            spinner.setAdapter(adapter2);
                            spinner.setPrompt("请选择配送方式");
                            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    TextView tv = (TextView) arg1;
                                    String str = (String) tv.getText();
                                    shopmoneyMap.put(shopname, str);
                                    transStore_idValueMap.put(store_id, str);
                                }

                                @Override
                                public void onNothingSelected(
                                        AdapterView<?> arg0) {
                                }

                            });
//                                JSONArray jarr = oj.getJSONArray("goods_list");
//                                LinearLayout imgs = (LinearLayout) layout.findViewById(R.id.goods);
//                                for (int j = 0; j < jarr.length(); j++) {
//
//                                    SimpleDraweeView img = new SimpleDraweeView(mActivity);
//                                    BaseActivity.displayImage(jarr.getString(j), img);
//                                    imgs.addView(img);
//
//                                    if (i == 0) {
//                                        LayoutParams img_lp = new LayoutParams(BaseActivity.dp2px(mActivity, 84), BaseActivity.dp2px(mActivity, 84));
//                                        img_lp.setMargins(MainActivity.dp2px(mActivity, 16), 0, 0, 0);
//                                        img.setLayoutParams(img_lp);
//                                    } else {
//                                        LayoutParams img_lp = new LayoutParams(BaseActivity.dp2px(mActivity, 84), BaseActivity.dp2px(mActivity, 84));
//                                        img_lp.setMargins(MainActivity.dp2px(mActivity, 8), 0, 0, 0);
//                                        img.setLayoutParams(img_lp);
//                                    }
//                                }
                            translayout.addView(layout);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), transmap);
        mRequestQueue.add(request3);

        //TODO 确定按钮
        rootView.findViewById(R.id.methodButton).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (getTargetFragment() == null)
                    return;
                Bundle b = new Bundle();
                //配送时间
                if (delivery_time.equals("")) {
                    delivery_time = "工作日9点-18点可配送";
                } else {
                    b.putString("delivery_time", delivery_time);
                }

                b.putString("paytype", paytype);
                String tv_str = "";
                for (String str : shopmoneyMap.keySet()) {
                    tv_str += str + "：" + shopmoneyMap.get(str) + "\n";
                }
                if (tv_str.length() > 1) {
                    tv_str = tv_str.substring(0, tv_str.length() - 1);
                    b.putString("trans_pay", tv_str);
                }

                if (addressId != null && !TextUtils.isEmpty(addressId)) {
                    b.putString("addressId", addressId);
                }

                b.putSerializable("transValueMap", new SerializableMap(transValueMap));
                b.putSerializable("transStore_idValueMap", new SerializableMap(transStore_idValueMap));

                //自提点编号已经在选择之后传入缓存
                Intent i = new Intent();
                i.putExtras(b);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Cart2Fragment.TRANS_AND_PAY, i);
                getFragmentManager().popBackStack();
            }
        });
        cb_user_delivery_self.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                ll_user_delivery_self.setVisibility(VISIBLE);
            } else if (!b) {
                ll_user_delivery_self.setVisibility(GONE);
            }
        });


        tv_select_delivery_space.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_goods_extract_address_select(bundleNext, cart2TransPayFragment);
            }
        });
        tv_select_delivery_time.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                showSelectTimeDialog(2);
            }
        });

        getOrderData();
        getAddressData();
        rg_time_styles = (RadioGroup) rootView.findViewById(R.id.rg_time_styles);
        cb_select_time = (RadioButton) rootView.findViewById(R.id.cb_select_time);
        cb_anytime = (RadioButton) rootView.findViewById(R.id.cb_anytime);
        cb_only_time = (RadioButton) rootView.findViewById(R.id.cb_only_time);
        cb_only_time.setChecked(true);
        tv_select_time = (TextView) rootView.findViewById(R.id.tv_select_time);
        rg_time_styles.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.cb_select_time:
                    tv_select_time.setVisibility(VISIBLE);
                    showSelectTimeDialog(1);
                    delivery_time = tv_select_time.getText().toString().trim();

                    break;
                case R.id.cb_anytime:
                    tv_select_time.setVisibility(INVISIBLE);
                    delivery_time = cb_anytime.getText().toString().trim();
                    break;
                case R.id.cb_only_time:
                    tv_select_time.setVisibility(INVISIBLE);
                    delivery_time = cb_only_time.getText().toString().trim();
                    break;
                default:
                    break;
            }
        });
        rg_express_method_tag = (RadioGroup) rootView.findViewById(R.id.rg_express_method_tag);
        tv_express_method_tag = (RadioButton) rootView.findViewById(R.id.tv_express_method_tag);
        rb_express_method = (RadioButton) rootView.findViewById(R.id.rb_express_method);
        rb_express_method.setChecked(true);
        tv_extract = (TextView) rootView.findViewById(R.id.tv_extract);
        rl_extract_information = (RelativeLayout) rootView.findViewById(R.id.rl_extract_information);
        tv_select_extract_address = (TextView) rootView.findViewById(R.id.tv_select_extract_address);
        rl_extract_information.setVisibility(INVISIBLE);
        rg_express_method_tag.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.tv_express_method_tag:
                    rl_extract_information.setVisibility(VISIBLE);
                    break;
                case R.id.rb_express_method:
                    rl_extract_information.setVisibility(INVISIBLE);
                    break;
                default:
                    break;
            }
        });
        tv_select_time.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                showSelectTimeDialog(1);
            }
        });


        //自提
        tv_extract.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                rl_extract_information.setVisibility(VISIBLE);
                tv_express_method.setBackgroundResource(R.drawable.frame_grey_dark);
                tv_extract.setBackgroundResource(R.drawable.frame_red);
            }
        });
        return rootView;
    }


    /**
     * 显示时间选择框选择具体的时间
     */
    private void showSelectTimeDialog(final int flag) {

        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_order_date_picker, null);
        dayTimeNumber = (NumberPicker) view.findViewById(R.id.daypicker);
        final String[] day_list = (String[]) orderInformationMap.get("day_list");
        final String[] timeStringArrayList = {"9:00-15:00", "15:00-19:00", "19:00-22:00", "9:00-15:00", "15:00-19:00", "19:00-22:00"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        dayTimeNumber.setOnValueChangedListener((numberPicker, i, i1) -> orderSelectDay = day_list[i1]);
        dayTimeNumber.setFormatter(i -> {
            String tmpStr = String.valueOf(i);
            if (i < 10) {
                tmpStr = "0" + tmpStr;
            }
            return tmpStr;
        });
        dayTimeNumber.setDisplayedValues(day_list);
        dayTimeNumber.setMinValue(0);
        dayTimeNumber.setMaxValue(day_list.length - 1);
        dayTimeNumber.setClickable(false);
        dayTimeNumber.setValue(4);
        dayTimeNumber.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        hourTimeNumber = (NumberPicker) view.findViewById(R.id.timepicker);
        hourTimeNumber.setOnValueChangedListener((numberPicker, i, i1) -> orderSelectTime = timeStringArrayList[i1]);
        hourTimeNumber.setFormatter(i -> {
            String tmpStr = String.valueOf(i);
            if (i < 10) {
                tmpStr = "0" + tmpStr;
            }
            return tmpStr;
        });
        Button b_commit = (Button) view.findViewById(R.id.b_commit);
        Button b_cancel = (Button) view.findViewById(R.id.b_cancel);
        b_commit.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                orderTime = orderSelectDay + orderSelectTime;
                if (orderSelectDay == "" || orderSelectTime == "") {
                    orderSelectDay = day_list[dayTimeNumber.getValue()];
                    orderSelectTime = timeStringArrayList[hourTimeNumber.getValue()];
                    orderTime = orderSelectDay + orderSelectTime;
                }
                if (1 == flag) {
                    tv_select_time.setText(orderTime);
                } else if (2 == flag) {
                    tv_select_delivery_time.setText(orderTime);
                }
                alertDialog.dismiss();
            }
        });
        b_cancel.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                alertDialog.dismiss();
            }
        });
        hourTimeNumber.setDisplayedValues(timeStringArrayList);
        hourTimeNumber.setMinValue(0);
        hourTimeNumber.setMaxValue(timeStringArrayList.length - 1);
        hourTimeNumber.setValue(4);
        hourTimeNumber.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
    }

    private void getOrderData() {
        mActivity.showProcessDialog();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Map paraMap = mActivity.getParaMap();
        paraMap.put("cart_ids", cart_ids_temp);
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                mActivity.getAddress() + "/app/goods_cart2.htm",
                result -> {
                    try {
                        parseJSONObject(result);
                    } catch (Exception e) {
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);
    }

    private void parseJSONObject(JSONObject result) throws Exception {
        JSONArray arr = result.getJSONArray("day_list");
        String[] dayTimeStringArray = new String[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            String dayTimeString = arr.getString(i);
            dayTimeList.add(dayTimeString);
            dayTimeStringArray[i] = dayTimeString;
        }
        orderInformationMap.put("cart_ids", result.getString("cart_ids"));
        orderInformationMap.put("day_list", dayTimeStringArray);
        orderInformationMap.put("order_goods_price", result.getString("order_goods_price"));
        orderInformationMap.put("store_ids", result.getString("store_ids"));
    }

    private void getAddressData() {
        mActivity.showProcessDialog();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Map paraMap = mActivity.getParaMap();
        paraMap.put("addr_id", order_addr_id);
        paraMap.put("beginCount", 0);
        paraMap.put("selectCount", 10);
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_cart2_delivery.htm",
                result -> {
                    try {
                        parseAddressJSONObject(result);
                        tv_select_extract_address.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                mActivity.go_goods_extract_address_select(bundleNext, cart2TransPayFragment);
                            }
                        });
                    } catch (Exception e) {
                    }
                }, error -> {Log.i("test", error.toString());}, paraMap);
        mRequestQueue.add(request);
    }

    private void parseAddressJSONObject(JSONObject result) throws Exception {
        JSONArray arr = result.getJSONArray("data");
        JSONString = arr.toString();
        bundleNext.putString("JSONString", JSONString);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle b = data.getExtras();
        String addressName = b.getString("addressName");
        addressId = b.getString("addressId");

        tv_select_extract_address.setText(addressName);
        getPaytypeAndTrans();

    }

    //自提后设置快递方式和价格
    void getPaytypeAndTrans() {

        LinearLayout translayout = (LinearLayout) rootView.findViewById(R.id.translayout);
        translayout.removeAllViews();
        transValueMap.clear();
        shopmoneyMap.clear();
        transStore_idValueMap.clear();
        List<String> deliverthods = new ArrayList<String>();
        LinearLayout layout = (LinearLayout) Cart2TransPayFragment.this.inflater.inflate(R.layout.item_trans_pay, null).findViewById(R.id.trans_item);
        TextView tv = (TextView) layout.findViewById(R.id.shopname);
        spinner = (Spinner) layout.findViewById(R.id.spinner);
        tv.setText("以下商品，请您到自提点自提");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, deliverthods);
        spinner.setAdapter(adapter2);
        spinner.setPrompt("请选择配送方式");
        translayout.addView(layout);


//        Map paramap = mActivity.getParaMap();
//        paramap.put("cart_ids", cart_ids_temp);
//        paramap.put("store_ids", store_id_temp);
//        paramap.put("addr_id", order_addr_id);
//        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/goods_cart2_cartsTrans.htm", paramap, new BaseSubscriber<JSONObject>(mActivity) {
//            @Override
//            public void onNext(JSONObject jsonObject) {
//                try {
//                    LinearLayout translayout = (LinearLayout) rootView.findViewById(R.id.translayout);
//                    translayout.removeAllViews();
//                    transValueMap.clear();
//                    shopmoneyMap.clear();
//                    transStore_idValueMap.clear();
//
//                    JSONArray trans_list = jsonObject.getJSONArray("trans_list");
//                    int trans_length = trans_list.length();
//                    for (int i = 0; i < trans_length; i++) {
//
//                        JSONObject oj = trans_list.getJSONObject(i);
//                        final String shopname = oj.getString("store_name");
//                        final String store_id = oj.getString("store_id");
//                        JSONArray transInfo_list = oj.getJSONArray("transInfo_list");
//                        int transInfo_length = transInfo_list.length();
//                        List<String> deliverthods = new ArrayList<String>();
//                        final List<String> delivervalues = new ArrayList<String>();
//                        for (int j = 0; j < transInfo_length; j++) {
//                            JSONObject jo = transInfo_list.getJSONObject(j);
//                            if (!shopname.equals("平台运营商")){
//                                transValueMap.put(jo.getString("key"), jo.getString("value"));
//                                // 快递方式 价格 map
//                                deliverthods.add(jo.getString("key"));
//                                delivervalues.add(jo.getString("value"));
//                            }
//
//                        }
//                        Log.i("test", deliverthods.toString());
//                        LinearLayout layout = (LinearLayout) Cart2TransPayFragment.this.inflater.inflate(R.layout.item_trans_pay, null).findViewById(R.id.trans_item);
//                        TextView tv = (TextView) layout.findViewById(R.id.shopname);
//                        spinner = (Spinner) layout.findViewById(R.id.spinner);
//
//                        if (!shopname.equals("平台运营商")){
//                            tv.setText("以下商品，由" + shopname + "为您发货");
//                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, deliverthods);
//                            spinner.setAdapter(adapter2);
//                            spinner.setPrompt("请选择配送方式");
//                            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//                                @Override
//                                public void onItemSelected(
//                                        AdapterView<?> arg0, View arg1,
//                                        int arg2, long arg3) {
//
//                                    TextView tv = (TextView) arg1;
//                                    String str = (String) tv.getText();
//
//                                    shopmoneyMap.put(shopname, str);
//                                    transStore_idValueMap.put(store_id, str);
//                                }
//
//                                @Override
//                                public void onNothingSelected(
//                                        AdapterView<?> arg0) {
//                                }
//
//                            });
//                            JSONArray jarr = oj.getJSONArray("goods_list");
//                            LinearLayout imgs = (LinearLayout) layout.findViewById(R.id.goods);
//                            for (int j = 0; j < jarr.length(); j++) {
//
//                                SimpleDraweeView img = new SimpleDraweeView(mActivity);
//                                BaseActivity.displayImage(jarr.getString(j), img);
//                                imgs.addView(img);
//
//                                if (i == 0) {
//                                    LayoutParams img_lp = new LayoutParams(BaseActivity.dp2px(mActivity, 84), BaseActivity.dp2px(mActivity, 84));
//                                    img_lp.setMargins(MainActivity.dp2px(mActivity, 16), 0, 0, 0);
//                                    img.setLayoutParams(img_lp);
//                                } else {
//                                    LayoutParams img_lp = new LayoutParams(BaseActivity.dp2px(mActivity, 84), BaseActivity.dp2px(mActivity, 84));
//                                    img_lp.setMargins(MainActivity.dp2px(mActivity, 8), 0, 0, 0);
//                                    img.setLayoutParams(img_lp);
//                                }
//                            }
//                            translayout.addView(layout);
//                        }else {
//                            tv.setText("以下商品，请您到自提点自提");
//                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, deliverthods);
//                            spinner.setAdapter(adapter2);
//                            spinner.setPrompt("请选择配送方式");
//
//                            JSONArray jarr = oj.getJSONArray("goods_list");
//                            LinearLayout imgs = (LinearLayout) layout.findViewById(R.id.goods);
//                            for (int j = 0; j < jarr.length(); j++) {
//
//                                SimpleDraweeView img = new SimpleDraweeView(mActivity);
//                                BaseActivity.displayImage(jarr.getString(j), img);
//                                imgs.addView(img);
//
//                                if (i == 0) {
//                                    LayoutParams img_lp = new LayoutParams(BaseActivity.dp2px(mActivity, 84), BaseActivity.dp2px(mActivity, 84));
//                                    img_lp.setMargins(MainActivity.dp2px(mActivity, 16), 0, 0, 0);
//                                    img.setLayoutParams(img_lp);
//                                } else {
//                                    LayoutParams img_lp = new LayoutParams(BaseActivity.dp2px(mActivity, 84), BaseActivity.dp2px(mActivity, 84));
//                                    img_lp.setMargins(MainActivity.dp2px(mActivity, 8), 0, 0, 0);
//                                    img.setLayoutParams(img_lp);
//                                }
//                            }
//                            translayout.addView(layout);
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

}
