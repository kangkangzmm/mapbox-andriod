//package com.yinglan.scrolllayout.demo;
//
//
//import android.app.Application;
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//
//
///**
// * @author：luck
// * @date：2019-12-03 22:53
// * @describe：Application
// */
//
//public class App extends Application implements IApp, CameraXConfig.Provider {
//    private static final String TAG = App.class.getSimpleName();
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        PictureAppMaster.getInstance().setApp(this);
//    }
//
//    @Override
//    public Context getAppContext() {
//        return this;
//    }
//
//    @Override
//    public PictureSelectorEngine getPictureSelectorEngine() {
//        return new PictureSelectorEngineImp();
//    }
//
//    @NonNull
//    @Override
//    public CameraXConfig getCameraXConfig() {
//        return Camera2Config.defaultConfig();
//    }
//}