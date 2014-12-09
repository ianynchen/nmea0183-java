package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;

public class IntegerMessageFieldCodec extends AbstractMessageFieldCodec {

	public IntegerMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "integer";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		if (bits.size() - startIndex < setting.getFieldWidth())
			return null;

		Integer value = MessageFieldCodecHelper.parseInteger(bits, startIndex, setting.getFieldWidth(), false);
		
		if (value == null)
			return null;
		
		try {
			field.set(obj, value);
			return setting.getFieldWidth();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			if (!MessageFieldCodecHelper.integerToBits(bits, field.getInt(obj), setting.getFieldWidth(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
