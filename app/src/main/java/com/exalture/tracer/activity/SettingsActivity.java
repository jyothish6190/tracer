package com.exalture.tracer.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.exalture.tracer.R;
import com.exalture.tracer.adapter.SettingsAdapter;

public class SettingsActivity extends Activity {
    private static final String TAG = "SettingsActivity";

    private ListView settingsList;

    private SettingsAdapter adapter;
    private static String[] settings = {"Share App", "Rate App", "More Apps", "About us", "Product Tour"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsList = (ListView) findViewById(R.id.settings_list);

        adapter = new SettingsAdapter(this, settings);
        settingsList.setAdapter(adapter);

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                performAction(position);
            }
        });


    }

    private void performAction(int position){
        switch (position){
            case 0:
                shareApp();
                break;
            case 1:
                rateApp();
                break;
            case 2:
                moreApps();
                break;
            case 3:
                aboutUs();
                break;
            case 4:

                break;
            default:break;
        }
    }

    private void rateApp(){
        final Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        loadUrl(uri);
    }

    private void shareApp(){
        String shareText = "Tracer is a simple android app to inform your current location to your friends or family by calling them or receiving a call from them (even with a missed call). https://play.google.com/store/apps/details?id=com.exalture.tracer&hl=en";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tracer (Open it in Google Play Store to Download the Application)");

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void moreApps(){
        final Uri uri = Uri.parse("https://play.google.com/store/search?q=exalture&hl=en");
        loadUrl(uri);
    }

    private void aboutUs(){
        final Uri uri = Uri.parse("http://www.exalture.com/");
        loadUrl(uri);
    }


    private void loadUrl(Uri uri){
        final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

        if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
        {
            startActivity(rateAppIntent);
        }
    }

}
