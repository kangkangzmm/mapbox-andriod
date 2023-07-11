package com.yinglan.scrolllayout.demo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yinglan.scrolllayout.demo.R;
import com.yinglan.scrolllayout.demo.util.SystemUtil;
import com.yinglan.scrolllayout.demo.util.permission.KbPermission;
import com.yinglan.scrolllayout.demo.util.permission.KbPermissionListener;
import com.yinglan.scrolllayout.demo.util.permission.KbPermissionUtils;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

public class PicMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mUploadPicture; //图片上传
    private Button mUploadVideo; //视频上传

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_postpic);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SystemUtil.setLightStatusBar(this, Color.WHITE);
        }

        mContext = this;

        FrameLayout backLayout = findViewById(R.id.btn_back);
        backLayout.setVisibility(View.GONE);
        TextView toolbarTitle = findViewById(R.id.tv_toolbar_title);
        toolbarTitle.setText(R.string.main_activity_title);
        mUploadPicture = findViewById(R.id.btn_upload_picture);
        mUploadPicture.setOnClickListener(this);
        mUploadVideo = findViewById(R.id.btn_upload_video);
        mUploadVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {

        if (KbPermissionUtils.needRequestPermission()) {
            KbPermission.with(this)
                    .requestCode(200)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callBack(new KbPermissionListener() {
                        @Override
                        public void onPermit(int requestCode, String... permission) {
                            if (v == mUploadPicture) {
                                startUploadPictureActivity();
                            } else if (v == mUploadVideo) {
                                startUploadVideoActivity();
                            }
                        }

                        @Override
                        public void onCancel(int requestCode, String... permission) {
                            KbPermissionUtils.goSetting(mContext);
                        }
                    })
                    .send();
        } else {
            if (v == mUploadPicture) {
                startUploadPictureActivity();
            } else if (v == mUploadVideo) {
                startUploadVideoActivity();
            }
        }
    }

    private void startUploadPictureActivity() {
        Intent uploadPictureIntent = new Intent(mContext, PictureSelectActivity.class);
        startActivity(uploadPictureIntent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }

    private void startUploadVideoActivity() {
        Intent uploadVideoIntent = new Intent(mContext, VideoSelectActivity.class);
        startActivity(uploadVideoIntent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }
}
