package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.SentenceField;

public class CharSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public boolean encode(StringBuilder builder, Object sentenceObject,
			SentenceField annotation, Field field) {

		try {
			Object value = field.get(sentenceObject);
			if (value != null && value instanceof Character) {
				builder.append(',').append(value);
				return true;
			}
			return false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			return false;
		}
	}

	@Override
	public String fieldCodecType() {
		return "char";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	@Override
	protected boolean doDecode(String[] segments, Object sentenceObject,
			Field field, int startIndex) {

		if (segments[startIndex].length() != 1)
			return false;
		
		try {
			field.set(sentenceObject, segments[startIndex].charAt(0));
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
