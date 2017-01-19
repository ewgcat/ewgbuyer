package com.ewgvip.buyer.android.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.utils.ViewPagerFragmentAllRefresh;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: ConsultResultFragment.java
 * </p>
 * <p/>
 * <p>
 * Description: 已经回复的商品咨询
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p/>
 * <p>
 * Company: 沈阳网之商科技有限公司 www.iskyshop.com
 * </p>
 *
 * @author niecong
 * @version 1.0
 * @date 2015-11-25
 */
public class ConsultResultFragment extends Fragment {
    private final int selectCount = 5;
    private List resultList = new ArrayList();
    private View rootView;
    private BaseActivity mActivity;
    private int beginCount = 0;
    private com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView lv_unresult_consult;
    private UnResultConsultAdapter adapter;
    private int flag;
    private ViewPagerFragmentAllRefresh viewPagerFragmentAllRefresh;



    public void setViewPagerFragmentAllRefresh(ViewPagerFragmentAllRefresh viewPagerFragmentAllRefresh) {
        if (null != viewPagerFragmentAllRefresh) {
            this.viewPagerFragmentAllRefresh = viewPagerFragmentAllRefresh;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.resultList = null;
        this.lv_unresult_consult = null;
        this.adapter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_consult_unresult, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        Bundle bundle = getArguments();
        flag = bundle.getInt("flag");
        lv_unresult_consult = (PullToRefreshListView) rootView.findViewById(R.id.lv_unresult_consult);
        /**
         * 底部更新
         */
        lv_unresult_consult.setOnLastItemVisibleListener(() -> getData());
        /**
         * 下拉刷新
         */
        lv_unresult_consult.setOnRefreshListener(refreshView -> {
            refreshData();
            viewPagerFragmentAllRefresh.hideRefreshViewPager(flag);
        });
        getData();
        return rootView;
    }

    public void refreshData() {
        beginCount = 0;
        resultList.clear();
        new GetDataTask().execute();
    }

    //刷新商品操作
    public void getData() {
        Map paramap = mActivity.getParaMap();
        paramap.put("beginCount", beginCount);
        paramap.put("selectCount", selectCount);
        if (0 == flag) {
            paramap.put("reply", "false");
        } else if (1 == flag) {
            paramap.put("reply", "true");
        }
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/consult.htm",
                result -> {
                    try {
                        String code = result.getString("code");
                        if ("100".equals(code)) {
                            JSONArray datasList = result.getJSONArray("datas");
                            for (int i = 0; i < datasList.length(); i++) {
                                JSONObject data = datasList.getJSONObject(i);
                                Map map = new HashMap();
                                map.put("addTime", data.getString("addTime"));
                                map.put("content", data.getString("content"));
                                map.put("goods_domainPath", data.getString("goods_domainPath"));
                                map.put("goods_id", data.getString("goods_id"));
                                map.put("goods_main_photo", data.getString("goods_main_photo"));
                                map.put("goods_name", data.getString("goods_name"));
                                map.put("goods_price", data.getString("goods_price"));
                                map.put("reply", data.getString("reply"));
                                if (1 == flag) {
                                    map.put("reply_content", data.getString("reply_content"));
                                    map.put("reply_time", data.getString("reply_time"));
                                    map.put("reply_user", data.getString("reply_user"));
                                }
                                resultList.add(map);
                            }
                            if (null == adapter) {
                                adapter = new UnResultConsultAdapter(mActivity, resultList);
                                lv_unresult_consult.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            if (resultList.size() > 0) {
                                lv_unresult_consult.setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.nothing).setVisibility(View.GONE);
                            } else {
                                rootView.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                                lv_unresult_consult.setVisibility(View.GONE);
                            }
                        } else {
                            rootView.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                            lv_unresult_consult.setVisibility(View.GONE);
                        }
                        mActivity.hideProcessDialog(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    beginCount += selectCount;
                    lv_unresult_consult.onRefreshComplete();
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

//    @Override
//    public void onResume() {
//        Log.e("0123457899876543210", "onResume()");
//        super.onResume();
//        mActivity.showProcessDialog();
//        resultList.clear();
//        beginCount = 0;
//        getData();
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mActivity.showProcessDialog();
        resultList.clear();
        beginCount = 0;
        getData();
    }

    public static class ViewHolder {
        private RelativeLayout rl_goods;
        private SimpleDraweeView img;
        private TextView tv_goods_name;
        private TextView tv_goods_price;
        private TextView tv_consult_time;
        private TextView tv_consult_question;
        private TextView tv_reple_time;
        private TextView tv_reply_user;
        private TextView tv_consult_answer;
        private RelativeLayout rl_reple_time;
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] str = {};
            getData();
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // 添加新加载的信息
            super.onPostExecute(result);
        }
    }

    private class UnResultConsultAdapter extends BaseAdapter {
        private List list;
        private BaseActivity mActivity;

        public UnResultConsultAdapter(BaseActivity mActivity, List list) {
            this.list = list;
            this.mActivity = mActivity;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int i) {
            return i;
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = null;
            ViewHolder viewHolder = null;
            if (null == convertView) {
                view = View.inflate(mActivity, R.layout.item_user_center_consult, null);
                viewHolder = new ViewHolder();
                viewHolder.rl_goods = (RelativeLayout) view.findViewById(R.id.rl_goods);
                viewHolder.img = (SimpleDraweeView) view.findViewById(R.id.img);
                viewHolder.tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
                viewHolder.tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);
                viewHolder.tv_consult_time = (TextView) view.findViewById(R.id.tv_consult_time);
                viewHolder.tv_consult_question = (TextView) view.findViewById(R.id.tv_consult_question);
                viewHolder.tv_consult_answer = (TextView) view.findViewById(R.id.tv_consult_answer);
                if (0 == flag) {
                    viewHolder.rl_reple_time = (RelativeLayout) view.findViewById(R.id.rl_reple_time);
                } else if (1 == flag) {
                    viewHolder.tv_reple_time = (TextView) view.findViewById(R.id.tv_reple_time);
                    viewHolder.tv_reply_user = (TextView) view.findViewById(R.id.tv_reply_user);
                    viewHolder.tv_consult_answer = (TextView) view.findViewById(R.id.tv_consult_answer);
                }
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            Map map = (HashMap) list.get(i);
            String addTime = (String) map.get("addTime");
            String content = (String) map.get("content");
            /*String goods_domainPath = (String) map.get("goods_domainPath");*/
            final String goods_id = (String) map.get("goods_id");
            String goods_main_photo = (String) map.get("goods_main_photo");
            String goods_name = (String) map.get("goods_name");
            String goods_price = (String) map.get("goods_price");
            /*String reply = (String) map.get("reply");*/
            String reply_content = (String) map.get("reply_content");
            String reply_time = (String) map.get("reply_time");
            String reply_user = (String) map.get("reply_user");
            viewHolder.rl_goods.setOnClickListener(view1 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_goods(goods_id);
                }
            });
            BaseActivity.displayImage(getResources().getString(R.string.http_url) + "/" + goods_main_photo, viewHolder.img);
            viewHolder.tv_goods_name.setText(goods_name);
            viewHolder.tv_goods_price.setText(goods_price);
            viewHolder.tv_consult_time.setText(addTime);
            viewHolder.tv_consult_question.setText(content);
            if (0 == flag) {
                viewHolder.rl_reple_time.setVisibility(View.GONE);
                viewHolder.tv_consult_answer.setText(getResources().getString(R.string.no_answer));
            } else if (1 == flag) {
                viewHolder.tv_reple_time.setText(reply_time);
                viewHolder.tv_reply_user.setText(reply_user);
                viewHolder.tv_consult_answer.setText(reply_content);
            }
            return view;
        }
    }
}
