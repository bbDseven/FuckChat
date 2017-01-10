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
 * <p/>
 * author: Greetty
 * <p/>
 * date: 2017/1/9 22:10
 * <p/>
 * update: 2017/1/9
 * <p/>
 * version: v1.0
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private class ViewHolder {

        Button chat_btn;
        RecyclerView rv_chat_target;
        EditText chat_et;
        TextView chat_tv_name;
        ImageView chat_iv_back;
    }

    private final static String TAG = "ChatActivity";

    final Uri uri = Uri.parse("content://com.threeman.fuckchat");  //内容提供者URI
    private final static String Greetty = "58734a3a61ff4b006d49c7b3";
    private final static String greetty = "587349211b69e6005ce101a2";
    private ViewHolder viewHolder;
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

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        viewHolder = new ViewHolder();
        viewHolder.chat_btn = findViewByIds(R.id.chat_btn);
        viewHolder.rv_chat_target = findViewByIds(R.id.rv_chat_target);
        viewHolder.chat_et = findViewByIds(R.id.chat_et);
        viewHolder.chat_iv_back = findViewByIds(R.id.chat_iv_back);
        viewHolder.chat_tv_name = findViewByIds(R.id.chat_tv_name);
    }

    private void initData() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        target = intent.getStringExtra("target");
        viewHolder.chat_tv_name.setText("当前用户是：" + username + "  向:" + target + " 发信息");

        //查询与target的左右会话
        chatDao = new ChatDao(ChatActivity.this);
        chats = chatDao.queryAll(username, target);
        Log.e(TAG, "大小1  : " + chats.size());

        //展示会话信息
        myAdapter = new MyAdapter();
        viewHolder.rv_chat_target.setLayoutManager(new LinearLayoutManager(this));
        viewHolder.rv_chat_target.setAdapter(myAdapter);

        //判断是否选哟创建新的会话容器
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(username);
        jsonArray.put(target);
        LearnCloudUtil util = new LearnCloudUtil(ChatActivity.this);
        util.queryEqual("_Conversation", new String[]{"m"},
                new Object[]{jsonArray}, new LCQueryEquals() {
                    @Override
                    public void queryCallBack(List<AVObject> list, AVException e) {
                        if (e != null) {
                            Log.e(TAG, "checkConversationID: " + e.toString());
                        }
                        Log.e(TAG, "list: " + list.size());
                        if (list != null && list.size() > 0) {
                            for (int i=0;i<list.size();i++){
                                Log.e(TAG, "id: "+list.get(i).getObjectId());
                            }
                            String conversationID = list.get(0).getObjectId();
                            if (TextUtils.isEmpty(conversationID)) {
                                Log.e(TAG, "没有该conversationID: ");
//                                sendMessages(username, target, content);
                                createConversation();
                            } else {
                                //通讯
//                                           getSquare(Greetty);
//                                           queryInSquare(username);
                                getSquare(conversationID);
                                queryInSquare(conversationID);
//                                getConversation(target);
//                                sendMessage(content);
                                Log.e(TAG, "有该conversationID值: " + conversationID);
                            }
                        } else {
                            Log.e(TAG, "数据库没有数据咯: ");
//                            sendMessages(username, target, content);
                            createConversation();
                        }
                    }
                });

        //监听数据库变化
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(uri, true, new MyContentObserver(new Handler()));


    }

    private void initEvent() {
        viewHolder.chat_btn.setOnClickListener(this);
        viewHolder.chat_iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_btn:
                content = viewHolder.chat_et.getText().toString().trim();
                if (ChatActivity.this.content.isEmpty()) {
                    toast("不能发送空消息喔。。。");
                    return;
                } else {
                    //发送信息
                    sendMessage(content);
                    viewHolder.chat_et.setText("");
                    //更新数据库
                    ChatDao chatDao = new ChatDao(ChatActivity.this);
                    chatDao.add(username, content, AppConfig.SEND, target,
                            DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
                }
                break;
            case R.id.chat_iv_back:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 内容监听者
     */
    private class MyContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //更新数据
            chatDao = new ChatDao(ChatActivity.this);
            chats = chatDao.queryAll(username, target);
            //刷新UI
            myAdapter.notifyDataSetChanged();
            viewHolder.rv_chat_target.smoothScrollToPosition(chats.size());
        }
    }


    /**
     * RecycleView适配器
     */
    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == AppConfig.SEND) {
                view = getLayoutInflater().inflate(R.layout.item_chat_send, parent, false);
                return new ViewHolderOne(view);
            } else if (viewType == AppConfig.RECEIVE) {
                view = getLayoutInflater().inflate(R.layout.item_chat_receiver, null);
                return new ViewHolderTwo(view);
            } else {
                view = getLayoutInflater().inflate(R.layout.item_chat_receiver, parent, false);
                return new ViewHolderTwo(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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


    public void createConversation() {
        AVIMClient client = AVImClientManager.getInstance().getClient();
        // 创建与Jerry之间的对话
        client.createConversation(Arrays.asList(target), username + " & " + target, null,
                new AVIMConversationCreatedCallback() {

                    @Override
                    public void done(AVIMConversation conversation, AVIMException e) {
                        if (e == null) {
                            setConversation(conversation);
                        } else {
                            Log.e(TAG, "e: " + e.toString());
                            UIUtil.toastShort(ChatActivity.this, "创建对话容器失败");
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
        } else {
            finish();
            UIUtil.toastShort(this, "Please call AVIMClient.open first!");
            Log.e(TAG, "Please call AVIMClient.open first!");
        }
//        Log.e(TAG, "conversationId: " + conversationId);
//        Log.e(TAG, "squareConversation: " + squareConversation);
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
                }
            }
        });
    }

    /**
     * 先查询自己是否已经在该 conversation，如果存在则直接赋值，否则先加入，再赋值
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
                        setConversation(list.get(0));
                        Log.e(TAG, "我加入了一次: "+list.get(0).getConversationId());
                    } else {
                        joinSquare();
                    }
                }
            }
        });
    }

    /**
     * 给会话容器赋值
     *
     * @param conversation
     */
    private void setConversation(AVIMConversation conversation) {
        imConversation = conversation;
        Log.e(TAG, "setConversation: "+conversation.getConversationId());
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
                } else {
                    Log.e(TAG, "发送信息失败啦..." + e.toString());
                    toast("发送信息失败啦...");
                }
            }
        });
    }

    /**
     * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
     * 如果存在，则直接赋值，否者创建后再赋值
     */
    private void getConversation(final String memberId) {
        final AVIMClient client = AVImClientManager.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.withMembers(Arrays.asList(memberId), true);
        conversationQuery.whereEqualTo("customConversationType", 1);
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (filterException(e)) {
                    //注意：此处仍有漏洞，如果获取了多个 conversation，默认取第一个
                    if (null != list && list.size() > 0) {
                        setConversation(list.get(0));
                        Log.e(TAG, "老子又加入了一次: ");
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

    
    @Override
    protected void onDestroy() {
        super.onDestroy();
       Log.e(TAG, "onDestroy: "+imConversation.getConversationId());
        NotificationUtils.removeTag(imConversation.getConversationId());
    }
}
