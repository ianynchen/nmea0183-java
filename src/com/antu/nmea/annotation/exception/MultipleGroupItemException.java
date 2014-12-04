package com.antu.nmea.annotation.exception;

public class MultipleGroupItemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7879896680348549612L;

	public MultipleGroupItemException() {
		super();
	}

	public MultipleGroupItemException(String message) {
		super(message);
	}

	public MultipleGroupItemException(Throwable cause) {
		super(cause);
	}

	public MultipleGroupItemException(String message, Throwable cause) {
		super(message, cause);
	}

	public MultipleGroupItemException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
