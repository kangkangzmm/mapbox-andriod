package com.yinglan.scrolllayout.demo;




import static com.mapbox.api.directions.v5.DirectionsCriteria.IMPERIAL;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_WALKING;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.yinglan.scrolllayout.demo.util.LayerIdConstants.NEIGHBORHOOD_PARKING_ZONE_FILL_LAYER_ID;
import static com.yinglan.scrolllayout.demo.util.LayerIdConstants.WALK_TO_VEHICLE_ROUTE_LINE_LAYER_ID;
import static com.yinglan.scrolllayout.demo.util.SourceIdConstants.PLACES_PLUGIN_SEARCH_RESULT_SOURCE_ID;
import static com.yinglan.scrolllayout.demo.util.SourceIdConstants.WALK_TO_VEHICLE_ROUTE_SOURCE_ID;

import static org.webrtc.VideoFrameDrawer.TAG;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonElement;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.turf.TurfConversion;
import com.mapbox.turf.TurfJoins;
import com.mapbox.vision.VisionManager;
import com.yinglan.scrolllayout.ScrollLayout;
import com.yinglan.scrolllayout.demo.model.Address;
import com.yinglan.scrolllayout.demo.model.Constant;
import com.yinglan.scrolllayout.demo.search.SearchSQLiteOpenHelper;
import com.yinglan.scrolllayout.demo.viewpager.MainPagerAdapter;
import com.yinglan.scrolllayout.demo.viewpager.RecyclerViewAdapter;
import com.yinglan.scrolllayout.demo.viewpager.TurnByTurnNavigationFragment;
import com.yinglan.scrolllayout.demo.viewpager.VehicleMapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @function 次页
 * @auther: Created by yinglan
 * @time: 16/3/16
 */
public class SecondActivity extends AppCompatActivity {

    private ScrollLayout mScrollLayout;
    private ArrayList<Address> mAllAddressList;
    private TextView mGirlDesText;
    private Toolbar toolbar;
    private MapView mapView;
    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;

    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;

    private View view;
    private LatLng currentSelectedVehicleLatLng;
    private MapboxMap mapboxMap;
    private MapboxDirections directionsApiClient;
    private LocationComponent locationComponent;
    private static final LatLng MAPBOX_SF_OFFICE_LAT_LNG_COORDINATES = new LatLng(30.4912557, 120.396398);
    private static final LatLng MIDDLE_OF_SF_COORDINATES = new LatLng(37.7590, -122.4498);
    private static final int PLACE_SEARCH_REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int PLACE_SEARCH_RESULT_CODE_AUTOCOMPLETE = -1;
    private boolean useNavigationLauncher = true;


    private Style mapStyle;
    private GeoJsonSource fillgeojson;

    //搜索框的
    private ImageView iv_searchBack;
    private Button btn_search;
    private EditText et_searchText;
    private TextView tv_historyText,tv_clearHistory,resultText;
    private List<String> searchList=new ArrayList<>();
    private int count=0;
    private String  infodata;
    private JSONObject lonlat;

