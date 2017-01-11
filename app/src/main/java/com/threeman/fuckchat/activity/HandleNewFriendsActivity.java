package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.sina.weibo.sdk.utils.UIUtils;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.bean.Contacts;
import com.threeman.fuckchat.db.dao.ContactsDao;
import com.threeman.fuckchat.globle.AppConfig;
import com.threeman.fuckchat.globle.ContactsState;
import com.threeman.fuckchat.learncloud.AVImClientManager;
import com.threeman.fuckchat.learncloud.NotificationUtils;
import com.threeman.fuckchat.util.SharedPreferencesUtils;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleBackView;

import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * description: 处理好友添加请求
 * <p/>
 * author: Greetty
 * <p/>
 * date: 2017/1/11 13:19
 * <p/>
 * update: 2017/1/11
 * <p/>
 * version: v1.0
 */
public class HandleNewFriendsActivity extends BaseActivity implements TitleBackView.OnBackClickListener {

    private final static String TAG = "Handle-Activity";

    private TitleBackView tbv_new_friends;
    private RecyclerView rv_handle_new_friends;
    private ContactsDao contactsDao;
    private String username;
    private List<Contacts> contactses;
    private AVIMConversation imConversation;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_new_friends);

        initView();
        initEvent();
        initData();

    }

    private void initView() {
        tbv_new_friends = findViewByIds(R.id.tbv_new_friends);
        rv_handle_new_friends = findViewByIds(R.id.rv_handle_new_friends);
        tbv_new_friends.setTitleName("新的朋友");

    }

    private void initEvent() {
        tbv_new_friends.setBackClickListener(this);

    }

    private void initData() {
        contactsDao = new ContactsDao(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        contactses = contactsDao.queryAllContacts(username);

        rv_handle_new_friends.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter();
        rv_handle_new_friends.setAdapter(myAdapter);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HandleNewFriendsActivity.this).
                    inflate(R.layout.item_handle_new_friends, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final String target_name = contactses.get(position).getUsername();
            holder.item_handle_friends_name.setText(contactses.get(position).getUsername());

            if (contactses.get(position).getContactsState().equals(ContactsState.CONTACTS_HAVED_ACCEPT)) {
                //已接受
                holder.item_handle_friends_action.setText("已接受");
                holder.item_handle_friends_action.setBackgroundColor(Color.parseColor("#C6C6C6"));
            } else if (contactses.get(position).getContactsState().equals(ContactsState.CONTACTS_NOT_HANDLE)) {
                //未处理
                holder.item_handle_friends_action.setText("接受");
                holder.item_handle_friends_action.setBackgroundColor(Color.parseColor("#01D9AE"));
            } else if (contactses.get(position).getContactsState().equals(ContactsState.CONTACTS_NOT_ACCEPT)) {
                //不接受，未实现，后续添加该功能
                holder.item_handle_friends_action.setText("已拒绝");
                holder.item_handle_friends_action.setBackgroundColor(Color.parseColor("#C6C6C6"));
            }else if (contactses.get(position).getContactsState().equals(ContactsState.CONTACTS_HAVE_SEND)) {
                //已发送
                holder.item_handle_friends_action.setText("已发送");
                holder.item_handle_friends_action.setBackgroundColor(Color.parseColor("#C6C6C6"));
            } else {
                holder.item_handle_friends_action.setText("已忽略");
                holder.item_handle_friends_action.setBackgroundColor(Color.parseColor("#C6C6C6"));
            }

            holder.item_handle_friends_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == RecyclerView.NO_POSITION) {
                        UIUtil.toastShort(HandleNewFriendsActivity.this, "你的操作有误，请从新选择");
                        return;
                    } else if (contactses.get(position).getContactsState().
                            equals(ContactsState.CONTACTS_NOT_HANDLE)) {
                        UIUtil.toastShort(HandleNewFriendsActivity.this, HandleNewFriendsActivity.this.username + "已成为好友，赶快去聊天吧");
                        //更新为已接受
                        contactsDao.updateContacts(username, target_name, ContactsState.CONTACTS_HAVED_ACCEPT);

                        //后续用LeanCloud可以不再发信息，直接在服务器为target_name添加一个好友
                        getConversationAndSendMsg(target_name);
                        holder.item_handle_friends_action.setText("已接受");
                        holder.item_handle_friends_action.setBackgroundColor(Color.parseColor("#C6C6C6"));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return contactses.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView item_handle_friends_head;
            TextView item_handle_friends_name;
            Button item_handle_friends_action;

            public MyViewHolder(View itemView) {
                super(itemView);
                item_handle_friends_head = (ImageView) itemView.findViewById(R.id.item_handle_friends_head);
                item_handle_friends_name = (TextView) itemView.findViewById(R.id.item_handle_friends_name);
                item_handle_friends_action = (Button) itemView.findViewById(R.id.item_handle_friends_action);
            }
        }
    }


    //返回按钮
    @Override
    public void Back() {
        //如果还有联系人的状态为未处理的设置为已忽略
        contactses.clear();
        //重新查询联系人状态
        contactses = contactsDao.queryAllContacts(username);
        for (Contacts con : contactses) {
            if (con.getContactsState().equals(ContactsState.CONTACTS_NOT_HANDLE)) {
                contactsDao.updateContacts(username, con.getUsername(),
                        ContactsState.CONTACTS_HAVED_READ);
            }
        }
        finish();

    }


    /**
     * 给会话容器赋值
     *
     * @param conversation 会话容器
     */
    private void setConversation(AVIMConversation conversation) {
        imConversation = conversation;
        Log.e(TAG, "setConversation: " + conversation.getConversationId());
        NotificationUtils.addTag(conversation.getConversationId());
    }


    /**
     * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
     * 如果存在，则直接赋值，否者创建后再赋值
     */
    private void getConversationAndSendMsg(final String memberId) {
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
                        Log.e(TAG, "老子就加入了一次: ");
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
                    sendMessage(AppConfig.ACCEPT_ADD_FRIENDS);
                }
            }
        });
    }


    /**
     * 发送信息
     *
     * @param content 发送内容
     */
    private void sendMessage(String content) {
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(content);
        if (imConversation==null){
            Log.e(TAG, "imConversation: 空");
        }else {
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
    }
}
