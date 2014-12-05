package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

public interface ISentenceFieldCodec {

	boolean decode(String[] segments, Object obj, Field field, int startIndex);
	
	boolean encode(StringBuilder builder, Object obj, Field field);
	
	String fieldCodecType();
	
	int requiredSegments();
}
