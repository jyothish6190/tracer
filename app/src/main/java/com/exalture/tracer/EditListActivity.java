package com.exalture.tracer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.exalture.tracer.activity.AddSecurityNumberActivity;
import com.exalture.tracer.adapter.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

public class EditListActivity extends Activity {
    private dbContacts dbcontact;
    List<Pref_Number> result_in;
    List<Pref_Number> result_out;
    ListView listview;
    String[] names;
    String[] numbers;

    int primaryPosition;


    private Button addFloatButton;

    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        addFloatButton = (Button) findViewById(R.id.add_contact_float_button);

        addFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditListActivity.this, AddSecurityNumberActivity.class));
            }
        });

        dbcontact = new dbContacts(getApplicationContext());
        listview = (ListView) findViewById(R.id.contactslist);
        listview.setClickable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = numbers[position];
                final String new_number = o.toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(EditListActivity.this);
                builder.setTitle("Remove From List").setMessage("Are you sure to remove " + names[position] + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbcontact.open();
                                boolean rs = dbcontact.deleteNumber(new_number);
                                dbcontact.close();
                                if (rs == true) {
                                    Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(EditListActivity.this, EditListActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)                        //Do nothing on no
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
                    if(result_in.get(i).getPrimary() == 1){
                        primaryPosition = i;
                    }
                    names[i] = result_in.get(i).getName();
                    numbers[i] = result_in.get(i).getNumber();

                }
                adapter = new ContactsAdapter(this, names, numbers, primaryPosition, 1);
                listview.setAdapter(adapter);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditListActivity.this);
                builder
                        .setTitle("No Contacts added..")
                        .setMessage("Do you want to add contacts ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(EditListActivity.this, AddSecurityNumberActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(EditListActivity.this, MainActivity.class));
                            }
                        })                        //Do nothing on no
                        .show();
            }
        } catch (NullPointerException e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_list, menu);
        return true;
    }
}
