package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ComplainThemeFragment extends Fragment {
    private final int selectCount = 5;
    private List resultList = new ArrayList();
    private View rootView;
    private BaseActivity mActivity;
    private int beginCount = 0;
    private Bundle bundle;
    private com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView lv_address_select;
    private ComplainChemeAdapter complainChemeAdapter;
    private String selectIdString;

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
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar, container, false);
        mActivity = (BaseActivity) getActivity();
        lv_address_select = (com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView) rootView.findViewById(R.id.listview);
        bundle = getArguments();
        selectIdString = bundle.getString("selectIdString");
        getData();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("选择主题");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
     *
     * @param menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
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


    public void getData() {
        Map paramap = mActivity.getParaMap();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/complaint_subject.htm",
                result -> {
                    try {
                        String code = result.getString("code");
                        if ("100".equals(code)) {
                            JSONArray datasList = result.getJSONArray("datas");
                            for (int i = 0; i < datasList.length(); i++) {
                                JSONObject data = datasList.getJSONObject(i);
                                Map map = new HashMap();
                                map.put("content", data.getString("content"));
                                map.put("id", data.getString("id"));
                                map.put("title", data.getString("title"));
                                resultList.add(map);
                            }
                            if (null == complainChemeAdapter) {
                                complainChemeAdapter = new ComplainChemeAdapter(resultList);
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
                                TextView textView = (TextView) rootView.findViewById(R.id.nodata_reason);
                                textView.setText("系统未设置投诉主题，不可投诉！");
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
//                        lv_unresult_consult.onRefreshComplete();
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

    private static class ViewHolder {
        private ImageView iv_tickpng;
        private TextView tv_name;
        private TextView tv_address;
        private TextView tv_phone;
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

    private class ComplainChemeAdapter extends BaseAdapter {
        private List list;

        public ComplainChemeAdapter(List list) {
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
            ViewHolder viewHolder = null;
            if (convertView != null) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(mActivity, R.layout.user_extract_address, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, MainActivity.dp2px(mActivity, 72));
                view.setLayoutParams(lp);
                viewHolder = new ViewHolder();
                viewHolder.iv_tickpng = (ImageView) view.findViewById(R.id.iv_tickpng);
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_address);
                viewHolder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
                view.setTag(viewHolder);
            }
            final Map map = (Map) list.get(position);
            final String id = (String) map.get("id");
            final String title = (String) map.get("title");
            final String content = (String) map.get("content");
            viewHolder.tv_name.setText(title);
            viewHolder.tv_address.setText("");
            viewHolder.tv_phone.setText(content);
            if (id.equals(selectIdString)) {
                viewHolder.iv_tickpng.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_tickpng.setVisibility(View.INVISIBLE);
            }
            view.setOnClickListener(view1 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("selectIdString", id);
                    bundle1.putString("selectThemeString", title);
                    Intent intent = new Intent();
                    intent.putExtras(bundle1);
                    if (getTargetFragment() != null) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
                    }
                    getFragmentManager().popBackStack();
                }
            });
            return view;
        }
    }
}
