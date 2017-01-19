package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.Map;

/*
    零元购订单详情
 */
public class OrderFreeDetailFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_free_detail, container, false);
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
        setHasOptionsMenu(false);//设置菜单可用



        Bundle bundle = getArguments();
        final Map paramap = mActivity.getParaMap();
        paramap.put("oid", bundle.get("oid"));

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/free_order_detail.htm",
                result -> {
                    try {
                        JSONObject data = result.getJSONObject("data");
                        SimpleDraweeView img = (SimpleDraweeView) rootView.findViewById(R.id.img);
                        BaseActivity.displayImage(data.getString("goods_img"), img);
                        TextView tv = (TextView) rootView.findViewById(R.id.name);
                        tv.setText(data.getString("goods_name"));
                        tv = (TextView) rootView.findViewById(R.id.mobilePhone);
                        tv.setText(data.getString("receiver_tel"));
                        tv = (TextView) rootView.findViewById(R.id.receiver);
                        tv.setText(data.getString("receiver_name"));
                        tv = (TextView) rootView.findViewById(R.id.address);
                        tv.setText(data.getString("receiver_address"));
                        tv = (TextView) rootView.findViewById(R.id.orderStatus);

                        int apply_status = data.getInt("apply_status");
                        // 0为待审核 5为申请通过 等待收货 -5申请失败

                        if (apply_status == 0) {
                            tv.setText("待审核");
                        } else if (apply_status == 5) {
                            tv.setText("申请通过");
                            rootView.findViewById(R.id.express).setVisibility(View.VISIBLE);
                            tv = (TextView) rootView.findViewById(R.id.express_company_name);
                            tv.setText("物流公司：" + data.getString("express_company_name"));
                            tv = (TextView) rootView.findViewById(R.id.shipCode);
                            tv.setText("物流单号：" + data.getString("shipCode"));
                            if (data.getInt("evaluate_status") == 0) {
                                rootView.findViewById(R.id.evaluate_line).setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
                                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                                EditText et = (EditText) rootView.findViewById(R.id.evaluate_content);
                                                String use_experience = et.getText().toString();
                                                if (use_experience != null && !use_experience.equals("")) {
                                                    paramap.put("use_experience", use_experience);
                                                    free_order_evaluate_save(paramap);
                                                } else {
                                                    Toast.makeText(mActivity, "请填写使用体验！", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                rootView.findViewById(R.id.evaluate_line2).setVisibility(View.VISIBLE);
                                tv = (TextView) rootView.findViewById(R.id.evaluate);
                                tv.setText(data.getString("use_experience"));
                                tv = (TextView) rootView.findViewById(R.id.orderStatus);
                                tv.setText("已评价");
                            }

                        } else if (apply_status == -5) {
                            tv.setText("申请失败");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

        return rootView;
    }

    void free_order_evaluate_save(Map paramap) {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/free_order_evaluate_save.htm",
                result -> {
                    try {
                        if (result.get("ret").equals("true")) {
                            getFragmentManager().popBackStack();
                            Toast.makeText(mActivity, "保存使用评价成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, "保存使用评价失败！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }
}
