package com.sharedpaint.connection;

import com.sharedpaint.SharedPaintException;

/**
 * Authentication Exception
 */
public class AuthenticationException extends SharedPaintException {

	private static final long serialVersionUID = 1L;
	
	public AuthenticationException() {
		super("email or password are incorrect");
	}
	
	public AuthenticationException(String message) {
		super(message);
	}

}
