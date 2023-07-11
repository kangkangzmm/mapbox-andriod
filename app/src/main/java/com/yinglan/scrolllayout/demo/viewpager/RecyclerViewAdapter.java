package com.yinglan.scrolllayout.demo.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.yinglan.scrolllayout.demo.R;
import com.yinglan.scrolllayout.demo.WebViewActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private AdapterView.OnItemClickListener mOnItemClickListener ;
    private int count = 0;
    private int weight = 0;

    public RecyclerViewAdapter(Context mContext,int count,int weight){
        this.mContext = mContext;
        this.count = count;
        this.weight = weight;
    }
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_listview, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(position ==0){
            holder.textView.setImageResource(R.mipmap.cesiumc);

//            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.textView.getLayoutParams();
//            lp.setMargins(10,0,0,0);

        }
        if(position ==1){
            holder.textView.setImageResource(R.mipmap.v);
        }
        if(position ==2){
            holder.textView.setImageResource(R.mipmap.sn);
        }
        if(position ==3){
            holder.textView.setImageResource(R.mipmap.cesiumc);
        }
        if(position ==4){
            holder.textView.setImageResource(R.mipmap.dp);
        }
        if(position ==5){
            holder.textView.setImageResource(R.mipmap.ht);
        }
        if(position ==6){
            holder.textView.setImageResource(R.mipmap.s);
        }
        if(position ==7){
            holder.textView.setImageResource(R.mipmap.dp);
        }
//        holder.textView.setText("测试" + position);
        // 点击事件注册及分发
//        if(null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(position == 0){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/vue-mall/#/home");
                        mContext.startActivity(intent);
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/savadata");
//                        intent.setData(content_url);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
//                        mContext.startActivity(intent);

                    }
                    if(position == 1){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/vuechat/");
                        mContext.startActivity(intent);

                    }
                    if(position == 2){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc:80/indoor/");
                        mContext.startActivity(intent);
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse("http://kangkangtk.gnway.cc/indoor/");
//                        intent.setData(content_url);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
//                        mContext.startActivity(intent);

                    }
                    if(position == 3    ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/SpatialInformation/");
                        mContext.startActivity(intent);

                    }
                    if(position == 4    ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/datavdemo/");
                        mContext.startActivity(intent);
                    }
                    if(position == 5    ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/dist-vue-admin/");
                        mContext.startActivity(intent);
                    }
                    if(position == 6    ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/historybuilding/");
                        mContext.startActivity(intent);
                    }
                    if(position == 7    ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/riverProduct/");
                        mContext.startActivity(intent);
                    }
                    if(position == 8    ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/vue-mall-admin/");
                        mContext.startActivity(intent);
                    }
                    if(position == 9    ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/build/");
                        mContext.startActivity(intent);
                    }
                    if(position == 10    ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/olapp/#/home/resource"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/olapp/#/home/resource");
                        mContext.startActivity(intent);
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse("http://kangkangtk.gnway.cc/olapp/#/home/resource");
//                        intent.setData(content_url);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
//                        mContext.startActivity(intent);
                    }
                    if(position == 11   ){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //为Intent设置Category属性
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/olapp/#/home/resource"));
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/olapp/#/home/resource");
                        mContext.startActivity(intent);
                    }
                    if(position == 12   ){
//                        Intent intent=new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        //为Intent设置Category属性
//                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                        intent.setData(Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/home"));
//                        intent.setClass(mContext, WebViewActivity.class);
//                        intent.putExtra("weburl","http://kangkangtk.gnway.cc/vue-mall/#/home");
//                        mContext.startActivity(intent);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("http://kangkangtk.gnway.cc/vue-mall/#/savadata");
                        intent.setData(content_url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
                        mContext.startActivity(intent);
                    }
                    if(position == 13   ){
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("http://kangkangtk.gnway.cc/olapp/#/home/resource");
                        intent.setData(content_url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
                        mContext.startActivity(intent);
                    }
                    if(position == 15   ){
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("http://kangkangtk.gnway.cc:80/indoor/");
                        intent.setData(content_url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
                        mContext.startActivity(intent);
                    }
                }
            });
//        }
    }

    @Override
    public int getItemCount() {
        if(count>0){
            return count;
        }else{
            return 50;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (ImageView) itemView.findViewById(R.id.item_tv_1);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            if(weight>0){
                params.weight =weight;
            }else{
                return;
            }
        }

        ImageView textView;
    }
}
