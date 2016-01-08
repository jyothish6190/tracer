package com.exalture.tracer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SetPreference {
	Context myContext;
	public SetPreference(Context ctx)
	{
		this.myContext = ctx;
	}
	public void setPreferenceNumber(String key, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);

		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(key, value);
		editor.commit();  // important!  Don't forget!

	}
    public void clearAllPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);

		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();  // important!  Don't forget!

	}

	public String getPreferenceNumber(String key) {
		String val = "";
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);
		val = prefs.getString(key, "");

		return val;
	}

	
}
