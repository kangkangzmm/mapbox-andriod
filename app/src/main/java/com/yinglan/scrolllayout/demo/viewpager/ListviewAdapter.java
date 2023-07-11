package com.yinglan.scrolllayout.demo.viewpager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yinglan.scrolllayout.demo.DetilesActivity;
import com.yinglan.scrolllayout.demo.NaviRouteActivity;
import com.yinglan.scrolllayout.demo.R;

import java.util.List;

/**
 * @function listviewadapter
 * @auther: Created by yinglan
 * @time: 16/3/16
 */
public class ListviewAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> dataList;

    public ListviewAdapter(Context context,List<String> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return this.dataList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;

        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_listview, null);
            viewholder = new ViewHolder(convertView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.textView.setText(dataList.get(position) + position);
        viewholder.imageView.setImageResource(R.drawable.ic_circle);

//        viewholder.textView.setText("测试");


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了" + position, Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Intent intent = new Intent();
                    intent.setClass(mContext, DetilesActivity.class);
                    mContext.startActivity(intent);
                }else{
                    String s = (String) viewholder.textView.getText();
                    Intent intent = new Intent();
                    intent.setClass(mContext, NaviRouteActivity.class);
                    intent.putExtra("data","{lonlat:"+s+"}");
                    mContext.startActivity(intent);
                    Log.d("点击listitem", String.valueOf(s));
                }
                Log.d("USB", String.valueOf(dataList));
                Log.d("你点击了","第" + position +"按钮");
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.item_tv_2);
            imageView = (ImageView) view.findViewById(R.id.item_tv_1);
        }
    }
}
