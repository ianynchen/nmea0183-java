package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.SentenceField;

public class LatitudeSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public boolean encode(StringBuilder builder, Object sentenceObject,
			SentenceField annotation, Field field) {
		
		try {
			Object obj = field.get(sentenceObject);
			
			if (obj instanceof Double) {
				double total = (Double)obj;
				
				boolean isNegative = total < 0;
				
				if (isNegative) total = -total;
				int degree = (int)(total / 100);
				
				double minute = total - degree * 100;
				
				String result = String.format("%02d%5.2f", degree, minute);
				if (isNegative) result += ",S";
				else result += ",N";
				
				builder.append(result);
				return true;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
		return false;
	}

	@Override
	public String fieldCodecType() {
		return "latitude";
	}

	@Override
	protected boolean doDecode(String[] segments, Object sentenceObject,
			Field field, int startIndex) {
		
		Integer degree = FieldCodecHelper.parseInt(segments[startIndex], 0, 2);
		
		if (degree != null) {
			
			Double minute = FieldCodecHelper.parseDouble(segments[startIndex], 2, 0);
			double total = degree + minute;
			
			if (segments[startIndex + 1].equals("S") || segments[startIndex + 1].equals("s")) {
				total = -total;
			} else if (!segments[startIndex + 1].equals("N") && !segments[startIndex + 1].equals("n")) {
				return false;
			}
			
			try {
				field.set(sentenceObject, new Double(total));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
		}
		
		return false;
	}

	@Override
	public int requiredSegments() {
		return 2;
	}

}
