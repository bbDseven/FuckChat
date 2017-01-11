package com.threeman.fuckchat.bean;

/**
 * description: 联系人（好友）bean
 *
 * author: Greetty
 *
 * date: 2017/1/11 10:18
 *
 * update: 2017/1/11
 *
 * version: v1.0
*/
public class Contacts {

    private String username;
    private String contactsState;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactsState() {
        return contactsState;
    }

    public void setContactsState(String contactsState) {
        this.contactsState = contactsState;
    }
}
