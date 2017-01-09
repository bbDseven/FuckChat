package com.threeman.fuckchat.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by bbDseven on 2017/1/5.
 */

public class BaseActivity extends AppCompatActivity {
    public <E extends View>E findViewByIds(int id){
        return (E)findViewById(id);
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            toast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    protected void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


}
