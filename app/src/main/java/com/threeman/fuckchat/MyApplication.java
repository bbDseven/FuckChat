package com.threeman.fuckchat;

import android.app.Activity;
import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.threeman.fuckchat.learncloud.MessageHandler;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Activity> mActivityList = new ArrayList<Activity>();
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


    //提供一个添加activity的方法
    public void addActivity(Activity activity) {
        mActivityList.add(activity);

    }

    //提供一个移除activity的方法
    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    //提供一个清空集合的方法
    public void finishAllActivity() {
        /**
         * 关闭所有activity
         */
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivityList.clear();
    }
}
