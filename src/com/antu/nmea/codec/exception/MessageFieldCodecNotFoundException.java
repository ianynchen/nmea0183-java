package com.antu.nmea.codec.exception;

public class MessageFieldCodecNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4973817439298395761L;

	public MessageFieldCodecNotFoundException() {
		super();
	}

	public MessageFieldCodecNotFoundException(String message) {
		super(message);
	}

	public MessageFieldCodecNotFoundException(Throwable cause) {
		super(cause);
	}

	public MessageFieldCodecNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageFieldCodecNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
