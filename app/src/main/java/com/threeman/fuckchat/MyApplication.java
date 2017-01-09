package com.threeman.fuckchat;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.threeman.fuckchat.learncloud.MessageHandler;

/**
 * description: 我的Application
 *
 * author: Greetty
 *
 * date: 2017/1/7 15:03
 *
 * update: 2017/1/7
 *
 * version: v1.0
*/
public class MyApplication extends Application {

    final String AV_ID = "rv4Btxvtpy8IvSa02GBhHhkA-gzGzoHsz";
    final String AV_KEY = "R392DXTFlI767wdcvQn8N1Dn";
    private final static String TAG="MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, AV_ID, AV_KEY);
//        AVOSCloud.useAVCloudCN();

        //注册默认的消息处理逻辑
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(this));
    }
}
