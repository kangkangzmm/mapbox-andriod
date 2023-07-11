package com.yinglan.scrolllayout.demo.net;



import com.yinglan.scrolllayout.demo.model.UploadVideoResp;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Description：网络请求接口类
 * Created by kang on 2018/3/9.
 */

public interface ApiInterface {
    /**
     * 文件整块上传
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("attachment/upload/")
    Call<UploadVideoResp> uploadFile(@Part MultipartBody.Part file);
}
