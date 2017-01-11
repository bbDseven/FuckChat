package com.threeman.fuckchat.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.db.SQLOpenHelper;
import com.threeman.fuckchat.globle.AppConfig;

import java.util.ArrayList;
import java.util.List;

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
     *增加用户
     * @param username  用户名
     * @param nickname  昵称
     * @param password  密码
     * @param netPath  头像网络保存路径
     * @param localPath   头像本地路径
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

    /**
     * 查询所有用户
     * @return  list
     */
    public List<User> queryAllContacts(){
        ArrayList<User> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("user_info", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            User user = new User();
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setLocalPath(cursor.getString(cursor.getColumnIndex("localPath")));
            user.setNetPath(cursor.getString(cursor.getColumnIndex("netPath")));
            list.add(user);
        }
        return list;
    }

}
