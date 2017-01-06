package com.threeman.fuckchat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.util.UIUtil;

public class AddFriendsActivity extends BaseActivity implements View.OnClickListener {

    private class ViewHolder {

        ImageView add_myFriends_back;
        Button add_myFriends_send;
    }

    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_frindctivity);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        viewHolder = new ViewHolder();
        viewHolder.add_myFriends_back = findViewByIds(R.id.add_myFriends_back);
        viewHolder.add_myFriends_send = findViewByIds(R.id.add_myFriends_send);
    }

    private void initEvent() {
        viewHolder.add_myFriends_back.setOnClickListener(this);
        viewHolder.add_myFriends_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_myFriends_back:
                finish();
                break;
            case R.id.add_myFriends_send:
                UIUtil.toastShort(this,"发送成功");
                break;
            default:
                break;
        }
    }

    private void initData() {

    }
}
