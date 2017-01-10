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
import com.threeman.fuckchat.db.dao.ChatDao;
import com.threeman.fuckchat.globle.AppConfig;
import com.threeman.fuckchat.learncloud.event.ImTypeMessageEvent;
import com.threeman.fuckchat.util.DateUtil;
import com.threeman.fuckchat.util.SharedPreferencesUtils;
import com.threeman.fuckchat.util.UIUtil;

import org.w3c.dom.Text;

import de.greenrobot.event.EventBus;


/**
 * Created by zhangxiaobo on 15/4/20.
 */
public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    private final static String TAG = "MessageHandler";
    private Context context;
    private String text;


    public MessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {

        String clientID = "";
        text = ((AVIMTextMessage) message).getText();
        Log.e(TAG, "conversation: "+conversation.getConversationId());
        Log.e(TAG, "client: "+client.getClientId());
        Log.e(TAG, "message.getFrom(): "+message.getFrom());
        Log.e(TAG, "内容是: " + text);
        SharedPreferencesUtils.setParam(context,"target",message.getFrom());
//
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
     * 因为没有 db，所以暂时先把消息广播出去，由接收方自己处理
     * 稍后应该加入 db
     *
     * @param message
     * @param conversation
     */
    private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation) {
        ImTypeMessageEvent event = new ImTypeMessageEvent();
        event.message = message;
        event.conversation = conversation;
        Log.e(TAG, "哈哈哈、女神发来消息: " + text);
        String username = (String) SharedPreferencesUtils.getParam(context, "username", "");
        String target = (String) SharedPreferencesUtils.getParam(context, "target", "");
        //创建数据库表
//        String key="Table_"+username;
//        boolean is_create_table = (boolean) SharedPreferencesUtils.
//                getParam(context, key, false);
//        if (is_create_table){
//
//        }
        ChatDao chatDao = new ChatDao(context);
        chatDao.add(username,text, AppConfig.RECEIVE,target, DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
        EventBus.getDefault().post(event);
    }

    private void sendNotification(AVIMTypedMessage message, AVIMConversation conversation) {
        String notificationContent = message instanceof AVIMTextMessage ?
                ((AVIMTextMessage) message).getText() : context.getString(R.string.unspport_message_type);
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.putExtra(Constants.CONVERSATION_ID, conversation.getConversationId());
        intent.putExtra(Constants.MEMBER_ID, message.getFrom());
        NotificationUtils.showNotification(context, "", notificationContent, null, intent);
        Log.e(TAG, "发送广播: ");
    }
}
