package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;

public class IntegerMessageFieldCodec implements IMessageFieldCodec {

	public IntegerMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "integer";
	}

	@Override
	public boolean decode(List<Byte> bits, int startIndex, Object obj,
			Field field) {
		
		MessageField annotation = field.getAnnotation(MessageField.class);
		Integer value = MessageFieldCodecHelper.parseInteger(bits, startIndex, annotation.requiredBits(), false);
		
		if (value == null)
			return false;
		
		try {
			field.set(obj, value);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	@Override
	public boolean encode(List<Byte> bits, Object obj, Field field,
			MessageField annotation) {
		
		try {
			if (!MessageFieldCodecHelper.integerToBits(bits, field.getByte(obj), annotation.requiredBits(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
