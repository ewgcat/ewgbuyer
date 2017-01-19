package com.ewgvip.buyer.android.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;

/**
 * Created by Administrator on 2015/11/27.
 */
public class PhotoDialog extends Dialog {
    BaseActivity context;

    public PhotoDialog(BaseActivity context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        windowDeploy(0, 0);
    }

    //dialog宽度
    public void windowDeploy(int x, int y) {
        Window window = getWindow();
        window.setWindowAnimations(R.style.AnimBottom); // 设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.transparent); // 设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        // 根据x，y坐标设置窗口需要显示的位置
        wl.gravity = Gravity.BOTTOM; // 设置重力
        wl.x = x; // x小于0左移，大于0右移
        wl.y = y; // y小于0上移，大于0下移
        // wl.alpha = 0.6f; //设置透明度
        window.setAttributes(wl);
        WindowManager m = context.getWindowManager();//
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用//
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值//
        p.width = d.getWidth(); // 宽度设置
        window.setAttributes(p);
    }
}
