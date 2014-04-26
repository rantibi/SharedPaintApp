package com.sharedpaint;

import com.sharedpaint.connection.ConfigManager;

/*
 * Main Application 
 */
public class Application extends android.app.Application {
	@Override
	public void onCreate() {		
		super.onCreate();
		ConfigManager.getInstance().setApplicationContext(getApplicationContext());
	}
}
