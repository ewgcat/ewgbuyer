package com.ewgvip.buyer.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.OrderEvaluateAddAdapter;
import com.ewgvip.buyer.android.models.SerializableMap;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangzhiyong  .
 * @Description
 * @date 2016/1/28
 */
public class OrderEvaluateCompleteFragment extends Fragment {
    View rootView;
    BaseActivity mActivity;
    PullToRefreshListView mPullRefreshListView;
    ListView actualListView;
    private int beginCount = 0;
    private int selectCount = 10;
    OrderEvaluateAddAdapter orderEvaluateAddAdapter;
    List dataList;

    //静态工厂方法
    public static android.support.v4.app.Fragment getInstance() {
        android.support.v4.app.Fragment fragment = new OrderEvaluateCompleteFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_evaluate_list, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        final Bundle bundle = getArguments();
        mPullRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.listview);

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        // 到底刷新
        mPullRefreshListView.setOnLastItemVisibleListener(() -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        if (beginCount <= dataList.size()) {
                            getData();
                        }
                    }
                });
        mPullRefreshListView.setOnRefreshListener(refreshView -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                beginCount = 0;
                dataList.clear();
                getData();
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
        dataList = new ArrayList();
        orderEvaluateAddAdapter = new OrderEvaluateAddAdapter(mActivity, dataList, OrderEvaluateCompleteFragment.this);
        actualListView.setAdapter(orderEvaluateAddAdapter);
        getData();
        actualListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Map map1 = (Map) dataList.get(i - 1);
            SerializableMap serializableMap = new SerializableMap(map1);
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("serializableMap", serializableMap);
            bundle1.putString("evaluate_id", map1.get("evaluate_id") + "");
            mActivity.go_order_evaluate_details(bundle1);
        });
        return rootView;
    }

    public void getData() {
        Map paramap = mActivity.getParaMap();
        paramap.put("beginCount", beginCount);
        paramap.put("selectCount", selectCount);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/evaluate_list.htm",
                result -> {
                    try {
                        JSONArray jsonArray = result.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Map map1 = new HashMap();
                            map1.put("evaluate_buyer_val", jsonObject.get("evaluate_buyer_val") + "");
                            map1.put("goods_main_photo", jsonObject.get("goods_main_photo") + "");
                            JSONArray jsonArray1 = jsonObject.getJSONArray("add_evaluate_photos");
                            List photoAddList = new ArrayList();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                photoAddList.add(jsonArray1.get(j));
                            }
                            map1.put("add_evaluate_photos", photoAddList);
                            map1.put("evaluate_info", jsonObject.get("evaluate_info") + "");
                            map1.put("goods_id", jsonObject.get("goods_id") + "");
                            JSONArray jsonArray2 = jsonObject.getJSONArray("evaluate_photos");
                            List photoList = new ArrayList();
                            for (int j = 0; j < jsonArray2.length(); j++) {
                                photoList.add(jsonArray2.get(j));
                            }
                            map1.put("evaluate_photos", photoList);
                            map1.put("evaluate_id", jsonObject.get("evaluate_id") + "");
                            if (jsonObject.has("reply")) {
                                map1.put("reply", jsonObject.get("reply") + "");
                            } else {
                                map1.put("reply", "");
                            }
                            if (jsonObject.has("addeva_info")) {
                                map1.put("addeva_info", jsonObject.get("addeva_info") + "");
                            } else {
                                map1.put("addeva_info", "");
                            }
                            List photoListAdd = new ArrayList();
                            if (jsonObject.has("add_evaluate_photos")) {
                                JSONArray jsonArray3 = jsonObject.getJSONArray("add_evaluate_photos");
                                for (int j = 0; j < jsonArray3.length(); j++) {
                                    photoListAdd.add(jsonArray3.get(j));
                                }
                            }
                            map1.put("add_evaluate_photos", photoListAdd);
                            map1.put("goods_name", jsonObject.get("goods_name") + "");
                            map1.put("evaluate_add_able", jsonObject.get("evaluate_add_able") + "");
                            map1.put("evaluate_able", jsonObject.get("evaluate_able") + "");
                            map1.put("addTime", jsonObject.get("addTime") + "");
                            dataList.add(map1);
                        }
                    } catch (Exception e) {
                    }

                    if (beginCount == 0 && dataList.size() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                            }
                        });
                        mPullRefreshListView.setVisibility(View.GONE);
                    } else {
                        mPullRefreshListView.setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                    }
                    beginCount += selectCount;
                    mActivity.hideProcessDialog(0);
                    orderEvaluateAddAdapter.notifyDataSetChanged();
                    mPullRefreshListView.onRefreshComplete();
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    rootView.findViewById(R.id.nodata).setVisibility(
                            View.VISIBLE);
                    rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            mActivity.showProcessDialog();
                            beginCount = 0;
                            dataList.clear();
                            getData();
                        }
                    });
                    mPullRefreshListView.setVisibility(View.GONE);
                }, paramap);
        mRequestQueue.add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle.getInt("type") == 1) {
            int position = bundle.getInt("position");
            Map map1 = (Map) dataList.get(position);
            map1.put("evaluate_able", 0);
            map1.put("evaluate_buyer_val", 1);
        } else {
            int position = bundle.getInt("position");
            Map map1 = (Map) dataList.get(position);
            List list = (List) map1.get("add_evaluate_photos");
            ArrayList<String> list1 = bundle.getStringArrayList("urlList");
            for (int i = 0; i < list1.size(); i++) {
                list.add(list1.get(i));
            }
            map1.put("add_evaluate_photos", list);
            map1.put("addeva_info", bundle.getString("evaluate_info"));
            map1.put("evaluate_add_able", 0);
        }
        orderEvaluateAddAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
