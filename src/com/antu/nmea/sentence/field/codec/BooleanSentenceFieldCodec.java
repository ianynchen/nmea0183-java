package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.SentenceField;

public class BooleanSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public boolean encode(StringBuilder builder, Object sentenceObject,
			SentenceField annotation, Field field) {

		try {
			Object value = field.get(sentenceObject);
			 
			if (value instanceof Boolean) {
				if ((Boolean)value) {
					builder.append(",A");
				} else {
					builder.append(",V");
				}
				return true;
			} else if (!annotation.defaultValue().isEmpty()) {
				builder.append(",").append(annotation.defaultValue());
				return true;
			}
			return false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	@Override
	public String fieldCodecType() {
		return "boolean";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	@Override
	protected boolean doDecode(String[] segments, Object sentenceObject,
			Field field, int startIndex) {
		
		SentenceField annotation = field.getAnnotation(SentenceField.class);
		
		if (segments[startIndex].equals("A") || segments[startIndex].equals("a")) {
			
			try {
				field.set(sentenceObject, true);
				return true;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
			
		} else if (segments[startIndex].equals("V") || segments[startIndex].equals("v")) {
			
			try {
				field.set(sentenceObject, false);
				return true;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
		} else if (segments[startIndex].isEmpty() && !annotation.isRequired()) {
			
			try {
				field.set(sentenceObject, Boolean.parseBoolean(annotation.defaultValue()));
				return true;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
		}
		return false;
	}

}
