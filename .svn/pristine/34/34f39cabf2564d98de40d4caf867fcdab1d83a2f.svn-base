package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.glideutils.GlideCircleTransform;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.rl_birthday;
import static com.ewgvip.buyer.android.R.id.rl_head_image_change;
import static com.ewgvip.buyer.android.R.id.rl_real_name;
import static com.ewgvip.buyer.android.R.id.rl_tv_gender;
import static com.ewgvip.buyer.android.R.id.tv_birthday;
import static com.ewgvip.buyer.android.R.id.tv_gender;
import static com.ewgvip.buyer.android.R.id.tv_real_name;

/**
 * 用户信息的修改页面
 */
public class UCInformationModify extends Fragment {
    public static final int MAKE_PHOTO = 0;
    public static final int SELECT_PHOTO = 1;
    //对话框
    Dialog alertDialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_head_icon)
    ImageView ivHeadIcon;
    @BindView(tv_real_name)
    TextView tvRealName;
    @BindView(tv_gender)
    TextView tvGender;
    @BindView(tv_birthday)
    TextView tvBirthday;

    private BaseActivity mActivity;
    private View rootView;

    private Calendar calendar;
    private String true_name = "";
    private String birthday = "";

    //选择
    private AlertDialog selectAlertDialog;


    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
        this.rootView = null;
        this.calendar = null;
        this.dateSet = null;
    }

    //日期设置匿名类
    DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mActivity.showProcessDialog();
            // 每次保存设置的日期
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear + 1);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            final String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            Map paraMap = mActivity.getParaMap();
            paraMap.put("birthday", str);
            RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().getJSONObject(mActivity.getAddress() + "/app/buyer/saveUser.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    try {
                        String code = jsonObject.has("code") ? jsonObject.getString("code") : "";
                        if ("100".equals(code)) {
                            tvBirthday.setText(str);
                            initData();
                        } else {
                            Toast.makeText(mActivity, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_user_information_modify, container, false);
        ButterKnife.bind(this, rootView);
        //设置标题
        toolbar.setTitle("用户信息");
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置导航点击事件
            @Override
            public void onClick(View v) {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(v);
                }
            }
        });
        //设置菜单可用
        setHasOptionsMenu(true);
        calendar = Calendar.getInstance();

        initData();

        return rootView;
    }



    /**
     * 页面数据
     */
    private void initData() {

        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/getUserMsg.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    String code = jsonObject.has("code") ? jsonObject.getString("code") : "";
                    if ("100".equals(code)) {
                        JSONObject userInformationObject = jsonObject.getJSONObject("result");
                        int gender = userInformationObject.has("gender") ? userInformationObject.getInt("gender") : -1;
                        birthday = userInformationObject.has("birthday") ? userInformationObject.getString("birthday") : "";
                        String photo = userInformationObject.has("photo") ? userInformationObject.getString("photo") : "";
                        true_name = userInformationObject.has("true_name") ? userInformationObject.getString("true_name") : "";
                        if (-1 == gender) {
                            tvGender.setText("保密");
                        } else if (0 == gender) {
                            tvGender.setText("女");
                        } else if (1 == gender) {
                            tvGender.setText("男");
                        } else {
                            tvGender.setText("未设置");
                        }
                        if ("".equals(birthday)) {
                            tvBirthday.setText("未设置");
                        } else {
                            tvBirthday.setText(birthday);
                        }
                        if ("".equals(photo)) {
                            String urlString = mActivity.getCache("user_image_photo_url");
                            Glide.with(mActivity).load(urlString).transform(new GlideCircleTransform(mActivity)).into(ivHeadIcon);

                        } else {
                            Glide.with(mActivity).load(photo).transform(new GlideCircleTransform(mActivity)).into(ivHeadIcon);
                        }
                        if ("".equals(true_name)) {
                            tvRealName.setText("未设置");
                        } else {
                            tvRealName.setText(true_name);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //选择图片对话框 返回用户信息界面
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO) {
            //获取返回图片路径并加到对应map集合中
            Bundle bundle = data.getExtras();
            ArrayList<String> updateURLList = bundle.getStringArrayList("updateURLList");
            ArrayList<String> idList = bundle.getStringArrayList("idList");
            String imageURL = updateURLList.get(0);
            String imageId = idList.get(0);

            Glide.with(mActivity).load(imageURL).transform(new GlideCircleTransform(mActivity)).into(ivHeadIcon);
            Map map = mActivity.getParaMap();
            map.put("img_id", imageId);
            RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/saveUser.htm", map, new BaseSubscriber<JSONObject>(mActivity) {
                @Override
                public void onNext(JSONObject jsonObject) {
                    try {
                        String code = jsonObject.has("code") ? jsonObject.getString("code") : "";
                        if ("100".equals(code)) {
                            initData();
                        } else {
                            Toast.makeText(mActivity, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //显示头像选择对话框
    private void showSelectPictureAlertDialog() {
        String[] arr = {"从相册中选择", "取消"};
        alertDialog = MyDialog.showAlert(mActivity, "操作", arr, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    if (FastDoubleClickTools.isFastDoubleClick()) {

                        Bundle bundle = new Bundle();
                        bundle.putInt("position", 0);
                        bundle.putInt("total", 0);
                        bundle.putInt("count", 1);
                        mActivity.go_select_photo_for_user_center_information_change_fragment(bundle, UCInformationModify.this);
                        alertDialog.dismiss();
                    }
                }
                if (i == 1) {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        alertDialog.dismiss();
                    }
                }
            }
        });

    }

    //显示真实姓名对话框
    private void showSelectRealNameAlertDialog() {
        View view = View.inflate(mActivity, R.layout.dialog_user_information_change_real_name, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        final EditText et_input_name = (EditText) view.findViewById(R.id.et_input_name);
        et_input_name.setText(true_name);
        Button b_dialog_dismiss = (Button) view.findViewById(R.id.b_dialog_dismiss);
        Button b_diglog_commit = (Button) view.findViewById(R.id.b_diglog_commit);
        b_diglog_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.hide_keyboard(view);
                    final String realNameString = et_input_name.getText().toString();
                    if ("".equals(realNameString)) {
                        Toast.makeText(mActivity, "请输入真实姓名", Toast.LENGTH_SHORT).show();
                    } else {
                        mActivity.showProcessDialog();
                        String address = getResources().getString(R.string.http_url);
                        String url = address + "/app/buyer/saveUser.htm";
                        Map paraMap = mActivity.getParaMap();
                        paraMap.put("true_name", realNameString);
                        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/saveUser.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                            @Override
                            public void onNext(JSONObject jsonObject) {
                                try {
                                    String code = jsonObject.has("code") ? jsonObject.getString("code") : "";
                                    if ("100".equals(code)) {
                                        tvRealName.setText(realNameString);
                                        //保存到本地首选项
                                        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                                        preferences.edit().putString("userName", realNameString).commit();
                                        initData();
                                        Toast.makeText(mActivity, "修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mActivity, "修改失败", Toast.LENGTH_SHORT).show();
                                    }
                                    mActivity.hideProcessDialog(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        selectAlertDialog.dismiss();
                    }
                }
            }
        });
        b_dialog_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.hide_keyboard(view);
                    selectAlertDialog.dismiss();
                }
            }
        });
        selectAlertDialog = builder.create();
        selectAlertDialog.setView(view, 0, 0, 0, 0);
        selectAlertDialog.show();
    }

    //显示性别对话框
    private void showSelectGenderAlertDialog() {
        String[] arr = {"男", "女", "保密"};
        alertDialog = MyDialog.showAlert(mActivity, "操作", arr, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Map paraMap = mActivity.getParaMap();
                    if (i == 0) {
                        paraMap.put("gender", "1");
                    } else if (i == 1) {
                        paraMap.put("gender", "0");
                    } else if (i == 2) {
                        paraMap.put("gender", "-1");
                    }

                    RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/saveUser.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                        @Override
                        public void onNext(JSONObject jsonObject) {
                            try {
                                String code = jsonObject.has("code") ? jsonObject.getString("code") : "";
                                if ("100".equals(code)) {
                                    initData();
                                } else {
                                    Toast.makeText(mActivity, "修改失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    alertDialog.dismiss();
                }
            }
        });
    }

    @OnClick({rl_head_image_change, rl_real_name, rl_tv_gender, rl_birthday})
    public void onClick(View view) {
        switch (view.getId()) {
            case rl_head_image_change:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    showSelectPictureAlertDialog();
                }
                break;
            case rl_real_name:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    showSelectRealNameAlertDialog();
                }
                break;
            case rl_tv_gender:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    showSelectGenderAlertDialog();
                }
                break;
            case rl_birthday:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    Date myDate = null;
                    if ("".equals(birthday)) {
                        myDate = new Date();
                    } else {
                        try {
                            myDate = format.parse(birthday);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    calendar.setTime(myDate);
                    String[] birth = birthday.split("-");

                    if (birthday.equals("")) {
                        //取得系统日期:
                        Calendar c = Calendar.getInstance();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        DatePickerDialog dlg = new DatePickerDialog(mActivity, dateSet, c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1), c.get(Calendar.DAY_OF_MONTH));
                        DatePicker datePicker = dlg.getDatePicker();
                        try {
                            datePicker.setMinDate(df.parse("1900-01-01").getTime());
                            datePicker.setMaxDate(df.parse(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH)).getTime());
                            dlg.show();
                        } catch (Exception e) {
                        }
                    } else {
                        DatePickerDialog dlg = new DatePickerDialog(mActivity, dateSet, Integer.parseInt(birth[0]), Integer.parseInt(birth[1]) - 1, Integer.parseInt(birth[2]));
                        DatePicker datePicker = dlg.getDatePicker();
                        //取得系统日期:
                        Calendar c = Calendar.getInstance();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            datePicker.setMinDate(df.parse("1900-01-01").getTime());
                            datePicker.setMaxDate(df.parse(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH)).getTime());
                            dlg.show();
                        } catch (Exception e) {
                        }
                    }
                }
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);
    }


    //菜单图文混合
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
