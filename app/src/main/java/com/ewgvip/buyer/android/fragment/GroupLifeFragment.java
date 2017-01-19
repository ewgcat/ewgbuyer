package com.ewgvip.buyer.android.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MapActivity;
import com.ewgvip.buyer.android.adapter.GroupEvaluateAdapter;
import com.ewgvip.buyer.android.layout.MyGroupScrollView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.Map;

@SuppressLint("NewApi")
public class GroupLifeFragment extends Fragment implements MyGroupScrollView.OnScrollListener {
    BaseActivity mActivity;
    View rootView;
    Bundle cart_bundle;
    SimpleDraweeView iv_group_life;
    RelativeLayout layout_top;
    RelativeLayout layout_top_hide;
    MyGroupScrollView scroll_view_group_life;
    Toolbar toolbar;
    JSONObject result1;
    boolean flag = false;
    GroupEvaluateAdapter groupEvaluateAdapter;
    String tel = "";
    Dialog dialog;

    String id = "";
    String geoLat = "";
    String geoLng = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_group_life, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("生活惠");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });

        setHasOptionsMenu(false);//设置菜单可用
        final Bundle bundle = getArguments();
        id = bundle.getString("id");
        geoLat = bundle.getString("geoLat");
        geoLng = bundle.getString("geoLng");
        layout_top = (RelativeLayout) rootView.findViewById(R.id.layout_top);
        layout_top_hide = (RelativeLayout) rootView.findViewById(R.id.layout_top_hide);
        scroll_view_group_life = (MyGroupScrollView) rootView.findViewById(R.id.scroll_view_group_life);

        iv_group_life = (SimpleDraweeView) rootView
                .findViewById(R.id.iv_group_life);
        ViewGroup.LayoutParams params = iv_group_life.getLayoutParams();
        params.width = mActivity.getScreenWidth();
        params.height = params.width * 186 / 278;
        iv_group_life.setLayoutParams(params);
        init();
        rootView.findViewById(R.id.map).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_map();
            }
        });


        scroll_view_group_life.setOnScrollListener(this);
        return rootView;
    }

    //测量listview高度
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            if (listAdapter.getCount() < 4) {
                if (i == 0) {
                    params.height = listItem.getMeasuredHeight();
                } else {
                    params.height += listItem.getMeasuredHeight() + listView.getDividerHeight();
                }
            } else {
                if (i < 3) {
                    if (i == 0) {
                        params.height = listItem.getMeasuredHeight();
                    } else {
                        params.height += listItem.getMeasuredHeight() + listView.getDividerHeight();
                    }
                }
            }

        }
        listView.setLayoutParams(params);
    }

    private void init() {
        Map paraMap = mActivity.getParaMap();
        paraMap.put("id", id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/group_life.htm",
                result -> {
                    try {
                        result1 = result;
                        BaseActivity.displayImage(result.has("gg_img") ? result.get("gg_img") + "" : "", iv_group_life);
                        TextView tv_group_life_name = (TextView) rootView.findViewById(R.id.tv_group_life_name);
                        tv_group_life_name.setText(result.has("gg_name") ? result.get("gg_name") + "" : "");
                        TextView tv_group_life_current_price = (TextView) rootView.findViewById(R.id.tv_group_life_current_price);
                        tv_group_life_current_price.setText(result.has("gg_price") ?mActivity.moneytodouble( result.get("gg_price") + "") : "");
                        TextView tv_group_life_old_price = (TextView) rootView.findViewById(R.id.tv_group_life_old_price);
                        tv_group_life_old_price.setText("门市价： ￥" + (result.has("gg_store_price") ?mActivity.moneytodouble( result.get("gg_store_price").toString()+ "") : ""));
                        tv_group_life_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
                        TextView tv_group_life_sale = (TextView) rootView.findViewById(R.id.tv_group_life_sale);
                        tv_group_life_sale.setText("已售 " + (result.has("gg_selled_count") ? result.get("gg_selled_count") + "" : ""));
                        TextView tv_group_life_store_name = (TextView) rootView.findViewById(R.id.tv_group_life_store_name);
                        tv_group_life_store_name.setText(result.has("store_name") ? result.get("store_name") + "" : "");
                        TextView tv_group_life_store_address = (TextView) rootView.findViewById(R.id.tv_group_life_store_address);
                        tv_group_life_store_address.setText(result.has("gai_name") ? result.get("gai_name") + "" : "");
                        TextView tv_group_life_scope = (TextView) rootView.findViewById(R.id.tv_group_life_scope);
                        tv_group_life_scope.setText(result.has("gg_scope") ? result.get("gg_scope") + "" : "");
                        TextView tv_group_life_time_limit = (TextView) rootView.findViewById(R.id.tv_group_life_time_limit);
                        tv_group_life_time_limit.setText((result.has("beginTime") ? result.get("beginTime") + "" : "") + " 至 " + (result.has("endTime") ? result.get("endTime") + "" : ""));
                        TextView tv_group_life_rule = (TextView) rootView.findViewById(R.id.tv_group_life_rule);
                        tv_group_life_rule.setText(result.has("gg_rules") ? result.get("gg_rules") + "" : "");
                        TextView tv_group_life_time = (TextView) rootView.findViewById(R.id.tv_group_life_time);
                        String startTime = "";
                        if ((result.has("gg_timerengestart") ? result.get("gg_timerengestart") + "" : "").equals("")) {
                            startTime = "00:00";
                        } else {
                            startTime = result.get("gg_timerengestart") + "";
                        }
                        String endTime = "";
                        if ((result.has("gg_timerengeend") ? result.get("gg_timerengeend") + "" : "").equals("")) {
                            endTime = "24:00";
                        } else {
                            endTime = result.get("gg_timerengeend") + "";
                        }
                        tv_group_life_time.setText(startTime + "-" + endTime);
                        tel = result.has("phone") ? result.get("phone") + "" : "";
                        TextView tv_group_life_distant = (TextView) rootView.findViewById(R.id.tv_group_life_distant);
                        Double Lng = Double.valueOf(result.has("gai_lng") ? result.get("gai_lng") + "" : "");
                        Double Lat = Double.valueOf(result.has("gai_lat") ? result.get("gai_lat") + "" : "");
                        if(geoLng.equals("")||geoLat.equals("")) {
                            tv_group_life_distant.setText("暂无距离信息！");
                        }else {
                            Double length = Math.sqrt(Math.pow(Math.abs(Lng - Double.valueOf(geoLng)), 2.0) + Math.pow(Math.abs(Lat - Double.valueOf(geoLat)), 2.0));
                            if (length >= 1) {
                                tv_group_life_distant.setText((int) (length * 1) + "km");
                            } else {
                                tv_group_life_distant.setText((int) (length * 1000) + "m");
                            }
                        }
                        cart_bundle = new Bundle();
                        cart_bundle.putString("id", id);
                        cart_bundle.putString("gg_name", result.has("gg_name") ? result.get("gg_name") + "" : "");
                        cart_bundle.putString("gg_img", result.has("gg_img") ? result.get("gg_img") + "" : "");
                        cart_bundle.putString("gg_price", result.has("gg_price") ? result.get("gg_price") + "" : "");
                        flag = true;
                        RelativeLayout layout_group_life_tel = (RelativeLayout) rootView.findViewById(R.id.layout_group_life_tel);
                        rootView.findViewById(R.id.btn_group_life).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                mActivity.go_group_life_cart1(cart_bundle);
                            }
                        });
                        layout_group_life_tel.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                dialog = com.ewgvip.buyer.android.dialog.MyDialog.showAlert(mActivity, "", "联系电话：" + tel, "确定", view1 -> {
                                    if (FastDoubleClickTools.isFastDoubleClick()) {
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        Uri data = Uri.parse("tel:" + tel);
                                        intent.setData(data);
                                        startActivity(intent);
                                    }
                                }, "取消", dialogInterface -> {
                                    if (FastDoubleClickTools.isFastDoubleClick()) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        LinearLayout layout_group_life_location = (LinearLayout) rootView.findViewById(R.id.map);

                        layout_group_life_location.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                try {
                                    Intent intent = new Intent(mActivity, MapActivity.class);
                                    intent.putExtra("gai_lat", Double.valueOf(geoLat));
                                    intent.putExtra("gai_lng", Double.valueOf(geoLng));
                                    mActivity.startActivity(intent);
                                } catch (Exception e) {
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    Toast.makeText(mActivity, "数据加载失败！", Toast.LENGTH_SHORT).show();
                }, paraMap);
        mRequestQueue.add(request);
    }

    @Override
    public void onScroll(int scrollY) {
        if (flag) {
            if (scrollY > iv_group_life.getLayoutParams().height) {
                layout_top_hide.setVisibility(View.VISIBLE);
                try {
                    TextView tv_group_life_current_price = (TextView) rootView.findViewById(R.id.tv_group_life_current_price_hide);
                    tv_group_life_current_price.setText(mActivity.moneytodouble(result1.get("gg_price") + ""));
                    TextView tv_group_life_old_price = (TextView) rootView.findViewById(R.id.tv_group_life_old_price_hide);
                    tv_group_life_old_price.setText("门市价： ￥" +mActivity.moneytodouble(result1.get("gg_store_price").toString()));
                    tv_group_life_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
                    rootView.findViewById(R.id.btn_group_life_hide).setOnClickListener(v -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            mActivity.go_group_life_cart1(cart_bundle);
                        }
                    });
                } catch (Exception e) {
                }
            } else {
                layout_top_hide.setVisibility(View.GONE);
            }
        }
    }
}
