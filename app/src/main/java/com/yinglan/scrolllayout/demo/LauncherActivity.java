package com.yinglan.scrolllayout.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.yinglan.scrolllayout.demo.core.RtcMainActivity;
import com.yinglan.scrolllayout.demo.core.base.RtcBaseActivity;
import com.yinglan.scrolllayout.demo.core.consts.Urls;
import com.yinglan.scrolllayout.demo.core.socket.IUserState;
import com.yinglan.scrolllayout.demo.core.socket.SocketManager;

public class LauncherActivity extends RtcBaseActivity implements IUserState {
    private EditText etUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        initView();

        if (SocketManager.getInstance().getUserState() == 1) {
            startActivity(new Intent(this, RtcMainActivity.class));
            finish();
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etUser = findViewById(R.id.et_user);
        String text = AppVdeo.getInstance().getUsername();
        etUser.setText(text);
    }

    @Override
    public void userLogin() {
        startActivity(new Intent(this, RtcMainActivity.class));
        finish();
    }

    @Override
    public void userLogout() {

    }

    public void enter(View view) {
        String username = etUser.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "please input your name", Toast.LENGTH_LONG).show();
            return;
        }
        // 设置用户名
        AppVdeo.getInstance().setUsername(username);
        // 添加登录回调
        SocketManager.getInstance().addUserStateCallback(this);
        // 连接socket:登录
        SocketManager.getInstance().connect(Urls.WS, username, 0);

    }
}

