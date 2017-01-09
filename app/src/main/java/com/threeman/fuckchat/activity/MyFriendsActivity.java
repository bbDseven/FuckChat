package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 我的朋友圈（我的相册）
 *
 * author: Greetty
 *
 * date: 2017/1/6 16:27
 *
 * update: 2017/1/6
 *
 * version: v1.0
*/
public class MyFriendsActivity extends BaseActivity implements TitleView.OnAddClickListener {

    private final static int TYPE_TOP=0;  //图片布局
    private final static int TYPE_ITEM=1;  //朋友圈信息
    private TitleView tv_myFriend;
    private RecyclerView rv_myFriend;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        tv_myFriend=findViewByIds(R.id.tv_myFriend);
        rv_myFriend=findViewByIds(R.id.rv_myFriend);
    }


    private void initEvent() {
        tv_myFriend.setAddClickListener(this);
    }
    private void initData() {
        tv_myFriend.setTitleName("我的相册");

        list=new ArrayList<>();
        for (int i=0;i<20;i++){
            list.add("陈贵堂"+i);
        }
        MyAdapter adapter=new MyAdapter();
        rv_myFriend.setLayoutManager(new LinearLayoutManager(this));
        rv_myFriend.setAdapter(adapter);
    }

    @Override
    public void add() {
        UIUtil.toastShort(this,"添加朋友圈");
        startActivity(new Intent(this,AddFriendsActivity.class));
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType==TYPE_TOP){
                view = LayoutInflater.from(MyFriendsActivity.this).inflate
                        (R.layout.item_friends_picture, parent, false);
                return new MyViewHolderOne(view);

            }else {
                 view = LayoutInflater.from(MyFriendsActivity.this).inflate
                        (R.layout.item_my_friends, parent, false);
                return new MyViewHolderTwo(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position)==TYPE_ITEM){
                MyViewHolderTwo holderTwo = (MyViewHolderTwo) holder;
                holderTwo.item_myFriend_day.setText("24");
                holderTwo.item_myFriend_month.setText("10月");
                holderTwo.item_myFriend_content.setText(list.get(position));
            }else if (getItemViewType(position)==TYPE_TOP){
                MyViewHolderOne holderOne = (MyViewHolderOne) holder;
                holderOne.ib_myFriend_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtil.toastShort(MyFriendsActivity.this,"信息个人详细信息");
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
            TextView item_myFriend_day;
            TextView item_myFriend_month;
            TextView item_myFriend_content;
            public MyViewHolderTwo(View itemView) {
                super(itemView);
                item_myFriend_day= (TextView) itemView.findViewById(R.id.item_myFriend_day);
                item_myFriend_month= (TextView) itemView.findViewById(R.id.item_myFriend_month);
                item_myFriend_content= (TextView) itemView.findViewById(R.id.item_myFriend_content);
            }
        }
    }


}
