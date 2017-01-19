package com.ewgvip.buyer.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.fragment.Cart1Fragment;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:39
 * Description:  满就送选择赠品
 */
public class ChooseGiftDialog {

    final Dialog dlg;
    String chosen = null;
    CheckBox temp;
    TextView content;
    BaseActivity mActivity;
    String selected_cartids;

    public ChooseGiftDialog(BaseActivity home, List list, String str, final Cart1Fragment fragment) {
        this.mActivity = home;
        this.selected_cartids = str;
        dlg = new Dialog(mActivity, R.style.AlertDialog);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_cart_gift_list, null);
        layout.setMinimumWidth(mActivity.getScreenWidth());
        content = (TextView) layout.findViewById(R.id.gift_chosen_text);
        final Button button = (Button) layout.findViewById(R.id.gift_submit);
        button.setOnClickListener(v -> {
            if (chosen != null && !chosen.equals("")) {
                Map Paramap = new HashMap();
                Paramap.put("gift_id", chosen);
                Paramap.put("cart_ids", selected_cartids);
                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/add_cart_gift.htm",
                        result -> {
                            try {
                                if (result.getInt("ret") == 100) {
                                    dlg.dismiss();
                                } else {
                                    Toast.makeText(mActivity, "选择失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> mActivity.hideProcessDialog(1), Paramap);
                mRequestQueue.add(request);

            }
        });

        GridView gridview = (GridView) layout
                .findViewById(R.id.content_list);
                /*
                 * if (list.size() <= 3) { gridview.setLayoutParams(new
				 * LayoutParams( LayoutParams.MATCH_PARENT,
				 * LayoutParams.WRAP_CONTENT)); }
				 */
        AlertAdapter adapter = new AlertAdapter(mActivity, list);
        gridview.setAdapter(adapter);

        // set a large value put it in bottom
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setOnDismissListener(dialog -> fragment.refresh());
        dlg.setContentView(layout);


    }

    public void show() {
        dlg.show();
    }

    class AlertAdapter extends BaseAdapter {
        private List items;
        private BaseActivity mActivity;
        private LayoutInflater inflater;

        public AlertAdapter(BaseActivity mActivity, List items) {
            this.mActivity = mActivity;
            this.items = items;
            inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_cart_gift, null);
                holder.img = (SimpleDraweeView) convertView.findViewById(R.id.gift_img);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                holder.name = (TextView) convertView.findViewById(R.id.gift_name);
                holder.price = (TextView) convertView.findViewById(R.id.gift_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Map map = (Map) items.get(position);
            holder.name.setText(map.get("goods_name").toString());
            holder.price.setText("￥" + map.get("goods_price"));
            holder.checkBox.setTag(map.get("goods_id"));
            final CheckBox check = holder.checkBox;
            BaseActivity.displayImage(CommonUtil.getAddress(mActivity) + "/" + map.get("goods_main_photo"), holder.img);

            check.setClickable(false);
            convertView.setOnClickListener(v -> {
                String tag = map.get("goods_id").toString();
                if (chosen != null && !chosen.equals("")) {
                    if (chosen.equals(tag)) {
                        chosen = "";
                        check.setChecked(false);
                        content.setText("已选择0/1件");
                    } else {
                        temp.setChecked(false);
                        chosen = tag;
                        check.setChecked(true);
                        temp = check;
                        content.setText("已选择1/1件");
                    }
                } else {
                    chosen = tag;
                    check.setChecked(true);
                    temp = check;
                    content.setText("已选择1/1件");
                }
            });

            return convertView;
        }

        class ViewHolder {
            SimpleDraweeView img;
            CheckBox checkBox;
            TextView name;
            TextView price;

        }
    }
}
