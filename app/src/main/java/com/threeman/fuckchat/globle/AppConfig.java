package com.threeman.fuckchat.globle;

/**
 * description: 全局变量
 *
 * author: Greetty
 *
 * date: 2017/1/7 13:24
 *
 * update: 2017/1/7
 *
 * version: v1.0
*/
public class AppConfig {

    /**
     * 闪屏页显示时间--毫秒
     */
    public final static int SPLASH_DELAY=1000;

    /**
     * 发送信息
     */
    public final static int SEND=1;

    /**
     * 接受信息
     */
    public final static int RECEIVE=0;

    /**
     * 数据库前缀的名字，fuckChat_用户名
     */
    public final static String DB_NAME="fuckChat";

    /**
     * 聊天表的前缀的名字，chat_用户名
     */
    public final static String TABLE_CHAT_NAME="chat";

    /**
     * 朋友圈表的前缀的名字，friends_用户名
     */
    public final static String TABLE_FRIENDS_NAME="friends";


}
