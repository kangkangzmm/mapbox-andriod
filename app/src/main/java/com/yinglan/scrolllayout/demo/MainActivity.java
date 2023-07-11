package com.yinglan.scrolllayout.demo;

import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionBase;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.yinglan.scrolllayout.demo.util.IconIdConstants.INDIVIDUAL_BIKE_ICON_IMAGE_ID;
import static com.yinglan.scrolllayout.demo.util.LayerIdConstants.INDIVIDUAL_BIKE_SYMBOL_LAYER_ID;
import static com.yinglan.scrolllayout.demo.util.LayerIdConstants.NEIGHBORHOOD_PARKING_ZONE_FILL_LAYER_ID;
import static com.yinglan.scrolllayout.demo.util.LayerIdConstants.NEIGHBORHOOD_PARKING_ZONE_LINE_LAYER_ID;
import static com.yinglan.scrolllayout.demo.util.LayerIdConstants.NO_PARK_ZONE_FILL_LAYER_ID;
import static com.yinglan.scrolllayout.demo.util.LayerIdConstants.WALK_TO_VEHICLE_ROUTE_LINE_LAYER_ID;
import static com.yinglan.scrolllayout.demo.util.SourceIdConstants.INDIVIDUAL_BIKE_SOURCE_ID;
import static com.yinglan.scrolllayout.demo.util.SourceIdConstants.NEIGHBORHOOD_PARK_ZONE_SOURCE_ID;
import static com.yinglan.scrolllayout.demo.util.SourceIdConstants.WALK_TO_VEHICLE_ROUTE_SOURCE_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonElement;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.VectorSource;
import com.mapbox.turf.TurfConversion;
import com.mapbox.vision.VisionManager;
import com.yinglan.scrolllayout.ScrollLayout;
import com.yinglan.scrolllayout.demo.entity.ListEntity;
import com.yinglan.scrolllayout.demo.util.ScreenUtil;
import com.yinglan.scrolllayout.demo.viewpager.ListviewAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * @function 主页
 * @auther: Created by yinglan
 * @time: 16/3/16
 */
public class MainActivity extends AppCompatActivity implements LocationEngineCallback<LocationEngineResult> {
    private ScrollLayout mScrollLayout;
    private TextView text_foot;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;
    private List<String> dataList =new ArrayList<>();

    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;
    //位置更新之间的距离
    public static final float DEFAULT_DISPLACEMENT = 5.0f;
    //位置更新的最大等待时间（以毫秒为单位）。
    private static final long DEFAULT_MAX_WAIT_TIME = 10000L;
    //位置更新的最快间隔（以毫秒为单位）
    private static final long DEFAULT_FASTEST_INTERVAL = 2L;
    //位置更新之间的默认间隔
    public static final long DEFAULT_INTERVAL = 5000L;
    private Location lastLocation;
    private List<Point> routeCoordinates;

    private Style mapStyle;
    private GeoJsonSource geoJsonSource;
    private FeatureCollection featureCollection;
    private LineLayer lineLayer;
    private LocationEngine mLocationEngine;


    private static final String LINE_SOURCE_ID = "line-source-id";
    private static final String LINE_LAYER_ID = "line-layer-id";
    private static final String LINE_IMAGE_ID = "line-image-id";


    private static final String FILL_SOURCE_ID = "fill-source-id";
    private static final String FILL_LAYER_ID = "fill-layer-id";
    private static final String FILL_IMAGE_ID = "fill-image-id";
    private boolean addOutlinesToNeighboorhoodParkingArea = false;



    private FillLayer fillLayer;
    private GeoJsonSource fillgeojson;
    private UserInfo userinfo;


    // variables for calculating and drawing a route
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;






    private LocationEngineRequest mLocationEngineRequest = new LocationEngineRequest.Builder(DEFAULT_INTERVAL)
            //要求最准确的位置
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            //请求经过电池优化的粗略位置
//            .setPriority(LocationEngineRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            //要求粗略〜10 km的准确位置
//            .setPriority(LocationEngineRequest.PRIORITY_LOW_POWER)
            //被动位置：除非其他客户端请求位置更新，否则不会返回任何位置
//            .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
            //设置位置更新之间的距离
            .setDisplacement(DEFAULT_DISPLACEMENT)
            //设置位置更新的最大等待时间（以毫秒为单位）。
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            //设置位置更新的最快间隔（以毫秒为单位）
            .setFastestInterval(DEFAULT_FASTEST_INTERVAL)
            .build();



