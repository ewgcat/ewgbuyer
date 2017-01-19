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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
    退款申请
 */
public class OrderGrouplifeReturnFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private String return_reason;
    private CheckBox temp;
    private ImageButton pikerMinus;
    private ImageButton pickerPlus;
    private int dialogcount=0;
    private TextView tv_totalCount;
    private int totalCount=0;
    private EditText et;

    private RelativeLayout rl_count;
    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.return_reason = null;
        this.temp = null;
        this.pikerMinus = null;
        this.pickerPlus = null;
        this.et = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_grouplife_return1, container, false);
        mActivity = (BaseActivity) getActivity();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        rl_count=(RelativeLayout)rootView.findViewById(R.id.rl_count);
        //设置标题
        toolbar.setTitle("退款申请");
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);
        rl_count.setVisibility(View.GONE);

        tv_totalCount = (TextView) rootView.findViewById(R.id.tv_totalCount);
        final Bundle bundle = getArguments();

        TextView tv = (TextView) rootView.findViewById(R.id.goods_name);
        tv.setText(bundle.getString("goods_name"));
        SimpleDraweeView img = (SimpleDraweeView) rootView.findViewById(R.id.iv_goods_icon);
        img.setImageURI(Uri.parse(bundle.getString("goods_img")));
        pikerMinus = (ImageButton) rootView.findViewById(R.id.pikerMinus);
        pickerPlus = (ImageButton) rootView.findViewById(R.id.pickerPlus);
        et = (EditText) rootView.findViewById(R.id.etCount);
        et.setFocusable(false);
        et.setClickable(false);
        rootView.findViewById(R.id.return_reason1).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        return_reason = getResources().getString(R.string.return_reason1);
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.return_reason1_check);
                        cb.setChecked(true);
                        if (temp != null) {
                            temp.setChecked(false);
                        }
                        temp = cb;
                    }
                });
        rootView.findViewById(R.id.return_reason2).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        return_reason = getResources().getString(R.string.return_reason2);
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.return_reason2_check);
                        cb.setChecked(true);
                        if (temp != null) {
                            temp.setChecked(false);
                        }
                        temp = cb;
                    }
                });
        rootView.findViewById(R.id.return_reason3).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        return_reason = getResources().getString(R.string.return_reason3);
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.return_reason3_check);
                        cb.setChecked(true);
                        if (temp != null) {
                            temp.setChecked(false);
                        }
                        temp = cb;
                    }
                });
        rootView.findViewById(R.id.return_reason4).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        return_reason = getResources().getString(R.string.return_reason4);
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.return_reason4_check);
                        cb.setChecked(true);
                        if (temp != null) {
                            temp.setChecked(false);
                        }
                        temp = cb;
                    }
                });
        rootView.findViewById(R.id.return_reason5).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        return_reason = getResources().getString(R.string.return_reason5);
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.return_reason5_check);
                        cb.setChecked(true);
                        if (temp != null) {
                            temp.setChecked(false);
                        }
                        temp = cb;
                    }
                });

        rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Map paramap = mActivity.getParaMap();
                        EditText et1 = (EditText) rootView.findViewById(R.id.return_goods_content);
                        String return_content = et1.getText().toString();
                        if (return_content != null
                                && !return_content.equals("")
                                && return_reason != null
                                && !return_reason.equals("")) {
                            paramap.put("return_content", return_content);
                            paramap.put("return_reasion", return_reason);
                            paramap.put("group_id", bundle.get("oid"));

                            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                            Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/grouplife_refund_apply_save.htm",
                                    result -> {
                                        try {
                                            if (result.get("ret").equals("true")) {
                                                Toast.makeText(mActivity, "申请成功，请等待商家审核", Toast.LENGTH_SHORT).show();
                                                if (getTargetFragment()!=null){
                                                    getTargetFragment().onActivityResult(getTargetRequestCode(),0, null);
                                                }

                                                getFragmentManager().popBackStack();
                                            } else {
                                                Toast.makeText(mActivity, "申请失败，请重试", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        mActivity.hideProcessDialog(0);
                                    }, error -> mActivity.hideProcessDialog(1), paramap);
                            mRequestQueue.add(request);
                        } else {
                            Toast.makeText(mActivity, "请填写退款原因及申请说明", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        if(tv_totalCount.getText().toString().trim().equals("")) {
            totalCount = 0;
        }else {
            totalCount = Integer.valueOf(tv_totalCount.getText().toString().trim());
        }
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
                    dialogcount = Integer.parseInt(s.toString());
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
                if (dialogcount > totalCount) {
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
                dialogcount++;
                et.setText(dialogcount + "");
            }

        });

        int curentCount = Integer.valueOf(et.getText().toString());
//        returnGoodAdjust(curentCount);
        return rootView;
    }

//    private void returnGoodAdjust(int currentCount) {
//        if (currentCount > totalCount) {
//            Toast.makeText(mActivity, "对不起，已超过退货申请", Toast.LENGTH_SHORT).show();
//        }
//        if (currentCount < 0) {
//            Toast.makeText(mActivity, "参数错误", Toast.LENGTH_SHORT).show();
//        }
//
//    }

}
