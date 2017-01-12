package com.threeman.fuckchat.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.bean.Contacts;
import com.threeman.fuckchat.bean.FriendsCircle;
import com.threeman.fuckchat.db.dao.ContactsDao;
import com.threeman.fuckchat.db.dao.FriendsCircleDao;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 我的朋友圈（我的相册）
 * <p/>
 * author: Greetty
 * <p/>
 * date: 2017/1/6 16:27
 * <p/>
 * update: 2017/1/6
 * <p/>
 * version: v1.0
 */
public class MyFriendsActivity extends BaseActivity implements TitleView.OnAddClickListener {

    private final static int TYPE_TOP = 0;  //图片布局
    private final static String TAG ="MyFriendsActivity";
    private final static int TYPE_ITEM = 1;  //朋友圈信息
    final Uri uri = Uri.parse("content://com.threeman.fuckchat.friends");  //内容提供者URI
    private TitleView tv_myFriend;
    private RecyclerView rv_myFriend;
    private List<FriendsCircle> listFriends;
    private FriendsCircleDao friendsDao;
    private MyAdapter myAdapter;
    private String username;
    private ContactsDao contactsDao;
    private List<Contacts> listContacts;
    private String local_date;  //本地朋友圈中，更新到最新的时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        tv_myFriend = findViewByIds(R.id.tv_myFriend);
        rv_myFriend = findViewByIds(R.id.rv_myFriend);
    }


    private void initEvent() {
        tv_myFriend.setAddClickListener(this);
    }

    private void initData() {
        tv_myFriend.setTitleName("我的相册");
        listFriends=new ArrayList<>();
        contactsDao = new ContactsDao(this);
        friendsDao = new FriendsCircleDao(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        //内容监听
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(uri, true, new MyContentObserver(new Handler()));

        //查询出所有联系人
        listContacts = contactsDao.queryAllAcceptContacts(username);


        //查询所有朋友圈
        listFriends = friendsDao.queryAll(username);

        if (listFriends.size()>0){
            local_date = listFriends.get(listFriends.size()-1).getDate();
            QueryAllFriends(local_date);
        }else {
            QueryAllFriends("2000-12-28 00:00:00");
        }


        //假数据，头部布局占位置
        listFriends = friendsDao.querySingleFriends(username);
        listFriends.add(0,new FriendsCircle());

        Log.e(TAG, "listFriends: "+listFriends.size());
        myAdapter = new MyAdapter();
        rv_myFriend.setLayoutManager(new LinearLayoutManager(this));
        rv_myFriend.setAdapter(myAdapter);
    }


    public void QueryAllFriends(String local_date) {
        AVQuery<AVObject> query = new AVQuery<>("FriendsCircle");
        query.whereEqualTo("username", username);
        for (Contacts con : listContacts) {
            query.whereEqualTo("username", con.getUsername());
        }
        Log.e(TAG, "local_date: "+local_date);
        query.whereGreaterThan("date",local_date);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e != null) {
                    UIUtil.toastShort(MyFriendsActivity.this, "出错啦");
                    Log.e(TAG, "出错啦: "+e.toString());
                } else {
                    Log.e(TAG, "list新朋友圈大小: " + list.size());
                    for (int i = 0; i < list.size(); i++) {
                        String username = list.get(i).getString("username");
                        String content = list.get(i).getString("content");
                        String imagePath = list.get(i).getString("imagePath");
                        String date = list.get(i).getString("date");
                        friendsDao.add(username, content, imagePath, date);
                    }
                }
            }
        });
    }


    /**
     * 内容监听者者
     */
    private class MyContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (listFriends.size() > 0) {
                listFriends.clear();
            }
            //假数据，头部布局占位置
            listFriends = friendsDao.queryAll(username);
            listFriends.add(0,new FriendsCircle());
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void add() {
//        UIUtil.toastShort(this, "添加朋友圈");
        Intent intent = new Intent(this, AddFriendsActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_TOP) {
                view = LayoutInflater.from(MyFriendsActivity.this).inflate
                        (R.layout.item_friends_picture, parent, false);
                return new MyViewHolderOne(view);

            } else {
                view = LayoutInflater.from(MyFriendsActivity.this).inflate
                        (R.layout.item_my_friends, parent, false);
                return new MyViewHolderTwo(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_ITEM) {
                MyViewHolderTwo holderTwo = (MyViewHolderTwo) holder;

                Log.e(TAG, "日期: "+listFriends.get(position).getDate());
                String moth = listFriends.get(position).getDate().substring(5, 7);
                String day = listFriends.get(position).getDate().substring(8, 10);
                holderTwo.item_myFriend_day.setText(moth);
                holderTwo.item_myFriend_month.setText(day);
                holderTwo.item_myFriend_content.setText(listFriends.
                        get(listFriends.size()-position).getContent());

            } else if (getItemViewType(position) == TYPE_TOP) {
                MyViewHolderOne holderOne = (MyViewHolderOne) holder;
                holderOne.ib_myFriend_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtil.toastShort(MyFriendsActivity.this, "信息个人详细信息");
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == TYPE_TOP) {
                return TYPE_TOP;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getItemCount() {
            return listFriends.size();
        }

        public class MyViewHolderOne extends RecyclerView.ViewHolder {
            ImageButton ib_myFriend_head;

            public MyViewHolderOne(View itemView) {
                super(itemView);
                ib_myFriend_head = (ImageButton) itemView.findViewById(R.id.ib_myFriend_head);
            }
        }

        public class MyViewHolderTwo extends RecyclerView.ViewHolder {
            TextView item_myFriend_day;
            TextView item_myFriend_month;
            TextView item_myFriend_content;

            public MyViewHolderTwo(View itemView) {
                super(itemView);
                item_myFriend_day = (TextView) itemView.findViewById(R.id.item_myFriend_day);
                item_myFriend_month = (TextView) itemView.findViewById(R.id.item_myFriend_month);
                item_myFriend_content = (TextView) itemView.findViewById(R.id.item_myFriend_content);
            }
        }
    }


}