    @Override
    public void onSuccess(LocationEngineResult result) {
        lastLocation = result.getLastLocation();
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            userinfo = (UserInfo) getApplication();
            Toast.makeText(this, "onSuccess LatLng: " + latitude + "," + longitude + "," +userinfo.getname()+ "," +userinfo.gettoken()  , Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onFailure(@NonNull Exception exception) {
        Toast.makeText(this, "onFailure : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //结束请求位置
        if (mLocationEngine != null) {
            mLocationEngine.removeLocationUpdates(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //请求位置
        mLocationEngine = LocationEngineProvider.getBestLocationEngine(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        mLocationEngine.requestLocationUpdates(mLocationEngineRequest, this, Looper.getMainLooper());
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
            }
            if (text_foot.getVisibility() == View.VISIBLE)
                text_foot.setVisibility(View.GONE);
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                text_foot.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
//
//        VisionManager.init((Application) getApplicationContext(), getResources().getString(R.string.mapbox_access_token));

        Mapbox.getInstance(getApplicationContext(), "sk.eyJ1IjoiemhhbmdtbSIsImEiOiJjbGFrbXM3NHYwNnM3M3BrMHVwaWR3NnFwIn0.ddu0apyK2st4E44oSwBLqw");

        VisionManager.init((Application) getApplicationContext(), "sk.eyJ1IjoiemhhbmdtbSIsImEiOiJjbGFrbXM3NHYwNnM3M3BrMHVwaWR3NnFwIn0.ddu0apyK2st4E44oSwBLqw");

        setContentView(R.layout.activity_main);

        final EditText etContent = (EditText) findViewById(R.id.etContent);
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        etContent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollLayout.setToOpen();
                /**加这个判断，防止该事件被执行两次*/
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                }
                return false;
            }
        });
//        etContent.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                mScrollLayout.setToOpen();
//            }
//        });

        mapView =findViewById(R.id.main_mapView);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                //必须设置地图样式
//                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                mapboxMap.setStyle(new Style.Builder().fromUri("asset://newstyle.json"), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapStyle = style;
                        addBikesToMap(mapStyle);
//                        initCoordinates();

                        MainActivity.this.mapboxMap = mapboxMap;
                        Button btnSeek = (Button) findViewById(R.id.btnSeek);
                        btnSeek.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String content = etContent.getText().toString();
                                Point pointa = Point.fromLngLat(119.86083984375,31.63467554954133);
                                Point pointb = Point.fromLngLat(118.38867187500001,26.86328062676624);

//                                Point pointa = Point.fromLngLat(120.157470703125,31.87755764334002);
//                                Point pointb = Point.fromLngLat(118.69628906249999,27.068909095463365);
//                                    getRoute(pointa,pointb);
//
//                                Log.d("content",content);
//                                //测试网络请求
////                                String url = getString(R.string.web_url)+"/location/matchPhrase";
//                                String url = "http://kangkangtk.gnway.cc/geolocation/matchPhrase";
//                                OkHttpUtils.get().url(url)
//                                        // .addParams("username", "admin")
//                                        .addParams("query", content).build().execute(new StringCallback() {
//                                    @Override
//                                    public void onError(Call call, Exception e, int id) {
//                                    }
//
//                                    @Override
//                                    public void onResponse(String response, int id) {
//                                        try {
//                                            JSONObject jsonobj = new  JSONObject(response);
//                                            JSONObject coorobj = jsonobj.getJSONArray("content").getJSONObject(0).getJSONObject("location");
//                                            Log.d("response", coorobj.getString("lat"));
//
//                                            Log.d("mLocationEngine", String.valueOf(lastLocation.getLatitude())+String.valueOf(lastLocation.getLongitude()));
//                                             String Latitude = String.valueOf(lastLocation.getLatitude());
//                                             String Longitude = String.valueOf(lastLocation.getLongitude());
//
////                                            String pathurl = getString(R.string.http_url)+"6005/api/latlng/cjbchbDijkstra";
//                                            String pathurl = "http://kangkangtk.gnway.cc/graphapi/latlng/cjbchbDijkstra";
//                                            OkHttpUtils.get().url(pathurl)
//                                                    .addParams("source_x", String.valueOf(lastLocation.getLongitude()))
//                                                    .addParams("source_y", String.valueOf(lastLocation.getLatitude()))
//                                                    .addParams("target_x", coorobj.getString("lon"))
//                                                    .addParams("target_y", coorobj.getString("lat")).build().execute(new StringCallback() {
//                                                @Override
//                                                public void onError(Call call, Exception e, int id) {
//                                                }
//
//                                                @Override
//                                                public void onResponse(String response, int id) {
//                                                    try {
//                                                        initCoordinates(response);
//                                                        initLineLayer(style);
//
//                                                        drawLineGradient(mapStyle);
//                                                    } catch (JSONException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            });
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
                            }
                        });

                        //绘制面图层
