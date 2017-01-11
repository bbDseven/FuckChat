package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.bean.FriendsCircle;
import com.threeman.fuckchat.db.dao.FriendsCircleDao;
import com.threeman.fuckchat.util.DateUtil;
import com.threeman.fuckchat.util.LearnCloudUtil;
import com.threeman.fuckchat.util.UIUtil;

import org.w3c.dom.Text;

/**
 * description: 添加朋友圈
 * <p/>
 * author: Greetty
 * <p/>
 * date: 2017/1/11 22:27
 * <p/>
 * update: 2017/1/11
 * <p/>
 * version: v1.0
 */
public class AddFriendsActivity extends BaseActivity implements View.OnClickListener {

    private class ViewHolder {

        ImageView add_myFriends_back;
        Button add_myFriends_send;
        EditText add_content;
    }

    private ViewHolder viewHolder;
    private FriendsCircleDao fiendsDao;
    private String username;


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
        viewHolder.add_content = findViewByIds(R.id.add_content);
    }

    private void initEvent() {
        viewHolder.add_myFriends_back.setOnClickListener(this);
        viewHolder.add_myFriends_send.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        fiendsDao = new FriendsCircleDao(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_myFriends_back:
                finish();
                break;
            case R.id.add_myFriends_send:
                final String content = viewHolder.add_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    UIUtil.toastShort(this, "不能发送空消息喔。。。");
                    break;
                }

                AVObject todoFolder = new AVObject("FriendsCircle");// 构建对象
                todoFolder.put("username", username);// 设置名称
                todoFolder.put("content",content );
                todoFolder.put("imgPath", "");
                todoFolder.put("date",DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss") );
                todoFolder.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e!=null){
                            UIUtil.toastShort(AddFriendsActivity.this,"发送失败");
                            finish();
                        }else {
                            UIUtil.toastShort(AddFriendsActivity.this, "发送成功");
                            fiendsDao.add(username, content, "", DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
                            finish();
                        }
                    }
                });// 保存到服务端

                break;
            default:
                break;
        }
    }
}
