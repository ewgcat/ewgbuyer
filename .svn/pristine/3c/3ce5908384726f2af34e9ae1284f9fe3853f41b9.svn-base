package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/*
    生活购订单详情
 */
public class OrderGrouplifeDetailFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private Map paramap;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.paramap = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_life_detail, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("订单详情");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用
        Bundle bundle = getArguments();
        final String order_id = bundle.getString("id");
        paramap = mActivity.getParaMap();
        paramap.put("oid", order_id);
        paramap.put("order_id", order_id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/grouplife_order_detail.htm",
                result -> {
                    try {
                        TextView tv = (TextView) rootView.findViewById(R.id.orderNumber);
                        tv.setText(result.has("order_id") ? result.get("order_id") + "" : "");

                        tv = (TextView) rootView.findViewById(R.id.orderStatus);
                        int status = Integer.valueOf(result.get("order_status") + "");
                        String str = "";
                        if (status == 0)
                            str = "已取消";
                        if (status == 10)
                            str = "待付款";
                        if (status == 20)
                            str = "已付款";
                        if (status == 30)
                            str = "已付款";
                        if (status == 50)
                            str = "交易完毕";
                        if (status == 65)
                            str = "不可评价";
                        tv.setText(str);

                        tv = (TextView) rootView.findViewById(R.id.addtime);
                        tv.setText(result.has("order_addTime") ? result.get("order_addTime") + "" : "");
                        tv = (TextView) rootView.findViewById(R.id.paytype);
                        tv.setText(result.has("order_pay_msg")?result.get("order_pay_msg")+"":"");
                        tv = (TextView) rootView.findViewById(R.id.name);
                        tv.setText(result.has("goods_name") ? result.get("goods_name") + "" : "");
                        tv = (TextView) rootView.findViewById(R.id.price);
                        tv.setText(result.has("goods_total_price") ? "总价：￥" + mActivity.moneytodouble(result.get("goods_total_price") + "") : "总价：￥" + "");
                        tv = (TextView) rootView.findViewById(R.id.count);
                        tv.setText(result.has("goods_count") ? "X" + result.get("goods_count") + "" : "X" + "");
                        SimpleDraweeView img = (SimpleDraweeView) rootView.findViewById(R.id.img);
                        BaseActivity.displayImage(result.has("goods_img") ? result.get("goods_img") + "" : "", img);

                        LinearLayout sn = (LinearLayout) rootView.findViewById(R.id.sn);

                        JSONArray gi = result.getJSONArray("groupinfos");

                        for (int i = 0; i < gi.length(); i++) {
                            JSONObject groupinfos = gi.getJSONObject(i);
                            if (groupinfos.length() != 0) {
                                sn.setVisibility(View.VISIBLE);
                            }
                            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.order_life_sn_item, null);
                            tv = (TextView) layout.findViewById(R.id.sn);
                            tv.setText(groupinfos.has("info_sn") ? groupinfos.get("info_sn") + "" : "");

                            tv = (TextView) layout.findViewById(R.id.group_statue);
                            tv.setText(groupinfos.has("info_status_msg") ? groupinfos.get("info_status_msg") + "" : "");
                            tv = (TextView) layout.findViewById(R.id.group_endtime);
                            tv.setText(groupinfos.has("info_end_time") ? groupinfos.get("info_end_time") + "" : "");
                            sn.addView(layout);
                        }
                        if ((result.has("order_status_msg") ? result.get("order_status_msg") + "" : "").equals("未支付")) {
                            Button button = (Button) rootView.findViewById(R.id.button);
                            View view_line = rootView.findViewById(R.id.divide_lines);
                            view_line.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            final Bundle bundle1 = new Bundle();
                            bundle1.putString("totalPrice", result.has("goods_total_price") ? result.get("goods_total_price") + "" : "");
                            bundle1.putString("order_id", order_id);
                            bundle1.putString("order_num", result.has("order_id") ? result.get("order_id") + "" : "");
                            button.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    bundle1.putString("type", "life");
                                    mActivity.go_pay(bundle1, "payonline");
                                }

                            });
                            rootView.findViewById(R.id.order_cancle).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.order_cancle).setOnClickListener(v -> {
                                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                                    // method stub
                                                    new AlertDialog.Builder(mActivity).setTitle("您要取消该订单？")
                                                            .setPositiveButton("确定", (dialog, which) -> grouplife_order_cancel())
                                                            .setNegativeButton("取消", null)
                                                            .show();
                                                }
                                            });
                        } else {
                            rootView.findViewById(R.id.button).setVisibility(View.GONE);
                            rootView.findViewById(R.id.order_cancle).setVisibility(View.GONE);
                            rootView.findViewById(R.id.divide_lines).setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        Log.e("test", e.toString());
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

        return rootView;
    }

    private void grouplife_order_cancel() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/grouplife_order_cancel.htm",
                result -> {
                    try {
                        if (result.getString("ret").equals("true")) {
                            getFragmentManager().popBackStack();
                            Toast.makeText(mActivity, "取消成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, "取消失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("test", e.toString());
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }
}