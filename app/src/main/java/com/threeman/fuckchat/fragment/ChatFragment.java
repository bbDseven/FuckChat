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
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.util.SharedPreferencesUtils;
import com.threeman.fuckchat.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面
 * Created by cjz on 2017/1/6 0006.
 */
public class ChatFragment extends Fragment {

    private final static String TAG = "ChatFragment";
    private final static int QUERY_ALL_USER = 1;
    private View ChatView;
    private RecyclerView rv_chat;
    private MyAdapter adapter;
    private String username;
    private ArrayList<User> users;



    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case QUERY_ALL_USER:
                    adapter=new MyAdapter();
                    rv_chat.setAdapter(adapter);
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
        initData();

        MainActivity mActivity = (MainActivity) getActivity();
        username = mActivity.getUsername();
        return ChatView;
    }

    private void initView(View view) {
        rv_chat = (RecyclerView) view.findViewById(R.id.rv_chat);

    }

    public void initData(){
        rv_chat.setLayoutManager(new LinearLayoutManager(getContext()));
        QueryAllUser();

    }

    /**
     * 查询所有用户
     */
    public void QueryAllUser(){
        users = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>("user_info");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e!=null){
                    Log.e(TAG, "出错了: ");
                    return;
                }
                for (AVObject ob : list) {
                    User user = new User();
                    user.setUsername(ob.getString("username"));
                    user.setNickname(ob.getString("nickname"));
                    user.setPassword(ob.getString("password"));
                    user.setNetPath(ob.getString("netPath"));
                    user.setLocalPath(ob.getString("localPath"));
                    users.add(user);
                }
                handler.sendEmptyMessage(QUERY_ALL_USER);

            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_chat, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.item_chat_name.setText(users.get(position).getUsername());
        }

        @Override
        public int getItemCount() {
            return users.size();
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
                        SharedPreferencesUtils.setParam(getContext(), "target", users.get(position));

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
