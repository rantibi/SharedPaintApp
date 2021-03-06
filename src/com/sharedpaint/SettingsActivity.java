package com.sharedpaint;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/*
 * Settings activity
 */
public class SettingsActivity extends PreferenceActivity {
	
	public static final String KEY_PREF_SERVER_ADDRESS = "server_address";
	public static final String KEY_PREF_SERVER_PORT = "server_port";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}

	public static class SettingsFragment extends PreferenceFragment implements
			OnSharedPreferenceChangeListener {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.pref_general);
			SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
			sp.registerOnSharedPreferenceChangeListener(this);
			Preference host = findPreference(KEY_PREF_SERVER_ADDRESS);
			host.setSummary(sp.getString(KEY_PREF_SERVER_ADDRESS, ""));
			Preference port = findPreference(KEY_PREF_SERVER_PORT);
			port.setSummary(sp.getString(KEY_PREF_SERVER_PORT, ""));
		}

		@Override
		public void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences()
					.unregisterOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			Preference pref = findPreference(key);
			if (pref instanceof EditTextPreference) {
				EditTextPreference etp = (EditTextPreference) pref;
				pref.setSummary(etp.getText());
			}
		}
	}
}