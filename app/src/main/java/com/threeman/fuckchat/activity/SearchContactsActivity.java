package com.threeman.fuckchat.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threeman.fuckchat.R;
import com.threeman.fuckchat.base.BaseActivity;
import com.threeman.fuckchat.util.UIUtil;

/**
 * description: 查询添加女神
 *
 * author: Greetty
 *
 * date: 2017/1/7 11:50
 *
 * update: 2017/1/7
 *
 * version: v1.0
*/
public class SearchContactsActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private class ViewHolder {

        ImageView bar_btn_back;
        EditText bar_et_search;
        Button bar_btn_clear_search;
        RelativeLayout search_item;
        TextView search_tv_content;
        TextView search_result;
    }

    private ViewHolder viewHolder;

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
        viewHolder.search_result = findViewByIds(R.id.search_result);
    }

    private void initEvent() {
        viewHolder.bar_btn_back.setOnClickListener(this);
        viewHolder.bar_btn_clear_search.setOnClickListener(this);
        viewHolder.bar_et_search.addTextChangedListener(this);
        viewHolder.search_item.setOnClickListener(this);
    }

    private void initDate() {

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
                 UIUtil.toastShort(this,"开始查询联系人");
                break;
            default:
                break;
        }

    }


    private String et_content;  //文本框内容
    //监听文本框变化-->变化前
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    //监听文本框变化-->变化中
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        et_content=s.toString();
    }
    //监听文本框变化-->变化后
    @Override
    public void afterTextChanged(Editable s) {
        viewHolder.search_tv_content.setText(et_content);
    }
}
