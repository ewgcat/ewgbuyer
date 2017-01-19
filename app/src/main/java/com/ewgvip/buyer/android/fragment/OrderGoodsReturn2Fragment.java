package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    退货申请
 */
public class OrderGoodsReturn2Fragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private Map paramap;
    private TextView tv;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.paramap = null;
        this.tv = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_goods_return2, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar=(Toolbar)rootView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);
        toolbar.setTitle(mActivity.getResources().getString(R.string.goods_return_apply));
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        //设置菜单可用
        setHasOptionsMenu(true);
        final Bundle bundle = getArguments();

        tv = (TextView) rootView.findViewById(R.id.goods_name);
        tv.setText(bundle.getString("goods_name"));
        SimpleDraweeView img = (SimpleDraweeView) rootView.findViewById(R.id.iv_goods_icon);
        BaseActivity.displayImage(bundle.getString("goods_img"), img);

        paramap = mActivity.getParaMap();
        paramap.put("goods_id", bundle.getString("goods_id"));
        paramap.put("goods_gsp_ids", bundle.getString("goods_gsp_ids"));
        paramap.put("oid", bundle.getString("oid"));
        paramap.put("goods_img", bundle.getString("goods_img"));

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_return_ship.htm",
                result -> {
                    try {
                        tv = (TextView) rootView.findViewById(R.id.return_goods_content);
                        tv.setText(result.getString("return_content"));
                        tv = (TextView) rootView.findViewById(R.id.return_store_info);
                        tv.setText(result.getString("self_address"));

                        final String rid = result.getString("rid");
                        JSONArray arr = result.getJSONArray("express_list");
                        List<String> express_list = new ArrayList<String>();
                        final Map map = new HashMap();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            express_list.add(obj.getString("express_name"));
                            map.put(obj.getString("express_name"), obj.getString("express_id"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, express_list);
                        final Spinner spinner = (Spinner) rootView.findViewById(R.id.trans_company);
                        spinner.setAdapter(adapter);
                        spinner.setPrompt("请选择物流公司");

                        rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                EditText et = (EditText) rootView.findViewById(R.id.trans_sn);
                                String trans_sn = et.getText().toString();
                                String express = map.get(spinner.getSelectedItem().toString()).toString();
                                if (trans_sn != null && !trans_sn.equals("") && express != null && !express.equals("")) {
                                    paramap.put("rid", rid);
                                    paramap.put("express_id", express);
                                    paramap.put("express_code", trans_sn);
                                    goods_return_ship_save();
                                } else {
                                    Toast.makeText(mActivity, "请填写物流公司及单号", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

        return rootView;
    }

    void goods_return_ship_save() {

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_return_ship_save.htm",
                result -> {
                    try {
                        if (result.get("ret").equals("true")) {
                            Toast.makeText(mActivity, "保存退货物流成功", Toast.LENGTH_SHORT).show();
                            if (getTargetFragment()!=null){
                                getTargetFragment().onActivityResult(getTargetRequestCode(),1, null);
                            }
                            getFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(mActivity, "保存失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }
}
