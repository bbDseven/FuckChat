package com.threeman.fuckchat.bean;

import java.io.Serializable;

/**
 * description: 用户javaBean
 *
 * author: Greetty
 *
 * date: 2017/1/7 15:50
 *
 * update: 2017/1/7
 *
 * version: v1.0
*/
public class User implements Serializable {

    private String username;  //用户名（唯一）
    private String nickname;  //昵称
    private String password;  //密码
    private String netPath;  //头像网络保存路径
    private String localPath;  //头像本地保存路径

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", netPath='" + netPath + '\'' +
                ", localPath='" + localPath + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
