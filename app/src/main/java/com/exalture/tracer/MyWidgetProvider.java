package com.exalture.tracer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.exalture.tracer.widgetdialog.WidgetDialog;

public class MyWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_CLICK = "ACTION_CLICK";
    public static String YOUR_AWESOME_ACTION = "YourAwesomeAction";
    public  Context context;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	    ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	    for (int widgetId : allWidgetIds) {
	        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
	        R.layout.widget);
	        remoteViews.setTextViewText(1,"Action");
	      // Register an onClickListener
	      Intent intent = new Intent(context, MyWidgetProvider.class);
	      intent.setAction(YOUR_AWESOME_ACTION);
	      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
	      PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	      remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
	      appWidgetManager.updateAppWidget(widgetId, remoteViews);
  
	    }
	}

	@Override
	public void onReceive(Context context, Intent intent) {
	    super.onReceive(context, intent);
	    GPSTracker gps=new GPSTracker(context);
	    
	    if (!gps.isGPSEnabled || !gps.isNetworkEnabled) {
	        intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  			context.startActivity(intent);
		}
	    if (intent.getAction().equals(YOUR_AWESOME_ACTION)) {
            Intent callIntent = new Intent(context, WidgetDialog.class);
                   callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(callIntent);
	    }
	}
}
	  