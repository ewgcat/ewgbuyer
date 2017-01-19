package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.dialog.PhotoDialog;
import com.ewgvip.buyer.android.utils.AlbumHelper;
import com.ewgvip.buyer.android.utils.Bimp;
import com.ewgvip.buyer.android.utils.BitmapCache;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.ImageBucket;
import com.ewgvip.buyer.android.utils.ImageItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/10.
 * 查看图片fragment
 */

public class PhotoUploadFragment extends Fragment {
    public static List<ImageBucket> contentList;//搜索图片结果信息集合
    private PopupWindow pop;
    private MyAdapter myAdapter = new MyAdapter();
    private View rootView;
    private ArrayList<ImageItem> dataList;//相册图片信息集合
    private ArrayList<ImageItem> dataList2;//相册图片信息集合
    private AlbumHelper helper;//用于遍历系统图片
    private ArrayList<String> idList;//相册图片信息集合
    private ArrayList<String> base64List;//相册图片信息集合
    private GridView gridView;
    private BitmapCache cache;
    private BaseActivity mActivity;
    private ArrayList<String> list2;//选中图片的position集合
    private ArrayList<Integer> list;//选中图片的position集合
    private int total = 0;//已经选中的图片数量
    private int count;
    private int current = 0;//用于多张异步上传的变量
    private TextView btn_order_evaluate_photo_complete;
    private TextView tv_order_evaluate_photo;
    private TextView tv_order_evaluate_photo_select;
    private RelativeLayout layout_order_evaluate_photo_back;
    private ArrayList<String> updateURLList;
    private ArrayList<ImageItem> returnImageList;


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

    public static PhotoUploadFragment getInstance() {
        PhotoUploadFragment fragment = new PhotoUploadFragment();
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.pop = null;
        this.myAdapter = null;
        this.rootView = null;
        this.dataList = null;
        this.helper = null;
        this.idList = null;
        this.gridView = null;
        this.cache = null;

        this.list = null;
        this.btn_order_evaluate_photo_complete = null;
        this.tv_order_evaluate_photo = null;
        this.tv_order_evaluate_photo_select = null;
        this.updateURLList = null;
        this.returnImageList = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photo_upload, null);
        mActivity = (BaseActivity) getActivity();
        total = getArguments().getInt("total");
        count = getArguments().getInt("count");
        list2 = new ArrayList<String>();
        list = new ArrayList<Integer>();
        idList = new ArrayList<String>();
        base64List = new ArrayList<String>();
        //图片选择返回（完成）
        btn_order_evaluate_photo_complete = (TextView) rootView.findViewById(R.id.btn_order_evaluate_photo_complete);
        tv_order_evaluate_photo = (TextView) rootView.findViewById(R.id.tv_order_evaluate_photo);
        tv_order_evaluate_photo_select = (TextView) rootView.findViewById(R.id.tv_order_evaluate_photo_select);
        //图片选择返回（左箭头）
        layout_order_evaluate_photo_back = (RelativeLayout) rootView.findViewById(R.id.layout_order_evaluate_photo_back);

