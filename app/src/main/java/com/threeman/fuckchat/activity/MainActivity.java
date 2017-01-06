package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.threeman.fuckchat.R;
import com.threeman.fuckchat.adapter.FragmentAdapter;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.fragment.AddressListFragment;
import com.threeman.fuckchat.fragment.ChatFragment;
import com.threeman.fuckchat.fragment.FriendsCircleFragment;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, TitleView.OnAddClickListener {

    private ViewHolder viewHolder;

    public class ViewHolder{

        private ViewPager viewPager;
        private List<Fragment> fragments;//fragments集合
        private FragmentAdapter fragmentAdapter;//fragments适配器
        private LinearLayout ll_chat,ll_address,ll_friends;
        private ImageButton ib_chat,ib_address,ib_friends;
        private TitleView tv_title;
//        private ImageButton ib_main_add;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
//        initDate();
        initFragmentViewPager();

    }
    /**
     *  初始化各个控件
     * */
    public void initView(){
        viewHolder = new ViewHolder();
        viewHolder.viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewHolder.fragments = new ArrayList<>();
        viewHolder.ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
        viewHolder.ll_address = (LinearLayout) findViewById(R.id.ll_address);
        viewHolder.ll_friends = (LinearLayout) findViewById(R.id.ll_friends);
        viewHolder.ib_chat = (ImageButton) findViewById(R.id.ib_chat);
        viewHolder.ib_address = (ImageButton) findViewById(R.id.ib_address);
        viewHolder.ib_friends = (ImageButton) findViewById(R.id.ib_friends);
//        viewHolder.ib_main_add = (ImageButton) findViewById(R.id.ib_main_add);
        viewHolder.tv_title = (TitleView) findViewById(R.id.tv_title);
    }

    private void initEvent() {
        viewHolder.ll_chat.setOnClickListener(this);
        viewHolder.ll_address.setOnClickListener(this);
        viewHolder.ll_friends.setOnClickListener(this);
//        viewHolder.ib_main_add.setOnClickListener(this);
        viewHolder.tv_title.setAddClickListener(this);

    }

    @Override
    public void add() {
        UIUtil.toastShort(this,"点击了添加按钮");
        startActivity(new Intent(this,MyFriendsActivity.class));
    }

//    private void initDate() {

//    }


    /**
     *  初始化 4个 Fragment 的ViewPager
     * */
    private void initFragmentViewPager() {
        viewHolder.fragments.add(new ChatFragment());           //装进一个聊天
        viewHolder.fragments.add(new AddressListFragment());    //装进一个通讯录
        viewHolder.fragments.add(new FriendsCircleFragment());  //装进一个朋友圈
        viewHolder.fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),viewHolder.fragments);
        viewHolder.viewPager.setAdapter(viewHolder.fragmentAdapter);
        viewHolder.viewPager.addOnPageChangeListener(new MyPageChangeListener());//ViewPager滑动改变事件
    }

    /**
     * 点击头部+按钮执行该方法
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_chat:
                viewHolder.viewPager.setCurrentItem(0);
                refreshIMG();
                viewHolder.ib_chat.setImageResource(R.mipmap.chat_show);
                break;
            case R.id.ll_address:
                viewHolder.viewPager.setCurrentItem(1);
                refreshIMG();
                viewHolder.ib_address.setImageResource(R.mipmap.address_show);
                break;
            case R.id.ll_friends:
                viewHolder.viewPager.setCurrentItem(2);
                refreshIMG();
                viewHolder.ib_friends.setImageResource(R.mipmap.friends_show);
                break;
            default:
                break;
        }
    }

    public void refreshIMG(){
        viewHolder.ib_chat.setImageResource(R.mipmap.chat_gone);
        viewHolder.ib_address.setImageResource(R.mipmap.address_gone);
        viewHolder.ib_friends.setImageResource(R.mipmap.friends_gone);
    }

    /**
     *  fragment 局部内刷新
     * */
    public void Refresh(){
        viewHolder.fragmentAdapter.notifyDataSetChanged();
    }

    /**
     *  ViewPager 的监听控制事件类
     * */
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    refreshIMG();
                    viewHolder.ib_chat.setImageResource(R.mipmap.chat_show);
                    break;
                case 1:
                    refreshIMG();
                    viewHolder.ib_address.setImageResource(R.mipmap.address_show);
                    break;
                case 2:
                    refreshIMG();
                    viewHolder.ib_friends.setImageResource(R.mipmap.friends_show);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
