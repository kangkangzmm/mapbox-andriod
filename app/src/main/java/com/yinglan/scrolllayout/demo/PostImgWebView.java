package com.yinglan.scrolllayout.demo;

import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.QbSdk;
import com.yinglan.scrolllayout.demo.feature.BaseWebViewActivity;

public class PostImgWebView extends BaseWebViewActivity {
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
        Bundle bundle = getIntent().getBundleExtra("location");	//从货船intent上拿到集装箱
        Double lat = bundle.getDouble("lat");
        Double lon = bundle.getDouble("lon");//从集装箱里拿到数据
        mWebView.loadUrl("http://kangkangtk.gnway.cc/olapp/#/home/resource?lat="+lat+"&lon="+lon);
        mWebView.addJavascriptInterface(this,"postlonlat");
    }


    private class JsInterface {

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJs(String name) {
            Log.d("dddddd","ddddddd");
        }
    }
}
