package com.threeman.fuckchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.activity.ChatActivity;
import com.threeman.fuckchat.activity.LoginActivity;
import com.threeman.fuckchat.activity.MainActivity;
import com.threeman.fuckchat.bean.Chat;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.db.dao.ChatDao;
import com.threeman.fuckchat.util.SharedPreferencesUtils;
import com.threeman.fuckchat.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天界面
 * Created by cjz on 2017/1/6 0006.
 */
public class ChatFragment extends Fragment {

    private final static String TAG = "ChatFragment";
    private final static int QUERY_ALL_USER = 1;
    private View ChatView;
    private RecyclerView rv_chat;
    private TextView tv_caht_tip;
    private MyAdapter adapter;
    private String username;
    private List<Chat> chatList;
    private List<Map> mapList;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QUERY_ALL_USER:

                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ChatView = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(ChatView);

        MainActivity mActivity = (MainActivity) getActivity();
        username = mActivity.getUsername();

        return ChatView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView(View view) {
        rv_chat = (RecyclerView) view.findViewById(R.id.rv_chat);
        tv_caht_tip = (TextView) view.findViewById(R.id.tv_caht_tip);

    }

    public void initData() {
        rv_chat.setLayoutManager(new LinearLayoutManager(getContext()));
        //查询所有会话
        QueryALLChats(username);
        //处理数据，整理出有多少会话对象
        HandleData(chatList);

        if (chatList.size()==0){
            tv_caht_tip.setVisibility(View.VISIBLE);
        }else {
            tv_caht_tip.setVisibility(View.GONE);
        }
        adapter = new MyAdapter();
        rv_chat.setAdapter(adapter);
//
//        Log.e(TAG, "mapList: " + mapList.size());
//        for (int i = 0; i < mapList.size(); i++) {
//            Log.e(TAG, "username: " + mapList.get(i).get("contactsName"));
//        }
//        QueryAllUser();

    }


    /**
     * 查询所有会话
     */
    private void QueryALLChats(String username) {
        chatList = new ArrayList<>();
        ChatDao chatDao = new ChatDao(getContext());
        chatList = chatDao.queryAllChat(username);
//        Log.e(TAG, "chats大小: " + chatList.size());
//        for (Chat chat : chatList) {
//            Log.e(TAG, "聊天列表用户名：" + chat.getTarget());
//        }
    }

//    /**
//     * 查询所有用户
//     */
//    public void QueryAllUser() {
//        users = new ArrayList<>();
//        AVQuery<AVObject> query = new AVQuery<>("user_info");
//        query.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (e != null) {
//                    Log.e(TAG, "出错了: ");
//                    return;
//                }
//                for (AVObject ob : list) {
//                    User user = new User();
//                    user.setUsername(ob.getString("username"));
//                    user.setNickname(ob.getString("nickname"));
//                    user.setPassword(ob.getString("password"));
//                    user.setNetPath(ob.getString("netPath"));
//                    user.setLocalPath(ob.getString("localPath"));
//                    users.add(user);
//                }
//                handler.sendEmptyMessage(QUERY_ALL_USER);
//
//            }
//        });
//    }

    /**
     * 处理数据，计算出会话好友个数
     * @param chatList  数据源
     * @return list<map>
     */
    public List<Map> HandleData(List<Chat> chatList) {
        mapList = new ArrayList<>();
        if (chatList != null && chatList.size() > 0) {
            for (int i = 0; i < chatList.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                if (i == 0) {
                    hashMap.put("contactsName", chatList.get(0).getTarget());
                    hashMap.put("content", chatList.get(0).getContent());
                    mapList.add(hashMap);
                } else if (!chatList.get(i - 1).getTarget().equals(chatList.get(i).getTarget())) {
                    hashMap.put("contactsName", chatList.get(i).getTarget());
                    hashMap.put("content", chatList.get(i).getContent());
                    mapList.add(hashMap);
                }

            }
        }
        return mapList;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        String contactsName;  //联系人姓名
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_chat, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            contactsName = (String) mapList.get(position).get("contactsName");
            String content = (String) mapList.get(position).get("content");
            holder.item_chat_name.setText(contactsName);
            holder.item_chat_content.setText(content);
        }

        @Override
        public int getItemCount() {
            return mapList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView item_chat_head;
            TextView item_chat_name;
            TextView item_chat_content;
            LinearLayout ll_item_chat;

            public MyViewHolder(View itemView) {
                super(itemView);
                item_chat_head = (ImageView) itemView.findViewById(R.id.item_chat_head);
                item_chat_name = (TextView) itemView.findViewById(R.id.item_chat_name);
                item_chat_content = (TextView) itemView.findViewById(R.id.item_chat_content);
                ll_item_chat = (LinearLayout) itemView.findViewById(R.id.ll_item_chat);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            UIUtil.toastShort(getContext(), "你的操作有误，请从新选择");
                            return;
                        }
                        SharedPreferencesUtils.setParam(getContext(), "target", contactsName);

                        //跳转到聊天界面
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("target", item_chat_name.getText().toString().trim());
                        getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
