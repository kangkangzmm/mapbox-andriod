package com.yinglan.scrolllayout.demo;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.QbSdk;
import com.yinglan.scrolllayout.demo.feature.BaseWebActivity;

public class WebViewActivity extends BaseWebActivity {
    private static final String M_TAG = "X5WebViewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.setTAG(M_TAG);
        super.onCreate(savedInstanceState);
        startDefinedUrl();
    }

    @Override
    protected void initWebView() {
        super.initWebView();
        Toast.makeText(this, mWebView.getIsX5Core() ?
                "X5内核: " + QbSdk.getTbsVersion(this) : "SDK系统内核" , Toast.LENGTH_SHORT).show();
    }

    private void startDefinedUrl() {
        Intent intent = getIntent();
        if (intent != null) {
//            String url = intent.getStringExtra("url");
            String webview_url=getIntent().getStringExtra("weburl");//得到URL
            if (mWebView != null) {
                mWebView.loadUrl(webview_url);
            }
        } else {
            Log.i(M_TAG, "Intent is null");
        }
    }
}

//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.tencent.smtt.export.external.TbsCoreSettings;
//import com.tencent.smtt.sdk.QbSdk;
//import com.tencent.smtt.sdk.WebChromeClient;
//import com.tencent.smtt.sdk.WebSettings;
//import com.tencent.smtt.sdk.WebView;
//
//import java.util.HashMap;
//
//public class WebViewActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.webview);
//        QbSdk.setDownloadWithoutWifi(true);
//        QbSdk.initX5Environment(WebViewActivity.this, new QbSdk.PreInitCallback() {
//            @Override
//            public void onCoreInitFinished() {
//                // 内核初始化完成，可能为系统内核，也可能为系统内核
//            }
//
//            /**
//             * 预初始化结束
//             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
//             * @param isX5 是否使用X5内核
//             */
//            @Override
//            public void onViewInitFinished(boolean isX5) {
//                if(isX5){
//
//                    //true
//                    Log.e("腾讯X5", " onViewInitFinished 加载 成功 "+isX5);
//                }else{
//
//
//                    Log.e("腾讯X5", " onViewInitFinished 加载 失败！！！使用原生安卓webview "+isX5);
//                }
//
//            }
//        });
//        // 在调用TBS初始化、创建WebView之前进行如下配置
//        HashMap map = new HashMap();
//        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
//        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
//        QbSdk.initTbsSettings(map);
//        WebView webview = (WebView) findViewById(R.id.webview1);
//        webview.setWebChromeClient(new WebChromeClient());
//        WebSettings webSettings = webview.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptEnabled(true); // 支持Javascript 与js交互
//
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 支持通过JS打开新窗口
//
//        webSettings.setAllowFileAccess(true); // 设置可以访问文件
//
//        webSettings.setSupportZoom(true); // 支持缩放
//
//        webSettings.setBuiltInZoomControls(true); // 设置内置的缩放控件
//
//        webSettings.setUseWideViewPort(true); // 自适应屏幕
//
//        webSettings.setSupportMultipleWindows(true); // 多窗口
//
//        webSettings.setDefaultTextEncodingName("utf-8"); // 设置编码格式
//
//        webSettings.setAppCacheEnabled(true);
//
//        webSettings.setDomStorageEnabled(true);
//
//        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
//
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 缓存模式
////        webSettings.setUseWideViewPort(true);
////        webSettings.setDomStorageEnabled(true);
////        webSettings.setLoadWithOverviewMode(true);
////        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//
//        //其他有任何需要设置可自行添加
//
////        webview.setWebViewClient(new WebViewClient(){
////
////            @Override
////
////            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
////
////                super.onReceivedError(view, request, error);
////
////            }
////
////
////
////
////            @Override
////
////            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
////
////                handler.proceed();
////
////            }
////onCoreInitFinished
////        });
//
//        String webview_url=getIntent().getStringExtra("weburl");//得到URL
////        webview.loadUrl("http://kangkangtk.gnway.cc/indoor/");http://kangkangtk.gnway.cc/vuechat/#/addressbook
////        webview.loadUrl("http://kangkangtk.gnway.cc/vuechat/");
//        webview.loadUrl(webview_url);
////        webview.loadUrl("https://sandcastle.cesium.com/?src=3D%20Tiles%20Clipping%20Planes.html");
//
//        //该方法解决的问题是打开浏览器不调用系统浏览器，直接用webview打开
////        webview.setWebViewClient(new WebViewClient() {
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                view.loadUrl(url);
////                return false;
////            }
////        });
//    }
//}
