package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.EvaluationListAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.DensityUtil;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品详情页的评价
 */
public class GoodsEvaluateFragment extends Fragment {
    private static final String GOODS_ID = "goods_id";

    private View rootView;
    private ListView listView;
    private String goodsId;
    private RadioGroup radioGroup;
    //评价分类radiobutton
    private RadioButton rbtn_evaluate_all;
    private RadioButton rbtn_evaluate_well;
    private RadioButton rbtn_evaluate_middle;
    private RadioButton rbtn_evaluate_bad;
    private PullToRefreshListView pullToRefreshListView;
    private String evaluateType = "";//评价类别空全部，1好评，0中评，-1差评
    private int beginCount = 0;//评价查询位置
    private int selectCount = 20;//评价查询数量
    private BaseActivity mActivity;
    private EvaluationListAdapter mEvaluationListAdapter;
    private List<Map> eva_list;//评价数据信息集合

    @Override
    public void onDetach() {
        super.onDetach();
        try {

            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");

            childFragmentManager.setAccessible(true);

            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);

        }
    }


    public static GoodsEvaluateFragment getInstance(String id) {
        GoodsEvaluateFragment fragment = new GoodsEvaluateFragment();
        Bundle args = new Bundle();
        args.putString(GOODS_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goodsId = getArguments().getString(GOODS_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods_evaluate_stub, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        getIndexEvaluate(goodsId);

        return rootView;
    }


    public void findview() {
        ViewStub viewstub = (ViewStub) rootView.findViewById(R.id.viewstub);
        viewstub.inflate();

        radioGroup = (RadioGroup) rootView.findViewById(R.id.rgroup_evaluate);
        pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.lv_evaluate);
        rbtn_evaluate_all = (RadioButton) rootView.findViewById(R.id.rbtn_evaluate_all);
        rbtn_evaluate_well = (RadioButton) rootView.findViewById(R.id.rbtn_evaluate_well);
        rbtn_evaluate_middle = (RadioButton) rootView.findViewById(R.id.rbtn_evaluate_middle);
        rbtn_evaluate_bad = (RadioButton) rootView.findViewById(R.id.rbtn_evaluate_bad);

        //初始加载数据
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);//设置刷新模式当出现最后一个item时自动加载
        pullToRefreshListView.setOnLastItemVisibleListener(() -> getIndexEvaluate(goodsId));
        listView = pullToRefreshListView.getRefreshableView();
        listView.setDividerHeight(DensityUtil.px2dip(mActivity, 0));//listview自定义的分割线取消
        eva_list = new ArrayList<>();
        mEvaluationListAdapter = new EvaluationListAdapter(mActivity, eva_list);
        listView.setAdapter(mEvaluationListAdapter);
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch (i) {
                case R.id.rbtn_evaluate_all://全部评价
                    mActivity.showProcessDialog();
                    eva_list.clear();
                    evaluateType = "";
                    beginCount = 0;
                    getIndexEvaluate(goodsId);
                    listView.setSelection(beginCount);
                    break;
                case R.id.rbtn_evaluate_well://好评
                    mActivity.showProcessDialog();
                    eva_list.clear();
                    evaluateType = "1";
                    beginCount = 0;
                    getIndexEvaluate(goodsId);
                    listView.setSelection(beginCount);
                    break;
                case R.id.rbtn_evaluate_middle://中评
                    mActivity.showProcessDialog();
                    eva_list.clear();
                    evaluateType = "0";
                    beginCount = 0;
                    getIndexEvaluate(goodsId);
                    listView.setSelection(beginCount);
                    break;
                case R.id.rbtn_evaluate_bad://差评
                    mActivity.showProcessDialog();
                    eva_list.clear();
                    evaluateType = "-1";
                    beginCount = 0;
                    getIndexEvaluate(goodsId);
                    listView.setSelection(beginCount);
                    break;
            }
        });
    }

    //请求网络获取评价数据并解析添加到eva_list中
    public void getIndexEvaluate(String id) {
        if (radioGroup == null)
            findview();
        Map map = new HashMap();
        map.put("id", id);//商品id
        map.put("type", evaluateType);//评价种类
        map.put("beginCount", String.valueOf(beginCount));//开始查询位置
        map.put("selectCount", String.valueOf(selectCount));//查询数量
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, CommonUtil.getAddress(mActivity) + "/app/goods_evaluate.htm",
                response -> {
                    try {
                        String ret = response.getString("ret");
                        //添加评价数量
                        String[] wellNum = response.getString("well").split("-");
                        rbtn_evaluate_well.setText(mActivity.getResources().getString(R.string.evaluate_well) + "(" + wellNum[0] + ")");
                        String[] middleNum = response.getString("middle").split("-");
                        rbtn_evaluate_middle.setText(mActivity.getResources().getString(R.string.evaluate_middle) + "(" + middleNum[0] + ")");
                        String[] badNum = response.getString("bad").split("-");
                        rbtn_evaluate_bad.setText(mActivity.getResources().getString(R.string.evaluate_bad) + "(" + badNum[0] + ")");
                        String allNum = String.valueOf(Integer.valueOf(wellNum[0]) + Integer.valueOf(middleNum[0]) + Integer.valueOf(badNum[0]));
                        rbtn_evaluate_all.setText(mActivity.getResources().getString(R.string.evaluate_all) + "(" + allNum + ")");

                        if (ret.equals("true")) {  //判断返回结果
                            JSONArray jSONArray = response.getJSONArray("eva_list");
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jsonObject = jSONArray.getJSONObject(i);
                                Map map1 = new HashMap();

                                if (jsonObject.has("user") && !TextUtils.isEmpty(jsonObject.getString("user"))) {
                                    map1.put("user", jsonObject.getString("user"));
                                } else {
                                    map1.put("user", "匿名");
                                }

                                if (jsonObject.has("content") && !TextUtils.isEmpty(jsonObject.getString("content"))) {
                                    map1.put("content", jsonObject.getString("content"));
                                } else {
                                    map1.put("content", "");
                                }

                                map1.put("addTime", jsonObject.getString("addTime"));
                                map1.put("user_img", jsonObject.getString("user_img"));
                                if (jsonObject.has("add_content"))
                                    map1.put("add_content", jsonObject.getString("add_content"));
                                if (jsonObject.has("evaluate_photos"))
                                    map1.put("evaluate_photos", jsonObject.getJSONArray("evaluate_photos"));
                                if (jsonObject.has("add_evaluate_photos"))
                                    map1.put("add_evaluate_photos", jsonObject.getJSONArray("add_evaluate_photos"));
                                eva_list.add(map1);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mActivity.hideProcessDialog(1);
                    }
                    if (beginCount == 0 && mEvaluationListAdapter.getCount() == 0) {
                        if (rootView != null) {
                            pullToRefreshListView.setVisibility(View.GONE);
                            if (mActivity.findViewById(R.id.nodata) != null) {
                                mActivity.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            }
                            if (mActivity.findViewById(R.id.nodata_refresh) != null) {
                                mActivity.findViewById(R.id.nodata_refresh).setVisibility(View.GONE);
                            }
                        }
                    } else {
                        pullToRefreshListView.setVisibility(View.VISIBLE);
                        if (mActivity.findViewById(R.id.nodata) != null) {
                            mActivity.findViewById(R.id.nodata).setVisibility(View.GONE);
                        }
                    }
                    beginCount += selectCount;
                    mEvaluationListAdapter.notifyDataSetChanged();
                    mActivity.hideProcessDialog(1);
                }, error -> {
                    if (rootView != null) {
                        //网络异常操作
                        CommonUtil.showSafeToast(mActivity, "网络异常，请稍后评价");
                        mActivity.hideProcessDialog(1);
                        pullToRefreshListView.setVisibility(View.GONE);
                        mActivity.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        mActivity.findViewById(R.id.nodata_refresh).setVisibility(View.GONE);
                    }
                }, map);
        mRequestQueue.add(request);

    }


}
