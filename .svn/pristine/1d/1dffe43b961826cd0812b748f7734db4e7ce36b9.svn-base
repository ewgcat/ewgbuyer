package com.ewgvip.buyer.android.activity;

import android.os.Bundle;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.adapter.PhotoPagerAdapter;
import com.ewgvip.buyer.android.layout.MyGallery;

/**
 * 图片展示
 */
public class PhotoShowActivity extends BaseActivity {
    //自定义Gallery
    private MyGallery myGallery;
    //图片预览适配器
    private PhotoPagerAdapter photoPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_show);
        myGallery = (MyGallery) findViewById(R.id.gallery_photo_show);
        //初始化适配器参数：上下文，图片地址集合
        photoPagerAdapter = new PhotoPagerAdapter(this, getIntent().getStringArrayListExtra("list"));
        myGallery.setAdapter(photoPagerAdapter);
        //galler初始显示图片
        myGallery.setSelection(getIntent().getIntExtra("position", 0));
    }
}
