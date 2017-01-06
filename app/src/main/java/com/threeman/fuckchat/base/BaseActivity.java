package com.threeman.fuckchat.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by bbDseven on 2017/1/5.
 */

public class BaseActivity extends AppCompatActivity {
    public <E extends View>E findViewByIds(int id){
        return (E)findViewById(id);
    }
}
