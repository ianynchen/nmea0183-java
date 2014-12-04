package com.antu.nmea.annotation;

import java.lang.reflect.Field;
import java.util.Comparator;

public class SentenceFieldAnnotationSorter implements Comparator<Field> {

	@Override
	public int compare(Field o1, Field o2) {
		
		SentenceField a = o1.getAnnotation(SentenceField.class);
		SentenceField b = o2.getAnnotation(SentenceField.class);
		
		return new Integer(a.order()).compareTo(new Integer(b.order()));
	}

}
