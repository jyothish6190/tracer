package com.exalture.tracer.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exalture.tracer.MainActivity;
import com.exalture.tracer.R;
import com.exalture.tracer.SetPreference;
import com.exalture.tracer.Utility.AlertUtility;
import com.exalture.tracer.dbContacts;


public class AddSecurityNumberActivity extends Activity implements OnClickListener, EditText.OnEditorActionListener, View.OnTouchListener{
    private static final String TAG = "AddSecurityNumberActivity";

    // UI references.
    private EditText editName;
    private EditText editNumber;
    private Button saveButton;
    private TextView helpView;
    private CheckBox addToPrimary;

    public static String pvt_number;
    private String name = null;
    private String phoneNumber = null;

    private boolean checked = false;
    private int primary = 0;

    private dbContacts dbcontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_security_number);

        editName = (EditText) findViewById(R.id.edit_text_name);
        editNumber = (EditText) findViewById(R.id.edit_text_phone_number);
        saveButton = (Button) findViewById(R.id.save_button);
        helpView = (TextView) findViewById(R.id.help_view);
        addToPrimary = (CheckBox) findViewById(R.id.check_primary);

        editName.setOnTouchListener(this);
        helpView.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        addToPrimary.setOnClickListener(this);
        editNumber.setOnEditorActionListener(this);

        dbcontact = new dbContacts(this);
    }

    private void saveSecurityNumber() {
        if (editNumber.getText().toString().isEmpty()) {
            editNumber.setError("Required");
        } else if (editName.getText().toString().isEmpty()) {
            editName.setError("Required");
        } else {
            SetPreference pref = new SetPreference(getApplicationContext());
            pref.setPreferenceNumber("pvt_number", editNumber.getText().toString());
            pref.setPreferenceNumber("my_name", editName.getText().toString());

            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String phoneNumber = tm.getLine1Number();
            String nums = phoneNumber;
            pref.setPreferenceNumber("mobile_number", phoneNumber);
            //Edited Kar
            pvt_number = pref.getPreferenceNumber("pvt_number");
            String s = pvt_number;
            Long private_num = Long.parseLong(s);

            SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("private_number", private_num);
            editor.commit();

            Toast.makeText(getApplicationContext(), "Contact added successfuly", Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    public void saveContact() {
        String number = editNumber.getText().toString();
        int length = number.length();
        if (length < 10) {
            editNumber.setError("Phone number should be atleast 10 digit long.");
            editNumber.setText("");
        } else {
            if (number.isEmpty()) {

                editNumber.setError("Required");

            } else {
                number = number.replaceAll(" ", "");
                number = number.replaceAll("-", "");
                number = number.substring(number.length() - 10, number.length());
                if (number != null) {
                    dbcontact.open();
                    boolean exist = dbcontact.isExist(number);
                    if (!exist) {
                        long insertid = dbcontact.addNumber(number, name, primary);
                        Toast.makeText(getApplicationContext(), "Contact added successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        editNumber.setError("Number already exists!");
                        editNumber.setText("");
                    }
                    dbcontact.close();

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver contentResolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:
                    Cursor cursor = null;

                    try {
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();
                        Cursor c = managedQuery(result, null, null, null, null);
                        if (c.moveToFirst()) {
                            String number = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                            Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{number}, null);
                            while (phoneCursor.moveToNext()) {
                                name = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                phoneNumber = phoneNumber.replaceAll("[()\\s-]+", "");
                            }

                            editName.setText(name);
                            editNumber.setText(phoneNumber);
                        }
                    } catch (Exception e) {
                    }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.save_button:
                if (checked) {
                    saveSecurityNumber();
                    primary = 1;
                }
                saveContact();
                break;

            case R.id.help_view:
                 AlertUtility.getInstance().showMessage(AddSecurityNumberActivity.this, "hello", "hello");
                break;

            case R.id.check_primary:
                if (((CheckBox) v).isChecked()) {
                    checked = true;
                }
                break;

            default:break;
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        saveContact();
        return  true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (editName.getRight() - editName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 1001);

                return true;
            }
        }
        return false;
    }
}

