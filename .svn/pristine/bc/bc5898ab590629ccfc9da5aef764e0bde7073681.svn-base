package com.ewgvip.buyer.android.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
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
 * Created by Administrator on 2015/11/26.
 */
public class ComplainListFragment extends Fragment {
    public static int NUM = 1;
    private final int selectCount = 20;
    private List resultList = new ArrayList();
    private View rootView;
    private BaseActivity mActivity;
    private int beginCount = 0;
    private com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView lv_unresult_consult;
    private ComplainListAdapter adapter;

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
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
        lv_unresult_consult = (PullToRefreshListView) rootView.findViewById(R.id.lv_unresult_consult);
        /**
         * 底部更新
         */
        lv_unresult_consult.setOnLastItemVisibleListener(() -> getData());
        /**
         * 下拉刷新
         */
        lv_unresult_consult.setOnRefreshListener(refreshView -> {
            beginCount = 0;
            resultList.clear();
            new GetDataTask().execute();
        });
        getData();
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getData();
    }

    public void getData() {
        Map paramap = mActivity.getParaMap();
        paramap.put("beginCount", beginCount);
        paramap.put("selectCount", selectCount);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/order_2_complain.htm",
                result -> {
                    try {
                        Log.e("投诉：",result.toString());
                        String code = result.getString("code");
                        if ("100".equals(code)) {
                            JSONArray datasList = result.getJSONArray("datas");
                            for (int i = 0; i < datasList.length(); i++) {
                                JSONObject data = datasList.getJSONObject(i);
                                Map map = new HashMap();
                                map.put("addTime", data.getString("addTime"));
                                map.put("oid", data.getString("oid"));
                                map.put("order_id", data.getString("order_id"));
                                JSONArray orderGoodsList = data.getJSONArray("goods_maps");
                                List goodsList = new ArrayList();
                                for (int j = 0; j < orderGoodsList.length(); j++) {
                                    JSONObject goods = orderGoodsList.getJSONObject(j);
                                    Map goodsMap = new HashMap();
                                    goodsMap.put("goods_count", goods.has("goods_count") ? goods.getString("goods_count") : "");
                                    goodsMap.put("goods_gsp_ids", goods.has("goods_gsp_ids") ? goods.getString("goods_gsp_ids") : "");
                                    goodsMap.put("goods_id", goods.has("goods_id") ? goods.getString("goods_id") : "");
                                    goodsMap.put("goods_img", goods.has("goods_img") ? goods.getString("goods_img") : "");
                                    goodsMap.put("goods_name", goods.has("goods_name") ? goods.getString("goods_name") : "");
                                    goodsMap.put("goods_price", goods.has("goods_count") ? goods.getString("goods_count") : "");
                                    goodsList.add(goodsMap);
                                }
                                map.put("goodsList", goodsList);
                                resultList.add(map);
                            }
                            if (null == adapter) {
                                adapter = new ComplainListAdapter(resultList);
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

    public static class ViewHolder {
        public TextView order_addTime;
        public TextView order_sn;
        public LinearLayout order_goods;
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

    private class ComplainListAdapter extends BaseAdapter {
        private List list;

        public ComplainListAdapter(List list) {
            this.list = list;
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position) {

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            if (convertView == null) {
                view = View.inflate(mActivity, R.layout.order_goods_return_list_item, null);
                holder = new ViewHolder();
                holder.order_sn = (TextView) view.findViewById(R.id.order_sn);
                holder.order_addTime = (TextView) view.findViewById(R.id.order_price);
                holder.order_goods = (LinearLayout) view.findViewById(R.id.return_list);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            final int index = position;
            final Map map = (Map) list.get(position);
            holder.order_sn.setText(mActivity.getResources().getString(R.string.order_id) + map.get("order_id").toString());
            holder.order_addTime.setText(mActivity.getResources().getString(R.string.order_time) + map.get("addTime").toString());
            holder.order_goods.removeAllViews();
            List goodsList = (List) map.get("goodsList");
            for (int j = 0; j < goodsList.size(); j++) {
                final Map goodsMap = (Map) goodsList.get(j);
                String goods_name = goodsMap.get("goods_name").toString();
                String goods_img = goodsMap.get("goods_img").toString();
                if (!"".equals(goods_name) && !"".equals(goods_img)) {
                    View return_goods = View.inflate(mActivity, R.layout.return_goods, null);
                    TextView tv = (TextView) return_goods.findViewById(R.id.goods_name);
                    tv.setText(goods_name);
                    SimpleDraweeView iv_goods_icon = (SimpleDraweeView) return_goods.findViewById(R.id.iv_goods_icon);
                    iv_goods_icon.setImageURI(Uri.parse(goods_img));
                    Button order_button = (Button) return_goods.findViewById(R.id.order_button);
                    order_button.setText(mActivity.getResources().getString(R.string.complain_goods));
                    order_button.setOnClickListener(view1 -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", map.get("order_id").toString());
                            bundle.putString("oid", map.get("oid").toString());
                            bundle.putString("goods_id", goodsMap.get("goods_id").toString());
                            bundle.putString("goods_gsp_ids", goodsMap.get("goods_gsp_ids").toString());
                            bundle.putString("goods_img", goodsMap.get("goods_img").toString());
                            bundle.putString("goods_name", goodsMap.get("goods_name").toString());
                            bundle.putString("goods_price", goodsMap.get("goods_price").toString());
                            bundle.putString("addTime", map.get("addTime").toString());
                            mActivity.go_my_complain(bundle, mActivity.getCurrentfragment());
                        }
                    }
                    );
                    holder.order_goods.addView(return_goods);
                }
            }
            return view;
        }
    }
}