//                        initFillLayer(mapStyle,false);
                        initExtrusionLayer(mapStyle);
                        List<List<Point>> points = new ArrayList<>();
                        points.add(Config.POLYGON_COORDINATES);
                        fillgeojson.setGeoJson(Polygon.fromLngLats(points));


                        addNeighborhoodParkingZonesToMap(style, addOutlinesToNeighboorhoodParkingArea);
                        initDashWalkingDirectionLineLayer(style);



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
                                    Toast.makeText(getApplicationContext(), feature.properties().toString(),      Toast.LENGTH_SHORT).show();
                                    Log.d("sag", feature.toString());

                                    // 确保特征feature具有已定义的属性
                                    if (feature.properties() != null) {
                                        for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                                            // 打印所有属性
                                            Log.d("sag", String.format("%s = %s", entry, entry.getValue()));
                                        }
                                    }
                                }
                                return false;
                            }
                        });


                    }
                });
                LatLng latlng = new LatLng(30.15442889806117,120.47381846373213);
                @SuppressLint("Range") CameraPosition cameraposition = new CameraPosition.Builder()
                        .target(latlng)
                        .zoom(15)
                        .bearing(0)
                        .tilt(90)
                        .build();
                mapboxMap.setCameraPosition(cameraposition);





                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Icon icon = iconFactory.fromResource(R.mipmap.sign);



                mapboxMap.addMarker(new MarkerOptions()
                        .title("title")
                        .snippet("snippet")
                        .icon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.map_default_map_marker))
//                        .icon(icon)
                        .position(latlng));






            }

            @SuppressWarnings( {"MissingPermission"})
            private void enableLocationComponent(@NonNull Style loadedMapStyle) {
            }

        });



//        locationEngine = new LostLocationEngine(MainActivity.this);
//
//        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
//
//        locationEngine.setInterval(5000);
//
//        locationEngine.activate();
//
//        Location lastLocation = locationEngine.getLastLocation();



        initView();

//        //全局初始化 okhttp框架
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
////                .addInterceptor(new LoggerInterceptor("TAG"))
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                //其他配置
//                .build();
//
//        OkHttpUtils.initClient(okHttpClient);
    }





    private void initView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.root);
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        text_foot = (TextView) findViewById(R.id.text_foot);
        TextView seek = (TextView) findViewById(R.id.tvSeek);
//        final EditText etContent = (EditText) findViewById(R.id.etContent);
//        etContent.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                mScrollLayout.setToOpen();
//            }
//        });
//        seek.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,SeekActivity.class);
//                startActivity(intent);
//            }
//        });
        ListEntity listdata = new ListEntity();
        ListView listView = (ListView) findViewById(R.id.list_view);
        for (int i= 1;i<=60;i++){
            listdata.setName("大傻吊"+i);
            listdata.setDatacontent("大傻吊"+i);
            dataList.add("腾讯游戏发布未成年人寒假限玩通知"+i);
        }
        Log.d("dashadiao", String.valueOf(dataList));
        listView.setAdapter(new ListviewAdapter(this,dataList));
//        Button button = (Button) findViewById(R.id.btn_go_second);

        // find view
        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rgrg);
        // init fragment
