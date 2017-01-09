package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.learncloud.AVImClientManager;
import com.threeman.fuckchat.learncloud.NotificationUtils;
import com.threeman.fuckchat.util.UIUtil;

import java.util.Arrays;
import java.util.List;

public class ChatActivity extends BaseActivity {

    private final static String TAG = "ChatActivity";

    private String content;
    private AVIMConversation squareConversation;
    protected AVIMConversation imConversation;
    private final static String Greetty = "58734a3a61ff4b006d49c7b3";
    private final static String greetty = "587349211b69e6005ce101a2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Button chat_btn = findViewByIds(R.id.chat_btn);
        final EditText chat_et = findViewByIds(R.id.chat_et);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        getSquare(Greetty);
        queryInSquare(username);

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = chat_et.getText().toString().trim();

                if (ChatActivity.this.content.isEmpty()) {
                    toast("不能发送空消息喔。。。");
                    return;
                } else {
                    sendMessage(content);
                }
            }
        });
    }


    /**
     * 根据 conversationId 查取本地缓存中的 conversation，如若没有缓存，则返回一个新建的 conversaiton
     */
    private void getSquare(String conversationId) {
        if (TextUtils.isEmpty(conversationId)) {
            throw new IllegalArgumentException("conversationId can not be null");
        }

        AVIMClient client = AVImClientManager.getInstance().getClient();
        if (null != client) {
            squareConversation = client.getConversation(conversationId);
            Log.e(TAG, " squareConversation = client.getConversation(conversationId);: ");
        } else {
            finish();
            UIUtil.toastShort(this, "Please call AVIMClient.open first!");
        }
        Log.e(TAG, "getSquare: ");
    }


    /**
     * 加入 conversation
     */
    private void joinSquare() {
        squareConversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Log.e(TAG, "setConversation: ");
                    setConversation(squareConversation);
                } else {
                    Log.e(TAG, "setConversation哈哈: " + e.getMessage());
                    Log.e(TAG, "setConversation: " + e.toString());
                }
//                if (filterException(e)) {
//                }
            }
        });
    }

    /**
     * 先查询自己是否已经在该 conversation，如果存在则直接给 chatFragment 赋值，否则先加入，再赋值
     */
    private void queryInSquare(String conversationId) {
        final AVIMClient client = AVImClientManager.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.whereEqualTo("objectId", conversationId);
        conversationQuery.containsMembers(Arrays.asList(AVImClientManager.getInstance().getClientId()));
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
//                if (filterException(e)) {
//                    if (null != list && list.size() > 0) {
//                        setConversation(list.get(0));
//                    } else {
//                        joinSquare();
//                    }
//                }

                if (e == null) {
                    if (null != list && list.size() > 0) {
                        setConversation(list.get(0));
                        Log.e(TAG, "queryInSquare + setConversation");

                    } else {
                        Log.e(TAG, "queryInSquare + joinSquare");
                        joinSquare();
                    }
                } else {
                    Log.e(TAG, "queryInSquare: " + e.toString());
                }
            }
        });
    }

    private void setConversation(AVIMConversation conversation) {
        imConversation = conversation;
        NotificationUtils.addTag(conversation.getConversationId());
    }

    private void sendMessage(String content) {
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(content);
        imConversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Log.e(TAG, "发送信息啦...");
                    toast("发送信息啦...");
                } else {
                    Log.e(TAG, "发送信息失败啦...");
                    toast("发送信息失败啦...");
                }
            }
        });
    }
}
