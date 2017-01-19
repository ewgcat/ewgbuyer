package com.ewgvip.buyer.android.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.ConsulListActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.adapter.AlertDialogDetailsAdapter;
import com.ewgvip.buyer.android.adapter.ViewPagerAdapter;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.dialog.GoodsEditDialog;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.dialog.ShareWetcharImageUtils;
import com.ewgvip.buyer.android.layout.MyWebViewClient;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ewgvip.buyer.android.R.id.distributionTo_tag;

/**
 * 商品详情页
 */
public class GoodsFragment extends Fragment {
    private static String goods_img;
    public String share_goods_name = "";
    public String share_goods_img = "";
    JSONArray specsList;
    ViewPagerAdapter mViewPagerAdapter;
    private Map map2 = new HashMap();// 规格分类 当前选择规格id
    private String area_id = "";
    private MainActivity mActivity;
    private View rootView;
    private TextView textview;
    private String goods_id;
    private List<String> imglist;
    private String status;
    private List<String> namelist;
    private List idlist;
    private String deliverstr = "";
    private String cartprice;
    private String cartgsp;
    private RelativeLayout addtocart;
    private RelativeLayout buyNow;
    private TextView distributionTo;
    private String favourite_statue;
    private int goods_count = 1;
    private int f_code = 0;
    private GoodsContainerFragment goodsContainerFragment;
    private GoodsEditDialog goodsEditDialog;
    private Bundle bundle;
    private int count;
    private boolean isPromotions = false;
    private boolean isFcode = false;
    private boolean isLimit = false;
    private int limitNum = 0;
    private RelativeLayout relativeLayout;
    private String user_id = "";
    private String advance_ding_pay = "true";
    private String advance_ding = "";
    private String[] invoiceString;
    private Dialog dialog;
    private SharedPreferences preferences;  //获取本地存储的地址
    private String cityId = "";
    private String areaName = "";
    private String province = "";
    private String district = "";
    private String city = "";
    private String currentLocation = "";
    private TextView current_Pricetextview;


