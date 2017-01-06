package com.threeman.fuckchat.util;

import android.content.Context;
import android.widget.Toast;

/**
 * description: UI工具类
 *
 * author: Greetty
 *
 * date: 2017/1/6 15:11
 *
 * update: 2017/1/6
 *
 * version: v1.0
*/
public class UIUtil {

    /**
     * 短时间吐司
     * @param context
     * @param content
     */
    public static void toastShort(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
