package com.yinglan.scrolllayout.demo;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.activity.CaptureActivity;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.vision.VisionManager;
import com.yinglan.scrolllayout.ScrollLayout;
import com.yinglan.scrolllayout.demo.cimactivity.LoginActivity;
import com.yinglan.scrolllayout.demo.model.Address;
import com.yinglan.scrolllayout.demo.model.Constant;
import com.yinglan.scrolllayout.demo.model.Constantnew;
import com.yinglan.scrolllayout.demo.search.SearchSQLiteOpenHelper;
import com.yinglan.scrolllayout.demo.util.EventMessage;
import com.yinglan.scrolllayout.demo.viewpager.ListviewAdapter;
import com.yinglan.scrolllayout.demo.viewpager.MainPagerAdapter;
import com.yinglan.scrolllayout.demo.viewpager.RecyclerViewAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class NaviRouteActivity  extends AppCompatActivity implements
        com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener{

    private ScrollLayout mScrollLayout;
    private ArrayList<Address> mAllAddressList;
    private TextView mGirlDesText;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;

    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;




    //搜索框的
    private ImageView iv_searchBack;
    private EditText et_searchText;
    private Button sys_buttun;
    private TextView tv_historyText,tv_clearHistory;
    private List<String> searchList=new ArrayList<>();
    private int count=0;

    NavMapFragment fragment = new NavMapFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navimap);
        Mapbox.getInstance(getApplicationContext(), "sk.eyJ1Ijoia2FuZ2thbmdrIiwiYSI6ImNsZ2txajAzaTBhemYzcG5vbTB6M2hlMDAifQ.fN0T8NXrBc8VQhfjIy1XGg");

        VisionManager.init((Application) getApplicationContext(), "sk.eyJ1Ijoia2FuZ2thbmdrIiwiYSI6ImNsZ2txajAzaTBhemYzcG5vbTB6M2hlMDAifQ.fN0T8NXrBc8VQhfjIy1XGg");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        EventBus.getDefault().register(this);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout_nva);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        /**
         * 注册目标组件，让总线得到这个目标中的发布者和订阅者
         *
         */
        /**
         * 在 总线中注册，只有在注册了才能接收到事件
         */





        NavMapFragment newFragment = new NavMapFragment();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.map_fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();


        initGirlUrl();
        initView();
        setListeners();
    }


    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        mGirlDesText = (TextView) findViewById(R.id.text_view);
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.getBackground().setAlpha(0);
//        toolbar.setNavigationIcon(R.mipmap.action_bar_return);
//        toolbar.setTitle("ScrollLayout");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);

        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.getBackground().setAlpha(0);
        mScrollLayout.setIsSupportExit(false);






        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_view_seconfd);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,50,5);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));





        // find view
        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        // init fragment
//        mFragments = new ArrayList<>(4);
//        mFragments.add(BlankFragment.newInstance("今日"));
//        mFragments.add(BlankFragment.newInstance("记录"));
//        mFragments.add(BlankFragment.newInstance("通讯录"));
//        mFragments.add(BlankFragment.newInstance("设置"));
        // init view pager
        mAdapter = new NaviRouteActivity.MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);


//        WebView webview = (WebView) findViewById(R.id.webview);
//        WebSettings webSettings = webview.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webview.loadUrl("http://kangkangtk.gnway.cc/vue-mall/");

//        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this);
//        mainPagerAdapter.setOnClickItemListener(mOnClickItemListener);
//        viewPager.setAdapter(mainPagerAdapter);
//        viewPager.setOnPageChangeListener(mOnPageChangeListener);
//        mainPagerAdapter.initViewUrl(mAllAddressList);
//        mGirlDesText.setText(mAllAddressList.get(0).getDesContent());
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });


        //搜索框的逻辑
        iv_searchBack=findViewById(R.id.iv_searchback);
//        btn_search=findViewById(R.id.btn_search);
        et_searchText=findViewById(R.id.et_searchtext);
        tv_historyText=findViewById(R.id.tv_searchhistory);
//        tv_clearHistory=findViewById(R.id.tv_clearsearch);
        sys_buttun = findViewById(R.id.btn_search_sm);





        //测试网络请求
