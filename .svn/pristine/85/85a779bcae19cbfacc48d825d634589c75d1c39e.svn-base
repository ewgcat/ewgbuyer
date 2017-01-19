package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择自提点
 */
public class Cart2PickupSiteFragment extends Fragment {

    private BaseActivity mActivity;
    private View rootView;
    private String order_addr_id;
    private String jsonString;
    private List addressList = new ArrayList();
    private com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView lv_address_select;
    private AddressSelectListViewAdapter addressSelectListViewAdapter;
    private String addressId = "";
    private AlertDialog alertDialog;

    @Override
    public void onDetach() {
        super.onDetach();
        
        this.rootView = null;
        this.order_addr_id = null;
        this.jsonString = null;
        this.addressList = null;
        this.lv_address_select = null;
        this.addressSelectListViewAdapter = null;
        this.addressId = null;
        this.alertDialog = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("选择自提点");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用

        Bundle bundle = getArguments();
        order_addr_id = bundle.getString("order_addr_id");
        addressId = mActivity.getCache("addressId");
        jsonString = bundle.getString("JSONString");
        lv_address_select = (com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView) rootView.findViewById(R.id.listview);
        /**
         * 解析数据
         */
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            parseDataJSONArray(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addressSelectListViewAdapter = new AddressSelectListViewAdapter(mActivity, addressList);
        lv_address_select.setAdapter(addressSelectListViewAdapter);
        /**
         * 下拉刷新
         */
        lv_address_select.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                new AsyncTask<Void, Void, String[]>() {
                    @Override
                    protected String[] doInBackground(Void... voids) {
                        addressList.clear();
                        getData();
                        String[] str = {};
                        return str;
                    }

                    @Override
                    protected void onPostExecute(String[] result) {
                        super.onPostExecute(result);
                        lv_address_select.onRefreshComplete();
                    }
                }.execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        /**
         * 底部更新
         */
        lv_address_select.setOnLastItemVisibleListener(() -> getData());
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

    /**
     * 填充优惠券推荐商品的列表展示
     */
    private void getData() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        String address = getResources().getString(R.string.http_url);
        String url = address + "/app/goods_cart2_delivery.htm";
        Map paraMap = mActivity.getParaMap();
        paraMap.put("addr_id", order_addr_id);
        paraMap.put("beginCount", addressList.size());
        paraMap.put("selectCount", 10);
       
        NormalPostRequest couponRequestJSONObject = new NormalPostRequest(mActivity, url,
                result -> {
                    Log.i("test", result.toString());
                    try {
                        JSONArray jsonArray = result.getJSONArray("data");
                        parseDataJSONArray(jsonArray);
                        addressSelectListViewAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
                }, paraMap);
        mRequestQueue.add(couponRequestJSONObject);
    }

    /**
     * 解析数据
     */
    private void parseDataJSONArray(JSONArray jsonArray) throws JSONException {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonArrayItem = jsonArray.getJSONObject(i);
                Map map = new HashMap();
                String addr = jsonArrayItem.has("addr") ? jsonArrayItem.getString("addr") : "";
                String id = jsonArrayItem.has("id") ? jsonArrayItem.getString("id") : "";
                String name = jsonArrayItem.has("name") ? jsonArrayItem.getString("name") : "";
                String tel = jsonArrayItem.has("tel") ? jsonArrayItem.getString("tel") : "";
                map.put("addr", addr);
                map.put("id", id);
                map.put("name", name);
                map.put("tel", tel);
                addressList.add(map);
            }
            mActivity.hideProcessDialog(0);
            if (jsonArray.length()==0) {
                mActivity.hideProcessDialog(1);
                rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> getData());
                lv_address_select.setVisibility(View.GONE);
            }
        } else {

        }
    }

    private void showAlertDialog(Map map) {
        View view = View.inflate(mActivity, R.layout.dialog_express_adress_select, null);
        AlertDialog.Builder builder = new Builder(mActivity);
        builder.setView(view);
        TextView tv_express_title = (TextView) view.findViewById(R.id.tv_express_title);
        TextView tv_address_name = (TextView) view.findViewById(R.id.tv_address_name);
        TextView tv_phone_number = (TextView) view.findViewById(R.id.tv_phone_number);
        Button b_commit_know = (Button) view.findViewById(R.id.b_commit_know);
        tv_express_title.setText((String) map.get("name"));
        tv_address_name.setText((String) map.get("addr"));
        tv_phone_number.setText((String) map.get("tel"));
        b_commit_know.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
    }

    /**
     * 定义一个适配器的内部ViewHolder类
     */
    private static class ViewHolder {
        private ImageView iv_tickpng;
        private TextView tv_name;
        private TextView tv_address;
        private TextView tv_phone;
    }

    private class AddressSelectListViewAdapter extends BaseAdapter {
        private List list;
        private BaseActivity mActivity;
        private LayoutInflater inflater;

        public AddressSelectListViewAdapter(BaseActivity mActivity, List list) {
            this.mActivity = mActivity;
            this.list = list;
            inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder viewHolder = null;
            if (convertView != null) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.user_extract_address, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, MainActivity.dp2px(mActivity, 100));
                view.setLayoutParams(lp);
                viewHolder = new ViewHolder();
                viewHolder.iv_tickpng = (ImageView) view.findViewById(R.id.iv_tickpng);
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_address);
                viewHolder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
                view.setTag(viewHolder);
            }
            final Map map = (Map) list.get(position);
            final String addressIdString = (String) map.get("id");
            final String addressNameString = (String) map.get("name");
            viewHolder.tv_name.setText((String) map.get("name"));
            viewHolder.tv_address.setText("地址:" + map.get("addr"));
            viewHolder.tv_phone.setText("电话:" + map.get("tel"));
            if (addressId.equals(addressIdString)) {
                viewHolder.iv_tickpng.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_tickpng.setVisibility(View.INVISIBLE);
            }
            view.setOnClickListener(view1 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Intent intent = new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putString("addressId", addressIdString);
                    bundle.putString("addressName", addressNameString);
                    intent.putExtras(bundle);
                    getFragmentManager().popBackStack();
                    if (getTargetFragment()!=null){
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 110, intent);
                    }
                }
            });
            view.setOnLongClickListener(view12 -> {
                showAlertDialog(map);
                return true;
            });
            return view;
        }
    }
}
