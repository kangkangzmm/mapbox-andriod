package com.yinglan.scrolllayout.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SeekActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_search);
        final EditText etContent = (EditText) findViewById(R.id.etContent);
        Button btnSeek = (Button) findViewById(R.id.btnSeek);
        btnSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etContent.getText().toString();
                Intent intent = new Intent(SeekActivity.this, MainActivity.class);
                startActivityForResult(intent,10086);

                Log.d("点击了搜索按钮","我是搜索框的搜索按钮");
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键处理
        if (keyCode == KeyEvent.KEYCODE_BACK)
            finish();
        return super.onKeyDown(keyCode, event);
    }
}
