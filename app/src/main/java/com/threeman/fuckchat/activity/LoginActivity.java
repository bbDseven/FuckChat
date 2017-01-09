package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.callback.LCQueryEquals;
import com.threeman.fuckchat.learncloud.AVImClientManager;
import com.threeman.fuckchat.learncloud.Constants;
import com.threeman.fuckchat.util.LearnCloudUtil;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleBackView;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 登录
 *
 * author: Greetty
 *
 * date: 2017/1/7 14:26
 *
 * update: 2017/1/7
 *
 * version: v1.0
*/
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private class ViewHolder{
        TitleBackView tbv_login;
        ImageView iv_login_head;
        EditText et_sign_in_account;
        EditText et_sign_in_pwd;
        Button btn_sign_in;
        Button btn_register_login;
    }
    private final static String TAG="LoginActivity";
    private ViewHolder viewHolder;
    private  String password;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvent();
        initData();
    }
    private void initView() {
        viewHolder=new ViewHolder();
        viewHolder.tbv_login=findViewByIds(R.id.tbv_login);
        viewHolder.iv_login_head=findViewByIds(R.id.iv_login_head);
        viewHolder.et_sign_in_account=findViewByIds(R.id.et_sign_in_account);
        viewHolder.et_sign_in_pwd=findViewByIds(R.id.et_sign_in_pwd);
        viewHolder.btn_sign_in=findViewByIds(R.id.btn_sign_in);
        viewHolder.btn_register_login=findViewByIds(R.id.btn_register_login);
    }

    private void initEvent() {
//        viewHolder.tbv_login.setBackClickListener(this);
        viewHolder.iv_login_head.setOnClickListener(this);
        viewHolder.btn_sign_in.setOnClickListener(this);
        viewHolder.btn_register_login.setOnClickListener(this);
    }

    private void initData() {
        viewHolder.tbv_login.setBackImageVisible(false);
        viewHolder.tbv_login.setTitleName("登陆");

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
//        viewHolder.et_sign_in_account.setText(username);
//        viewHolder.et_sign_in_pwd.setText(password);
        viewHolder.et_sign_in_account.setText("greetty");
        viewHolder.et_sign_in_pwd.setText("123456");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_login_head:
                UIUtil.toastShort(this,"更改头像");
                break;
            case R.id.btn_sign_in:
                username=viewHolder.et_sign_in_account.getText().toString().trim();
                password=viewHolder.et_sign_in_pwd.getText().toString().trim();
                if (username.isEmpty()){
                    UIUtil.toastShort(this,"用户名不能为空");
                    return;
                }else if(password.isEmpty()){
                    UIUtil.toastShort(this,"密码不能为空");
                    return;
                }else {
                    login();
                }
                break;
             case R.id.btn_register_login:
                 startActivity(new Intent(this,RegisterActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 用户登录
     */
    private void login() {
        LearnCloudUtil util = new LearnCloudUtil(this);
        util.queryEqual("user_info", new String[]{"username", "password"},
                new String[]{username, password}, new LCQueryEquals() {
            @Override
            public void queryCallBack(List<AVObject> list, AVException e) {
                if (list==null || list.size()==0){
                    UIUtil.toastShort(LoginActivity.this,"用户名或密码错误...");
                }else {
                    final User user = new User();
                    for (AVObject ob : list) {
                        user.setUsername(ob.getString("username"));
                        user.setNickname(ob.getString("nickname"));
                        user.setPassword(ob.getString("password"));
                        user.setNetPath(ob.getString("netPath"));
                        user.setLocalPath(ob.getString("localPath"));
                    }
                    AVImClientManager.getInstance().open(username, new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (filterException(e)) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("user_info",user);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
            }
        });
    }
}
