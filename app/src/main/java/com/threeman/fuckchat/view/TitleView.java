package com.threeman.fuckchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threeman.fuckchat.R;

/**
 * description: 头部titleBar样式局域栏
 *
 * author: Greetty
 *
 * date: 2017/1/6 14:28
 *
 * update: 2017/1/6
 *
 * version: v1.0
*/
public class TitleView extends LinearLayout {
    private OnAddClickListener mOnAddClickListener;
    private ImageView title_add_contacts;
    private TextView title_name;

    public TitleView(Context context) {
        super(context,null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_title, this);
        title_add_contacts = (ImageView) findViewById(R.id.title_add_contacts);
        title_name= (TextView) findViewById(R.id.title_name);
        title_add_contacts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddClickListener!=null){
                    mOnAddClickListener.add();
                }
            }
        });
    }

    /**
     * 设置title图片
     * @param resId  图片资源ID
     */
    public void setTitleImage(int resId){
        title_add_contacts.setImageResource(resId);
    }
    /**
     * 设置title名字
     * @param titleName  名字
     */
    public void setTitleName(String titleName){
        title_name.setText(titleName);
    }
    public interface OnAddClickListener{
        void add();
    }

    public void setAddClickListener(OnAddClickListener onAddClickListener){
        this.mOnAddClickListener=onAddClickListener;
    }


}