//        String url = getString(R.string.http_url)+"8086/location/matchPhrase";
//        OkHttpUtils.get().url(url)
//               // .addParams("username", "admin")
//                .addParams("query", "河南").build().execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.d("response", response);
//                    }
//                });

    }

    private void initGirlUrl() {
        mAllAddressList = new ArrayList<>();
        String url = "http://kangkangtk.gnway.cc/attachment/findallhouseinfolist";
        OkHttpUtils.get().url(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonobj = new  JSONObject(response);
                    JSONArray jsonarray = jsonobj.getJSONArray("data");
                    // for循环
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Address address = new Address();
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        address.setImageUrl(jsonObj.getString("hoseholderidcardimgz"));
                        address.setDesContent(jsonObj.getString("landinformation"));
                        mAllAddressList.add(address);
                    }
                    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
                    mGirlDesText = (TextView) findViewById(R.id.text_view);
                    MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(NaviRouteActivity.this);
                    mainPagerAdapter.setOnClickItemListener(mOnClickItemListener);
                    viewPager.setAdapter(mainPagerAdapter);
                    viewPager.setOnPageChangeListener(mOnPageChangeListener);
                    mainPagerAdapter.initViewUrl(mAllAddressList);
                    mGirlDesText.setText(mAllAddressList.get(0).getDesContent());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        for (int i = 0; i < 5; i++) {
//            Address address = new Address();
//            address.setImageUrl(Constant.ImageUrl[i]);
//            address.setDesContent(Constant.DesContent[i]);
//            mAllAddressList.add(address);
//        }
    }

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
//                toolbar.getBackground().setAlpha(255 - (int) precent);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                // finish();
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mGirlDesText.setText(mAllAddressList.get(position).getDesContent());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }

    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    Log.d("点击跳转", radioButton.getText().toString());
                    if (radioButton.getText().toString().equals("记录")) {
                        startActivity(new Intent(NaviRouteActivity.this, SearchActivity.class));
                    }
                    if (radioButton.getText().toString().equals("扫码")) {
                        startActivity(new Intent(NaviRouteActivity.this, ArActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if (radioButton.getText().toString().equals("首页")) {
                        startActivity(new Intent(NaviRouteActivity.this, LiveActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if (radioButton.getText().toString().equals("设置")) {
                        startActivity(new Intent(NaviRouteActivity.this, LoginActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if (radioButton.getText().toString().equals("我的")) {
                        startActivity(new Intent(NaviRouteActivity.this, loginActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    private MainPagerAdapter.OnClickItemListenerImpl mOnClickItemListener = new MainPagerAdapter.OnClickItemListenerImpl() {
        @Override
        public void onClickItem(View item, int position) {
            if (mScrollLayout.getCurrentStatus() == ScrollLayout.Status.OPENED) {
                mScrollLayout.scrollToClose();
            } else {
                startActivity(new Intent(NaviRouteActivity.this, ThreeActivity.class));
            }
        }
    };


    /**
     * 实现搜索功能
     */
    private void setListeners() {


        /**
         * 存放搜索历史的表
         */
        SQLiteOpenHelper helper= SearchSQLiteOpenHelper.getmInstance(NaviRouteActivity.this);

        /**
         * 返回服务页面
         */
        iv_searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NaviRouteActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 给搜索历史传入空
         */
        tv_historyText.setText(" ");


        /**
         * 输入框点击事件sys_buttun
         */

        et_searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NaviRouteActivity.this, SearchDemoActivity.class);
                NaviRouteActivity.this.startActivity(intent);
            }
        });


        sys_buttun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
                Log.d("sag", "adafafadafd点击扫码");
            }
        });

        /**
         * 搜索按钮的监听
         */
//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String obtain=et_searchText.getText().toString().trim();
//                /**
//                 * 下一次点击搜索按钮时清空前一次搜索列表
//                 */
//                count++;
//                if(count%1==0){
//                    searchList.clear();
//                }
//                /**
//                 * 将搜索框内容放入到搜索历史当中去
//                 */
//                tv_historyText.append(obtain+" ");
//                /**
//                 * 将搜索框内容放入到搜索历史表当中去
//                 */
//                SQLiteDatabase db_history=helper.getWritableDatabase();
//                if(db_history.isOpen()){
//                    String add_historysearchname_sql="insert into historysearch(historyname) values(?);";
//                    db_history.execSQL(add_historysearchname_sql,new Object[]{obtain});
//                    Toast.makeText(SecondActivity.this,"增加成功",Toast.LENGTH_SHORT).show();
//                }
//                db_history.close();
//
//                /**
//                 * 判断搜索框是否为空
//                 */
//                if(obtain.isEmpty()){
//                    Toast.makeText(SecondActivity.this,"搜索框为空",Toast.LENGTH_SHORT).show();
//                    searchList.clear();
//                }else{
//                    /**
//                     * 获取数据库中的表，取出搜索框中的首字符放入查询语句进行查询相匹配的内容
//                     */
//                    SQLiteDatabase db_search=helper.getReadableDatabase();
//                    if(db_search.isOpen()){
//                        String firstChar=obtain.substring(0,1);
//                        String query_sql="select * from search where searchname like '"+firstChar+"%'";
//                        Cursor cursor = db_search.rawQuery(query_sql,null);
//                        if(cursor.getCount()==0){
//                            Toast.makeText(SecondActivity.this,"没有该服务",Toast.LENGTH_SHORT).show();
//                        }else{
//                            cursor.moveToFirst();
//                            String searchname=cursor.getString(cursor.getColumnIndex("searchname"));
//                            searchList.add(searchname);
//                        }
//                        while(cursor.moveToNext()){
//                            String searchname1=cursor.getString(cursor.getColumnIndex("searchname"));
//                            searchList.add(searchname1);
//                        }
//                        cursor.close();
//                    }
//                    db_search.close();
//                }
//
//            }
//        });

        SQLiteDatabase db_get_history=helper.getReadableDatabase();
        if(db_get_history.isOpen()){
            String sql_history_query="select * from historysearch;";
            Cursor cursor = db_get_history.rawQuery(sql_history_query, null);
            if(cursor.getCount()==0){
                Toast.makeText(NaviRouteActivity.this,"没有搜索历史",Toast.LENGTH_SHORT).show();
            }else{
                cursor.moveToFirst();
                @SuppressLint("Range") String history_name=cursor.getString(cursor.getColumnIndex("historyname"));
                tv_historyText.append(history_name+" ");
            }
            while (cursor.moveToNext()){
                @SuppressLint("Range") String history_name=cursor.getString(cursor.getColumnIndex("historyname"));
                tv_historyText.append(history_name+" ");
            }
            cursor.close();
        }
        db_get_history.close();



//        tv_clearHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SQLiteDatabase db_delete_history=helper.getWritableDatabase();
//                if(db_delete_history.isOpen()){
//                    String sql_delete_history="delete  from historysearch;";
//                    db_delete_history.execSQL(sql_delete_history);
//                    Toast.makeText(NaviRouteActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
//                }
//                tv_historyText.setText(" ");
//            }
//        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navigation_drawer_account) {
            EventMessage message = new EventMessage(1, "测试图层");

            EventBus.getDefault().postSticky(message);//把事件发出，通过eventbus将事件传递给该事件的订阅者使用
           Log.d("shshhgg","dianjichishga");
            // Handle showing account information
        } else if (id == R.id.navigation_drawer_payment) {
            EventMessage message = new EventMessage(1, "position");

            EventBus.getDefault().postSticky(message);//把事件发出，通过eventbus将事件传递给该事件的订阅者使用
            Log.d("shshhgg","dianjichishga");
            // Handle showing payment information
        } else if (id == R.id.navigation_drawer_help) {
            EventMessage message = new EventMessage(1, "data");

            EventBus.getDefault().postSticky(message);//把事件发出，通过eventbus将事件传递给该事件的订阅者使用
            Log.d("shshhgg","dianjichishga");
            // Handle showing help information
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout_nva);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_nva);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(EventMessage event) {
//
//    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true,priority = 2)
    public void onGetMessage(EventMessage  message) {

        mAllAddressList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Address address = new Address();
            address.setImageUrl(Constantnew.ImageUrl[i]);
            address.setDesContent(Constantnew.DesContent[i]);
            mAllAddressList.add(address);
        }
        mGirlDesText = (TextView) findViewById(R.id.text_view);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this);
        viewPager.setAdapter(mainPagerAdapter);
        mainPagerAdapter.initViewUrl(mAllAddressList);
        mGirlDesText.setText(mAllAddressList.get(0).getDesContent());
        Log.d("message",message.toString());
    }



    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .CAMERA)) {
                Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(NaviRouteActivity.this, new String[]{Manifest.permission.CAMERA}, com.yinglan.scrolllayout.demo.util.Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(NaviRouteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, com.yinglan.scrolllayout.demo.util.Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(NaviRouteActivity.this, CaptureActivity.class);
        startActivityForResult(intent, com.yinglan.scrolllayout.demo.util.Constant.REQ_QR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == com.yinglan.scrolllayout.demo.util.Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(com.yinglan.scrolllayout.demo.util.Constant.INTENT_EXTRA_KEY_QR_SCAN);
            Toast.makeText(NaviRouteActivity.this, scanResult, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case com.yinglan.scrolllayout.demo.util.Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(NaviRouteActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
            case com.yinglan.scrolllayout.demo.util.Constant.REQ_PERM_EXTERNAL_STORAGE:
                // 文件读写权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(NaviRouteActivity.this, "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}