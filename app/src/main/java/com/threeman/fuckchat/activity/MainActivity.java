package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.adapter.FragmentAdapter;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.fragment.AddressListFragment;
import com.threeman.fuckchat.fragment.ChatFragment;
import com.threeman.fuckchat.fragment.FriendsCircleFragment;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, TitleView.OnAddClickListener {

    public class ViewHolder {
        private ViewPager viewPager;
        private List<Fragment> fragments;//fragments集合
        private FragmentAdapter fragmentAdapter;//fragments适配器
        private RelativeLayout ll_chat, ll_address, ll_friends;
        private ImageButton ib_chat, ib_address, ib_friends;
        private TitleView tv_title;
    }

    private ViewHolder viewHolder;
    private final static String TAG="MainActivity";
    private static int button_selext_index = 0;  //底部导航栏选中的位置
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initDate();
        initFragmentViewPager();

    }

    /**
     * 初始化各个控件
     */
    public void initView() {
        viewHolder = new ViewHolder();
        viewHolder.viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewHolder.fragments = new ArrayList<>();
        viewHolder.ll_chat = (RelativeLayout) findViewById(R.id.ll_chat);
        viewHolder.ll_address = (RelativeLayout) findViewById(R.id.ll_address);
        viewHolder.ll_friends = (RelativeLayout) findViewById(R.id.ll_friends);
        viewHolder.ib_chat = (ImageButton) findViewById(R.id.ib_chat);
        viewHolder.ib_address = (ImageButton) findViewById(R.id.ib_address);
        viewHolder.ib_friends = (ImageButton) findViewById(R.id.ib_friends);
        viewHolder.tv_title = (TitleView) findViewById(R.id.tv_title);
    }

    private void initEvent() {
        viewHolder.ll_chat.setOnClickListener(this);
        viewHolder.ll_address.setOnClickListener(this);
        viewHolder.ll_friends.setOnClickListener(this);
        viewHolder.tv_title.setAddClickListener(this);

    }

    private void initDate() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user_info");
         username = user.getUsername();
//        Log.e(TAG, "username: "+username);
//        Log.e(TAG, "password: "+user.getPassword());
    }

    @Override
    public void add() {

        switch (button_selext_index) {
            case 0:  //发起聊天
                UIUtil.toastShort(this, "发起聊天");
//                Intent intent = new Intent(this, TestActivity.class);
//                intent.putExtra("username",username);
//                startActivity(intent);
//                sendMessageToJerryFromTom();
                break;
            case 1:  //添加朋友
                startActivity(new Intent(this, SearchContactsActivity.class));
                break;
            case 2:  //发朋友圈
                startActivity(new Intent(this, AddFriendsActivity.class));
                break;
            default:
                break;
        }

    }

    /**
     * 初始化 4个 Fragment 的ViewPager
     */
    private void initFragmentViewPager() {
        viewHolder.fragments.add(new ChatFragment());           //装进一个聊天
        viewHolder.fragments.add(new AddressListFragment());    //装进一个通讯录
        viewHolder.fragments.add(new FriendsCircleFragment());  //装进一个朋友圈
        viewHolder.fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), viewHolder.fragments);
        viewHolder.viewPager.setAdapter(viewHolder.fragmentAdapter);
        viewHolder.viewPager.addOnPageChangeListener(new MyPageChangeListener());//ViewPager滑动改变事件
    }

    /**
     * 点击头部+按钮执行该方法
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_chat:
                viewHolder.viewPager.setCurrentItem(0);
                refreshIMG();
                viewHolder.ib_chat.setImageResource(R.mipmap.weixin_pressed);
                break;
            case R.id.ll_address:
                viewHolder.viewPager.setCurrentItem(1);
                refreshIMG();
                viewHolder.ib_address.setImageResource(R.mipmap.contact_list_pressed);
                break;
            case R.id.ll_friends:
                viewHolder.viewPager.setCurrentItem(2);
                refreshIMG();
                viewHolder.ib_friends.setImageResource(R.mipmap.find_pressed);
                break;
            default:
                break;
        }
    }

    public void refreshIMG() {
        viewHolder.ib_chat.setImageResource(R.mipmap.weixin_normal);
        viewHolder.ib_address.setImageResource(R.mipmap.contact_list_normal);
        viewHolder.ib_friends.setImageResource(R.mipmap.find_normal);
    }

    /**
     * fragment 局部内刷新
     */
    public void Refresh() {
        viewHolder.fragmentAdapter.notifyDataSetChanged();
    }

    /**
     * ViewPager 的监听控制事件类
     */
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    refreshIMG();
                    viewHolder.ib_chat.setImageResource(R.mipmap.weixin_pressed);
                    button_selext_index = 0;
                    break;
                case 1:
                    refreshIMG();
                    viewHolder.ib_address.setImageResource(R.mipmap.contact_list_pressed);
                    button_selext_index = 1;
                    break;
                case 2:
                    refreshIMG();
                    viewHolder.ib_friends.setImageResource(R.mipmap.find_pressed);
                    button_selext_index = 2;
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public String getUsername(){
        return username;
    }


}
