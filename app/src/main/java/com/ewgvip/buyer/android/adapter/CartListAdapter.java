package com.ewgvip.buyer.android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.dialog.ChooseGiftDialog;
import com.ewgvip.buyer.android.dialog.GoodsEditDialog;
import com.ewgvip.buyer.android.fragment.Cart1Fragment;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/10/27.
 */
public class CartListAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private Cart1Fragment fragment;
    private LayoutInflater inflater;
    private List<Map> cart_list;
    private Set<String> chosen_list;// 选中的id
    private Set<CheckBox> checkboxlist;// 勾选按钮
    private AlertDialog customDialog;
    private int edit_status = 0;
    private GoodsEditDialog goodsEditDialog;

    public CartListAdapter(BaseActivity context, Cart1Fragment fragment, List<Map> cart_list, Set<String> chosen_list, Set<CheckBox> checkboxlist) {
        this.mActivity = context;
        this.fragment = fragment;
        this.cart_list = cart_list;
        this.chosen_list = chosen_list;
        this.checkboxlist = checkboxlist;
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
            convertView = inflater.inflate(R.layout.item_cart1_discount, null);
            holder = new ViewHolder();
            holder.discount_type = (TextView) convertView.findViewById(R.id.discount_type);
            holder.discount_info = (TextView) convertView.findViewById(R.id.discount_info);
            holder.discount_header = (RelativeLayout) convertView.findViewById(R.id.discount_header);
            holder.cart_goods = (LinearLayout) convertView.findViewById(R.id.cart_goods);
            holder.discount_footer = (RelativeLayout) convertView.findViewById(R.id.discount_footer);
            holder.choose_gift = (Button) convertView.findViewById(R.id.choose_gift);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final List<Map> item_cart_list = (List<Map>) map.get("cart_list");
        Map gift_map = null;
        if (map.containsKey("type")) {
            holder.discount_header.setVisibility(View.VISIBLE);
            holder.discount_footer.setVisibility(View.VISIBLE);
            String type = map.get("type").toString();
            holder.choose_gift.setVisibility(View.GONE);
            if (type.equals("reduce")) {
                holder.discount_type.setText("满减");
            } else if (type.equals("gift")) {
                holder.discount_type.setText("满送");
                holder.choose_gift.setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                String str = "";
                                for (Map goods : item_cart_list) {
                                    if (chosen_list.contains(goods.get("cart_id").toString())) {
                                        str += goods.get("cart_id") + ",";
                                    }
                                }
                                ChooseGiftDialog chooseGiftDialog = new ChooseGiftDialog(mActivity, ((List) map.get("gift_list")), str, fragment);
                                chooseGiftDialog.show();
                            }
                        });
                if (map.get("whether_enough").toString().equals("1")) {
                    holder.choose_gift.setVisibility(View.VISIBLE);

                }
                if (map.get("whether_enough").toString().equals("2")) {
                    holder.choose_gift.setVisibility(View.VISIBLE);
                    holder.choose_gift.setText("重选赠品");
                    gift_map = (Map) map.get("gift");
                }
            } else if (type.equals("combine")) {
                holder.discount_type.setText("组合");
            }
            holder.discount_info.setText(map.get("info").toString());
            holder.cart_goods.removeAllViews();
            if (holder.cart_goods.getChildCount() == 0) {

                if (map.containsKey("suit_info")) {

                    for (Map goods : item_cart_list) {
                        RelativeLayout layout_head = (RelativeLayout) inflater
                                .inflate(R.layout.item_cart1_combine_head, null)
                                .findViewById(R.id.mylist_item);
//                        if ((goods.get("inventory_ids") + "").equals("1")) {
//                            holder.choose_gift.setVisibility(View.GONE);
//                        }
                        setcombine_head(layout_head, goods);
                        holder.cart_goods.addView(layout_head);
                        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_cart1_combine_goods, null).findViewById(R.id.mylist_item);
                        setcombine_suit(layout, goods);
                        holder.cart_goods.addView(layout);
                    }

                    List<Map> suit_list = (List<Map>) map.get("suit_info");
                    for (Map object : suit_list) {
                        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_cart1_combine_goods, null).findViewById(R.id.mylist_item);
                        setcombine_suit(layout, object);
                        holder.cart_goods.addView(layout);
                    }
                } else {
                    for (Map goods : item_cart_list) {
                        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_cart1_goods, null).findViewById(R.id.mylist_item);
                        setitem(layout, goods);
                        holder.cart_goods.addView(layout);
                    }
                    if (gift_map != null) {
                        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_cart1_gift_goods, null).findViewById(R.id.mylist_item);
                        setgift(layout, gift_map);
                        holder.cart_goods.addView(layout);
                    }
                }
            }
        } else {
            holder.discount_header.setVisibility(View.GONE);
            holder.discount_footer.setVisibility(View.GONE);
            holder.cart_goods.removeAllViews();
            if (holder.cart_goods.getChildCount() == 0) {
                RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_cart1_goods, null).findViewById(R.id.mylist_item);
                setitem(layout, map);
                holder.cart_goods.addView(layout);
            }
        }


        return convertView;
    }

    private void setgift(RelativeLayout layout, Map gift_map) {

        ViewHolder2 holder = new ViewHolder2();
        holder.goods_img = (SimpleDraweeView) layout.findViewById(R.id.goods_img);
        holder.goods_name = (TextView) layout.findViewById(R.id.goods_name);
        holder.buy_count = (TextView) layout.findViewById(R.id.textView3);
        holder.tv_goods_sold_out = (TextView) layout.findViewById(R.id.tv_goods_sold_out);
        holder.tv_no_goods_bg = (TextView) layout.findViewById(R.id.tv_no_goods_bg);
        holder.buy_count.setText("赠品");
        holder.buy_count.setTextColor(mActivity.getResources().getColor(R.color.red));
        BaseActivity.displayImage((String) gift_map.get("goods_main_photo"), holder.goods_img);
        holder.goods_name.setText(gift_map.get("goods_name").toString());
        if ((gift_map.get("inventory_ids") + "").equals("1")) {
            holder.tv_goods_sold_out.setVisibility(View.VISIBLE);
            holder.tv_no_goods_bg.setVisibility(View.VISIBLE);
        } else {
            holder.tv_no_goods_bg.setVisibility(View.GONE);
            holder.tv_goods_sold_out.setVisibility(View.GONE);
        }

    }

    private void setcombine_suit(RelativeLayout layout, final Map map) {
        ViewHolder2 holder = new ViewHolder2();
        holder.tv_goods_sold_out = (TextView) layout.findViewById(R.id.tv_goods_sold_out);
        holder.tv_no_goods_bg = (TextView) layout.findViewById(R.id.tv_no_goods_bg);
        holder.goods_img = (SimpleDraweeView) layout.findViewById(R.id.goods_img);
        holder.goods_name = (TextView) layout.findViewById(R.id.goods_name);
        holder.buy_count = (TextView) layout.findViewById(R.id.textView3);

        BaseActivity.displayImage((String) map.get("goods_main_photo"), holder.goods_img);
        holder.goods_name.setText(map.get("goods_name").toString());
        holder.tv_goods_sold_out = (TextView) layout.findViewById(R.id.tv_goods_sold_out);
        if ((map.get("inventory_ids") + "").equals("1")) {
            holder.tv_goods_sold_out.setVisibility(View.VISIBLE);
            holder.tv_no_goods_bg.setVisibility(View.VISIBLE);
        } else {
            holder.tv_no_goods_bg.setVisibility(View.GONE);
            holder.tv_goods_sold_out.setVisibility(View.GONE);
        }
    }

    private void setcombine_head(RelativeLayout layout, final Map map) {


        ViewHolder2 holder = new ViewHolder2();

        holder.buy_count = (EditText) layout.findViewById(R.id.buy_count);
        holder.goods_price = (TextView) layout.findViewById(R.id.goods_money);
        holder.minus = (ImageButton) layout.findViewById(R.id.minus);
        holder.plus = (ImageButton) layout.findViewById(R.id.plus);
        holder.checkBox = (CheckBox) layout.findViewById(R.id.checkBox);
        holder.check_area = layout.findViewById(R.id.check_area);
        layout.setTag(holder);
        if (edit_status == 1) {
            layout.findViewById(R.id.layout_edit).setVisibility(View.VISIBLE);
        } else {
            layout.findViewById(R.id.layout_edit).setVisibility(View.GONE);
        }
        final CheckBox now_checkbox = holder.checkBox;
        final String cartid = map.get("cart_id").toString();
        final EditText cart_count = (EditText) holder.buy_count;
        holder.goods_price.setText("套装单价￥" + mActivity.moneytodouble(map.get("goods_price").toString()));

        final int count = Integer.parseInt(map.get("goods_count").toString());

        holder.buy_count.setText(count + "");
        final ImageButton minus = holder.minus;

        final TextView cat_count = holder.buy_count;
        holder.buy_count.setTag("now_changing" + cartid);

        holder.check_area.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (chosen_list.contains(cartid)) {
                    chosen_list.remove(cartid);
                    now_checkbox.setChecked(false);
                } else {
                    chosen_list.add(cartid);
                    now_checkbox.setChecked(true);
                }

                fragment.sumMoney();
            }
        });
        checkboxlist.add(holder.checkBox);
        if (chosen_list.contains(cartid)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        if ((map.get("inventory_ids") + "").equals("0")) {
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
            });
            cart_count.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {


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

            holder.minus.setOnClickListener(v -> {

                if (FastDoubleClickTools.isFastDoubleClick()) {
                    int count1 = Integer
                            .parseInt(cat_count.getText().toString());
                    chosen_list.add(cartid);
                    now_checkbox.setChecked(true);
                    cart_count_adjust(cartid, count1 - 1);
                }
            });
            holder.plus.setOnClickListener(v -> {

                if (FastDoubleClickTools.isFastDoubleClick()) {
                    int count12 = Integer
                            .parseInt(cat_count.getText().toString());
                    chosen_list.add(cartid);
                    now_checkbox.setChecked(true);
                    cart_count_adjust(cartid, count12 + 1);
                }
            });
        } else {
            cart_count.setFocusable(false);
        }
        if (count == 1) {
            holder.minus.setClickable(false);
        }
    }

    private void setitem(RelativeLayout layout, final Map map) {


        final ViewHolder2 holder = new ViewHolder2();
        holder.tv_no_goods_bg = (TextView) layout.findViewById(R.id.tv_no_goods_bg);
        holder.goods_info = layout.findViewById(R.id.goods_info);
        holder.tv_goods_sold_out = (TextView) layout.findViewById(R.id.tv_goods_sold_out);
        holder.goods_edit = layout.findViewById(R.id.goods_edit);
        holder.goods_img = (SimpleDraweeView) layout.findViewById(R.id.goods_img);
        holder.goods_name = (TextView) layout.findViewById(R.id.goods_name);
        holder.goods_gsp = (TextView) layout.findViewById(R.id.goods_gsp);
        holder.goods_gsp_edit = (TextView) layout.findViewById(R.id.goods_gsp_edit);
        holder.goods_count = (TextView) layout.findViewById(R.id.goods_count);
        holder.buy_count = (TextView) layout.findViewById(R.id.buy_count);
        holder.goods_price = (TextView) layout
                .findViewById(R.id.goods_price);
        holder.minus = (ImageButton) layout.findViewById(R.id.minus);
        holder.plus = (ImageButton) layout.findViewById(R.id.plus);
        holder.checkBox = (CheckBox) layout.findViewById(R.id.checkBox);
        holder.check_area = layout.findViewById(R.id.check_area);
        holder.layout_edit = (RelativeLayout) layout.findViewById(R.id.layout_edit);
        layout.setTag(holder);

        final CheckBox now_checkbox = holder.checkBox;
        final String cartid = map.get("cart_id").toString();
        BaseActivity.displayImage((String) map.get("goods_main_photo"),
                holder.goods_img);
        Log.e("++", map.get("inventory_ids") + "");
        if ((map.get("inventory_ids") + "").equals("1")) {
            holder.tv_goods_sold_out.setVisibility(View.VISIBLE);
            holder.tv_no_goods_bg.setVisibility(View.VISIBLE);
        } else {
            holder.tv_goods_sold_out.setVisibility(View.GONE);
            holder.tv_no_goods_bg.setVisibility(View.GONE);
        }
        holder.goods_name.setText(map.get("goods_name").toString());
        holder.goods_gsp.setText(map.get("goods_spec").toString());
        holder.goods_gsp_edit.setText(map.get("goods_spec").toString());
        if (map.get("goods_spec").toString().length() > 0) {
            holder.layout_edit.setVisibility(View.VISIBLE);
        } else {
            holder.layout_edit.setVisibility(View.GONE);
        }
        goodsEditDialog = new GoodsEditDialog(mActivity, R.style.AlertDialogStyle, null, map.get("goods_id").toString(), map.get("goods_spec_ids").toString(), holder.buy_count.getText().toString(), "1", mActivity.getCache("area_id"), false, false, false, 0,"true");
        if ((map.get("inventory_ids") + "").equals("0")) {
            holder.layout_edit.setOnClickListener(view -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Map parmap = new HashMap();
                    parmap.put("id", map.get("goods_id"));
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request2 = new NormalPostRequest(mActivity,
                            mActivity.getAddress() + "/app/goods_specs.htm",
                            specsobj -> {
                                try {
                                    JSONArray specsList = specsobj.getJSONArray("spec_list");// 获取JSONArray
                                    if (!goodsEditDialog.isShowing() || goodsEditDialog == null) {
                                        goodsEditDialog = new GoodsEditDialog(mActivity, R.style.AlertDialogStyle, specsList, map.get("goods_id").toString(), map.get("goods_spec_ids").toString(), holder.buy_count.getText().toString(), "1", mActivity.getCache("area_id"), false, false, false, 0,"true");
                                        goodsEditDialog.show();
                                    }
                                    goodsEditDialog.setOnCancelListener(dialogInterface -> {
                                        //如果gsp和count与原来相同，不作操作否则请求修改规格
                                        if (map.get("goods_spec_ids").toString().equals(goodsEditDialog.getGsp())) {

                                        } else {
                                            //1点击确认返回刷新，0点击外部或返回键不刷新
                                            if (goodsEditDialog.getRefresh().equals("1")) {
                                                mActivity.showProcessDialog();
                                                SharedPreferences preferences = mActivity.getSharedPreferences(
                                                        "user", Context.MODE_PRIVATE);
                                                Map map1 = new HashMap();
                                                map1.put("gsp", goodsEditDialog.getGsp());
                                                map1.put("id", map.get("cart_id").toString());
                                                map1.put("user_id", preferences.getString("user_id", ""));
                                                map1.put("token", preferences.getString("token", ""));
                                                map1.put("cart_mobile_ids", preferences.getString("cart_mobile_ids", ""));
                                                RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                                Request<JSONObject> request3 = new NormalPostRequest(mActivity,
                                                        mActivity.getAddress() + "/app/goods_cart1_spec_save.htm",
                                                        specsobj1 -> {
                                                            try {
                                                                mActivity.hideProcessDialog(0);
                                                                fragment.refresh();
                                                            } catch (Exception e) {
                                                            }

                                                        }, error -> {

                                                        }, map1);
                                                mRequestQueue1.add(request3);
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                }

                            }, error -> {

                            }, parmap);
                    mRequestQueue.add(request2);
                }
            });
        }
        holder.goods_price.setText("￥" + mActivity.moneytodouble(map.get("goods_price").toString()));
        final int count = Integer.parseInt(map.get("goods_count").toString());
        final TextView cart_count = holder.buy_count;
        holder.goods_count.setText("×" + count);
        cart_count.setText(count + "");
        final ImageButton minus = holder.minus;
        if ((map.get("inventory_ids") + "").equals("0")) {
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
        } else {
            cart_count.setFocusable(false);
        }
        holder.checkBox.setOnClickListener(v -> {

            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (((CheckBox) v).isChecked()) {
                    chosen_list.add(cartid);
                } else {
                    chosen_list.remove(cartid);
                }
                fragment.sumMoney();
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

                fragment.sumMoney();
            }
        });

        checkboxlist.add(holder.checkBox);
        if (chosen_list.contains(cartid)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        if ((map.get("inventory_ids") + "").equals("0")) {
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
        }
        if (count == 1) {
            holder.minus.setClickable(false);
            holder.minus.setImageResource(R.mipmap.minusgray);
        }
        if (edit_status == 0) {
            holder.goods_info.setVisibility(View.VISIBLE);
            holder.goods_edit.setVisibility(View.GONE);
            holder.goods_img.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_goods(map.get("goods_id").toString());
                }
            });
            holder.goods_info.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_goods(map.get("goods_id").toString());
                }
            });
        } else {
            holder.goods_edit.setVisibility(View.VISIBLE);
            holder.goods_info.setVisibility(View.GONE);
        }
    }

    void cart_count_adjust(String cart_id, int count) {
        if (count < 0) {
            Toast.makeText(mActivity, "只能购买这么多", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences preferences = mActivity
                    .getSharedPreferences("user", Context.MODE_PRIVATE);
            String user_id = preferences.getString("user_id", "");
            String token = preferences.getString("token", "");
            String cart_mobile_ids = preferences.getString(
                    "cart_mobile_ids", "");
            Map paramap = new HashMap();
            paramap.put("user_id", user_id);
            paramap.put("token", token);
            paramap.put("cart_mobile_ids", cart_mobile_ids);
            paramap.put("cart_id", cart_id);
            paramap.put("count", count);

            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(
                    mActivity, mActivity.getAddress() + "/app/cart_count_adjust.htm",
                    result -> {
                        try {
                            int code = Integer.parseInt(result.get("code").toString());
                            if (code == 100) {
                                fragment.sumMoney();
                            }
                            if (code == 200) {
                                Toast.makeText(mActivity, "库存不足", Toast.LENGTH_SHORT).show();
                            }
                            if (code == 300) {
                                Toast.makeText(mActivity, "库存不足", Toast.LENGTH_SHORT).show();
                            }
                            if (code == 400) {
                                Toast.makeText(mActivity, "已超出限购数量!", Toast.LENGTH_SHORT).show();
                            }
                            if (code == 500) {
                                Toast.makeText(mActivity, "F码商品一次只能购买一件!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> mActivity.hideProcessDialog(1), paramap);
            mRequestQueue.add(request);
        }
    }

    public class ViewHolder {

        public TextView discount_type;
        public TextView discount_info;
        public LinearLayout cart_goods;
        public RelativeLayout discount_header;
        public RelativeLayout discount_footer;
        public Button choose_gift;
    }

    public class ViewHolder2 {
        public SimpleDraweeView goods_img;
        public TextView goods_name;
        public TextView goods_price;
        public TextView goods_gsp;
        public TextView goods_gsp_edit;
        public TextView goods_count;
        public TextView buy_count;
        public TextView tv_goods_sold_out;
        public TextView tv_no_goods_bg;
        public ImageButton minus;
        public ImageButton plus;
        public CheckBox checkBox;
        public View check_area;
        public RelativeLayout layout_edit;
        public View goods_info;
        public View goods_edit;

    }
}