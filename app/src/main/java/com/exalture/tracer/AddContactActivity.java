package com.exalture.tracer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactActivity extends Activity {
	private dbContacts dbcontact;
	EditText num;
	EditText editName;
	private Button saveButton;

	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		dbcontact = new dbContacts(getApplicationContext());

		num = (EditText) findViewById(R.id.edit_text_phone_number);
		editName = (EditText) findViewById(R.id.edit_text_name);

		saveButton = (Button) findViewById(R.id.save_button);

		editName.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int DRAWABLE_LEFT = 0;
				final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				final int DRAWABLE_BOTTOM = 3;

				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (event.getRawX() >= (editName.getRight() - editName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

//                      show contacts
						Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
						startActivityForResult(contactPickerIntent, 1001);

						return true;
					}
				}
				return false;
			}
		});

		num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				addNumber();
				return true;
			}
		});


		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addNumber();
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ContentResolver contentResolver = getContentResolver();
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 1001:
					Cursor cursor = null;
					String phoneNumber = null;
					try {
						Uri result = data.getData();
						String id = result.getLastPathSegment();
						Cursor c = managedQuery(result, null, null, null, null);

						if (c.moveToFirst()) {
							String number = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

							Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{number}, null);
							while (phoneCursor.moveToNext()) {
								phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								name = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
							}
							num = (EditText) findViewById(R.id.edit_text_phone_number);
							num.setText(phoneNumber);

							editName.setText(name);
						}
					} catch (Exception e) {

					}
			}
		}
	}



	public void addNumber() {
		String number = null;

		int length = num.getText().length();
		if (length < 10) {
			num.setError("Phone number should be atleast 10 digit long.");
			num.setText("");
		} else {
			if (num.getText().toString().isEmpty()) {

				num.setError("Required");

			} else {
				number = num.getText().toString();
				number = number.replaceAll(" ", "");
				number = number.replaceAll("-", "");
				number = number.substring(number.length() - 10, number.length());
				if (number != null) {
					dbcontact.open();
					boolean exist = dbcontact.isExist(number);
					if (!exist) {
//						long insertid = dbcontact.addNumber(number, name);
						Toast.makeText(getApplicationContext(), "Contact added successfully", Toast.LENGTH_LONG).show();
						startActivity(new Intent(this, MainActivity.class));
					} else {
						num.setError("Number already exists!");
						num.setText("");

					}
					dbcontact.close();

				}
			}
		}
	}


}