    private MainPagerAdapter.OnClickItemListenerImpl mOnClickItemListener = new MainPagerAdapter.OnClickItemListenerImpl() {
        @Override
        public void onClickItem(View item, int position) {
            if (mScrollLayout.getCurrentStatus() == ScrollLayout.Status.OPENED) {
                mScrollLayout.scrollToClose();
            } else {
                startActivity(new Intent(SecondActivity.this, ThreeActivity.class));
            }
        }
    };

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
                toolbar.getBackground().setAlpha(255 - (int) precent);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), "sk.eyJ1IjoiemhhbmdtbSIsImEiOiJjbGZ4ajQ4cDcwNXF4M29wODVzMHF4cmoxIn0.CJEnEqnxAL3P1_snTPVfzQ");

        VisionManager.init((Application) getApplicationContext(), "sk.eyJ1IjoiemhhbmdtbSIsImEiOiJjbGZ4ajQ4cDcwNXF4M29wODVzMHF4cmoxIn0.CJEnEqnxAL3P1_snTPVfzQ");

        setContentView(R.layout.activity_second);
        mapView =findViewById(R.id.main_mapView);
        // 获取意图对象
        Intent intent = getIntent();
        try {
            infodata = intent.getStringExtra("data");
            lonlat = new  JSONObject(infodata).getJSONObject("lonlat");
            Log.d("infodatainfodata", lonlat.getString("lon")+lonlat.getString("lat"));


            mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                //必须设置地图样式
//                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                mapboxMap.setStyle(new Style.Builder().fromUri("asset://newstyle.json"), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        SecondActivity.this.mapboxMap = mapboxMap;
                        mapStyle = style;
                        initDashWalkingDirectionLineLayer(mapStyle);
//                        initCoordinates();

                        findViewById(R.id.start_turn_by_turn_navigate_to_bike_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (View.VISIBLE == view.findViewById(R.id.single_vehicle_distance_and_time_info_cardview).getVisibility()) {
                                    view.findViewById(R.id.main_mapView).setVisibility(View.INVISIBLE);

                                    startNavigation(useNavigationLauncher, currentSelectedVehicleLatLng);
                                }
                            }
                        });







                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener(){
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
//                                point,"ExtrusionLayer"
//                                 List<Feature> features = mapboxMap.queryRenderedFeatures();
                                // user clicked on the map
                                // 将LatLng坐标转换为屏幕像素，只查询呈现的特性。
                                final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);

                                List<Feature> features = mapboxMap.queryRenderedFeatures(pixel);

                                // 获取列表中的第一个特征(如果存在)
                                if (features.size() > 0) {
                                    Feature feature = features.get(0);
                                    infodata = infodata;
                                    Log.d("sssssssssssag", feature.properties().toString());

                                    // 确保特征feature具有已定义的属性
                                    if (feature.properties() != null) {
                                        for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                                            // 打印所有属性
                                            Log.d("nnnnnnnnsag", String.format("%s = %s", entry, entry.getValue()));

                                        }
                                    }
                                }
                                return false;
                            }
                        });


                    }
                });
//                LatLng latlng = new LatLng(30.15442889806117,120.47381846373213);

                try {
                    String lat ="30.15442889806117";
                    String lon ="120.47381846373213";
                    lat = lonlat.getString("lat");
                    lon = lonlat.getString("lon");
                    LatLng centorlatlng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
                    @SuppressLint("Range") CameraPosition cameraposition = new CameraPosition.Builder()
                            .target(centorlatlng)
                            .zoom(15)
                            .bearing(90)
                            .tilt(90)
                            .build();
                    mapboxMap.setCameraPosition(cameraposition);

                } catch (JSONException e) {
                    e.printStackTrace();
                }






            }
        });
        }catch(Exception e){
            mapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                                        //必须设置地图样式
//                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                                        mapboxMap.setStyle(new Style.Builder().fromUri("asset://newstyle.json"), new Style.OnStyleLoaded() {
                                            @Override
                                            public void onStyleLoaded(@NonNull Style style) {
                                                SecondActivity.this.mapboxMap = mapboxMap;
                                                mapStyle = style;
                                                initDashWalkingDirectionLineLayer(mapStyle);
//                        initCoordinates();

                                                findViewById(R.id.start_turn_by_turn_navigate_to_bike_button).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (View.VISIBLE == findViewById(R.id.single_vehicle_distance_and_time_info_cardview).getVisibility()) {
                                                            findViewById(R.id.main_mapView).setVisibility(View.INVISIBLE);

                                                            startNavigation(useNavigationLauncher, currentSelectedVehicleLatLng);
                                                        }
                                                    }
                                                });


                                                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                                                    @Override
                                                    public boolean onMapClick(@NonNull LatLng point) {
//                                point,"ExtrusionLayer"
//                                 List<Feature> features = mapboxMap.queryRenderedFeatures();
                                                        // user clicked on the map
                                                        // 将LatLng坐标转换为屏幕像素，只查询呈现的特性。
                                                        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);

                                                        List<Feature> features = mapboxMap.queryRenderedFeatures(pixel);

                                                        evaluateClick(features, R.drawable.bike_icon, false);

                                                        // 获取列表中的第一个特征(如果存在)
                                                        if (features.size() > 0) {
                                                            Feature feature = features.get(0);
                                                            infodata = infodata;
                                                            Log.d("sag", feature.toString());
                                                            Log.d("propertiessag", feature.properties().get("layername").toString());
//                                                            dialog1(feature.properties().get("layername").toString(),feature.properties().toString());

                                                            // 确保特征feature具有已定义的属性
//                                                            if (feature.properties() != null) {
//                                                                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
//                                                                    // 打印所有属性
//                                                                    Log.d("featuresag", String.format("%s = %s", entry, entry.getValue()));
//                                                                    dialog1();
//
////                                                                    Intent intent1 = new Intent(SecondActivity.this,
////                                                                            VideoActivity.class);
////                                                                    startActivity(intent1);
//
//                                                                }
//                                                            }
                                                        }
                                                        return false;
                                                    }
                                                });


                                            }
                                        });
