package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

public abstract class AbstractSentenceFieldCodec implements ISentenceFieldCodec {
	
	@Override
	public boolean decode(String[] segments, Object sentenceObject, Field field, int startIndex) {
				
		if (segments.length < startIndex + this.requiredSegments())
			return false;
		
		return this.doDecode(segments, sentenceObject, field, startIndex);
	}

	abstract protected boolean doDecode(String[] segments, Object sentenceObject, Field field,
			int startIndex);
}
