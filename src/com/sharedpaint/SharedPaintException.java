package com.sharedpaint;

/*
 * General project's exception
 */
public class SharedPaintException extends Exception{
	public SharedPaintException() {
		super();
	}

	public SharedPaintException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SharedPaintException(String detailMessage) {
		super(detailMessage);
	}

	public SharedPaintException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
