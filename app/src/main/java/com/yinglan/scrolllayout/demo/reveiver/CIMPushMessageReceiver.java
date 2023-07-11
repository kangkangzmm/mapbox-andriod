/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * **************************************************************************************
 *
 *                         Website : http://www.farsunset.com                           *
 *
 * **************************************************************************************
 */
package com.yinglan.scrolllayout.demo.reveiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;



import com.yinglan.scrolllayout.demo.R;
import com.yinglan.scrolllayout.demo.app.CIMApplication;
import com.yinglan.scrolllayout.demo.cimactivity.MessageActivity;
import com.yinglan.scrolllayout.demo.farsunset.cim.sdk.android.CIMEventBroadcastReceiver;
import com.yinglan.scrolllayout.demo.farsunset.cim.sdk.android.CIMListenerManager;
import com.yinglan.scrolllayout.demo.farsunset.cim.sdk.android.model.Message;


/**
 * 消息入口，所有消息都会经过这里
 */
public final class CIMPushMessageReceiver extends CIMEventBroadcastReceiver {


    /**
     * 当收到消息时调用此方法
     */
    @Override
    public void onMessageReceived(com.yinglan.scrolllayout.demo.farsunset.cim.sdk.android.model.Message sdkMessage, Intent intent) {

        /*
         * 通知到每个页面接收消息
         */
        CIMListenerManager.notifyOnMessageReceived(sdkMessage);


        /*
         * 切换到后台 弹通知栏
         */
        if (CIMApplication.getInstance().isAppInBackground()){
            showMessageNotification(sdkMessage);
        }
    }

    private void showMessageNotification(Message message){

        NotificationManager notificationMgr = ContextCompat.getSystemService(CIMApplication.getInstance(),NotificationManager.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(CIMApplication.getInstance(),CIMApplication.NOTIFICATION_CHANNEL_ID);

        Intent intent =  new Intent();
        intent.setClass(CIMApplication.getInstance(), MessageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(CIMApplication.getInstance(), 1,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setWhen(System.currentTimeMillis());
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);

        builder.setContentTitle(CIMApplication.NOTIFICATION_CHANNEL_NAME);
        builder.setContentText(message.getContent());


        Notification notification = builder.build();
        notificationMgr.notify(0, notification);
    }

}
