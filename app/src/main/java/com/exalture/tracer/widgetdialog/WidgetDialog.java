package com.exalture.tracer.widgetdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.exalture.tracer.GPSTracker;
import com.exalture.tracer.R;

public class WidgetDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Danger situation?").setMessage("Do you want to inform your location to your secure contact ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        SharedPreferences sp = WidgetDialog.this.getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                        Long myIntValue = sp.getLong("private_number", -1);
                        if(myIntValue != -1){
                            String secureContact=Long.toString(myIntValue);
                            GPSTracker gps=new GPSTracker(context);

                            String msg = gps.message();
                            msg = "I am in danger!" + msg;
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(secureContact,null, msg, null,null);
                            Toast.makeText(WidgetDialog.this, "Emergency message send to " + secureContact, Toast.LENGTH_LONG).show();

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            callIntent.setData(Uri.parse("tel:" + secureContact));
                            startActivity(callIntent);
                        }
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        finish();
                    }
                })
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_widget_dialog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
