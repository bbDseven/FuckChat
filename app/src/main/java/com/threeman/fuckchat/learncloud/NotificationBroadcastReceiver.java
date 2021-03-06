package com.threeman.fuckchat.learncloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.threeman.fuckchat.MyApplication;
import com.threeman.fuckchat.activity.ChatActivity;
import com.threeman.fuckchat.activity.HandleNewFriendsActivity;
import com.threeman.fuckchat.activity.LoginActivity;
import com.threeman.fuckchat.activity.MainActivity;
import com.threeman.fuckchat.activity.TestActivity;
import com.threeman.fuckchat.globle.ContactsState;
import com.threeman.fuckchat.util.SharedPreferencesUtils;
import com.threeman.fuckchat.util.UIUtil;

/**
 * Created by wli on 15/9/8.
 * 因为 notification 点击时，控制权不在 app，此时如果 app 被 kill 或者上下文改变后，
 * 有可能对 notification 的响应会做相应的变化，所以此处将所有 notification 都发送至此类，
 * 然后由此类做分发。
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private final static String TAG = "Notification-Receiver";
    private boolean IS_NEW_FRIEND;
    private String username;
    ;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AVImClientManager.getInstance().getClient() == null) {
            gotoLoginActivity(context);
        } else {
            String conversationId = intent.getStringExtra(Constants.CONVERSATION_ID);
            IS_NEW_FRIEND = intent.getBooleanExtra(ContactsState.key, false);

            Log.e(TAG, "IS_NEW_FRIEND: " + IS_NEW_FRIEND);
            username = (String) SharedPreferencesUtils.getParam(context, "username", "");

            if (IS_NEW_FRIEND) {
                finishAll();
                UIUtil.toastShort(context, "跳转到是否接受该好友请求");
                Intent newFriendIntent = new Intent(context, HandleNewFriendsActivity.class);
                newFriendIntent.putExtra("username", username);
                newFriendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newFriendIntent);
            } else if (!TextUtils.isEmpty(conversationId)) {
                gotoSingleChatActivity(context, intent);
            }
        }
    }

    /**
     * 如果 app 上下文已经缺失，则跳转到登陆页面，走重新登陆的流程
     *
     * @param context
     */
    private void gotoLoginActivity(Context context) {
        finishAll();
        Intent startActivityIntent = new Intent(context, LoginActivity.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }

    /**
     * 跳转至单聊页面
     *
     * @param context 上下文对象
     * @param intent  intent
     */
    private void gotoSingleChatActivity(Context context, Intent intent) {
        finishAll();
        String target = (String) SharedPreferencesUtils.getParam(context, "target", "");
        Intent startActivityIntent = new Intent(context, ChatActivity.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityIntent.putExtra("username", username);
        startActivityIntent.putExtra("target", target);
        Log.e(TAG, "username: " + username);
        Log.e(TAG, "target: " + target);
        startActivityIntent.putExtra(Constants.MEMBER_ID, intent.getStringExtra(Constants.MEMBER_ID));
        context.startActivity(startActivityIntent);
    }

    /**
     * 关闭掉之前的所有Activity
     */
    public void finishAll(){
        MyApplication myApplication = new MyApplication();
        myApplication.finishAllActivity();
    }
}
