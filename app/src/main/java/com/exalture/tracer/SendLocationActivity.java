package com.exalture.tracer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.exalture.tracer.adapter.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SendLocationActivity extends Activity {
    private dbContacts dbcontact;
    List<Pref_Number> result_in;
    ListView listview ;
    String[] names;
    String[] numbers;

    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_location);

        dbcontact = new dbContacts(getApplicationContext());
        listview = (ListView)findViewById(R.id.contactslist);
        listview.setClickable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = listview.getItemAtPosition(position);
                final String toWhome = numbers[position];
                AlertDialog.Builder builder = new AlertDialog.Builder(SendLocationActivity.this);
                builder.setTitle("Send Location").setMessage("Do you want to send your current location to "+names[position]+" ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                GPSTracker gps = new GPSTracker(SendLocationActivity.this);
                                String message = gps.message();
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(toWhome, null, message, null, null);
                                startActivity(new Intent(SendLocationActivity.this, MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)						//Do nothing on no
                        .show();
            }
        });

        result_in = new ArrayList<Pref_Number>();
        try {
            dbcontact.open();
            result_in = dbcontact.getContactLists();
            dbcontact.close();
            if (result_in.size() > 0) {
                int size = result_in.size();
                names = new String[size];
                numbers = new String[size];
                for (int i = 0; i < result_in.size(); i++) {
                    names[i] = result_in.get(i).getName();
                    numbers[i] = result_in.get(i).getNumber();
                }
                adapter = new ContactsAdapter(this, names, numbers,1 ,2);
                listview.setAdapter(adapter);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SendLocationActivity.this);
                builder
                        .setTitle("No Contacts added..")
                        .setMessage("Do you want to add contacts ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(SendLocationActivity.this, AddContactActivity.class));
                            }
                        })
                        .setNegativeButton("No", null)                        //Do nothing on no
                        .show();
            }
        } catch(NullPointerException e) { }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_list, menu);
        return true;
    }
}
