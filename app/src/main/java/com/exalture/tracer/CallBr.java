package com.exalture.tracer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class CallBr extends BroadcastReceiver {
	Context mContext = null;
	private dbContacts dbcontact;
	String city;
	private static final String ACTION_REBOOT = "android.intent.action.BOOT_COMPLETED";
    private  static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	SetPreference pref;
	List<Pref_Number> call_list;
	String link;
	GPSTracker gps;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		dbcontact = new dbContacts(context);
		dbcontact.open();
		call_list = new ArrayList<Pref_Number>();

		try {
			call_list = dbcontact.getContactLists();
			Log.e("call_list", ""
					+ call_list.size() );
		} catch (NullPointerException e) { }

		dbcontact.close();
        dbcontact.close();
        pref = new SetPreference(context);
        gps = new GPSTracker(context);
        city = gps.message();

        // *****DETECT INCOMING MESSAGE ****

        if(intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();        //---get the SMS message passed in---
            if (bundle != null && call_list.size() > 0) try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] recievedSms = new SmsMessage[pdus.length];
                recievedSms[0] = SmsMessage.createFromPdu((byte[]) pdus[0]);
                String sender = recievedSms[0].getOriginatingAddress();
                sender = sender.substring(sender.length() - 10);
                String msgBody = recievedSms[0].getMessageBody();

                if (msgBody.equals("Location") || msgBody.equals("location")) {
                    for (int j = 0; j < call_list.size(); j++) {
                        String curr = call_list.get(j).getNumber();
                        if (sender.equals(curr) && city != null) {
                            sendSMS(sender, city);
                            pref.setPreferenceNumber("curr_num", sender);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("Exception caught", e.getMessage());
            }
        }

		// ******ACTION REBOOT*****

		if (intent.getAction().equals(ACTION_REBOOT)) {
			try {
				SetPreference setPreference = new SetPreference(context);
                TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
				try {
                    String currPhoneNumber = tm.getLine1Number();
                    String oldPhoneNumber = setPreference.getPreferenceNumber("mobile_number");
                    String serialNumber = tm.getSimSerialNumber();
                    String pvt_number = setPreference.getPreferenceNumber("pvt_number");
                    if (pvt_number != null && oldPhoneNumber != null && !currPhoneNumber.equals(oldPhoneNumber)) {
                        String owner_name = setPreference.getPreferenceNumber("my_name");
                        link = gps.getLink();
                        String messageToSend =
                                        ""+owner_name+"'s mobile having serial number "+serialNumber+" is currently located in "+city+" view in map :"+link;
                        sendSMS(pvt_number, messageToSend);
                    }
				} catch (NullPointerException e) { }
			} catch (Exception e) { }
		}
	}

    public void sendSMS(String toWhom, String messageToSend) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(toWhom, null,
                    messageToSend, null, null);
        } catch (Exception e) {
        }
    }
}
