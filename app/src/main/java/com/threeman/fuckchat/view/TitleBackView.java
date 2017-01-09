package com.threeman.fuckchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threeman.fuckchat.R;

/**
 * description: 头部titleBar样式栏(含返回键)
 *
 * author: Greetty
 *
 * date: 2017/1/6 14:28
 *
 * update: 2017/1/6
 *
 * version: v1.0
*/
public class TitleBackView extends RelativeLayout {
    private OnBackClickListener mOnBackClickListener;
    private ImageView bar_back;
    private TextView bar_title;

    public TitleBackView(Context context) {
        super(context,null);
    }

    public TitleBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.bar_back, this);
        bar_back = (ImageView) findViewById(R.id.bar_back);
        bar_title= (TextView) findViewById(R.id.bar_title);
        bar_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBackClickListener!=null){
                    mOnBackClickListener.Back();
                }
            }
        });
    }

    /**
     * 设置title图片
     * @param resId  图片资源ID
     */
    public void setTitleImage(int resId){
        bar_back.setImageResource(resId);
    }
    /**
     * 设置title名字
     * @param titleName  名字
     */
    public void setTitleName(String titleName){
        bar_title.setText(titleName);
    }
    public interface OnBackClickListener{
        void Back();
    }

    public void setBackClickListener(OnBackClickListener onBackClickListener){
        this.mOnBackClickListener=onBackClickListener;
    }

    /**
     * 设置返回图标是否可见
     * @param visible
     */
    public void setBackImageVisible(boolean visible){
        if (visible){
            bar_back.setVisibility(VISIBLE);
        }else {
            bar_back.setVisibility(INVISIBLE);
        }
    }


}