    public static GoodsFragment getInstance(String goods_id, GoodsContainerFragment goodsContainerFragment) {

        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("goods_id", goods_id);
        fragment.setArguments(bundle);
        fragment.setGoodsContainerFragment(goodsContainerFragment);
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods, container, false);
        mActivity = (MainActivity) getActivity();
        mActivity.showProcessDialog();
        bundle = getArguments();
        invoiceString = new String[5];
        goods_id = bundle.getString("goods_id");
        addtocart = (RelativeLayout) rootView.findViewById(R.id.add_to_car);
        buyNow = (RelativeLayout) rootView.findViewById(R.id.buy_now);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.layout_promotional_info);

        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        cityId = preferences.getString("cityId", "");
        areaName = preferences.getString("areaName", "");
        province = preferences.getString("province", "");
        district = preferences.getString("district", "");
        city = preferences.getString("city", "");
        currentLocation = province + ">" + city + ">" + district;

        // 获取商品详细信息
        user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        final Map paramap = new HashMap();
        paramap.put("id", goods_id);
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("cityName", province + city);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods.htm",
                result -> {
                    try {
                        //TODO 设置市场价格和钻卡价格
                        ViewStub viewStub = (ViewStub) rootView.findViewById(R.id.viewstub);
                        viewStub.inflate();
                        init();
                        Log.i("test", result.toString());
                        distributionTo = (TextView) rootView.findViewById(R.id.distributionTo);
                        if (result.has("country")
                                && result.has("country_photo")
                                && !TextUtils.isEmpty(result.getString("country").trim())
                                && !TextUtils.isEmpty(result.getString("country_photo").trim())) {
                            View ll_made_in_country = rootView.findViewById(R.id.ll_made_in_country);
                            ll_made_in_country.setVisibility(View.VISIBLE);
                            ImageView iv_made_in_country = (ImageView) rootView.findViewById(R.id.iv_made_in_country);
                            String urlString = result.getString("country_photo");
                            Glide.with(mActivity).load(urlString).into(iv_made_in_country);
                            TextView tv_made_in_country = (TextView) rootView.findViewById(R.id.tv_made_in_country);
                            tv_made_in_country.setText(result.getString("country"));
                        }
                        imglist = new ArrayList<String>();
                        String positon = mActivity.getCache("current_city");
                        //处理地址为空的情况
                        if (positon.equals(">>")) {
                            positon = "自动获取地址失败！请手动选择地址";
                        }
                        distributionTo.setText(positon);
                        area_id = mActivity.getCache("area_id");

                        distributionTo.setVisibility(View.GONE);
                        rootView.findViewById(distributionTo_tag).setVisibility(View.GONE);


                        // 商品图片
                        JSONArray nameList = result.getJSONArray("goods_photos");// 获取JSONArray
                        int length = nameList.length();
                        for (int i = 0; i < length; i++) {// 遍历JSONArray
                            imglist.add(nameList.getString(i));
                        }
                        goods_img = nameList.get(0).toString();
                        share_goods_img = result.get("goods_photos_small") + "";
                        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
                        mViewPagerAdapter = new ViewPagerAdapter(mActivity, imglist);
                        mViewPager.setAdapter(mViewPagerAdapter);
                        final TextView viewPager_mark = (TextView) rootView.findViewById(R.id.viewpager_mark);
                        viewPager_mark.setText("1/" + imglist.size());
                        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
                            @Override
                            public void onPageSelected(int arg0) {
                                viewPager_mark.setText((arg0 + 1) + "/" + imglist.size());
                            }

                            @Override
                            public void onPageScrolled(int arg0, float arg1, int arg2) {
                            }

                            @Override
                            public void onPageScrollStateChanged(int arg0) {
                            }
                        });
                        //商品名称
                        textview = (TextView) rootView.findViewById(R.id.goods_name);
                        String goods_name = result.get("goods_name").toString();
                        share_goods_name = result.get("goods_name").toString();
                        bundle.putString("goods_name", goods_name);
                        if (result.getInt("goods_type") == 0) {// 自营
                            rootView.findViewById(R.id.layout_store_info).setVisibility(View.GONE);
                            rootView.findViewById(R.id.store).setVisibility(View.GONE);
                            goods_name += "    ";
                            int start = goods_name.length();
                            goods_name += " 自营 ";
                            int end = goods_name.length();
                            SpannableStringBuilder style = new SpannableStringBuilder(goods_name);
                            style.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.red)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            style.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            style.setSpan(new AbsoluteSizeSpan(14, true), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                            textview.setText(style);
                        } else {// 第三方
                            JSONObject store_info = result.getJSONObject("store_info");
                            textview.setText(goods_name);// 名称
                            rootView.findViewById(R.id.store).setVisibility(View.VISIBLE);
                            final String store_id = store_info.get("store_id").toString();
                            rootView.findViewById(R.id.layout_store_info).setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    final Bundle bundle1 = new Bundle();
                                    bundle1.putString("store_id", store_id);
                                    mActivity.go_store_index(bundle1);
                                }
                            });
                            rootView.findViewById(R.id.go_into_store).setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    final Bundle bundle1 = new Bundle();
                                    bundle1.putString("store_id", store_id);
                                    mActivity.go_store_index(bundle1);
                                }
                            });
                            rootView.findViewById(R.id.store).setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    final Bundle bundle1 = new Bundle();
                                    bundle1.putString("store_id", store_id);
                                    mActivity.go_store_index(bundle1);
                                }
                            });
                            rootView.findViewById(R.id.contact_customer_service);
                            rootView.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    mActivity.go_chat(bundle);
                                }
                            });

                            textview = (TextView) rootView.findViewById(R.id.store_name);
                            textview.setText(store_info.get("store_name").toString());
                            SimpleDraweeView store_logo = (SimpleDraweeView) rootView.findViewById(R.id.store_logo);
                            BaseActivity.displayImage(store_info.get("store_logo").toString(), store_logo);

                            textview = (TextView) rootView.findViewById(R.id.descriptionEvaluate);
                            textview.setText(getString(R.string.descriptionEvaluate) + store_info.get("description_evaluate").toString());
                            textview = (TextView) rootView.findViewById(R.id.serviceEvaluate);
                            textview.setText(getString(R.string.serviceEvaluate) + store_info.get("service_evaluate").toString());
                            textview = (TextView) rootView.findViewById(R.id.shipEvaluate);
                            textview.setText(getString(R.string.shipEvaluate) + store_info.get("ship_evaluate").toString());
                            textview = (TextView) rootView.findViewById(R.id.store_allgoods);
                            textview.setText(store_info.get("store_goods_count").toString());
                            textview = (TextView) rootView.findViewById(R.id.concerned_amount);
                            textview.setText(store_info.get("store_concern_count").toString());

                            double store_rate = Double.parseDouble(store_info.get("store_rate").toString());
                            ImageView imageview = (ImageView) rootView.findViewById(R.id.store_rate);
                            if (store_rate > 4) {
                                imageview.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.heart_5));
                            } else if (store_rate > 3) {
                                imageview.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.heart_4));
                            } else if (store_rate > 2) {
                                imageview.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.heart_3));
                            } else if (store_rate > 1) {
                                imageview.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.heart_2));
                            } else {
                                imageview.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.heart_1));
                            }
                        }
                        //商品广告语
                        textview = (TextView) rootView.findViewById(R.id.goods_ad_words);
                        if (result.has("goods_ad_words")) {
                            String goods_ad_words = result.get("goods_ad_words").toString();
                            textview.setText(goods_ad_words);
                        } else {
                            textview.setVisibility(View.GONE);
                        }

                        ImageView imageview = (ImageView) rootView.findViewById(R.id.favourite_img);
                        TextView favourite_word = (TextView) rootView.findViewById(R.id.favourite_word);
                        if (Boolean.parseBoolean(result.get("favorite").toString())) {
                            favourite_statue = "del";
                            imageview.setImageResource(R.mipmap.favourited);
                            favourite_word.setText("已收藏");
                        } else {
                            favourite_statue = "add";
                            imageview.setImageResource(R.mipmap.favourite);
                            favourite_word.setText("收藏");
                        }

                        if (result.getInt("goods_choice_type") == 1) {
                            rootView.findViewById(R.id.layout_trans).setVisibility(View.GONE);
                        }


                        //TODO 现价
                        current_Pricetextview = (TextView) rootView.findViewById(R.id.current_Price);
                        String price = result.get("goods_current_price").toString();
                        String moneytodouble = mActivity.moneytodouble(price);
                        current_Pricetextview.setText("¥" + moneytodouble);// 现价
                        //市场价格
                        textview = (TextView) rootView.findViewById(R.id.goods_price);
                        String goods_price = result.get("goods_price").toString();
                        textview.setText("¥" + mActivity.moneytodouble(goods_price));
                        textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                        //会员价格
                        textview = (TextView) rootView.findViewById(R.id.jewel_price);
                        String jewel_price = result.get("jewel_price").toString();
                        if ("钻卡会员".equals(mActivity.getCache("level_name"))) {
                            textview.setVisibility(View.GONE);
                        } else {
                            textview.setVisibility(View.VISIBLE);
                            textview.setText("会员价格：¥" + mActivity.moneytodouble(jewel_price));
                        }