//        mFragments = new ArrayList<>(4);
//        mFragments.add(BlankFragment.newInstance("今日"));
//        mFragments.add(BlankFragment.newInstance("记录"));
//        mFragments.add(BlankFragment.newInstance("通讯录"));
//        mFragments.add(BlankFragment.newInstance("设置"));
        // init view pager
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

        /**设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.55));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(this, 108));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();

        mScrollLayout.getBackground().setAlpha(0);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.scrollToExit();
                Log.d("MainActivity","Hello Debug");
            }
        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, SecondActivity.class));
//            }
//        });
        text_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.setToOpen();
            }
        });
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
                    RadioButton radioButton = (RadioButton)findViewById(group.getCheckedRadioButtonId());
                    Log.d("点击跳转", radioButton.getText().toString());
                    if(radioButton.getText().toString().equals("记录") ){
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                    }
                    if(radioButton.getText().toString().equals("web") ){
                        startActivity(new Intent(MainActivity.this, VideoActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if(radioButton.getText().toString().equals("设置") ){
                        startActivity(new Intent(MainActivity.this, NavigationActivity.class));
//                        startActivity(new Intent(MainActivity.this, ArActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if(radioButton.getText().toString().equals("登录") ){
//                        startActivity(new Intent(MainActivity.this, ArActivity.class));
                        startActivity(new Intent(MainActivity.this, loginActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
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



    private void initCoordinates( String point) throws JSONException {
        JSONObject jsonobj = new  JSONObject(point);
        JSONArray coorobj = jsonobj.getJSONArray("coordinates");
        routeCoordinates = new ArrayList<>();
        try {
            int length = coorobj.length();
            for(int i = 0; i < length; i++){//遍历JSONArray
                JSONArray oj = coorobj.getJSONArray(i);
                routeCoordinates.add(Point.fromLngLat(Double.valueOf(oj.get(0).toString()), Double.valueOf(oj.get(1).toString())));

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

//        routeCoordinates.add(Point.fromLngLat(-77.044211, 38.852924));
//        routeCoordinates.add(Point.fromLngLat(-77.045659, 38.860158));
//        routeCoordinates.add(Point.fromLngLat(-77.044232, 38.862326));
//        routeCoordinates.add(Point.fromLngLat(-77.040879, 38.865454));
//        routeCoordinates.add(Point.fromLngLat(-77.039936, 38.867698));
//        routeCoordinates.add(Point.fromLngLat(-77.040338, 38.86943));
//        routeCoordinates.add(Point.fromLngLat(-77.04264, 38.872528));
//        routeCoordinates.add(Point.fromLngLat(-77.03696, 38.878424));
//        routeCoordinates.add(Point.fromLngLat(-77.032309, 38.87937));
//        routeCoordinates.add(Point.fromLngLat(-77.030056, 38.880945));
//        routeCoordinates.add(Point.fromLngLat(-77.027645, 38.881779));
//        routeCoordinates.add(Point.fromLngLat(-77.026946, 38.882645));
//        routeCoordinates.add(Point.fromLngLat(-77.026942, 38.885502));
//        routeCoordinates.add(Point.fromLngLat(-77.028054, 38.887449));
//        routeCoordinates.add(Point.fromLngLat(-77.02806, 38.892088));
//        routeCoordinates.add(Point.fromLngLat(-77.03364, 38.892108));
//        routeCoordinates.add(Point.fromLngLat(-77.033643, 38.899926));
    }

    private void drawImageLine(Style style) {
        if (style == null) return;

        //Source
        featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{Feature.fromGeometry(LineString.fromLngLats(routeCoordinates))});
        geoJsonSource.setGeoJson(featureCollection);
        //Layer
        lineLayer.setProperties(
                lineCap(LINE_CAP_ROUND),
                lineJoin(LINE_JOIN_ROUND),
                lineWidth(14f),
                PropertyFactory.linePattern(LINE_IMAGE_ID)
        );
    }

    private void drawLineGradient(Style style) {
        if (style == null) return;
        //Source
        featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(routeCoordinates)));
        geoJsonSource.setGeoJson(featureCollection);
        //Layer
        lineLayer.setProperties(
                lineCap(LINE_CAP_ROUND),
                lineJoin(LINE_JOIN_ROUND),
                lineWidth(14f),
                lineGradient(interpolate(
                        linear(), lineProgress(),
                        stop(0f, rgb(6, 1, 255)), // blue
                        stop(0.1f, rgb(59, 118, 227)), // royal blue
                        stop(0.3f, rgb(7, 238, 251)), // cyan
                        stop(0.5f, rgb(0, 255, 42)), // lime
                        stop(0.7f, rgb(255, 252, 0)), // yellow
                        stop(1f, rgb(255, 30, 0)) // red
                )));
    }

    private void drawNormalLine(Style style) {
        if (style == null) return;

        //Source
        featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{Feature.fromGeometry(LineString.fromLngLats(routeCoordinates))});
        geoJsonSource.setGeoJson(featureCollection);
        //Layer
        lineLayer.setProperties(
                //应用于线条的模糊，以密度无关的像素为单位
//                PropertyFactory.lineBlur(0f),
                /*
                 * 线帽
                 * 布局 属性。 可选的枚举。一，，。默认为"butt"。 "butt""round""square"
                 * 行尾的显示。
                 *
                 * "butt"：
                 * 端部带有正方形的笔帽，绘制到直线的确切端点。
                 *
                 * "round"：
                 * 具有圆形末端的盖，该圆形末端以线宽的一半的半径超出线的端点绘制，并以线的端点为中心。
                 *
                 * "square"：
                 * 端部带有正方形的笔帽，该笔帽以超出线宽一半的距离画出线的端点。
                 */
                lineCap(Property.LINE_CAP_BUTT),
                //线条的绘制颜色
                lineColor(Color.RED),
                /*
                 * 行破折号
                 * 油漆 属性。大于或等于的数字s的 可选数组。线宽单位。由line-pattern 禁用。可转换的。
                 * 指定形成虚线图案的交替虚线和间隙的长度。长度随后通过线宽缩放。要将破折号长度转换为像素，请将该长度乘以当前线宽。
                 * 请注意，lineMetrics: true指定了GeoJSON的源不会将虚线渲染到预期的比例。
                 * 另请注意，仅在整数缩放级别下才会评估与缩放相关的表达式。
                 */
