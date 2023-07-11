package com.yinglan.scrolllayout.demo;



import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yinglan.scrolllayout.demo.pic.FullyGridLayoutManager;
import com.yinglan.scrolllayout.demo.pic.GridImageAdapter;
import com.yinglan.scrolllayout.demo.utils.HttpUtil;


import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PicActivity extends AppCompatActivity {
    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private RecyclerView mRecyclerView;
    private PopupWindow pop;
    private Button bt;
    private String picname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pic);

        mRecyclerView = findViewById(R.id.recycler);

        initWidget();
//        EventBus.getDefault().register(this);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true,priority = 2)
//    public void onGetMessage(EventMessage  message) {
//        Log.d("aaaaaa00000000",message.toString());
//    }
    private void initWidget() {
        bt = (Button) findViewById(R.id.savebutton1);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);
        EditText editText1=(EditText)findViewById(R.id.editText1);
        EditText editText2=(EditText)findViewById(R.id.editText2);
        EditText editText3=(EditText)findViewById(R.id.editText3);
        EditText editText4=(EditText)findViewById(R.id.editText4);
        EditText editText5=(EditText)findViewById(R.id.editText5);
        EditText editText6=(EditText)findViewById(R.id.editText6);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);


                            PictureSelector.create(PicActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(PicActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(PicActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated methodstub
//                Log.d("匿名内部类", editText1.getText().toString());
                //延时方法三
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        Bundle bundle = getIntent().getBundleExtra("info_location");	//从货船intent上拿到集装箱
                        Double lat = bundle.getDouble("lon");
                        Double lon = bundle.getDouble("lat");//从集装箱里拿到数据picname
//                        Log.d("匿名内部类lat", String.valueOf(lat));
//                        Log.d("匿名内部类lon", String.valueOf(lon));
                        if(!picname.isEmpty()){
                            try {
                                JSONObject picresult  = new JSONObject(picname);
                                JSONObject picnameobj = picresult.getJSONObject( "result");
                                picname = picnameobj.getString("url");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

//                        Log.d("picname", picname);
                        String rsgstr = "{\"name\":"+ editText1.getText().toString() +","+"\"address\":" +editText2.getText().toString()+","+"\"phone\":" +editText4.getText().toString()+","+"\"imgurl\":"+picname+","+"\"videourl\":"+picname+","+"\"lat\":"+lat.toString()+","+"\"lon\":"+lon.toString()+"}";


//                        String rsgstr = "{\"name\":"+ editText1.getText().toString() +","+"\"address\":" +editText2.getText().toString()+","+"\"imgurl\":"+picname+","+"\"videourl\":"+picname+","+"\"lat\":"+lat.toString()+","+"\"lon\":"+lon.toString()+"}";
//                        Log.d("response", rsgstr);

                        String jsonString = "";
                        try {
                            JSONObject jsonObject = new JSONObject(rsgstr);
                            jsonString = jsonObject.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonString);
                        OkHttpClient client = new OkHttpClient(); // 创建一个okhttp客户端对象
                        Request request = new Request.Builder().post(body).url("http://kangkangtk.gnway.cc:80/attachment/addvideoinfo").build();
                        Call call = client.newCall(request); // 根据请求结构创建调用对象
                        // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) { // 请求失败
                                Log.d("response", "请求失败");
                                // 回到主线程操纵界面
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException { // 请求成功.setText("调用登录接口返回：\n"+resp));
                                Log.d("responsersgstr", response.toString());
                            }
                        });
                        this.cancel();

                    }
                }, 5000);
                finish();
            }


        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @SuppressLint("CheckResult")
        @Override
        public void onAddPicClick() {
            //获取写的权限
            RxPermissions rxPermission = new RxPermissions(PicActivity.this);
            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.granted) {// 用户已经同意该权限
                                //第一种方式，弹出选择和拍照的dialog
                                showPop();

                                //第二种方式，直接进入相册，但是 是有拍照得按钮的
//                                showAlbum();
                            } else {
                                Toast.makeText(PicActivity.this, "拒绝", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    private void showAlbum() {
        //参数很多，根据需要添加
        PictureSelector.create(PicActivity.this)
                .openGallery(PictureMimeType.ofAll())
//                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .previewVideo(true)
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.selectionMedia(selectList)// 是否传入已选图片
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void showPop() {
        View bottomView = View.inflate(PicActivity.this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(PicActivity.this)
                                .openGallery(PictureMimeType.ofAll())
//                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(PicActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调


                images = PictureSelector.obtainMultipleResult(data);
                selectList.addAll(images);

                File file = new File(images.get(0).getPath());
//                final Map<String, File> files = new HashMap<String, File>();
//                files.put("file",file);
                //测试网络请求
//                String url = "http://kangkangtk.gnway.cc/attachment/upload";

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
//                .addFormDataPart("name", "lisi")
                        .addFormDataPart("file", file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator)+1), RequestBody.create(MediaType.parse("*/*"), file)) // 第一个参数传到服务器的字段名，第二个你自己的文件名，第三个MediaType.parse("*/*")数据类型，这个是所有类型的意思,file就是我们之前创建的全局file，里面是创建的图片
                        .build();

                HttpUtil.uploadFile("http://kangkangtk.gnway.cc/attachment/upload", requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("=============");
                        System.out.println("异常：：");
                        e.printStackTrace();
                        //Toast.makeText(MainActivity.this, "上传异常", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        EventMessage message = new EventMessage(2, response.body().toString());
//
//                        EventBus.getDefault().postSticky(message);//把事件发出，通过eventbus将事件传递给该事件的订阅者使用
                        
//                        picname = response.body().toString();
                          picname = response.body().string();//获取字符串数据

//                        Toast.makeText(PicActivity.this, "上传成功"+picname, Toast.LENGTH_SHORT).show();
                    }
                });

//                OkHttpUtils.post()
//                        .url(url)
//                        .files("file", files)
//                        .build().execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Log.d("starong","agagaddfsfsfsgf");
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.d("starong","agagadgf");
//                    }
//                });


//                OkHttpClient okHttpClient = new OkHttpClient();
//                Log.d("imagePath", images.get(0).getPath());
//                File file = new File(images.get(0).getPath());
//                RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
//                RequestBody requestBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("file", images.get(0).getPath(), image)
//                        .build();
//                Request request = new Request.Builder()
//                        .url(url)
//                        .post(requestBody)
//                        .build();
//                try {
//                    Response response = okHttpClient.newCall(request).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                //selectList = PictureSelector.obtainMultipleResult(data);

                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void onClick(View view){
        Log.d("点击数据采集页面","点击数据采集页面");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
        }
    }

}
