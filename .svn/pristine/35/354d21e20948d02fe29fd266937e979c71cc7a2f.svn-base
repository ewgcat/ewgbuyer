package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.adapter.GoodsclassFirstAdapter;
import com.ewgvip.buyer.android.adapter.GoodsclassSecondAdapter;
import com.ewgvip.buyer.android.models.GoodsClassSecond;
import com.ewgvip.buyer.android.models.GoodsClassThird;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类
 * liugexin
 */
public class GoodsClassFragment extends Fragment {

    private static GoodsClassFragment fragment = null;
    private MainActivity mActivity;
    private ListView fristListView, secondListView;
    private View rootView;
    private List<Map<String, List<GoodsClassSecond>>> listMapSecond;//二级分类
    private List<GoodsClassSecond> listSecond;//二级分类
    private List<Map<String, Object>> first_list;//一级分类
    private List<GoodsClassThird> listThird;//三级分类
    private GoodsclassFirstAdapter goodsclassFirstAdapter;//一级分类适配器
    private GoodsclassSecondAdapter goodsclassSecondAdapter;//二级分类适配器
    private LinearLayout linearMain;//主布局
    private GoodsClassSecond goodsClassSecond;
    private GoodsClassThird goodsClassThird;
    private TextView tv_nodata;//没有数据
    private TextView tvError;//没有网络时显示文本内容

    //静态工厂方法
    public static GoodsClassFragment getInstance() {
        if (fragment == null) {
            fragment = new GoodsClassFragment();
        }
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.fristListView = null;
        this.secondListView = null;
        this.tvError = null;
        this.listMapSecond = null;
        this.listSecond = null;
        this.first_list = null;
        this.listThird = null;
        this.goodsclassFirstAdapter = null;
        this.goodsclassSecondAdapter = null;
        this.linearMain = null;
        this.goodsClassSecond = null;
        this.goodsClassThird = null;
        this.tv_nodata = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goodsclass, container, false);
        mActivity = (MainActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        mActivity.showProcessDialog();

        onInit();
        return rootView;
    }

