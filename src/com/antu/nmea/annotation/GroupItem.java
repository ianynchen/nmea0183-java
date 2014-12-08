package com.antu.nmea.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation for items in a group in nmea sentence.
 * @author yining
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GroupItem {

	int order();
	
	String itemType();
	
	boolean isRequired() default false;
	
	String defaultValue() default "";
	
	int fieldWidth() default 1;
}
