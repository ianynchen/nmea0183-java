package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;

public class ByteMessageFieldCodec implements IMessageFieldCodec {

	public ByteMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "byte";
	}

	@Override
	public boolean decode(List<Byte> bits, int startIndex, Object obj,
			Field field) {
		
		MessageField annotation = field.getAnnotation(MessageField.class);
		Byte b = MessageFieldCodecHelper.parseByte(bits, startIndex, annotation.requiredBits(), false);
		
		if (b == null)
			return false;
		
		try {
			field.set(obj, b);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	@Override
	public boolean encode(List<Byte> bits, Object obj, Field field,
			MessageField annotation) {
		
		try {
			if (!MessageFieldCodecHelper.byteToBits(bits, field.getByte(obj), annotation.requiredBits(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}
}
