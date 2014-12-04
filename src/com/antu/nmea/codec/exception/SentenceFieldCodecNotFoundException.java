package com.antu.nmea.codec.exception;

public class SentenceFieldCodecNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3781512415506323300L;

	public SentenceFieldCodecNotFoundException() {
		super();
	}

	public SentenceFieldCodecNotFoundException(String message) {
		super(message);
	}

	public SentenceFieldCodecNotFoundException(Throwable cause) {
		super(cause);
	}

	public SentenceFieldCodecNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SentenceFieldCodecNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
