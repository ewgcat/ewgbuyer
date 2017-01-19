package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.SosUniversalAdapter;
import com.ewgvip.buyer.android.layout.SosUniversalListView;
import com.ewgvip.buyer.android.models.Area;
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
import java.util.List;

/**
 * 定位城市
 *
 * @author lgx
 *         A simple {@link Fragment} subclass.
 */
public class GroupCityChooseFragment extends Fragment {
    private BaseActivity mActivity;
    private SosUniversalListView lsComposer; // Diy ListView
    private SectionComposerAdapter adapter; // 适配器
    private SharedPreferences preferences;
    private View rootView;
    private View scrollView;
    private List<Area> localList;
    private String city;


    @Override
    public void onDetach() {
        super.onDetach();
        this.lsComposer = null;
        this.adapter = null;
        this.preferences = null;
        this.rootView = null;
        this.scrollView = null;
        this.localList = null;
        this.city = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_brand, container,
                false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("城市");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        city = preferences.getString("city", "");
        localList = new ArrayList<Area>();
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);

        lsComposer = (SosUniversalListView) rootView.findViewById(R.id.listview);
        scrollView = LayoutInflater.from(mActivity).inflate(R.layout.item_composer_header, lsComposer, false);
        mActivity.showProcessDialog();
        final List<Pair<String, List<Area>>> list = new ArrayList<Pair<String, List<Area>>>();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/getAreaIndex.htm",
                result -> {
                    try {
                        JSONArray areaLists = result.getJSONArray("area_list");// 获取JSONArray
                        int length = areaLists.length();
                        for (int i = 0; i < length; i++) {// 遍历JSONArray
                            JSONObject oj = areaLists.getJSONObject(i);
                            JSONArray areaList = oj.getJSONArray("area_list");
                            int length2 = areaList.length();
                            List<Area> relist = new ArrayList<Area>();
                            for (int j = 0; j < length2; j++) {
                                JSONObject area = areaList.getJSONObject(j);
                                if (area.get("areaName").toString().equals(city)) {
                                    localList.add(new Area(area.get("id").toString(), area.get("areaName").toString()));
                                }
                                relist.add(new Area(area.get("id").toString(), area.get("areaName").toString()));
                            }
                            if (i == 0) {
                                list.add(new Pair<String, List<Area>>("当前定位的城市", localList));
                            }
                            list.add(new Pair<String, List<Area>>(oj.getString("word").toString(), relist));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (lsComposer != null) {
                        lsComposer.setAdapter(adapter = new SectionComposerAdapter(list));
                        lsComposer.setPinnedHeaderView(scrollView);
                        lsComposer.setOnItemClickListener((parent, view, position, id) -> {
                            TextView area_id = (TextView) view.findViewById(R.id.area_id);
                            TextView area_name = (TextView) view.findViewById(R.id.area_name);
                            SharedPreferences.Editor edit = preferences.edit();
                            Bundle bundle = new Bundle();
                            bundle.putInt("flag", 2);
                            edit.putString("cityId", area_id.getText().toString());
                            edit.putString("areaName", area_name.getText().toString());
                            edit.commit();
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            if (getTargetFragment() == null)
                                return;
                            getTargetFragment().onActivityResult(getTargetRequestCode(), GroupShoppingFragment.NUM, intent);
                            getFragmentManager().popBackStack();

                        });
                        adapter.notifyDataSetChanged();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), null);
        mRequestQueue.add(request);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu,true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
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
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class SectionComposerAdapter extends SosUniversalAdapter {
        List<Pair<String, List<Area>>> all;

        public SectionComposerAdapter(List<Pair<String, List<Area>>> list) {
            all = list;
        }

        public int getCount() {
            int res = 0;
            for (int i = 0; i < all.size(); i++) {
                res += all.get(i).second.size();
            }
            return res;
        }

        public Area getItem(int position) {
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return all.get(i).second.get(position - c);
                }
                c += all.get(i).second.size();
            }
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        protected void onNextPageRequested(int page) {
        }

        protected void bindSectionHeader(View view, int position,
                                         boolean displaySectionHeader) {
            if (displaySectionHeader) {
                view.findViewById(R.id.header).setVisibility(View.VISIBLE);
                TextView lSectionTitle = (TextView) view.findViewById(R.id.header);
                lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
            } else {
                view.findViewById(R.id.header).setVisibility(View.GONE);
            }
        }

        public View getAmazingView(int position, View convertView,
                                   ViewGroup parent) {
            View res = convertView;
            if (res == null)
                res = mActivity.getLayoutInflater().inflate(R.layout.item_area_composer, null);

            TextView id = (TextView) res.findViewById(R.id.area_id);
            TextView name = (TextView) res.findViewById(R.id.area_name);

            Area composer = getItem(position);
            id.setText(composer.id);
            name.setText(composer.areaName);

            return res;
        }

        public void configurePinnedHeader(View header, int position, int alpha) {
            TextView lSectionHeader = (TextView) header;
            lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
            lSectionHeader.setBackgroundResource(R.color.brandtop);
            lSectionHeader.setTextColor(getResources().getColor(R.color.textdark));
        }

        public int getPositionForSection(int section) {
            if (section < 0)
                section = 0;
            if (section >= all.size())
                section = all.size() - 1;
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (section == i) {
                    return c;
                }
                c += all.get(i).second.size();
            }
            return 0;
        }

        public int getSectionForPosition(int position) {
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return i;
                }
                c += all.get(i).second.size();
            }
            return -1;
        }

        public String[] getSections() {
            String[] res = new String[all.size()];
            for (int i = 0; i < all.size(); i++) {
                res[i] = all.get(i).first;
            }
            return res;
        }

    }


}