//                            textview = (TextView) rootView.findViewById(R.id.evaluation_people);
//                            textview.setText(result.get("evaluate_count") + "人评论");
//                            textview = (TextView) rootView.findViewById(R.id.evaluation_rate);
//                            textview.setText(result.get("goods_well_evaluate").toString());
                        int goods_inventory = result.getInt("goods_inventory");
                        textview = (TextView) rootView.findViewById(R.id.inventory);
                        if (goods_inventory > 20) {
                            textview.setText("库存： 现货");// 库存
                        } else if (goods_inventory > 0) {
                            textview.setText("库存： " + goods_inventory + "件");// 库存
                        } else {
                            textview.setText("库存： 无货");
                        }
                        textview = (TextView) rootView.findViewById(R.id.purchaseConsulting_layout);
                        textview.setText(getString(R.string.purchaseConsulting) + "(" + result.get("consult_count") + ")");
                        textview = (TextView) rootView.findViewById(R.id.distributionTo);
                        mActivity.setCache("current_city", currentLocation);
                        if (result.has("area_id")) {
                            area_id = result.get("area_id") + "";
                            mActivity.setCache("area_id", area_id);
                        }
                        bundle.putString("photo", imglist.get(0));
                        // 促销
                        textview = (TextView) rootView.findViewById(R.id.youhui);
                        status = (String) result.get("status");
                        if (status != null && !status.equals("")) {
                            textview.setText(status);
                            TextView activity_info = (TextView) rootView.findViewById(R.id.youhuiinfo);
                            activity_info.setText(result.get("status_info").toString());
                            if (status.equals("团购")) {
                            } else if (status.equals("促销")) {
                                setActivityWord();
                            } else if (status.equals("F码")) {
                                f_code = 1;
                                TextView add_to_car_text = (TextView) rootView.findViewById(R.id.add_to_car_text);
                                add_to_car_text.setText("F码购买");
                                buyNow.setVisibility(View.GONE);
                                isFcode = true;
                            } else if (status.equals("组合")) {
                                rootView.findViewById(R.id.layout_promotional_info).setClickable(true);
                                rootView.findViewById(R.id.iv_marking).setVisibility(View.VISIBLE);
                                bundle.putString("goods_img", imglist.get(0));
                            } else if (status.equals("预售")) {
                                isPromotions = true;
                                addtocart.setVisibility(View.GONE);
                                TextView buyNow1 = (TextView) rootView.findViewById(R.id.buyNow);
                                buyNow1.setText("立即付定金");
                                rootView.findViewById(R.id.iv_marking).setVisibility(View.VISIBLE);
                                if (result.has("advance_ding")) {
                                    advance_ding = result.get("advance_ding") + "";
                                }
                                if (result.has("advance_ding_pay")) {
                                    if (result.get("advance_ding_pay").toString().equals("true")) {
                                        advance_ding_pay = "true";
                                    } else {
                                        advance_ding_pay = "false";
                                    }
                                }
                                invoiceString[0] = advance_ding;
                                invoiceString[1] = result.get("advance_wei") + "";
                                invoiceString[2] = result.get("ding_pay_end") + "";
                                invoiceString[3] = result.get("wei_pay_end") + "";
                                invoiceString[4] = result.get("ship_Date") + "";
                            }

                        } else {
                            rootView.findViewById(R.id.layout_promotional_info).setClickable(false);
                            rootView.findViewById(R.id.layout_promotional_info).setVisibility(View.GONE);
                        }
                        rootView.findViewById(R.id.purchaseConsulting_layout).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                Intent intent = new Intent();
                                try {
                                    intent.putExtra("title", (String) result.get("goods_name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("id", goods_id);
                                intent.setClass(mActivity, ConsulListActivity.class);
                                startActivity(intent);
                            }
                        });
                        rootView.findViewById(R.id.distributionTo).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                getArea(null);
                            }
                        });
                        if (result.has("goods_limit") && result.getInt("goods_limit") != 0) {
                            isLimit = true;
                            int all_count = result.getInt("all_count");
                            textview = (TextView) rootView.findViewById(R.id.limit);
                            if (result.has("buy_count")) {
                                int can_buy_count = all_count - result.getInt("buy_count");
                                textview.setText("(限购" + all_count + "件,已购" + can_buy_count + "件)");
                            } else {
                                textview.setText("(限购" + all_count + "件)");
                            }
                            limitNum = result.getInt("buy_count");
                        } else {
                            isLimit = false;
                        }
                    } catch (Exception e) {
                    }
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

        return rootView;
    }

    private void go_evaluate() {
        goodsContainerFragment.go_evaluate();
    }

    void init() {

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        // 初始默认规格

        cartgsp = "";
        String specs = "";
        goods_count = 1;
        Map paramap = new HashMap();
        paramap.put("id", goods_id);
        Request<JSONObject> request2 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_specs.htm",
                specsobj -> {
                    String spec_str = "";
                    try {
                        specsList = specsobj.getJSONArray("spec_list");// 获取JSONArray
                        int specslength = specsList.length();
                        for (int i = 0; i < specslength; i++) {// 遍历JSONArray
                            JSONObject oj = specsList.getJSONObject(i);
                            String specskey = oj.getString("spec_key");
                            JSONArray valueList = oj.getJSONArray("spec_values");
                            JSONObject myobj = valueList.getJSONObject(0);
                            spec_str += myobj.getString("val") + "  ";
                            if (i == 0) {
                                cartgsp += myobj.getInt("id");
                            } else {
                                cartgsp += "," + myobj.getInt("id");
                            }
                        }
                        spec_str += "1件";
                        setSpec();
                        textview = (TextView) rootView.findViewById(R.id.specification);
                        textview.setText(spec_str);
                        goodsEditDialog = new GoodsEditDialog(mActivity, R.style.AlertDialogStyle, specsList, goods_id, cartgsp, goods_count + "", "0", area_id, isPromotions, isFcode, isLimit, limitNum, advance_ding_pay);
                        rootView.findViewById(R.id.layout_spec).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                //TODO 点击弹出购买商品编辑框
                                if (!goodsEditDialog.isShowing() || goodsEditDialog == null) {
                                    goodsEditDialog.show();
                                }
                                goodsEditDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        //数量不能为空或者小于1
                                        if (goodsEditDialog.getCount().toString().trim().equals("")) {
                                        } else {
                                            if (Double.valueOf(goodsEditDialog.getPrice()) == 0 || Integer.valueOf(goodsEditDialog.getCount()) < 1) {

                                            } else {
                                                TextView textView = (TextView) rootView.findViewById(R.id.specification);
                                                //TODO 现价
                                                TextView textview2 = (TextView) rootView.findViewById(R.id.current_Price);
                                                cartgsp = goodsEditDialog.getGsp();
                                                goods_count = Integer.parseInt(goodsEditDialog.getCount());

                                                cartprice = goodsEditDialog.getPrice();

                                                textView.setText(goodsEditDialog.getInfo());
                                                textview2.setText("¥ " + mActivity.moneytodouble(cartprice + ""));
                                                if (goodsEditDialog.getImgList().size() != 0) {
                                                    imglist.clear();
                                                    imglist = goodsEditDialog.getImgList();
                                                    ViewPager mViewPager = (ViewPager) rootView
                                                            .findViewById(R.id.viewpager);
                                                    mViewPagerAdapter = new ViewPagerAdapter(
                                                            mActivity, imglist);
                                                    mViewPager.setAdapter(mViewPagerAdapter);
                                                    final TextView viewPager_mark = (TextView) rootView
                                                            .findViewById(R.id.viewpager_mark);
                                                    viewPager_mark.setText("1/" + imglist.size());
                                                    mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

                                                        @Override
                                                        public void onPageSelected(int arg0) {

                                                            viewPager_mark.setText((arg0 + 1)
                                                                    + "/" + imglist.size());
                                                        }

                                                        @Override
                                                        public void onPageScrolled(int arg0,
                                                                                   float arg1, int arg2) {

                                                        }

                                                        @Override
                                                        public void onPageScrollStateChanged(
                                                                int arg0) {

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                        goodsEditDialog = new GoodsEditDialog(mActivity, R.style.AlertDialogStyle, specsList, goods_id, cartgsp, goods_count + "", "0", area_id, isPromotions, isFcode, isLimit, limitNum, advance_ding_pay);
                                        setSpec();
                                    }
                                });
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {Log.i("test", error.toString());}, paramap);
        mRequestQueue.add(request2);

        Map map = new HashMap();
        map.put("id", goods_id);//商品id
        map.put("beginCount", 0);//开始查询位置
        map.put("selectCount", 3);//查询数量
        Request<JSONObject> request3 = new NormalPostRequest(mActivity, CommonUtil.getAddress(mActivity) + "/app/goods_evaluate.htm", response -> {
            try {
                LinearLayout layout_evaluate = (LinearLayout) rootView.findViewById(R.id.layout_evaluate);
                String ret = response.getString("ret");
                if (ret.equals("true")) {//判断返回结果
                    JSONArray jSONArray = response.getJSONArray("eva_list");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsonObject = jSONArray.getJSONObject(i);
                        View view = View.inflate(mActivity, R.layout.item_goods_evaluate_simple, null);
                        SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.iv_item_evaluate_user_img);
                        BaseActivity.displayImage(jsonObject.getString("user_img"), img);
                        TextView textView = (TextView) view.findViewById(R.id.tv_item_evaluate_user);
                        if (jsonObject.has("user") && !TextUtils.isEmpty(jsonObject.getString("user"))) {
                            textView.setText(jsonObject.getString("user"));
                        } else {
                            textView.setText("匿名");
                        }
                        textView = (TextView) view.findViewById(R.id.tv_item_evaluate_time);
                        textView.setText(jsonObject.getString("addTime"));
                        textView = (TextView) view.findViewById(R.id.tv_item_evaluate_content);
                        if (jsonObject.getString("content").equals("")) {
                            textView.setText("这个人很懒，什么也没留下！");
                        } else {
                            textView.setText(jsonObject.getString("content"));
                        }

                        layout_evaluate.addView(view);
                    }
                    rootView.findViewById(R.id.evaluate_tag).setOnClickListener(v -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            go_evaluate();
                        }
                    });
                    rootView.findViewById(R.id.layout_evaluate).setOnClickListener(v -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            go_evaluate();
                        }
                    });
                    rootView.findViewById(R.id.evaluate_withimg).setOnClickListener(v -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            go_evaluate();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {Log.i("test", error.toString());}, map);
        mRequestQueue.add(request3);

        rootView.findViewById(R.id.layout_goods_imgs).setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, mActivity.getScreenWidth()));

        rootView.findViewById(R.id.favourite).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (mActivity.islogin()) {
                    favourite();
                } else {
                    mActivity.go_login();
                }
            }
        });


        //点击加入购物车--->效果变为跳转编辑框
        addtocart.setOnClickListener(v -> {
            //TODO 点击弹出购买商品编辑框
            if (goodsEditDialog != null || !goodsEditDialog.isShowing()) {
                goodsEditDialog.show();


                goodsEditDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        //数量不能为空或者小于1
                        if (goodsEditDialog.getCount().toString().trim().equals("")) {
                        } else {
                            if (Double.valueOf(goodsEditDialog.getPrice()) == 0 || Integer.valueOf(goodsEditDialog.getCount()) < 1) {

                            } else {
                                TextView textView = (TextView) rootView.findViewById(R.id.specification);
                                //TODO 现价
                                TextView textview2 = (TextView) rootView.findViewById(R.id.current_Price);
                                cartgsp = goodsEditDialog.getGsp();
                                goods_count = Integer.parseInt(goodsEditDialog.getCount());

                                cartprice = goodsEditDialog.getPrice();

                                textView.setText(goodsEditDialog.getInfo());
                                textview2.setText("¥ " + mActivity.moneytodouble(cartprice + ""));
                                if (goodsEditDialog.getImgList().size() != 0) {
                                    imglist.clear();
                                    imglist = goodsEditDialog.getImgList();
                                    ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
                                    mViewPagerAdapter = new ViewPagerAdapter(mActivity, imglist);
                                    mViewPager.setAdapter(mViewPagerAdapter);
                                    final TextView viewPager_mark = (TextView) rootView.findViewById(R.id.viewpager_mark);
                                    viewPager_mark.setText("1/" + imglist.size());
                                    mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

                                        @Override
                                        public void onPageSelected(int arg0) {
                                            viewPager_mark.setText((arg0 + 1) + "/" + imglist.size());
                                        }

                                        @Override
                                        public void onPageScrolled(int arg0, float arg1, int arg2) {
                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int arg0) {
                                        }
                                    });
                                }
                            }
                        }
                        goodsEditDialog = new GoodsEditDialog(mActivity, R.style.AlertDialogStyle, specsList, goods_id, cartgsp, goods_count + "", "0", area_id, isPromotions, isFcode, isLimit, limitNum, advance_ding_pay);
                        setSpec();
                    }
                });
            }

        });
        //TODO 点击立即购买--->效果变为跳转编辑框
        buyNow.setOnClickListener(v -> {
            //TODO 点击弹出购买商品编辑框
            if (goodsEditDialog != null && !goodsEditDialog.isShowing()) {
                goodsEditDialog.show();

                goodsEditDialog.setOnCancelListener(dialogInterface -> {
                    //数量不能为空或者小于1
                    if (goodsEditDialog.getCount().toString().trim().equals("")) {
                    } else {
                        if (Double.valueOf(goodsEditDialog.getPrice()) == 0 || Integer.valueOf(goodsEditDialog.getCount()) < 1) {

                        } else {
                            TextView textView = (TextView) rootView.findViewById(R.id.specification);
                            //TODO 现价
                            TextView textview2 = (TextView) rootView.findViewById(R.id.current_Price);
                            cartgsp = goodsEditDialog.getGsp();
                            goods_count = Integer.parseInt(goodsEditDialog.getCount());

                            cartprice = goodsEditDialog.getPrice();

                            textView.setText(goodsEditDialog.getInfo());
                            textview2.setText("¥ " + mActivity.moneytodouble(cartprice + ""));
                            if (goodsEditDialog.getImgList().size() != 0) {
                                imglist.clear();
                                imglist = goodsEditDialog.getImgList();
                                ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
                                mViewPagerAdapter = new ViewPagerAdapter(mActivity, imglist);
                                mViewPager.setAdapter(mViewPagerAdapter);
                                final TextView viewPager_mark = (TextView) rootView.findViewById(R.id.viewpager_mark);
                                viewPager_mark.setText("1/" + imglist.size());
                                mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
                                    @Override
                                    public void onPageSelected(int arg0) {
                                        viewPager_mark.setText((arg0 + 1) + "/" + imglist.size());
                                    }

                                    @Override
                                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int arg0) {
                                    }
                                });
                            }
                        }
                    }
                    goodsEditDialog = new GoodsEditDialog(mActivity, R.style.AlertDialogStyle, specsList, goods_id, cartgsp, goods_count + "", "0", area_id, isPromotions, isFcode, isLimit, limitNum, advance_ding_pay);
                    setSpec();
                });
            }
        });

        rootView.findViewById(R.id.chat).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_chat(bundle);
                    }
                });


        WebView webview = (WebView) rootView.findViewById(R.id.webDetail);
        webview.setWebViewClient(new MyWebViewClient(mActivity));
        webview.getSettings().setSupportZoom(true);

        WebSettings webviewSettings = webview.getSettings();
        webviewSettings.setJavaScriptEnabled(true);
        webviewSettings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webview.loadUrl(CommonUtil.getAddress(getActivity()) + "/app/goods_introduce.htm?id=" + goods_id + "&user_id=" + user_id);

    }

    protected void F_addToCart(final View view) {
        if (f_code == 1) {
            final EditText f_et = new EditText(mActivity);
            new AlertDialog.Builder(mActivity).setTitle("请输入F码").setView(f_et)
                    .setPositiveButton("确定", (dialog1, which) -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    String f_string = f_et.getText().toString();
                                    Map paramap = new HashMap();
                                    final SharedPreferences preferences1 = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                                    final String user_id1 = preferences1.getString("user_id", "");
                                    String token = preferences1.getString("token", "");
                                    final String cart_mobile_ids = preferences1.getString("cart_mobile_ids", "");
                                    paramap.put("user_id", user_id1);
                                    paramap.put("token", token);
                                    paramap.put("cart_mobile_ids", cart_mobile_ids);
                                    paramap.put("goods_id", goods_id);
                                    paramap.put("count", goods_count + "");
                                    paramap.put("price", cartprice + "");
                                    paramap.put("gsp", cartgsp);
                                    paramap.put("f_code", f_string);
                                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                    Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/add_f_code_goods_cart.htm",
                                            result -> {
                                                try {
                                                    if (result.getBoolean("ret")) {
                                                        if ("".equals(user_id1)) {
                                                            String cart_mobile_id = result.getString("cart_mobile_id");
                                                            if (!cart_mobile_id.equals("")) {
                                                                Editor editor = preferences1.edit();
                                                                editor.putString("cart_mobile_ids", cart_mobile_ids + cart_mobile_id + ",");
                                                                editor.commit();
                                                            }
                                                        }
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity).setTitle("添加成功！").setMessage("商品已成功加入购物车")
                                                                .setPositiveButton("去购物车", (dialog11, which1) -> {
                                                                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                                                                dialog11.dismiss();// 关闭对话框
                                                                                mActivity.go_cart();
                                                                                mActivity.hide_keyboard(view);
                                                                            }
                                                                        })
                                                                .setNegativeButton("再逛逛", (dialog11, which1) -> {
                                                                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                                                                dialog11.dismiss();// 关闭对话框
                                                                                mActivity.hide_keyboard(view);
                                                                            }
                                                                        });
                                                        builder.create().show();// 创建对话框并且显示该对话框
                                                    } else {
                                                        Toast.makeText(mActivity, "F码错误或已被使用", Toast.LENGTH_SHORT).show();
                                                        mActivity.hide_keyboard(view);
                                                    }
                                                } catch (Exception e) {
                                                }
                                            }, error -> mActivity.hideProcessDialog(1), paramap);
                                    mRequestQueue.add(request);
                                }
                            }).setNegativeButton("取消", null).show();
        }
    }

    protected void addToCart() {
        final Map paramap = new HashMap();
        final SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        final String cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("cart_mobile_ids", cart_mobile_ids);
        paramap.put("goods_id", goods_id);
        paramap.put("count", goods_count + "");
        paramap.put("price", cartprice + "");
        paramap.put("gsp", cartgsp);
        paramap.put("area_id", area_id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/add_goods_cart.htm",
                result -> {
                    try {
                        int code = result.getInt("code");
                        //Log.e("area_id", paramap.toString());
                        if (code == 0 || code == 1 || code == 2) {
                            if ("".equals(user_id)) {
                                String cart_mobile_id = result.getString("cart_id");
                                if (!cart_mobile_id.equals("")) {
                                    Editor editor = preferences.edit();
                                    editor.putString("cart_mobile_ids", cart_mobile_ids + cart_mobile_id + ",");
                                    editor.commit();
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity).setTitle("添加成功！").setMessage("商品已成功加入购物车").setPositiveButton("去购物车", (dialog1, which) -> {
                                        if (FastDoubleClickTools.isFastDoubleClick()) {
                                            dialog1.dismiss();// 关闭对话框
                                            mActivity.go_cart();
                                        }
                                    }
                            ).setNegativeButton("再逛逛", (dialog1, which) -> {
                                        if (FastDoubleClickTools.isFastDoubleClick()) {
                                            dialog1.dismiss();// 关闭对话框
                                        }
                                    }
                            );
                            builder.create().show();// 创建对话框并且显示该对话框
                        }
                        if (code == -3) {
                            Toast.makeText(mActivity, "库存不足", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -4) {
                            Toast.makeText(mActivity, "超过限购数量", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -5) {
                            Toast.makeText(mActivity, "限购商品,请登录", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -1) {
                            Toast.makeText(mActivity, "添加失败", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -2) {
                            Toast.makeText(mActivity, "商品已下架", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -7) {
                            Toast.makeText(mActivity, "购买数量必须大于商品的最低购买数量", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -8) {
                            Toast.makeText(mActivity, "购买数量必须小于商品的最高购买数量\n", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -9) {
                            Toast.makeText(mActivity, "当前用户等级低于该商品购买等级", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

    private void setActivityWord() {

//        final TextView activity = (TextView) rootView.findViewById(R.id.youhuiinfo);
//        Map paramap = new HashMap();
//        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
//        String user_id = preferences.getString("user_id", "");
//        String token = preferences.getString("token", "");
//        if ("".equals(user_id)) {
//            activity.setText("当前商品为促销商品，用户登录后方可享受促销价格！");
//        } else {
//            paramap.put("goods_id", goods_id);
//            paramap.put("user_id", user_id);
//            paramap.put("token", token);
//            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
//            Request<JSONObject> request = new NormalPostRequest(mActivity,
//                    mActivity.getAddress()
//                            + "/app/query_goodsActivity_price.htm",
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject result) {
//                            try {
//                                Log.i("test",result.toString());
////                                activity.setText("您当前为" + result.getString("level_name") + "，享受商城价格" + result.getString("act_rate") + "折的优惠");
//                                //TODO 现价
//                                textview = (TextView) rootView.findViewById(R.id.current_Price);
//                                String price = result.get("act_price").toString();
//                                textview.setText("¥" + mActivity.moneytodouble(price));// 现价
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    mActivity.hideProcessDialog(1);
//                }
//            }, paramap);
//            mRequestQueue.add(request);
//        }
    }

    private void favourite() {

        Map paramap = new HashMap();
        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        paramap.put("goods_id", goods_id);
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("type", favourite_statue);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_favorite_save.htm",
                result -> {
                    try {
                        int resultint = result.getInt("code");
                        ImageView imageview = (ImageView) rootView.findViewById(R.id.favourite_img);
                        if (resultint == 200) {// 添加成功
                            favourite_statue = "del";
                            textview = (TextView) rootView.findViewById(R.id.favourite_word);
                            textview.setText("已收藏");
                            imageview.setImageResource(R.mipmap.favourited);
                        }
                        if (resultint == 300) {// 删除成功
                            favourite_statue = "add";
                            textview = (TextView) rootView.findViewById(R.id.favourite_word);
                            textview.setText("收藏");
                            imageview.setImageResource(R.mipmap.favourite);
                        }
                        if (resultint == 100) {
                            Toast.makeText(mActivity, "收藏成功", Toast.LENGTH_SHORT).show();
                        }

                        if (resultint == -500)
                            Toast.makeText(mActivity, "请求错误", Toast.LENGTH_SHORT).show();
                        if (resultint == -400)
                            Toast.makeText(mActivity, "用户信息错误", Toast.LENGTH_SHORT).show();
                        if (resultint == -300)
                            Toast.makeText(mActivity, "已经收藏过该商品", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

    void getArea(String id) {
        mActivity.showProcessDialog();
        Map paraMap = new HashMap();
        if (id != null) {
            paraMap.put("id", id);
        } else {
            deliverstr = "";
        }

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/area_load.htm",
                result -> {
                    try {
                        JSONArray nameList = result.getJSONArray("area_list");
                        int length = nameList.length();
                        namelist = new ArrayList();
                        idlist = new ArrayList();
                        for (int i = 0; i < length; i++) {// 遍历JSONArray
                            JSONObject oj = nameList.getJSONObject(i);
                            namelist.add(oj.getString("name"));
                            idlist.add(oj.getInt("id"));
                        }

                        final String city_name[] = new String[namelist.size()];
                        for (int i = 0, j = namelist.size(); i < j; i++) {
                            city_name[i] = namelist.get(i);
                        }

                        dialog = MyDialog.showAlert(mActivity, "配送至", city_name, (adapterView, view, i, l) -> {
                            dialog.dismiss();
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                if (deliverstr.split(">").length == 2) {
                                    deliverstr += city_name[i];
                                    //getTransFee(city_name[i]);
                                } else {
                                    deliverstr += city_name[i] + ">";
                                }
                                if (deliverstr.split(">").length == 3) {
                                    distributionTo.setText(deliverstr);
                                    area_id = idlist.get(i).toString();
//                                            mActivity.setCache("current_city", deliverstr);
                                    mActivity.setCache("area_id", area_id);
                                    setSpec();
                                } else {
                                    getArea(idlist.get(i).toString());
                                }
                            }
                        });

                        mActivity.hideProcessDialog(0);
                        dialog.show();// 创建对话框并且显示该对话框
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);

    }


    public void getTransFee(String area_id) {
//        Map para = new HashMap();
//        para.put("goods_id", goods_id);
//        para.put("current_city", area_id);
//
//        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
//        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_trans_fee.htm",
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject result) {
//                        textview = (TextView) rootView.findViewById(R.id.distributionFee);
//                        try {
//                            textview.setText("" + result.getString("trans_information"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        mActivity.hideProcessDialog(1);
//                    }
//                }, para);
//        mRequestQueue.add(request);
    }

    public void easybuy() {
        if (mActivity.islogin()) {
            Map paramap = new HashMap();
            final SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
            final String user_id = preferences.getString("user_id", "");
            String token = preferences.getString("token", "");
            final String cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
            paramap.put("user_id", user_id);
            paramap.put("token", token);
            paramap.put("cart_mobile_ids", cart_mobile_ids);
            paramap.put("goods_id", goods_id);
            paramap.put("count", goods_count + "");
            paramap.put("price", cartprice + "");
            paramap.put("gsp", cartgsp);
            paramap.put("area_id", area_id);

            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/add_goods_cart.htm",
                    result -> {
                        try {
                            int code = result.getInt("code");
                            if (code == 0 || code == 1 || code == 2) {
                                String cart_mobile_id = result.getString("cart_id");
                                mActivity.go_cart2(cart_mobile_id);
                            }
                            if (code == -3) {
                                Toast.makeText(mActivity, "库存不足",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code == -4) {
                                Toast.makeText(mActivity, "超过限购数量",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code == -5) {
                                Toast.makeText(mActivity, "限购商品,请登录",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code == -1) {
                                Toast.makeText(mActivity, "添加失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code == -2) {
                                Toast.makeText(mActivity, "商品已下架",
                                        Toast.LENGTH_SHORT).show();
                            }

                            if (code == -7) {
                                Toast.makeText(mActivity, "购买数量必须大于商品的最低购买数量",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code == -8) {
                                Toast.makeText(mActivity, "购买数量必须小于商品的最高购买数量\n",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code == -9) {
                                Toast.makeText(mActivity, "当前用户等级低于该商品购买等级",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> mActivity.hideProcessDialog(1), paramap);
            mRequestQueue.add(request);
        } else {
            mActivity.go_login();
        }
    }

    private void setSpec() {
        Map paramap = mActivity.getParaMap();
        paramap.put("id", goods_id);
        if (cartgsp != "") {
            paramap.put("gsp", cartgsp);
        }
        paramap.put("area_id", area_id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/load_goods_gsp.htm",
                result -> {
                    try {
                        count = result.getInt("count");
                        String act_price1 = result.get("act_price").toString();
                        //TODO 现价
                        TextView textview1 = (TextView) rootView.findViewById(R.id.current_Price);
                        if (Double.valueOf(act_price1) > 0) {

                            if (advance_ding.equals("")) {
                                textview1.setText("￥" + mActivity.moneytodouble(act_price1));
                            } else {
                                textview1.setText("￥" + advance_ding + ".0");
                            }
                        } else {
                            if (!advance_ding.equals("")) {
                                textview1.setText("￥" + advance_ding + ".0");
                            }
                        }
                        textview1 = (TextView) rootView.findViewById(R.id.inventory);


                        if (count > 0) {
                            if (count > 20) {
                                textview1.setText("库存： 现货");// 库存
                            } else {
                                textview1.setText("库存： " + count + "件");// 库存
                            }
                            addtocart.setClickable(true);
                            addtocart.setBackgroundResource(R.color.light_yellow);
                            buyNow.setClickable(true);
                            buyNow.setBackgroundResource(R.color.red);
                        } else {
                            textview1.setText("库存： 无货");
                            addtocart.setClickable(false);
                            addtocart.setBackgroundResource(R.color.dark_gray);
                            buyNow.setClickable(false);
                            buyNow.setBackgroundResource(R.color.dark_gray);
                        }
                        GoodsFragment.this.goods_count = goods_count;
                        GoodsFragment.this.cartgsp = cartgsp;

                        cartprice = result.get("price").toString();
                        //TODO 现价
                        textview1 = (TextView) rootView.findViewById(R.id.current_Price);
                        textview1.setText("￥" + mActivity.moneytodouble(cartprice + ""));

                        if (Double.valueOf(act_price1) > 0) {
                            if (advance_ding.equals("")) {
                                textview1.setText("￥" + mActivity.moneytodouble(act_price1));
                                cartprice = mActivity.moneytodouble(act_price1);
                            } else {
                                textview1.setText("￥" + mActivity.moneytodouble(advance_ding));
                            }

                        } else {
                            if (!advance_ding.equals("")) {
                                textview1.setText("￥" + mActivity.moneytodouble(advance_ding));
                            }
                        }
                        textview1 = (TextView) rootView.findViewById(R.id.inventory);
                        if (count > 0) {
                            if (count > 20) {
                                textview1.setText("库存： 现货");// 库存
                            } else {
                                textview1.setText("库存： " + count + "件");// 库存
                            }
                            addtocart.setClickable(true);
                            addtocart.setBackgroundResource(R.color.light_yellow);
                            buyNow.setClickable(true);
                            buyNow.setBackgroundResource(R.color.red);
                        } else {
                            textview1.setText("库存： 无货");
                            addtocart.setClickable(false);
                            addtocart.setBackgroundResource(R.color.dark_gray);
                            buyNow.setClickable(false);
                            buyNow.setBackgroundResource(R.color.dark_gray);
                        }
                        rootView.findViewById(R.id.layout_promotional_info).setOnClickListener(v -> {
                                            TextView youhui = (TextView) rootView.findViewById(R.id.youhui);
                                            if (youhui.getText().equals("组合")) {
                                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                                    bundle.putDouble("goods_price", Double.valueOf(cartprice));
                                                    bundle.putString("count", count + "");
                                                    mActivity.go_goods_combine(bundle);
                                                }
                                            }
                                            if (youhui.getText().equals("预售")) {
                                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                                    final Dialog dlg = new Dialog(mActivity, R.style.my_dialog);
                                                    LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_layout_show, null);
                                                    TextView textView = (TextView) layout.findViewById(R.id.title_show);
                                                    textView.setText("预售详情");
                                                    textView.setTextColor(getResources().getColor(R.color.black));
                                                    layout.findViewById(R.id.layout_button_show).setVisibility(View.VISIBLE);
                                                    layout.findViewById(R.id.layout_message_show).setVisibility(View.GONE);
                                                    final ListView list = (ListView) layout.findViewById(R.id.layout_listview_show);
                                                    list.setVisibility(View.VISIBLE);
                                                    AlertDialogDetailsAdapter adapter = new AlertDialogDetailsAdapter(mActivity, invoiceString);
                                                    list.setAdapter(adapter);

                                                    dlg.setCanceledOnTouchOutside(true);
                                                    dlg.setContentView(layout);
                                                    dlg.show();
                                                    Button button_confirm = (Button) layout.findViewById(R.id.button_confirm_show);
                                                    View v_line =  layout.findViewById(R.id.v_line);
                                                    Button button_cancel = (Button) layout.findViewById(R.id.button_cancel_show);
                                                    button_confirm.setOnClickListener(view -> dlg.dismiss());
                                                    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                    layoutParams.height = BaseActivity.dp2px(mActivity, 40);
                                                    button_confirm.setLayoutParams(layoutParams);
                                                    v_line.setVisibility(View.GONE);
                                                    button_cancel.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

    public void setGoodsContainerFragment(GoodsContainerFragment goodsContainerFragment) {
        this.goodsContainerFragment = goodsContainerFragment;
    }


    //微信分享
    int wxShareType = 0;//0微信好友分享1微信朋友圈分享

    //微信分享
    public void share_to_wechat(final int type) {

        Log.i("test", share_goods_img);
        if (mActivity.mwxapi == null) {
            mActivity.mwxapi = WXAPIFactory.createWXAPI(mActivity, Constants.WECHAT_API_KEY);
        }
        if (!mActivity.mwxapi.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信客户端,无法进行分享", Toast.LENGTH_SHORT).show();
            return;
        }
        if (share_goods_img != null && !share_goods_img.equals("")) {
            wxShareType = type;
            new MyTask().execute(share_goods_img);
        } else {
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            share_wx(thumb, type);
        }
    }


    private void share_wx(Bitmap thumb, int type) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = CommonUtil.getAddress(mActivity) + "/wap/goods.htm?id=" + goods_id;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = share_goods_name;
        msg.description = getString(R.string.share_tip);
        if (thumb != null) {
            msg.thumbData = ShareWetcharImageUtils.bmpToByteArray(thumb, false);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareWetcharImageUtils.buildTransaction("webpage");
        req.message = msg;
        req.scene = type == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        mActivity.mwxapi.sendReq(req);
    }

    class MyTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            return ShareWetcharImageUtils.getBitmapFromURL(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            share_wx(bitmap, wxShareType);
            super.onPostExecute(bitmap);
        }
    }


    //QQ好友、QQ空间分享
    public void qq_share(int type) {
        if (mActivity.mTencent == null) {
            mActivity.mTencent = Tencent.createInstance(Constants.QQ_API_KEY, mActivity.getApplicationContext());
        }
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, share_goods_name);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, getString(R.string.share_tip));
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, CommonUtil.getAddress(mActivity) + "/wap/goods.htm?id=" + goods_id);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share_goods_img);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
        if (type == 0)
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        else
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mActivity.mTencent.shareToQQ(mActivity, params, mActivity);
    }


}
