package com.yinglan.scrolllayout.demo;

import android.app.Application;

import com.dds.skywebrtc.SkyEngineKit;
import com.yinglan.scrolllayout.demo.core.util.CrashHandler;
import com.yinglan.scrolllayout.demo.core.voip.VoipEvent;
import com.yinglan.scrolllayout.demo.net.HttpRequestPresenter;
import com.yinglan.scrolllayout.demo.net.urlconn.UrlConnRequest;

public class AppVdeo extends Application {

    private static AppVdeo appvdeo;
    private String username = "";
    private String roomId = "";
    private String otherUserId = "";

    @Override
    public void onCreate() {
        super.onCreate();
        appvdeo = this;
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
        // 初始化网络请求
        HttpRequestPresenter.init(new UrlConnRequest());
        // 初始化信令
        SkyEngineKit.init(new VoipEvent());

    }

    public static AppVdeo getInstance() {
        return appvdeo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }
}