//                PropertyFactory.lineDasharray(new Float[]{0.01f, 2f}),
                //行宽 以像素为单位。
                lineWidth(14f),
                //在线的实际路径之外绘制线框。值表示内部间隙的宽度。
//                PropertyFactory.lineGapWidth(2f),
                //线梯度 定义用于为线要素着色的渐变。只能与指定的GeoJSON源一起使用"lineMetrics": true。
//                PropertyFactory.lineGradient(interpolate(
//                        linear(), lineProgress(),
//                        stop(0f, rgb(6, 1, 255)), // blue
//                        stop(0.1f, rgb(59, 118, 227)), // royal blue
//                        stop(0.3f, rgb(7, 238, 251)), // cyan
//                        stop(0.5f, rgb(0, 255, 42)), // lime
//                        stop(0.7f, rgb(255, 252, 0)), // yellow
//                        stop(1f, rgb(255, 30, 0)) // red
//                )),
                /*
                 * 线连接
                 * 布局 属性。 可选的枚举。一，，。默认为"miter"。 "bevel" "round" "miter"
                 * 连接时显示线。
                 *
                 * "bevel"：
                 * 具有平方根末端的连接，该末端在线宽的一半处超出线的端点。
                 *
                 * "round"：
                 * 具有圆形末端的连接，该末端以线宽度的一半的半径超出线的端点绘制，并以线的端点为中心。
                 *
                 * "miter"：
                 * 具有尖角的角的连接，其外侧超出路径的端点，直到它们汇合为止。
                 */
                lineJoin(Property.LINE_JOIN_MITER),
                //线斜接极限 用于自动将斜接连接转换为斜角连接以形成锐角。
//                PropertyFactory.lineMiterLimit(0f),
                //行数限制 用于自动将圆角连接转换为斜角较小的斜角连接。
//                PropertyFactory.lineRoundLimit(0f),
                /*
                 * 线偏移
                 * 油漆 属性。 可选号码。以像素为单位。默认为。支持和表达。可转换的。
                 * 线的偏移量。对于线性特征，正值使线相对于线的方向向右偏移，负值向左偏移。
                 */
//                PropertyFactory.lineOffset(0f),
                //填充不透明度
//                PropertyFactory.lineOpacity(0.7f),
                //填充模式  可选的resolveImage。 Sprite中用于绘制图像填充的图像名称。对于无缝图案，图像的宽度和高度必须是两倍（2、4、8，...，512）。请注意，仅在整数缩放级别会评估与缩放相关的表达式。
