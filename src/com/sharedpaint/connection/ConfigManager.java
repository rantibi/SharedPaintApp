package com.sharedpaint.connection;

import com.sharedpaint.SettingsActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Configuration manager for connection
 */
public class ConfigManager {
	private static ConfigManager instance = new ConfigManager();
	private Context context;
	
	private ConfigManager() {
		
	}
	
	public static ConfigManager getInstance() {
		return instance;
	}
	
	public void setApplicationContext(Context context) {
		this.context = context;
	}
	
	
	public String getHost() {		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_SERVER_ADDRESS, "");
	}
	
	public String getPort() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(SettingsActivity.KEY_PREF_SERVER_PORT, "");		
	}
	
	public String getValue(String key) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(key, null);
	}
	
	public void setValue(String key, String value) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		if (value != null) {
			editor.putString(key, value);
		} else {
			editor.remove(key);
		}
		editor.commit();
	}
}
