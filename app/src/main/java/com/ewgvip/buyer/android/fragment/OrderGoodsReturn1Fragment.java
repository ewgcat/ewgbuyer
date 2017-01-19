package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

/*
    申请退货页面
 */
public class OrderGoodsReturn1Fragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private Map paramap;
    private ImageButton pikerMinus;
    private ImageButton pickerPlus;
    private EditText et;
    private int dialogcount;
    private TextView return_goods_content;
    private int totalCount;


    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.paramap = null;
        this.pikerMinus = null;
        this.pickerPlus = null;
        this.et = null;
        this.return_goods_content = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_goods_return1, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActivity = (BaseActivity) getActivity();
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setTitle("退货申请");//设置标题
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });

        setHasOptionsMenu(true);//设置菜单可用
        final Bundle bundle = getArguments();
        pikerMinus = (ImageButton) rootView.findViewById(R.id.pikerMinus);
        pickerPlus = (ImageButton) rootView.findViewById(R.id.pikerPlus);
        et = (EditText) rootView.findViewById(R.id.etCount);
        TextView tv = (TextView) rootView.findViewById(R.id.goods_name);

        return_goods_content = (TextView) rootView.findViewById(R.id.return_goods_content);
        tv.setText(bundle.getString("goods_name"));
        SimpleDraweeView img = (SimpleDraweeView) rootView.findViewById(R.id.iv_goods_icon);
        img.setImageURI(Uri.parse(bundle.getString("goods_img")));
        paramap = mActivity.getParaMap();
        paramap.put("goods_id", bundle.getString("goods_id"));
        paramap.put("goods_gsp_ids", bundle.getString("goods_gsp_ids"));
        paramap.put("oid", bundle.getString("oid"));

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_return_apply.htm",
                result -> {
                    try {
                        String return_count = result.get("return_count") + "";
                        TextView tv_totalCount = (TextView) rootView.findViewById(R.id.tv_totalCount);
                        tv_totalCount.setText(return_count + "");
                        totalCount = Integer.valueOf(tv_totalCount.getText().toString().trim());
                        if (!et.getText().toString().equals("")) {
                            paramap.put("return_goods_count", et.getText().toString());
                        }

//                            rootView.findViewById(R.id.order_submit).setClickable(true);
                        rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                if (return_goods_content.getText().toString().trim().equals("")) {
                                    Toast.makeText(mActivity, "请填写退货理由", Toast.LENGTH_SHORT).show();
                                } else {
                                    paramap.put("return_goods_content", return_goods_content.getText() + "");

                                    if ((et.getText() + "").equals("")) {
                                        paramap.put("return_goods_count", 1);
                                        goods_return_apply_save();
                                    } else {
                                        boolean b = returnGoodAdjust(Integer.parseInt(et.getText().toString().trim()));
                                        if (b) {
                                            if (Integer.valueOf(et.getText() + "") > totalCount) {
                                                paramap.put("return_goods_count", totalCount);
                                            } else {
                                                paramap.put("return_goods_count", et.getText() + "");
                                            }
                                            goods_return_apply_save();
                                        }
                                    }

//                                        goods_return_apply_save();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0)
                    dialogcount = Integer.valueOf(s.toString());
                if (dialogcount < 1) {
                    dialogcount = 1;
                    et.setText(dialogcount + "");
                }
                et.setSelection(s.toString().length());
                if (dialogcount == 1) {
                    pikerMinus.setClickable(false);
                } else {
                    pikerMinus.setClickable(true);
                }
                if (dialogcount == totalCount) {
                    pickerPlus.setClickable(false);
                } else {
                    pickerPlus.setClickable(true);
                }

            }
        });
        et.setText(dialogcount + "");
        pikerMinus.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (dialogcount > 1) {
                    dialogcount--;
                    et.setText(dialogcount + "");
                }
            }
        });
        pickerPlus.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (dialogcount < totalCount) {
                    dialogcount++;
                } else if (dialogcount == totalCount) {
                    pickerPlus.setClickable(false);
                }
                et.setText(dialogcount + "");
            }

        });
        return rootView;
    }

    private boolean returnGoodAdjust(int currentCount) {
        if (currentCount > totalCount) {
            Toast.makeText(mActivity, "对不起，已超过退货件数", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (currentCount < 0) {
            Toast.makeText(mActivity, "参数错误", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    void goods_return_apply_save() {

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_return_apply_save.htm",
                result -> {
                    try {
                        if (result.getInt("code") == 100) {
                            Toast.makeText(mActivity, "申请成功，请等待商家审核", Toast.LENGTH_SHORT).show();
                            if (getTargetFragment()!=null){
                                getTargetFragment().onActivityResult(getTargetRequestCode(),1, null);
                            }
                            getFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(mActivity, "申请失败，请重试",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

}
