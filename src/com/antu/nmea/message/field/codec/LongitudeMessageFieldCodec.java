package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class LongitudeMessageFieldCodec implements IMessageFieldCodec {

	public LongitudeMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "longitude";
	}

	@Override
	public boolean decode(List<Byte> bits, int startIndex, Object obj,
			Field field) {
		
		MessageField annotation = field.getAnnotation(MessageField.class);
		Integer val = MessageFieldCodecHelper.parseInteger(bits, startIndex, annotation.requiredBits(), true);
		
		if (val == null)
			return false;

		try {
			if (val == 0x6791AC0)
				field.set(obj, AbstractAisSentence.LONGITUDE_NOT_AVAILABLE);
			else
				field.set(obj, val / 600000.0);
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
			
			int b = 0x6791AC0;
			if (val != null && val >= -90 && val <= 90) {
				b = (int) (val * 60 * 10000);
			}
			if (!MessageFieldCodecHelper.integerToBits(bits, b, annotation.requiredBits(), true)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
