package com.sharedpaint.connection;

public class ServerProxy {
	private static ServerInterface instance = new RestServer();

	public static ServerInterface getInstance() {
		return instance;
	}	
}
