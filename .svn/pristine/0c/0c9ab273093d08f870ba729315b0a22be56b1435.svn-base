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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.SerializableMap;
import com.ewgvip.buyer.android.utils.BitmapCache;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.ImageItem;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author zhangzhiyong  .
 * @Description
 * @date 2016/1/28
 */
public class OrderEvaluateAddFragment extends Fragment {
    View rootView;
    List idList;
    ArrayList photoList;
    ArrayList urlList;
    MyAdapter myAdapter;
    GridView gview_item_order_evaluate;
    BitmapCache cache;
    BaseActivity mActivity;
    EditText et_item_order_evaluate;
    Map bundleMap;
    String acc_id = "";
    Bundle bundle;
    SimpleDraweeView iv_item_order_evaluate;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_evaluate_add, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("追加晒单");//设置标题
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
        iv_item_order_evaluate = (SimpleDraweeView) rootView.findViewById(R.id.iv_item_order_evaluate);
        TextView tv_item_order_evaluate_title = (TextView) rootView.findViewById(R.id.tv_item_order_evaluate_title);
        bundle = getArguments();
        SerializableMap serializableMap = (SerializableMap) bundle.get("serializableMap");
        bundleMap = serializableMap.getMap();
        tv_item_order_evaluate_title.setText(bundleMap.get("goods_name") + "");
        BaseActivity.displayImage(bundleMap.get("goods_main_photo") + "", iv_item_order_evaluate);
        et_item_order_evaluate = (EditText) rootView.findViewById(R.id.et_item_order_evaluate);
        idList = new ArrayList();
        photoList = new ArrayList();
        urlList = new ArrayList();
        cache = new BitmapCache();
        myAdapter = new MyAdapter();
        rootView.findViewById(R.id.btn_order_evaluate_commit_btn).setOnClickListener(view -> {
            if ((et_item_order_evaluate.getText() + "").equals("")) {
                Toast.makeText(mActivity, "请输入追加评语", Toast.LENGTH_SHORT).show();
            } else {
                mActivity.showProcessDialog();
                acc_id = "";
                Map map1 = mActivity.getParaMap();
                map1.put("evaluate_id", bundleMap.get("evaluate_id"));
                map1.put("goods_id", bundleMap.get("goods_id"));
                map1.put("evaluate_info_" + bundleMap.get("goods_id"), et_item_order_evaluate.getText() + "");
                for (int i = 0; i < photoList.size(); i++) {
                    acc_id += idList.get(i) + ",";
                }
                if (acc_id.equals("")) {
                    map1.put("evaluate_photos_" + bundleMap.get("goods_id"), acc_id);
                } else {
                    map1.put("evaluate_photos_" + bundleMap.get("goods_id"), acc_id.substring(0, acc_id.length() - 1));
                }
                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                Request request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/evaluate_add.htm",
                        response -> {
                            Toast.makeText(mActivity, "追加成功", Toast.LENGTH_SHORT).show();
                            if (getTargetFragment() == null)
                                return;
                            Bundle b = new Bundle();
                            b.putInt("position", bundle.getInt("position"));
                            b.putInt("current", 1);
                            b.putInt("type", 0);
                            b.putString("evaluate_info", et_item_order_evaluate.getText() + "");
                            b.putStringArrayList("urlList", urlList);
                            Intent intent = new Intent();
                            intent.putExtras(b);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                            getFragmentManager().popBackStack();
                            mActivity.hideProcessDialog(0);
                        }, error -> {
                            Toast.makeText(mActivity, "追加失败", Toast.LENGTH_SHORT).show();
                            mActivity.hideProcessDialog(1);
                        }, map1);

                mRequestQueue.add(request);
            }
        });
        rootView.findViewById(R.id.btn_item_order_evaluate_photo).setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                BaseActivity mActivity1 = (BaseActivity) getActivity();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", 0);
                bundle1.putInt("total", photoList.size());
                bundle1.putInt("count", 4);
                mActivity1.go_order_evaluate_photo(bundle1, OrderEvaluateAddFragment.this);
            }
        });
        gview_item_order_evaluate = (GridView) rootView.findViewById(R.id.gview_item_order_evaluate);
        gview_item_order_evaluate.setAdapter(myAdapter);
        photoRefresh();
        return rootView;
    }

    public void photoRefresh() {
        if (photoList.size() == 0) {//无图片时隐藏
            gview_item_order_evaluate.setVisibility(View.GONE);
        } else {
            gview_item_order_evaluate.setVisibility(View.VISIBLE);
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        ArrayList<String> list = bundle.getStringArrayList("idList");
        String icc_id = "";
        StringBuffer stringBuffer = new StringBuffer(icc_id);
        for (int j = 0; j < list.size(); j++) {
            idList.add(list.get(j));
        }
        ArrayList<ImageItem> dataList = (ArrayList) bundle.getSerializable("dataList");
        for (int i = 0; i < bundle.getStringArrayList("list2").size(); i++) {
            for (int j = 0; j < dataList.size(); j++) {
                if (dataList.get(j).getImageId().equals(bundle.getStringArrayList("list2").get(i))) {
                    photoList.add(dataList.get(j));
                }
            }
        }
        ArrayList<String> list1 = (ArrayList) bundle.getStringArrayList("updateURLList");
        for (int i = 0; i < list1.size(); i++) {
            urlList.add(list1.get(i));
        }
        photoRefresh();
        super.onActivityResult(requestCode, resultCode, data);
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (photoList != null) {
                return photoList.size();
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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_order_evaluate_add_item, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item_order_evaluate_photo_item = (ImageView) view.findViewById(R.id.iv_item_order_evaluate_photo_item);
                viewHolder.tv_item_order_evaluate_photo_item_del = (TextView) view.findViewById(R.id.tv_item_order_evaluate_photo_item_del);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            ImageItem item = (ImageItem) photoList.get(i);
            viewHolder.iv_item_order_evaluate_photo_item.setTag(item.imagePath);
            cache.displayBmp(viewHolder.iv_item_order_evaluate_photo_item, item.thumbnailPath, item.imagePath, callback);
            viewHolder.tv_item_order_evaluate_photo_item_del.setOnClickListener(view1 -> {
                idList.remove(i);
                photoList.remove(i);
                urlList.remove(i);
                photoRefresh();
            });
            return view;
        }

        class ViewHolder {
            ImageView iv_item_order_evaluate_photo_item;
            TextView tv_item_order_evaluate_photo_item_del;
        }
    }
}
