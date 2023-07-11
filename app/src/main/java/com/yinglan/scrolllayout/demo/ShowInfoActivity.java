package com.yinglan.scrolllayout.demo;

import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ShowInfoActivity extends AppCompatActivity {
    private String stream_url = "http://kangkangtk.gnway.cc/data/video/c.mp4";

    private VideoView vv_vidao_view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_info_pic);


        initView();


        vv_vidao_view = (VideoView) findViewById(R.id.vv_vidao_view);

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


    private void initView(){
        Bundle bundle = getIntent().getBundleExtra("info_properties");
        String imguri = bundle.getString("imgurl");
        imguri = "http://kangkangtk.gnway.cc/attachment?picId="+imguri;
        TextView editText1=(TextView)findViewById(R.id.editText1);
        TextView editText2=(TextView)findViewById(R.id.editText2);
        TextView editText3=(TextView)findViewById(R.id.editText3);
        Button exitbutton  = (Button)findViewById(R.id.exitbutton1);
        ImageView imageView = (ImageView)findViewById(R.id.img_shoucang);
        exitbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        Intent fullimg = new Intent();
        Bundle fullimgurl = new Bundle();
        fullimgurl.putString("fullimgurl",imguri);
        fullimg.setClass(ShowInfoActivity.this, DoubleTouchImageViewActivity.class);
        fullimg.putExtra("fullimgurl",fullimgurl);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(fullimg);
//                startActivity(new Intent(ShowInfoActivity.this, DoubleTouchImageViewActivity.class));
            }
        });
        editText1.setText(bundle.getString("name"));
        editText2.setText(bundle.getString("address"));
        editText3.setText(bundle.getString("phone"));
        Glide.with(ShowInfoActivity.this)
                .load(Uri.parse(imguri.replace("\"","")))
                .into(imageView);
//        imageView.setImageURI(Uri.parse(imguri.replace("\"","")));
    }


}
