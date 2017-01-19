package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.zxing.camera.CameraManager;
import com.ewgvip.buyer.android.zxing.decoding.CaptureActivityHandler;
import com.ewgvip.buyer.android.zxing.decoding.InactivityTimer;
import com.ewgvip.buyer.android.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扫描二维码
 */

public class QRScanFragment extends Fragment
        implements
        Callback,
        View.OnClickListener {

    public static final float BEEP_VOLUME = 0.10f;
    public static final int REQUEST_CODE = 100;
    public static final int PARSE_BARCODE_SUC = 300;
    public static final int PARSE_BARCODE_FAIL = 303;
    public static final long VIBRATE_DURATION = 200L;
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private Bitmap scanBitmap;
    private BaseActivity mActivity;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_qr_scan, container, false);
        mActivity = (BaseActivity) getActivity();
        CameraManager.init(mActivity.getApplication());
        viewfinderView = (ViewfinderView) rootView.findViewById(R.id.viewfinder_view);
        Button mButtonBack = (Button) rootView.findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(this);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(mActivity);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) rootView
                .findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        playBeep = true;
        AudioManager audioService = (AudioManager) mActivity
                .getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        onResultHandler(resultString, barcode);
    }

    private void onResultHandler(String resultString, Bitmap bitmap) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(mActivity, "扫描失败！", Toast.LENGTH_SHORT).show();
            return;
        } else {

            String reg = "goods_(\\d)+";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(resultString.toLowerCase());
            if (matcher.find()) {
                String id = matcher.group().replace("goods_", "");
                this.getFragmentManager().popBackStack();
                mActivity.go_goods(id);
            }
            reg = "iskyshop_login\\.htm\\?qr_session_id=([A-Za-z0-9_-])+";
            pattern = Pattern.compile(reg);
            matcher = pattern.matcher(resultString.toLowerCase());
            if (matcher.find()) {
                String qr_session_id = matcher.group().replace(
                        "iskyshop_login.htm?qr_session_id=", "");
                if (mActivity.islogin()) {
                    this.getFragmentManager().popBackStack();
                    mActivity.go_qr_login(qr_session_id);
                }else {
                    mActivity.go_login();
                }
            }

            Log.i("test",resultString);
            //扫描邀请码，跳到注册界面
            if(resultString.contains("/spread/")){
                String replace = resultString.replace(".htm", "");
                String[] split = replace.split("/");
               String invitationCode=split[split.length-1];
                Bundle bundle=new Bundle();
                bundle.putString("invitationCode",invitationCode);
                mActivity.go_regist(bundle);
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(QRScanFragment.this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            mActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
}