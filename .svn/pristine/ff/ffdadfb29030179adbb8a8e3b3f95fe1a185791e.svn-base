package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
public class OrderEvaluateEditFragment extends Fragment {
    View rootView;
    BaseActivity mActivity;
    TextView tv_order_evaluate_edit_name;
    SimpleDraweeView iv_order_evaluate_edit;
    EditText et_item_order_evaluate;
    GridView gview_order_evaluate_edit_photo;
    Map map;
    List idList;
    List photoList;
    PhotoAdapter photoAdapter;
    RatingBar rbar_item_order_evaluate_service;
    RatingBar rbar_item_order_evaluate_descript;
    RatingBar rbar_item_order_evaluate_speed;
    Map commitMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        rootView = inflater.inflate(R.layout.fragment_order_evaluate_edit, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("评价修改");//设置标题
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
        tv_order_evaluate_edit_name = (TextView) rootView.findViewById(R.id.tv_order_evaluate_edit_name);
        iv_order_evaluate_edit = (SimpleDraweeView) rootView.findViewById(R.id.iv_order_evaluate_edit);
        et_item_order_evaluate = (EditText) rootView.findViewById(R.id.et_item_order_evaluate);
        gview_order_evaluate_edit_photo = (GridView) rootView.findViewById(R.id.gview_order_evaluate_edit_photo);
        rbar_item_order_evaluate_service = (RatingBar) rootView.findViewById(R.id.rbar_item_order_evaluate_service);
        rbar_item_order_evaluate_descript = (RatingBar) rootView.findViewById(R.id.rbar_item_order_evaluate_descript);
        rbar_item_order_evaluate_speed = (RatingBar) rootView.findViewById(R.id.rbar_item_order_evaluate_speed);
        commitMap = mActivity.getParaMap();
        idList = new ArrayList();
        Bundle bundle = getArguments();
        SerializableMap serializableMap = (SerializableMap) bundle.getSerializable("serializableMap");
        map = serializableMap.getMap();
        tv_order_evaluate_edit_name.setText(map.get("goods_name") + "");
        BaseActivity.displayImage(map.get("goods_main_photo") + "", iv_order_evaluate_edit);
        getData();
        rootView.findViewById(R.id.btn_order_evaluate_commit_btn).setOnClickListener(view -> {
            if ((et_item_order_evaluate.getText() + "").equals("")) {
                Toast.makeText(mActivity, "请输入评语！", Toast.LENGTH_SHORT).show();
            } else {
                commitMap.put("evaluate_info_" + map.get("goods_id"), et_item_order_evaluate.getText() + "");
                String acc_id = "";
                if (idList.size() != 0) {
                    for (int i = 0; i < idList.size(); i++) {
                        acc_id += idList.get(i) + ",";
                    }
                    commitMap.put("evaluate_photos_" + map.get("goods_id"), acc_id.substring(0, acc_id.length() - 1) + "");
                } else {
                    commitMap.put("evaluate_photos_" + map.get("goods_id"), acc_id);
                }
                RequestQueue requestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                Request request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/evaluate_modify.htm",
                        response -> {
                            try {
                                if ((response.get("result") + "").equals("SUCCESS")) {
                                    Toast.makeText(mActivity, "修改成功", Toast.LENGTH_SHORT).show();
                                    if (getTargetFragment() == null)
                                        return;
                                    Bundle b = new Bundle();
                                    b.putInt("position", getArguments().getInt("position"));
                                    b.putInt("current", 1);
                                    b.putInt("type", 1);
                                    Intent intent = new Intent();
                                    intent.putExtras(b);
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                                    getFragmentManager().popBackStack();
                                } else {
                                    Toast.makeText(mActivity, "修改失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                            }
                        }, error -> Toast.makeText(mActivity, "网络超时", Toast.LENGTH_SHORT).show(), commitMap);
                requestQueue.add(request);
            }
        });
        commitMap.put("goods_id", map.get("goods_id") + "");
        commitMap.put("evaluate_id", map.get("evaluate_id") + "");
        commitMap.put("evaluate_buyer_val" + map.get("goods_id"), "1");

        rbar_item_order_evaluate_service.setOnRatingBarChangeListener((ratingBar, v, b) -> commitMap.put("service_evaluate" + map.get("goods_id") + "", (int)v + ""));
        rbar_item_order_evaluate_descript.setOnRatingBarChangeListener((ratingBar, v, b) -> commitMap.put("description_evaluate" + map.get("goods_id") + "", (int)v + ""));
        rbar_item_order_evaluate_speed.setOnRatingBarChangeListener((ratingBar, v, b) -> commitMap.put("ship_evaluate" + map.get("goods_id") + "", (int)v + ""));
        rootView.findViewById(R.id.btn_item_order_evaluate_photo).setOnClickListener(view -> {
            Bundle bundle1 = new Bundle();
            bundle1.putInt("position", 0);
            bundle1.putInt("total", photoList.size());
            bundle1.putInt("count", 4);
            mActivity.go_order_evaluate_photo(bundle1, OrderEvaluateEditFragment.this);
        });
        return rootView;
    }

    public void getData() {
        Map map1 = mActivity.getParaMap();
        map1.put("evaluate_id", map.get("evaluate_id"));
        RequestQueue requestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/evaluate_detail.htm",
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        et_item_order_evaluate.setText(jsonObject.get("content") + "");
                        et_item_order_evaluate.setSelection((et_item_order_evaluate.getText() + "").length());
                        photoList = new ArrayList();
                        photoAdapter = new PhotoAdapter();
                        if (jsonObject.has("evaluate_photos")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("evaluate_photos");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                photoList.add(jsonArray.get(i) + "");
                            }
                            if (photoList.size() != 0) {
                                gview_order_evaluate_edit_photo.setVisibility(View.VISIBLE);
                                String acc_id = jsonObject.get("evaluate_photos_id") + "";
                                String[] strings = acc_id.split(",");
                                for (int i = 0; i < strings.length; i++) {
                                    idList.add(strings[i]);
                                }
                                gview_order_evaluate_edit_photo.setAdapter(photoAdapter);
                            } else {
                                gview_order_evaluate_edit_photo.setVisibility(View.GONE);
                            }
                        } else {
                        }
                        commitMap.put("service_evaluate" + map.get("goods_id") + "", jsonObject.get("service_evaluate") + "");
                        commitMap.put("description_evaluate" + map.get("goods_id") + "", jsonObject.get("description_evaluate") + "");
                        commitMap.put("ship_evaluate" + map.get("goods_id") + "", jsonObject.get("ship_evaluate") + "");
                        rbar_item_order_evaluate_service.setRating(Float.valueOf(jsonObject.get("service_evaluate") + ""));
                        rbar_item_order_evaluate_descript.setRating(Float.valueOf(jsonObject.get("description_evaluate") + ""));
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

    class PhotoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return photoList.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_order_evaluate_edit_item, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item_order_evaluate_edit = (SimpleDraweeView) view.findViewById(R.id.iv_item_order_evaluate_edit);
                viewHolder.tv_item_order_evaluate_photo_item_del = (TextView) view.findViewById(R.id.tv_item_order_evaluate_photo_item_del);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BaseActivity.displayImage(photoList.get(i) + "", viewHolder.iv_item_order_evaluate_edit);
            viewHolder.tv_item_order_evaluate_photo_item_del.setOnClickListener(view1 -> {
                photoList.remove(i);
                idList.remove(i);
                photoAdapter.notifyDataSetChanged();
                if (photoList.size() == 0) {
                    gview_order_evaluate_edit_photo.setVisibility(View.GONE);
                }
            });
            return view;
        }

        class ViewHolder {
            SimpleDraweeView iv_item_order_evaluate_edit;
            TextView tv_item_order_evaluate_photo_item_del;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        ArrayList<String> list = bundle.getStringArrayList("idList");
        String icc_id = "";
        for (int j = 0; j < list.size(); j++) {
            idList.add(list.get(j));
        }
        ArrayList<String> list1 = (ArrayList) bundle.getStringArrayList("updateURLList");
        for (int i = 0; i < list1.size(); i++) {
            photoList.add(list1.get(i));
        }
        gview_order_evaluate_edit_photo.setVisibility(View.VISIBLE);
        photoAdapter = new PhotoAdapter();
        gview_order_evaluate_edit_photo.setAdapter(photoAdapter);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
