package com.ewgvip.buyer.android.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/2.  组合适配器
 */
public class CombineAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private List list;
    private List list_goods = new ArrayList();
    private int checkedbax_visible;
    private LayoutInflater layoutInflater;
    private CombineItemIAdapter combineItemIAdapter;
    private ViewHolder viewHolder = null;
    private LinearLayoutManager linearLayoutManager;
    private List<String> selected_list;
    private Map map;
    private int goods_count;
    Dialog alertDialog;

    public CombineAdapter(BaseActivity mActivity, List list, int checkedbax_visible) {
        this.mActivity = mActivity;
        this.list = list;
        this.checkedbax_visible = checkedbax_visible;
        layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.fragment_combine, null);
            viewHolder.tv_combinename = (TextView) view.findViewById(R.id.tv_combinename);
            viewHolder.tv_combine_total = (TextView) view.findViewById(R.id.tv_combine_total);
            viewHolder.tv_total_price = (TextView) view.findViewById(R.id.tv_total_price);
            viewHolder.lv_list_combine = (RecyclerView) view.findViewById(R.id.lv_list_combine);
            viewHolder.iv_combine_details = (ImageView) view.findViewById(R.id.iv_combine_details);
            viewHolder.rl_height = (RelativeLayout) view.findViewById(R.id.rl_height);
            viewHolder.ll_combine_item_one = (LinearLayout) view.findViewById(R.id.ll_combine_item_one);

            view.setTag(viewHolder);

            viewHolder.iv_combine_details.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        selected_list = new ArrayList<String>();
        final Map map = (Map) list.get(i);
        viewHolder.tv_combinename.setText("套餐" + (i + 1));
        viewHolder.tv_combine_total.setText("￥" + map.get("plan_price").toString());
        viewHolder.tv_total_price.setText("原价￥" + map.get("all_price").toString());
        viewHolder.tv_total_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tv_combine_total.setVisibility(View.VISIBLE);
        if (checkedbax_visible == 0) {

            viewHolder.tv_total_price.setVisibility(View.VISIBLE);
        } else {

            viewHolder.tv_total_price.setVisibility(View.GONE);
        }

        final String status = map.get("status") + "";
        list_goods = (List) map.get("goods_list");

        //为下拉按钮（整个item点击）添加点击事件 功能：查看商品详情，并且加入购物车
        viewHolder.rl_height.setOnClickListener(view12 -> {
            if (status.equals("0")) {
                CombineAdapter.this.notifyDataSetChanged();
                map.put("status", 1);
            } else {
                CombineAdapter.this.notifyDataSetChanged();
                map.put("status", 0);
            }
        });

        if (status.equals("0")) {

            viewHolder.iv_combine_details.setImageResource(R.mipmap.combine_select);
            viewHolder.lv_list_combine.setVisibility(View.VISIBLE);
            viewHolder.ll_combine_item_one.setVisibility(View.GONE);

        } else {
            viewHolder.iv_combine_details.setImageResource(R.mipmap.combine_selectup);
            viewHolder.lv_list_combine.setVisibility(View.GONE);
            viewHolder.ll_combine_item_one.setVisibility(View.VISIBLE);
            viewHolder.ll_combine_item_one.removeAllViews();

            //循环获取每一项的数据
            for (int n = 0; n < list_goods.size() + 1; n++) {
                //动态添加布局1
                final View view1 = layoutInflater.inflate(R.layout.item_itemone, null);
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view1.findViewById(R.id.iv_combine_items_details_pre);
                TextView textView = (TextView) view1.findViewById(R.id.tv_combine_context);
                final CheckBox checkBox = (CheckBox) view1.findViewById(R.id.check_combine_item);

                //第一个位置显示组合套装本品
                if (n == 0) {
                    Map map2 = (Map) list.get(n);
                    if (map2.containsKey("goods_img")) {
                        simpleDraweeView.setImageURI(Uri.parse(map2.get("goods_img").toString()));
                        textView.setText(map2.get("goods_name").toString());

                    }
                    if (map2.containsKey("goods_count")) {
                        goods_count = Integer.parseInt(map2.get("goods_count").toString());
                    }
                    //判断为配件页时，为第一个本品动态设置间距
                    if (checkedbax_visible == 1) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) simpleDraweeView.getLayoutParams();
                        params.leftMargin = BaseActivity.dp2px(mActivity, 40);
                        simpleDraweeView.setLayoutParams(params);
                    }

                }
                String goods_id = "";
                if (n > 0 && n <= list_goods.size()) {
                    Map map1 = (Map) list_goods.get(n - 1);
                    simpleDraweeView.setImageURI(Uri.parse(map1.get("goods_img").toString()));
                    textView.setText(map1.get("goods_name").toString());
                    goods_id = map1.get("goods_id").toString();
                    if (map1.containsKey("goods_count")) {
                        goods_count = Integer.parseInt(map1.get("goods_count").toString());
                    }
                }

                final String goods_id_str = goods_id;
                view1.setOnClickListener(v -> {

                    if (selected_list.contains(goods_id_str)) {
                        checkBox.setChecked(false);
                        selected_list.remove(goods_id_str);
                    } else {
                        checkBox.setChecked(true);
                        selected_list.add(goods_id_str);
                    }
                });
                viewHolder.ll_combine_item_one.addView(view1);

                //动态添加布局2
                View view2 = layoutInflater.inflate(R.layout.item_itemtwo, null);
                final Button button = (Button) view2.findViewById(R.id.bt_joincar_combine);

                if ((n + 2) == (list_goods.size() + 2)) {
                    if (n < list.size()) {
                        Map map2 = (Map) list.get(n);
                    }
                    viewHolder.ll_combine_item_one.addView(view2);
                }
                if (checkedbax_visible == 0) {

                    checkBox.setVisibility(View.GONE);
                    button.setOnClickListener(view3 -> {
                        // 套装加入购物车
                        if (goods_count > 0) {
                            go_car2(i);
                        } else {
                            Toast.makeText(mActivity, "库存不足！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (checkedbax_visible == 1) {

                    if (n == 0) {
                        checkBox.setVisibility(View.GONE);
                    } else {
                        checkBox.setVisibility(View.VISIBLE);
                    }
                    button.setOnClickListener(view32 -> {
                        if (goods_count > 0) {
                            addToCart(i);
                        } else {
                            Toast.makeText(mActivity, "库存不足！", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        }
        combineItemIAdapter = new CombineItemIAdapter(mActivity, list_goods, checkedbax_visible);
        viewHolder.lv_list_combine.setAdapter(combineItemIAdapter);


        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewHolder.lv_list_combine.setLayoutManager(linearLayoutManager);

        combineItemIAdapter = new CombineItemIAdapter(mActivity, list_goods, checkedbax_visible);
        viewHolder.lv_list_combine.setAdapter(combineItemIAdapter);

        return view;
    }

    public static class ViewHolder {
        private TextView tv_combinename;
        private TextView tv_combine_total;
        private RecyclerView lv_list_combine;
        private ImageView iv_combine_details;
        private RelativeLayout rl_height;
        private LinearLayout ll_combine_item_one;
        public TextView tv_total_price;

        //点击下拉显示内容
        //   private PullToRefreshListView lv_listview_combine;
    }

    //为RecyclerView设置adapter，填充数据
    public class CombineItemIAdapter extends RecyclerView.Adapter<CombineItemIAdapter.ViewHolder> {

        BaseActivity mActivity;
        List list_goods;
        int checkedbax_visible;
        private LayoutInflater mInflater;

        public CombineItemIAdapter(BaseActivity mActivity, List list_goods, int checkedbax_visible) {
            this.mActivity = mActivity;
            this.list_goods = list_goods;
            this.checkedbax_visible = checkedbax_visible;
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mInflater.from(parent.getContext()).inflate(R.layout.item_combine_items, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public int getItemCount() {
            return list_goods.size() + 1;
        }

        public void onBindViewHolder(CombineItemIAdapter.ViewHolder holder, int position) {

            if (position == 0) {
                Map map = (Map) list.get(position);
                holder.iv_combine_items.setImageURI(Uri.parse(map.get("goods_img").toString()));

            }
            if (position > 0 && position < list_goods.size() + 1) {
                Map map1 = (Map) list_goods.get(position - 1);
                holder.iv_combine_items.setImageURI(Uri.parse(map1.get("goods_img").toString()));
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public View convertView;

            private SimpleDraweeView iv_combine_items;

            public ViewHolder(View v) {
                super(v);
                convertView = v;
                iv_combine_items = (SimpleDraweeView) v.findViewById(R.id.iv_combine_items);


            }
        }
    }

    //加入购物车配件
    protected void addToCart(int j) {


        Map paramap = new HashMap();

        final SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        map = new HashMap();
        map = (Map) list.get(0);
        final String cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("cart_mobile_ids", cart_mobile_ids);
        paramap.put("", "");
        paramap.put("goods_id", map.get("goods_id").toString());
        paramap.put("count", 1);
        paramap.put("price", map.get("all_price"));
        paramap.put("gsp", "");
        paramap.put("buy_type", "parts");
        String combin_ids = "";
        for (int i = 0; i < selected_list.size(); i++) {
            if (i == 0) {
                combin_ids += selected_list.get(i);
            } else {
                combin_ids += "," + selected_list.get(i);
            }
        }
        paramap.put("combin_ids", combin_ids);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/add_goods_cart.htm",
                result -> {
                    try {
                        int code = result.getInt("code");

                        if (code == 0 || code == 1 || code == 2) {
                            if ("".equals(user_id)) {
                                String cart_mobile_id = result.getString("cart_mobile_id");
                                if (!cart_mobile_id.equals("")) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("cart_mobile_ids", cart_mobile_ids + cart_mobile_id + ",");
                                    editor.commit();
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                                    .setTitle("添加成功！")
                                    .setMessage("商品已成功加入购物车")
                                    .setPositiveButton(
                                            "去购物车",
                                            (dialog, which) -> {
                                                dialog.dismiss();// 关闭对话框
                                                mActivity.go_cart();
                                            })
                                    .setNegativeButton(
                                            "再逛逛",
                                            (dialog, which) -> {
                                                dialog.dismiss();// 关闭对话框
                                            });
                            builder.create().show();// 创建对话框并且显示该对话框
                        }
                        if (code == -100) {
                            Toast.makeText(mActivity,
                                    "添加失败，请稍后重试",
                                    Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mActivity, "购买数量必须小于商品的最高购买数量",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (code == -9) {
                            Toast.makeText(mActivity, "当前用户等级低于该商品购买等级",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);


    }


    //套装加入购物车
    private void go_car2(int position) {
        Map paramap = new HashMap();
        final SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        final String cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
        final int index = position + 1;

        Map map = new HashMap();
        map = (Map) list.get(position);

        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("cart_mobile_ids", cart_mobile_ids);
        paramap.put("goods_id", map.get("goods_id").toString());
        paramap.put("count", 1);
        paramap.put("price", "");
        paramap.put("gsp", "");
        paramap.put("combin_version", index);
        paramap.put("buy_type", "suit");
        String combin_ids = "";

        final List goods_list = (List) map.get("goods_list");


        for (int i = 0; i < goods_list.size(); i++) {
            Map map3 = (Map) goods_list.get(i);
            if (i == 0) {
                combin_ids += map3.get("goods_id");
            } else {
                combin_ids += "," + map3.get("goods_id");
            }
        }
        paramap.put("combin_ids", combin_ids);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/add_goods_cart.htm",
                result -> {
                    try {
                        int code = result.getInt("code");
                        if (code == 0 || code == 1 || code == 2) {
                            if ("".equals(user_id)) {
                                String cart_mobile_id = result.getString("cart_mobile_id");
                                if (!cart_mobile_id.equals("")) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("cart_mobile_ids", cart_mobile_ids + cart_mobile_id + ",");
                                    editor.commit();
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    mActivity)
                                    .setTitle("添加成功！")
                                    .setMessage("商品已成功加入购物车")
                                    .setPositiveButton("去购物车", (dialog, which) -> {
                                                dialog.dismiss();// 关闭对话框
                                                mActivity.go_cart();
                                            })
                                    .setNegativeButton("再逛逛",
                                            (dialog, which) -> {
                                                dialog.dismiss();// 关闭对话框
                                            });
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
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

}
