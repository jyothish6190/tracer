package com.exalture.tracer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
 
public class myReceiver extends Service{
	
	private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
	private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    private CallBr br_call;
	Context context;
	
	  @Override
	  public void onCreate()
	   {
	    super.onCreate();  
	    this.context = getApplicationContext();  
	    //br_call = new CallBr();
	   }

	 
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId)
	   {
	    final IntentFilter filter = new IntentFilter();
	    filter.addAction(ACTION_OUT);
	    filter.addAction(ACTION_IN); 
	    this.br_call = new CallBr();
	    this.registerReceiver(this.br_call, filter); 
	    return (START_STICKY);
	   }
	  
	  @Override
	  public IBinder onBind(Intent intent) 
	  {
	   return null;
	  }
	  
}
