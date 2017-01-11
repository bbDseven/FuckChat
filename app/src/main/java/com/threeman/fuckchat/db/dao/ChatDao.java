package com.threeman.fuckchat.db.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.threeman.fuckchat.bean.Chat;
import com.threeman.fuckchat.db.SQLOpenHelper;
import com.threeman.fuckchat.globle.AppConfig;
import com.threeman.fuckchat.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 聊天表工具类
 * <p>
 * author: Greetty
 * <p>
 * date: 2017/1/9 18:51
 * <p>
 * update: 2017/1/9
 * <p>
 * version: v1.0
 */
public class ChatDao {

    final Uri uri = Uri.parse("content://com.threeman.fuckchat");  //内容提供者URI
    private Context mContext;
    private SQLOpenHelper helper;

    public ChatDao(Context context) {
        this.mContext = context;
    }

    /**
     * 增加聊天信息
     *
     * @param username 用户名
     * @param content  聊天内容
     * @param send     1-->username自己发送给别人target
     * @param target   聊天对象
     * @param date     日期
     */
    public void add(String username, String content, int send, String target, String date) {
        ContentResolver cr = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("content", content);
        values.put("send", send);
        values.put("target", target);
        values.put("date", date);
        cr.insert(uri, values);
    }

    /**
     * 查询和target的左右会话
     *
     * @param username 用户名
     * @return list
     */
    public List<Chat> queryAllChat(String username) {
        String table_name = AppConfig.TABLE_CHAT_NAME + username;
        ArrayList<Chat> chats = new ArrayList<>();
        helper = new SQLOpenHelper(mContext, AppConfig.DB_NAME, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(table_name, null, null, null, null, null,null);
        while(cursor.moveToNext()) {
            Chat chat = new Chat();
            chat.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            chat.setTarget(cursor.getString(cursor.getColumnIndex("target")));
            chat.setContent(cursor.getString(cursor.getColumnIndex("content")));
            chat.setDate(cursor.getString(cursor.getColumnIndex("date")));
            chat.setSend(cursor.getInt(cursor.getColumnIndex("send")));
            chats.add(chat);
        }
        return chats;
    }


    /**
     * 删除和某人的全部会话
     *
     * @param username 用户名
     * @param target   聊天对象
     * @return
     */
    public int delete(String username, String target) {
        ContentResolver cr = mContext.getContentResolver();
        return cr.delete(uri, "username=? and target=?", new String[]{username, target});
    }


    /**
     * 查询和target的左右会话
     *
     * @param username 用户名
     * @param target   目标会话对象
     * @return list
     */
    public List<Chat> querySingleAll(String username, String target) {
        List<Chat> chats = new ArrayList<>();
        SQLOpenHelper helper = new SQLOpenHelper(mContext, AppConfig.DB_NAME, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
//        username = (String) SharedPreferencesUtils.getParam(mContext, "username", "");
        String table_chat = AppConfig.TABLE_CHAT_NAME + username;
        Cursor cursor = db.query(table_chat, null,
                "username=? and target=?", new String[]{username, target}, null, null, null);
        while (cursor.moveToNext()) {
            Chat chat = new Chat();
            chat.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            chat.setContent(cursor.getString(cursor.getColumnIndex("content")));
            chat.setDate(cursor.getString(cursor.getColumnIndex("date")));
            chat.setSend(cursor.getInt(cursor.getColumnIndex("send")));
            chat.setTarget(cursor.getString(cursor.getColumnIndex("target")));
            chats.add(chat);
        }
        return chats;
    }
}