        layout_order_evaluate_photo_back.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        tv_order_evaluate_photo_select.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                showDialog();
            }
        });
        tv_order_evaluate_photo.setText(total + "/" + count);
        returnImageList = new ArrayList<ImageItem>();
        cache = new BitmapCache();
        gridView = (GridView) rootView.findViewById(R.id.gview_order_evaluate_photo);
        helper = AlbumHelper.getHelper();
        helper.init(getActivity().getApplicationContext());
        contentList = helper.getImagesBucketList(false);
        if (contentList.size() == 0) {
            rootView.findViewById(R.id.layout_photo_upload).setVisibility(View.GONE);
            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.nodata_refresh).setVisibility(View.GONE);

        } else {
            dataList = new ArrayList<ImageItem>();
            dataList2 = new ArrayList<ImageItem>();
            for (int i = 0; i < contentList.size(); i++) {
                dataList.addAll(contentList.get(i).imageList);
                dataList2.addAll(contentList.get(i).imageList);
            }
            for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).isSelected = false;
            }
            gridView.setAdapter(myAdapter);

            btn_order_evaluate_photo_complete.setOnClickListener(view -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    if (list2.size() != 0) {
                        dataList.clear();
                        for (int i = 0; i < contentList.size(); i++) {
                            dataList.addAll(contentList.get(i).imageList);
                        }
                        mActivity.showProcessDialog();
                        new MyTask().execute(list2);
                    }
                }
            });
            updateURLList = new ArrayList<String>();

        }
        return rootView;
    }

    public void showDialog() {
        final PhotoDialog photoDialog = new PhotoDialog(mActivity, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_photo_popup, null);
        ListView listView = (ListView) view.findViewById(R.id.lview_item_photo_popup);
        MyPopupAdapter myPopupAdapter = new MyPopupAdapter();
        listView.setAdapter(myPopupAdapter);
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                dataList.clear();
                dataList.addAll(contentList.get(i).imageList);
                myAdapter.notifyDataSetChanged();
                photoDialog.dismiss();
            }
        });
        photoDialog.setContentView(view);
        photoDialog.show();
        setListViewHeightBasedOnChildren(listView);
    }

    //测量listview高度
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter.getCount() < 5) {
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(0, 0);
            params.height = listItem.getMeasuredHeight() * listAdapter.getCount() + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        } else {
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(0, 0);
            params.height = listItem.getMeasuredHeight() * 5 + listView.getDividerHeight() * 4;
        }
        listView.setLayoutParams(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class MyPopupAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contentList.size();
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
            Holder holder = null;
            if (view == null) {
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_photo_popup_item, null);
                holder = new Holder();
                holder.tv_item_photo_popup_item_name = (TextView) view.findViewById(R.id.tv_item_photo_popup_item_name);
                holder.tv_item_photo_popup_item_num = (TextView) view.findViewById(R.id.tv_item_photo_popup_item_num);
                holder.iv_item_photo_popup_item = (ImageView) view.findViewById(R.id.iv_item_photo_popup_item);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            ImageItem imageItem = contentList.get(i).imageList.get(0);
            holder.tv_item_photo_popup_item_name.setText(contentList.get(i).bucketName);
            holder.tv_item_photo_popup_item_num.setText(contentList.get(i).imageList.size() + "张");
            cache.displayBmp(holder.iv_item_photo_popup_item, imageItem.thumbnailPath, imageItem.imagePath, callback);
            return view;
        }

        class Holder {
            TextView tv_item_photo_popup_item_name;
            TextView tv_item_photo_popup_item_num;
            ImageView iv_item_photo_popup_item;
        }
    }

    class MyTask extends AsyncTask<Object, Void, Integer> {
        @Override
        protected Integer doInBackground(Object... integers) {
            ArrayList<String> arrayList = (ArrayList<String>) integers[0];
            while (current < arrayList.size()) {//异步上传图片
                try {
                    Map map = new HashMap();
                    for (int i = 0; i < dataList.size(); i++) {
                        if (arrayList.get(current).equals(dataList.get(i).getImageId())) {
                            map.put("image", base64List.get(current));
                        }
                    }
                    map.put("user_id", mActivity.getParaMap().get("user_id"));
                    map.put("image_mark", "");
                    String result = mActivity.sendPost("/app/image_upload.htm", map);
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        idList.add(jsonObject.getJSONObject("data").getString("acc_id") + "");
                        updateURLList.add(jsonObject.getJSONObject("data").getString("acc_url"));
                        current++;
                    } else {
                        mActivity.hideProcessDialog(1);
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
            return current;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid == list2.size()) {
                mActivity.hideProcessDialog(0);
                //上传成功后返回到评价页面并把图片信息传回
                if (getTargetFragment() == null)
                    return;
                Bundle b = new Bundle();
                Intent intent = new Intent();
                b.putSerializable("dataList", dataList2);
                //
                b.putInt("position", getArguments().getInt("position"));
                //图片id
                b.putStringArrayList("idList", idList);
                //图片url
                b.putStringArrayList("updateURLList", updateURLList);
                //图片本地id
                b.putStringArrayList("list2", list2);
                //图片
                b.putIntegerArrayList("list", list);
                for (int y = 0; y < list.size(); y++) {
                    returnImageList.add(dataList.get(list.get(y)));
                }
                b.putSerializable("returnImageList", returnImageList);
                intent.putExtras(b);
                getTargetFragment().onActivityResult(getTargetRequestCode(), OrderEvaluateFragment.NUM, intent);
                getFragmentManager().popBackStack();
            } else {
                //上传失败
                Toast.makeText(mActivity, "上传失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dataList.size();
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
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final ViewHoleder viewHoleder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_order_evaluate_photo, null);
                viewHoleder = new ViewHoleder();
                viewHoleder.iv_item_order_evaluate_photo = (ImageView) view.findViewById(R.id.iv_item_order_evaluate_photo);
                viewHoleder.tv_item_order_evaluate_photo = (TextView) view.findViewById(R.id.tv_item_order_evaluate_photo);
                view.setTag(viewHoleder);
            } else {
                viewHoleder = (ViewHoleder) view.getTag();
            }
            final ImageItem item = dataList.get(position);
            viewHoleder.iv_item_order_evaluate_photo.setTag(item.imagePath);
            cache.displayBmp(viewHoleder.iv_item_order_evaluate_photo, item.thumbnailPath, item.imagePath, callback);
            if (item.isSelected) {
                viewHoleder.tv_item_order_evaluate_photo.setBackgroundResource(R.mipmap.checked_round);
            } else {
                viewHoleder.tv_item_order_evaluate_photo.setBackgroundResource(R.mipmap.unchecked_round);
            }
            viewHoleder.iv_item_order_evaluate_photo.setOnClickListener(view1 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {

                    if (!item.isSelected) {
                        if (total < count) {
                            total++;
                            item.isSelected = true;
                            list.add(position);
                            list2.add(item.getImageId());
                            try {
                                base64List.add(Bimp.imgToBase64(Bimp.rotateBitmap(Bimp.revitionImageSize(dataList.get(position).imagePath), Bimp.readPictureDegree(dataList.get(position).imagePath))));
                            } catch (Exception e) {
                            }
                            tv_order_evaluate_photo.setText(total + "/" + count);
                            viewHoleder.tv_item_order_evaluate_photo.setBackgroundResource(R.mipmap.checked_round);
                        }
                    } else {
                        item.isSelected = false;
                        total--;
                        for (int i = 0; i < list2.size(); i++) {
                            if (list2.get(i).equals(item.getImageId())) {
                                list2.remove(i);
                                base64List.remove(i);
                                tv_order_evaluate_photo.setText(total + "/" + count);
                            }
                            if (list.get(i) == position) {
                                list.remove(i);
                                tv_order_evaluate_photo.setText(total + "/" + count);
                            }
                        }
                        viewHoleder.tv_item_order_evaluate_photo.setBackgroundResource(R.mipmap.unchecked_round);
                    }
                }
            });
            return view;
        }

        public class ViewHoleder {
            ImageView iv_item_order_evaluate_photo;
            TextView tv_item_order_evaluate_photo;
        }
    }

}

