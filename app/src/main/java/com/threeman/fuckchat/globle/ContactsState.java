package com.threeman.fuckchat.globle;

/**
 * description: 请求添加联系人时的状态
 * <p/>
 * author: Greetty
 * <p/>
 * date: 2017/1/11 10:04
 * <p/>
 * update: 2017/1/11
 * <p/>
 * version: v1.0
 */
public class ContactsState {

    /**
     * 已添加为好友
     */
    public final static String CONTACTS_HAVED_ACCEPT = "已接受";

    /**
     * 别人发送了请求，自己没有处理的联系人
     */
    public final static String CONTACTS_NOT_HANDLE = "未处理";

    /**
     * 拒绝该请求
     */
    public final static String CONTACTS_NOT_ACCEPT = "不接受";

    /**
     * 点击进入看过了该请求，没有做任何操作
     */
    public final static String CONTACTS_HAVED_READ = "已浏览";

    /**
     * 已发送
     */
    public final static String CONTACTS_HAVE_SEND="已发送";

    /**
     * SharedPreferences保存时用的Key
     * key
     */
    public final static String key = "CONTACTS_STATE";
}
