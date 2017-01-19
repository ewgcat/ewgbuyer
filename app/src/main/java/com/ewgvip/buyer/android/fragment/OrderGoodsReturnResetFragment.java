package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.Map;

/**
 * <p>Title: ${file_name}</p>
 * <p>
 * <p>Description: </p>
 * <p>
 * <p>Company: 沈阳网之商科技有限公司 www.iskyshop.com</p>
 *
 * @author zhangmanyuan
 * @date ${date}
 */

public class OrderGoodsReturnResetFragment extends Fragment {

    private View rootView;
    private BaseActivity baseActivity;
    private SimpleDraweeView iv_goods_icon;
    private TextView goods_name;
    private EditText etCount;
    private ImageView pikerPlus, pikerMinus;
    private LinearLayout ll_return_count;
    private EditText return_goods_content;
    private Button order_submit;
    private Map paramap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_goods_return1, container, false);
        baseActivity = (BaseActivity) getActivity();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        baseActivity.setSupportActionBar(toolbar);
        toolbar.setTitle("取消申请");
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                baseActivity.onBackPressed();
            }
        });
        //设置菜单可用
        setHasOptionsMenu(false);

        iv_goods_icon = (SimpleDraweeView) rootView.findViewById(R.id.iv_goods_icon);
        goods_name = (TextView) rootView.findViewById(R.id.goods_name);
        etCount = (EditText) rootView.findViewById(R.id.etCount);
        return_goods_content = (EditText) rootView.findViewById(R.id.return_goods_content);
        pikerPlus = (ImageView) rootView.findViewById(R.id.pikerPlus);
        pikerMinus = (ImageView) rootView.findViewById(R.id.pikerMinus);
        ll_return_count = (LinearLayout) rootView.findViewById(R.id.ll_return_count);
        order_submit = (Button) rootView.findViewById(R.id.order_submit);

        Bundle bundle = getArguments();
        BaseActivity.displayImage(bundle.get("goods_img").toString(), iv_goods_icon);
        goods_name.setText(bundle.get("goods_name").toString());
        pikerMinus.setClickable(false);
        pikerPlus.setClickable(false);
        ll_return_count.setVisibility(View.GONE);
        return_goods_content.setFocusable(false);
        return_goods_content.setClickable(false);
        etCount.setFocusable(false);
        etCount.setClickable(false);

        paramap = baseActivity.getParaMap();
        paramap.put("goods_id", bundle.getString("goods_id"));
        paramap.put("goods_gsp_ids", bundle.getString("goods_gsp_ids"));
        paramap.put("oid", bundle.getString("oid"));

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(baseActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(baseActivity, baseActivity.getAddress() + "/app/buyer/goods_return_apply.htm",
                result -> {
                    try {
                        if (result.has("return_count")) {
                            etCount.setText(result.get("return_count") + "");
                        }
                        if (result.has("return_goods_content")) {
                            return_goods_content.setText(result.get("return_goods_content") + "");
                        }
                        order_submit.setText("取消申请");
                        order_submit.setOnClickListener(view -> {
                            RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(baseActivity).getRequestQueue();
                            Request<JSONObject> request1 = new NormalPostRequest(baseActivity, baseActivity.getAddress() + "/app/buyer/goods_return_cancel_save.htm",
                                    result1 -> {
                                        try {
                                            String msg = "取消失败";
                                            if (result1.getString("ret").equals("true")) {
                                                msg = "取消成功";
                                            }
                                            Toast.makeText(baseActivity, msg, Toast.LENGTH_SHORT).show();
                                            if (getTargetFragment() != null) {
                                                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, null);
                                            }
                                            getFragmentManager().popBackStack();
                                        } catch (Exception e) {
                                        }
                                        baseActivity.hideProcessDialog(0);
                                    }, error -> baseActivity.hideProcessDialog(1), paramap);
                            mRequestQueue1.add(request1);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    baseActivity.hideProcessDialog(0);
                }, error -> baseActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
        return rootView;

    }
}
