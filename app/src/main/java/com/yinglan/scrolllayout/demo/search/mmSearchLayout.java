package com.yinglan.scrolllayout.demo.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yinglan.scrolllayout.demo.R;
import com.yinglan.scrolllayout.demo.entity.ListEntity;
import com.yinglan.scrolllayout.demo.viewpager.ListviewAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;

public class mmSearchLayout extends LinearLayout {
    private  String msearch_hint;
    private  int msearch_baground;
    Context context;
    private View ib_searchtext_delete;
    private EditText et_searchtext_search;
    private LinearLayout searchview;
    private Button bt_text_search_back;
    private TextView tvclearolddata;


    //历史搜索
    private selfSearchGridView gridviewolddata;
    private SearchOldDataAdapter OldDataAdapter;
    private ArrayList<String> OldDataList =new ArrayList<String>();
    //热门搜索
    FlowLayout hotflowLayout;

    private String backtitle="取消",searchtitle="搜索";
    private OnClickListener TextViewItemListener;
    private int countOldDataSize=15;//默认搜索记录的条数， 正确的是传入进来的条数

    private List<String> dataList =new ArrayList<>();


    public mmSearchLayout(Context context) {
        super(context);
        this.context = context;
        InitView();
    }

    public mmSearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.searchmlist);
        msearch_hint = ta.getString(R.styleable.searchmlist_search_hint);
        msearch_baground = ta.getResourceId(R.styleable.searchmlist_search_baground,R.drawable.search_baground_shap);
        ta.recycle();


        InitView();
    }

    public mmSearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        InitView();
    }




    private void  InitView(){

        backtitle=getResources().getString(R.string.search_cancel);
        searchtitle=getResources().getString(R.string.search_verify);


        searchview =(LinearLayout) LayoutInflater.from(context).inflate(R.layout.mmsearchlayout, null);
        //把获得的view加载到这个控件中
        addView(searchview);
        //把两个按钮从布局文件中找到
        ib_searchtext_delete = (ImageView) searchview.findViewById(R.id.ib_searchtext_delete);
        et_searchtext_search = (EditText) searchview.findViewById(R.id.et_searchtext_search);
        et_searchtext_search.setBackgroundResource(msearch_baground);
        et_searchtext_search.setHint(msearch_hint);
        //搜索返回时一个按钮
        bt_text_search_back = (Button) searchview.findViewById(R.id.buttonback);
        //清除历史记录
        tvclearolddata = (TextView) searchview.findViewById(R.id.tvclearolddata);

        gridviewolddata=  (selfSearchGridView)searchview.findViewById(R.id.gridviewolddata);
        gridviewolddata.setSelector(new ColorDrawable(Color.TRANSPARENT));//去除背景点击效果

        hotflowLayout =  (FlowLayout)searchview.findViewById(R.id.id_flowlayouthot);

        setLinstener();


        ListView listView = (ListView) findViewById(R.id.list_view);
        ListEntity listdata = new ListEntity();
        //测试网络请求
        String url = "http://kangkangtk.gnway.cc/attachment/findallvideoinfoinfo";
        OkHttpUtils.get().url(url)
//                            .addParams("query", String.valueOf(searchtext)).build().execute(new StringCallback() {
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonobj = new  JSONObject(response);
                    JSONArray jsonarray = jsonobj.getJSONArray("data");
                    // for循环
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        listdata.setName(jsonObj.getString("name"));
                        listdata.setDatacontent(jsonObj.getString("address"));
                        dataList.add(jsonObj.getString("address")+",name:"+jsonObj.getString("name"));
                        ListviewAdapter adapter = new ListviewAdapter(context,dataList);
                        listView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    //文本观察者
    private class MyTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }
        //当文本改变时候的操作
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            //如果编辑框中文本的长度大于0就显示删除按钮否则不显示
            if(s.length() > 0){
                ib_searchtext_delete.setVisibility(View.VISIBLE);
                bt_text_search_back.setText(searchtitle);
            }else{
                ib_searchtext_delete.setVisibility(View.GONE);
                bt_text_search_back.setText(backtitle);
            }
        }

    }

    private void setLinstener() {


        //给删除按钮添加点击事件
        ib_searchtext_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_searchtext_search.setText("");
            }
        });
        //给编辑框添加文本改变事件
        et_searchtext_search.addTextChangedListener(new MyTextWatcher());


        et_searchtext_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    String searchtext = et_searchtext_search.getText().toString().trim();
                    //dosoming
