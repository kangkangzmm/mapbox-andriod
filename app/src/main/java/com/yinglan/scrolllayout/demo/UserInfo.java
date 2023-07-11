package com.yinglan.scrolllayout.demo;

import android.app.Application;

public class UserInfo extends Application {
    //声明一个变量
    public String nameString;
    public String token;
    public String data;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        setname("英雄联盟");
    }

    //实现setname()方法，设置变量的值
    public void setname(String name) {
        this.nameString = name;
    }

    //实现getname()方法，获取变量的值
    public String getname() {
        return nameString;

    }
    //实现setname()方法，设置变量的值
    public void settoken(String token) {
        this.token = token;
    }

    //实现getname()方法，获取变量的值
    public String gettoken() {
        return token;

    }

    //实现setname()方法，设置变量的值
    public void setdata(String data) {
        this.data = data;
    }

    //实现getname()方法，获取变量的值
    public String getdata() {
        return data;

    }
}
