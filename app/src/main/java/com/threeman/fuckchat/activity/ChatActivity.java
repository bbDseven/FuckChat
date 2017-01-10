package com.threeman.fuckchat.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.bean.Chat;
import com.threeman.fuckchat.callback.LCQueryEquals;
import com.threeman.fuckchat.db.dao.ChatDao;
import com.threeman.fuckchat.globle.AppConfig;
import com.threeman.fuckchat.learncloud.AVImClientManager;
import com.threeman.fuckchat.learncloud.NotificationUtils;
import com.threeman.fuckchat.util.DateUtil;
import com.threeman.fuckchat.util.LearnCloudUtil;
import com.threeman.fuckchat.util.UIUtil;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * description: 聊天Activity
 * <p>
 * author: Greetty
 * <p>
 * date: 2017/1/9 22:10
 * <p>
 * update: 2017/1/9
 * <p>
 * version: v1.0
 */
public class ChatActivity extends BaseActivity {

    private final static String TAG = "ChatActivity";
    final Uri uri = Uri.parse("content://com.threeman.fuckchat");  //内容提供者URI
    private final static String Greetty = "58734a3a61ff4b006d49c7b3";
    private final static String greetty = "587349211b69e6005ce101a2";
    private String content;
    private AVIMConversation squareConversation;
    protected AVIMConversation imConversation;
    private String username;
    private String target;
    private List<Chat> chats;
    private MyAdapter myAdapter;
    private ChatDao chatDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Button chat_btn = findViewByIds(R.id.chat_btn);
        final RecyclerView rv_chat_target = findViewByIds(R.id.rv_chat_target);
        final EditText chat_et = findViewByIds(R.id.chat_et);
        ImageView chat_iv_back = findViewByIds(R.id.chat_iv_back);
        TextView chat_tv_name = findViewByIds(R.id.chat_tv_name);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        target = intent.getStringExtra("target");
        Log.e(TAG, "username: " + username);
        Log.e(TAG, "target: " + target);

        chat_tv_name.setText("当前用户是：" + username + "向：" + target + " 发信息");

        //查询与target的左右会话
        chatDao = new ChatDao(ChatActivity.this);
        chats = chatDao.queryAll(username, target);
        Log.e(TAG, "大小1  : " + chats.size());
//        for (Chat chat : chats) {
//            Log.e(TAG, "username1  : " + chat.getUsername());
//            Log.e(TAG, "content1  : " + chat.getContent());
//            Log.e(TAG, "send1  : " + chat.getSend());
//            Log.e(TAG, "target1  : " + chat.getTarget());
//            Log.e(TAG, "date1  : " + chat.getDate());
//            Log.e(TAG, "---------------------------------");
//        }

        //展示会话信息
        myAdapter = new MyAdapter();
        rv_chat_target.setLayoutManager(new LinearLayoutManager(this));
        rv_chat_target.setAdapter(myAdapter);

//        String clientID = checkClientID(username, target);
//        Log.e(TAG, "clientID: "+clientID);
//        if (clientID.isEmpty()){
//            clientID="587476471b69e6005cb58ed4";
//        }

        //通讯
        getSquare(Greetty);
        queryInSquare(username);
        getConversation(target);

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = chat_et.getText().toString().trim();
                if (ChatActivity.this.content.isEmpty()) {
                    toast("不能发送空消息喔。。。");
                    return;
                } else {
                    sendMessage(content);
                    chat_et.setText("");
                    ChatDao chatDao = new ChatDao(ChatActivity.this);
                    chatDao.add(username, content, AppConfig.SEND, target,
                            DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
                }
            }
        });

        chat_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                chatDao = new ChatDao(ChatActivity.this);
                chats = chatDao.queryAll(username, target);
                Log.e(TAG, "接收到内容变化通知，开始刷新数据: ");
                myAdapter.notifyDataSetChanged();
                rv_chat_target.smoothScrollToPosition(chats.size());
                Log.e(TAG, "大小: " + chats.size());
