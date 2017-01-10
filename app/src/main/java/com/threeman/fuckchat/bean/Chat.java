package com.threeman.fuckchat.bean;

/**
 * description: 聊天表bean
 *
 * author: Greetty
 *
 * date: 2017/1/9 19:46
 *
 * update: 2017/1/9
 *
 * version: v1.0
*/
public class Chat {

    private String username;
    private String content;
    private int send;
    private String target;
    private String date;

    @Override
    public String toString() {
        return "Chat{" +
                "username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", send='" + send + '\'' +
                ", target='" + target + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

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

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
