package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;

public class ShortMessageFieldCodec extends AbstractMessageFieldCodec {

	public ShortMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "short";
	}

	@Override
	protected boolean doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {

		if (bits.size() < startIndex + setting.getFieldWidth())
			return false;
		
		Short value = MessageFieldCodecHelper.parseShort(bits, startIndex, setting.getFieldWidth(), false);
		
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
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			if (!MessageFieldCodecHelper.shortToBits(bits, field.getShort(obj), setting.getFieldWidth(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}
}
