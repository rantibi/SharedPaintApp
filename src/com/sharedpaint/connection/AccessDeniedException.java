package com.sharedpaint.connection;

import com.sharedpaint.SharedPaintException;


public class AccessDeniedException extends SharedPaintException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedException() {
		super();
	}

	public AccessDeniedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AccessDeniedException(String detailMessage) {
		super(detailMessage);
	}

	public AccessDeniedException(Throwable throwable) {
		super(throwable);
	}

}
