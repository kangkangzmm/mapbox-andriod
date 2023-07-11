package com.yinglan.scrolllayout.demo.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    /**
     *
     * @param address  服务器地址
     * @param requestBody  请求体数据
     * @param callback  回调接口
     */
    public static void uploadFile(String address, RequestBody requestBody , okhttp3.Callback callback){

        //发送请求
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(360, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(360, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

}

