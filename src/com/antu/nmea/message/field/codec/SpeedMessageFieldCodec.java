package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class SpeedMessageFieldCodec implements IMessageFieldCodec {

	public SpeedMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "speed";
	}

	@Override
	public boolean decode(List<Byte> bits, int startIndex, Object obj,
			Field field) {
		
		MessageField annotation = field.getAnnotation(MessageField.class);
		Short b = MessageFieldCodecHelper.parseShort(bits, startIndex, annotation.requiredBits(), false);
		
		if (b == null || b < 0)
			return false;

		try {
			if (b == 1023)
				field.set(obj, AbstractAisSentence.SPEED_NOT_AVAILABLE);
			else if (b == 1022)
				field.set(obj, AbstractAisSentence.SPEED_EXCEED_LIMIT);
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
			
			short b = 1023;
			if (val != null && val >= 0) {
				if (val == AbstractAisSentence.SPEED_EXCEED_LIMIT)
					b = 1022;
				else {
					b = (short) (val * 10);
				}
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
