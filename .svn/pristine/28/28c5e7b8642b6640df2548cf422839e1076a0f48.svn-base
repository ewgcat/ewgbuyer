package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Mode;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
    购物车
 */
public class IntegralCartFragment1 extends Fragment {

    private BaseActivity mActivity;
    private Set<String> chosen_list;// 选中的id
    private Set<String> all_cartslist;// 所有购物车id
    private Set<CheckBox> checkboxlist;// 勾选按钮
    private List<Map> cart_list;
    private Button checkout;// 结算按钮
    private ListView cartlist;// 购物车列表
    private CheckBox checkBoxAll;// 全选
    private View rootView;
    private CartListAdapter myadapter;
    private int user_integral;
    private int all_integral;
    private double all_shipfee;
    private PullToRefreshListView mPullRefreshListView;
    private String order_total_price;
    private String goods_total_price;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart1, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });


        rootView.findViewById(R.id.total_price_tag).setVisibility(View.GONE);
        checkBoxAll = (CheckBox) rootView.findViewById(R.id.checkBoxAll);
        checkout = (Button) rootView.findViewById(R.id.checkout);
        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.car_list);
        chosen_list = new HashSet<String>();
        all_cartslist = new HashSet<String>();
        checkboxlist = new HashSet<CheckBox>();
        cart_list = new ArrayList<Map>();

        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(refreshView -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                all_cartslist.clear();
                checkboxlist.clear();
                cart_list.clear();
                init();
            }
        });

        cartlist = mPullRefreshListView.getRefreshableView();
        myadapter = new CartListAdapter(mActivity, cart_list);
        cartlist.setAdapter(myadapter);
        cartlist.setDivider(null);
        sumMoney();

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
                if (all_integral != 0 && user_integral > all_integral) {
                    mActivity.go_integral_order(getSelectedIds(), all_integral, all_shipfee, goods_total_price, order_total_price);
                } else {
                    Toast.makeText(mActivity, "积分不足", Toast.LENGTH_SHORT).show();
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
                    Builder builder = new Builder(mActivity).setTitle("请问您是否要删除该商品？")
                            .setPositiveButton("确定", (dialog, which) -> {
                                dialog.dismiss();// 关闭对话框
                                String delete_ids = getSelectedIds();
                                delete_cart(delete_ids);
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                dialog.dismiss();// 关闭对话框
                            });
                    builder.create().show();// 创建对话框并且显示该对话框
                }
            }
        });
        return rootView;
    }

    void init() {
        mActivity.showProcessDialog();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/integral_cartlist.htm",
                result -> {
                    cart_list.clear();
                    try {
                        user_integral = result.getInt("user_integral");
                        JSONArray j_list = result.getJSONArray("cart_list");
                        for (int i = 0; i < j_list.length(); i++) {
                            JSONObject obj = j_list.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("id", obj.get("id"));
                            map.put("ig_goods_img", obj.get("ig_goods_img"));
                            map.put("ig_goods_name", obj.get("ig_goods_name"));
                            map.put("trans_fee", obj.get("trans_fee"));
                            map.put("count", obj.get("count"));
                            map.put("integral", obj.get("integral"));
                            if (obj.has("price") && !TextUtils.isEmpty(obj.get("price").toString().trim())) {
                                map.put("price", obj.get("price"));
                            }
                            cart_list.add(map);
                        }
                        if (cart_list.size() == 0) {
                            mPullRefreshListView.setVisibility(View.GONE);
                            rootView.findViewById(R.id.car_bottom).setVisibility(View.GONE);
                            rootView.findViewById(R.id.cartempty).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.gotoindex).setVisibility(View.GONE);
                        } else {
                            mPullRefreshListView.setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.car_bottom).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.cartempty).setVisibility(View.GONE);
                            rootView.findViewById(R.id.gotoindex).setVisibility(View.GONE);
                        }
                        myadapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                    mPullRefreshListView.onRefreshComplete();
                }, error -> {
                    mPullRefreshListView.setVisibility(View.GONE);
                    checkout.setClickable(false);
                    checkout.setBackgroundResource(R.color.dark_gray);
                    checkout.setText("结算(0)");
                    mActivity.hideProcessDialog(1);
                }, mActivity.getParaMap());
        mRequestQueue.add(request);

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

    private void sumMoney() {
        String cart_ids = getSelectedIds();
        Map paramap = new HashMap();
        paramap.put("cart_ids", cart_ids);
        mActivity.showProcessDialog();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/integral_cart_cal.htm",
                result -> {
                    try {
                        Log.i("test", result.toString());
                        TextView total_price = (TextView) rootView.findViewById(R.id.total_price);
                        all_integral = result.getInt("all_integral");
                        all_shipfee = result.getDouble("all_shipfee");
                        if (result.has("code") && "-300".equals(result.getString("code"))) {
                            if (result.has("ig_name")) {
                                Toast.makeText(mActivity, "商品 " + result.getString("ig_name") + "已下架", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (result.has("goods_total_price")) {
                                goods_total_price = result.getString("goods_total_price");
                                if (result.has("order_total_price")) {
                                    order_total_price = result.getString("order_total_price");
                                    total_price.setText("积分:" + all_integral
                                            + "\n商品总金额：" + mActivity.moneytodouble(goods_total_price)
                                            + "\n运费:￥" + mActivity.moneytodouble(all_shipfee + "")
                                            + "\n订单总金额：" + mActivity.moneytodouble(order_total_price)
                                    );
                                }
                            } else {
                                total_price.setText("积分:" + all_integral
                                        + "\n运费:￥" + mActivity.moneytodouble(all_shipfee + "")
                                );
                            }
                            int num = result.getInt("size");
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
                            checkout.setText("结算（" + num + "）");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    init();
                }, error -> {
                    checkout.setClickable(false);
                    checkout.setBackgroundResource(R.color.dark_gray);
                    checkout.setText("结算(0)");
                    mActivity.hideProcessDialog(1);
                }, paramap);
        mRequestQueue.add(request);

    }

    private void delete_cart(final String delete_ids) {
        Map paramap = mActivity.getParaMap();
        paramap.put("cart_ids", delete_ids);
        mActivity.showProcessDialog();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/integral_cart_del.htm",
                result -> {
                    try {
                        int code = result.getInt("code");
                        String str = result.getString("dele_cart_ids");
                        String[] strr = str.split(",");
                        if (code == 100) {
                            for (String s : strr) {
                                if (chosen_list.contains(delete_ids)) {
                                    chosen_list.remove(delete_ids);
                                }
                            }
                            all_cartslist.clear();
                            checkboxlist.clear();
                            cart_list.clear();
                            init();
                            sumMoney();
                        } else {
                            Toast.makeText(mActivity, "删除失败，请稍候重试", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] str = {};
            // 这里可以写查询事件
            all_cartslist.clear();
            checkboxlist.clear();
            cart_list.clear();
            init();
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // 添加新加载的信息

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    class CartListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        List<Map> cart_list;
        private int edit_status = 0;

        public CartListAdapter(Context context, List<Map> cart_list) {
            this.context = context;
            this.cart_list = cart_list;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public int getEdit_status() {
            return edit_status;
        }

        public void setEdit_status(int edit_status) {
            this.edit_status = edit_status;
        }


        @Override
        public int getCount() {
            return cart_list.size();
        }

        @Override
        public Object getItem(int position) {
            return cart_list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final int index = position;
            final Map map = cart_list.get(index);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_cart1_goods, null);
                holder = new ViewHolder();
                holder.goods_info = convertView.findViewById(R.id.goods_info);
                holder.goods_edit = convertView.findViewById(R.id.goods_edit);
                holder.goods_img = (SimpleDraweeView) convertView.findViewById(R.id.goods_img);
                holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
                //运费
                holder.goods_gsp = (TextView) convertView.findViewById(R.id.goods_gsp);
                holder.goods_gsp_edit = (TextView) convertView.findViewById(R.id.goods_gsp_edit);

                //数量
                holder.goods_count = (TextView) convertView.findViewById(R.id.goods_count);
                holder.buy_count = (EditText) convertView.findViewById(R.id.buy_count);
                //积分
                holder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);


                holder.minus = (ImageButton) convertView.findViewById(R.id.minus);
                holder.plus = (ImageButton) convertView.findViewById(R.id.plus);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                holder.check_area = convertView.findViewById(R.id.check_area);
                holder.layout_edit = (RelativeLayout) convertView.findViewById(R.id.layout_edit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.layout_edit.setVisibility(View.GONE);
            if (edit_status == 0) {
                holder.goods_info.setVisibility(View.VISIBLE);
                holder.goods_edit.setVisibility(View.GONE);
            } else {
                holder.goods_info.setVisibility(View.GONE);
                holder.goods_edit.setVisibility(View.VISIBLE);
            }

            final CheckBox now_checkbox = holder.checkBox;
            final String cartid = map.get("id").toString();
            BaseActivity.displayImage((String) map.get("ig_goods_img"), holder.goods_img);
            holder.goods_name.setText(map.get("ig_goods_name").toString());
            holder.goods_gsp.setText("运费：" + mActivity.moneytodouble(map.get("trans_fee").toString()));
            if (map.containsKey("price")) {
                holder.goods_price.setText(map.get("integral").toString() + "积分 +" + map.get("price").toString() + "元");
            } else {
                holder.goods_price.setText(map.get("integral").toString() + "积分");
            }

            final int count = Integer.parseInt(map.get("count").toString());
            final EditText cart_count = holder.buy_count;
            cart_count.setText(count + "");
            holder.goods_count.setText("×" + count);
            final ImageButton minus = holder.minus;

            cart_count.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String str = cart_count.getText().toString();
                    if (str.length() == 0) {
                        str = "0";
                    }
                    int count_edit = Integer.parseInt(str);
                    if (count_edit < 1) {
                        count_edit = 1;
                        cart_count.setText("1");
                    }
                    if (count_edit != count) {
                        chosen_list.add(cartid);
                        now_checkbox.setChecked(true);
                        cart_count_adjust(cartid, count_edit);
                    }
                }
                return false;
            });
            cart_count.setOnFocusChangeListener((v, hasFocus) -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    String str = cart_count.getText().toString();
                    if (str.length() == 0) {
                        str = "0";
                    }
                    int count_edit = Integer.parseInt(str);
                    if (count_edit < 1) {
                        count_edit = 1;
                        cart_count.setText("1");
                    }
                    if (!hasFocus && count_edit != count) {
                        chosen_list.add(cartid);
                        now_checkbox.setChecked(true);
                        cart_count_adjust(cartid, count_edit);
                    }
                }
            });
            cart_count.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int scount = 0;
                    if (s.toString().length() > 0) {
                        scount = Integer.parseInt(s.toString());
                        if (scount == 1) {
                            minus.setClickable(false);
                            minus.setImageResource(R.mipmap.minusgray);
                        } else {
                            minus.setClickable(true);
                            minus.setImageResource(R.mipmap.minus);
                        }
                    }
                }
            });
            holder.checkBox.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    if (((CheckBox) v).isChecked()) {
                        chosen_list.add(cartid);
                    } else {
                        chosen_list.remove(cartid);
                    }
                    sumMoney();
                }
            });
            holder.check_area.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    if (chosen_list.contains(cartid)) {
                        chosen_list.remove(cartid);
                        now_checkbox.setChecked(false);
                    } else {
                        chosen_list.add(cartid);
                        now_checkbox.setChecked(true);
                    }
                    sumMoney();
                }
            });
            all_cartslist.add(cartid);
            checkboxlist.add(holder.checkBox);
            if (chosen_list.contains(cartid)) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }

            holder.minus.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    int count1 = Integer.parseInt(cart_count.getText().toString());
                    chosen_list.add(cartid);
                    now_checkbox.setChecked(true);
                    cart_count_adjust(cartid, count1 - 1);
                }
            });
            holder.plus.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    int count12 = Integer.parseInt(cart_count.getText().toString());
                    chosen_list.add(cartid);
                    now_checkbox.setChecked(true);
                    cart_count_adjust(cartid, count12 + 1);
                }
            });
            if (count == 1) {
                holder.minus.setClickable(false);
                holder.minus.setImageResource(R.mipmap.minusgray);
            }
            return convertView;
        }

        void cart_count_adjust(String cart_id, final int count) {
            mActivity.showProcessDialog();
            if (count < 0) {
                Toast.makeText(mActivity, "参数错误", Toast.LENGTH_SHORT).show();
            } else {
                Map paramap = mActivity.getParaMap();
                paramap.put("cart_id", cart_id);
                paramap.put("count", count);
                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/integral_count_adjust.htm",
                        result -> {
                            mActivity.hideProcessDialog(0);
                            try {
                                int code = Integer.parseInt(result.get("code").toString());
                                if (code == 100) {
                                    init();
                                    sumMoney();
                                }else if (code == -100) {
                                    Toast.makeText(mActivity, "库存不足", Toast.LENGTH_SHORT).show();
                                }else if (code == -200) {
                                    Toast.makeText(mActivity, "无此商品", Toast.LENGTH_SHORT).show();
                                }else if (code == -300) {
                                    Toast.makeText(mActivity, "已为最大可兑换数量", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> mActivity.hideProcessDialog(1), paramap);
                mRequestQueue.add(request);

            }

        }

        public class ViewHolder {
            public SimpleDraweeView goods_img;
            public TextView goods_name;
            public TextView goods_price;
            public TextView goods_gsp;
            public TextView goods_gsp_edit;
            public TextView goods_count;
            public EditText buy_count;
            public ImageButton minus;
            public ImageButton plus;
            public CheckBox checkBox;
            public View check_area;
            public RelativeLayout layout_edit;
            public View goods_info;
            public View goods_edit;

        }
    }
}
