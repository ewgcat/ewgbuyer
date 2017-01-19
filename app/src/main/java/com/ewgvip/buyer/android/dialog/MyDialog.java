package com.ewgvip.buyer.android.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.AlertDialogAdapter;
import com.ewgvip.buyer.android.adapter.InventoryDialogAdapter;

import java.util.List;


/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:40
 * Description:  自定义对话框
 */
public final class MyDialog {


    public static Dialog showAlert(final BaseActivity context, final String title, final String message, String confirm, final View.OnClickListener confirm_listener, String cancel, OnCancelListener cancelListener) {
        final Dialog dlg = new Dialog(context, R.style.my_dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);
        TextView textView = (TextView) layout.findViewById(R.id.title);
        textView.setText(title);
        textView = (TextView) layout.findViewById(R.id.message);
        textView.setText(message);

        layout.findViewById(R.id.button_cancel).setOnClickListener(v -> dlg.cancel());
        layout.findViewById(R.id.button_confirm).setOnClickListener(confirm_listener);

        dlg.setCanceledOnTouchOutside(true);
        if (cancelListener != null) {
            dlg.setOnCancelListener(cancelListener);
        }
        dlg.setContentView(layout);
        dlg.show();


        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = screenW * 7 / 8;
        w.setAttributes(lp);
        return dlg;
    }


    public static Dialog showAlert(final BaseActivity context, final String title, final String[] items, final OnItemClickListener onItemClickListener) {
        final Dialog dlg = new Dialog(context, R.style.my_dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);
        TextView textView = (TextView) layout.findViewById(R.id.title);
        textView.setText(title);
        layout.findViewById(R.id.layout_button).setVisibility(View.GONE);
        layout.findViewById(R.id.layout_message).setVisibility(View.GONE);
        final ListView list = (ListView) layout.findViewById(R.id.layout_listview);
        list.setVisibility(View.VISIBLE);
        AlertDialogAdapter adapter = new AlertDialogAdapter(context, items);
        list.setAdapter(adapter);

        list.setOnItemClickListener(onItemClickListener);

        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        dlg.show();


        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = screenW * 7 / 8;
        w.setAttributes(lp);
        return dlg;
    }

    /**
     * 提交订单时库存不足和订单详情组合、满送弹出的dialog
     *
     * @param context
     * @param title
     * @param dataList
     * @param back_to_cart
     * @param back_to_cart_listener
     * @param address_edit
     * @param address_edit_listener
     * @param type                  0表示库存不足时，1表示组合满送时
     * @return
     */
    public static Dialog showAlert(final BaseActivity context, final String title, final List dataList, String back_to_cart, final View.OnClickListener back_to_cart_listener, String address_edit, final View.OnClickListener address_edit_listener, int type) {
        final Dialog dlg = new Dialog(context, R.style.my_dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);
        View view_bg = layout.findViewById(R.id.view_bg);
        Button button_cancel = (Button) layout.findViewById(R.id.button_cancel);
        button_cancel.setText(back_to_cart);
        button_cancel.setOnClickListener(back_to_cart_listener);
        Button button_confirm = (Button) layout.findViewById(R.id.button_confirm);
        button_confirm.setText(address_edit);
        button_confirm.setOnClickListener(address_edit_listener);
        if (type == 0) {
            button_cancel.setVisibility(View.VISIBLE);
            button_confirm.setVisibility(View.VISIBLE);
            view_bg.setVisibility(View.VISIBLE);
        } else {
            button_cancel.setVisibility(View.GONE);
            view_bg.setVisibility(View.GONE);
            button_confirm.setVisibility(View.VISIBLE);
        }
        TextView textView = (TextView) layout.findViewById(R.id.title);
        textView.setText(title);
        layout.findViewById(R.id.layout_message).setVisibility(View.GONE);
        final ListView list = (ListView) layout.findViewById(R.id.layout_listview);
        list.setVisibility(View.VISIBLE);
        InventoryDialogAdapter inventoryDialogAdapter = new InventoryDialogAdapter(context, dataList, type);
        list.setAdapter(inventoryDialogAdapter);
        ListAdapter listAdapter = list.getAdapter();
        ViewGroup.LayoutParams params = list.getLayoutParams();
        if (listAdapter.getCount() < 2) {
            View listItem = listAdapter.getView(0, null, list);
            listItem.measure(0, 0);
            params.height = listItem.getMeasuredHeight() * listAdapter.getCount();
        } else {
            View listItem = listAdapter.getView(0, null, list);
            listItem.measure(0, 0);
            params.height = BaseActivity.dp2px(context, listItem.getMeasuredHeight() * 2);
        }
        list.setLayoutParams(params);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        dlg.show();


        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = screenW * 7 / 8;
        w.setAttributes(lp);
        return dlg;
    }

}
