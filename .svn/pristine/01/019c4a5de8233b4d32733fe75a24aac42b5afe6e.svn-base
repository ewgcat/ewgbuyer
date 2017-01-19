package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.SerializableMap;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangzhiyong  .
 * @Description
 * @date 2016/1/29
 */
public class OrderEvaluateDetailsFragment extends Fragment {
    private View rootView;
    private BaseActivity mActivity;
    private LinearLayout layout_order_evaluate_detail_value;
    private LinearLayout layout_order_evaluate_detail_info;
    private LinearLayout layout_order_evaluate_detail_info_add;
    private LinearLayout layout_order_evaluate_detail_reply;
    private LinearLayout layout_order_evaluate_detail_photo;
    private LinearLayout layout_order_evaluate_detail_photo_add;
    private TextView tv_order_evaluate_detail_name;
    private TextView tv_order_evaluate_detail_value;
    private TextView tv_order_evaluate_detail_info;
    private TextView tv_order_evaluate_detail_info_add;
    private TextView tv_order_evaluate_detail_reply;
    private GridView gview_order_evaluate_detail_photo;
    private GridView gview_order_evaluate_detail_photo_add;
    private SimpleDraweeView iv_order_evaluate_detail;
    private Map map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        rootView = inflater.inflate(R.layout.fragment_order_evaluate_detail, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.requestFocus();
        toolbar.setTitle("评价详情");//设置标题
        mActivity = (BaseActivity) getActivity();
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });
        setHasOptionsMenu(false);//设置菜单可用

        initView();
        Bundle bundle = getArguments();
        SerializableMap serializableMap = (SerializableMap) bundle.getSerializable("serializableMap");
        map = serializableMap.getMap();
        tv_order_evaluate_detail_name.setText(map.get("goods_name") + "");
        BaseActivity.displayImage(map.get("goods_main_photo") + "", iv_order_evaluate_detail);
        getData();

        return rootView;
    }

    public void getData() {

        Map map1 = mActivity.getParaMap();
        map1.put("evaluate_id", getArguments().getString("evaluate_id"));
        RequestQueue requestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/evaluate_detail.htm",
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        if ((jsonObject.get("evaluate_buyer_val") + "").equals("1")) {
                            tv_order_evaluate_detail_value.setText("评价：好评");
                        } else if ((jsonObject.get("evaluate_buyer_val") + "").equals("0")) {
                            tv_order_evaluate_detail_value.setText("评价：中评");
                        } else if ((jsonObject.get("evaluate_buyer_val") + "").equals("-1")) {
                            tv_order_evaluate_detail_value.setText("评价：差评");
                        }
                        if ((jsonObject.get("content") + "").equals("")) {
                            layout_order_evaluate_detail_info.setVisibility(View.GONE);
                        } else {
                            layout_order_evaluate_detail_info.setVisibility(View.VISIBLE);
                            tv_order_evaluate_detail_info.setText("评语：" + jsonObject.get("content") + "");
                        }
                        if (jsonObject.has("evaluate_photos")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("evaluate_photos");
                            List photoList = new ArrayList();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                photoList.add(jsonArray.get(i) + "");
                            }
                            if (photoList.size() != 0) {
                                layout_order_evaluate_detail_photo.setVisibility(View.VISIBLE);
                                PhotoAdapter photoAdapter = new PhotoAdapter(photoList);
                                gview_order_evaluate_detail_photo.setAdapter(photoAdapter);
                            } else {
                                layout_order_evaluate_detail_photo.setVisibility(View.GONE);
                            }
                        } else {
                            layout_order_evaluate_detail_photo.setVisibility(View.GONE);
                        }
                        if (jsonObject.has("add_evaluate_photos")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("add_evaluate_photos");
                            List photoAddList = new ArrayList();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                photoAddList.add(jsonArray.get(i) + "");
                            }
                            if (photoAddList.size() != 0) {
                                layout_order_evaluate_detail_photo_add.setVisibility(View.VISIBLE);
                                PhotoAddAdapter photoAddAdapter = new PhotoAddAdapter(photoAddList);
                                gview_order_evaluate_detail_photo_add.setAdapter(photoAddAdapter);
                            } else {
                                layout_order_evaluate_detail_photo_add.setVisibility(View.GONE);
                            }
                        } else {
                            layout_order_evaluate_detail_photo_add.setVisibility(View.GONE);
                        }
                        if (jsonObject.has("add_content")) {
                            layout_order_evaluate_detail_info_add.setVisibility(View.VISIBLE);
                            tv_order_evaluate_detail_info_add.setText("追加评语：" + jsonObject.get("add_content") + "");
                        } else {
                            layout_order_evaluate_detail_info_add.setVisibility(View.GONE);
                        }
                        RatingBar rbar_item_order_evaluate_service = (RatingBar) rootView.findViewById(R.id.rbar_item_order_evaluate_service);
                        rbar_item_order_evaluate_service.setRating(Float.valueOf(jsonObject.get("service_evaluate") + ""));
                        RatingBar rbar_item_order_evaluate_descript = (RatingBar) rootView.findViewById(R.id.rbar_item_order_evaluate_descript);
                        rbar_item_order_evaluate_descript.setRating(Float.valueOf(jsonObject.get("description_evaluate") + ""));
                        RatingBar rbar_item_order_evaluate_speed = (RatingBar) rootView.findViewById(R.id.rbar_item_order_evaluate_speed);
                        rbar_item_order_evaluate_speed.setRating(Float.valueOf(jsonObject.get("ship_evaluate") + ""));
                    } catch (Exception e) {
                    }
                    rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                    rootView.findViewById(R.id.scroll_view).setVisibility(View.VISIBLE);
                    mActivity.hideProcessDialog(0);
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.scroll_view).setVisibility(View.GONE);
                    rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            mActivity.showProcessDialog();
                            getData();
                        }
                    });
                }, map1);
        requestQueue.add(request);
    }


    public void initView() {
        layout_order_evaluate_detail_value = (LinearLayout) rootView.findViewById(R.id.layout_order_evaluate_detail_value);
        layout_order_evaluate_detail_info = (LinearLayout) rootView.findViewById(R.id.layout_order_evaluate_detail_info);
        layout_order_evaluate_detail_info_add = (LinearLayout) rootView.findViewById(R.id.layout_order_evaluate_detail_info_add);
        layout_order_evaluate_detail_reply = (LinearLayout) rootView.findViewById(R.id.layout_order_evaluate_detail_reply);
        layout_order_evaluate_detail_photo = (LinearLayout) rootView.findViewById(R.id.layout_order_evaluate_detail_photo);
        layout_order_evaluate_detail_photo_add = (LinearLayout) rootView.findViewById(R.id.layout_order_evaluate_detail_photo_add);
        tv_order_evaluate_detail_name = (TextView) rootView.findViewById(R.id.tv_order_evaluate_detail_name);
        tv_order_evaluate_detail_value = (TextView) rootView.findViewById(R.id.tv_order_evaluate_detail_value);
        tv_order_evaluate_detail_info = (TextView) rootView.findViewById(R.id.tv_order_evaluate_detail_info);
        tv_order_evaluate_detail_info_add = (TextView) rootView.findViewById(R.id.tv_order_evaluate_detail_info_add);
        tv_order_evaluate_detail_reply = (TextView) rootView.findViewById(R.id.tv_order_evaluate_detail_reply);
        iv_order_evaluate_detail = (SimpleDraweeView) rootView.findViewById(R.id.iv_order_evaluate_detail);
        gview_order_evaluate_detail_photo = (GridView) rootView.findViewById(R.id.gview_order_evaluate_detail_photo);
        gview_order_evaluate_detail_photo_add = (GridView) rootView.findViewById(R.id.gview_order_evaluate_detail_photo_add);
    }

    class PhotoAdapter extends BaseAdapter {
        List list;

        public PhotoAdapter(List list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_order_evaluate_detail, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item_order_evaluate_detail = (SimpleDraweeView) view.findViewById(R.id.iv_item_order_evaluate_detail);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BaseActivity.displayImage(list.get(i) + "", viewHolder.iv_item_order_evaluate_detail);
            return view;
        }

        class ViewHolder {
            SimpleDraweeView iv_item_order_evaluate_detail;
        }
    }

    class PhotoAddAdapter extends BaseAdapter {
        List list;

        public PhotoAddAdapter(List list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_order_evaluate_detail, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item_order_evaluate_detail = (SimpleDraweeView) view.findViewById(R.id.iv_item_order_evaluate_detail);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BaseActivity.displayImage(list.get(i) + "", viewHolder.iv_item_order_evaluate_detail);
            return view;
        }

        class ViewHolder {
            SimpleDraweeView iv_item_order_evaluate_detail;
        }
    }
}
