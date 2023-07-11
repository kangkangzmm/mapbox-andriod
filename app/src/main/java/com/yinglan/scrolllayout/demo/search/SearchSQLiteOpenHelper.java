package com.yinglan.scrolllayout.demo.search;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SearchSQLiteOpenHelper extends SQLiteOpenHelper {
    private static SQLiteOpenHelper mInstance=null;
    public static synchronized SQLiteOpenHelper getmInstance(Context context){
        if(mInstance==null){
            mInstance=new SearchSQLiteOpenHelper(context,"searchitem.db",null,3);
        }
        return mInstance;
    }
    public SearchSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table search(_id integer primary key autoincrement,searchname varchar(20));";
        db.execSQL(sql);
        String sql_history="create table historysearch(_id integer primary key autoincrement,historyname varchar(20));";
        db.execSQL(sql_history);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
