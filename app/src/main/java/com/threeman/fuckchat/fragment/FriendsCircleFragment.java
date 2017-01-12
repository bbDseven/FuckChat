package com.threeman.fuckchat.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.threeman.fuckchat.activity.AddFriendsActivity;
import com.threeman.fuckchat.activity.MainActivity;
import com.threeman.fuckchat.activity.MyFriendsActivity;
import com.threeman.fuckchat.bean.Contacts;
import com.threeman.fuckchat.bean.FriendsCircle;
import com.threeman.fuckchat.db.dao.ContactsDao;
import com.threeman.fuckchat.db.dao.FriendsCircleDao;
import com.threeman.fuckchat.util.UIUtil;
import com.threeman.fuckchat.view.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 朋友圈界面
 * Created by cjz on 2017/1/6 0006.
 */
public class FriendsCircleFragment extends Fragment {

    private final static int TYPE_TOP = 0;  //图片布局
    private final static int TYPE_ITEM = 1;  //朋友圈信息
    private final static String TAG = "FriendsCircleFragment";
    final Uri uri = Uri.parse("content://com.threeman.fuckchat.friends");  //内容提供者URI
    private View FriendsCircleView;
    private RecyclerView rv_myFriend;
    private MainActivity mMainActivity;
    private String username;
    private List<FriendsCircle> listFriends;
    private FriendsCircleDao friendsDao;
    private ContactsDao contactsDao;
    private MyAdapter myAdapter;
    private List<Contacts> listContacts;
    private String local_date;  //本地朋友圈中，更新到最新的时间



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FriendsCircleView = inflater.inflate(R.layout.fragment_friendscircle, container, false);

        initView(FriendsCircleView);
        initEvent();
        return FriendsCircleView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView(View view) {
        rv_myFriend = (RecyclerView) view.findViewById(R.id.rv_myFriend);
    }


    private void initEvent() {
    }

    private void initData() {
        mMainActivity = (MainActivity) getActivity();
        username = mMainActivity.getUsername();
        friendsDao = new FriendsCircleDao(getContext());
        contactsDao = new ContactsDao(getContext());
        listFriends = new ArrayList<>();

        ContentResolver resolver = getContext().getContentResolver();
        resolver.registerContentObserver(uri, true, new MyContentObserver(new Handler()));


        //查询出所有联系人
        listContacts = contactsDao.queryAllAcceptContacts(username);

        //查询所有朋友圈
        listFriends = friendsDao.queryAll(username);
        for (FriendsCircle fir :
                listFriends) {
            Log.e(TAG, "日期: "+fir.getDate());
        }
        if (listFriends.size()>0){
            local_date = listFriends.get(listFriends.size()-1).getDate();
            QueryAllFriends(local_date);
        }else {
            QueryAllFriends("2000-12-28 00:00:00");
        }




        //假数据，头部布局占位置
        listFriends.add(0, new FriendsCircle());
        Log.e(TAG, "listFriends: " + listFriends.size());

        myAdapter = new MyAdapter();
        rv_myFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_myFriend.setAdapter(myAdapter);
    }

    public void QueryAllFriends(String local_date) {
        AVQuery<AVObject> query = new AVQuery<>("FriendsCircle");
        query.whereEqualTo("username", username);
        for (Contacts con : listContacts) {
            query.whereEqualTo("username", con.getUsername());
            Log.e(TAG, "con.getUsername(): "+con.getUsername());
        }
        Log.e(TAG, "local_date: "+local_date);
        query.whereGreaterThan("date",local_date);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e != null) {
                    UIUtil.toastShort(getContext(), "出错啦");
                    Log.e(TAG, "出错啦: "+e.toString());
                } else {
                    Log.e(TAG, "list新朋友圈大小: " + list.size());
                    for (int i = 0; i < list.size(); i++) {
//                        String username = list.get(list.size() - i - 1).getString("username");
//                        String content = list.get(list.size() - i - 1).getString("content");
//                        String imagePath = list.get(list.size() - i - 1).getString("imagePath");
//                        String date = list.get(list.size() - i - 1).getString("date");
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
            listFriends = friendsDao.queryAll(username);
            listFriends.add(0, new FriendsCircle());
            myAdapter.notifyDataSetChanged();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_TOP) {
                view = LayoutInflater.from(getContext()).inflate
                        (R.layout.item_friends_picture, parent, false);
                return new MyViewHolderOne(view);
            } else {
                view = LayoutInflater.from(getContext()).inflate
                        (R.layout.item_friends, parent, false);
                return new MyViewHolderTwo(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_ITEM) {
                MyViewHolderTwo holderTwo = (MyViewHolderTwo) holder;
                holderTwo.item_friends_name.setText(listFriends.get(listFriends.size()-position).getUsername());
                holderTwo.item_friends_content.setText(listFriends.get(listFriends.size()-position).getContent());
            } else if (getItemViewType(position) == TYPE_TOP) {
                MyViewHolderOne holderOne = (MyViewHolderOne) holder;
                holderOne.ib_myFriend_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), MyFriendsActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
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
            ImageView item_contacts_head;
            TextView item_friends_name;
            TextView item_friends_content;

            public MyViewHolderTwo(View itemView) {
                super(itemView);
                item_contacts_head = (ImageView) itemView.findViewById(R.id.item_contacts_head);
                item_friends_name = (TextView) itemView.findViewById(R.id.item_friends_name);
                item_friends_content = (TextView) itemView.findViewById(R.id.item_friends_content);
            }
        }
    }

}
