package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.BitmapCache;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.ImageItem;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ComplainCommitFragment extends Fragment {
    private View rootView;
    private BaseActivity mActivity;
    private Bundle bundle;
    private com.facebook.drawee.view.SimpleDraweeView image_goods;
    private int currentPictureStatus = 0;
    private String selectIdString = "-1";
    private String complainPictureIdString = "";
    private BitmapCache cache;
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
    //    private Button b_complain_picture_manager;
    //图片队列
    private List<ImageView> complainPictureList = new ArrayList<ImageView>();
    private ImageView iv_complain_picture_1;
    private ImageView iv_complain_picture_2;
    private ImageView iv_complain_picture_3;
    private ImageView iv_complain_picture_1_delete;
    private ImageView iv_complain_picture_2_delete;
    private ImageView iv_complain_picture_3_delete;
    private ArrayList<ImageItem> resultDataList = new ArrayList<ImageItem>();
    private ArrayList<String> idList = new ArrayList<String>();
    private RelativeLayout rl_complain_picture_1;
    private RelativeLayout rl_complain_picture_2;
    private RelativeLayout rl_complain_picture_3;

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
        this.rootView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_complain_detail, container, false);
        mActivity = (BaseActivity) getActivity();
        bundle = getArguments();
        cache = new BitmapCache();
        image_goods = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.image_goods);
        image_goods.setImageURI(Uri.parse(bundle.getString("goods_img")));
        TextView tv_goods_name = (TextView) rootView.findViewById(R.id.tv_goods_name);
        tv_goods_name.setText(bundle.getString("goods_name"));
        TextView tv_goods_time = (TextView) rootView.findViewById(R.id.tv_goods_time);
        tv_goods_time.setText("下单时间：" + bundle.getString("addTime"));
        RelativeLayout rl_select_theme = (RelativeLayout) rootView.findViewById(R.id.rl_select_theme);
        rl_select_theme.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
            bundle.putString("selectIdString", selectIdString);
            mActivity.go_complain_theme(bundle, ComplainCommitFragment.this);
            }
        });
        final LinearLayout ll_complain_picture_list = (LinearLayout) rootView.findViewById(R.id.ll_complain_picture_list);

        Button b_complain_commit_done = (Button) rootView.findViewById(R.id.b_complain_commit_done);
        b_complain_commit_done.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                commit_complain();
            }
        });
        rl_complain_picture_1 = (RelativeLayout) rootView.findViewById(R.id.rl_complain_picture_1);
        rl_complain_picture_2 = (RelativeLayout) rootView.findViewById(R.id.rl_complain_picture_2);
        rl_complain_picture_3 = (RelativeLayout) rootView.findViewById(R.id.rl_complain_picture_3);
        iv_complain_picture_1 = (ImageView) rootView.findViewById(R.id.iv_complain_picture_1);
        iv_complain_picture_2 = (ImageView) rootView.findViewById(R.id.iv_complain_picture_2);
        iv_complain_picture_3 = (ImageView) rootView.findViewById(R.id.iv_complain_picture_3);
        iv_complain_picture_1_delete = (ImageView) rootView.findViewById(R.id.iv_complain_picture_1_delete);
        iv_complain_picture_2_delete = (ImageView) rootView.findViewById(R.id.iv_complain_picture_2_delete);
        iv_complain_picture_3_delete = (ImageView) rootView.findViewById(R.id.iv_complain_picture_3_delete);
        complainPictureList.add(iv_complain_picture_1);
        complainPictureList.add(iv_complain_picture_2);
        complainPictureList.add(iv_complain_picture_3);
        iv_complain_picture_1.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
            //图片
            if (0 == resultDataList.size()) {
                int count = 3 - resultDataList.size();
                if (count <= 0) {
                    Toast.makeText(mActivity, "最多上传3张图片用以投诉！", Toast.LENGTH_SHORT).show();
                } else {
                    bundle.putInt("position", 0);
                    bundle.putInt("total", 0);
                    bundle.putInt("count", count);
                    mActivity.go_select_photo_for_complain_commit(bundle, ComplainCommitFragment.this);
                }
            }
            }
        });
        iv_complain_picture_2.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
            //图片
            if (1 == resultDataList.size()) {
                int count = 3 - resultDataList.size();
                if (count <= 0) {
                    Toast.makeText(mActivity, "最多上传3张图片用以投诉！", Toast.LENGTH_SHORT).show();
                } else {
                    bundle.putInt("position", 0);
                    bundle.putInt("total", 0);
                    bundle.putInt("count", count);
                    mActivity.go_select_photo_for_complain_commit(bundle, ComplainCommitFragment.this);
                }
            }
            }
        });
        iv_complain_picture_3.setOnClickListener(view -> {
            //图片
            if (FastDoubleClickTools.isFastDoubleClick()) {
            if (2 == resultDataList.size()) {
                int count = 3 - resultDataList.size();
                if (count <= 0) {
                    Toast.makeText(mActivity, "最多上传3张图片用以投诉！", Toast.LENGTH_SHORT).show();
                } else {
                    bundle.putInt("position", 0);
                    bundle.putInt("total", 0);
                    bundle.putInt("count", count);
                    mActivity.go_select_photo_for_complain_commit(bundle, ComplainCommitFragment.this);
                }
            }
            }
        });
        iv_complain_picture_1_delete.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
            if (0 == resultDataList.size()) {
                /*rl_complain_picture_1.setVisibility(View.VISIBLE);
                rl_complain_picture_2.setVisibility(View.GONE);
                rl_complain_picture_3.setVisibility(View.GONE);*/
                Toast.makeText(mActivity, "没有图片", Toast.LENGTH_SHORT).show();
            } else if (1 == resultDataList.size()) {
                resultDataList.clear();
                idList.clear();
                /*rl_complain_picture_1.setVisibility(View.VISIBLE);
                rl_complain_picture_2.setVisibility(View.VISIBLE);
                rl_complain_picture_3.setVisibility(View.GONE);*/
                iv_complain_picture_1.setImageResource(R.mipmap.upload_complain_image);
            } else if (2 == resultDataList.size()) {
                resultDataList.remove(0);
                idList.remove(0);
                /*iv_complain_picture_3.setVisibility(View.GONE);*/
                iv_complain_picture_2.setImageResource(R.mipmap.upload_complain_image);
            } else if (3 == resultDataList.size()) {
                resultDataList.remove(0);
                idList.remove(0);
                iv_complain_picture_3.setImageResource(R.mipmap.upload_complain_image);
            }
            for (int i = 0; i < resultDataList.size(); i++) {
                ImageItem item = resultDataList.get(i);
                complainPictureList.get(i).setTag(item.imagePath);
                cache.displayBmp(complainPictureList.get(i), item.thumbnailPath, item.imagePath, callback);
            }
            showPicture();
            }
        });
        iv_complain_picture_2_delete.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
            if (0 == resultDataList.size()) {
                /*rl_complain_picture_1.setVisibility(View.VISIBLE);
                rl_complain_picture_2.setVisibility(View.GONE);
                rl_complain_picture_3.setVisibility(View.GONE);*/
                Toast.makeText(mActivity, "没有图片", Toast.LENGTH_SHORT).show();
            } else if (1 == resultDataList.size()) {
                resultDataList.clear();
                idList.clear();
                /*rl_complain_picture_1.setVisibility(View.VISIBLE);
                rl_complain_picture_2.setVisibility(View.VISIBLE);
                rl_complain_picture_3.setVisibility(View.GONE);*/
                iv_complain_picture_1.setImageResource(R.mipmap.upload_complain_image);
            } else if (2 == resultDataList.size()) {
                resultDataList.remove(1);
                idList.remove(1);
                /*iv_complain_picture_3.setVisibility(View.GONE);*/
                iv_complain_picture_2.setImageResource(R.mipmap.upload_complain_image);
            } else if (3 == resultDataList.size()) {
                resultDataList.remove(1);
                idList.remove(1);
                iv_complain_picture_3.setImageResource(R.mipmap.upload_complain_image);
            }
            for (int i = 0; i < resultDataList.size(); i++) {
                ImageItem item = resultDataList.get(i);
                complainPictureList.get(i).setTag(item.imagePath);
                cache.displayBmp(complainPictureList.get(i), item.thumbnailPath, item.imagePath, callback);
            }
            showPicture();
            }
        });
        iv_complain_picture_3_delete.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
            if (0 == resultDataList.size()) {
                /*rl_complain_picture_1.setVisibility(View.VISIBLE);
                rl_complain_picture_2.setVisibility(View.GONE);
                rl_complain_picture_3.setVisibility(View.GONE);*/
                Toast.makeText(mActivity, "没有图片", Toast.LENGTH_SHORT).show();
            } else if (1 == resultDataList.size()) {
                resultDataList.clear();
                idList.clear();
                /*rl_complain_picture_1.setVisibility(View.VISIBLE);
                rl_complain_picture_2.setVisibility(View.VISIBLE);
                rl_complain_picture_3.setVisibility(View.GONE);*/
                iv_complain_picture_1.setImageResource(R.mipmap.upload_complain_image);
            } else if (2 == resultDataList.size()) {
                resultDataList.remove(2);
                idList.remove(2);
                /*iv_complain_picture_3.setVisibility(View.GONE);*/
                iv_complain_picture_2.setImageResource(R.mipmap.upload_complain_image);
            } else if (3 == resultDataList.size()) {
                resultDataList.remove(2);
                idList.remove(2);
                iv_complain_picture_3.setImageResource(R.mipmap.upload_complain_image);
            }
            for (int i = 0; i < resultDataList.size(); i++) {
                ImageItem item = resultDataList.get(i);
                complainPictureList.get(i).setTag(item.imagePath);
                cache.displayBmp(complainPictureList.get(i), item.thumbnailPath, item.imagePath, callback);
            }
            showPicture();
            }
        });
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.complain_desc));//设置标题
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            Bundle resultBundle = data.getExtras();
            selectIdString = resultBundle.getString("selectIdString");
            TextView tv_select_theme = (TextView) rootView.findViewById(R.id.tv_select_theme);
            tv_select_theme.setText(resultBundle.getString("selectThemeString"));
        } else if (requestCode == 1) {
            Bundle resultBundle = data.getExtras();
            ArrayList<ImageItem> dataList = (ArrayList) resultBundle.getSerializable("dataList");
            for (int i = 0; i < resultBundle.getStringArrayList("list2").size(); i++) {
                for (int j = 0; j < dataList.size(); j++) {
                    if (dataList.get(j).getImageId().equals(resultBundle.getStringArrayList("list2").get(i))) {
                        resultDataList.add(dataList.get(j));
                    }
                }
            }
            if (1 == resultDataList.size()) {
                iv_complain_picture_2.setVisibility(View.VISIBLE);
            } else if (2 <= resultDataList.size()) {
                iv_complain_picture_2.setVisibility(View.VISIBLE);
                iv_complain_picture_3.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < resultDataList.size(); i++) {
                ImageItem item = resultDataList.get(i);
                complainPictureList.get(i).setTag(item.imagePath);
                cache.displayBmp(complainPictureList.get(i), item.thumbnailPath, item.imagePath, callback);
            }
            for (int i = 0; i < resultBundle.getStringArrayList("idList").size(); i++) {
                idList.add(resultBundle.getStringArrayList("idList").get(i));
                /*if ("".equals(complainPictureIdString)) {
                    complainPictureIdString += resultBundle.getStringArrayList("idList").get(i);
                } else {
                    complainPictureIdString += "," + resultBundle.getStringArrayList("idList").get(i);
                }*/
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void commit_complain() {
        complainPictureIdString = "";
        for (int i = 0; i < idList.size(); i++) {
            if ("".equals(complainPictureIdString)) {
                complainPictureIdString += idList.get(i);
            } else {
                complainPictureIdString += "," + idList.get(i);
            }
        }
        TextView ed_complain_title = (TextView) rootView.findViewById(R.id.ed_complain_title);
        final String content = ed_complain_title.getText().toString();
        TextView ed_complain_content = (TextView) rootView.findViewById(R.id.ed_complain_content);
        final String from_user_content = ed_complain_content.getText().toString();
        boolean flag = true;
        String msg = "";
        if (flag && "-1".equals(selectIdString)) {
            flag = false;
            msg += "请选择投诉主题！";
        }
        if (flag && "".equals(content)) {
            flag = false;
            msg += "问题描述不能为空！";
        }
        if (flag && "".equals(from_user_content)) {
            flag = false;

            msg += "投诉内容不能为空！";
        }
        if (currentPictureStatus == 1) {
            if (flag && "".equals(complainPictureIdString)) {
                flag = false;
                msg += "请选择上传的图片！";
            }
        }
        if (flag) {
            new AlertDialog.Builder(mActivity).setTitle("你确定要提交投诉吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                        Map paramap = mActivity.getParaMap();
                        paramap.put("order_id", bundle.getString("oid"));
                        paramap.put("cs_id", selectIdString);
                        paramap.put("goods_ids", bundle.getString("goods_id"));
                        paramap.put("goods_gsp_ids", bundle.getString("goods_gsp_ids"));
                        paramap.put("imgs", complainPictureIdString);
                        paramap.put("content", content);
                        paramap.put("from_user_content", from_user_content);
                        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
                        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/complaint_add.htm",
                                result -> {
                                    try {
                                        String code = result.getString("code");
                                        if ("100".equals(code)) {
                                            Toast.makeText(mActivity, "投诉成功", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent();
                                            getFragmentManager().popBackStack();
                                            if (getTargetFragment()!=null){
                                                getTargetFragment().onActivityResult(getTargetRequestCode(), ComplainListFragment.NUM, intent);
                                            }

                                        } else {
                                            Toast.makeText(mActivity, "投诉失败", Toast.LENGTH_SHORT).show();
                                        }
                                        mActivity.hideProcessDialog(0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }, error -> {
                                    Toast.makeText(mActivity, "投诉失败", Toast.LENGTH_SHORT).show();
                                    mActivity.hideProcessDialog(1);
                                }, paramap);
                        mRequestQueue.add(request);
                        }
                    }).setNegativeButton("取消", null).show();
        } else {
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showPicture() {
        if (0 == resultDataList.size()) {
            rl_complain_picture_2.setVisibility(View.INVISIBLE);
            rl_complain_picture_3.setVisibility(View.INVISIBLE);
            iv_complain_picture_1_delete.setVisibility(View.GONE);
            iv_complain_picture_2_delete.setVisibility(View.GONE);
            iv_complain_picture_3_delete.setVisibility(View.GONE);
        } else if (1 == resultDataList.size()) {
            rl_complain_picture_2.setVisibility(View.VISIBLE);
            rl_complain_picture_3.setVisibility(View.INVISIBLE);
            iv_complain_picture_1_delete.setVisibility(View.VISIBLE);
            iv_complain_picture_2_delete.setVisibility(View.GONE);
            iv_complain_picture_3_delete.setVisibility(View.GONE);
        } else if (2 == resultDataList.size()) {
            rl_complain_picture_2.setVisibility(View.VISIBLE);
            rl_complain_picture_3.setVisibility(View.VISIBLE);
            iv_complain_picture_1_delete.setVisibility(View.VISIBLE);
            iv_complain_picture_2_delete.setVisibility(View.VISIBLE);
            iv_complain_picture_3_delete.setVisibility(View.GONE);
        } else if (3 == resultDataList.size()) {
            rl_complain_picture_2.setVisibility(View.VISIBLE);
            rl_complain_picture_3.setVisibility(View.VISIBLE);
            iv_complain_picture_1_delete.setVisibility(View.VISIBLE);
            iv_complain_picture_2_delete.setVisibility(View.VISIBLE);
            iv_complain_picture_3_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        showPicture();
    }
}
