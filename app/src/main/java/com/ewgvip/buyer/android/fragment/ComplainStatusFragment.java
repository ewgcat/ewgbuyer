package com.ewgvip.buyer.android.fragment;


import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.utils.CommonUtil;
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
 * Created by Administrator on 2015/11/26.
 */
public class ComplainStatusFragment extends Fragment {
    private final int selectCount = 20;
    private int beginCount = 0;
    private List resultList = new ArrayList();
    private View rootView;
    private BaseActivity mActivity;
    private com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView lv_address_select;
    private ComplainStausAdapter complainChemeAdapter;
    //取消投诉
    private AlertDialog selectPictureAlertDialog;

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
        this.rootView = null;
        this.resultList = null;
        this.complainChemeAdapter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listview_single, container, false);
        mActivity = (BaseActivity) getActivity();
        lv_address_select = (com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView) rootView.findViewById(R.id.listview);
        /**
         * 底部更新
         */
        lv_address_select.setOnLastItemVisibleListener(() -> getData());
        /**
         * 下拉刷新
         */
        lv_address_select.setOnRefreshListener(refreshView -> {
            beginCount = 0;
            resultList.clear();
            new GetDataTask().execute();
        });
        getData();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //菜单选点点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/complaint_list.htm",
                result -> {
                    try {
                        String code = result.getString("code");
                        if ("100".equals(code)) {
                            JSONArray datasList = result.getJSONArray("datas");
                            for (int i = 0; i < datasList.length(); i++) {
                                JSONObject data = datasList.getJSONObject(i);
                                Map map = new HashMap();
                                map.put("id", data.has("id") ? data.getString("id") : "");
                                map.put("store_name", data.has("store_name") ? data.getString("store_name") : "");
                                map.put("addTime", data.has("addTime") ? data.getString("addTime") : "");
                                map.put("status", data.has("status") ? data.getString("status") : "");
                                map.put("img", data.has("img") ? data.getString("img") : "");
                                resultList.add(map);
                            }
                            if (null == complainChemeAdapter) {
                                complainChemeAdapter = new ComplainStausAdapter(resultList);
                                lv_address_select.setAdapter(complainChemeAdapter);
                            } else {
                                complainChemeAdapter.notifyDataSetChanged();
                            }
                            if (resultList.size() > 0) {
                                lv_address_select.setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                            } else {
                                rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                                lv_address_select.setVisibility(View.GONE);
                            }
                        } else {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            lv_address_select.setVisibility(View.GONE);
                        }
                        mActivity.hideProcessDialog(0);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    beginCount += selectCount;
                    lv_address_select.onRefreshComplete();
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

    private void showCancelComplainAlertDialog(final String id, final int position) {
        View view = View.inflate(mActivity, R.layout.dialog_delete_complain, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        Button b_cancel_complain = (Button) view.findViewById(R.id.b_cancel_complain);
        Button b_cancel_commit = (Button) view.findViewById(R.id.b_cancel_commit);
        b_cancel_complain.setOnClickListener(view1 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.showProcessDialog();
                Map paramap = mActivity.getParaMap();
                paramap.put("id", id);
                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
                Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/complaint_cancel.htm",
                        result -> {
                            try {
                                String code = result.getString("code");
                                if ("100".equals(code)) {
                                    resultList.remove(position);
                                    complainChemeAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mActivity, "取消失败", Toast.LENGTH_SHORT).show();
                                }
                                mActivity.hideProcessDialog(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> mActivity.hideProcessDialog(1), paramap);
                mRequestQueue.add(request);
                selectPictureAlertDialog.dismiss();
            }
        });
        b_cancel_commit.setOnClickListener(view12 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                selectPictureAlertDialog.dismiss();
            }
        });
        selectPictureAlertDialog = builder.create();
        selectPictureAlertDialog.setView(view, 0, 0, 0, 0);
        selectPictureAlertDialog.show();
    }

    private static class ViewHolder {
        private com.facebook.drawee.view.SimpleDraweeView image_complain_picture;
        private TextView tv_complain_store;
        private TextView tv_complain_status;
        private TextView tv_complain_time;
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

    private class ComplainStausAdapter extends BaseAdapter {
        private List list;

        public ComplainStausAdapter(List list) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder viewHolder = null;
            if (convertView != null) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(mActivity, R.layout.item_complain_status, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, MainActivity.dp2px(mActivity, 72));
                view.setLayoutParams(lp);
                viewHolder = new ViewHolder();
                viewHolder.image_complain_picture = (com.facebook.drawee.view.SimpleDraweeView) view.findViewById(R.id.image_complain_picture);
                viewHolder.tv_complain_store = (TextView) view.findViewById(R.id.tv_complain_store);
                viewHolder.tv_complain_status = (TextView) view.findViewById(R.id.tv_complain_status);
                viewHolder.tv_complain_time = (TextView) view.findViewById(R.id.tv_complain_time);
                view.setTag(viewHolder);
            }
            Map map = (Map) list.get(position);
            final String id = (String) map.get("id");
            String store_name = (String) map.get("store_name");
            String addTime = (String) map.get("addTime");
            String status = (String) map.get("status");
            String img = (String) map.get("img");
            BaseActivity.displayImage(img, viewHolder.image_complain_picture);
            viewHolder.tv_complain_store.setText(store_name);
            if ("0".equals(status)) {
                viewHolder.tv_complain_status.setText("新投诉");
            } else if ("1".equals(status)) {
                viewHolder.tv_complain_status.setText("待申诉");
            } else if ("2".equals(status)) {
                viewHolder.tv_complain_status.setText("沟通中");
            } else if ("3".equals(status)) {
                viewHolder.tv_complain_status.setText("待仲裁");
            } else if ("4".equals(status)) {
                viewHolder.tv_complain_status.setText("投诉结束");
            }
            viewHolder.tv_complain_time.setText(addTime);
            view.setOnLongClickListener(view1 -> {
                showCancelComplainAlertDialog(id, position);
                return false;
            });
            view.setOnClickListener(view12 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    showCancelComplainAlertDialog(id, position);
                }
            });
            view.setOnClickListener(view13 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle detailBundle = new Bundle();
                    detailBundle.putString("id", id);
                    mActivity.go_complain_detail(detailBundle);
                }
            });
            return view;
        }
    }
}
