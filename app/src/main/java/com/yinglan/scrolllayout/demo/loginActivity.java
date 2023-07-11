package com.yinglan.scrolllayout.demo;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yinglan.scrolllayout.demo.util.DBOpenHelper;
import com.yinglan.scrolllayout.demo.util.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import okhttp3.Call;

public class loginActivity extends AppCompatActivity {

    private DBOpenHelper mDBOpenHelper;
    private UserInfo userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        ButterKnife.bind(this);
        mDBOpenHelper = new DBOpenHelper(this);
        findViewById(R.id.other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {startActivity(new Intent(loginActivity.this,Other3rd.class));
            }
        });

    }



//    @BindView(R.id.iv_loginactivity_back)
//    ImageView mIvLoginactivityBack;
//    @BindView(R.id.tv_loginactivity_register)
//    TextView mTvLoginactivityRegister;
//    @BindView(R.id.rl_loginactivity_top)
//    RelativeLayout mRlLoginactivityTop;
//    @BindView(R.id.et_loginactivity_username)
//    EditText mEtLoginactivityUsername;
//    @BindView(R.id.et_loginactivity_password)
//    EditText mEtLoginactivityPassword;
//    @BindView(R.id.ll_loginactivity_two)
    LinearLayout mLlLoginactivityTwo;
//    @BindView(R.id.tv_loginactivity_forget)
//    TextView mTvLoginactivityForget;
//    @BindView(R.id.tv_loginactivity_check)
//    TextView mTvLoginactivityCheck;
//    @BindView(R.id.tv_loginactivity_else)
//    TextView mBtLoginactivityElse;

//    @OnClick({
//            // R.id.iv_loginactivity_back,
//            R.id.tv_loginactivity_register,
//            //R.id.tv_loginactivity_forget,
//            //R.id.tv_loginactivity_check,
//            R.id.bt_loginactivity_login,
//            //R.id.tv_loginactivity_else
//    })

    public void onClick(View view) {
        TextView nameview = (TextView) findViewById(R.id.et_loginactivity_username);
        TextView passwordview = (TextView) findViewById(R.id.et_loginactivity_password);

        switch (view.getId()) {

            case R.id.tv_loginactivity_register:

                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.iv_loginactivity_back:
                startActivity(new Intent(this, NaviRouteActivity.class));
                finish();
                break;
            case R.id.bt_loginactivity_login:
                String name = nameview.getText().toString().trim();
                String password = passwordview.getText().toString().trim();
                userinfo = (UserInfo) getApplication();
                //测试网络请求
                String url = "http://kangkangtk.gnway.cc:80/authentication/form";
                OkHttpUtils.post().url(url)
                        .addParams("username", name)
                        .addParams("password", password).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                            }

                            @Override
                            public void onResponse(String response, int id) {

                                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                                    ArrayList<User> data = mDBOpenHelper.getAllData();
                                    try {
                                        JSONObject jsonobj = new  JSONObject(response);
                                        boolean match = true;
                                        for(int i=0;i<data.size();i++) {
                                            User user = data.get(i);
                                            if (jsonobj.getString("message") .equals("登录成功") ){
                                                String pathurl = "http://kangkangtk.gnway.cc/oauth/token";
                                                OkHttpUtils.get().url(pathurl)
                                                        .addParams("username", name)
                                                        .addParams("password", password)
                                                        .addParams("grant_type","password" )
                                                        .addParams("client_secret", password)
                                                        .addParams("client_id", name).build().execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        try {
                                                            JSONObject jsontokenres = new  JSONObject(response);
                                                            userinfo.settoken(jsontokenres.getString("access_token"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                userinfo.setname(jsonobj.getString("message")+name);
                                                match = true;

                                                break;
                                            }else{
                                                match = false;
                                            }
                                        }
                                        if (match) {
                                            Toast.makeText(loginActivity.this, jsonobj.getString("data"), Toast.LENGTH_SHORT).show();
                                            userinfo.setdata(jsonobj.getString("data"));
//                                            Intent intent = new Intent(loginActivity.this, MainActivity.class);
//                                            startActivity(intent);
                                            startActivity(new Intent(loginActivity.this, UserinfoActivity.class));
                                            finish();//销毁这个Activity
                                        }else {
                                            Toast.makeText(loginActivity.this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(loginActivity.this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
                break;
            //case R.id.tv_loginactivity_else:
            //TODO 第三方登录，时间有限，暂时未实现
            //    break;
        }
    }

}



