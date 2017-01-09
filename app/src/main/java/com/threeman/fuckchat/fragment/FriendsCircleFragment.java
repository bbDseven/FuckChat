package com.threeman.fuckchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.threeman.fuckchat.R;
import com.threeman.fuckchat.activity.AddFriendsActivity;
import com.threeman.fuckchat.activity.MyFriendsActivity;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 朋友圈界面
 * Created by cjz on 2017/1/6 0006.
 */
public class FriendsCircleFragment extends Fragment implements TitleView.OnAddClickListener {

    private final static int TYPE_TOP=0;  //图片布局
    private final static int TYPE_ITEM=1;  //朋友圈信息
    private View FriendsCircleView;
    private RecyclerView rv_myFriend;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FriendsCircleView = inflater.inflate(R.layout.fragment_friendscircle,container,false);

        initView(FriendsCircleView);
        initEvent();
        initData();
        return FriendsCircleView;
    }


    private void initView(View view) {
        rv_myFriend= (RecyclerView) view.findViewById(R.id.rv_myFriend);
    }


    private void initEvent() {
    }
    private void initData() {

        list=new ArrayList<>();
        for (int i=0;i<20;i++){
            list.add("陈贵堂"+i);
        }
        MyAdapter adapter=new MyAdapter();
        rv_myFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_myFriend.setAdapter(adapter);
    }

    @Override
    public void add() {
        UIUtil.toastShort(getContext(),"添加朋友圈");
        startActivity(new Intent(getContext(),AddFriendsActivity.class));
    }


    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType==TYPE_TOP){
                view = LayoutInflater.from(getContext()).inflate
                        (R.layout.item_friends_picture, parent, false);
                return new MyViewHolderOne(view);
            }else {
                view = LayoutInflater.from(getContext()).inflate
                        (R.layout.item_friends, parent, false);
                return new MyViewHolderTwo(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position)==TYPE_ITEM){
                MyViewHolderTwo holderTwo = (MyViewHolderTwo) holder;
                holderTwo.item_friends_name.setText(list.get(position));
            }else if (getItemViewType(position)==TYPE_TOP){
                MyViewHolderOne holderOne = (MyViewHolderOne) holder;
                    holderOne.ib_myFriend_head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getContext(), MyFriendsActivity.class));
                        }
                    });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position==TYPE_TOP){
                return TYPE_TOP;
            }else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        public class MyViewHolderOne extends RecyclerView.ViewHolder {
            ImageButton ib_myFriend_head;
            public MyViewHolderOne(View itemView) {
                super(itemView);
                ib_myFriend_head= (ImageButton) itemView.findViewById(R.id.ib_myFriend_head);
            }
        }

        public class MyViewHolderTwo extends RecyclerView.ViewHolder {
            ImageView item_contacts_head;
            TextView item_friends_name;
            TextView item_friends_content;
            public MyViewHolderTwo(View itemView) {
                super(itemView);
                item_contacts_head= (ImageView) itemView.findViewById(R.id.item_contacts_head);
                item_friends_name= (TextView) itemView.findViewById(R.id.item_friends_name);
                item_friends_content= (TextView) itemView.findViewById(R.id.item_friends_content);
            }
        }
    }

}
