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
	
	int requiredBits();
	
	String fieldType();

	int order();
	
	boolean isGroup() default false;
}
