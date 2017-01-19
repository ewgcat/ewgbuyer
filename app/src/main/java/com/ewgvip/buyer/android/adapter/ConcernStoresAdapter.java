package com.ewgvip.buyer.android.adapter;

import android.app.Dialog;
import android.content.Context;
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
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 我的收藏店铺适配器
 */
public class ConcernStoresAdapter extends BaseAdapter {
    private List list;
    private LayoutInflater inflater;
    private BaseActivity mActivity;
    Dialog alertDialog;

    public ConcernStoresAdapter(BaseActivity mActivity, List list) {
        this.list = list;
        this.mActivity = mActivity;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {

        return list.size();
    }

    public Object getItem(int position) {

        return position;
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_concern_store, null);
            holder.teImageView = (SimpleDraweeView) convertView.findViewById(R.id.iv_item);
            holder.tView1 = (TextView) convertView.findViewById(R.id.tv_shopname);
            holder.rView2 = (TextView) convertView.findViewById(R.id.tv_shopinfo);
            holder.ll_storeItem = (LinearLayout) convertView.findViewById(R.id.ll_storeItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Map map = (Map) list.get(position);
        holder.tView1.setText(map.get("store_name").toString());
        holder.rView2.setText(map.get("favorite_count").toString() + "人已关注");
        BaseActivity.displayImage(map.get("store_photo").toString(), holder.teImageView);
        final String storeId = map.get("store_id") + "";
        final String contern_id = map.get("id") + "";
        holder.ll_storeItem.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_store(storeId + "");
            }
        });
        holder.ll_storeItem.setOnLongClickListener(view -> {

            String[] arr = {"取消关注"};
            alertDialog = MyDialog.showAlert(mActivity, "操作", arr, (adapterView, view1, i, l) -> {
                if (position == 0) {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Map paramap = mActivity.getParaMap();
                        paramap.put("mulitId", contern_id + ",");
                        paramap.put("type", 1);

                        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                        Request<JSONObject> request = new NormalPostRequest(
                                mActivity, CommonUtil.getAddress(mActivity) + "/app/buyer/favorite_del.htm",
                                result -> {
                                    try {
                                        if (result.getBoolean("verify")) {
                                            list.remove(map);
                                            ConcernStoresAdapter.this.notifyDataSetChanged();
                                            Toast.makeText(mActivity, "取消成功", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }, error -> {

                                }, paramap);
                        mRequestQueue.add(request);
                    }

                }
            });

            return false;
        });

        return convertView;
    }

    private static class ViewHolder {
        SimpleDraweeView teImageView;
        TextView tView1;
        TextView rView2;
        LinearLayout ll_storeItem;
    }

}

