package com.ewgvip.buyer.android.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.IdCardUtil;
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
 * 收货地址编辑
 */
public class AddressEditFragment extends Fragment {
    private Dialog dialog;
    private String area_id = "";
    private String addr_id = "";
    private int current = 0;
    private MyAdapter adapter = new MyAdapter();
    private StringBuffer stringBuffer = new StringBuffer();
    private View rootView;
    private BaseActivity mActivity;
    private EditText et_address_edit_name;
    private EditText et_address_edit_tel;
    private EditText et_address_edit_address;
    private EditText et_address_edit_card;
    private TextView tv_address_edit_area;
    private Button btn_fragment_address_edit;
    private ArrayList<String> namelist;
    private ArrayList<Integer> idList;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.et_address_edit_name = null;
        this.et_address_edit_tel = null;
        this.et_address_edit_address = null;
        this.et_address_edit_card = null;
        this.tv_address_edit_area = null;
        this.btn_fragment_address_edit = null;
        this.namelist = null;
        this.idList = null;
        this.stringBuffer = null;
        this.dialog = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_address_edit, container, false);
        mActivity = (BaseActivity) getActivity();

        et_address_edit_name = (EditText) rootView.findViewById(R.id.et_address_edit_name);
        et_address_edit_tel = (EditText) rootView.findViewById(R.id.et_address_edit_tel);
        et_address_edit_address = (EditText) rootView.findViewById(R.id.et_address_edit_address);
        et_address_edit_card = (EditText) rootView.findViewById(R.id.et_address_edit_card);
        tv_address_edit_area = (TextView) rootView.findViewById(R.id.tv_address_edit_area);
        btn_fragment_address_edit = (Button) rootView.findViewById(R.id.btn_fragment_address_edit);
        Bundle bundle = getArguments();
        final String type = bundle.get("type") + "";

        if (type.equals("add")) {
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            toolbar.setTitle("新增地址");//设置标题
            mActivity.setSupportActionBar(toolbar);//设置toolbar
            toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置导航点击事件
                @Override
                public void onClick(View v) {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.onBackPressed();
                    }
                }
            });
            setHasOptionsMenu(false);//设置菜单可用

            et_address_edit_name.setText("");
            et_address_edit_tel.setText("");
            et_address_edit_address.setText("");
            et_address_edit_card.setText("");
            tv_address_edit_area.setText("");
            btn_fragment_address_edit.setText("添加");
        } else if (type.equals("edit")) {
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            toolbar.setTitle("修改地址");//设置标题
            mActivity.setSupportActionBar(toolbar);//设置toolbar
            toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
            toolbar.setNavigationOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                }
            });
            setHasOptionsMenu(false);//设置菜单可用
            et_address_edit_name.setText(bundle.getString("trueName"));
            et_address_edit_tel.setText(bundle.getString("mobile"));
            et_address_edit_address.setText(bundle.getString("areaInfo"));
            et_address_edit_card.setText(bundle.getString("card"));
            area_id = bundle.getString("area_id");
            String area = bundle.getString("area");
            String[] loacl = area.split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < loacl.length; i++) {
                stringBuffer.append(loacl[i]);
            }
            tv_address_edit_area.setText(stringBuffer.toString());
            tv_address_edit_area.setTextColor(mActivity.getResources().getColor(R.color.black));
            addr_id = bundle.getString("addr_id");
            Log.i("AddressEditFragment",addr_id);
            btn_fragment_address_edit.setText("保存");
        }
        //收货地址的编辑
        btn_fragment_address_edit.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (et_address_edit_name.getText().toString().equals("")) {
                    Toast.makeText(mActivity, "收款人姓名不能为空", Toast.LENGTH_SHORT).show();
                } else if ((et_address_edit_tel.getText() + "").equals("")) {
                    Toast.makeText(mActivity, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }else if (!et_address_edit_tel.getText().toString().matches(Constants.phone_reg)) {
                    Toast.makeText(mActivity, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                } else if (tv_address_edit_area.getText().toString().equals("")) {
                    Toast.makeText(mActivity, "所在区域不能为空", Toast.LENGTH_SHORT).show();
                } else if (et_address_edit_address.getText().toString().equals("")) {
                    Toast.makeText(mActivity, "详细地址不能为空", Toast.LENGTH_SHORT).show();
                } else if (et_address_edit_card.getText().toString().trim().length()>0&&et_address_edit_card.getText().toString().trim().length()<18){
                    Toast.makeText(mActivity, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
                } else if (et_address_edit_card.getText().toString().trim().length()==18&& !IdCardUtil.checkCard(et_address_edit_card.getText().toString().trim())){
                    Toast.makeText(mActivity, "您输入的身份证号码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    if (type.equals("add")) {
                        //新增地址
                        Map map =  mActivity.getParaMap();
                        map.put("trueName", et_address_edit_name.getText().toString());
                        map.put("card", et_address_edit_card.getText().toString());
                        map.put("mobile", "");
                        map.put("zip","000000");
                        map.put("telephone", et_address_edit_tel.getText().toString());
                        map.put("area_info",  et_address_edit_address.getText().toString());
                        map.put("area_id", area_id.toString());
                        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/address_save.htm", map, new BaseSubscriber<JSONObject>(mActivity) {
                            @Override
                            public void onNext(JSONObject jsonObject) {
                                Toast.makeText(mActivity, "添加成功", Toast.LENGTH_SHORT).show();
                                if (getTargetFragment() == null){
                                    return;
                                }
                                Intent intent = new Intent();
                                getTargetFragment().onActivityResult(getTargetRequestCode(), AddressListFragment.NUM, intent);
                                getFragmentManager().popBackStack();
                            }
                        });

                    } else if (type.equals("edit")) {

                        //修改地址
                        Map map =  mActivity.getParaMap();
                        map.put("trueName", et_address_edit_name.getText().toString());
                        map.put("card", et_address_edit_card.getText().toString());
                        map.put("mobile", "");
                        map.put("telephone", et_address_edit_tel.getText().toString());
                        map.put("area_info", et_address_edit_address.getText().toString());
                        map.put("area_id", area_id.toString());
                        map.put("addr_id", addr_id.toString());

                        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/address_save.htm", map, new BaseSubscriber<JSONObject>(mActivity) {
                            @Override
                            public void onNext(JSONObject jsonObject) {
                                Toast.makeText(mActivity, "修改成功", Toast.LENGTH_SHORT).show();
                                if (getTargetFragment() == null){
                                    return;
                                }
                                Intent intent = new Intent();
                                getTargetFragment().onActivityResult(getTargetRequestCode(), AddressListFragment.NUM, intent);
                                getFragmentManager().popBackStack();
                            }
                        });
                    }
                }
            }
        });
        tv_address_edit_area.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                current = 0;
                stringBuffer.delete(0, stringBuffer.length());
                getcity(null);
            }
        });

        return rootView;
    }


    //获取区域信息
    public void getcity(final Map map) {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(
                mActivity, mActivity.getAddress() + "/app/area_load.htm",
                result -> {
                    try {
                        namelist = new ArrayList<>();
                        idList = new ArrayList<>();
                        final JSONArray nameList = result.getJSONArray("area_list");
                        int length = nameList.length();
                        String[] citys = new String[length];
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = nameList.getJSONObject(i);
                            namelist.add(oj.getString("name"));
                            idList.add(oj.getInt("id"));
                            citys[i] = oj.getString("name") + "";
                        }
                        current++;
                        dialog = MyDialog.showAlert(mActivity, "请选择城市", citys, (adapterView, view, i, l) -> {
                            dialog.dismiss();
                            if (current < 3) {
                                Map map1 = new HashMap();
                                map1.put("id", idList.get(i));
                                stringBuffer.append(namelist.get(i));
                                getcity(map1);
                                if (adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                stringBuffer.append(namelist.get(i));
                                area_id = idList.get(i) + "";
                                tv_address_edit_area.setText("");
                                tv_address_edit_area.setText(stringBuffer.toString());
                                tv_address_edit_area.setTextColor(mActivity.getResources().getColor(R.color.black));
                                stringBuffer.delete(0, stringBuffer.length());
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test",error.toString()), map);
        mRequestQueue.add(request);
    }

    /**
     * 填充数据的adapter
     */
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return namelist.size();
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
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_address_edit_dialog, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_item_address_edit_dialog = (TextView) view.findViewById(R.id.tv_item_address_edit_dialog);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_item_address_edit_dialog.setText(namelist.get(i));
            return view;
        }

        class ViewHolder {
            TextView tv_item_address_edit_dialog;
        }
    }


}
