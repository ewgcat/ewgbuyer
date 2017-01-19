package com.ewgvip.buyer.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.Response;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏商品列表适配器
 */
public class ConcernGoodsAdapter extends BaseAdapter {
    private List list;
    private LayoutInflater inflater;
    private BaseActivity mActivity;
    Dialog alertDialog;

    public ConcernGoodsAdapter(BaseActivity mActivity, List list) {
        this.list = list;
        this.mActivity = mActivity;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int i) {
        return i;
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolderGoods holder = null;
        if (view == null) {
            holder = new ViewHolderGoods();
            view = inflater.inflate(R.layout.item_concern_goods, null);
            holder.teImageView2 = (SimpleDraweeView) view.findViewById(R.id.iv_item2);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_shopname2);
            holder.tv_info = (TextView) view.findViewById(R.id.tv_shopinfo2);
            holder.ll_goodsItem = (LinearLayout) view.findViewById(R.id.ll_goodsItem);
            view.setTag(holder);
        } else {
            holder = (ViewHolderGoods) view.getTag();
        }
        final Map map = (Map) list.get(i);
        holder.tv_name.setText(map.get("name").toString());
        holder.tv_info.setText("￥" +mActivity.moneytodouble( map.get("good_price").toString()));
        BaseActivity.displayImage(map.get("big_photo").toString(),
                holder.teImageView2);
        final String goodId = map.get("goods_id") + "";
        final String contern_id = map.get("id") + "";
        //点击商品各项跳转
        holder.ll_goodsItem.setOnClickListener(view1 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_goods(goodId + "");
            }

        });
        //对话框中为取消关注按钮设置点击事件
        holder.ll_goodsItem.setOnLongClickListener(view12 -> {

            String[] arr = {"取消关注", "添加购物车"};
            alertDialog = MyDialog.showAlert(mActivity, "操作", arr, (parent, view121, position, id) -> {
                if (position == 0) {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Map paramap = mActivity.getParaMap();
                        paramap.put("mulitId", contern_id + ",");
                        paramap.put("type", 0);
                        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                        Request<JSONObject> request = new NormalPostRequest(
                                mActivity, CommonUtil.getAddress(mActivity) + "/app/buyer/favorite_del.htm",
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject result) {
                                        try {
                                            alertDialog.dismiss();
                                            if (result.getBoolean("verify")) {
                                                list.remove(map);
                                                ConcernGoodsAdapter.this.notifyDataSetChanged();
                                                Toast.makeText(mActivity, "取消成功", Toast.LENGTH_SHORT).show();

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, error -> {
                                }, paramap);
                        mRequestQueue.add(request);

                    }
                } else if (position == 1) {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Map paramap = new HashMap();
                        final SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                        final String user_id = preferences.getString("user_id", "");
                        String token = preferences.getString("token", "");
                        final String cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
                        paramap.put("user_id", user_id);
                        paramap.put("token", token);
                        paramap.put("cart_mobile_ids", cart_mobile_ids);
                        paramap.put("goods_id", goodId);
                        paramap.put("count", 1);

                        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/add_goods_cart.htm",
                                result -> {
                                    try {
                                        alertDialog.dismiss();
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

                                            Toast.makeText(mActivity, "恭喜,已添加至购物车", Toast.LENGTH_SHORT).show();
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
                }
            });

            return false;
        });
        return view;
    }

    public static class ViewHolderGoods {
        SimpleDraweeView teImageView2;
        TextView tv_name;
        TextView tv_info;
        LinearLayout ll_goodsItem;
    }


}




