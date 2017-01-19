package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.BitmapCache;
import com.ewgvip.buyer.android.utils.DensityUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.ImageItem;
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
import java.util.Map;

/*
    订单评价页
 */
public class OrderEvaluateFragment extends Fragment {
    public static int NUM = 1;//requestcode常量
    private ArrayList<String> name_list;//订单商品名称集合
    private ArrayList<String> photo_list;//订单商品图片集合
    private ArrayList<String> goods_id_list;//订单商品id集合
    private MyAdapter myAdapter;
    private Map<Integer, ArrayList<ImageItem>> map;//不同item晒单图片的map集合
    private BaseActivity mActivity;
    private String user_id;
    private String order_id;
    private String token;
    private Map commitMap = new HashMap();//提交评价时map集合
    private View view;
    private CheckBox cb_noname;

    @Override
    public void onDetach() {
        super.onDetach();
        this.name_list = null;
        this.photo_list = null;
        this.goods_id_list = null;
        this.myAdapter = null;
        this.map = null;
        this.user_id = null;
        this.order_id = null;
        this.token = null;
        this.commitMap = null;
        this.view = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_evaluate, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("订单评价");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用
        cb_noname = (CheckBox) view.findViewById(R.id.cb_noname);
        mActivity.showProcessDialog();
        name_list = new ArrayList<String>();
        photo_list = new ArrayList<String>();
        goods_id_list = new ArrayList<String>();
        map = new HashMap<Integer, ArrayList<ImageItem>>();
        order_id = getArguments().getString("order_id") + "";
        SharedPreferences preferences = mActivity.getSharedPreferences("user",
                Context.MODE_PRIVATE);
        user_id = preferences.getString("user_id", "");
        token = preferences.getString("token", "");
        Map paramap = new HashMap<String, String>();
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("order_id", order_id);
        commitMap.put("user_id", user_id);
        commitMap.put("token", token);
        commitMap.put("order_id", order_id);
        //获取
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_order_evaluate_query.htm",
                result -> {
                    try {
                        JSONArray jsonArray = result.getJSONArray("goods_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            commitMap.put("evaluate_info_" + jsonObject.getString("goods_id"), "");//评语
                            commitMap.put("evaluate_photos_" + jsonObject.getString("goods_id"), "");//晒单图片
                            commitMap.put("evaluate_buyer_val" + jsonObject.getString("goods_id"), 1 + "");//评价1好评0中评-1差评
                            commitMap.put("description_evaluate" + jsonObject.getString("goods_id"), 5 + "");
                            commitMap.put("service_evaluate" + jsonObject.getString("goods_id"), 5 + "");
                            commitMap.put("ship_evaluate" + jsonObject.getString("goods_id"), 5 + "");
                            goods_id_list.add(jsonObject.getString("goods_id"));
                            name_list.add(jsonObject.getString("goods_name"));
                            photo_list.add(jsonObject.getString("goods_mainphoto_path"));
                            ArrayList<ImageItem> list = new ArrayList<ImageItem>();
                            map.put(i, list);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myAdapter = new MyAdapter();
                    ListView listView = (ListView) view.findViewById(R.id.lv_order_evaluate);
                    listView.setDividerHeight(DensityUtil.px2dip(mActivity, 0));
                    listView.setAdapter(myAdapter);
                    mActivity.hideProcessDialog(0);

                    Button btn_order_evaluate_commit = (Button) view.findViewById(R.id.btn_order_evaluate_commit_btn);
                    btn_order_evaluate_commit.setOnClickListener(view1 -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            boolean flag = true;
                            for (int i = 0; i < goods_id_list.size(); i++) {
                                if (commitMap.get("evaluate_info_" + goods_id_list.get(i)).equals("")) {
                                    commitMap.put("evaluate_info_" + goods_id_list.get(i), "这个人很懒，什么也没留下！");
                                    flag = true;
                                }
                            }
                            if (cb_noname.isChecked()) {
                                commitMap.put("checkState", "0");
                            } else {
                                commitMap.put("checkState", "");
                            }
                            if (flag) {
                                mActivity.showProcessDialog();
                                RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                Request<JSONObject> request1 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_order_evaluate.htm",
                                        result1 -> {
                                            try {
                                                Log.e("commitMap:", commitMap.toString());
                                                int code = result1.getInt("code");
                                                mActivity.hideProcessDialog(0);
                                                if (code == 100) {
                                                    Toast.makeText(mActivity,
                                                            "评价成功", Toast.LENGTH_SHORT)
                                                            .show();
                                                    //提交成功后返回到评价搜索页面
                                                    if (getTargetFragment() == null)
                                                        return;
                                                    Intent i = new Intent();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("current", 0);
                                                    i.putExtras(bundle);
                                                    getTargetFragment().onActivityResult(getTargetRequestCode(), OrderListFragment.NUM, i);
                                                    getFragmentManager().popBackStack();
                                                    OrderAllTabFragment.REFRESH = true;
                                                } else {
                                                    Toast.makeText(mActivity,
                                                            "评价失败，请稍后重试!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }, error -> Toast.makeText(mActivity, "网络异常，评价失败，请稍后重试!", Toast.LENGTH_SHORT).show(), commitMap);
                                mRequestQueue1.add(request1);
                            }
                        }
                    });
                }, error -> {
            Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
            mActivity.hideProcessDialog(1);
        }, paramap);
        mRequestQueue.add(request);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NUM) {//获取返回图片路径并加到对应map集合中
            Bundle bundle = data.getExtras();
            ArrayList<String> idList = bundle.getStringArrayList("idList");
            String icc_id = "";
            StringBuffer stringBuffer = new StringBuffer(icc_id);
            for (int j = 0; j < idList.size(); j++) {
                if (j == 0) {
                    stringBuffer.append(idList.get(j));
                } else {
                    stringBuffer.append("," + idList.get(j));
                }
            }
            if (commitMap.get("evaluate_photos_" + goods_id_list.get(bundle.getInt("position"))).equals("")) {
                commitMap.put("evaluate_photos_" + goods_id_list.get(bundle.getInt("position")), stringBuffer.toString());
            } else {
                commitMap.put("evaluate_photos_" + goods_id_list.get(bundle.getInt("position")), commitMap.get("evaluate_photos_" + goods_id_list.get(bundle.getInt("position"))) + "," + stringBuffer.toString());
            }
            ArrayList<ImageItem> dataList = (ArrayList) bundle.getSerializable("dataList");
            for (int i = 0; i < bundle.getStringArrayList("list2").size(); i++) {
                for (int j = 0; j < dataList.size(); j++) {
                    if (dataList.get(j).getImageId().equals(bundle.getStringArrayList("list2").get(i))) {
                        map.get(bundle.getInt("position")).add(dataList.get(j));
                    }
                }
            }
        }
        myAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //用于加载订单信息
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return goods_id_list.size();
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
            final ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_order_evaluate, null);
                viewHolder = new ViewHolder();
                viewHolder.btn_item_order_evaluate_photo = (Button) view.findViewById(R.id.btn_item_order_evaluate_photo);
                viewHolder.gview_item_order_evaluate = (GridView) view.findViewById(R.id.gview_item_order_evaluate);
                viewHolder.iv_item_order_evaluate = (SimpleDraweeView) view.findViewById(R.id.iv_item_order_evaluate);
                viewHolder.tv_item_order_evaluate_title = (TextView) view.findViewById(R.id.tv_item_order_evaluate_title);
                viewHolder.rbar_item_order_evaluate_service = (RatingBar) view.findViewById(R.id.rbar_item_order_evaluate_service);
                viewHolder.rbar_item_order_evaluate_descript = (RatingBar) view.findViewById(R.id.rbar_item_order_evaluate_descript);
                viewHolder.rbar_item_order_evaluate_speed = (RatingBar) view.findViewById(R.id.rbar_item_order_evaluate_speed);
                viewHolder.et_item_order_evaluate = (EditText) view.findViewById(R.id.et_item_order_evaluate);
                viewHolder.rgroup_item_order_evaluate = (RadioGroup) view.findViewById(R.id.rgroup_item_order_evaluate);
                viewHolder.rbtn_item_order_evaluate_well = (RadioButton) view.findViewById(R.id.rbtn_item_order_evaluate_well);
                viewHolder.rbtn_item_order_evaluate_middle = (RadioButton) view.findViewById(R.id.rbtn_item_order_evaluate_middle);
                viewHolder.rbtn_item_order_evaluate_bad = (RadioButton) view.findViewById(R.id.rbtn_item_order_evaluate_bad);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if (map.get(i).size() == 0) {//无图片时隐藏
                viewHolder.gview_item_order_evaluate.setVisibility(View.GONE);
            } else {
                viewHolder.gview_item_order_evaluate.setVisibility(View.VISIBLE);
                viewHolder.gview_item_order_evaluate.setAdapter(new OrderEvaluatePhotoAdapter(getActivity(), map.get(i), i));
            }
            if (map.get(i).size() < 4) {//图片不能超过4个
                viewHolder.btn_item_order_evaluate_photo.setOnClickListener(view1 -> {//跳转到相册图片加载页面
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        BaseActivity mActivity1 = (BaseActivity) getActivity();
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", i);
                        bundle.putInt("total", map.get(i).size());
                        bundle.putInt("count", 4);
                        mActivity1.go_order_evaluate_photo(bundle, OrderEvaluateFragment.this);
                    }
                });
            }
            if (viewHolder.rbtn_item_order_evaluate_well.isChecked()) {
                viewHolder.rbtn_item_order_evaluate_well.setAlpha((float) 1);
                viewHolder.rbtn_item_order_evaluate_middle.setAlpha((float) 0.5);
                viewHolder.rbtn_item_order_evaluate_bad.setAlpha((float) 0.5);
            } else if (viewHolder.rbtn_item_order_evaluate_middle.isChecked()) {
                viewHolder.rbtn_item_order_evaluate_well.setAlpha((float) 0.5);
                viewHolder.rbtn_item_order_evaluate_middle.setAlpha((float) 1);
                viewHolder.rbtn_item_order_evaluate_bad.setAlpha((float) 0.5);
            } else {
                viewHolder.rbtn_item_order_evaluate_well.setAlpha((float) 0.5);
                viewHolder.rbtn_item_order_evaluate_middle.setAlpha((float) 0.5);
                viewHolder.rbtn_item_order_evaluate_bad.setAlpha((float) 1);
                commitMap.put("evaluate_buyer_val" + goods_id_list.get(i), -1 + "");
            }
            viewHolder.rgroup_item_order_evaluate.setOnCheckedChangeListener((radioGroup, id) -> {
                switch (id) {
                    case R.id.rbtn_item_order_evaluate_well:
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            viewHolder.rbtn_item_order_evaluate_well.setAlpha((float) 1);
                            viewHolder.rbtn_item_order_evaluate_middle.setAlpha((float) 0.5);
                            viewHolder.rbtn_item_order_evaluate_bad.setAlpha((float) 0.5);
                            commitMap.put("evaluate_buyer_val" + goods_id_list.get(i), 1 + "");
                        }
                        break;
                    case R.id.rbtn_item_order_evaluate_middle:
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            viewHolder.rbtn_item_order_evaluate_well.setAlpha((float) 0.5);
                            viewHolder.rbtn_item_order_evaluate_middle.setAlpha((float) 1);
                            viewHolder.rbtn_item_order_evaluate_bad.setAlpha((float) 0.5);
                            commitMap.put("evaluate_buyer_val" + goods_id_list.get(i), 0 + "");
                        }
                        break;
                    case R.id.rbtn_item_order_evaluate_bad:
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            viewHolder.rbtn_item_order_evaluate_well.setAlpha((float) 0.5);
                            viewHolder.rbtn_item_order_evaluate_middle.setAlpha((float) 0.5);
                            viewHolder.rbtn_item_order_evaluate_bad.setAlpha((float) 1);
                            commitMap.put("evaluate_buyer_val" + goods_id_list.get(i), -1 + "");
                        }
                        break;
                }
            });
            viewHolder.rbar_item_order_evaluate_service.setOnRatingBarChangeListener((ratingBar, v, b) -> commitMap.put("service_evaluate" + goods_id_list.get(i), (int) v + ""));
            viewHolder.rbar_item_order_evaluate_descript.setOnRatingBarChangeListener((ratingBar, v, b) -> commitMap.put("description_evaluate" + goods_id_list.get(i), (int) v + ""));
            viewHolder.rbar_item_order_evaluate_speed.setOnRatingBarChangeListener((ratingBar, v, b) -> commitMap.put("ship_evaluate" + goods_id_list.get(i), (int) v + ""));
            viewHolder.et_item_order_evaluate.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    commitMap.put("evaluate_info_" + goods_id_list.get(i), s.toString() + "");
                }
            });
            BaseActivity.displayImage(photo_list.get(i), viewHolder.iv_item_order_evaluate);
            viewHolder.tv_item_order_evaluate_title.setText(name_list.get(i));
            return view;
        }

        public class ViewHolder {
            Button btn_item_order_evaluate_photo;
            GridView gview_item_order_evaluate;
            SimpleDraweeView iv_item_order_evaluate;
            TextView tv_item_order_evaluate_title;
            RatingBar rbar_item_order_evaluate_service;
            RatingBar rbar_item_order_evaluate_descript;
            RatingBar rbar_item_order_evaluate_speed;
            EditText et_item_order_evaluate;
            RadioGroup rgroup_item_order_evaluate;
            RadioButton rbtn_item_order_evaluate_well;
            RadioButton rbtn_item_order_evaluate_middle;
            RadioButton rbtn_item_order_evaluate_bad;
        }
    }

    class OrderEvaluatePhotoAdapter extends BaseAdapter {
        ArrayList<ImageItem> list;//晒单图片路径集合
        Context context;//上下文
        int position;
        BitmapCache cache;
        private BitmapCache.ImageCallback callback = (imageView, bitmap, params) -> {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals(imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                } else {
                }
            } else {
            }
        };

        public OrderEvaluatePhotoAdapter(Context context, ArrayList<ImageItem> list, int position) {
            this.list = list;
            this.context = context;
            this.position = position;
            cache = new BitmapCache();
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
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
                view = LayoutInflater.from(context).inflate(R.layout.item_order_evaluate_edit_item, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item_order_evaluate_edit = (SimpleDraweeView) view.findViewById(R.id.iv_item_order_evaluate_edit);
                viewHolder.tv_item_order_evaluate_photo_item_del = (TextView) view.findViewById(R.id.tv_item_order_evaluate_photo_item_del);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            ImageItem item = list.get(i);
            viewHolder.iv_item_order_evaluate_edit.setTag(item.imagePath);
            cache.displayBmp(viewHolder.iv_item_order_evaluate_edit, item.thumbnailPath, item.imagePath, callback);
            viewHolder.tv_item_order_evaluate_photo_item_del.setOnClickListener(view1 -> {
                String[] strings = (commitMap.get("evaluate_photos_" + goods_id_list.get(position)) + "").split(",");
                String photo_id = "";
                for (int j = 0; j < strings.length; j++) {
                    if (j != i) {
                        photo_id += strings[j] + ",";
                    }
                }
                commitMap.put("evaluate_photos_" + goods_id_list.get(position), photo_id.equals("") ? "" : photo_id.substring(0, photo_id.length() - 1));
                list.remove(i);
                OrderEvaluatePhotoAdapter.this.notifyDataSetChanged();
                if (photo_id.equals("")) {
                    myAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }

        class ViewHolder {
            SimpleDraweeView iv_item_order_evaluate_edit;
            TextView tv_item_order_evaluate_photo_item_del;
        }
    }
}
