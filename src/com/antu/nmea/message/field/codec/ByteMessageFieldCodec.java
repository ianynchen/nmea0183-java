package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;

public class ByteMessageFieldCodec extends AbstractMessageFieldCodec {

	public ByteMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "byte";
	}

	@Override
	protected boolean doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		Byte b = MessageFieldCodecHelper.parseByte(bits, startIndex, setting.getFieldWidth(), false);
		
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
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			if (!MessageFieldCodecHelper.byteToBits(bits, field.getByte(obj), setting.getFieldWidth(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}
}
