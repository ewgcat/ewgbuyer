package com.ewgvip.buyer.android.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 收货地址列表
 */
public class AddressListFragment extends Fragment {
    public static int NUM = 1;
    Dialog dialog;
    private View rootView;
    private BaseActivity mActivity;
    private ListView lview_fragment_address_list;
    private ArrayList<Map<String, String>> addressList;
    private AddressAdapter addressAdapter;
    private Button btn_fragment_address_list_new;
    private boolean isRefresh = true;//防止网络不好时多次请求改变默认地址

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.lview_fragment_address_list = null;
        this.addressList = null;
        this.addressAdapter = null;
        this.btn_fragment_address_list_new = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_address_list, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        addressList = new ArrayList<Map<String, String>>();
        //地址列表listview
        lview_fragment_address_list = (ListView) rootView.findViewById(R.id.lview_fragment_address_list);
        //新增地址按钮
        btn_fragment_address_list_new = (Button) rootView.findViewById(R.id.btn_fragment_address_list_new);
        //点击新增地址按钮切换到新增地址界面
        btn_fragment_address_list_new.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "add");
                mActivity.go_address_edit(bundle, AddressListFragment.this);
            }
        });
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("收货地址");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        addressAdapter = new AddressAdapter();
        lview_fragment_address_list.setAdapter(addressAdapter);
        getAddressInfo();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mActivity.showProcessDialog();
        getAddressInfo();
    }

    //获取地址信息
    private void getAddressInfo() {

        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/address.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    isRefresh = true;
                    addressList.clear();
                    JSONArray jsonarr;
                    int length;
                    if(jsonObject.has("address_list")) {
                        jsonarr= jsonObject.getJSONArray("address_list");
                        length= jsonarr.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = jsonarr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("card", oj.getString("card"));
                            map.put("areaInfo", oj.getString("areaInfo"));
                            map.put("area", oj.getString("area"));
                            map.put("area_id", oj.getString("area_id"));
                            map.put("trueName", oj.getString("trueName"));
                            map.put("addr_id", oj.getString("addr_id"));
                            if (oj.getString("mobile").equals("")) {
                                map.put("mobile", oj.getString("telephone"));
                            } else {
                                map.put("mobile", oj.getString("mobile"));
                            }
                            if (oj.has("default")
                                    && oj.getBoolean("default")) {
                                map.put("visibility", "1");
                            } else {
                                map.put("visibility", "0");
                            }
                            addressList.add(map);
                        }
                        if (addressList.size() == 0) {
                            mActivity.findViewById(R.id.noaddress).setVisibility(View.VISIBLE);
                            lview_fragment_address_list.setVisibility(View.GONE);
                        } else {
                            mActivity.findViewById(R.id.noaddress).setVisibility(View.GONE);
                            lview_fragment_address_list.setVisibility(View.VISIBLE);
                        }

                        if (addressAdapter!=null) {
                            addressAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 地址的adapter
     */
    class AddressAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return addressList.size();
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
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_address_list2, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_item_address_list_name = (TextView) view.findViewById(R.id.tv_item_address_list_name);
                viewHolder.tv_item_address_list_tel = (TextView) view.findViewById(R.id.tv_item_address_list_tel);
                viewHolder.tv_item_address_list_address = (TextView) view.findViewById(R.id.tv_item_address_list_address);
                viewHolder.tv_item_address_list_address_card = (TextView) view.findViewById(R.id.tv_item_address_list_address_card);
                viewHolder.rbtn_item_address_list = (RadioButton) view.findViewById(R.id.rbtn_item_address_list);
                viewHolder.btn_item_address_list_edit = (Button) view.findViewById(R.id.btn_item_address_list_edit);
                viewHolder.btn_item_address_list_delete = (Button) view.findViewById(R.id.btn_item_address_list_delete);
                viewHolder.layout_item_address_list = (RelativeLayout) view.findViewById(R.id.layout_item_address_list);
                viewHolder.layout_item_address_default = (RelativeLayout) view.findViewById(R.id.layout_item_address_default);
                viewHolder.layout_item_address_edit = (RelativeLayout) view.findViewById(R.id.layout_item_address_edit);
                viewHolder.layout_item_address_select = (RelativeLayout) view.findViewById(R.id.layout_item_address_select);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_item_address_list_name.setText(addressList.get(i).get("trueName"));
            viewHolder.tv_item_address_list_tel.setText(addressList.get(i).get("mobile"));
            //TODO 身份证号码显示
            String card = addressList.get(i).get("card");
            if (!TextUtils.isEmpty(card)){
                viewHolder.tv_item_address_list_address_card.setVisibility(View.VISIBLE);
                viewHolder.tv_item_address_list_address_card.setText("身份证号码：XXX"+card.substring(14));
            }else{
                viewHolder.tv_item_address_list_address_card.setVisibility(View.GONE);
            }


            //地址显示
            viewHolder.tv_item_address_list_address.setText(addressList.get(i).get("area") + addressList.get(i).get("areaInfo"));

            if (getArguments().getInt("type", 0) == 1) {
                viewHolder.layout_item_address_edit.setVisibility(View.VISIBLE);
                viewHolder.layout_item_address_default.setVisibility(View.GONE);
                viewHolder.layout_item_address_select.setOnClickListener(view1 -> {
                    Map paramap = new HashMap<String, String>();
                    paramap.put("user_id", mActivity.getParaMap().get("user_id"));
                    paramap.put("token", mActivity.getParaMap().get("token"));
                    paramap.put("addr_id", addressList.get(i).get("addr_id"));
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(
                            mActivity, mActivity.getAddress()
                            + "/app/buyer/address_default_set.htm",
                            result -> {
                                if (getTargetFragment() == null)
                                    return;
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("address", addressList.get(i).get("area") + addressList.get(i).get("areaInfo"));
                                bundle.putString("mobile", addressList.get(i).get("mobile"));
                                bundle.putString("username", addressList.get(i).get("trueName"));
                                bundle.putString("lottery_id", getArguments().containsKey("lottery_id") ? getArguments().getString("lottery_id") : "");
                                bundle.putString("current", getArguments().containsKey("current") ? getArguments().getInt("current") + "" : "");
                                intent.putExtras(bundle);
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Cart2Fragment.ADDRESS, intent);
                                getFragmentManager().popBackStack();
                            }, error -> Log.i("test",error.toString()), paramap);
                    mRequestQueue.add(request);
                });
                viewHolder.layout_item_address_edit.setOnClickListener(view12 -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "edit");
                        bundle.putString("trueName", addressList.get(i).get("trueName"));
                        bundle.putString("mobile", addressList.get(i).get("mobile"));
                        bundle.putString("areaInfo", addressList.get(i).get("areaInfo"));
                        bundle.putString("area", addressList.get(i).get("area"));
                        bundle.putString("area_id", addressList.get(i).get("area_id"));
                        bundle.putString("zip", addressList.get(i).get("zip"));
                        bundle.putString("addr_id", addressList.get(i).get("addr_id"));
                        bundle.putString("card", addressList.get(i).get("card"));
                        mActivity.go_address_edit(bundle, AddressListFragment.this);
                    }
                });
            } else {
                viewHolder.layout_item_address_default.setVisibility(View.VISIBLE);
                viewHolder.layout_item_address_edit.setVisibility(View.GONE);
                if (addressList.get(i).get("visibility").equals("1")) {
                    viewHolder.rbtn_item_address_list.setChecked(true);
                } else {
                    viewHolder.rbtn_item_address_list.setChecked(false);
                }
                viewHolder.layout_item_address_list.setOnClickListener(view13 -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        if (!viewHolder.rbtn_item_address_list.isChecked()) {
                            if (addressList.size() != 0) {
                                if (isRefresh) {
                                    isRefresh = false;
                                    Map paramap = new HashMap<String, String>();
                                    paramap.put("user_id", mActivity.getParaMap().get("user_id"));
                                    paramap.put("token", mActivity.getParaMap().get("token"));
                                    paramap.put("addr_id", addressList.get(i).get("addr_id"));
                                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                    Request<JSONObject> request = new NormalPostRequest(
                                            mActivity, mActivity.getAddress()
                                            + "/app/buyer/address_default_set.htm",
                                            result -> getAddressInfo(), error -> {
                                                Log.i("test",error.toString());
                                                viewHolder.rbtn_item_address_list.setChecked(false);
                                                isRefresh = true;
                                            }, paramap);
                                    mRequestQueue.add(request);
                                }
                            }
                        }
                    }
                });
                //点击删除地址弹出的对话框
                viewHolder.btn_item_address_list_delete.setOnClickListener(view14 -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        dialog = com.ewgvip.buyer.android.dialog.MyDialog.showAlert(mActivity, "系统提示", "是否删除该地址？", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view14) {
                                Map paramap = new HashMap<String, String>();
                                paramap.put("user_id", mActivity.getParaMap().get("user_id"));
                                paramap.put("token", mActivity.getParaMap().get("token"));
                                paramap.put("addr_id", addressList.get(i).get("addr_id"));
                                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                Request<JSONObject> request = new NormalPostRequest(
                                        mActivity, mActivity.getAddress()
                                        + "/app/buyer/address_delete.htm",
                                        result -> {
                                            dialog.dismiss();
                                            Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
                                            addressList.clear();
                                            getAddressInfo();
                                        }, error -> {
                                            Log.i("test",error.toString());
                                            Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }, paramap);
                                mRequestQueue.add(request);
                            }
                        }, "取消", dialogInterface -> dialog.dismiss());

                    }
                });

                /**
                 * 地址的编辑
                 */
                viewHolder.btn_item_address_list_edit.setOnClickListener(view15 -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "edit");
                        bundle.putString("trueName", addressList.get(i).get("trueName"));
                        bundle.putString("mobile", addressList.get(i).get("mobile"));
                        bundle.putString("areaInfo", addressList.get(i).get("areaInfo"));
                        bundle.putString("area", addressList.get(i).get("area"));
                        bundle.putString("area_id", addressList.get(i).get("area_id"));
                        bundle.putString("zip", addressList.get(i).get("zip"));
                        bundle.putString("card", addressList.get(i).get("card"));
                        bundle.putString("addr_id", addressList.get(i).get("addr_id"));
                        mActivity.go_address_edit(bundle, AddressListFragment.this);
                    }
                });
            }

            return view;
        }

        class ViewHolder {
            TextView tv_item_address_list_name;
            TextView tv_item_address_list_tel;
            TextView tv_item_address_list_address;
            TextView tv_item_address_list_address_card;
            RadioButton rbtn_item_address_list;
            Button btn_item_address_list_edit;
            Button btn_item_address_list_delete;
            RelativeLayout layout_item_address_list;
            RelativeLayout layout_item_address_default;
            RelativeLayout layout_item_address_edit;
            RelativeLayout layout_item_address_select;
        }
    }

}
