package com.antu.nmea.sentence.exception;

public class SentenceCreationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8503051206403879754L;

	public SentenceCreationException() {
		super();
	}

	public SentenceCreationException(String message) {
		super(message);
	}

	public SentenceCreationException(Throwable cause) {
		super(cause);
	}

	public SentenceCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SentenceCreationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
