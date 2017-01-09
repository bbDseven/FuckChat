package com.threeman.fuckchat.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * description: LearnCloud查询相等的回调接口
 *
 * author: Greetty
 *
 * date: 2017/1/7 15:29
 *
 * update: 2017/1/7
 *
 * version: v1.0
*/
public interface LCQueryEquals {
    void queryCallBack(List<AVObject> list, AVException e);
}
