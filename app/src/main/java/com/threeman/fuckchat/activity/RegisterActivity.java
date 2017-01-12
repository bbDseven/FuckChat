package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.callback.LCQueryEquals;
import com.threeman.fuckchat.db.SQLOpenHelper;
import com.threeman.fuckchat.globle.AppConfig;
import com.threeman.fuckchat.util.LearnCloudUtil;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleBackView;

import java.util.List;

public class RegisterActivity extends BaseActivity implements TitleBackView.OnBackClickListener, View.OnClickListener {

    private class ViewHolder{
        TitleBackView tbv_register;
        EditText et_sign_up_account;
        EditText et_sign_up_pwd;
        Button btn_sign_up;
    }
    private ViewHolder viewHolder;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        viewHolder=new ViewHolder();
        viewHolder.tbv_register=findViewByIds(R.id.tbv_register);
        viewHolder.et_sign_up_account=findViewByIds(R.id.et_sign_up_account);
        viewHolder.et_sign_up_pwd=findViewByIds(R.id.et_sign_up_pwd);
        viewHolder.btn_sign_up=findViewByIds(R.id.btn_sign_up);
    }

    private void initEvent() {
        viewHolder.tbv_register.setBackClickListener(this);
        viewHolder.btn_sign_up.setOnClickListener(this);
    }

    private void initData() {
        viewHolder.tbv_register.setTitleName("注册");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_up:
                username=viewHolder.et_sign_up_account.getText().toString().trim();
                password=viewHolder.et_sign_up_pwd.getText().toString().trim();
                if (username.isEmpty()){
                    UIUtil.toastShort(this,"用户名不能为空");
                    return;
                }else if(checkUserName()){
                    UIUtil.toastShort(this,"用户名已被注册");
                    return;
                }else if(password.length()>22){
                    UIUtil.toastShort(this,"用户名不可以大于22位");
                    return;
                }else if(password.isEmpty()){
                    UIUtil.toastShort(this,"密码不能为空");
                    return;
                }else if(password.length()<6  || password.length()>16){
                    UIUtil.toastShort(this,"密码长度应为6到16位");
                    return;
                }else {
                   register();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 注册用户
     */
    private void register() {
        User user = new User();
        LearnCloudUtil util = new LearnCloudUtil(this);
        user.setUsername(username);
        user.setPassword(password);
        AVException e = util.saveUserInfo("user_info", user);
        if (e==null){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("password",password);
            startActivity(intent);
            UIUtil.toastShort(this,"注册成功，马上登录");
            finish();
        }else {
            UIUtil.toastShort(this,"注册失败,请从新注册");
        }
    }

    /**
     * 判断用户名是否已经被注册
     * @return boolean
     */
    public boolean checkUserName(){
        final boolean[] is_have_user = {false};
        LearnCloudUtil util = new LearnCloudUtil(this);
        util.queryEqual("user_info", new String[]{"username"},
                new String[]{username}, new LCQueryEquals() {
            @Override
            public void queryCallBack(List<AVObject> list, AVException e) {
                if (list==null || list.size()==0){
                    is_have_user[0] =false;
                }else {
                    is_have_user[0] =true;
                }
            }
        });
        return is_have_user[0];
    }


    /**
     * 返回
     */
    @Override
    public void Back() {
        finish();
    }
}
