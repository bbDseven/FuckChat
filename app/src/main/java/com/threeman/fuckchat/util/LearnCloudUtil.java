package com.threeman.fuckchat.util;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.callback.LCQueryEquals;

import java.util.List;

/**
 * description: LeanrnCloud工具类
 *
 * author: Greetty
 *
 * date: 2017/1/7 15:22
 *
 * update: 2017/1/7
 *
 * version: v1.0
*/
public class LearnCloudUtil {
    private Context mContext;
    public LearnCloudUtil(Context context){
        this.mContext=context;
    }

    /**
     * LearnCloud查询key与values是否相等
     * @param table  表名
     * @param Keys  LearnCloud字段
     * @param Values  字段查询值
     * @param queryEquals  回调接口
     */
    public void queryEqual(String table, String[] Keys, Object [] Values, final LCQueryEquals queryEquals ){
        AVQuery<AVObject> query = new AVQuery<>(table);
        for (int i=0;i<Keys.length;i++){
//            Log.e("LearnCloudUtil", "Keys["+i+"] : "+Keys[i]);
//            Log.e("LearnCloudUtil", "Values["+i+"] : "+Values[i]);
            query.whereEqualTo(Keys[i],Values[i]);
        }
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e!=null){
                    UIUtil.toastShort(mContext,"出错啦");
                    Log.e("LearnCloudUtil", "e: "+e.toString());
                    return;
                }
                queryEquals.queryCallBack(list,e);
            }
        });
    }


    /**
     * 保存用户信息
     * @param table  表名
     * @param user  用户对象
     * @return  异常AVException
     */
    public AVException saveUserInfo(String table,User user){
        final AVException[] mException = new AVException[1];
        if (user!=null){
            AVObject todoFolder = new AVObject(table);// 构建对象
            todoFolder.put("username", user.getUsername());// 设置名称
            todoFolder.put("nickname", user.getNickname());
            todoFolder.put("password", user.getPassword());
            todoFolder.put("netPath", user.getNetPath());
            todoFolder.put("localPath", user.getLocalPath());
            todoFolder.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    mException[0] =e;
                }
            });// 保存到服务端
        }
        return mException[0];
    }


}
