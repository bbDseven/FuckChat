package com.threeman.fuckchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

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

public class MainActivity extends BaseActivity implements TitleView.OnAddClickListener {

    private ViewHolder viewHolder;

    public class ViewHolder{

        private ViewPager viewPager;
        private List<Fragment> fragments;//fragments集合
        private FragmentAdapter fragmentAdapter;//fragments适配器
        private TitleView tv_title;
    }
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
     *  初始化各个控件
     * */
    public void initView(){
        viewHolder = new ViewHolder();
        viewHolder.viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewHolder.tv_title=findViewByIds(R.id.tv_title);
        viewHolder.fragments = new ArrayList<>();
    }

    private void initEvent() {
        viewHolder.tv_title.setAddClickListener(this);
    }
    private void initDate() {
        viewHolder.tv_title.setTitleName("约会神器");
    }



    /**
     * 点击头部+按钮执行该方法
     */
    @Override
    public void add() {
        UIUtil.toastShort(this,"点击了添加按钮");
        startActivity(new Intent(this,MyFriendsActivity.class));
    }

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
     *  fragment 内刷新
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

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
