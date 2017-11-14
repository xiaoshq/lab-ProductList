package com.xiaoshq.productlist;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class mWidget extends AppWidgetProvider {
    private app myApp;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.m_widget);
        updateViews.setImageViewResource(R.id.appwidget_image, R.drawable.shoplist);
        Intent i = new Intent(context, ProductList.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置点击事件
        updateViews.setOnClickPendingIntent(R.id.m_widget, pi);
        // Instruct the widget manager to update the widget
        ComponentName me = new ComponentName(context, mWidget.class);
        appWidgetManager.updateAppWidget(me, updateViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int p_id = intent.getIntExtra("itemid", 0);
        myApp = (app) context.getApplicationContext();
        String productName = myApp.product_list.dataList.get(p_id).get("name").toString();
        String productPrice = myApp.product_list.dataList.get(p_id).get("price").toString();
        int img = (int) myApp.product_list.dataList.get(p_id).get("img");

        if(intent.getAction().equals("MyStaticFilter")) {
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.m_widget);
            Intent i = new Intent(context, ProductDetail.class);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.putExtras(intent.getExtras());
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setTextViewText(R.id.appwidget_text, productName+"仅售"+productPrice+"！");
            updateViews.setImageViewResource(R.id.appwidget_image, img);
            updateViews.setOnClickPendingIntent(R.id.m_widget, pi);
            ComponentName me = new ComponentName(context, mWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me, updateViews);
        } else if (intent.getAction().equals("MyDynamicFilter")) {
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.m_widget);
            Intent i = new Intent(context, ProductList.class);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.putExtra("isShoppingCar", false);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setTextViewText(R.id.appwidget_text, productName+"已添加到购物车");
            updateViews.setImageViewResource(R.id.appwidget_image, img);
            updateViews.setOnClickPendingIntent(R.id.m_widget, pi);
            ComponentName me = new ComponentName(context, mWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me, updateViews);
        }
    }
}

