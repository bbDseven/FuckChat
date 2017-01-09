package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Arrays;
import java.util.List;

public class TestActivity extends BaseActivity {

    private final static String TAG="TestActivity";
    private AVIMConversation squareConversation;
    protected AVIMConversation imConversation;
    private String content;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final EditText et_send_content = findViewByIds(R.id.et_send_content);
        Button btn_send = findViewByIds(R.id.btn_send);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = et_send_content.getText().toString().trim();
                if (content.isEmpty()) {
                    toast("不能发送空消息喔。。。");
                } else {
                    queryInSquare(username);
                }
            }
        });
    }

    public void sendMessage() {
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(content);
        imConversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                Log.e(TAG, "发送信息啦...");
                toast("发送信息啦...");
            }
        });
    }

    public void setConversation(AVIMConversation conversation) {
        if (null != conversation) {
            imConversation = conversation;
            sendMessage();
        }
    }

    /**
     * 加入 conversation
     */
    private void joinSquare() {
        squareConversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (filterException(e)) {
                    setConversation(squareConversation);
                    Log.e(TAG, "嘿嘿嘿黑: ");
                }
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
                if (filterException(e)) {
                    if (null != list && list.size() > 0) {
                        Log.e(TAG, "哈哈哈哈 ");
                        setConversation(list.get(0));
                    } else {
                        joinSquare();
                    }
                }
            }
        });
    }


}
