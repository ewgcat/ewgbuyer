package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/13
 * 用户积分显示的列表适配器(ListView)
 */
public class UserIntegrationListViewAdapter extends BaseAdapter {

    /**
     * 环境变量
     */
    BaseActivity mActivity;
    /**
     * 保存传入的参数数据
     */
    private List list;
    /**
     * 积分状态
     */
    private Map integrationTypeMap = new HashMap();
    /**
     * 打气筒对象
     */
    private LayoutInflater inflater;
    private String integral_all;

    /**
     * 重写构造器
     */
    /**
     *
     * @param mActivity
     * @param list
     * @param integral_all  积分总数
     */
    public UserIntegrationListViewAdapter(BaseActivity mActivity, List list, String integral_all) {
        this.mActivity = mActivity;
        this.list = list;
        this.integral_all=integral_all;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        integrationTypeMap.put("reg", mActivity.getResources().getString(R.string.user_integration_type_reg));
        integrationTypeMap.put("system", mActivity.getResources().getString(R.string.user_integration_type_system));
        integrationTypeMap.put("login", mActivity.getResources().getString(R.string.user_integration_type_login));
        integrationTypeMap.put("order", mActivity.getResources().getString(R.string.user_integration_type_order));
        integrationTypeMap.put("integral_order", mActivity.getResources().getString(R.string.user_integration_type_integral_order));
    }


    @Override
    public int getCount() {
        return list.size() + 1;
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
        if (0 == position) {
            View titleView = inflater.inflate(R.layout.item_integral_detail_title, null);
            TextView tv_my_integral = (TextView) titleView.findViewById(R.id.tv_my_integral);


            tv_my_integral.setText(integral_all+"");
            Button b_integral_index = (Button) titleView.findViewById(R.id.b_integral_index);
            b_integral_index.setOnClickListener(view -> mActivity.go_integral_index());
            TextView tv_integral_order = (TextView) titleView.findViewById(R.id.tv_integral_order);
            tv_integral_order.setOnClickListener(view -> mActivity.go_order_integral_list());
            TextView tv_integral_time_detail = (TextView) titleView.findViewById(R.id.tv_integral_time_detail);
            if (1 <= list.size()) {
                tv_integral_time_detail.setText(mActivity.getResources().getString(R.string.integral_time_detail));
            } else if (0 == list.size()) {
                tv_integral_time_detail.setText(mActivity.getResources().getString(R.string.integral_time_null));
            }
            return titleView;
        } else {
            View view = null;
            ViewHolder viewHolder = null;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.item_detaillist, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_type = (TextView) view.findViewById(R.id.textview_detailname);
                viewHolder.tv_time = (TextView) view.findViewById(R.id.textview_detaildate);
                viewHolder.tv_integral = (TextView) view.findViewById(R.id.textview_detailvalue);
                view.setTag(viewHolder);
            }
            Map map = (Map) list.get(position - 1);
            String integrationString = "";
            Iterator iterator = integrationTypeMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String string = (String) entry.getKey();
                if ((map.get("type")).equals(string)) {
                    integrationString = (String) (integrationTypeMap.get(string));
                    viewHolder.tv_type.setText(integrationString);
                }
            }
            if ("".equals(integrationString)) {
                viewHolder.tv_type.setText((String) map.get("type"));
            }
            viewHolder.tv_time.setText((String) map.get("time"));
            String integralString = (String) map.get("integral");
            if (integralString.startsWith("-")) {
                viewHolder.tv_integral.setTextColor(mActivity.getResources().getColor(R.color.integral_minus));
                viewHolder.tv_integral.setText(integralString);
            } else {
                viewHolder.tv_integral.setTextColor(mActivity.getResources().getColor(R.color.integral_plus));
                viewHolder.tv_integral.setText("+" + integralString);
            }
            return view;
        }
    }

    /**
     * 定义一个适配器的内部ViewHolder类
     */
    private static class ViewHolder {
        private TextView tv_type;
        private TextView tv_time;
        private TextView tv_integral;
    }

}
