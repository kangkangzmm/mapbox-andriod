package com.yinglan.scrolllayout.demo;



import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yinglan.scrolllayout.demo.doubleTouchTool.ZoomImageView;

public class DoubleTouchImageViewActivity extends AppCompatActivity {

    private ZoomImageView zoomImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreenimg);

        init();
    }


    /**
     * 初始化
     * */
    private void init(){
        zoomImageView = findViewById(R.id.zoomImageView);
        Bundle bundle = getIntent().getBundleExtra("fullimgurl");
        String fullimgurl = bundle.getString("fullimgurl");
        Glide.with(DoubleTouchImageViewActivity.this)
                .load((fullimgurl.replace("\"","")))
                .into(zoomImageView);
        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"点击当前图片",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
