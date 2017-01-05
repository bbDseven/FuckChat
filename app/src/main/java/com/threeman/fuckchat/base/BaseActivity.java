package com.threeman.fuckchat.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by bbDseven on 2017/1/5.
 */

public class BaseActivity extends Activity {
    public <E extends View>E findViewByIds(int id){
        return (E)findViewById(id);
    }
}
