package com.xiaweizi.qnews;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.xiaweizi.qnews.bean.MessageBean;
import com.xiaweizi.qnews.commons.NotificationUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by xiaweizi on 2017/3/14.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver---->";

    private NotificationManager nm;
    private NotificationUtils mUtils;
    private Gson mGson;

    @Override
    public void onReceive(Context context, Intent intent) {

        mGson = new Gson();
        mUtils = new NotificationUtils(context);
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        Log.i(TAG, "onReceive - " + intent.getAction() + ", extras: " + bundle.toString());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.i(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "接受到推送下来的自定义消息");
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "接受到推送下来的通知");

            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "用户点击打开了通知");

            Log.i(TAG, "消息是: " + bundle.toString());
            openNotification(context, bundle);

        } else {
            Log.i(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.i(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.i(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i(TAG, "openNotification: " + extras);

    }

    private void processCustomMessage(Context context, Bundle bundle) {

        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        if (TextUtils.isEmpty(message)) {
            Log.w(TAG, "Unexpected: empty title (friend). Give up");
            return;
        }

        MessageBean messageBean = mGson.fromJson(message, MessageBean.class);
        mUtils.normalNotify(messageBean);
    }

}
