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
import android.widget.Button;

import com.exalture.tracer.activity.SettingsActivity;


public class MainActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private dbContacts dbcontact;
    Context	context;

    private Button moreButton;
    private Button locationButton;
    private Button contactsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbcontact = new dbContacts(getApplicationContext());

        contactsButton = (Button) findViewById(R.id.contacts_button);
        locationButton = (Button) findViewById(R.id.location_button);
        moreButton = (Button) findViewById(R.id.more_button);

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

        contactsButton.setOnClickListener(this);
        locationButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contacts_button:
                startActivity(new Intent(this, EditListActivity.class));
                break;
            case R.id.location_button:
                startActivity(new Intent(this, SendLocationActivity.class));
                break;
            case R.id.more_button:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }
    }
}
