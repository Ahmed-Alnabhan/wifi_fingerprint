package com.elearnna.www.wififingerprint.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.activities.APsList;

/**
 * Created by Ahmed on 10/15/2017.
 */

public class WifiWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wifi_fingerprint_widget);

        Intent intent = new Intent(context, APsList.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_toolbar, pendingIntent);
        // Instruct the widget manager to update the widget. This intent
        // will be delivered to the onGetViewFactory() method in the WidgetService
        Intent intnt = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_aps_list_view, intnt);

        Intent ingListIntent = new Intent(context, APsList.class);
        PendingIntent ingPendingIntent = PendingIntent.getActivity(context, 0, ingListIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_aps_list_view, ingPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds( new ComponentName(context, WifiWidgetProvider.class));
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_aps_list_view);
        }
        super.onReceive(context, intent);
    }

}
