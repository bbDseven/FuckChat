package com.threeman.fuckchat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.threeman.fuckchat.R;
import com.threeman.fuckchat.adapter.FragmentAdapter;
import com.threeman.fuckchat.fragment.AddressListFragment;
import com.threeman.fuckchat.fragment.ChatFragment;
import com.threeman.fuckchat.fragment.FriendsCircleFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewHolder viewHolder;

    public class ViewHolder{
        private ViewPager viewPager;
        private List<Fragment> fragments;//fragments集合
        private FragmentAdapter fragmentAdapter;//fragments适配器
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragmentViewPager();

    }

    /**
     *  初始化各个控件
     * */
    public void initView(){
        viewHolder = new ViewHolder();
        viewHolder.viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewHolder.fragments = new ArrayList<Fragment>();
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
