package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.SentenceField;

public class IntegerSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public boolean encode(StringBuilder builder, Object sentenceObject,
			SentenceField annotation, Field field) {

		Object obj;
		try {
			obj = field.get(sentenceObject);

			if (obj instanceof Integer) {
				if (annotation.width() == 0)
					builder.append(',').append(((Integer)obj).toString());
				else {
					String format = String.format("%%0%dd", annotation.width());
					builder.append(',').append(String.format(format, (Integer)obj));
				}
				return true;
			} else {
				if (annotation.isRequired()) {
					builder.append(",").append(annotation.defaultValue());
				} else {
					builder.append(",");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			return false;
		}
		
		return false;
	}

	@Override
	public String fieldCodecType() {
		return "integer";
	}

	@Override
	protected boolean doDecode(String[] segments, Object sentenceObject,
			Field field, int startIndex) {

		try {
			
			SentenceField annotation = (SentenceField)field.getAnnotation(SentenceField.class);
			if (segments[startIndex].isEmpty()) {
				field.set(sentenceObject, Integer.parseInt(annotation.defaultValue()));
			} else {
				field.set(sentenceObject, Integer.parseInt(segments[startIndex]));
			}
			return true;
		} catch (IllegalArgumentException
				| IllegalAccessException e) {
			
			return false;
		}
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

}