//                    Toast.makeText(context, "搜索" +searchtext, Toast.LENGTH_SHORT).show();

                    executeSearch_and_NotifyDataSetChanged(searchtext);

                    return true;
                }
                return false;
            }
        });



        bt_text_search_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = et_searchtext_search.getText().toString().trim();
                if (bt_text_search_back.getText().toString().equals(searchtitle)) {
                    ListView listView = (ListView) findViewById(R.id.list_view);
                    ListEntity listdata = new ListEntity();
                    //测试网络请求
                    String url = "http://kangkangtk.gnway.cc/attachment/findallvideoinfoinfo";
                    OkHttpUtils.get().url(url)
//                            .addParams("query", String.valueOf(searchtext)).build().execute(new StringCallback() {
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonobj = new  JSONObject(response);
                                JSONArray jsonarray = jsonobj.getJSONArray("data");
                                // for循环
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonObj = jsonarray.getJSONObject(i);
                                    listdata.setName(jsonObj.getString("name"));
                                    listdata.setDatacontent(jsonObj.getString("address"));
                                    dataList.add(jsonObj.getString("address")+",name:"+jsonObj.getString("name"));
                                    ListviewAdapter adapter = new ListviewAdapter(context,dataList);
                                    listView.setAdapter(adapter);
                                    Toast.makeText(context, "点击button搜索" + searchtext, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
//                    for (int i= 1;i<=60;i++){
//                        listdata.setName("大傻吊"+i);
//                        listdata.setDatacontent("大傻吊"+i);
//                        dataList.add("腾讯游戏发布未成年人寒假限玩通知"+i);
//                    }
//                    Log.d("dashadiao", String.valueOf(dataList));
                    executeSearch_and_NotifyDataSetChanged(searchtext);
                } else {
                    Toast.makeText(context, "点击button  返回", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    if (sCBlistener != null)
                        sCBlistener.Back();
                }
            }
        });



        tvclearolddata.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "清除历史搜索记录", Toast.LENGTH_SHORT).show();
                if(sCBlistener!=null) {
                    OldDataList.clear();
                    OldDataAdapter.notifyDataSetChanged();
                    sCBlistener.ClearOldData();
                }
            }
        });




        TextViewItemListener = new OnClickListener(){
            @Override
            public void onClick(View v) {
                String string = ((TextView)v).getText().toString();

//                Toast.makeText(context, "Item点击"+string, Toast.LENGTH_SHORT).show();

                executeSearch_and_NotifyDataSetChanged(string);

            }
        };



        gridviewolddata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context, "历史记录点击"+OldDataList.get(position), Toast.LENGTH_SHORT).show();
                if(sCBlistener!=null){
                    sCBlistener.Search(OldDataList.get(position).trim());
                }
            }
        });

    }


    /**
     *
     * @param olddatalist  历史搜索数据集合
     * @param hotdata  热门搜索数据集合
     * @param sCb  事件处理监听
     */
    public void initData(List<String> olddatalist, List<String> hotdata, setSearchCallBackListener sCb){

        SetCallBackListener(sCb);

        hotflowLayout.removeAllViews();
        OldDataList.clear();
        if(olddatalist!=null)
            OldDataList.addAll(olddatalist);


//        countOldDataSize = OldDataList.size();

        OldDataAdapter = new SearchOldDataAdapter(context,OldDataList);
        gridviewolddata.setAdapter(OldDataAdapter);



        LayoutInflater mInflater = LayoutInflater.from(context);
        for (int i = 0; i < hotdata.size(); i++)
        {
            TextView tv = (TextView) mInflater.inflate(R.layout.suosou_item,hotflowLayout, false);
            tv.setText(hotdata.get(i));
            tv.setOnClickListener(TextViewItemListener);
            tv.getBackground().setLevel(MyRandom(1,5));
//            hotflowLayout.addView(tv);//热门搜索
        }
    }



    private void executeSearch_and_NotifyDataSetChanged(String str){
        if(sCBlistener!=null&&(!str.equals(""))){
            if (OldDataList.size() > 0 && OldDataList.get(0).equals(str)) {
            }
            else
            {
                if (OldDataList.size() == countOldDataSize&&OldDataList.size()>0)
                    OldDataList.remove(OldDataList.size() - 1);
                OldDataList.add(0, str);//把最新的添加到前面
                OldDataAdapter.notifyDataSetChanged();
                sCBlistener.SaveOldData(OldDataList);
            }
            sCBlistener.Search(str);
        }
    }




    /**
     * 生成随机数
     * @param max  最大值
     * @param min   最小值
     * @return
     */
    public int MyRandom(int min,int max){

        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }


    //对外开发的接口
//--------------------------------------------------------------------------------------

    /**
     * @author liuyunming
     */
    public interface setSearchCallBackListener{

        /**
         * @param str  搜索关键字
         */
        public void Search(String str);

        /**
         * 后退
         */
        public abstract void Back();

        /**
         * 清除历史搜索记录
         */
        public abstract void ClearOldData();

        /**
         * 保存搜索记录
         */
        public abstract void SaveOldData(ArrayList<String> AlloldDataList);

    }

    private setSearchCallBackListener sCBlistener;
    /**
     * 设置接口回调
     * @param sCb   setCallBackListener接口
     */
    private void SetCallBackListener(setSearchCallBackListener sCb){
        sCBlistener=sCb;
    }
}
