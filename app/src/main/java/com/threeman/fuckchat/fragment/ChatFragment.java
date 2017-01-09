package com.threeman.fuckchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.threeman.fuckchat.R;
import com.threeman.fuckchat.activity.ChatActivity;
import com.threeman.fuckchat.activity.MainActivity;
import com.threeman.fuckchat.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面
 * Created by cjz on 2017/1/6 0006.
 */
public class ChatFragment extends Fragment {

    private View ChatView;
    private RecyclerView rv_chat;
    private MyAdapter adapter;
    private List<String> list;
    private String username;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ChatView = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(ChatView);

        MainActivity mActivity = (MainActivity) getActivity();
        username = mActivity.getUsername();
        return ChatView;
    }

    private void initView(View view) {
        rv_chat = (RecyclerView) view.findViewById(R.id.rv_chat);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("陈贵堂" + i);
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
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.item_chat_name.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView item_chat_head;
            TextView item_chat_name;
            TextView item_chat_content;

            public MyViewHolder(View itemView) {
                super(itemView);
                item_chat_head = (ImageView) itemView.findViewById(R.id.item_chat_head);
                item_chat_name = (TextView) itemView.findViewById(R.id.item_chat_name);
                item_chat_content = (TextView) itemView.findViewById(R.id.item_chat_content);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                            UIUtil.toastShort(getContext(), "please click again!");
                            return;
                        }
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("username",username);
                        getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
