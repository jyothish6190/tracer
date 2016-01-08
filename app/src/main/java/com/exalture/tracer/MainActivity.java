package com.exalture.tracer;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;

import com.exalture.tracer.activity.AddSecurityNumberActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private dbContacts dbcontact;
    List<Pref_Number> result_in;
    Context	context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbcontact = new dbContacts(getApplicationContext());

        context = getApplicationContext();
        context.startService(new Intent(context, myReceiver.class));
        SetPreference pref = new SetPreference(getApplicationContext());
        pref.setPreferenceNumber("curr_num", "null");
        pref.setPreferenceNumber("messCount", "0");

        GPSTracker gp = new GPSTracker(getBaseContext());
        if (!gp.isGPSEnabled || !gp.isNetworkEnabled) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean previouslyStarted = prefs.getBoolean(getString(R.string.previous), false);
		if(!previouslyStarted) {
			SharedPreferences.Editor edit = prefs.edit();
			edit.putBoolean(getString(R.string.previous), Boolean.TRUE);
			edit.commit();
			showHelp();
		}
	}
	
	public void showHelp() {
		Intent i=new Intent(MainActivity.this,ImageSlider.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void addContact(View v) {
		startActivity(new Intent(this, EditListActivity.class));
	}

	public void goToPref(View v) {
		startActivity(new Intent(this, AddSecurityNumberActivity.class));
	}

	public void goToUrgent(View v) { startActivity(new Intent(this, SendLocationActivity.class)); }

	public void goToEdit(View v) {
		result_in = new ArrayList<Pref_Number>();
		try {
			dbcontact.open();
			result_in = dbcontact.getContactLists();
			dbcontact.close();
			if(result_in.size() <= 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder
				.setTitle("No Contacts added..")
				.setMessage("Do you want to add contacts ?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {			      	
						startActivity(new Intent(MainActivity.this, EditListActivity.class));
					}
				})
				.setNegativeButton("No", null)						//Do nothing on no
				.show();
			} else {
				startActivity(new Intent(this, EditListActivity.class));
			}
		}
		catch(NullPointerException e) { }
	}
}
