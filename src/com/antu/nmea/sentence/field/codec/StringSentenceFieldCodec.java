package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.SentenceField;

public class StringSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public boolean encode(StringBuilder builder, Object sentenceObject, 
			SentenceField annotation, Field field) {

		try {
			Object obj = field.get(sentenceObject);
			builder.append(',').append(obj.toString());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			return false;
		}

		return true;
	}

	@Override
	public String fieldCodecType() {
		return "string";
	}

	@Override
	protected boolean doDecode(String[] segments, Object sentenceObject, Field field,
			int startIndex) {

		try {
			field.set(sentenceObject, segments[startIndex]);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
		return true;
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

}
