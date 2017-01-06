package com.threeman.fuckchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threeman.fuckchat.R;

/**
 *  聊天界面
 * Created by cjz on 2017/1/6 0006.
 */
public class ChatFragment extends Fragment{

    private View ChatView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ChatView = inflater.inflate(R.layout.fragment_chat,container,false);
        return ChatView;
    }
}
