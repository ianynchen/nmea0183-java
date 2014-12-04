package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;

public class ShortMessageFieldCodec implements IMessageFieldCodec {

	public ShortMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "short";
	}

	@Override
	public boolean decode(List<Byte> bits, int startIndex, 
			Object obj, Field field) {

		MessageField annotation = field.getAnnotation(MessageField.class);

		if (bits.size() < startIndex + annotation.requiredBits())
			return false;
		
		Short value = MessageFieldCodecHelper.parseShort(bits, startIndex, annotation.requiredBits(), false);
		
		if (value != null) {
			try {
				field.setShort(obj, value);
				return true;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
		}
		
		return false;
	}

	@Override
	public boolean encode(List<Byte> bits, Object obj, Field field,
			MessageField annotation) {
		
		try {
			if (!MessageFieldCodecHelper.shortToBits(bits, field.getByte(obj), annotation.requiredBits(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}
}
