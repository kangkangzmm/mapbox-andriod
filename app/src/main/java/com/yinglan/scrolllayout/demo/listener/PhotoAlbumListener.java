package com.yinglan.scrolllayout.demo.listener;

/**
 * Description：
 * Created by kang on 2018/3/29.
 */

public interface PhotoAlbumListener<T> {
    void onSelected(T t);

    void onClickCamera();
}
