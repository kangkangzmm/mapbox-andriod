package com.yinglan.scrolllayout.demo;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yinglan.scrolllayout.demo.utils.HttpUtil;
import com.yinglan.scrolllayout.demo.utils.Utils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostPictureActivity extends AppCompatActivity implements View.OnClickListener {


    private Button xz;
    private Button sc;
    private ImageView iv_image;
    public static final int CHOOSE_PHOTO = 1;
    public static final int STORAGE_PERMISSION = 1;
    private File file=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_picture);
        //按钮
        xz = (Button) findViewById(R.id.xz);//选择照片按钮
        sc = (Button) findViewById(R.id.sc);//上传按钮
        //图片
        iv_image = (ImageView) findViewById(R.id.iv_image);//展示图片
        //设置点击事件监听
        xz.setOnClickListener(this);//选择
        sc.setOnClickListener(this);//上传

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xz:
                if (ContextCompat.checkSelfPermission(PostPictureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostPictureActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                } else {
                    xzImage();
                }
                break;
            case R.id.sc:
                scImage();
                break;
        }
    }




    private void xzImage() {

        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO); // 打开本地存储
    }

    private void scImage() {
        Log.d("file",file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator)+1));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
//                .addFormDataPart("name", "lisi")
                .addFormDataPart("file", file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator)+1), RequestBody.create(MediaType.parse("*/*"), file)) // 第一个参数传到服务器的字段名，第二个你自己的文件名，第三个MediaType.parse("*/*")数据类型，这个是所有类型的意思,file就是我们之前创建的全局file，里面是创建的图片
                .build();

        HttpUtil.uploadFile("http://kangkangtk.gnway.cc/attachment/upload", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("=============");
                System.out.println("异常：：");
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, "上传异常", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                showResponse(response.body().string());
                Log.d("response",response.body().string());
                //Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //选择图片后的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                //显示图片
                iv_image.setImageURI(data.getData());
                //System.out.println("图片在手机上的虚拟路径为："+data.getData());
                String realPath = Utils.getRealPath(this, data);
                file = new File(realPath);
                //System.out.println("图片在手机上的真实路径为："+realPath);
                break;
            default:
                break;
        }
    }


    //选择权限后的回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    xzImage();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    //ui操作，提示框
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                Toast.makeText(PostPictureActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

