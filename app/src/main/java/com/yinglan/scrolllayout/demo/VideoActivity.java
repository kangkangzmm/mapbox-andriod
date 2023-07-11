package com.yinglan.scrolllayout.demo;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    private String stream_url = "http://kangkangtk.gnway.cc/data/video/c.mp4";

    private VideoView vv_vidao_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplayer);
        vv_vidao_view = (VideoView) findViewById(R.id.vv_vidao_view);
        // 获取意图对象

        Intent intent = getIntent();

        //获取传递的值

        String str = intent.getStringExtra("videeoid");
        //设置值

        //设置播放的来源
        vv_vidao_view.setVideoPath(stream_url);
        vv_vidao_view.start();


        //实例化多媒体控制器
        MediaController mediaController=new MediaController(this);
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv_vidao_view.setVideoPath("http://kangkangtk.gnway.cc/data/video/a.mp4");
                //next button clicked
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv_vidao_view.setVideoPath("http://kangkangtk.gnway.cc/data/video/b.mp4");
                //previous button clicked
            }
        });
        mediaController.setMediaPlayer(vv_vidao_view);
        vv_vidao_view.setMediaController(mediaController);



    }




}
