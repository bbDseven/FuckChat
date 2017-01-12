package com.threeman.fuckchat.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.threeman.fuckchat.bean.Contacts;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.db.SQLOpenHelper;
import com.threeman.fuckchat.globle.AppConfig;
import com.threeman.fuckchat.globle.ContactsState;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 好友工具类
 *
 * author: Greetty
 *
 * date: 2017/1/11 9:39
 *
 * update: 2017/1/11
 *
 * version: v1.0
*/
public class ContactsDao {

    private Context mContext;
    private SQLOpenHelper helper;

    public ContactsDao(Context context){
        this.mContext=context;
        helper=new SQLOpenHelper(mContext, AppConfig.DB_NAME,1);
    }


    /**
     * 增加好友
     * @param username  用户名
     * @param ContactsState  状态
     * @return
     */
    public long add(String username,String target,String ContactsState){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",target);
        values.put("ContactsState",ContactsState);
        long size = db.insert(AppConfig.TABLE_CONTACTS_NAME+username, null, values);
        db.close();
        return size;
    }

    /**
     * 查询所有联系人（包括发送过请求和别人发送过加好友请求）
     * @param username  用户名
     * @return  list
     */
    public List<Contacts> queryAllContacts(String username){
        ArrayList<Contacts> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(AppConfig.TABLE_CONTACTS_NAME + username, null, null, null, null, null, null);
       while (cursor.moveToNext()){
           Contacts contacts = new Contacts();
           contacts.setUsername(cursor.getString(cursor.getColumnIndex("username")));
           contacts.setContactsState(cursor.getString(cursor.getColumnIndex("contactsState")));
           list.add(contacts);
       }
        db.close();
        return list;
    }


    /**
     * 查询所有联系人（好友）
     * @param username  用户名
     * @return  list
     */
    public List<Contacts> queryAllAcceptContacts(String username){
        ArrayList<Contacts> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(AppConfig.TABLE_CONTACTS_NAME + username, null, "contactsState=?",
                new String[]{ContactsState.CONTACTS_HAVED_ACCEPT}, null, null, null);
        while (cursor.moveToNext()){
            Contacts contacts = new Contacts();
            contacts.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            contacts.setContactsState(cursor.getString(cursor.getColumnIndex("contactsState")));
            list.add(contacts);
        }
        db.close();
        return list;
    }

    public boolean queryContacts(String username,String contacts){
        boolean is_have=false;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(AppConfig.TABLE_CONTACTS_NAME + username, null, "username=?",
                new String[]{contacts}, null, null, null);
        while (cursor.moveToNext()){
            is_have=true;
        }
        return is_have;
    }

    /**
     * 更新联系人
     * @param username  自己的用户名
     * @param target  联系人名字
     * @param contactsState  更新状态
     * @return  int
     */
    public int updateContacts(String username,String target,String contactsState){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contactsState",contactsState);
        int update = db.update(AppConfig.TABLE_CONTACTS_NAME + username, values,
                "username=?", new String[]{target});
        db.close();
        return update;
    }
}
