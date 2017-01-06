package com.threeman.fuckchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.threeman.fuckchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 *  通讯录界面
 * Created by cjz on 2017/1/6 0006.
 */
public class AddressListFragment extends Fragment{

    private View addressView;
    private RecyclerView rv_contacts;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addressView = inflater.inflate(R.layout.fragment_address,container,false);
        initView(addressView);
        initData();
        return addressView;
    }

    private void initView(View view) {
        rv_contacts= (RecyclerView) view.findViewById(R.id.rv_contacts);
    }

    private void initData() {
        MyAdapter adapter = new MyAdapter();
        list=new ArrayList<>();
        for (int i=0;i<20;i++){
            list.add("陈贵堂"+i);
        }
        rv_contacts.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_contacts.setAdapter(adapter);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_contacts, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.item_contacts_name.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView item_contacts_head;
            TextView item_contacts_name;
            public MyViewHolder(View itemView) {
                super(itemView);
                item_contacts_head= (ImageView) itemView.findViewById(R.id.item_contacts_head);
                item_contacts_name= (TextView) itemView.findViewById(R.id.item_contacts_name);
            }
        }
    }
}
