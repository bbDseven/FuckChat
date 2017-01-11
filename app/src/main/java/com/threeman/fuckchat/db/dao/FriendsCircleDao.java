package com.threeman.fuckchat.db.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.threeman.fuckchat.bean.FriendsCircle;
import com.threeman.fuckchat.db.SQLOpenHelper;
import com.threeman.fuckchat.globle.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 朋友圈数据库工具类
 * <p/>
 * author: Greetty
 * <p/>
 * date: 2017/1/11 21:49
 * <p/>
 * update: 2017/1/11
 * <p/>
 * version: v1.0
 */
public class FriendsCircleDao {

    final Uri uri = Uri.parse("content://com.threeman.fuckchat.friends");  //内容提供者URI
    private Context mContext;
    private SQLOpenHelper helper;

    public FriendsCircleDao(Context context) {
        this.mContext = context;
        helper = new SQLOpenHelper(mContext, AppConfig.DB_NAME, 1);
    }


    /**
     * 添加朋友圈
     *
     * @param username  用户名（该条朋友圈是谁的朋友圈）
     * @param content   内容
     * @param imagePath 图片路径
     * @param date      日期
     */
    public void add(String username, String content, String imagePath, String date) {
        ContentResolver cr = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("content", content);
        values.put("imagePath", imagePath);
        values.put("date", date);
        cr.insert(uri, values);
    }

    /**
     * 删除朋友圈
     * @param username  用户名（该条朋友圈是谁的朋友圈）
     * @return  int
     */
    public int delete(String username) {
        ContentResolver cr = mContext.getContentResolver();
        int delete = cr.delete(uri, "username=?", new String[]{username});
        return delete;
    }

    /**
     * 查询所有朋友圈内容
     * @param username  用户名（登陆者）
     * @return  list
     */
    public List<FriendsCircle> queryAll(String username){
        ArrayList<FriendsCircle> listFriends = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String table_chat = AppConfig.TABLE_FRIENDS_NAME + username;
        Cursor cursor = db.query(table_chat, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            FriendsCircle friendsCircle = new FriendsCircle();
            friendsCircle.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            friendsCircle.setContent(cursor.getString(cursor.getColumnIndex("content")));
            friendsCircle.setImagePath(cursor.getString(cursor.getColumnIndex("imagePath")));
            friendsCircle.setDate(cursor.getString(cursor.getColumnIndex("date")));
            listFriends.add(friendsCircle);
        }
        db.close();
        cursor.close();
        return listFriends;
    }


    /**
     * 查询某友朋友圈内容
     * @param username  用户名（登陆者）
     * @return  list
     */
    public List<FriendsCircle> querySingleFriends(String username){
        ArrayList<FriendsCircle> listFriends = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String table_chat = AppConfig.TABLE_FRIENDS_NAME + username;
        Cursor cursor = db.query(table_chat, null, "username=?", new String[]{username},
                null, null, null);
        while (cursor.moveToNext()){
            FriendsCircle friendsCircle = new FriendsCircle();
            friendsCircle.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            friendsCircle.setContent(cursor.getString(cursor.getColumnIndex("content")));
            friendsCircle.setImagePath(cursor.getString(cursor.getColumnIndex("imagePath")));
            friendsCircle.setDate(cursor.getString(cursor.getColumnIndex("date")));
            listFriends.add(friendsCircle);
        }
        db.close();
        cursor.close();
        return listFriends;
    }
}
