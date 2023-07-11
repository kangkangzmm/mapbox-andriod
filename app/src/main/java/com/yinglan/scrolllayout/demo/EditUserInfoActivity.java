package com.yinglan.scrolllayout.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.File;

public class EditUserInfoActivity extends AppCompatActivity {
    private static final int CUT_OK = 0x0013;
    private static final int CAMERA_REQUEST = 0x0012;

    private File tempFile;
    private ImageView iv_avatar;

    private ImageView iv_back;
    private EditText tv_name;
    private EditText tv_sex;
    private EditText tv_dianwei;
    private EditText tv_dianhua;
    private EditText tv_youxiang;
    private EditText zhiwei;

    private TextView baocun;


    //WebServer服务变量定义
    private static String SOAP_ACTION="*******";
    private static String NAMESPACE="**********";
    private static String METHON_NAME="*********";
    private static String URL="************";


    private static String SOAP_ACTION1="***************";
    private static String NAMESPACE1="****************";
    private static String METHON_NAME1="*************";
    private static String URL1="******************";


    //获取登陆的用户和密码
    private SharedPreferences sharedPreferences;

    private String yonghuming;
    private String mima;


    private String[]sexArry=new String[]{"男","女"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_userinfo);

        iv_avatar=(ImageView)findViewById(R.id.iv_avatar);


        RelativeLayout re_sex=(RelativeLayout)findViewById(R.id.re_sex);
        re_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(EditUserInfoActivity.this);
                builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_sex.setText(sexArry[which]);
                        dialog.dismiss();

                    }
                });
                builder.show();

            }
        });





        RelativeLayout re_avatar=(RelativeLayout)findViewById(R.id.re_avatar);
        re_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });





        iv_back=(ImageView)findViewById(R.id.iv_back);
        tv_name=(EditText)findViewById(R.id.tv_name);
        tv_dianhua=(EditText)findViewById(R.id.tv_dianhua);
        tv_dianwei=(EditText)findViewById(R.id.tv_dianwei);
        tv_sex=(EditText)findViewById(R.id.tv_sex);
        tv_youxiang=(EditText)findViewById(R.id.tv_youxiang);
        zhiwei=(EditText)findViewById(R.id.zhiwei);
        baocun=(TextView)findViewById(R.id.baocun);
        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();

            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();






    }
    private void clipImage(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 数据 uri 代表裁剪哪一张
        intent.setDataAndType(uri, "image/*");
        // 传递数据
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        // 你待会裁剪完之后需要获取数据   startActivityForResult
        startActivityForResult(intent, CUT_OK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            switch (requestCode){
                case 1:
                    Uri uri=data.getData();
                    clipImage(uri);

                    break;
                case CUT_OK:
                    Bundle extras=data.getExtras();
                    if (extras!=null)
                    {
                        Bitmap bitmap=extras.getParcelable("data");
                        iv_avatar.setImageBitmap(bitmap);
                        //上传图片到服务器，以file形式

                        //saveBitmapToFile(bitmap);

                    }
                    break;
                case CAMERA_REQUEST:
                    clipImage(Uri.fromFile(tempFile));

            }
        }
    }
}
