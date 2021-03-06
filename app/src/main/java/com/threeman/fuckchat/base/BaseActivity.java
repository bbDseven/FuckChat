package com.threeman.fuckchat.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.threeman.fuckchat.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbDseven on 2017/1/5.
 */

public class BaseActivity extends AppCompatActivity {

    public MyApplication mApplication = new MyApplication();

    public <E extends View>E findViewByIds(int id){
        return (E)findViewById(id);
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            Log.e("BaseActivity", "e: "+e.toString());
            toast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    protected void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApplication.removeActivity(this);
    }

}
