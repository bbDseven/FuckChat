package com.threeman.fuckchat.bean;

/**
 * description: 朋友圈bean
 *
 * author: Greetty
 *
 * date: 2017/1/11 21:51
 *
 * update: 2017/1/11
 *
 * version: v1.0
*/
public class FriendsCircle {

    private String username;
    private String content;
    private String imagePath;
    private String date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
