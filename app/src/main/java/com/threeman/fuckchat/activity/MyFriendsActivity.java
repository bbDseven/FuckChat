package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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

    private TitleView tv_myFriend;
    private RecyclerView rv_myFriend;
    private List list;

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
       // MyAdapter adapter=new MyAdapter();
//        rv_myFriend.setLayoutManager(new LinearLayoutManager(this));
        //rv_myFriend.setAdapter(adapter);
    }

    @Override
    public void add() {
        UIUtil.toastShort(this,"添加朋友圈");
        startActivity(new Intent(this,AddFriendsActivity.class));
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


}