//                LatLng latlng = new LatLng(30.15442889806117,120.47381846373213);
                                            String lat = "30.15442889806117";
                                            String lon = "120.47381846373213";
                                            LatLng centorlatlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                            @SuppressLint("Range") CameraPosition cameraposition = new CameraPosition.Builder()
                                                    .target(centorlatlng)
                                                    .zoom(15)
                                                    .bearing(90)
                                                    .tilt(90)
                                                    .build();
                                            mapboxMap.setCameraPosition(cameraposition);

                                    }
            });
            System.out.println("base64Code:::"+e.toString());//出现异常的处理
        }

        initGirlUrl();
        initView();
        setListeners();


        //获取传递的值
//        String str = intent.getStringExtra("data");
//        Log.d("传递数据", str);
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        mGirlDesText = (TextView) findViewById(R.id.text_view);
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);
        toolbar.setNavigationIcon(R.mipmap.action_bar_return);
        toolbar.setTitle("ScrollLayout");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        mAdapter = new SecondActivity.MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
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

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this);
        mainPagerAdapter.setOnClickItemListener(mOnClickItemListener);
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setOnPageChangeListener(mOnPageChangeListener);
        mainPagerAdapter.initViewUrl(mAllAddressList);
        mGirlDesText.setText(mAllAddressList.get(0).getDesContent());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //搜索框的逻辑
        iv_searchBack=findViewById(R.id.iv_searchback);
//        btn_search=findViewById(R.id.btn_search);
        et_searchText=findViewById(R.id.et_searchtext);
        tv_historyText=findViewById(R.id.tv_searchhistory);
