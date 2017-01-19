package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.SosUniversalAdapter;
import com.ewgvip.buyer.android.layout.SosUniversalListView;
import com.ewgvip.buyer.android.models.Brand;
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
import java.util.List;

public class BrandFragment extends Fragment {

    private static BrandFragment fragment = null;
    BaseActivity mActivity;
    SosUniversalListView lsComposer; // Diy ListView
    SectionComposerAdapter adapter; // 适配器

    public BrandFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brand, container,
                false);
        mActivity = (BaseActivity) getActivity();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("品牌");//设置标题

        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        mActivity.showProcessDialog();

        lsComposer = (SosUniversalListView) rootView
                .findViewById(R.id.listview);

        final List<Pair<String, List<Brand>>> list = new ArrayList<Pair<String, List<Brand>>>();
        String url = "/app/brand_list.htm";
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                mActivity.getAddress() + url,
                result -> {
                    Log.i("BrandFragment",result.toString());
                    try {
                        JSONArray nameList = result
                                .getJSONArray("brand_list");// 获取JSONArray
                        int length = nameList.length();
                        for (int i = 0; i < length; i++) {// 遍历JSONArray
                            JSONObject oj = nameList.getJSONObject(i);
                            JSONArray imgList = oj
                                    .getJSONArray("brand_list");
                            int length2 = imgList.length();
                            List<Brand> relist = new ArrayList<Brand>();
                            for (int j = 0; j < length2; j++) {
                                JSONObject img = imgList.getJSONObject(j);
                                relist.add(new Brand(img.get("id")
                                        .toString(), img.get("name")
                                        .toString(), img.get("photo")
                                        .toString()));
                            }
                            list.add(new Pair<String, List<Brand>>(oj.getString("word").toString(), relist));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lsComposer
                            .setAdapter(adapter = new SectionComposerAdapter(
                                    list));
                    lsComposer.setPinnedHeaderView(LayoutInflater.from(
                            mActivity).inflate(
                            R.layout.item_composer_header, lsComposer,
                            false));
                    //品牌条目点击事件
                    lsComposer.setOnItemClickListener((parent, view, position, id) -> {
                                TextView g_id = (TextView) view.findViewById(R.id.brand_id);
                                TextView title = (TextView) view.findViewById(R.id.brand_name);
                                int gb_id = Integer.parseInt(g_id.getText().toString());
                                mActivity.go_goodslist("gb_id", gb_id + "", title.getText().toString());
                            });
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), null);
        mRequestQueue.add(request);
        return rootView;
    }

    class SectionComposerAdapter extends SosUniversalAdapter {
        List<Pair<String, List<Brand>>> all;

        public SectionComposerAdapter(List<Pair<String, List<Brand>>> list) {
            all = list;
        }

        public int getCount() {
            int res = 0;
            for (int i = 0; i < all.size(); i++) {
                res += all.get(i).second.size();
            }
            return res;
        }

        public Brand getItem(int position) {
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

        public View getAmazingView(int position, View convertView, ViewGroup parent) {
            View res = convertView;
            if (res == null)
                res = mActivity.getLayoutInflater().inflate(R.layout.item_composer, null);
            TextView id = (TextView) res.findViewById(R.id.brand_id);
            TextView name = (TextView) res.findViewById(R.id.brand_name);
            SimpleDraweeView img = (SimpleDraweeView) res.findViewById(R.id.brand_img);

            Brand composer = getItem(position);
            id.setText(composer.id);
            name.setText(composer.name);

            BaseActivity.displayImage(composer.url, img);
            return res;
        }

        public void configurePinnedHeader(View header, int position, int alpha) {
            TextView lSectionHeader = (TextView) header;
            lSectionHeader
                    .setText(getSections()[getSectionForPosition(position)]);
            lSectionHeader.setBackgroundResource(R.color.brandtop);
            lSectionHeader.setTextColor(getResources().getColor(
                    R.color.textdark));
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
