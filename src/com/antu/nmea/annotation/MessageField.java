package com.antu.nmea.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for encapsulated NMEA sentence fields
 * @author yining
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MessageField {
	
	int startBit() default -1;
	
	/**
	 * How many bits this field requires
	 * @return
	 */
	int requiredBits();
	
	/**
	 * Type of the field, used to get MessageFieldCodec.
	 * @return
	 */
	String fieldType();

	int order();
	
	boolean isGroup() default false;
	
	String defaultValue() default "";
}