//                PropertyFactory.linePattern(LINE_IMAGE_ID),
                //填充排序键 基于此值按升序对要素进行排序。具有较高排序键的功能将显示在具有较低排序键的功能上方。
//                PropertyFactory.lineSortKey(),
                //填充翻译 几何的偏移量。值为[x，y]，其中负数分别表示左和上。 以像素为单位
//                PropertyFactory.lineTranslate(new Float[]{0f, 0f}),
                /*
                 * 填充翻译锚 可选的枚举 "map" "viewport" 之一。默认为"map"。需要设置line-translate。
                 * "map"：填充相对于地图进行转换。
                 * "viewport"：填充相对于视口平移。
                 */
                PropertyFactory.lineTranslateAnchor(Property.LINE_TRANSLATE_ANCHOR_MAP),
                //可见性
                PropertyFactory.visibility(Property.VISIBLE)
        );
    }

    private void initLineLayer(Style style) {
        if (style == null) return;
        //source
        geoJsonSource = new GeoJsonSource(LINE_SOURCE_ID, new GeoJsonOptions().withLineMetrics(true));
        style.addSource(geoJsonSource);
        //image
        Drawable drawable = getResources().getDrawable(R.drawable.ic_circle);
        style.addImage(LINE_IMAGE_ID, drawable);
        //Layer
        lineLayer = new LineLayer(LINE_LAYER_ID, LINE_SOURCE_ID);
        style.addLayer(lineLayer);
    }
    //初始化面图层
    private void initFillLayer (Style style, boolean isUseImage){
        //Source
        fillgeojson = new GeoJsonSource(FILL_SOURCE_ID);
        style.addSource(fillgeojson);
        //Layer
        fillLayer = new FillLayer(FILL_LAYER_ID, FILL_SOURCE_ID).withProperties(
                //填充抗锯齿 默认为true
                PropertyFactory.fillAntialias(true),
                //填充内部填充色
                fillColor(Color.parseColor("#3bb2d0")),
                //填充不透明度
                fillOpacity(0.5f),
                //填充轮廓线颜色
                PropertyFactory.fillOutlineColor(Color.parseColor("#AF0000")),
                //填充模式  可选的resolveImage。 Sprite中用于绘制图像填充的图像名称。对于无缝图案，图像的宽度和高度必须是两倍（2、4、8，...，512）。请注意，仅在整数缩放级别会评估与缩放相关的表达式。
//                PropertyFactory.fillPattern(FILL_IMAGE_ID),
                //填充排序键 基于此值按升序对要素进行排序。具有较高排序键的功能将显示在具有较低排序键的功能上方。
//                PropertyFactory.fillSortKey(),
                //填充翻译 几何的偏移量, 默认为0,0。值为[x，y]，其中负数分别表示左和上。 以像素为单位
                PropertyFactory.fillTranslate(new Float[]{0f, 0f}),
                /*
                 * 填充翻译锚 可选的枚举 "map" "viewport" 之一。默认为"map"。需要设置fill-translate。
                 * "map"：填充相对于地图进行转换。
                 * "viewport"：填充相对于视口平移。
                 */
                PropertyFactory.fillTranslateAnchor(Property.FILL_TRANSLATE_ANCHOR_MAP),
                //可见性 默认为可见
                PropertyFactory.visibility(Property.VISIBLE)
        );
        if (isUseImage){
            //image
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher_round);
            style.addImage(FILL_IMAGE_ID, drawable);
            fillLayer.setProperties(PropertyFactory.fillPattern(FILL_IMAGE_ID));
            Toast.makeText(this, "等待图源加载完成...", Toast.LENGTH_SHORT).show();
        }
        style.addLayer(fillLayer);
    }
    static final class Config {
        static final int BLUE_COLOR = Color.parseColor("#3bb2d0");
        static final int RED_COLOR = Color.parseColor("#AF0000");

        //多边形的点的集合
        static final List<Point> POLYGON_COORDINATES = new ArrayList<Point>() {
            {
                add(Point.fromLngLat(120.004297646165838, 30.977621838316924));
                add(Point.fromLngLat(120.190097041947112, 30.984141115361879));
                add(Point.fromLngLat(120.281366920576502, 30.964583284227011));
                add(Point.fromLngLat(120.346559691026073, 30.883092321165048));
                add(Point.fromLngLat(120.340040413981114, 30.808120635148043));
                add(Point.fromLngLat(120.245510896829245, 30.742927864698473));
                add(Point.fromLngLat( 120.111865717407625, 30.65817726311403));
                add(Point.fromLngLat(119.99777836912088, 30.694033286861295));
                add(Point.fromLngLat(119.945624152761226, 30.775524249923258));
                add(Point.fromLngLat(119.899989213446517, 30.909169429344875));
                add(Point.fromLngLat(120.004297646165838, 30.977621838316924));
            }
        };
        //孔洞点的集合
        static final List<List<Point>> HOLE_COORDINATES = new ArrayList<List<Point>>() {
            {
                add(new ArrayList<>(new ArrayList<Point>() {
                    {
                        add(Point.fromLngLat(55.30084858315658, 25.256531695820797));
                        add(Point.fromLngLat(55.298280197635705, 25.252243254705405));
                        add(Point.fromLngLat(55.30163885563897, 25.250501032248863));
                        add(Point.fromLngLat(55.304059065092645, 25.254700192612702));
                        add(Point.fromLngLat(55.30084858315658, 25.256531695820797));
                    }
                }));
                add(new ArrayList<>(new ArrayList<Point>() {
                    {
                        add(Point.fromLngLat(55.30173763969924, 25.262517391695198));
                        add(Point.fromLngLat(55.301095543307355, 25.26122200491396));
                        add(Point.fromLngLat(55.30396028103232, 25.259479911263526));
                        add(Point.fromLngLat(55.30489872958182, 25.261132667394975));
                        add(Point.fromLngLat(55.30173763969924, 25.262517391695198));
                    }
                }));
            }
        };
    }

    //初始化3d图层

    private void initExtrusionLayer(Style style){
        //Source
        fillgeojson = new GeoJsonSource("ExtrusionLayer");
        style.addSource(fillgeojson);
        FillExtrusionLayer courseExtrusionLayer = new FillExtrusionLayer("course", "ExtrusionLayer");
        courseExtrusionLayer.setProperties(
                fillExtrusionColor(Color.parseColor("#3bb2d0")),
                fillExtrusionHeight(10000F),
                fillExtrusionBase(0F),
                fillExtrusionOpacity(0.7f)

        );
        style.addLayer(courseExtrusionLayer);
    }



    private void drawNavigationPolylineRoute(DirectionsRoute route) {
        if (mapboxMap.getStyle() != null) {
            GeoJsonSource navLineRouteSource = mapboxMap.getStyle().getSourceAs(WALK_TO_VEHICLE_ROUTE_SOURCE_ID);
            Feature routeline = Feature.fromGeometry(LineString.fromPolyline(
                    route.geometry(), Constants.PRECISION_6));
            if (navLineRouteSource != null) {
                navLineRouteSource.setGeoJson(Feature.fromGeometry(LineString.fromPolyline(
                        route.geometry(), Constants.PRECISION_6)));
            }
        }
    }

    private void initDashWalkingDirectionLineLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(WALK_TO_VEHICLE_ROUTE_SOURCE_ID));
        loadedMapStyle.addLayerAbove(new LineLayer(WALK_TO_VEHICLE_ROUTE_LINE_LAYER_ID,
                WALK_TO_VEHICLE_ROUTE_SOURCE_ID)
                .withProperties(
                        lineWidth(6f),
                        lineOpacity(.6f),
                        lineCap(LINE_CAP_ROUND),
                        lineJoin(LINE_JOIN_ROUND),
                        lineColor(Color.parseColor("#d742f4")),
                        lineDasharray(new Float[] {1f, 2f})), NEIGHBORHOOD_PARKING_ZONE_FILL_LAYER_ID);
    }
    private void addNeighborhoodParkingZonesToMap(@NonNull Style loadedMapStyle, @NonNull boolean addLineOutline) {
        // Use the Mapbox Dataset and Tileset sections of your Mapbox Studio account to pull in
        // your own data.
        loadedMapStyle.addSource(new VectorSource(NEIGHBORHOOD_PARK_ZONE_SOURCE_ID,
                "mapbox://langsmith.cjf7o0ajv084v3uojct9q3k8b-0o6pu"));
        FillLayer neighborhoodParkingZone = new FillLayer(NEIGHBORHOOD_PARKING_ZONE_FILL_LAYER_ID,
                NEIGHBORHOOD_PARK_ZONE_SOURCE_ID)
                .withProperties(fillOpacity(0.4f),
                        fillColor(Color.parseColor("#45AAE9")));
        neighborhoodParkingZone.setSourceLayer("GoShare_Bike_Neighborhood_Parkin");
        loadedMapStyle.addLayerBelow(neighborhoodParkingZone, NO_PARK_ZONE_FILL_LAYER_ID);

        if (addLineOutline) {
            LineLayer neighborhoodZoneLineOutline = new LineLayer(NEIGHBORHOOD_PARKING_ZONE_LINE_LAYER_ID,
                    NEIGHBORHOOD_PARK_ZONE_SOURCE_ID)
                    .withProperties(
                            lineWidth(2f),
                            lineCap(LINE_CAP_ROUND),
                            lineJoin(LINE_JOIN_ROUND),
                            lineColor(Color.parseColor("#006db2")));
            neighborhoodZoneLineOutline.setSourceLayer("GoShare_Bike_Neighborhood_Parkin");
            loadedMapStyle.addLayerBelow(neighborhoodZoneLineOutline, NO_PARK_ZONE_FILL_LAYER_ID);
        }
    }


    private void setVehicleWalkToDistance(DirectionsResponse response) {
        TextView walkingDistance = (TextView)findViewById(R.id.mileage_walking_distance_to_vechicle);
        if (response.routes().get(0).distance() != null) {
            walkingDistance.setText(" " + String.format(getString(R.string.walking_miles),
                    new DecimalFormat("0.0").format(TurfConversion.convertLength(
                            response.routes().get(0).distance(),
                            "meters", "miles"))));
        } else {
            walkingDistance.setText(getString(R.string.travel_time_unknown));
        }
    }






    private void getRoute(Point origin, Point destination){



        NavigationRoute.builder(this)
                .accessToken("sk.eyJ1IjoiemhhbmdtbSIsImEiOiJjbGFrbXM3NHYwNnM3M3BrMHVwaWR3NnFwIn0.ddu0apyK2st4E44oSwBLqw")
                .alternatives(false)
                .annotations()
                .baseUrl("http://kangkangtk.gnway.cc/")
                .exclude("mapbox/driving")
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DirectionsResponse> call, Response<DirectionsResponse> response) {


                if (response.body() == null){
                    Log.e(TAG, "No route found, check user access token");
                    return;
                }else{
                    if (response.body().routes().size() == 0){
                        Log.e(TAG, "No route found, check user access token");
                        return;
                    }

                    DirectionsRoute currentRoute = response.body().routes().get(0);

                    if (navigationMapRoute != null){
                    }else{
                        drawNavigationPolylineRoute(currentRoute);
                        setVehicleWalkToDistance(response.body());
//                        navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap);
//                        navigationMapRoute.addRoute(currentRoute);
                    }


                }

            }

            @Override
            public void onFailure(retrofit2.Call<DirectionsResponse> call, Throwable t) {

                Log.e (TAG, "Error:" + t.getMessage());

            }
        });

    }


    private void addBikesToMap(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage(INDIVIDUAL_BIKE_ICON_IMAGE_ID, BitmapFactory.decodeResource(
                mapView.getResources(), R.drawable.bike_icon));
        // Use the Mapbox Dataset and Tileset sections of your Mapbox Studio account to pull in
        // your own data.
        loadedMapStyle.addSource(new VectorSource(INDIVIDUAL_BIKE_SOURCE_ID,
                "mapbox://langsmith.cjf7bdm5a110a30mcclh7p0rj-06ykj"));
        SymbolLayer bikeLocationSymbolLayer = new SymbolLayer(INDIVIDUAL_BIKE_SYMBOL_LAYER_ID,
                INDIVIDUAL_BIKE_SOURCE_ID)
                .withProperties(
                        iconImage(INDIVIDUAL_BIKE_ICON_IMAGE_ID),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                );
        bikeLocationSymbolLayer.setSourceLayer("GoShare_Bike_Locations");
        loadedMapStyle.addLayer(bikeLocationSymbolLayer);
    }







}