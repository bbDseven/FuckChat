package com.threeman.fuckchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threeman.fuckchat.R;

/**
 *  通讯录界面
 * Created by cjz on 2017/1/6 0006.
 */
public class AddressListFragment extends Fragment{

    private View AddressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AddressView = inflater.inflate(R.layout.fragment_address,container,false);
        return AddressView;
    }
}
