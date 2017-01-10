package com.threeman.fuckchat.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.threeman.fuckchat.db.SQLOpenHelper;
import com.threeman.fuckchat.globle.AppConfig;
import com.threeman.fuckchat.util.SharedPreferencesUtils;


/**
 * description: 联系人内容提供者
 *
 * author: Greetty
 *
 * date: 2016/12/30 15:16
 *
 * update: 2016/12/30
 *
 * version: v1.0
*/
public class ChatProvider extends ContentProvider{

    private final static String TAG="ChatProvider";
    private SQLOpenHelper helper;
    private String username;
    private String table_chat;

    @Override
    public boolean onCreate() {
        helper = new SQLOpenHelper(getContext(), AppConfig.DB_NAME, 1);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    /**
     * 添加联系人
     * @param uri  内容提供者uri
     * @param values  联系人数据
     * @return  null
     *
     */

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        username= (String) SharedPreferencesUtils.getParam(getContext(),"username","");
        table_chat=AppConfig.TABLE_CHAT_NAME+username;
        SQLiteDatabase db = helper.getWritableDatabase();
        Log.e(TAG, "table_chat: "+table_chat);
        long contacts = db.insert(table_chat, null, values);
        if (contacts>0){
            ContentResolver resolver = getContext().getContentResolver();
            //发通知，表示已更新数据库
            resolver.notifyChange(uri,null);
        }
        db.close();
        return null;
    }

    /**
     * 删除联系人
     * @param uri  内容提供者uri
     * @param selection  删除条件
     * @param selectionArgs  条件值
     * @return  int 受影响行数
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        username= (String) SharedPreferencesUtils.getParam(getContext(),"username","");
        table_chat=AppConfig.TABLE_CHAT_NAME+username;
        SQLiteDatabase db = helper.getWritableDatabase();
        int contacts = db.delete(table_chat, selection, selectionArgs);
        if (contacts>0){
            ContentResolver resolver = getContext().getContentResolver();
            //发通知，表示已更新数据库
            resolver.notifyChange(uri,null);
        }
        db.close();
        return contacts;
    }

    /**
     * 更新联系人
     * @param uri    内容提供者uri
     * @param values  更新的数据
     * @param selection   更新条件
     * @param selectionArgs  更新条件值
     * @return  int 受影响行数
     */

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        username= (String) SharedPreferencesUtils.getParam(getContext(),"username","");
        table_chat=AppConfig.TABLE_CHAT_NAME+username;
        SQLiteDatabase db = helper.getWritableDatabase();
        int contacts = db.update(table_chat, values, selection, selectionArgs);
        if (contacts>0){
            ContentResolver resolver = getContext().getContentResolver();
            //发通知，表示已更新数据库
            resolver.notifyChange(uri,null);
        }
        db.close();
        return contacts;
    }
}
