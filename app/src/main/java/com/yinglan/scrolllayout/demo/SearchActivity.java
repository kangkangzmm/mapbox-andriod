package com.yinglan.scrolllayout.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;


import com.yinglan.scrolllayout.demo.search.mmSearchLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends Activity {
    private mmSearchLayout msearchLy;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seartch);
        msearchLy = (mmSearchLayout)findViewById(R.id.msearchlayout);
//        listView=(ListView) findViewById(R.id.list_view);
//        String[] data={"菠萝","芒果","石榴","葡萄", "苹果", "橙子", "西瓜","菠萝","芒果","石榴","葡萄", "苹果", "橙子", "西瓜","菠萝","芒果","石榴","葡萄", "苹果", "橙子", "西瓜"};
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
//        ListView listView = findViewById(R.id.searchlist_view);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(SearchDemoActivity.this, "您选择的水果是：" + data[i], Toast.LENGTH_SHORT).show();
//            }
//        });

        initData();
    }

    protected void initData() {
        String shareData = "柯桥区安昌,山西菜";
        List<String> skills = Arrays.asList(shareData.split(","));

        String shareHotData ="柯桥区安昌,苏菜";
        List<String> skillHots = Arrays.asList(shareHotData.split(","));

        msearchLy.initData(skills, skillHots, new mmSearchLayout.setSearchCallBackListener() {
            @Override
            public void Search(String str) {
                //进行或联网搜索
            }
            @Override
            public void Back() {
                finish();
            }

            @Override
            public void ClearOldData() {
                //清除历史搜索记录  更新记录原始数据
            }
            @Override
            public void SaveOldData(ArrayList<String> AlloldDataList) {
                //保存所有的搜索记录
            }
        });
    }
}