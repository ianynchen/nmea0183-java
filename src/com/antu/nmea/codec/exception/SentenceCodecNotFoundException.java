package com.antu.nmea.codec.exception;

public class SentenceCodecNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8679760373380789633L;

	public SentenceCodecNotFoundException() {
		super();
	}

	public SentenceCodecNotFoundException(String message) {
		super(message);
	}

	public SentenceCodecNotFoundException(Throwable cause) {
		super(cause);
	}

	public SentenceCodecNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SentenceCodecNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
