package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.CartListAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Mode;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 购物车首页
 */
public class Cart1Fragment extends Fragment {

    private static Cart1Fragment fragment = null;
    private BaseActivity mActivity;
    private Set<String> chosen_list;// 选中的id
    private Set<String> all_cartslist;// 所有购物车id
    private Set<CheckBox> checkboxlist;// 勾选按钮
    private List<Map> cart_list;
    private List<String> discount_List;// 优惠活动列表
    private Button checkout;// 结算按钮
    private ListView cartlist;// 购物车列表
    private CheckBox checkBoxAll;// 全选
    private View rootView;
    private CartListAdapter myadapter;
    private String cart_mobile_ids;
    private PullToRefreshListView mPullRefreshListView;


    //静态工厂方法
    public static Cart1Fragment getInstance() {
        if (fragment == null) {
            fragment = new Cart1Fragment();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart1, container, false);
        mActivity = (BaseActivity) getActivity();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);//设置toolbar

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
            toolbar.setNavigationOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                }
            });
        }


        checkBoxAll = (CheckBox) rootView.findViewById(R.id.checkBoxAll);

        checkout = (Button) rootView.findViewById(R.id.checkout);
        mPullRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.car_list);
        chosen_list = new HashSet<String>();
        all_cartslist = new HashSet<String>();
        checkboxlist = new HashSet<CheckBox>();
        cart_list = new ArrayList<Map>();
        discount_List = new ArrayList();
        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        mPullRefreshListView
                .setOnRefreshListener(refreshView -> {
                    refresh();
                });

        cartlist = mPullRefreshListView.getRefreshableView();


        myadapter = new CartListAdapter(mActivity, Cart1Fragment.this, cart_list, chosen_list, checkboxlist);
        cartlist.setAdapter(myadapter);
        cartlist.setDividerHeight(BaseActivity.dp2px(mActivity, 0.3f));
        View footer = new View(mActivity);
        footer.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BaseActivity.dp2px(getActivity(), 56)));
        cartlist.addFooterView(footer);


        // 返回首页
        rootView.findViewById(R.id.gotoindex).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_index();
                    }
                });
        // 全选
        rootView.findViewById(R.id.check_area).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                boolean b = checkBoxAll.isChecked();
                checkBoxAll.setChecked(!b);
                b = !b;
                for (CheckBox box : checkboxlist) {
                    box.setChecked(b);
                }
                checkBoxAll.setChecked(b);
                if (b) {
                    chosen_list.addAll(all_cartslist);
                } else {
                    chosen_list.clear();
                }
                sumMoney();
            }
        });
        // 提交订单
        checkout.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (mActivity.islogin()) {
                    String select_ids = getSelectedIds();
                    SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                    boolean flag = true;
                    if (preferences.contains("inventory_ids") && !(mActivity.getCache("inventory_ids").equals(""))) {
                        String[] s = mActivity.getCache("inventory_ids").split(",");
                        String[] ids = select_ids.split(",");
                        for (int i = 0; i < s.length; i++) {
                            for (int j = 0; j < ids.length; j++) {
                                if (s[i].equals(ids[j])) {
                                    flag = false;
                                }
                            }
                        }
                        Log.e("+", flag + "--");
                    }
                    if (flag) {
                        mActivity.go_cart2(select_ids);
                    } else {
                        Toast.makeText(mActivity, "所选商品库存不足", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mActivity.go_login();
                }
            }
        });

        final TextView edit = (TextView) rootView.findViewById(R.id.cart_edit);
        edit.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (myadapter.getEdit_status() == 0) {
                    rootView.findViewById(R.id.relativeLayout_submint).setVisibility(View.GONE);
                    rootView.findViewById(R.id.relativeLayout_edit).setVisibility(View.VISIBLE);

                    edit.setText(getString(R.string.finish));
                    myadapter.setEdit_status(1);
                    myadapter.notifyDataSetChanged();
                } else {
                    rootView.findViewById(R.id.relativeLayout_submint).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.relativeLayout_edit).setVisibility(View.GONE);
                    mActivity.hide_keyboard(v);
                    edit.setText(getString(R.string.edit));
                    myadapter.setEdit_status(0);
                    myadapter.notifyDataSetChanged();
                }
            }
        });

        // 删除购物车
        rootView.findViewById(R.id.delete).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        String delete_str = getSelectedIds();
                        if (delete_str.length() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                                    .setTitle("请问您是否要删除该商品？")
                                    .setPositiveButton("确定", (dialog, which) -> {
                                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                                    dialog.dismiss();// 关闭对话框
                                                    String delete_ids = getSelectedIds();
                                                    delete_cart(delete_ids);
                                                }
                                            })
                                    .setNegativeButton("取消",
                                            (dialog, which) -> {
                                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                                    dialog.dismiss();// 关闭对话框
                                                }
                                            });
                            builder.create().show();// 创建对话框并且显示该对话框
                        }
                    }
                });

        // 登陆同步购物车
        rootView.findViewById(R.id.cart_top_login).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_login();
                    }
                });

        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {


        if (!hidden) {
            if (rootView != null) {
                rootView.findViewById(R.id.relativeLayout_submint).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.relativeLayout_edit).setVisibility(View.GONE);

                TextView edit = (TextView) rootView.findViewById(R.id.cart_edit);
                edit.setText(getString(R.string.edit));
                chosen_list.clear();
                myadapter.setEdit_status(0);
                myadapter.notifyDataSetChanged();
                chosen_list.clear();
                refresh();
            }
        }
        super.onHiddenChanged(hidden);
    }

    void init() {

        Map paramap = new HashMap();
        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        Log.e("++", preferences.contains("inventory_ids") + "++");
        if (preferences.contains("inventory_ids")) {
            Log.e("++", preferences.getString("inventory_ids", "++"));
        }
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        if (user_id != null && !user_id.equals("") && token != null && !token.equals("")) {
            rootView.findViewById(R.id.login_header).setVisibility(View.GONE);
            paramap.put("user_id", user_id);
            paramap.put("token", token);
        } else {
            rootView.findViewById(R.id.login_header).setVisibility(View.VISIBLE);
        }
        String cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
        paramap.put("cart_mobile_ids", cart_mobile_ids);
        paramap.put("selected_ids", getSelectedIds());

        mActivity.showProcessDialog();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_cart1.htm",
                result -> {
                    try {
                        all_cartslist.clear();
                        checkboxlist.clear();
                        cart_list.clear();
                        discount_List.clear();
                        JSONArray normal_cart_list = result.getJSONArray("cart_list");// 普通商品
                        for (int i = 0; i < normal_cart_list.length(); i++) {
                            JSONObject oj = normal_cart_list.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("goods_main_photo", oj.get("goods_main_photo") + "");
                            map.put("cart_id", oj.get("cart_id") + "");
                            map.put("goods_id", oj.get("goods_id") + "");
                            map.put("cart_price", oj.get("cart_price") + "");
                            map.put("goods_price", oj.get("goods_price") + "");
                            map.put("goods_status", oj.get("goods_status") + "");
                            map.put("goods_name", oj.get("goods_name") + "");
                            map.put("goods_spec", oj.get("goods_spec") + "");
                            map.put("goods_count", oj.get("goods_count") + "");
                            map.put("goods_spec_ids", oj.has("goods_spec_ids") ? oj.get("goods_spec_ids") + "" : "");
                            if (mActivity.islogin()) {
                                SharedPreferences preferences1 = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                                if (preferences1.contains("inventory_ids")) {
                                    String inventory_ids = preferences1.getString("inventory_ids", "");
                                    String[] strings = inventory_ids.split(",");
                                    map.put("inventory_ids", "0");
                                    for (int i1 = 0; i1 < strings.length; i1++) {
                                        if (strings[i1].equals(oj.get("cart_id") + "")) {
                                            map.put("inventory_ids", "1");
                                        }
                                    }
                                } else {
                                    map.put("inventory_ids", "0");
                                }
                            } else {
                                map.put("inventory_ids", "0");
                            }
                            cart_list.add(map);
                            all_cartslist.add(oj.getString("cart_id"));
                        }
                        JSONArray discount_list = result
                                .getJSONArray("discount_list");// 活动列表
                        for (int i = 0; i < discount_list.length(); i++) {// 遍历活动
                            Map discount_map = new HashMap();

                            String key = (String) discount_list.get(i);
                            discount_List.add(key);
                            JSONObject obj = result.getJSONObject(key);
                            String info = obj.getString("info");
                            JSONArray discount_cartlist = obj.getJSONArray("goods_list");// 活动购物车
                            List discount_cart_list = new ArrayList();
                            for (int j = 0; j < discount_cartlist.length(); j++) {
                                JSONObject oj = discount_cartlist.getJSONObject(j);
                                Map map = new HashMap();
                                map.put("goods_main_photo", oj.get("goods_main_photo") + "");
                                map.put("cart_id", oj.get("cart_id") + "");
                                map.put("goods_id", oj.get("goods_id") + "");
                                map.put("cart_price", oj.get("cart_price") + "");
                                map.put("goods_price", oj.get("goods_price") + "");
                                map.put("goods_status", oj.get("goods_status") + "");
                                map.put("goods_name", oj.get("goods_name") + "");
                                map.put("goods_spec", oj.get("goods_spec") + "");
                                map.put("goods_count", oj.get("goods_count") + "");
                                map.put("goods_spec_ids", oj.has("goods_spec_ids") ? oj.get("goods_spec_ids") + "" : "");
                                if (mActivity.islogin()) {
                                    SharedPreferences preferences1 = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                                    if (preferences1.contains("inventory_ids")) {
                                        String inventory_ids = preferences1.getString("inventory_ids", "");
                                        String[] strings = inventory_ids.split(",");
                                        map.put("inventory_ids", "0");
                                        for (int i1 = 0; i1 < strings.length; i1++) {
                                            if (strings[i1].equals(oj.get("cart_id") + "")) {
                                                map.put("inventory_ids", "1");
                                            }
                                        }
                                    } else {
                                        map.put("inventory_ids", "0");
                                    }
                                } else {
                                    map.put("inventory_ids", "0");
                                }
                                discount_cart_list.add(map);
                                all_cartslist.add(oj.get("cart_id") + "");

                                if (oj.has("suit_info")) {
                                    List suit_list = new ArrayList();
                                    JSONObject suit_info = oj.getJSONObject("suit_info");
                                    JSONArray combine_suit = suit_info.getJSONArray("goods_list");
                                    for (int k = 0; k < combine_suit.length(); k++) {
                                        JSONObject suit = combine_suit.getJSONObject(k);
                                        Map stuit_map = new HashMap();
                                        stuit_map.put("goods_name", suit.get("name") + "");
                                        stuit_map.put("goods_main_photo", mActivity.getAddress() + "/" + suit.get("img"));
                                        suit_list.add(stuit_map);
                                    }
                                    discount_map.put("suit_info", suit_list);
                                }
                            }

                            String[] str = key.split("_");
                            discount_map.put("name", key);
                            discount_map.put("type", str[0]);
                            discount_map.put("id", str[1]);
                            discount_map.put("info", info);
                            discount_map.put("cart_price", obj.get("cart_price"));// 小计
                            discount_map.put("reduce", obj.get("reduce"));// 满减
                            discount_map.put("cart_list", discount_cart_list);

                            if (obj.has("whether_enough")) {
                                String whether_enough = obj.get("whether_enough").toString();
                                discount_map.put("whether_enough", whether_enough);

                                List gift_list = new ArrayList();
                                JSONArray gift_arr = obj.getJSONArray("gift_list");
                                for (int n = 0; n < gift_arr.length(); n++) {
                                    JSONObject gift = gift_arr.getJSONObject(n);
                                    Map map = new HashMap();
                                    map.put("goods_main_photo", gift.get("goods_main_photo"));
                                    map.put("goods_id", gift.get("goods_id"));
                                    map.put("goods_name", gift.get("goods_name"));
                                    map.put("goods_price", gift.get("goods_price"));
                                    gift_list.add(map);
                                }
                                discount_map.put("gift_list", gift_list);

                                if (whether_enough.equals("2")) {
                                    JSONObject gift = obj.getJSONObject("gift");
                                    Map gift_map = new HashMap();
                                    gift_map.put("goods_name", gift.get("goods_name"));
                                    gift_map.put("goods_id", gift.get("goods_id"));
                                    gift_map.put("goods_main_photo", gift.get("goods_main_photo"));
                                    discount_map.put("gift", gift_map);
                                }
                            }

                            cart_list.add(discount_map);
                        }
                        IndexNavigatiorFragment.setbadge(all_cartslist.size());
                        if (all_cartslist.size() == 0) {
                            rootView.findViewById(R.id.car_bottom).setVisibility(View.GONE);
                            rootView.findViewById(R.id.cart_edit).setVisibility(View.GONE);
                            rootView.findViewById(R.id.cartempty).setVisibility(View.VISIBLE);
                        } else {
                            rootView.findViewById(R.id.car_bottom).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.cart_edit).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.car_list).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.cartempty).setVisibility(View.GONE);
                            // 添加新加载的信息
                            if (myadapter!=null) {
                                myadapter.notifyDataSetChanged();
                            }

                        }
                        mPullRefreshListView.onRefreshComplete();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mActivity.hideProcessDialog(0);
                }, error -> {
                    checkout.setClickable(false);
                    checkout.setBackgroundResource(R.color.dark_gray);
                    checkout.setText("结算(0)");
                    mActivity.hideProcessDialog(1);
                }, paramap);
        mRequestQueue.add(request);

    }

    @Override
    public void onResume() {

        refresh();
        super.onResume();
    }

    private String getSelectedIds() {

        String cart_ids = "";
        for (String id : chosen_list) {
            cart_ids += id + ",";
        }
        if (cart_ids.length() > 0)
            cart_ids = cart_ids.substring(0, cart_ids.length() - 1);

        return cart_ids;
    }

    public void sumMoney() {
        String cart_ids = getSelectedIds();
        Map paramap = new HashMap();
        paramap.put("cart_ids", cart_ids);
        mActivity.showProcessDialog();

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/compute_cart.htm",
                result -> {
                    try {
                        String select_cart_price = result.getString("select_cart_price");
                        TextView total_price = (TextView) rootView.findViewById(R.id.total_price);
                        total_price.setText(mActivity.moneytodouble(select_cart_price));

                        int num = result.getInt("select_cart_number");
                        if (num != 0 && num == all_cartslist.size())
                            checkBoxAll.setChecked(true);
                        else {
                            checkBoxAll.setChecked(false);
                        }
                        if (num == 0) {
                            checkout.setClickable(false);
                            checkout.setBackgroundResource(R.color.dark_gray);
                        } else {
                            checkout.setClickable(true);
                            checkout.setBackgroundResource(R.color.toolbar_color);
                        }
                        checkout.setText("结算(" + num + ")");
                        init();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                        mActivity.hideProcessDialog(0);
                }, error -> {
                    checkout.setClickable(false);
                    checkout.setBackgroundResource(R.color.dark_gray);
                    checkout.setText("结算(0)");
                    mActivity.hideProcessDialog(1);
                }, paramap);
        mRequestQueue.add(request);

    }

    public void refresh() {
        init();
        sumMoney();
    }

    private void delete_cart(final String delete_ids) {

        mActivity.showProcessDialog();
        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
        Map paramap = new HashMap();
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("cart_mobile_ids", cart_mobile_ids);

//        Log.i("test", "删除请求参数cart_mobile_ids==" + cart_mobile_ids+",cart_ids=="+delete_ids);

        paramap.put("cart_ids", delete_ids);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/remove_goods_cart.htm",
                result -> {
                    try {
                        Log.i("test","删除请求返回数据"+result.toString());
                        int code = result.getInt("code");
                        String str = result.getString("dele_cart_mobile_ids");
                        String[] strr = str.split(",");
                        if (code == 100) {
                            for (String s : strr) {
                                if (cart_mobile_ids.contains(s)) {
                                    cart_mobile_ids = cart_mobile_ids.replaceAll(s + ",", "");
                                }
                                if (chosen_list.contains(delete_ids)) {
                                    chosen_list.remove(delete_ids);
                                }
                            }
                            refresh();
                        } else {
                            Toast.makeText(mActivity, "删除失败，请稍候重试",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }
}
