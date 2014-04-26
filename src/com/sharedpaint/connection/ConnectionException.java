package com.sharedpaint.connection;

import com.sharedpaint.SharedPaintException;

/**
 * Connection Exception
 */
public class ConnectionException extends SharedPaintException {

	private static final long serialVersionUID = 1L;

	public ConnectionException() {
		super();
	}

	public ConnectionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ConnectionException(String detailMessage) {
		super(detailMessage);
	}

	public ConnectionException(Throwable throwable) {
		super(throwable);
	}

}
