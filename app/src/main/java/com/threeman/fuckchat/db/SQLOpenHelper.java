package com.threeman.fuckchat.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** 
 * description: 数据库
 *
 * author: Greetty
 * 
 * date: 2017/1/6 13:53
 * 
 * update: 2017/1/6
 * 
 * version: v1.0
*/
public class SQLOpenHelper extends SQLiteOpenHelper {

    public SQLOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user_info(username text,nickname text,password text," +
                "netPath text,localPath text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
