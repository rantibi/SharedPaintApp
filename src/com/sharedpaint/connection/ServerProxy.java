package com.sharedpaint.connection;

/**
 * Server connection factory
 */
public class ServerProxy {
	private static ServerInterface instance = new RestServer();

	public static ServerInterface getInstance() {
		return instance;
	}	
}
