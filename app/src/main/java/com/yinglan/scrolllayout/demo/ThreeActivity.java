package com.yinglan.scrolllayout.demo;

import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionBase;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonElement;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
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
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.yinglan.scrolllayout.ScrollLayout;
import com.yinglan.scrolllayout.demo.util.ScreenUtil;
import com.yinglan.scrolllayout.demo.viewpager.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThreeActivity extends AppCompatActivity {

    private ScrollLayout mScrollLayout;
    private TextView text_foot;
    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;
    private MapboxMap mapboxMap;
    private MapView mapView;
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
    private FillLayer fillLayer;
    private GeoJsonSource fillgeojson;



    private static final String LINE_SOURCE_ID = "line-source-id";
    private static final String LINE_LAYER_ID = "line-layer-id";
    private static final String LINE_IMAGE_ID = "line-image-id";


    private static final String FILL_SOURCE_ID = "fill-source-id";
    private static final String FILL_LAYER_ID = "fill-layer-id";
    private static final String FILL_IMAGE_ID = "fill-image-id";


    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            Log.d("currentProgresscurrentProgress", String.valueOf(currentProgress));
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
        setContentView(R.layout.activity_three);

//        WebView webview = (WebView) findViewById(R.id.cesiumwebview);
//        WebSettings webSettings = webview.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//
//        //其他有任何需要设置可自行添加
//
//        webview.setWebViewClient(new WebViewClient(){
//
//            @Override
//
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//
//                super.onReceivedError(view, request, error);
//
//            }
//
//
//
//
//            @Override
//
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//
//                handler.proceed();
//
//            }
//
//        });
//
//        webview.loadUrl("http://kangkangtk.gnway.cc/SpatialInformation/");

        mapView =findViewById(R.id.main_mapView);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                //必须设置地图样式
//                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                mapboxMap.setStyle(new Style.Builder().fromUri("asset://style.json"), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapStyle = style;
//                        initCoordinates();


                        //绘制面图层
//                        initFillLayer(mapStyle,false);
                        initExtrusionLayer(mapStyle);
                        List<List<Point>> points = new ArrayList<>();
                        points.add(MainActivity.Config.POLYGON_COORDINATES);
                        fillgeojson.setGeoJson(Polygon.fromLngLats(points));



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
                        .bearing(90)
                        .tilt(90)
                        .build();
                mapboxMap.setCameraPosition(cameraposition);






            }
        });
        initView();
    }

    private void initView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.root);
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        text_foot = (TextView) findViewById(R.id.text_foot);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_view);
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
        mAdapter = new ThreeActivity.MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);




        /**设置 setting*/
        mScrollLayout.setMinOffset(10);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.7));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(this, 90));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();

        mScrollLayout.getBackground().setAlpha(0);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.scrollToExit();
            }
        });

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
                    RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    Log.d("点击跳转", radioButton.getText().toString());
                    if (radioButton.getText().toString().equals("记录")) {
                        startActivity(new Intent(ThreeActivity.this, SecondActivity.class));
                    }
                    if (radioButton.getText().toString().equals("web")) {
                        startActivity(new Intent(ThreeActivity.this, ThreeActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if (radioButton.getText().toString().equals("首页")) {
                        startActivity(new Intent(ThreeActivity.this, NaviRouteActivity.class));
                        Log.d("点击跳转", String.valueOf(group.getChildAt(i).getId()));
                    }
                    if (radioButton.getText().toString().equals("导航")) {
                        startActivity(new Intent(ThreeActivity.this, MapActivity.class));
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
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
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
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
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
                PropertyFactory.lineColor(Color.RED),
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
                PropertyFactory.fillColor(Color.parseColor("#3bb2d0")),
                //填充不透明度
                PropertyFactory.fillOpacity(0.5f),
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

//    初始化3d图层

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
}
