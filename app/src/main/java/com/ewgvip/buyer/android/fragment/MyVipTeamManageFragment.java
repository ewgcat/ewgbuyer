package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.MyVipTeamListadapter;
import com.ewgvip.buyer.android.models.GradeNameBean;
import com.ewgvip.buyer.android.models.TeamItem;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * 我的vip团队管理.
 */
public class MyVipTeamManageFragment extends Fragment {

    private View rootView;
    private BaseActivity mActivity;
    private ArrayList<TeamItem> list = new ArrayList<TeamItem>();
    private SharedPreferences preferences;
    private int VisibleItemCount;
    private TextView tv_load_more;
    private int visibleLastIndex = 0;   //最后的可视项索引
    int begincount = 0;
    int selectcount = 10;
    private ListView listview;
    private MyVipTeamListadapter adapter;
    private TextView tv_teamlist_detail;
    private TextView tv_select_teamlist_detail;
    private String child_one_total;
    private String child_two_total;
    private String child_three_total;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.mActivity = null;
        this.list = null;
        this.listview = null;
        this.adapter = null;
        this.tv_teamlist_detail = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        rootView = inflater.inflate(R.layout.fragment_my_vip_team_manage, container, false);

        tv_teamlist_detail = (TextView) rootView.findViewById(R.id.tv_teamlist_detail);
        tv_select_teamlist_detail = (TextView) rootView.findViewById(R.id.tv_select_teamlist_detail);

        //返回键
        ImageView iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });

        listview = (ListView) rootView.findViewById(R.id.listview);
        adapter = new MyVipTeamListadapter(mActivity, list);
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
                VisibleItemCount = visibleItemCount;
                visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });

        listview.setOnItemClickListener((parent, view, position, id) -> {
            TextView tv_team_count = (TextView) view.findViewById(R.id.tv_team_count);
            String s = tv_team_count.getText().toString();
            if (!s.equals("0人")) {
                Bundle bundle = new Bundle();
                bundle.putString("select_user_id", list.get(position).user_id);
                mActivity.go_to_team2(bundle);
            }
        });
        return rootView;
    }





    private void loadData() {
        String user_id = preferences.getString("user_id", "");
        Map paraMap = mActivity.getParaMap();
        paraMap.put("begincount", begincount + "");
        paraMap.put("selectcount", selectcount + "");
        paraMap.put("select_user_id", user_id);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request =new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/vip_team_manage.htm",
                jsonObject -> {
                    try {
                        String ret = jsonObject.get("ret").toString();
                        if ("1".equals(ret)) {
                            if (jsonObject.has("child_one_total")){
                                child_one_total = jsonObject.get("child_one_total").toString();
                                if (TextUtils.isEmpty(child_one_total.trim())){
                                    child_one_total ="0";
                                }
                            }
                            if (jsonObject.has("child_two_total")){
                                child_two_total = jsonObject.get("child_two_total").toString();
                                if (TextUtils.isEmpty(child_one_total.trim())){
                                    child_two_total ="0";
                                }
                            }
                            if (jsonObject.has("child_three_total")){
                                child_three_total = jsonObject.get("child_three_total").toString();
                                if (TextUtils.isEmpty(child_one_total.trim())){
                                    child_three_total ="0";
                                }
                            }
                            tv_teamlist_detail.setText("梦想e+:" + child_one_total + "人 激情e+:" + child_two_total + "人 疯狂e+:" + child_three_total + "人");
                            tv_select_teamlist_detail.setText("梦想e+");
                            //封装团队信息对象到集合
                            String teamlist = jsonObject.get("teamlist").toString();
                            JSONArray teamlistJsonArr = new JSONArray(teamlist);
                            if (teamlistJsonArr.length() > 0) {
                                begincount += 10;
                                for (int i = 0; i < teamlistJsonArr.length(); i++) {
                                    JSONObject teamlistobj = (JSONObject) teamlistJsonArr.get(i);
                                    String photo_url = teamlistobj.get("photo_url").toString();
                                    String userName = teamlistobj.get("userName").toString();
                                    String child_size = teamlistobj.get("child_size").toString();
                                    String addTime = teamlistobj.get("addTime").toString();
                                    String user_id1 = teamlistobj.get("user_id").toString();
                                    String gradeName = teamlistobj.get("gradeName").toString();
                                    JSONObject obj = new JSONObject(gradeName);
                                    String icon = obj.get("icon").toString();
                                    String name = obj.get("name").toString();
                                    GradeNameBean gradeNameBean = new GradeNameBean(icon, name);
                                    TeamItem teamItem = new TeamItem(addTime, child_size, photo_url, userName, user_id1, gradeNameBean);
                                    list.add(teamItem);
                                }
                                mActivity.runOnUiThread(() -> adapter.notifyDataSetChanged());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test",error.toString()),paraMap);
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
                if (getTargetFragment()!=null){
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 100, intent);
                }
                return true;
            }
            return false;
        });
    }


}
