package com.antu.nmea.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A SentenceMessage annotation is to be used by encapsulated messages.
 * 
 * As some messages can contain fields with undetermined length, this is required
 * during encoding/decoding. This is not needed for all encapsulated messages
 * @author yining
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SentenceMessage {

	/**
	 * specifies whether encoding need to pad bits at the end to make total
	 * number of bits multiples of 6
	 * @return
	 */
	boolean alignBytes() default false;
}
