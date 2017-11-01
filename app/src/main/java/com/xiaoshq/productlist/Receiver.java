package com.xiaoshq.productlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by ivan on 2017/10/30.
 */

public class Receiver extends BroadcastReceiver {
    public static final String STATICACTION = "MyStaticFilter";//静态广播的Action字符串
    public static final String DYNAMICACTION = "MyDynamicFilter";//动态广播的Action字符串
    private app myApp;

    @Override
    public void onReceive (Context context, Intent intent) {
        int p_id = intent.getIntExtra("itemid", 0);
        myApp = (app) context.getApplicationContext();
        String productName = myApp.product_list.dataList.get(p_id).get("name").toString();
        String productPrice = myApp.product_list.dataList.get(p_id).get("price").toString();
        int img = (int) myApp.product_list.dataList.get(p_id).get("img");
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), img);

        //动作检测
        if (intent.getAction().equals(STATICACTION)) { //静态广播
            //获取状态通知栏管理
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化通知栏构造器builder
            Notification.Builder builder = new Notification.Builder(context);
            //配置builder
            builder.setContentTitle("新商品热卖")
                    .setContentText(productName + "仅售" + productPrice + "！")
                    .setTicker("您有一条新消息")  //上升动画效果
                    .setLargeIcon(bm)
                    .setSmallIcon(img)
                    .setAutoCancel(true);
            //绑定intent，点击图标进入商品详情activity
            Intent mIntent = new Intent(context, ProductDetail.class);
            mIntent.putExtra("itemid", p_id);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            //绑定Notification，发送通知请求
            Notification notify = builder.build();
            manager.notify(0, notify);

        } else if (intent.getAction().equals(DYNAMICACTION)) { //动态广播
            //获取状态通知栏管理
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化通知栏构造器
            Notification.Builder builder = new Notification.Builder(context);
            //配置builder
            builder.setContentTitle("马上下单")
                    .setContentText(productName + "已添加到购物车")
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bm)
                    .setSmallIcon(img)
                    .setAutoCancel(true);
            //绑定intent，点击图标进入activity
            Intent mIntent = new Intent(context, ProductList.class);
            mIntent.putExtra("isShoppingCar", false);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            //绑定Notification，发送通知请求
            Notification notify = builder.build();
            manager.notify(myApp.notify_id++, notify);
        }
    }
}
