package com.threeman.fuckchat.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.bean.User;
import com.threeman.fuckchat.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 查询添加女神
 * <p/>
 * author: Greetty
 * <p/>
 * date: 2017/1/7 11:50
 * <p/>
 * update: 2017/1/7
 * <p/>
 * version: v1.0
 */
public class SearchContactsActivity extends BaseActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {

    private class ViewHolder {

        ImageView bar_btn_back;

        EditText bar_et_search;
        Button bar_btn_clear_search;
        RelativeLayout search_item;
        TextView search_tv_content;
        ListView lv_contacts;
    }

    private final static String TAG = "SearchContactsActivity";

    private final static int HAVE_CONTACTS = 1;  //有相关的联系人
    private final static int NO_CONTACTS = 0;  //没有相关联系人
    private ViewHolder viewHolder;
    private String mContent;  //用户输入信息
    private MyAdapter myAdapter;
    private List<User> users;
    private MyViewHolder myViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);

        initView();
        initEvent();
        initDate();
    }


    private void initView() {
        viewHolder = new ViewHolder();
        viewHolder.bar_btn_back = findViewByIds(R.id.bar_btn_back);
        viewHolder.bar_et_search = findViewByIds(R.id.bar_et_search);
        viewHolder.bar_btn_clear_search = findViewByIds(R.id.bar_btn_clear_search);
        viewHolder.search_item = findViewByIds(R.id.search_item);
        viewHolder.search_tv_content = findViewByIds(R.id.search_tv_content);
        viewHolder.lv_contacts = findViewByIds(R.id.lv_contacts);
    }

    private void initEvent() {
        viewHolder.bar_btn_back.setOnClickListener(this);
        viewHolder.bar_btn_clear_search.setOnClickListener(this);
        viewHolder.bar_et_search.addTextChangedListener(this);
        viewHolder.search_item.setOnClickListener(this);
    }

    private void initDate() {
        users = new ArrayList<>();
        myAdapter = new MyAdapter();
        viewHolder.lv_contacts.setAdapter(myAdapter);

        viewHolder.lv_contacts.setOnItemClickListener(this);
    }

    //ListView点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_request_contacts, null);
        final TextView dialog_et_content = (TextView) dialogView.findViewById(R.id.dialog_et_content);
        Button dialog_btn_cannal = (Button) dialogView.findViewById(R.id.dialog_btn_cannal);
        Button dialog_btn_ok = (Button) dialogView.findViewById(R.id.dialog_btn_ok);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        dialog_btn_cannal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = dialog_et_content.getText().toString().trim();
                UIUtil.toastShort(SearchContactsActivity.this,content);
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_btn_back:
                finish();
                break;
            case R.id.bar_btn_clear_search:
                viewHolder.bar_et_search.setText("");
                break;
            case R.id.search_item:
//                UIUtil.toastShort(this, "开始查询联系人");
                users.clear();
                mContent = viewHolder.bar_et_search.getText().toString().trim();
                AVQuery<AVObject> query = new AVQuery<>("user_info");
                query.whereContains("username", mContent);
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e != null) {
                            Log.e(TAG, "查询联系人出错啦:");
                            return;
                        }
                        if (list != null && list.size() > 0) {
                            for (AVObject ob : list) {
                                User user = new User();
                                user.setUsername(ob.getString("username"));
                                user.setNickname(ob.getString("nickname"));
                                user.setPassword(ob.getString("password"));
                                user.setNetPath(ob.getString("netPath"));
                                user.setLocalPath(ob.getString("localPath"));
                                users.add(user);
                            }
                            myAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = View.inflate(SearchContactsActivity.this, R.layout.item_contacts, null);
                myViewHolder = new MyViewHolder();
                myViewHolder.item_contacts_head = (ImageView) view.findViewById(R.id.item_contacts_head);
                myViewHolder.item_contacts_name = (TextView) view.findViewById(R.id.item_contacts_name);
                view.setTag(myViewHolder);
            } else {
                view = convertView;
                myViewHolder = (MyViewHolder) view.getTag();
            }

            myViewHolder.item_contacts_name.setText(users.get(position).getUsername());

            return view;
        }
    }

    static class MyViewHolder {
        ImageView item_contacts_head;
        TextView item_contacts_name;
    }


    private String et_content;  //文本框内容

    //监听文本框变化-->变化前
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //监听文本框变化-->变化中
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        et_content = s.toString();
    }

    //监听文本框变化-->变化后
    @Override
    public void afterTextChanged(Editable s) {
        viewHolder.search_tv_content.setText(et_content);
    }
}
