package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.SentenceField;

public interface ISentenceFieldCodec {

	boolean decode(String[] segments, Object sentenceObject, Field field, int startIndex);
	
	boolean encode(StringBuilder builder, Object sentenceObject, SentenceField annotation, Field field);
	
	String fieldCodecType();
	
	int requiredSegments();
}
