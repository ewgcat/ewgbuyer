package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.MyCommissionListAdapter;
import com.ewgvip.buyer.android.models.VipCommissionItem;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPost2Request;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.Response;
import com.ewgvip.buyer.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 返佣收益.
 */
public class MyCommissionFragment extends Fragment {
    @BindView(R.id.listview)
    ListView listview;
    private int visibleLastIndex = 0;   //最后的可视项索引
    int begincount = 0;
    int selectcount = 10;
    private View rootView;
    private BaseActivity mActivity;
    private ArrayList<VipCommissionItem> list = new ArrayList<VipCommissionItem>();
    private MyCommissionListAdapter adapter;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.adapter = null;
        this.list = null;
        this.mActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_commission, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new MyCommissionListAdapter(mActivity, list);
        listview.setAdapter(adapter);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == itemsLastIndex) {
                    //如果是自动加载,可以在这里放置异步加载数据的代码
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        getFocus();
    }

    //主界面获取焦点
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent();
                    getFragmentManager().popBackStack();
                    if (getTargetFragment()!=null){
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 100, intent);
                    }
                    return true;
                }
                return false;
            }
        });
    }


    //加载数据
    private void loadData() {
        Map paraMap = mActivity.getParaMap();
        paraMap.put("begincount", begincount + "");
        paraMap.put("selectcount", selectcount + "");
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONArray> request = new NormalPost2Request(mActivity, getResources().getString(R.string.http_url) + "/app/buyer/userordercommission_list.htm", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    begincount += 10;
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            String addTime = jsonObject.get("addTime").toString();
                            String commission_amount = jsonObject.getString("commission_amount");
                            String photo = jsonObject.get("photo").toString();
                            String commission_status = jsonObject.getString("commission_status");
                            String userName = jsonObject.get("userName").toString();
                            String user_relation = jsonObject.get("user_relation").toString();
                            VipCommissionItem item = new VipCommissionItem(addTime, commission_amount, commission_status, photo, userName, user_relation);
                            list.add(item);
                        }
                        adapter.update(list);
                    } else {
                        Toast.makeText(mActivity, "亲，暂时没有返佣收益", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
            }
        }, paraMap);

        mRequestQueue.add(request);
    }


    //点击监听
    @OnClick(R.id.iv_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
        }
    }
}
