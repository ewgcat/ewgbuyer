package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 零元购申请
 */
public class FreeApplyFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private String goods_id;
    private String addr_id;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.goods_id = null;
        this.addr_id = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_free_apply, container,
                false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("填写申请");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        initAddress();

        rootView.findViewById(R.id.manageRecieverInfo).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Bundle bundle1 =new Bundle();
                        bundle1.putInt("type",1);
                        mActivity.go_address_list(bundle1, mActivity.getCurrentfragment());
                    }
                });

        rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {

                        order_submit();
                    }
                });

        return rootView;
    }

    protected void order_submit() {
        mActivity.showProcessDialog();
        Map paramap = mActivity.getParaMap();
        paramap.put("id", goods_id);
        EditText et = (EditText) rootView.findViewById(R.id.apply_reason);
        paramap.put("apply_reason", et.getText().toString());
        paramap.put("addr_id", addr_id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/free_apply_save.htm",
                result -> {
                    try {
                        String code = result.get("code").toString();
                        if (code.equals("-100")) {
                            Toast.makeText(mActivity, "您有尚未评价的0元购或您申请过此0元购", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("100")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "free");
                            bundle.putString("order_id", result.getString("order_id"));
                            mActivity.go_success(bundle);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }



    private void initAddress() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/address_default.htm",
                result -> {
                    try {
                        if (!result.has("trueName")) {
                            rootView.findViewById(R.id.norecieverInfo).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.recieverInfo).setVisibility(View.GONE);
                        } else {
                            rootView.findViewById(R.id.norecieverInfo).setVisibility(View.GONE);
                            rootView.findViewById(R.id.recieverInfo).setVisibility(View.VISIBLE);
                            TextView tx = (TextView) rootView.findViewById(R.id.receiver);
                            tx.setText(result.getString("trueName"));
                            tx = (TextView) rootView.findViewById(R.id.mobilePhone);
                            tx.setText(result.getString("telephone"));
                            tx = (TextView) rootView.findViewById(R.id.address);
                            tx.setText(result.getString("area") + result.getString("areaInfo"));
                            addr_id = result.get("addr_id").toString();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), mActivity.getParaMap());
        mRequestQueue.add(request);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Cart2Fragment.ADDRESS){
            initAddress();
        }
    }
}
