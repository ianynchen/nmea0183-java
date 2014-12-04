package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class CourseMessageFieldCodec implements IMessageFieldCodec {

	public CourseMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "course";
	}

	@Override
	public boolean decode(List<Byte> bits, int startIndex, Object obj,
			Field field) {
		
		MessageField annotation = field.getAnnotation(MessageField.class);
		Short b = MessageFieldCodecHelper.parseShort(bits, startIndex, annotation.requiredBits(), false);
		
		if (b == null || b < 0 || b > 3600)
			return false;

		try {
			if (b == 3600)
				field.set(obj, AbstractAisSentence.COURSE_NOT_AVAILABLE);
			else
				field.set(obj, b / 10.0);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	@Override
	public boolean encode(List<Byte> bits, Object obj, Field field,
			MessageField annotation) {
		
		try {
			Double val = field.getDouble(obj);
			
			short b = 3600;
			if (val != null && val >= 0 && val < 360) {
				b = (short) (val * 10);
			}
			if (!MessageFieldCodecHelper.shortToBits(bits, b, annotation.requiredBits(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
