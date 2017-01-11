package com.threeman.fuckchat.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.activity.HandleNewFriendsActivity;
import com.threeman.fuckchat.activity.MainActivity;
import com.threeman.fuckchat.bean.Contacts;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.db.dao.ContactsDao;
import com.threeman.fuckchat.globle.ContactsState;
import com.threeman.fuckchat.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录界面
 * Created by cjz on 2017/1/6 0006.
 */
public class AddressListFragment extends Fragment {

    private final static String TAG = "ChatFragment";
    private final static int QUERY_ALL_USER = 1;
    private boolean is_have_new_friend = false;  //有没有新朋友
    private View addressView;
    private MyAdapter adapter;
    private RecyclerView rv_contacts;
    private ArrayList<User> users;
    private ContactsDao contactsDao;
    private MainActivity mMainActivity;
    private String username;
    private int new_friend_sum = 0;
    private List<Contacts> listContacts;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QUERY_ALL_USER:
                    User user = new User();
                    user.setUsername("新朋友");
                    users.add(0, user);
                    adapter = new MyAdapter();
                    rv_contacts.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addressView = inflater.inflate(R.layout.fragment_address, container, false);
        initView(addressView);
        return addressView;
    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    private void initView(View view) {
        rv_contacts = (RecyclerView) view.findViewById(R.id.rv_contacts);
    }

    private void initData() {
        rv_contacts.setLayoutManager(new LinearLayoutManager(getContext()));
        QueryAllUser();
        listContacts=new ArrayList<>();
        contactsDao = new ContactsDao(getContext());
        mMainActivity = (MainActivity) getActivity();
        username = mMainActivity.getUsername();
        listContacts.clear();
        listContacts = contactsDao.queryAllContacts(username);
        for (Contacts con : listContacts) {
            if (con.getContactsState().equals(ContactsState.CONTACTS_NOT_HANDLE)) {
                is_have_new_friend = true;
                new_friend_sum++;
            } else {
                is_have_new_friend = false;
            }
        }
    }

    /**
     * 查询所有用户
     */
    public void QueryAllUser() {
        users = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>("user_info");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e != null) {
                    Log.e(TAG, "出错了: ");
                    return;
                }
                for (AVObject ob : list) {
                    User user = new User();
                    user.setUsername(ob.getString("username"));
                    user.setNickname(ob.getString("nickname"));
                    user.setPassword(ob.getString("password"));
                    user.setNetPath(ob.getString("netPath"));
                    user.setLocalPath(ob.getString("localPath"));
                    users.add(user);
                }
                handler.sendEmptyMessage(QUERY_ALL_USER);

            }
        });
    }


    /**
     * 适配器
     */
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_contacts, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (position == 0) {
                holder.item_contacts_head.setImageResource(R.mipmap.icon_titleaddfriend);
                holder.item_contacts_head.setBackgroundColor(Color.parseColor("#01D9AE"));
                //有新朋友添加请求
                if (is_have_new_friend) {
                    holder.item_contacts_name.setText("有" + new_friend_sum + "个新朋友 ");
                    holder.item_contacts_name.setTextSize(20);
                    holder.item_contacts_name.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    holder.item_contacts_name.setText(users.get(position).getUsername());
                }
            } else {
                holder.item_contacts_name.setText(users.get(position).getUsername());
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView item_contacts_head;
            TextView item_contacts_name;

            public MyViewHolder(View itemView) {
                super(itemView);
                item_contacts_head = (ImageView) itemView.findViewById(R.id.item_contacts_head);
                item_contacts_name = (TextView) itemView.findViewById(R.id.item_contacts_name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            UIUtil.toastShort(getContext(), "你的操作有误，请从新选择");
                            return;
                        } else {
                            if (position == 0) {
                                Intent intent = new Intent(getContext(), HandleNewFriendsActivity.class);
                                intent.putExtra("username", username);
                                getContext().startActivity(intent);
                            } else {
                                //进入聊天界面
                            }
                        }
                    }
                });
            }
        }
    }
}
