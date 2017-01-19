package com.ewgvip.buyer.android.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.AlertDialogShowAdapter;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

/**
 * 填写订单时,填写发票信息
 * @author zhangmanyuan
 */
public class Cart2InvoiceFragment extends Fragment {

    private BaseActivity mActivity;
    private View rootView;
    private Boolean vatInvoice_boolean;
    private Button bt_details_invoice;
    private String[] invoiceString = null;
    private String companyname = "";
    private Boolean b=true;
    private LinearLayout ll_no_invoice;
    private   String invoiceTitle = "";
    private    Bundle bundle;
    private TextView   textView_title;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart2_invoice_info,
                container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("发票信息");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar

        ll_no_invoice = (LinearLayout) rootView.findViewById(R.id.ll_no_invoice);
        textView_title =(TextView)rootView.findViewById(R.id.invoiceTitle);

        bt_details_invoice = (Button) rootView.findViewById(R.id.bt_details_invoice);

        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单不可用
        bundle = getArguments();

        if (bundle.containsKey("vatInvoice")) {
            vatInvoice_boolean = bundle.getBoolean("vatInvoice");
        }


        if (bundle.containsKey("invoiceString")) {
            invoiceString = bundle.getStringArray("invoiceString");
            companyname = invoiceString[0];
        }
        //点击详情时，弹出对话框
        bt_details_invoice.setOnClickListener(view -> {

            final Dialog dlg = new Dialog(mActivity, R.style.my_dialog);
            LayoutInflater inflater1 = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout layout = (LinearLayout) inflater1.inflate(R.layout.alert_dialog_layout_show, null);
            TextView textView = (TextView) layout.findViewById(R.id.title_show);
            textView.setText("发票详情");
            layout.findViewById(R.id.layout_button_show).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.layout_message_show).setVisibility(View.GONE);
            final ListView list = (ListView) layout.findViewById(R.id.layout_listview_show);
            list.setVisibility(View.VISIBLE);
            AlertDialogShowAdapter adapter = new AlertDialogShowAdapter(mActivity, invoiceString);
            list.setAdapter(adapter);

            dlg.setCanceledOnTouchOutside(true);
            dlg.setContentView(layout);
            dlg.show();
            Button button_confirm = (Button) layout.findViewById(R.id.button_confirm_show);
            View v_line= layout.findViewById(R.id.v_line);
            Button button_cancel = (Button) layout.findViewById(R.id.button_cancel_show);
            button_confirm.setOnClickListener(view1 -> dlg.dismiss());
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height=BaseActivity.dp2px(mActivity,40);
            button_confirm.setLayoutParams(layoutParams);
            v_line.setVisibility(View.GONE);
            button_cancel.setVisibility(View.GONE);


        });

        String invoiceType = bundle.getString("invoiceType");
        final RadioGroup rg = (RadioGroup) rootView
                .findViewById(R.id.radiogroup);
        if (invoiceType.equals("普通发票")) {
            rg.check(R.id.regularInvoice);
        }
        if (invoiceType.equals("专用发票")) {
            rg.check(R.id.valueAddedTaxInvoice);
        }

        final EditText et = (EditText) rootView.findViewById(R.id.invoiceTitle);
        RadioButton radioButton = (RadioButton) rootView.findViewById(R.id.valueAddedTaxInvoice);
        RadioButton radioButton1 = (RadioButton) rootView.findViewById(R.id.regularInvoice);
        radioButton1.setOnClickListener(view -> {
            invoiceTitle = bundle.getString("invoiceTitle");
            et.setText(invoiceTitle);
            bt_details_invoice.setVisibility(View.GONE);
            textView_title.setFocusable(true);
            textView_title.requestFocus();
            textView_title.setClickable(true);
        });

        if (vatInvoice_boolean == false) {
            rootView.findViewById(R.id.valueAddedTaxInvoice).setFocusable(false);
            rootView.findViewById(R.id.valueAddedTaxInvoice).setClickable(false);
            ll_no_invoice.setVisibility(View.VISIBLE);

        } else {
            rootView.findViewById(R.id.valueAddedTaxInvoice).setFocusable(true);
            rootView.findViewById(R.id.valueAddedTaxInvoice).setClickable(true);

            radioButton.setOnClickListener(view -> {
                et.setText(companyname);
                bt_details_invoice.setVisibility(View.VISIBLE);
                textView_title.clearFocus();
                textView_title.setFocusable(false);
                textView_title.setClickable(false);
            });
        }
        if (radioButton.isChecked()) {
            invoiceTitle = companyname;
            et.setText(invoiceTitle);

            textView_title.setFocusable(false);
            textView_title.setClickable(false);

        } else {
            invoiceTitle = bundle.getString("invoiceTitle");
            et.setText(invoiceTitle);
            textView_title.setFocusable(true);
            textView_title.setClickable(true);
        }



        rootView.findViewById(R.id.yes).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (getTargetFragment() == null)
                    return;
                RadioButton rb = (RadioButton) rootView.findViewById(rg.getCheckedRadioButtonId());
                Bundle b1 = new Bundle();
                b1.putString("invoiceType", rb.getText() + "");
                b1.putString("invoiceTitle", et.getText() + "");
                Intent i = new Intent();
                i.putExtras(b1);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Cart2Fragment.INVOICE, i);
                getFragmentManager().popBackStack();
            }
        });
        return rootView;
    }
}
