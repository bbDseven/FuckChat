package com.threeman.fuckchat.learncloud;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.bean.Contacts;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.db.dao.ChatDao;
import com.threeman.fuckchat.db.dao.ContactsDao;
import com.threeman.fuckchat.globle.AppConfig;
import com.threeman.fuckchat.globle.ContactsState;
import com.threeman.fuckchat.learncloud.event.ImTypeMessageEvent;
import com.threeman.fuckchat.util.DateUtil;
import com.threeman.fuckchat.util.SharedPreferencesUtils;
import com.threeman.fuckchat.util.UIUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * description: 接收信息
 * <p/>
 * author: Greetty
 * <p/>
 * date: 2017/1/11 9:52
 * <p/>
 * update: 2017/1/11
 * <p/>
 * version: v1.0
 */
public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    private final static String TAG = "MessageHandler";
    private Context context;
    private String text;
    private ContactsDao contactsDao;
    private ChatDao chatDao;

    private String username;
    private boolean IS_NEW_FRIEND = true;  //是否是新朋友
    private boolean IS_NEED_INSERT_DB = true;  //是否添加朋友恢复
    private List<String> listUserName;  //用户名列表


    public MessageHandler(Context context) {
        this.context = context;
        contactsDao = new ContactsDao(context);
        chatDao = new ChatDao(context);
        listUserName=new ArrayList<>();
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {

        String clientID = "";
        text = ((AVIMTextMessage) message).getText();
        Log.e(TAG, "conversationId: " + conversation.getConversationId());
        Log.e(TAG, "内容是: " + text);
        String target = message.getFrom();
        SharedPreferencesUtils.setParam(context, "target", target);

        username = (String) SharedPreferencesUtils.getParam(context, "username", "");

        //判断是否是恢复内容
        judgeAcceptAddFriends(text,target);

        //判断是否是新朋友
        judgeNewFriend(target);

        Log.e(TAG, "IS_NEW_FRIEND: " + IS_NEW_FRIEND);

        try {
            clientID = AVImClientManager.getInstance().getClientId();
            if (client.getClientId().equals(clientID)) {

                // 过滤掉自己发的消息
                if (!message.getFrom().equals(clientID)) {
                    sendEvent(message, conversation);
                    if (NotificationUtils.isShowNotification(conversation.getConversationId())) {
                        sendNotification(message, conversation);
                    }
                }
            } else {
                client.close(null);
            }
        } catch (IllegalStateException e) {
            client.close(null);
        }
    }

    /**
     * 判断是否是添加朋友的恢复,如果是，更新数据库该联系人状态为已接受
     *
     * 后续把表添加到LeanCloud上可以不用判断，直接查询LeanCloud
     *
     * @param content
     * @param target
     */
    public void judgeAcceptAddFriends(String content, String target ) {
        List<Contacts> contacts = contactsDao.queryAllContacts(username);
        if (content.equals(AppConfig.ACCEPT_ADD_FRIENDS)) {
            for (Contacts cont : contacts) {
                if (cont.getUsername().equals(target) &&
                        cont.getContactsState().equals(ContactsState.CONTACTS_HAVE_SEND)) {
                    int size = contactsDao.updateContacts(username, target, ContactsState.CONTACTS_HAVED_ACCEPT);
                    Log.e(TAG, "接受了,受影响行数: "+size);
                }
            }
        }
    }

    /**
     * 判断是否是新朋友
     *
     * @param target   新朋友名字
     */
    public void judgeNewFriend(String target) {
        List<Contacts> contacts = contactsDao.queryAllContacts(username);
        if (contacts.size() == 0 || contacts == null) {
            IS_NEW_FRIEND = true;
            contactsDao.add(username, target, ContactsState.CONTACTS_NOT_HANDLE);
        } else {
            for (Contacts cont : contacts) {
                Log.e(TAG, "联系人名字name: "+cont.getUsername());
                Log.e(TAG, "from: "+target);

                //存在于数据库中，且状态为已通过请求验证（1、已经成为联系人的，2、自己发送请求添加，别人同意添加）
                if (cont.getUsername().equals(target) && cont.getContactsState().
                        equals(ContactsState.CONTACTS_HAVED_ACCEPT)) {
                    IS_NEW_FRIEND = false;
                    Log.e(TAG, "我等于: ");
                } else if(cont.getUsername().equals(target) && !cont.getContactsState().
                        equals(ContactsState.CONTACTS_HAVED_ACCEPT)) {
                    //已经创建数据库，但还没有成为好友
                    IS_NEW_FRIEND = true;
                    Log.e(TAG, "我不等于,我不添加数据库 ");
                }
                listUserName.add(cont.getUsername());
            }
            Log.e(TAG, " for .... IS_NEW_FRIEND: " + IS_NEW_FRIEND);

            //比较查询该数据库中没有该用户
            if(!listUserName.contains(target)){
                //被别人请求加为好友，还没有数据
                IS_NEW_FRIEND = true;
                contactsDao.add(username, target, ContactsState.CONTACTS_NOT_HANDLE);
                Log.e(TAG, "我不等于,我要添加数据库 ");
            }

            Log.e(TAG, " if .... IS_NEW_FRIEND: " + IS_NEW_FRIEND);
        }
    }

    /**
     * 因为没有 db，所以暂时先把消息广播出去，由接收方自己处理
     * 稍后应该加入 db
     *
     * @param message      信息
     * @param conversation 容器
     */
    private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation) {
        ImTypeMessageEvent event = new ImTypeMessageEvent();
        event.message = message;
        event.conversation = conversation;
        String target = (String) SharedPreferencesUtils.getParam(context, "target", "");
        chatDao.add(username, text, AppConfig.RECEIVE, target, DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
        EventBus.getDefault().post(event);
    }

    private void sendNotification(AVIMTypedMessage message, AVIMConversation conversation) {
        String notificationContent = message instanceof AVIMTextMessage ?
                ((AVIMTextMessage) message).getText() : context.getString(R.string.unspport_message_type);
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.putExtra(Constants.CONVERSATION_ID, conversation.getConversationId());
        intent.putExtra(Constants.MEMBER_ID, message.getFrom());
        intent.putExtra(ContactsState.key, IS_NEW_FRIEND);
        NotificationUtils.showNotification(context, "", notificationContent, null, intent);
    }
}
