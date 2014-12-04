package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.SentenceField;

public class DoubleSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public boolean encode(StringBuilder builder, Object sentenceObject,
			SentenceField annotation, Field field) {
		try {
			Object value = field.get(sentenceObject);
			 
			if (value instanceof Double) {
				String format = String.format("%%1$%d.%df", annotation.decimalPlaces() + 2, annotation.decimalPlaces());
				builder.append(",").append(String.format(format, (Double)value));
				return true;
			}
			return false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	@Override
	public String fieldCodecType() {
		return "double";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	@Override
	protected boolean doDecode(String[] segments, Object sentenceObject,
			Field field, int startIndex) {
		
		Double value = FieldCodecHelper.parseDouble(segments[startIndex], 0, 0);
		if (value != null) {
			
			try {
				field.set(sentenceObject, value);
				return true;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
			
		}
		return false;
	}

}
