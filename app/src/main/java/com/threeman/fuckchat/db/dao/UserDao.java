package com.threeman.fuckchat.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.threeman.fuckchat.db.SQLOpenHelper;
import com.threeman.fuckchat.globle.AppConfig;

/**
 * description: 用户数据库工具类
 *
 * author: Greetty
 *
 * date: 2017/1/6 13:59
 *
 * update: 2017/1/6
 *
 * version: v1.0
*/
public class UserDao {
    private Context mContext;
    private SQLOpenHelper helper;

    public UserDao(Context context,String username){
        this.mContext=context;
        helper=new SQLOpenHelper(mContext, AppConfig.DB_NAME,1);
    }

    /**
     *
     * @param username
     * @param nickname
     * @param password
     * @param netPath
     * @param localPath
     * @return
     */
    public long add(String username,String nickname,String password,String netPath,String localPath){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("nickname",nickname);
        values.put("password",password);
        values.put("netPath",netPath);
        values.put("localPath",localPath);
        long user_info = db.insert("user_info", null, values);
        db.close();
        return user_info;
    }

}