//                for (Chat chat : chats) {
//                    Log.e(TAG, "username: " + chat.getUsername());
//                    Log.e(TAG, "content: " + chat.getContent());
//                    Log.e(TAG, "send: " + chat.getSend());
//                    Log.e(TAG, "target: " + chat.getTarget());
//                    Log.e(TAG, "date: " + chat.getDate());
//                    Log.e(TAG, "---------------------------------");
//                }
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == AppConfig.SEND) {
                view = getLayoutInflater().inflate(R.layout.item_chat_send, parent, false);
                return new ViewHolderOne(view);
            } else if (viewType == AppConfig.RECEIVE) {
                view = getLayoutInflater().inflate(R.layout.item_chat_receiver, null);
                return new ViewHolderTwo(view);
            } else {
                view = getLayoutInflater().inflate(R.layout.item_chat_receiver, parent, false);
                return new ViewHolderTwo(view);
//                return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            Log.e(TAG, "getItemViewType(position): " + getItemViewType(position));
            if (getItemViewType(position) == AppConfig.SEND) {
                ViewHolderOne holderOne = (ViewHolderOne) holder;
                holderOne.chat_tv_contentTo.setText(chats.get(position).getContent() + "");
            } else if (getItemViewType(position) == AppConfig.RECEIVE) {
                ViewHolderTwo holderTwo = (ViewHolderTwo) holder;
                holderTwo.chat_tv_contentFrom.setText(chats.get(position).getContent() + "");
            } else {
                Log.e(TAG, "没有该类型的布局喔: ");
            }
        }

        @Override
        public int getItemCount() {
            return chats.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (chats.get(position).getSend() == AppConfig.SEND) {
                return AppConfig.SEND;
            } else if (chats.get(position).getSend() == AppConfig.RECEIVE) {
                return AppConfig.RECEIVE;
            } else {
                return 2;
            }
        }

        public class ViewHolderOne extends RecyclerView.ViewHolder {
            TextView chat_tv_contentTo;

            public ViewHolderOne(View itemView) {
                super(itemView);
                chat_tv_contentTo = (TextView) itemView.findViewById(R.id.chat_tv_contentTo);
            }
        }

        public class ViewHolderTwo extends RecyclerView.ViewHolder {
            TextView chat_tv_contentFrom;

            public ViewHolderTwo(View itemView) {
                super(itemView);
                chat_tv_contentFrom = (TextView) itemView.findViewById(R.id.chat_tv_contentFrom);
            }
        }
    }


    public void sendMessaages(final String content) {
        // Tom 用自己的名字作为clientId，获取AVIMClient对象实例
        AVIMClient tom = AVIMClient.getInstance(username);
        // 与服务器连接
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 创建与Jerry之间的对话
                    client.createConversation(Arrays.asList(target), username + " & " + target, null,
                            new AVIMConversationCreatedCallback() {

                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVIMTextMessage msg = new AVIMTextMessage();
                                        msg.setText(content);
                                        // 发送消息
                                        conversation.sendMessage(msg, new AVIMConversationCallback() {

                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    Log.e(TAG, "发送成功！");
                                                } else {
                                                    Log.e(TAG, "e: " + e.toString());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
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
//            Log.e(TAG, " squareConversation = client.getConversation(conversationId);: ");
        } else {
            finish();
            UIUtil.toastShort(this, "Please call AVIMClient.open first!");
        }
//        Log.e(TAG, "getSquare: ");
    }


    /**
     * 加入 conversation
     */
    private void joinSquare() {
        squareConversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
//                    Log.e(TAG, "setConversation: ");
                    setConversation(squareConversation);
                } else {
//                    Log.e(TAG, "setConversation哈哈: " + e.getMessage());
//                    Log.e(TAG, "setConversation: " + e.toString());
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
//                        Log.e(TAG, "queryInSquare + setConversation");

                    } else {
//                        Log.e(TAG, "queryInSquare + joinSquare");
                        joinSquare();
                    }
                } else {
//                    Log.e(TAG, "queryInSquare: " + e.toString());
                }
            }
        });
    }

    private void setConversation(AVIMConversation conversation) {
        imConversation = conversation;
        NotificationUtils.addTag(conversation.getConversationId());
    }

    /**
     * 发送信息
     *
     * @param content 发送内容
     */
    private void sendMessage(String content) {
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(content);
        imConversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Log.e(TAG, "发送成功啦...");
//                    toast("发送信息啦...");
                } else {
                    Log.e(TAG, "发送信息失败啦..." + e.toString());
                    toast("发送信息失败啦...");
                }
            }
        });
    }

    /**
     * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
     * 如果存在，则直接赋值给 ChatFragment，否者创建后再赋值
     */
    private void getConversation(final String memberId) {
        final String[] objectId = {null};
        final AVIMClient client = AVImClientManager.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.withMembers(Arrays.asList(memberId), true);
        conversationQuery.whereEqualTo("customConversationType", 1);
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
//                Log.e(TAG, "done: ");
                if (filterException(e)) {
                    //注意：此处仍有漏洞，如果获取了多个 conversation，默认取第一个
                    if (null != list && list.size() > 0) {
//                        final JSONArray jsonArray = new JSONArray();
//                        jsonArray.put(username);
//                        jsonArray.put(target);
//                        LearnCloudUtil util = new LearnCloudUtil(ChatActivity.this);
//                        util.queryEqual("_Conversation", new String[]{"m"},
//                                new Object[]{jsonArray}, new LCQueryEquals() {
//                                    @Override
//                                    public void queryCallBack(List<AVObject> list, AVException e) {
//                                        if (e != null) {
//                                            Log.e(TAG, "e: " + e.toString());
//                                        }
//                                        if (list != null && list.size() > 0) {
//                                            objectId[0] = list.get(0).getObjectId();
//                                        }
////                                        for (AVObject ob : list) {
//////                                            objectId[0] = ob.getObjectId();
////                                        }
//                                    }
//                                });
//                        for (int i = 0; i < list.size(); i++) {
//                            if (list.get(0).getConversationId() == objectId[0]) {
                                setConversation(list.get(0));
//                            } else {
//                                HashMap<String, Object> attributes = new HashMap<String, Object>();
//                                attributes.put("customConversationType", 1);
//                                client.createConversation(Arrays.asList(memberId), null, attributes, false,
//                                        new AVIMConversationCreatedCallback() {
//                                            @Override
//                                            public void done(AVIMConversation avimConversation, AVIMException e) {
//                                                setConversation(avimConversation);
//                                            }
//                                        });
//                            }
//                        }


                    } else {
                        HashMap<String, Object> attributes = new HashMap<String, Object>();
                        attributes.put("customConversationType", 1);
                        client.createConversation(Arrays.asList(memberId), null, attributes, false,
                                new AVIMConversationCreatedCallback() {
                                    @Override
                                    public void done(AVIMConversation avimConversation, AVIMException e) {
                                        setConversation(avimConversation);
                                    }
                                });
                    }
                }
            }
        });
    }

    public String checkClientID(String username,String target){
        final String[] objectId = {null};
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put(username);
        jsonArray.put(target);
        LearnCloudUtil util = new LearnCloudUtil(ChatActivity.this);
        util.queryEqual("_Conversation", new String[]{"m"},
                new Object[]{jsonArray}, new LCQueryEquals() {
                    @Override
                    public void queryCallBack(List<AVObject> list, AVException e) {
                        if (e != null) {
                            Log.e(TAG, "e: " + e.toString());
                        }
                        if (list != null && list.size() > 0) {
                            objectId[0] = list.get(0).getObjectId();
                        }
                    }
                });
        return objectId[0];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationUtils.removeTag(imConversation.getConversationId());
    }
}
