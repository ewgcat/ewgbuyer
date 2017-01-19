package com.ewgvip.buyer.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;

/**
 * Created by Administrator on 2016/7/27.
 */
public class DownloadDialog extends AlertDialog {

    private Context context;
    private ProgressBar progressBar;
    private TextView tv_progress;
    private Handler mHandler;

    public DownloadDialog(Context context) {
        this(context, 0);
        this.context=context;
    }

    public DownloadDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.download_dialog, null);


        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tv_progress = (TextView) view.findViewById(R.id.tv_progress);
        mHandler = new Handler(msg -> {
            if (msg.what==1){
                String progresstext= (String) msg.obj;
                tv_progress.setText(progresstext);
            }
            return false;
        });
        setContentView(view);
    }

    public void setProgress(int progress,String progresstext) {
        progressBar.setProgress(progress);
        Message msg=Message.obtain();
        msg.what=1;
        msg.obj=progresstext;
        mHandler.sendMessage(msg);
    }

}
