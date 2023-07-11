package com.yinglan.scrolllayout.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserinfoActivity extends AppCompatActivity {
    private UserInfo userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        ImageView imgview = (ImageView) findViewById(R.id.edit_shoucang);
        TextView yhmtextView = (TextView) findViewById(R.id.yonghuxingming);
        userinfo = (UserInfo) getApplication();
        try {
            JSONObject jsonuserinfo = new  JSONObject(userinfo.getdata());
            yhmtextView.setText(jsonuserinfo.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dddddddd","userinfo");
                startActivity(new Intent(UserinfoActivity.this,EditUserInfoActivity.class));
            }
        });;
    }

    public void onClick(View view) {
        startActivity(new Intent(UserinfoActivity.this,EditUserInfoActivity.class));
        Log.d("dddddddd",userinfo.getdata());
    }
}
