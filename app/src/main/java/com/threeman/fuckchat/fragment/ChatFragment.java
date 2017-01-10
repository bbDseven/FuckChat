package com.threeman.fuckchat.fragment;

import android.content.Intent;
import android.os.Bundle;
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
    private View ChatView;
    private RecyclerView rv_chat;
    private MyAdapter adapter;
    private List<String> list;
    private String username;
    private ArrayList<User> users;



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
        users = new ArrayList<>();
//        AVQuery<AVObject> query = new AVQuery<>("user_info");
//        query.whereEqualTo("password","123456");
//        query.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (e!=null){
//                    Log.e(TAG, "出错了: ");
//                    return;
//                }
//                Log.e(TAG, "list: "+list.size());
//                for (AVObject ob : list) {
//                    User user = new User();
//                    user.setUsername(ob.getString("username"));
//                    user.setNickname(ob.getString("nickname"));
//                    user.setPassword(ob.getString("password"));
//                    user.setNetPath(ob.getString("netPath"));
//                    user.setLocalPath(ob.getString("localPath"));
//                    users.add(user);
//                }
//            }
//        });

        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i == 0) {
                list.add("greetty");
            } else if (i == 1) {
                list.add("gui");
            } else if (i == 2) {
                list.add("haha");
            }else {
                list.add("陈贵堂" + i);
            }
        }

        rv_chat.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        rv_chat.setAdapter(adapter);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_chat, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.item_chat_name.setText(list.get(position));
//            holder.ll_item_chat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (getAdapterPosition() == RecyclerView.NO_POSITION) {
//                        UIUtil.toastShort(getContext(), "please click again!");
//                        return;
//                    }
//                    SharedPreferencesUtils.setParam(getContext(), "target", list.get());
//
//                    Intent intent = new Intent(getContext(), ChatActivity.class);
//                    intent.putExtra("username", username);
//                    intent.putExtra("target", holder.item_chat_name.getText().toString().trim());
//                    getContext().startActivity(intent);
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return list.size();
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
                            UIUtil.toastShort(getContext(), "please click again!");
                            return;
                        }
//                        Log.e(TAG, "position: "+position);
                        SharedPreferencesUtils.setParam(getContext(), "target", list.get(position));

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
