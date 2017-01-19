package com.ewgvip.buyer.android.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.MyVipRewardListadapter;
import com.ewgvip.buyer.android.models.VipRewardItem;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPost2Request;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的vip奖励管理.
 */
public class MyVipRewardManageFragment extends Fragment {

    private View rootView;
    private BaseActivity mActivity;
    private ArrayList<VipRewardItem> list = new ArrayList<VipRewardItem>();
    private SharedPreferences preferences;
    private int visibleLastIndex = 0;   //最后的可视项索引
    int begincount = 0;
    int selectcount = 10;
    private ListView listview;
    private MyVipRewardListadapter adapter;
    private int VisibleItemCount;
    private TextView tv_load_more;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
    }

    public MyVipRewardManageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        rootView = inflater.inflate(R.layout.fragment_my_vip_reward_manage, container, false);
        //返回键
        ImageView iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });

        listview = (ListView) rootView.findViewById(R.id.listview);

        adapter = new MyVipRewardListadapter(mActivity, list);
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

        listview.setOnItemClickListener((parent, view, position, id) -> {
            final VipRewardItem vipRewardItem = list.get(position);
            String vipRewardState = vipRewardItem.vip_reward_state;
            if (vipRewardState.equals("待领取")) {
                new AlertDialog.Builder(mActivity).setMessage("领取到红包")
                        .setPositiveButton("确定", (dialog, which) -> getRewardToRedPackage(vipRewardItem))
                        .setNegativeButton("取消", (dialog, which) -> {
                        }).create().show();
            }
        });


        return rootView;
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                ArrayList<VipRewardItem> mlist = (ArrayList<VipRewardItem>) msg.obj;
                adapter.update(mlist);

            }
            return false;
        }
    });

    private void getRewardToRedPackage(VipRewardItem vipRewardItem) {
        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        final Map paraMap = new HashMap();
        paraMap.put("user_id", user_id);
        paraMap.put("token", token);
        paraMap.put("id", vipRewardItem.id);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        NormalPostRequest request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/vip_reward_manage_receive.htm",
                response -> {
                    try {
                        String ret = response.get("ret").toString();
                        if (ret.contains("0")) {
                            CommonUtil.showSafeToast(mActivity, "奖励已领取");
                        } else if (ret.contains("RECEIVED")) {
                            CommonUtil.showSafeToast(mActivity, "已领取");
                        } else if (ret.contains("WAITING_RECEIVE")) {
                            CommonUtil.showSafeToast(mActivity, "等待领取");
                        } else if (ret.contains("SEND_OUT")) {
                            CommonUtil.showSafeToast(mActivity, "奖励已发放到红包");
                            new Thread(() -> {
                                loadDataAfterGetReward();
                            }).start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test", error.toString()), paraMap);
        mRequestQueue.add(request);
    }


    public void loadDataAfterGetReward() {
        begincount = 0;
        selectcount = 10;
        list.clear();
        loadData();
    }


    private void loadData() {
        Map paraMap = mActivity.getParaMap();
        paraMap.put("begincount", begincount + "");
        paraMap.put("selectcount", selectcount + "");
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONArray> request = new NormalPost2Request(mActivity, getResources().getString(R.string.http_url) + "/app/buyer/vip_reward_manage_item.htm",
                response -> {
                    try {
                        if (response.length() > 0) {
                            begincount += 10;
                            selectcount += 10;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                String vip_reward_state = jsonObject.get("vip_reward_state").toString();
                                if ("WAITING_RECEIVE".equals(vip_reward_state)) {
                                    vip_reward_state = "待领取";
                                } else if ("RECEIVED".equals(vip_reward_state)) {
                                    vip_reward_state = "已领取";
                                } else if ("SEND_OUT".equals(vip_reward_state)) {
                                    vip_reward_state = "已发放";
                                }
                                String add_time = jsonObject.get("add_time").toString();
                                String id = jsonObject.get("id").toString();
                                String photo = jsonObject.get("photo").toString();
                                String reward_money = jsonObject.get("reward_money").toString();
                                String userName = jsonObject.get("userName").toString();
                                String user_relationship_name = jsonObject.get("user_relationship_name").toString();
                                VipRewardItem item = new VipRewardItem(vip_reward_state, add_time, id, photo, reward_money, userName, user_relationship_name);
                                list.add(item);
                            }
                            Message msg = Message.obtain();
                            msg.obj = list;
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        } else {
                            Toast.makeText(mActivity, "没有更多返佣记录", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("test", error.toString());
                    Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
                }, paraMap);
        mRequestQueue.add(request);
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
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Intent intent = new Intent();
                getFragmentManager().popBackStack();
                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 100, intent);
                }
                return true;
            }
            return false;
        });
    }


}