    private void onInit() {
        tvError = (TextView) rootView.findViewById(R.id.tvError);
        tv_nodata = (TextView) rootView.findViewById(R.id.tv_nodata);
        linearMain = (LinearLayout) rootView.findViewById(R.id.linearMain);
        rootView.findViewById(R.id.scan_qr).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_scan_code();
                    }
                });
        rootView.findViewById(R.id.sort_top_search).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_search();
                    }
                });
        fristListView = (ListView) rootView.findViewById(R.id.listView);
        secondListView = (ListView) rootView.findViewById(R.id.listMain);
        listMapSecond = new ArrayList<>();
        listSecond = new ArrayList<>();
        getFirst();
    }

    /**
     * 获得第一列数据
     */
    private void getFirst() {
        String sUrl = mActivity.getAddress() + "/app/goodsclass.htm";
        final RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, sUrl,
                result -> {
                    first_list = new ArrayList<>();
                    try {
                        JSONArray nameList = result.getJSONArray("goodsclass_list");// 获取JSONArray
                        int length = nameList.length();
                        for (int i = 0; i < length; i++) {// 遍历JSONArray
                            JSONObject oj = nameList.getJSONObject(i);
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", oj.getInt("id"));
                            map.put("className", oj.getString("className"));
                            first_list.add(map);
                        }

                        goodsclassFirstAdapter = new GoodsclassFirstAdapter(mActivity, first_list);
                        goodsclassFirstAdapter.setOnClickListener(iPosition -> {
                        List<GoodsClassSecond> list = getClassSeconds(iPosition);
                        if (null == list) {
                            getSecond(iPosition);
                        } else {
                            listSecond.clear();
                            listSecond.addAll(list);
                            secondListView.setAdapter(goodsclassSecondAdapter);
                        }
                        fristListView.smoothScrollToPositionFromTop(iPosition, 0);
                        goodsclassFirstAdapter.setSelectedPosition(iPosition);
                        goodsclassFirstAdapter.notifyDataSetChanged();
                    });
                        goodsclassFirstAdapter.setSelectedPosition(0);
                        fristListView.setAdapter(goodsclassFirstAdapter);
                        getSecond(0);
                        mActivity.hideProcessDialog(0);
                        tvError.setVisibility(View.GONE);
                        linearMain.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mActivity.hideProcessDialog(1);
                    }

                }, error -> {
                    Log.i("test",error.toString());
                    mActivity.hideProcessDialog(1);
                    tvError.setVisibility(View.VISIBLE);
                    linearMain.setVisibility(View.GONE);
                }, null);
        mRequestQueue.add(request);
    }

    /**
     * 获得第二列数据
     *
     * @param iPosition
     */
    private void getSecond(final int iPosition) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(mActivity.getAddress());
        sBuffer.append("/app/goodsclass.htm?id=");
        sBuffer.append(first_list.get(iPosition).get("id"));

        final RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, sBuffer.toString(), result -> {
                    try {
                        JSONArray nameList = result.getJSONArray("goodsclass_list");// 获取JSONArray
                        int length = nameList.length();
                        if (length == 0) {
                            secondListView.setVisibility(View.GONE);
                            tv_nodata.setVisibility(View.VISIBLE);
                        } else {
                            secondListView.setVisibility(View.VISIBLE);
                            tv_nodata.setVisibility(View.GONE);
                            int iMaxSize = 0;

                            List<GoodsClassSecond> listSeconds = new ArrayList<>();
                            Map<String, List<GoodsClassSecond>> map = new HashMap<>();

                            for (int i = 0; i < length; i++) {// 遍历JSONArray
                                JSONObject oj = nameList.getJSONObject(i);
                                goodsClassSecond = new GoodsClassSecond();
                                goodsClassSecond.setId(oj.getInt("id"));
                                goodsClassSecond.setName(oj.getString("className"));
                                listThird = new ArrayList<>();

                                JSONArray jsonArray = oj.getJSONArray("third");
                                int iCount = jsonArray.length();
                                if (iMaxSize < iCount) {
                                    iMaxSize = iCount;
                                }
                                for (int j = 0; j < iCount; j++) {
                                    oj = jsonArray.getJSONObject(j);
                                    goodsClassThird = new GoodsClassThird();
                                    goodsClassThird.setId(oj.getInt("id"));
                                    goodsClassThird.setName(oj.getString("className"));
                                    goodsClassThird.setUrl(oj.getString("icon_path"));
                                    listThird.add(goodsClassThird);
                                }
                                goodsClassSecond.setMaxSize(iMaxSize);
                                goodsClassSecond.setList(listThird);
                                listSeconds.add(goodsClassSecond);
                            }
                            listSecond.clear();
                            listSecond.addAll(listSeconds);
                            map.put(String.valueOf(iPosition), listSeconds);
                            listMapSecond.add(map);
                            goodsclassSecondAdapter = new GoodsclassSecondAdapter(mActivity, listSecond);
                            secondListView.setAdapter(goodsclassSecondAdapter);
                            goodsclassSecondAdapter.setClickListener((iPosition1, iParentPosition) -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    goodsClassThird = listSecond.get(iParentPosition).getList().get(iPosition1);
                                    mActivity.go_goodslist("gc_id", String.valueOf(goodsClassThird.getId()), String.valueOf(goodsClassThird.getName()));
                                }
                            }
                            );
                            mActivity.hideProcessDialog(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mActivity.hideProcessDialog(1);
                    }
                }, error -> {
                    Log.i("test",error.toString());
                    mActivity.hideProcessDialog(1);
                }, null);
        mRequestQueue.add(request);
    }

    private List<GoodsClassSecond> getClassSeconds(int iPosition) {

        if (listMapSecond == null) {
            return null;
        }
        String sPosition = String.valueOf(iPosition);
        int iCount = listMapSecond.size();
        for (int i = 0; i < iCount; i++) {
            Map<String, List<GoodsClassSecond>> map = listMapSecond.get(i);
            if (map.containsKey(sPosition)) {
                return map.get(sPosition);
            }
        }
        return null;
    }
}
