package com.yinglan.scrolllayout.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yinglan.scrolllayout.demo.util.Code;
import com.yinglan.scrolllayout.demo.util.DBOpenHelper;

import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private String realCode;
    private DBOpenHelper mDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
//        ButterKnife.bind(this);
        mDBOpenHelper = new DBOpenHelper(this);

        //将验证码用图片的形式显示出来
//        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
//        realCode = Code.getInstance().getCode().toLowerCase();
    }

//    @BindView(R.id.rl_registeractivity_top)
//    RelativeLayout mRlRegisteractivityTop;
//    @BindView(R.id.iv_registeractivity_back)
//    ImageView mIvRegisteractivityBack;
//    @BindView(R.id.ll_registeractivity_body)
//    LinearLayout mLlRegisteractivityBody;
//    @BindView(R.id.et_registeractivity_username)
//    EditText mEtRegisteractivityUsername;
//    @BindView(R.id.et_registeractivity_password1)
//    EditText mEtRegisteractivityPassword1;
//    @BindView(R.id.et_registeractivity_password2)
//    EditText mEtRegisteractivityPassword2;
//    @BindView(R.id.et_registeractivity_phoneCodes)
//    EditText mEtRegisteractivityPhonecodes;
//    @BindView(R.id.iv_registeractivity_showCode)
//    ImageView mIvRegisteractivityShowcode;
//    @BindView(R.id.rl_registeractivity_bottom)
//    RelativeLayout mRlRegisteractivityBottom;


    public void onClick(View view) {
        TextView usernameview = (TextView) findViewById(R.id.et_registeractivity_username);
        TextView userpasswordview1 = (TextView) findViewById(R.id.et_registeractivity_password1);
        TextView userpasswordview2 = (TextView) findViewById(R.id.et_registeractivity_password2);
        TextView userphoneview = (TextView) findViewById(R.id.et_registeractivity_phone2);
        TextView userphonecodeview = (TextView) findViewById(R.id.et_registeractivity_phoneCodes);
        ImageView mIvRegisteractivityShowcode = (ImageView)findViewById(R.id.iv_registeractivity_showCode);
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, loginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.iv_registeractivity_showCode:    //改变随机验证码的生成
                mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = usernameview.getText().toString().trim();
                String password = userpasswordview1.getText().toString().trim();
                String phone = userphoneview.getText().toString().trim();
                String jsonString = "";
                String phoneCode = userphonecodeview.getText().toString().toLowerCase();
                String rsgstr = "{\"role\":[1],\"newpassword1\":"+ password +","+"\"membername\":" +username+","+"\"phone\":"+phone+"}";
                Log.d("response", rsgstr);


                try {
                    JSONObject jsonObject = new JSONObject(rsgstr);
                    jsonString = jsonObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonString);
                OkHttpClient client = new OkHttpClient(); // 创建一个okhttp客户端对象
                Request request = new Request.Builder().post(body).url("http://kangkangtk.gnway.cc:80/rest/member").build();
                Call call = client.newCall(request); // 根据请求结构创建调用对象
                // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) { // 请求失败
                        Log.d("response", "请求失败");
                        // 回到主线程操纵界面
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException { // 请求成功.setText("调用登录接口返回：\n"+resp));
                        Log.d("response", response.toString());
                    }
                });


                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneCode) ) {
                    if (phoneCode.equals(realCode)) {
                        //将用户名和密码加入到数据库中
                        mDBOpenHelper.add(username, password);
                        Intent intent2 = new Intent(this, NaviRouteActivity.class);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this,  "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