//        tv_clearHistory=findViewById(R.id.tv_clearsearch);





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
        for (int i = 0; i < 5; i++) {
            Address address = new Address();
            address.setImageUrl(Constant.ImageUrl[i]);
            address.setDesContent(Constant.DesContent[i]);
            mAllAddressList.add(address);
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
                        startActivity(new Intent(SecondActivity.this, SecondActivity.class));
                    }
                    if (radioButton.getText().toString().equals("web")) {
                        startActivity(new Intent(SecondActivity.this, ThreeActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if (radioButton.getText().toString().equals("首页")) {
                        startActivity(new Intent(SecondActivity.this, MainActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if (radioButton.getText().toString().equals("设置")) {
                        startActivity(new Intent(SecondActivity.this, LauncherActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if (radioButton.getText().toString().equals("扫码")) {
                        startActivity(new Intent(SecondActivity.this, SysActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
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




    /**
     * 实现搜索功能
     */
    private void setListeners() {

        /**
         * 存放搜索历史的表
         */
        SQLiteOpenHelper helper= SearchSQLiteOpenHelper.getmInstance(SecondActivity.this);

        /**
         * 返回服务页面
         */
        iv_searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 给搜索历史传入空
         */
        tv_historyText.setText(" ");


        /**
         * 输入框点击事件
         */

        et_searchText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("sag", "adafafadafd");
                            Intent intent = new Intent();
                            intent.setClass(SecondActivity.this, SearchDemoActivity.class);
                            SecondActivity.this.startActivity(intent);
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
                Toast.makeText(SecondActivity.this,"没有搜索历史",Toast.LENGTH_SHORT).show();
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



        tv_clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db_delete_history=helper.getWritableDatabase();
                if(db_delete_history.isOpen()){
                    String sql_delete_history="delete  from historysearch;";
                    db_delete_history.execSQL(sql_delete_history);
                    Toast.makeText(SecondActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                }
                tv_historyText.setText(" ");
            }
        });
    }
    /**
     * 创建一个dialog
     */
    private void dialog1(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle(title); //设置标题
        builder.setMessage(message); //设置内容
        builder.setIcon(R.drawable.bike_icon);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                Toast.makeText(SecondActivity.this, "确认" + which, Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(SecondActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("去这里", new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(SecondActivity.this, "去这里" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }


    private void setVehicleCardview(int drawable, boolean scooterSelected) {
        ImageView vehicleTypeImageView = findViewById(R.id.vechicle_type_imageview);
        findViewById(R.id.single_vehicle_distance_and_time_info_cardview).setVisibility(View.VISIBLE);
        TextView vehicleNumber = findViewById(R.id.vehicle_number);
        vehicleNumber.setText(String.format("#" + String.valueOf(new Random().nextInt(900))));
        Button rentVehicleButton = findViewById(R.id.start_turn_by_turn_navigate_to_bike_button);
        rentVehicleButton.setBackgroundColor(Color.parseColor(scooterSelected ? "#8045CA" : "#A2FCA2"));
        rentVehicleButton.setTextColor(Color.parseColor(scooterSelected ? "#FFFFFF" : "#000000"));
    }

    private void evaluateClick(List<Feature> featureList, int drawable, boolean scooterSelected) {
//        Feature feature = featureList.get(0);
        setVehicleCardview(drawable, scooterSelected);
//        Polygon singleLocationPolygon = (Polygon) feature.geometry();
        Point singleLocationPoint = Point.fromLngLat(120.49072, 30.152924);
        currentSelectedVehicleLatLng = new LatLng(singleLocationPoint.coordinates().get(1),
                singleLocationPoint.coordinates().get(0));
        getInformationFromDirectionsApi(currentSelectedVehicleLatLng);
        adjustCameraZoom();
        adjustCompass(true);
    }

    @SuppressWarnings( {"MissingPermission"})
    private void getInformationFromDirectionsApi(@NonNull final LatLng selectedVehicleCoordinates) {
        directionsApiClient = MapboxDirections.builder()
                .origin(getAppropriateOriginPoint())
                .destination(Point.fromLngLat(selectedVehicleCoordinates.getLongitude(),
                        selectedVehicleCoordinates.getLatitude()))
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(PROFILE_WALKING)
                .accessToken("sk.eyJ1Ijoia2FuZ2thbmdrIiwiYSI6ImNsZ2txajAzaTBhemYzcG5vbTB6M2hlMDAifQ.fN0T8NXrBc8VQhfjIy1XGg")
                .build();

        directionsApiClient.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // Check that the response isn't null and that the response has a route
                if (response.body() == null) {
                    Log.d(TAG, getString(R.string.set_right_token));
                } else if (response.body().routes().size() < 1) {
                    Log.d(TAG, getString(R.string.no_routes_found));
                } else {
                    // Retrieve and draw the navigation route on the map
                    drawNavigationPolylineRoute(response.body().routes().get(0));
                    setVehicleWalkToDistance(response.body());
                    setVehicleWalkToTime(response.body());
                    getVehicleLocation(Point.fromLngLat(selectedVehicleCoordinates.getLongitude(),
                            selectedVehicleCoordinates.getLatitude()));
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Toast.makeText(SecondActivity.this, R.string.failure_to_retrieve, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void adjustCameraZoom() {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(currentSelectedVehicleLatLng.getLatitude(), currentSelectedVehicleLatLng.getLongitude()),
                mapboxMap.getCameraPosition().zoom < 12 ? 14 : mapboxMap.getCameraPosition().zoom), 1800);
    }
    private void adjustCompass(boolean lowerBelowCardview) {
        mapboxMap.getUiSettings().setCompassMargins(mapboxMap.getUiSettings().getCompassMarginLeft(),
                lowerBelowCardview ? findViewById(R.id.single_vehicle_distance_and_time_info_cardview).getMeasuredHeight()
                        + 30 : 0,
                mapboxMap.getUiSettings().getCompassMarginRight(),
                mapboxMap.getUiSettings().getCompassMarginBottom());
    }

    @SuppressWarnings( {"MissingPermission"})
    private Point getAppropriateOriginPoint() {
        Point originPoint;
        if (locationComponent != null) {
            if (locationIsInSanFranciscoBounds()) {
                originPoint = Point.fromLngLat(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude(),
                        mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
            } else {
                originPoint = Point.fromLngLat(MAPBOX_SF_OFFICE_LAT_LNG_COORDINATES.getLongitude(),
                        MAPBOX_SF_OFFICE_LAT_LNG_COORDINATES.getLatitude());
            }
        } else {
            originPoint = Point.fromLngLat(MAPBOX_SF_OFFICE_LAT_LNG_COORDINATES.getLongitude(),
                    MAPBOX_SF_OFFICE_LAT_LNG_COORDINATES.getLatitude());
        }
        return originPoint;
    }



    private void drawNavigationPolylineRoute(DirectionsRoute route) {
        if (mapboxMap.getStyle() != null) {
            GeoJsonSource navLineRouteSource = mapboxMap.getStyle().getSourceAs(WALK_TO_VEHICLE_ROUTE_SOURCE_ID);
            if (navLineRouteSource != null) {
                navLineRouteSource.setGeoJson(Feature.fromGeometry(LineString.fromPolyline(
                        route.geometry(), Constants.PRECISION_6)));
            }
        }
    }

    private void setVehicleWalkToDistance(DirectionsResponse response) {
        TextView walkingDistance = findViewById(R.id.mileage_walking_distance_to_vechicle);
        if (response.routes().get(0).distance() != null) {
            walkingDistance.setText(" " + String.format(getString(R.string.walking_miles),
                    new DecimalFormat("0.0").format(TurfConversion.convertLength(
                            response.routes().get(0).distance(),
                            "meters", "miles"))));
        } else {
            walkingDistance.setText(getString(R.string.travel_time_unknown));
        }
    }

    private void setVehicleWalkToTime(DirectionsResponse response) {
        TextView walkingTime = findViewById(R.id.walking_time_to_vechicle);
        DecimalFormat decimalFormat;
        if (response.routes().get(0).duration() != null) {
            decimalFormat = new DecimalFormat(response.routes().get(0).duration() / 60 >= 10 ? "00" : "0");
            walkingTime.setText(String.format(getString(R.string.walking_minutes), decimalFormat.format(
                    response.routes().get(0).duration() / 60)));
        } else {
            walkingTime.setText(getString(R.string.travel_time_unknown));
        }
    }

    private void getVehicleLocation(Point vehicleLocation) {
        MapboxGeocoding.builder()
                .accessToken("sk.eyJ1Ijoia2FuZ2thbmdrIiwiYSI6ImNsZ2txajAzaTBhemYzcG5vbTB6M2hlMDAifQ.fN0T8NXrBc8VQhfjIy1XGg")
                .query(Point.fromLngLat(vehicleLocation.longitude(), vehicleLocation.latitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build()
                .enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                        List<CarmenFeature> results = response.body().features();
                        if (results.size() > 0) {
                            TextView vehicleAddress = findViewById(R.id.vehicle_address);
                            vehicleAddress.setText(results.get(0).address() != null ?
                                    results.get(0).placeName().split(",")[0] : getString(R.string.address_unknown));
                        } else {
                            // No result for your request were found.
                            Log.d(TAG, "onResponse: No result found");
                        }
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private boolean locationIsInSanFranciscoBounds() {
        if (locationComponent != null && locationComponent.getLastKnownLocation() != null) {
            List<List<Point>> listOfPolygonPoints = new ArrayList<>();
            List<Point> polygonPoints = new ArrayList<>();
            polygonPoints.add(Point.fromLngLat(-122.477, 37.810));
            polygonPoints.add(Point.fromLngLat(-122.486, 37.7890));
            polygonPoints.add(Point.fromLngLat(-122.509, 37.786));
            polygonPoints.add(Point.fromLngLat(-122.515, 37.781));
            polygonPoints.add(Point.fromLngLat(-122.498, 37.6889));
            polygonPoints.add(Point.fromLngLat(-122.490, 37.630));
            polygonPoints.add(Point.fromLngLat(-122.405, 37.582));
            polygonPoints.add(Point.fromLngLat(-122.357, 37.590));
            polygonPoints.add(Point.fromLngLat(-122.381, 37.6109));
            polygonPoints.add(Point.fromLngLat(-122.355, 37.6140));
            polygonPoints.add(Point.fromLngLat(-122.348, 37.83));
            polygonPoints.add(Point.fromLngLat(-122.460, 37.833));
            listOfPolygonPoints.add(polygonPoints);
            return TurfJoins.inside(Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                    locationComponent.getLastKnownLocation().getLatitude()), Polygon.fromLngLats(listOfPolygonPoints));
        }
        return false;
    }

    private void initDashWalkingDirectionLineLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(WALK_TO_VEHICLE_ROUTE_SOURCE_ID));
        loadedMapStyle.addLayer(new LineLayer(WALK_TO_VEHICLE_ROUTE_LINE_LAYER_ID,
                WALK_TO_VEHICLE_ROUTE_SOURCE_ID)
                .withProperties(
                        lineWidth(6f),
                        lineOpacity(.6f),
                        lineCap(LINE_CAP_ROUND),
                        lineJoin(LINE_JOIN_ROUND),
                        lineColor(Color.parseColor("#d742f4")),
                        lineDasharray(new Float[] {1f, 2f})));
    }

    @SuppressWarnings( {"MissingPermission"})
    private void startNavigation(boolean useNavigationLauncher, LatLng selectedDestination) {
        if (useNavigationLauncher) {
            NavigationRoute.builder(SecondActivity.this)
                    .accessToken("sk.eyJ1Ijoia2FuZ2thbmdrIiwiYSI6ImNsZ2txajAzaTBhemYzcG5vbTB6M2hlMDAifQ.fN0T8NXrBc8VQhfjIy1XGg")
                    .voiceUnits(IMPERIAL)
                    .profile(PROFILE_WALKING)
                    .origin(getAppropriateOriginPoint())
                    .destination(Point.fromLngLat(selectedDestination.getLongitude(), selectedDestination.getLatitude()))
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                    .directionsRoute(response.body().routes().get(0))
                                    .shouldSimulateRoute(true)
                                    .build();
                            findViewById(R.id.main_mapView).setVisibility(View.INVISIBLE);
                                NavigationLauncher.startNavigation(SecondActivity.this, options);
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                            Toast.makeText(SecondActivity.this, R.string.failure_to_retrieve, Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            double originLat = getAppropriateOriginPoint().latitude();
            double originLong = getAppropriateOriginPoint().longitude();
            Bundle args = new Bundle();
            args.putDouble("NAVIGATION_DESTINATION_LAT", selectedDestination.getLatitude());
            args.putDouble("NAVIGATION_DESTINATION_LONG", selectedDestination.getLongitude());
            args.putDouble("NAVIGATION_ORIGIN_LAT", originLat);
            args.putDouble("NAVIGATION_ORIGIN_LONG", originLong);
            TurnByTurnNavigationFragment turnByTurnNavigationFragment = new TurnByTurnNavigationFragment();
            turnByTurnNavigationFragment.setArguments(args);
            FragmentTransaction transaction = SecondActivity.this.getFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.map_fragment_container, turnByTurnNavigationFragment);
            transaction.commit();
        }
    }


    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_SEARCH_REQUEST_CODE_AUTOCOMPLETE && resultCode == PLACE_SEARCH_RESULT_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Retrieve and update the source designated for showing a selected location's symbol layer icon
            if (mapboxMap.getStyle() != null) {
                GeoJsonSource source = mapboxMap.getStyle().getSourceAs(PLACES_PLUGIN_SEARCH_RESULT_SOURCE_ID);
                if (source != null) {
                    source.setGeoJson(FeatureCollection.fromFeatures(
                            new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                }

                // Move map camera to the selected location
                CameraPosition newCameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(selectedCarmenFeature.center().coordinates().get(1),
                                selectedCarmenFeature.center().coordinates().get(0)))
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition), 2000);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}